'use strict'

require('colors');
const config        = require('config'),
      express       = require('express'),
      helmet        = require('helmet'),
      bodyParser    = require('body-parser'),
      http          = require('http'),
      db_config     = config.get('Customer.dababase.db_config'),
      logs_config   = config.get('Customer.server.logs');

var   io            = require('socket.io');

const app_api       = express(),
      app_socket    = express(),
      socket_config = config.get('Customer.server.web_socket'),
      api_config    = config.get('Customer.server.api');

// Database
const database      = require('./database/database');

// Controllers
const accounts_ctrl = require('./controllers/accounts_controller');
const contents_ctrl = require('./controllers/contents_controller');
const votes_ctrl    = require('./controllers/votes_controller');

// Init IO Socket
var serverIO = http.createServer(app_socket);
io = io.listen(serverIO);

// Middlewares
app_api.use(helmet());
app_api.use(api_config.route, (req, res, next) => {
  res.header('Access-Control-Allow-Origin', '*');
  res.header('Access-Control-Allow-Headers', 'Origin, X-Requested-With, Content-type, Accept');
  next();
});
app_api.use(bodyParser.json());
app_api.use(bodyParser.urlencoded({extended:true}));

if(logs_config.enabled) {
  app_api.use(api_config.route, (req, res, next) => {
    console.log((req.method+' '+req.originalUrl).yellow);
    console.log("req.params : ".bold+JSON.stringify(req.params));
    console.log("req.query : ".bold+JSON.stringify(req.query));
    console.log("req.body : ".bold+JSON.stringify(req.body));
    next();
  })
}

// Sockets
io.sockets.on('connection', votes_ctrl.initSocket);

// Routes
app_api.get(api_config.route+'/account/:id', accounts_ctrl.findAccountByID);
app_api.get(api_config.route+'/find_my_account', accounts_ctrl.getAccountFromLogin);
app_api.get(api_config.route+'/accountNbContents/:id', accounts_ctrl.getNbContentsToMake)
app_api.get(api_config.route+'/account/:id/contents_to_vote', contents_ctrl.getContentsToVote);

app_api.post(api_config.route+'/account', accounts_ctrl.addAccountIfNotExist);
app_api.post(api_config.route+'/content', contents_ctrl.createContent);

app_api.put(api_config.route+'/account/:id', accounts_ctrl.updateAccountByID);


// Database connection & server launch
database(() => {
  console.log('✓ Serveur ENV='.green+process.env.NODE_ENV.yellow);
  console.log('✓ Connexion établie avec la base MongoDB'.green+ (' ('+db_config.host+':'+db_config.db_name+')').yellow);
  serverIO.listen(socket_config.port, socket_config.host, () => console.log("✓ Server SOCKET started on ".green + (socket_config.host+":"+socket_config.port).cyan));
  app_api.listen(api_config.port, api_config.host, () => console.log("✓ Server API started on ".green + (api_config.host+":"+api_config.port).cyan));
});


'use strict'

require('colors');
const express       = require('express'),
      helmet        = require('helmet'),
      bodyParser    = require('body-parser');
      http          = require('http');

var   io            = require('socket.io');

const app_api       = express(),
      app_socket    = express(),
      socketConfig  = config.get('Customer.server.webSocket'),
      apiConfig     = config.get('Customer.server.api'),

// Models
const database      = require('./models/database');

// Controllers
const accountsCtrl  = require('./controllers/accounts');

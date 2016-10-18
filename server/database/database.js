'use strict'

const config        = require('config'),
      mongoose      = require('mongoose');

var db_config       = config.get('Customer.dababase.db_config');

mongoose.connect(db_config.type+'://'+db_config.host+'/'+db_config.db_name);

var db = mongoose.connection;

db.on('error', () => console.error('✗ Impossible de se connecter à la base MongoDB '.red + (db_config.host+":"+db_config.dbName).cyan));

module.exports = (onceReady) => {
  if (onceReady) {
    db.on('open', onceReady);
  }
};
// models/Account.js

'use strict';

const mongoose = require('mongoose');

mongoose.Promise = global.Promise;

var accountSchema = mongoose.Schema({
  'pseudo'        : {type:String},
  'fb_id'         : {type:String, index:true},
  'google_id'     : {type:String, index:true},
  'reputation'    : {type:Number, default:0},
  'votesSpent'    : {type:Number, default:0}
}, {versionKey: false});

module.exports = mongoose.model('Account', accountSchema);
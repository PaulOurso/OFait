// models/Vote.js

'use strict';

const mongoose = require('mongoose');

mongoose.Promise = global.Promise;

var voteSchema = mongoose.Schema({
  'account_id'    : {type:String, required:true, index:true},
  'content_id'    : {type:String, required:true, index:true},
  'value'         : {type:Number},
  'created_date'  : {type:Date, default: Date.now}
}, {versionKey: false});

module.exports = mongoose.model('Vote', voteSchema);
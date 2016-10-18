// models/Content.js

'use strict';

const mongoose = require('mongoose');

mongoose.Promise = global.Promise;

var contentSchema = mongoose.Schema({
  'account_id'    : {type:String, required:true, index:true},
  'content_value' : {type:String, required:true},
  'created_date'  : {type:Date, default: Date.now}
}, {versionKey: false});

module.exports = mongoose.model('Content', contentSchema);
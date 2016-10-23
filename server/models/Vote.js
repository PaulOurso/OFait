// models/Vote.js

'use strict';

const mongoose = require('mongoose'),
        Schema = mongoose.Schema;

mongoose.Promise = global.Promise;

var voteSchema = mongoose.Schema({
  'account'       : {type: Schema.Types.ObjectId, ref: 'Account', required:true, index:true},
  'content'       : {type: Schema.Types.ObjectId, ref: 'Content', required:true, index:true},
  'value'         : {type:Number},
  'created_date'  : {type:Date, default: Date.now}
}, {versionKey: false});

module.exports = mongoose.model('Vote', voteSchema);
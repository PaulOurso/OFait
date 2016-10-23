// models/Content.js

'use strict';

const mongoose = require('mongoose'),
        Schema = mongoose.Schema;

mongoose.Promise = global.Promise;

var contentSchema = mongoose.Schema({
  'created_by'    : { type: Schema.Types.ObjectId, ref: 'Account', index:true, required:true },
  'content_value' : {type:String, required:true},
  'created_date'  : {type:Date, default: Date.now},
  'votes'         : [{type: Schema.Types.ObjectId, ref: 'Vote'}]
}, {versionKey: false});

module.exports = mongoose.model('Content', contentSchema);
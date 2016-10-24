// models/Account.js

'use strict';

const mongoose = require('mongoose'),
        Schema = mongoose.Schema;

mongoose.Promise = global.Promise;

var accountSchema = mongoose.Schema({
  'pseudo'        : {type:String},
  'fb_id'         : {type:String, index:true},
  'google_id'     : {type:String, index:true},
  'reputation'    : {type:Number, default:0},
  'notif'         : {type:Boolean, default:false},
  'votes_spent'    : {type:Number, default:0},
  'votes'         : [{type: Schema.Types.ObjectId, ref: 'Vote'}]
}, {versionKey: false});

module.exports = mongoose.model('Account', accountSchema);
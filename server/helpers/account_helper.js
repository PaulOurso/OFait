'use strict'
const 	Vote = require('../models/Vote');

exports.getVotesUnused = function getVotesUnused(account){
	Vote.find({account_id:account._id}).count().lean().exec()
		.then(function(nbVotes){
			console.log(nbVotes);
			return nbVotes;
		})
}
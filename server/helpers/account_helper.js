'use strict'
const 	Vote = require('../models/Vote'),
		config = require('config'),
		list_config = config.get('Customer.levels');

exports.getVotesUnused = function getVotesUnused(account){
	Vote.find({account_id:account._id}).count().lean().exec()
		.then(function(nbVotes){
			console.log(nbVotes);

			return nbVotes - account.votesSpent;
		})
}

exports.getNbVoteToMakeContent = function getNbVoteToMakeContent(account){

	var nbVotes = list_config.default;

	for(var i = 0; i<list_config.list.length; i++){
		if(account.reputation < list_config.list[i].reputation){
			nbVotes = list_config.list.cost_vote;
		}
	}
	return nbVotes;
}
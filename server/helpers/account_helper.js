'use strict'
const 	Vote = require('../models/Vote'),
		config = require('config'),
		list_config = config.get('Customer.levels');

exports.getVotesConstants = function getVotesConstants(account){

	var constants = {reputation: -1,
					cost_vote: list_config.default};

	for(var i = 0; i<list_config.list.length; i++){
		if(account.reputation < list_config.list[i].reputation){
			constants = list_config.list[i];
      break;
		}
	}
	return constants;
}

exports.getPreviousLvlReputation= function getPreviousLvlReputation(ObjectList){
	if(ObjectList.reputation == -1){
		return list_config.list[list_config.list.length -1].reputation;
	}

	var index = list_config.list.indexOf(ObjectList);

	if(index == 0){
		return 0;
	}
	else{
		return list_config.list[index-1].reputation;
	}
} 
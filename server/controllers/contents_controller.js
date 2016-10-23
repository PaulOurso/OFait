'use strict'

const Account 		 = require('../models/Account'),
	  Content        = require('../models/Content'),
      response       = require('../helpers/answer_helper'),
      accountHelper  = require('../helpers/account_helper');

exports.createContent = function createContent(req,res){
	var account_id = req.body.account_id;
	var content_value = req.body.content_value;

	if(account_id && content_value){

		Account.findById(account_id).lean().exec()
  		.then(function(account){
	        if (account === null){
	          return response.formatErr(res, 404, {message:'Compte inexistant.'});
	        }

	        var nbVotesUnused = accountHelper.getVotesUnused(account);
	        var nbVotesToUse = accountHelper.getNbVoteToMakeContent(account);

	        if(nbVotesUnused >= nbVotesToUse){

		        var newContent = new Content(req.body);

				newContent.save()
				.then(function(content){
					account.votesSpent += nbVotesToUse;
	        		account.save();
					response.formatAnswerObject(res, 201, {message:null},content);
				}).catch(function(err){
					response.formatErr(res, 500, err);
				});	
	        }
	        else{
	        	return response.formatErr(res, 403, {message: "Vous n'avez pas suffisament de votes pour créer un contenu"});
	        }
	    	
		})
  		.catch((err) => response.formatErr(res, 500, err));
	}
	else{
		response.formatErr(res, 400, {message: 'Paramètres manquants.'});
	}
}
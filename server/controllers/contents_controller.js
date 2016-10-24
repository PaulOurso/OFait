'use strict'

const Account 		 = require('../models/Account'),
	  Content        = require('../models/Content'),
	  Vote 					 = require('../models/Vote'),
    response       = require('../helpers/answer_helper'),
    accountHelper  = require('../helpers/account_helper');

exports.createContent = function createContent(req,res){
	var created_by = req.body.created_by;
	var content_value = req.body.content_value;
  var select = '_id created_by content_value created_date notif';
	if(created_by && content_value){

		Account.findById(created_by).exec()
  		.then(function(account){
        if (account === null){
          return response.formatErr(res, 404, {message:'Compte inexistant.'});
        }
        
        var nbVotesUnused = account.votes.length - account.votes_spent;
        //var nbVotesUnused = 200;
        var voteConstants = accountHelper.getVotesConstants(account);
        var nbVotesToUse = voteConstants.cost_vote;

		if(nbVotesUnused >= nbVotesToUse){
			var newContent = new Content(req.body);
			newContent.save()
				.then(function(content){
					account.votes_spent += nbVotesToUse;
		    		account.save();
		    		Content.findById(content._id, select).populate('created_by', 'pseudo google_id fb_id votes_spent reputation notif').lean().exec()
						.then((content) => {
							response.formatAnswerObject(res, 201, {message:null}, content);
						})
						.catch(function(err){
							response.formatErr(res, 500, err);
						});
				})
				.catch(function(err){
					response.formatErr(res, 500, err);
				});
		}
        else{
        	response.formatErr(res, 403, {message: "Vous n'avez pas suffisament de votes pour créer un contenu"});
        }
      })
  		.catch((err) => response.formatErr(res, 500, err));
  }
  else{
		response.formatErr(res, 400, {message: 'Paramètres manquants.'});
	}
}

exports.getContentsToVote = function getContentsToVote(req,res) {
	var account_id = req.params.id;
  	var select = '_id created_by content_value created_date votes';
	if (account_id && account_id.match(/^[0-9a-fA-F]{24}$/)) {
		Vote.find({account: account_id}).lean().exec()
			.then((myVotes) => {
				var myVotesContentsID = myVotes.map((elem) => elem.content);
				Content.find({ $and: [ {created_by: {$ne: account_id}}, {_id: {$nin: myVotesContentsID}} ] }, select).populate('created_by votes').lean().exec()
					.then((contents) => {
						contents = contents.map((c) => {
							return {
								_id 					: c._id,
								created_by		: { pseudo: c.created_by.pseudo },
								content_value	: c.content_value,
								created_date  : c.created_date,
								nb_votes			: c.votes.length,
								nb_points 		: c.votes.reduce((total, curVote) => { return total + curVote.value }, 0)
							};
						});

						// Sort nb_points DESC
						contents = contents.sort((first, second) => { return second.nb_points - first.nb_points});
						// limite answer 20
						contents = contents.splice(0, 20);
						response.formatAnswerArray(res, 200, {message:null}, contents);
					})
					.catch((err) => {
						response.formatErr(res, 500, err);
					});
			})
			.catch((err) => {
				response.formatErr(res, 500, err);
			});
	}
	else {
		response.formatErr(res, 400, {message: 'Paramètres manquants.'});
	}
}

exports.getFavoriteContents = function getFavoriteContents(req,res){
	var account_id = req.params.id;
	
}
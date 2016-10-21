'use strict'

const Content        = require('../models/Content'),
      response       = require('../helpers/answer_helper');

exports.createContent = function createContent(req,res){
	var account_id = req.body.account_id;
	var content_value = req.body.content_value;

	if(account_id && content_value){

		Account.findById(account_id).lean().exec()
      		.then((account) => {
		        if (account === null){
		          return response.formatErr(res, 404, {message:'Compte inexistant.'});
		        }

		        var voteLeft = 
		    	var newContent = new Content(req.body);

				newContent.save()
					.then(function(content){
						response.formatAnswerObject(res, 201, {message:null},newContent);
					}).catch(function(err){
						response.formatErr(res, 500, err);
					});
				})
      		.catch((err) => response.formatErr(res, 500, err));

		
	}
	else{
		response.formatErr(res, 400, {message: 'Paramètres manquants.'});
	}
}
'use strict'

const Content        = require('../models/Content'),
      response       = require('../helpers/answer_helper');

exports.createContent = function createContent(req,res){
	var account_id = req.body.account_id;
	var content_value = req.body.content_value;

	if(account_id && content_value){

		newContent = new Content(req.body);

		Content.save(newContent).lean().exec()
			.then(function(content){
				response.formatAnswerObjects(res, 201, {message:null},account);
			}).catch(function(err){
				response.formatErr(res, 500, err)
			});
	}
	else{
		response.formatErr(res, 400, {message: 'Paramètres manquants.'});
	}
}
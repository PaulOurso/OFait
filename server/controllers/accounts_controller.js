'use strict'

const Account        = require('../models/Account'),
      accountHelper  = require('../helpers/account_helper'),
      response       = require('../helpers/answer_helper');

exports.findAccountByID = function findAccountByID(req, res) {
  var id = req.params.id;
  var select = '_id pseudo votesSpent reputation fb_id google_id notif';
  if (id && id.match(/^[0-9a-fA-F]{24}$/)) {
    Account.findById(req.params.id, select).lean().exec()
      .then((account) => {
        if (account === null)
          return response.formatErr(res, 404, {message:'Compte inexistant.'});
        response.formatAnswerObject(res, 200, {message:null}, account);
      })
      .catch((err) => response.formatErr(res, 500, err));
  }
  else {
    response.formatErr(res, 400, {message: 'Erreur dans la requête.'});
  }
}

exports.getAccountFromLogin = function getAccountFromLogin(req, res) {
  var fb_id = req.query.fb_id;
  var google_id = req.query.google_id;
  var select = '_id pseudo votesSpent reputation fb_id google_id notif';
  if (fb_id || google_id) {
    var param = fb_id ? {fb_id: fb_id} : {google_id: google_id};
    Account.findOne(param, select).lean().exec()
      .then((account) => {
        if (account === null)
          return response.formatErr(res, 404, {message:'Compte inexistant.'});
        response.formatAnswerObject(res, 200, {message:null}, account);
      })
      .catch((err) => response.formatErr(res, 500, err));
  }
  else {
    response.formatErr(res, 400, {message: 'Paramètres manquants.'});
  }
}

exports.addAccountIfNotExist = function addAccountIfNotExist(req, res) {
  var fb_id = req.body.fb_id;
  var google_id = req.body.google_id;
  var select = '_id pseudo votesSpent reputation fb_id google_id notif';
  if (fb_id || google_id) {
    var param = fb_id ? {fb_id: fb_id} : {google_id: google_id};
    Account.findOne(param, select).lean().exec()
      .then((account) => {
        if (account && account._id)
          return response.formatAnswerObject(res, 200, {message:null}, account);
        return new Account(req.body);
      })
      .then((newAccount) => newAccount.save())
      .then((account) => response.formatAnswerObject(res, 201, {message:null}, account))
      .catch((err) => response.formatErr(res, 500, err));
  }
  else {
    response.formatErr(res, 400, {message: 'Paramètres manquants.'});
  }
}

exports.updateAccountByID = function updateAccountByID(req, res) {
  var id = req.params.id;
  var select = '_id pseudo votesSpent reputation fb_id google_id notif'
  if (id && id.match(/^[0-9a-fA-F]{24}$/)) {
    Account.findById(id, select).exec()
      .then((account) => {
        if (!account)
          return Promise.reject('Compte non trouvé.');
        account.pseudo = req.body.pseudo || account.pseudo;
        account.notif  = req.body.notif || account.notif;
        return account;
      })
      .then((account) => account.save())
      .then((account) => response.formatAnswerObject(res, 201, {message:null}, account))
      .catch((err) => response.formatErr(res, 500, err));
  }
  else {
    response.formatErr(res, 400, {message: 'Paramètres manquants.'});
  }
}

exports.getStatsAccountByID = function getStatsAccountByID(req, res){

  var id = req.params.id;

  Account.findById(id).lean().exec()
      .then((account) => {

        var nbVotesUnused = account.votes.length - account.votesSpent;
        var votesByContent = accountHelper.getNbVoteToMakeContent(account);
        var nextLvlReputation = accountHelper.getNextLevelReputation(account);

        var remaining_contents = 0;
        if (votesByContent > 0)
          remaining_contents = Math.floor(nbVotesUnused/votesByContent)

        if(nbVotesUnused == -1){
          return response.formatErr(res,500,{message:"Erreur sur la recherche de votes"})
        }
        account = {
          _id               : account._id,
          pseudo            : account.pseudo,
          google_id         : account.google_id,
          fb_id             : account.fb_id,
          votesSpent        : account.votesSpent,
          notif             : account.notif,
          votes             : account.votes,
          reputation        : account.reputation,
          remaining_contents: remaining_contents,
          nextLvlReputation : nextLvlReputation
        }

        return response.formatAnswerObject(res, 201, {message:null}, account);
      })
      .catch(function(err){
        response.formatErr(res,500,{message:"Probleme rencontré pour trouvé le compte"});
      }); 
}
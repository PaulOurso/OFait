'use strict'

const Account        = require('../models/Account'),
      response       = require('../helpers/answer_helper');

exports.findAccountByID = function findAccountByID(req, res) {
  var id = req.params.id;
  if (id && id.match(/^[0-9a-fA-F]{24}$/)) {
    Account.findById(req.params.id).lean().exec()
      .then((account) => {
        if (account === null)
          return response.formatErr(res, 404, 'error', 'Compte inexistant.');
        response.formatAnswerObject(res, 200, 'success', '', account);
      })
      .catch((err) => response.formatErr(res, 500, 'error', err));
  }
  else {
    response.formatErr(res, 400, 'error', {message: 'Erreur dans la requête.'});
  }
}

exports.addAccountIfNotExist = function addAccountIfNotExist(req, res) {
  var fb_id = req.body.fb_id;
  var google_id = req.body.google_id;
  if (fb_id || google_id) {
    var param = fb_id ? {fb_id: fb_id} : {google_id: google_id};
    Account.findOne(param).lean().exec()
      .then((account) => {
        if (account && account._id)
          return response.formatAnswerObject(res, 200, 'success', '', account);
        return new Account(req.body);
      })
      .then((newAccount) => newAccount.save())
      .then((account) => response.formatAnswerObject(res, 201, 'success', '', account))
      .catch((err) => response.formatErr(res, 500, 'error', err));
  }
  else {
    response.formatErr(res, 400, 'error', {message: 'Paramètres manquants.'});
  }
}

exports.updateAccountByID = function updateAccountByID(req, res) {
  var id = req.params.id;
  if (id && id.match(/^[0-9a-fA-F]{24}$/)) {
    Account.findById(id).exec()
      .then((account) => {
        if (!account)
          return Promise.reject('Compte non trouvé.');
        account.pseudo = req.body.pseudo || account.pseudo;
        return account;
      })
      .then((account) => account.save())
      .then((account) => response.formatAnswerObject(res, 201, 'success', '', account))
      .catch((err) => response.formatErr(res, 500, 'error', err));
  }
  else {
    response.formatErr(res, 400, 'error', {message: 'Paramètres manquants.'});
  }
}
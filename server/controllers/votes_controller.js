'use strict'

const Account      = require('../models/Account'),
    Content        = require('../models/Content'),
    Vote           = require('../models/Vote'),
    config         = require('config'),
    levels         = config.get('Customer.levels');

var clients        = [];

exports.initSocket = function initSocket(socket) {
  //console.log('SOCKET : Un client est connecté ! ID: '+socket.id);
  socket.on('init_socket', (account_id) => setClient(socket, account_id));
  socket.on('vote', (vote)              => actionVote(socket, vote));
  socket.on('disconnect', ()            => disconnect(socket));
}

function setClient(socket, account_id) {
  //console.log('SOCKET : Le client est: '+account_id);
  var i = clients.map((e) => { return e.socket_id }).indexOf(socket.id);
  clients.push({account_id:account_id, socket_id:socket.id});
  //console.log(clients);
}

function actionVote(socket, voteJSON) {
  if (voteJSON.account && voteJSON.content && Number.isInteger(voteJSON.value)) {
    var vote = new Vote(voteJSON);
    vote.save()
      .then((newVote) => {
        Account.findById(newVote.account).exec()
          .then((account) => {
            account.votes.push(newVote);
            if ((account.votes.length % levels.nb_votes_to_win_rep) == 0) {
              account.reputation += levels.win_rep_after_nb_votes;
            }
            account.save();
          })
          .catch((err) => console.log(err));
        Content.findById(newVote.content).populate('created_by').exec()
          .then((content) => {
            content.votes.push(newVote);
            if (content.votes.length == levels.nb_votes_to_win_hot && newVote.value == 1) {
              var account = content.created_by;
              account.reputation += levels.win_rep_after_hot;
              account.save();
            }
            content.save();
            Content.find({created_by: content.created_by}).sort({created_date:-1}).limit(1).lean().exec()
              .then((lastContent) => {
                if (lastContent && lastContent.length > 0) {
                  var lastC = lastContent[0];
                  if (String(lastC._id).localeCompare(String(content._id)) == 0 && lastC.notif) {
                    var iDest = clients.map((e) => { return String(e.account_id) }).indexOf(String(lastC.created_by));
                    if (iDest >= 0 && iDest < clients.length) {
                      socket.to(clients[iDest].socket_id).emit("voted_for_me", {value:newVote.value});
                    }
                  }
                }
              })
              .catch((err) => console.log(err));
          })
          .catch((err) => console.log(err));
      })
      .catch((err) => console.log(err));
  }
  else {
    console.log("Format Vote error");
  }
}

function disconnect(socket) {
  //console.log('Deconnexion de '+socket.id);
  var i = clients.map((e) => { return e.socket_id }).indexOf(socket.id);
  clients.splice(i, 1);
  //console.log(clients);
}
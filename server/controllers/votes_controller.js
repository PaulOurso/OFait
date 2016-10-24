'use strict'

const Account      = require('../models/Account'),
    Content        = require('../models/Content'),
    Vote           = require('../models/Vote');

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
            account.save();
          });
        Content.findById(newVote.content).exec()
          .then((content) => {
            content.votes.push(newVote);
            content.save();
            if (content.notif) {
              var iDest = clients.map((e) => { return e.account_id }).indexOf(newVote.account);
              if (iDest >= 0 && iDest < clients.length) {
                socket.to(clients[select].id).emit("voted_for_me", {value:newVote.value});
              }
            }
          });
      });
  }
}

function disconnect(socket) {
  //console.log('Deconnexion de '+socket.id);
  var i = clients.map((e) => { return e.socket_id }).indexOf(socket.id);
  clients.splice(i, 1);
  //console.log(clients);
}
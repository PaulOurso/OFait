'use strict'
const 	Content = require('../models/Content'),
		Account = require('../models/Account'),
		config = require('config'),
		list_config = config.get('Customer.levels');

exports.checkIfFavorite = function checkIfFavorite(content,account_id){
  if (content.favorite_for_account) {
    var account_index = content.favorite_for_account.indexOf(account_id);
    return (account_index != -1);
  }
  return false;
}

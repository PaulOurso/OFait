'use strict'

exports.formatAnswerArray = function formatAnswerArray(res, status, message, datas) {
  return res.status(status).json({status:status, message:message, datas:datas});
}
exports.formatAnswerObject = function formatAnswerObject(res, status, message, data) {
  return res.status(status).json({status:status, message:message, data:data});
}

exports.formatErr = function formatErr(res, status, err) {
  return res.status(status).json({status:status, message:err});
}
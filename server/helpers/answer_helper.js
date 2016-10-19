'use strict'

exports.formatAnswerArray = function formatAnswerArray(res, status, code, message, datas) {
  return res.status(status).json({status:status, code:code, message:message, datas:datas});
}
exports.formatAnswerObject = function formatAnswerObject(res, status, code, message, data) {
  return res.status(status).json({status:status, code:code, message:message, data:data});
}

exports.formatErr = function formatErr(res, status, code, err) {
  return res.status(status).json({status:status, code:code, message:err});
}
//Bunch of exports which can be used application wide

var name = 'OpenWoZ'
exports.appName = name;

exports.log = {
  path: __dirname + "/var/log/app_#{name}.log"
};  

exports.server = {
  port: 80,
  ip: '127.0.0.1'
};  

//DB to be decided
exports.db = {
  URL: ""
};
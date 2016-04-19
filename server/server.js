//Get Instance of ExpressJS
var express = require('express');
var app = express();

var config = require('./config');

//Access static content from assets folder
app.use(express.static('assets'));

//Map all controllers to URL's
[
  'test'
].map(function(controllerName){
  var controller = require('./controllers/' + controllerName);
  controller.setup(app);
});

//Route for '/'
app.get('/', function(req, res){
	res.send('Got get request, serving from Server Root!');
});

app.listen(config.server.port, function(){
	console.log('Starting openwoz server at port ' + config.server.port + '!');
});

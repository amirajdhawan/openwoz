//Get Instance of ExpressJS
var express = require('express');
var app = express();
var fs = require('fs');
var path = require('path');
var robo_profiles_ds = require('./models/robot_profiles')
var config = require('./config');

//Access static content from assets folder
app.use(express.static('assets'));

//Read all robot profiles
var fileNames = fs.readdirSync(path.join(__dirname, 'robotprofiles/'));

for(var i = 0; i < fileNames.length; i++){
	var fileContents = JSON.parse(fs.readFileSync('robotprofiles/' + fileNames[i], 'utf8'));
	robo_profiles_ds.robot_profiles[fileNames[i]] = fileContents
}

//Map all controllers to URL's
[
  'test'
].map(function(controllerName){
  var controller = require('./controllers/' + controllerName);
  controller.setup(app);
});

//Route for '/'
app.get('/', function(req, res){
	//res.send('Got get request, serving from Server Root!');
	var string = "";
	for(profile in robo_profiles_ds.robot_profiles){
		string = string + "<br/>" + JSON.stringify(robo_profiles_ds.robot_profiles[profile])
	}
	res.send(string);
});

app.listen(config.server.port, function(){
	console.log('Starting openwoz server at port ' + config.server.port + '!');
});


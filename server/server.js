//Get Instance of ExpressJS
var express = require('express');
var app = express();
var fs = require('fs');
var path = require('path');
var redis = require('redis');

//Custom
var routes = require('./routes');
var robo_profiles_ds = require('./models/robot_profiles')
var config = require('./config');

//Access static content from assets folder
app.use(express.static('assets'));
app.set('views', __dirname + '/views');
app.set('view engine', 'jade');

//Redis channels
var redisChannels = [];

//Read all robot profiles
var fileNames = fs.readdirSync(path.join(__dirname, 'robotprofiles/'));

for(var i = 0; i < fileNames.length; i++){
	var fileContents = JSON.parse(fs.readFileSync('robotprofiles/' + fileNames[i], 'utf8'));
	var filename = fileNames[i].split(".js")[0];
	robo_profiles_ds.robot_profiles[filename] = fileContents
	redisChannels.push(filename);
}
var message = {"event": "Event1", "param" : ["param1", "param2"]};
publisher  = redis.createClient();
publisher.publish(redisChannels[0], JSON.stringify(message));

//Map all controllers to URL's
[
'test'
].map(function(controllerName){
	var controller = require('./controllers/' + controllerName);
	controller.setup(app);
});

app.get('/', routes.index);
app.get('/partials/:name', routes.partials);

//Route for '/'
app.get('/profiles/view', function(req, res){
	//res.send('Got get request, serving from Server Root!');
	/*var string = "";
	for(profile in robo_profiles_ds.robot_profiles){
		string = string + "<br/>" + JSON.stringify(robo_profiles_ds.robot_profiles[profile])
	}*/
	res.json({profiles:robo_profiles_ds.robot_profiles});
});

app.listen(config.server.port, function(){
	console.log('Starting openwoz server at port ' + config.server.port + '!');
});


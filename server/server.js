var redis_pass = "";

//Get Instance of ExpressJS
var express = require('express');
var app = express();
var fs = require('fs');
var path = require('path');
var redis = require('redis');
var pug = require('pug');
var bodyParser = require('body-parser')

app.use(bodyParser.json());       // to support JSON-encoded bodies
app.use(bodyParser.urlencoded({     // to support URL-encoded bodies
  extended: true
}));

//Custom
var routes = require('./routes');
var robo_profiles_ds = require('./models/robot_profiles')
var config = require('./config');

//Access static content from assets folder
app.use(express.static('assets'));
app.set('views', __dirname + '/views');
app.set('view engine', 'pug');

//Redis channels
var redisChannels = {};

//Read all robot profiles
var fileNames = fs.readdirSync(path.join(__dirname, 'robotprofiles/'));

for(var i = 0; i < fileNames.length; i++){
	var fileContents = JSON.parse(fs.readFileSync('robotprofiles/' + fileNames[i], 'utf8'));
	var filename = fileNames[i].split(".js")[0];
	robo_profiles_ds.robot_profiles[filename] = fileContents
	redisChannels[filename] = filename;
}

publisher = redis.createClient();
publisher.auth(redis_pass);
//publisher.publish(redisChannels["vyo_robot"], JSON.stringify(message));

//Map all controllers to URL's
[
'test'
].map(function(controllerName){
	var controller = require('./controllers/' + controllerName);
	controller.setup(app);
});

app.get('/', function(req, res){
	res.render("index");
});

app.get('/robots/:profile_name/:event_name', function(req, res){
	//Actually send it to the redis channel
	//console.log(req.body);
	var profile = req.params.profile_name;
	var event = req.params.event_name;
	//console.log("Value: " + req.body.newVal);
	//var new_value = parseFloat(req.body.newVal);

	var message = robo_profiles_ds.robot_profiles[profile].events[event];
	//message.value = new_value;
	console.log("Sending redis channel: " + redisChannels[profile] + " message:" + JSON.stringify(message));
	publisher.publish(redisChannels[profile], JSON.stringify(message));
	
	//res.render("profile", {profile: robo_profiles_ds.robot_profiles[profile], msg: "Event successfully triggered!"});
	res.json({msg:"Event successfully triggered!"});
	//res.render("event", {msg: "Message successfully passed!", profile: robo_profiles_ds.robot_profiles[profile], 
	//	event_name: req.params.event_name});
});

/*app.get('/robots/:profile_name/:event_name', function(req, res){
	res.render("event", {profile: robo_profiles_ds.robot_profiles[req.params.profile_name], event_name: req.params.event_name});
});*/

app.get('/robots/:profile_name', function(req, res){
	res.render("profile", {profile: robo_profiles_ds.robot_profiles[req.params.profile_name]});
});

app.get('/robots', function(req, res){
	res.render("robots", {profiles: robo_profiles_ds.robot_profiles});
});

app.get('/sendevent', function(req, res){
	//"events/index.html");
});

/*app.get('/test', routes.index);
app.get('/partials/:name', routes.partials);*/

//Route for '/'
/*app.get('/profiles/view', function(req, res){
	//res.send('Got get request, serving from Server Root!');
	var string = "";
	for(profile in robo_profiles_ds.robot_profiles){
		string = string + "<br/>" + JSON.stringify(robo_profiles_ds.robot_profiles[profile])
	}
	res.json({profiles:robo_profiles_ds.robot_profiles});
});*/

app.listen(config.server.port, function(){
	console.log('Starting openwoz server at port ' + config.server.port + '!');
});


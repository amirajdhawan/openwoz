//Get Instance of ExpressJS
var express = require('express');
var app = express();

//Access static content from assets folder
app.use(express.static('assets'));



//Start the server at port 8080
app.listen(8080, function(){
	console.log('Starting openwoz server at port 8080.');
});
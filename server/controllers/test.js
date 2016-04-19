
TestController = function(){
}

TestController.prototype.showTest = function (req, res){
	res.send('Got Controller get request, serving from test Controller!');
}

//Setup controller
exports.setup = function(app) {
	var controller = new TestController();
	var route = '/test';
	app.get(route, controller.showTest);
}


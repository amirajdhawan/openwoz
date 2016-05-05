'use strict';
// Declare app level module which depends on filters, and services
angular.module('openwoz', ['ngRoute']).
  config(['$routeProvider', '$locationProvider', function($routeProvider, $locationProvider) {
    $routeProvider.
      when('/profiles/view', {
        templateUrl: 'partials/index',
        controller: ProfileIndexCtrl
      }).
      when('/profiles/addProfile', {
        templateUrl: 'partials/addProfile',
        controller: ProfileAddCtrl
      }).
      when('/profiles/viewprofile/:id', {
        templateUrl: 'partials/viewProfile',
        controller: ProfileViewCtrl
      }).
      when('/profiles/editprofile/:id', {
        templateUrl: 'partials/editProfile',
        controller: ProfileEditCtrl
      }).
      when('/profiles/deleteprofile/:id', {
        templateUrl: 'partials/deleteProfile',
        controller: ProfileDelCtrl
      }).
      otherwise({
        redirectTo: '/'
      });
    $locationProvider.html5Mode(true);
  }]);
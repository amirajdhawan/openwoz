function ProfileIndexCtrl($scope, $http) {
  $http.get('/profiles/view').
    success(function(data, status, headers, config) {
      $scope.profiles = data.profiles;
    });
}

function ProfileAddCtrl($scope, $http, $location) {
  $scope.form = {};
  $scope.submitPost = function () {
    $http.post('/profiles/addprofile/', $scope.form).
      success(function(data) {
        $location.path('/');
      });
  };
}

function ProfileViewCtrl($scope, $http, $routeParams) {
  $http.get('/profiles/view/' + $routeParams.profileName).
    success(function(data) {
      $scope.profile = data.profile;
    });
}

function ProfileEditCtrl($scope, $http, $location, $routeParams) {
  $scope.form = {};
  $http.get('/profiles/edit/' + $routeParams.profileName).
    success(function(data) {
      $scope.form = data.post;
    });

  $scope.editProfile = function () {
    $http.put('/profiles/edit/' + $routeParams.profileName, $scope.form).
      success(function(data) {
        $location.url('/view/' + $routeParams.profileName);
      });
  };
}

function ProfileDelCtrl($scope, $http, $location, $routeParams) {
  $http.get('/profiles/delete/' + $routeParams.profileName).
    success(function(data) {
      $scope.profile = data.profile;
    });

  $scope.deleteProfile = function () {
    $http.delete('/profiles/delete/' + $routeParams.profileName).
      success(function(data) {
        $location.url('/');
      });
  };

  $scope.home = function () {
    $location.url('/');
  };
}
angular.module('app.controllers', ['ngCordova', 'ionic', 'ngCordovaOauth', 'ngStorage'])

  .controller('homeCtrl', function ($scope, $cordovaGeolocation, $ionicPlatform, $cordovaOauth, $localStorage, $http) {


    $scope.data = {};

    $scope.response = null;

    $scope.ask = function () {
      console.log($localStorage.forcePositive);
      if ($scope.data.question.length > 0) {
        $http.get("http://isitgood.co/api/question", {
          params: {
            question: $scope.data.question,
            forcePositive: $localStorage.forcePositive === null ? false :  $localStorage.forcePositive
          }
        }).then(function (response) {
          $scope.response = response.data;
        });
      }
    };

    $scope.askAgain = function () {
      $ionicPlatform.ready(function () {
        $scope.response = null;
        $scope.data.question = "";
      });
    };

  })

  .controller('settingsCtrl', function ($ionicPlatform, $scope, $localStorage) {

    $scope.data = {};

    $ionicPlatform.ready(function () {
      $scope.data.positive =  $localStorage.forcePositive;
    });

    $scope.changed = function () {
      $localStorage.forcePositive = $scope.data.positive;
    }

  });


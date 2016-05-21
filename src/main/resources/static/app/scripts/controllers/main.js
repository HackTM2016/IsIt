'use strict';

angular.module('isItApp')
  .controller('MainCtrl', function ($scope, $http, $timeout) {

    $scope.question = "";

    $scope.response = null;

    $scope.ask = function () {
      console.log('asking' + $scope.question);
      if ($scope.question.length > 0) {

        $http.get("http://localhost:8080/api/question", {params: {question: $scope.question}}).then(function (response) {
          $scope.response = response.data;
          console.log($scope.response);
        });
      }
    };

    $scope.askAgain = function () {
      $scope.response = null;
      $scope.question = "";
      $timeout(function () {
        $('#question').focus();
      }, 50);

    };

  });

angular.module('imbExample', [ 'encounterService',  'ui.bootstrap' ])

    .controller('ImbExampleCtrl', [ '$scope', '$http',
        function($scope, $http) {

            $scope.calculateBmi = function() {

                console.log("weight=" + $scope.weight);
                console.log("height=" + $scope.height);

                var bmi = $scope.weight / ($scope.height * $scope.height) ;

                var errors = [];
                if (!$scope.weight) {
                    errors.push("Missing weight");
                }
                if (!$scope.height) {
                    errors.push("Missing height");
                }

                if (!bmi) {
                    errors.push("Invalid");
                }
                if (bmi < 10) {
                    errors.push("Invalid: BMI too low");
                }
                if (bmi > 40) {
                    errors.push("Invalid: BMI too high (" + bmi + ")");
                }

                if (errors.length > 0) {
                    $scope.errorMessage = errors.join(", ");
                }

                return bmi;
            }

            $scope.searchForPatients = function(searchTerm) {
                $http.get("/" + OPENMRS_CONTEXT_PATH + "/ws/rest/v1/patient?v=default&q=" + searchTerm)
                    .success(function(data) {
                        $scope.patientsFound = data.results;
                    });
            }

            $scope.selectPatient = function(patient) {
                $scope.selectedPatient = patient;
            }

            $scope.saveWeightAndHeight = function() {
                var encounter = {
                    patient: $scope.selectedPatient.uuid,
                    encounterType: 'TODO',
                    obs: [
                        {
                            concept: '5089-uuid',
                            value: $scope.weight
                        },
                        {
                            concept: '5090-uuid',
                            value: $scope.height
                        }
                    ]
                };

                $http.post("/" + OPENMRS_CONTEXT_PATH + "/ws/rest/v1/encounter",
                    encounter)
                    .success(function() {
                        window.alert("Saved!");
                    })
                    .error(function(error) {
                        console.log(error);
                        window.alert("Error");
                    })
            }

        }])
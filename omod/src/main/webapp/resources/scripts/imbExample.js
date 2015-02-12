angular.module('imbExample', [ 'encounterService',  'ui.bootstrap' ])

    .controller('ImbExampleCtrl', [ '$scope', '$http',
        function($scope, $http) {

            $scope.calculateBmi = function() {

                console.log("weight=" + $scope.weight);
                console.log("height=" + $scope.height);

                var bmi = $scope.weight / (($scope.height * $scope.height) /10000 );
                $scope.errorMessage = '';
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
                if (bmi < 18.5) {
                    errors.push("Warning: BMI too low, UNDERWEIGHT");
                }
                if (bmi > 25 && bmi < 30) {
                    errors.push("Warning: BMI too high. OVERWEIGHT (" + bmi + ")");
                }
                if (bmi >= 30) {
                    errors.push("Warning: BMI is very high, OBESITY");
                }

                if (errors.length > 0) {
                    $scope.errorMessage = errors.join(", ");
                } else {
                    $scope.errorMessage = 'Normal';
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
                    encounterType: 'daf32375-d293-4e27-a68d-2a58494c96e1',
                    obs: [
                        {
                            concept: '3ce93b62-26fe-102b-80cb-0017a47871b2', //5089-uuid
                            value: $scope.weight
                        },
                        {
                            concept: '3ce93cf2-26fe-102b-80cb-0017a47871b2', //5090-uuid
                            value: $scope.height
                        }
                    ]
                };

                console.log("submit bmi");


                $http.post("/" + OPENMRS_CONTEXT_PATH + "/ws/rest/v1/encounter",
                    encounter)
                    .success(function() {
                        window.alert("Saved!");
                    })
                    .error(function(error) {
                        console.log(error);
                        window.alert("Error");
                    });

            }

        }])
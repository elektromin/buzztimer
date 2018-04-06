angular.module('buzztimer', ['ngRoute', 'ngGeolocation'])

    .config(function($routeProvider) {
        $routeProvider.when('/', {
            controller: 'DeparturesController as departures',
            templateUrl: 'departures.html'
        }).when('/departures', {
            controller: 'DeparturesController as departures',
            templateUrl: 'departures.html'
        }).when('/stations', {
            controller: 'StationController as stations',
            templateUrl: 'stations.html'
        })
    })

    .controller('StationController', function($http, $geolocation) {
        var stations = this;
        stations.error = null;
        stations.closestStation = null;

        stations.getStationsFromServer = function() {
            $geolocation.getCurrentPosition({ timeout: 60000 })
                .then(function(position) {
                    $http.get('/api/station/closest?latitude=' + position.coords.latitude
                            + '&longitude=' + position.coords.longitude)
                        .then(function(success) {
                            stations.closestStation = success.data;
                        }, function(error) {
                            stations.error = error;
                        });
                });
        };
        stations.getStationsFromServer();
    })

    .controller('DeparturesController', function($http, $geolocation) {
            var departures = this;
            departures.error = null;
            departures.result = null;

            departures.getDeparturesFromServer = function() {
                $geolocation.getCurrentPosition({ timeout: 60000 })
                    .then(function(position) {
                        $http.get('/api/departures/location?latitude=' + position.coords.latitude
                                + '&longitude=' + position.coords.longitude)
                            .then(function(success) {
                                departures.result = success.data;
                            }, function(error) {
                                departures.error = error;
                            });
                    });
            };
            departures.getDeparturesFromServer();
        });
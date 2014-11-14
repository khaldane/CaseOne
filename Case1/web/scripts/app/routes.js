/* routes.js
 * Used to setup routes for partial pages and match the page with 
 * the correct controller
 */
(function(app) {
    app.config(['$routeProvider', function($routeProvider) {
            $routeProvider
                    .when('/', {
                        controller: '',
                        templateUrl: 'partials/home.html'
                    })
                    .when('/addVendor', {
                        controller: 'AddVendorCtrl',
                        templateUrl: 'partials/addVendor.html'
                    })
                    .when('/Vendors', {
                        controller: 'VendorCtrl',
                        templateUrl: 'partials/Vendors.html'
                    })
                    .when('/Products', {
                        controller: 'ProductCtrl',
                        templateUrl: 'partials/Products.html'
                    })
                    .when('/Generator', {
                        controller: 'GeneratorCtrl',
                        templateUrl: 'partials/Generator.html'
                    })
                    .otherwise({redirectTo: '/'});
        }]);
})(angular.module('case1'));
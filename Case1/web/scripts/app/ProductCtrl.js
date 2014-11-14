(function (app) {
var ProductCtrl = function($scope, $modal, RESTFactory, $filter){
    var baseurl = 'webresources/product';
    var init = function() {
        
        //load data for page from WEB api
        $scope.status = 'Loading Products...';
        $scope.products= RESTFactory.restCall('get', baseurl, -1, '').then(function(products){
            
            if (products.length > 0){ //object returned it worked, number indicates status
                $scope.products = products;
                $scope.status = 'products Retreved';
            }
            else{
                $scope.status = 'products not retrieved code - ' + products;
            }
        }, function(reason) { //error
            $scope.status='products not retireved ' + reason;
        });
        $scope.products = $scope.products[0];
    }; //init   
    init();
    
    //sort on col and highlight row
    $scope.findSelected = function(col, order){
        $scope.products = $filter('orderby') ($scope.products, col, order);
        for (i=0; i < $scope.products.length; i++){
            if($scope.products[i].vendorno === $scope.products.vendorno){
                $scope.selectRow = i;                
            }
        }
    };

        $scope.selectRow = function(row, product){
            if (row <0) {
                $scope.todo = 'add';
                $scope.product = new Object();
            }
            else {
                $scope.product = product;
                $scope.selectedRow = row;
                $scope.todo = 'update';
            }

            var modalInstance = $modal.open({
               templateUrl: 'partials/productModal.html',
               controller: 'ProductModalCtrl',
               scope: $scope
            });

            //modal returns results 
            modalInstance.result.then(function(results){
                switch (results.operation) {
                    case 'delete':
                        //locate and remove from array
                        for (var i= 0; i< $scope.products.length; i++){
                            if ($scope.products[i].productCode === results.productcode) {
                            $scope.products.splice(i,1);
                            break;
                        }
                }
                if (results.numOfRows === 1){
                    $scope.selectedRow = null;
                    $scope.status = 'Product ' + results.productcode + ' Deleted!';
                }
                else{
                    $scope.status = 'Product ' + results.productcode + ' Not Deleted!';
                }
                break;
            case 'update': 
                if(results.numOfRows === 1){
                 $scope.status = 'Product ' + results.productcode + ' Updated!';
                }
                else {
                    $scope.status = 'Product Not Updated';
                }
                break;
            case 'add':
                if (results.numOfRows === 1){
                    $scope.status = 'Product ' + results.productno + ' Added!';
                    $scope.selectedRow = $scope.products.length - 1;
                }
                else {
                    $scope.status = 'Product Not Added';
                }
                break;
            case 'cancel':
                $scope.status = 'Product Not Updated';
                break;
            }
        });
    };
};
    app.controller('ProductCtrl',['$scope', '$modal', 'RESTFactory', '$filter' , ProductCtrl]);
})(angular.module('case1'));  
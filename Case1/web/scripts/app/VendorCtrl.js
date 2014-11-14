(function (app) {
var VendorCtrl = function($scope, $modal, RESTFactory, $filter){
    var baseurl = 'webresources/vendor';
    var init = function() {
        
        //load data for page from WEB api
        $scope.status = 'Loading Vendors...';
        $scope.vendors= RESTFactory.restCall('get', baseurl, -1, '').then(function(vendors){
            
            if (vendors.length>0){ //object returned it worked, number indicates status
                $scope.vendors = vendors;
                $scope.status = 'Vendors Retreved';
            }
            else{
                $scope.status = 'Vendors not retrieved code- ' + vendors;
            }
        }, function(reason) { //error
            $scope.status='Vendors not retireved ' + reason;
        });
        $scope.vendor = $scope.vendors[0];
    }; //init   
    init();
    
    //sort on col and highlight row
    $scope.findSelected = function(col, order){
        $scope.vendors = $filter('orderby') ($scope.vendors, col, order);
        for (i=0; i < $scope.vendors.length; i++){
            if($scope.vendors[i].vendorno === $scope.vendor.vendorno){
                $scope.selectRow = i;                
            }
        }
    };

        $scope.selectRow = function(row, vendor){
            if (row <0) {
                $scope.todo = 'add';
                $scope.vendor = new Object();
            }
            else {
                $scope.vendor = vendor;
                $scope.selectedRow = row;
                $scope.todo = 'update';
            }

            var modalInstance = $modal.open({
               templateUrl: 'partials/vendorModal.html',
               controller: 'VendorModalCtrl',
               scope: $scope
            });

            //modal returns results 
            modalInstance.result.then(function(results){
                switch (results.operation) {
                    case 'delete':
                        //locate and remove from array
                        for (var i= 0; i< $scope.vendors.length; i++){
                            if ($scope.vendors[i].vendorno === results.vendorno) {
                            $scope.vendors.splice(i,1);
                            break;
                        }
                }
                if (results.numOfRows === 1){
                    $scope.selectedRow = null;
                    $scope.status = 'Vendor ' + results.vendorno + ' Deleted!';
                }
                else{
                    $scope.status = 'Vendor ' + results.vendorno + ' Not Deleted!';
                }
                break;
            case 'update': 
                if(results.numOfRows === 1){
                 $scope.status = 'Vendor ' + results.vendorno + ' Updated!';  
                }
                else {
                    $scope.status = 'Vendor Not Updated';
                }
                break;
            case 'add':
                if (results.numOfRows === 1){
                    $scope.status = 'Vendor ' + results.vendorno + ' Added!';
                    $scope.selectedRow = $scope.vendors.length - 1;
                }
                else {
                    $scope.status = 'Vendor Not Added';
                }
                break;
            case 'cancel':
                $scope.status = 'Vendor Not Updated';
                break;
            }
        });
    };
};
    app.controller('VendorCtrl',['$scope', '$modal', 'RESTFactory', '$filter' , VendorCtrl]);
})(angular.module('case1'));  
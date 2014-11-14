/*
 * Name: Katherine Haldane
 * Date: Oct 17, 2014
 * Description: Vendor Modal Control that adds, deletes, and updates vendors. 
 */

(function(app) {
    
    // VendorModalCtrl - angularjs controller for VendorModal.html
    var VendorModalCtrl = function($scope, $modalInstance, RESTFactory) {
        
        var baseurl = 'webresources/vendor';
        var retVal = {operation: '', vendorno: -1, numOfRows: -1};
        /*
         * Name: init
         * Params: None 
         * Returns: None
         * Description: Sets up default vendor items in the modal
         */
        var init = function() {
            if ($scope.todo === 'add') {
                $scope.modalTitle = 'Add New Vendor';
                $scope.vendor.name = 'Test Vendor';
                $scope.vendor.email = 'tv@here.com';
                $scope.vendor.address1 = '123 Road St.';
                $scope.vendor.city = 'London';
                $scope.vendor.postalCode = 'N1N 1N1';
                $scope.vendor.phone = '519-999-5555';
                $scope.vendor.vendortype = 'Trusted';
                $scope.vendor.province = 'Ontario';
            }
            else {
                $scope.modalTitle = 'Vendor: ' + $scope.vendor.vendorno;
            } 
        }; // init
        
        
        /*
         * Name: Add
         * Params: None 
         * Returns: retVal
         * Description: Adds the vendor to the database in the modal
         */
        $scope.add = function() {
            RESTFactory.restCall('post', baseurl, -1, $scope.vendor).then(function(results) {
                if (results.substring) { //string returned it worked, number inc
                    if (parseInt(results) > 0) {
                        $scope.vendor.vendorno = parseInt(results);
                        $scope.vendors.push($scope.vendor);
                        retVal.numOfRows = 1;
                        retVal.operation = 'add';
                        retVal.vendorno = parseInt(results);
                        $modalInstance.close(retVal);
                    }
                    else{
                        retVal = 'Vendor was not added! - system error ' + results;
                    }
                }
                else {
                    retVal = 'Vendor was not added! - system error ' + results;
                    $modalInstance.close(parseInt(results));
                }
            }, function(reason){
                retVal = 'Vendor was not added! - system error ' + reason;
                $modalInstance.close(retVal);
            });
        }; // add
        
        /*
         * Name: Delete
         * Params: None 
         * Returns: retVal
         * Description: Deletes the vendor to the database in the modal
         */
        $scope.delete = function() {
            RESTFactory.restCall('delete', baseurl, $scope.vendor.vendorno, '').then(function(results){
                retVal.operation = 'delete';
                retVal.vendorno = $scope.vendor.vendorno;
                
                if(results.substring){ //string = worked,number = problem
                    retVal.numOfRows = parseInt(results);
                }
                else {
                    retVal.numOfRows = -1;
                }
                
                $modalInstance.close(retVal);
            }, function() { //error
                retVal.numOfRows = 1;
                $modalInstance.close(retVal);
            });           
        };
        
        /*
         * Name: Update
         * Params: None 
         * Returns: retVal
         * Description: Updates the vendor in the database in the modal
         */
        $scope.update = function() {
            RESTFactory.restCall('put', baseurl, -1, $scope.vendor).then(function(results) {
                retVal.operation = 'update';
                retVal.vendorno = $scope.vendor.vendorno;
                
                if (results.substring) {
                    retVal.numOfRows = parseInt(results);
                }
                else {
                    retVal.numOfRows = -1;
                }
                
                $modalInstance.close(retVal);
            }, function(reason) { // error
            retVal = 'Vendor was not updated! - system error ' + reason;
                $modalInstance.close(retVal);
            });
            
        }; // update
        
        $scope.cancel = function() {
            $modalInstance.close();
        };
        
        init();
    };
    
    app.controller('VendorModalCtrl', ['$scope', '$modalInstance', 'RESTFactory', VendorModalCtrl]);

})(angular.module('case1'));
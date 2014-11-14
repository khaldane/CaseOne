/*
 * Name: Katherine Haldane
 * Date: Oct 17, 2014
 * Description: Product Modal control that adds a new product to the database
 */
(function(app) {

    // ProductModalCtrl - angularjs controller for productModal.html
    var ProductModalCtrl = function($scope, $modalInstance, RESTFactory /*,$filter */) {

        var baseurl = 'webresources/product';
        var retVal = {operation: '', productno: '', numOfRows: -1};
        /*
         * Name: init
         * Params: None 
         * Returns: None
         * Description: Sets up default product info in the modal
         */
        var init = function() {
            if ($scope.todo === 'add') {
                $scope.modalTitle = 'Add New Product';
                $scope.product.productCode = '16X45';
                $scope.product.vendorsku = 'C12345';
                $scope.product.productName = 'Doo Hickey';
                $scope.product.costPrice = 1299.98;
                $scope.product.msrp = 1599.99;
                $scope.product.rop = 5;
                $scope.product.eoq = 6;
                $scope.product.qoh = 10;
                $scope.product.qoo = 0;

            }
            else {
                $scope.modalTitle = 'Product: ' + $scope.product.productCode;
            }
        }; // init

        $scope.vendors = RESTFactory.restCall('get', 'webresources/vendor', -1, '').then(function(vendors) {
            if (vendors.length > 0) {
                $scope.vendors = vendors;
            }
            else {
                $scope.status = 'Vendrs not retrueved code - ' + vendors;
            }
        }, function(reason) { //error
            $scope.status = 'Vendors not retrieved' + reason;
        });

        /*
         * Name: Add
         * Params: None 
         * Returns: retVal
         * Description: Adds the product to the database in the modal
         */
        $scope.add = function() {
            RESTFactory.restCall('post', baseurl, -1, $scope.product).then(function(results) {
                if (results.substring) { //string returned it worked, number inc
                    if (results.length > 0) {
                        $scope.product.productno = results;
                        $scope.products.push($scope.product);
                        retVal.numOfRows = 1;
                        retVal.operation = 'add';
                        retVal.productno = results;
                        $modalInstance.close(retVal);
                    }
                    else {
                       $scope.modalStatus = 'Vendor was not added! - system error ' + results;
                       modalInstance.close(retVal);
                    }
                }
                else {
                    $scope.modalStatus = 'Product was not added! - system error ' + results;
                    modalInstance.close(retVal);
                }
            }, function(reason) {
               $scope.modalStatus = 'Product was not added! - system error ' + reason;
                modalInstance.close(retVal);
            });
        }; // add

        /*
         * Name: Delete
         * Params: None 
         * Returns: retVal
         * Description: Deletes the product to the database in the modal
         */
        $scope.delete = function() {
            RESTFactory.restCall('delete', baseurl, $scope.product.productCode, '').then(function(results) {
                retVal.operation = 'delete';
                retVal.productcode = $scope.product.productCode;
                if (results.substring) { //string = worked,number = problem
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
         * Description: Updates the product in the database in the modal
         */
        $scope.update = function() {
            RESTFactory.restCall('put', baseurl, -1, $scope.product).then(function(results) {
                retVal.operation = 'update';
                retVal.productcode = $scope.product.productCode;

                if (results.substring) {
                    retVal.numOfRows = parseInt(results);
                }
                else {
                    retVal.numOfRows = -1;
                }

                $modalInstance.close(retVal);
            }, function(reason) { // error
                retVal = 'Product was not updated! - system error ' + reason;
                $modalInstance.close(retVal);
            });

        }; // update
        
        $scope.cancel = function() {
            $modalInstance.close();
        };
        
        init();
    };

    app.controller('ProductModalCtrl', ['$scope', '$modalInstance', 'RESTFactory', ProductModalCtrl]);

})(angular.module('case1'));
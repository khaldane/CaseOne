/*
 * Name: Katherine Haldane
 * Date: Oct 17, 2014
 * Description: Generator control that adds a new pos to the database 
 */
(function(app) {

    // GeneratorCtrl - angularjs controller for productModal.html
    var GeneratorCtrl = function($scope, $window, RESTFactory /*,$filter */) {

        var baseurl = 'webresources/generator';

        //Get the vendors
        $scope.vendors = RESTFactory.restCall('get', 'webresources/vendor', -1, '').then(function(vendors) {
            $scope.status = "Choose a Vendor";
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
         * Name: changeVendors
         * Params: None 
         * Returns: None
         * Description: Display the prodcts and qty option to add a product
         */
        $scope.changeVendors = function() {
            $scope.items = new Array();
            $scope.products = RESTFactory.restCall('get', 'webresources/product', $scope.product.vendorno, '').then(function(products) {
                if (products.length > 0)
                {
                    $scope.status = "Products for abc@supply.com Retrieved!"
                    $scope.products = products;
                    $scope.pickedVendor = true;
                }
                else {
                    $scope.status = 'No Products';
                }
            });
        };

        /*
         * Name: add
         * Params: None 
         * Returns: None
         * Description: Adds a po to details table
         */
        $scope.add = function() {
            $scope.productTrue = true;
            $scope.qtyTrue = true;
            //Validate everything has been chosen
            if (!$scope.productName)
            {
                $scope.productTrue = false;
                $scope.status = "Please pick a product!!";
            }
            else if (!$scope.quantity)
            {
                $scope.qtyTrue = false;
                $scope.status = "Please pick a quantity!";
            }
            //Check if EOQ is 0
            if ($scope.productTrue === true && $scope.qtyTrue === true) {
                $scope.totalForm = true; 
                $scope.addPO = true;
                $scope.pdf = false;
                $scope.noProducts = false;
                if ($scope.quantity === '0') {
                    //Check if exists in array and delete it
                    for (var i = 0; i < $scope.items.length; i++) {
                        if ($scope.items[i].productName === $scope.productName) {
                            //Remove from the array
                            $scope.items.splice(i, 1);
                        }
                        //check if array list is empty if so hide panel
                        if ($scope.items.length === 0)
                        {
                            $scope.status = "Please add a product";
                            $scope.noProducts = false;
                            $scope.totalForm = false;
                        }
                        //Recalculate everything
                        $scope.calculate();
                    }
                    if($scope.noProducts === false && $scope.items.length === 0)
                    {
                        $scope.totalForm = false;
                        $scope.noProducts = true;
                    }
                }
                else if ($scope.quantity === 'EOQ')
                {
                    //Find what the product EOQ is
                    for (var i = 0; i < $scope.products.length; i++) {
                        if ($scope.products[i].productName === $scope.productName) {
                            $scope.populateArray($scope.products[i].eoq);
                            $scope.status = "Item Added!";
                            $scope.calculate();
                        }
                    }
                }
                else {
                    $scope.populateArray($scope.quantity);
                    $scope.status = "Item Added!";
                    $scope.calculate();
                }
            }
        };

        /*
         * Name: populateArray
         * Params: None 
         * Returns: None
         * Description: pPopulates the array
         */
        $scope.populateArray = function(qty) {
            //Check if product is already in the array and update it
            for (var i = 0; i < $scope.items.length; i++) {
                if ($scope.items[i].productName === $scope.productName) {
                    //Remove from the array
                    $scope.items.splice(i, 1);
                }
            }

            //add products to the array 
            for (var i = 0; i < $scope.products.length; i++) {
                //Check if prodname exists in array and delete it
                if ($scope.products[i].productName === $scope.productName) {
                    $scope.items.push({
                        productName: $scope.products[i].productName,
                        costPrice: $scope.products[i].costPrice,
                        price: $scope.products[i].costPrice,
                        qty: qty,
                        productCode: $scope.products[i].productCode
                    });
                }
            }
        };

        /*
         * Name: calculate
         * Params: None 
         * Returns: None
         * Description: Calculates the extended, sub, and tax
         */
        $scope.calculate = function() {
            $scope.sub = 0;
            $scope.tax = 0;
            $scope.total = 0;
            for (var i = 0; i < $scope.items.length; i++)
            {
                $scope.Extended = $scope.items[i].costPrice * $scope.items[i].qty;

                $scope.sub += $scope.Extended;

                $scope.tax += $scope.Extended * 0.13;
            }
        };

        /*
         * Name: createPO
         * Params: None 
         * Returns: None
         * Description: Creates the product order
         */
        $scope.createPO = function() {
            $scope.status = "Wait...";
            var PODTO = new Object();
            PODTO.total = $scope.total;
            PODTO.vendorno = $scope.product.vendorno;
            PODTO.items = $scope.items;
            $scope.PO = RESTFactory.restCall('post', 'webresources/purchaseorder', $scope.product.vendorno, PODTO).then(function(results) {
                if (results.length > 0) { //object returned it worked, number                    
                    $scope.status = "PO " + results + " Added!";
                    $scope.pono = results;
                    $scope.generated = false;
                    $scope.generatorForm.$setPristine();
                    $scope.addPO = false; 
                    $scope.pdf = true;
                }
                else {
                    alert(JSON.stringify(results));
                    $scope.status = 'PO not created - ' + results;
                    $scope.addPO = true; 
                    $scope.pdf = false;
                }
            }, function(reason) { //error
                $scope.status = 'PO not created - ' + reason;
                $scope.addPO = true; 
                $scope.pdf = false;
            });
        };
        
        $scope.viewPdf = function() {
          $window.location.href = 'POPDFServlet?po=' + $scope.pono; 
        };//view pdf

    };
    app.controller('GeneratorCtrl', ['$scope', '$window', 'RESTFactory', GeneratorCtrl]);

})(angular.module('case1'));
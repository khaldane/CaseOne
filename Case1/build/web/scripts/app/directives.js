(function(app) {
    app.directive('confirmClick', function() { //delete confirmation
        return {
            restrict: 'A',
            priority: 1,
            terminal: true,
            link: function (scope, element, attr) {
                var msg = attr.confirmationNeeded || "Really Delete?";
                var clickAction = attr.ngClick;
                element.bind('click', function() {
                   if(window.confirm(msg)){
                       scope.$apply(clickAction);
                   } 
                });
            }
        };
    });
}(angular.module('case1')));
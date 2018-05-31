(function () {
    'use strict';

    angular
        .module('saturnApp')
        .controller('SaturnVaultPwdGenController', SaturnVaultPwdGenController);

    SaturnVaultPwdGenController.$inject = ['$timeout', '$scope', '$uibModalInstance'];

    function SaturnVaultPwdGenController($timeout, $scope, $uibModalInstance) {
        var vm = this;

        vm.clear = clear;
        vm.generate = generate;
        vm.save = save;

        vm.genOptions = {
            length: 8,
            lower: true,
            upper: true,
            digits: true,
            special: true,
            repetition: false
        };

        vm.chars = {
            lower: "qwertyuiopasdfghjklzxcvbnm",
            upper: "QWERTYUIOPASDFGHJKLZXCVBNM",
            digits: "0123456789",
            special: "!@#$%-_"
        };

        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function generate() {
            var chars = "";
            vm.password = "";

            if (vm.genOptions.lower) {
                chars += vm.chars.lower;
            }

            if (vm.genOptions.upper) {
                chars += vm.chars.upper;
            }

            if (vm.genOptions.digits) {
                chars += vm.chars.digits;
            }

            if (vm.genOptions.special) {
                chars += vm.chars.special;
            }

            for (var i = 0; i < vm.genOptions.length; i++) {
                var position = Math.round(Math.random() * chars.length);

                if (vm.genOptions.repetition) {
                    if (vm.password.indexOf(chars[position]) === -1) {
                        vm.password += chars[position];
                    }
                } else {
                    vm.password += chars[position];
                }
            }
        }

        function save() {
            $scope.$emit('saturnApp:SaturnVaultPwdGen', vm.password);
            $uibModalInstance.close(vm.password);
        }
    }
})();

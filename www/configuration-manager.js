var exec = require('cordova/exec');
var PLUGIN_NAME = 'ConfigurationManager';
var ConfigurationManager = {

    launchSettings: (param, cb, error) => {
        exec(cb, error, PLUGIN_NAME, 'launchSettings', [param]);
    }

};
module.exports = ConfigurationManager;
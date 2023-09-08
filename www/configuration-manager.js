var exec = require('cordova/exec');
var PLUGIN_NAME = 'ConfigurationManager';
var ConfigurationManager = {
    // param : string | {action: string; extras?: {key: string; value: any}[]}
    launchSettings: (param, cb, error) => {
        exec(cb, error, PLUGIN_NAME, 'launchSettings', [param]);
    }

};
module.exports = ConfigurationManager;
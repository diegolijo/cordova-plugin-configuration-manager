# cordova-plugin-configuration-manager

This Cordova plugin allows you to launch system settings on Android devices. You can open settings such as Wi-Fi, Bluetooth, sound, display, location, application settings, and many more directly from your Cordova application.

## Installation

You can install this plugin using Cordova CLI or npm. Make sure you have Cordova installed globally on your machine. Then, navigate to the root directory of your Cordova project and run the following command:

```bash
cordova plugin add cordova-plugin-configuration-manager
```

## Usage

To use the plugin, make sure that the device and platform are Android compatible. 
Then, you can call the `launchSettings` method of the `cordova.plugins.configurationManager` object to open a specific setting.

param : string | {action: string; extras?: {key: string; value: any}[]}

Here is an example of how to launch the Wi-Fi settings:

```javascript
cordova.plugins.ConfigurationManager.launchSettings(
    "android.settings.ACTION_WIFI_SETTINGS",
    () => {
        console.log("Setting launched successfully");
    },
    (error) => {
        console.error("Error launching setting: " + error);
    });
```
Replace `"android.settings.ACTION_WIFI_SETTINGS"` with the specific intent action you want to launch. You can refer to the list of tested intent actions in the [settings-android.json](./settings-android.json) file. Alternatively, you can use any other valid intent action for launching the desired activity.


Another example. 

```javascript
    cordova.plugins.ConfigurationManager.launchSettings({
            action: 'android.intent.action.SET_TIMER',
            extras: [
                {
                    key: 'android.intent.extra.alarm.LENGTH',
                    value: 60 * 15
                },
                {
                    key: 'android.intent.extra.alarm.SKIP_UI',
                    value: true
                }
            ]
        },
        () => {
            console.log("successfully");
        },
        (error) => {
            console.error(error);
        });
```

This example demonstrates how to use the launchSettings method with a specific intent action and extras. 
It sets the timer length to 15 minutes and enables the skipping of the UI. 

## Contribution

If you wish to contribute to this plugin, feel free to fork the repository, make your changes, and submit a pull request. We appreciate any kind of contribution, whether it's reporting issues, sending fixes, or adding new features.

## License

This plugin is distributed under the MIT License. See the [LICENSE](./LICENSE) file for more information.

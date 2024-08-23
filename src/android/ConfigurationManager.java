
package com.vayapedal.configuration;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ConfigurationManager extends CordovaPlugin {

    @Override
    protected void pluginInitialize() {
        super.pluginInitialize();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean execute(
            String action,
            JSONArray args,
            CallbackContext callbackContext
    ) throws JSONException {
        switch (action) {
            case "launchSettings":
                Object actionParam = args.get(0);
                String settingAction;
                String settingCategory = null;
                int settingFlag = 0;
                JSONArray extras = new JSONArray();

                if (actionParam instanceof String) {
                    settingAction = (String) actionParam;
                } else if (actionParam instanceof JSONObject) {
                    JSONObject params = (JSONObject) actionParam;
                    settingAction = params.optString("action");
                    extras = params.optJSONArray("extras");
                    category = params.optString("category");
                    flag = params.optInt("flag");
                } else {
                    callbackContext.error("Invalid action parameter");
                    return false;
                }

                if (!settingAction.isEmpty()) {
                    launchSettings(callbackContext, settingAction, extras, category, flag);
                } else {
                    return getAndroidSettings(callbackContext);
                }
                return true;
            case "canScheduleExactAlarms":
                canScheduleExactAlarms(callbackContext);
                return true;
        }
        return false;
    }

    private void canScheduleExactAlarms(CallbackContext callbackContext) {
        boolean isEnable = true;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) cordova.getActivity().getBaseContext().getSystemService(Context.ALARM_SERVICE);
            isEnable = alarmManager.canScheduleExactAlarms();
        }
        callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, isEnable));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void launchSettings(
            CallbackContext callbackContext,
            String settingAction,
            JSONArray extras,
            String settingCategory,
            int settingFlag
    ) {
        boolean success = false;
        StringBuilder exceptionMessages = new StringBuilder();
        String successMessage = "";

        for (int attempt = 0; attempt <= 2; attempt++) {
            try {
                Intent intent = new Intent(settingAction);
                if (attempt == 0) {
                    intent.setData(
                            Uri.fromParts(
                                    "package",
                                    cordova.getActivity().getPackageName(),
                                    null
                            )
                    );
                } else if (attempt == 1) {
                    intent.putExtra(
                            Settings.EXTRA_APP_PACKAGE,
                            cordova.getActivity().getPackageName()
                    );
                }

                if (extras != null && extras.length() > 0) {
                    addExtrasToIntent(intent, extras);
                }

                if (settingCategory != null) {
                    intent.addCategory(settingCategory);
                }

                if (settingFlag != 0) {
                    intent.setFlags(settingFlag);
                }

                cordova.getActivity().startActivity(intent);

                success = true;
                switch (attempt) {
                    case 0:
                        successMessage = "Ok. Package URI";
                        break;
                    case 1:
                        successMessage = "Ok, Package extras";
                        break;
                    case 2:
                        successMessage = "Ok, No Package";
                        break;
                }
                break;
            } catch (Exception e) {
                exceptionMessages.append(e.getMessage()).append("\n");
            }
        }

        if (success) {
            Log.e("***********", successMessage);
            PluginResult result = new PluginResult(
                    PluginResult.Status.OK,
                    successMessage
            );
            callbackContext.sendPluginResult(result);
            callbackContext.success();
        } else {
            callbackContext.error(
                    "Error al abrir la configuración: " + exceptionMessages.toString()
            );
        }
    }

    private void addExtrasToIntent(Intent intent, JSONArray extras)
            throws JSONException {
        for (int i = 0; i < extras.length(); i++) {
            JSONObject extra = extras.getJSONObject(i);
            String key = extra.getString("key");
            Object value = extra.get("value");
            if (value instanceof Integer) {
                intent.putExtra(key, (Integer) value);
            } else if (value instanceof String) {
                intent.putExtra(key, (String) value);
            } else if (value instanceof Boolean) {
                intent.putExtra(key, (Boolean) value);
            } else if (value instanceof Float) {
                intent.putExtra(key, (Float) value);
            } else if (value instanceof Double) {
                intent.putExtra(key, (Double) value);
            } else if (value instanceof Long) {
                intent.putExtra(key, (Long) value);
            }
        }
    }

    private boolean getAndroidSettings(CallbackContext callbackContext) {
        JSONArray jsonSettings = new JSONArray();

        jsonSettings.put(
                createSettingObject(
                        "android.settings.DISPLAY_SETTINGS",
                        "Configuración general de pantalla",
                        false,
                        null
                )
        );
        jsonSettings.put(
                createSettingObject(
                        "android.settings.LOCALE_SETTINGS",
                        "Configuración regional",
                        false,
                        null
                )
        );
        jsonSettings.put(
                createSettingObject(
                        "android.settings.INTERNAL_STORAGE_SETTINGS",
                        "Resumen de almacenamiento interno",
                        false,
                        null
                )
        );
        jsonSettings.put(
                createSettingObject(
                        "android.settings.APPLICATION_SETTINGS",
                        "Lista de aplicaciones",
                        false,
                        null
                )
        );
        jsonSettings.put(
                createSettingObject(
                        "android.settings.SOUND_SETTINGS",
                        "Configuración de sonido",
                        false,
                        null
                )
        );
        jsonSettings.put(
                createSettingObject(
                        "android.settings.WIFI_SETTINGS",
                        "Configuración de Wi-Fi",
                        false,
                        null
                )
        );
        jsonSettings.put(
                createSettingObject(
                        "android.settings.WIFI_IP_SETTINGS",
                        "Configuración de IP de Wi-Fi",
                        false,
                        null
                )
        );
        jsonSettings.put(
                createSettingObject(
                        "android.settings.BLUETOOTH_SETTINGS",
                        "Configuración de Bluetooth",
                        false,
                        null
                )
        );
        jsonSettings.put(
                createSettingObject(
                        "android.settings.LOCATION_SOURCE_SETTINGS",
                        "Configuración de ubicación",
                        false,
                        null
                )
        );
        jsonSettings.put(
                createSettingObject(
                        "android.settings.REQUEST_SCHEDULE_EXACT_ALARM",
                        "Programar alarmas",
                        false,
                        "com.android.alarm.permission.SET_ALARM"
                )
        );
        jsonSettings.put(
                createSettingObject(
                        "android.settings.USAGE_ACCESS_SETTINGS",
                        "Configuración de acceso a uso",
                        false,
                        null
                )
        );
        jsonSettings.put(
                createSettingObject(
                        "android.settings.APP_SEARCH_SETTINGS",
                        "Búsqueda en la configuración de la aplicación",
                        false,
                        null
                )
        );
        jsonSettings.put(
                createSettingObject(
                        "android.settings.BATTERY_SAVER_SETTINGS",
                        "Configuración del ahorro de batería",
                        false,
                        null
                )
        );
        jsonSettings.put(
                createSettingObject(
                        "android.settings.DREAM_SETTINGS",
                        "Configuración de la pantalla de inicio",
                        false,
                        null
                )
        );
        jsonSettings.put(
                createSettingObject(
                        "android.provider.AlarmClock.SHOW_ALARMS",
                        "Configuración de la alarma",
                        false,
                        "com.android.alarm.permission.SET_ALARM"
                )
        );
        jsonSettings.put(
                createSettingObject(
                        "android.settings.AIRPLANE_MODE_SETTINGS",
                        "Configuración del modo avión",
                        false,
                        null
                )
        );
        jsonSettings.put(
                createSettingObject(
                        "android.settings.ACCESSIBILITY_SETTINGS",
                        "Configuración de accesibilidad",
                        false,
                        null
                )
        );
        jsonSettings.put(
                createSettingObject(
                        "android.settings.MANAGE_DEFAULT_APPS_SETTINGS",
                        "Configuración de aplicaciones predeterminadas",
                        false,
                        null
                )
        );
        jsonSettings.put(
                createSettingObject(
                        "android.settings.MANAGE_ALL_APPLICATIONS_SETTINGS",
                        "Todas las aplicaciones",
                        false,
                        null
                )
        );
        jsonSettings.put(
                createSettingObject(
                        "android.settings.APPLICATION_DEVELOPMENT_SETTINGS",
                        "Opciones de desarrollo de aplicaciones",
                        false,
                        null
                )
        );
        jsonSettings.put(
                createSettingObject(
                        "android.settings.ZEN_MODE_PRIORITY_SETTINGS",
                        "Configuración de prioridad del modo No molestar",
                        false,
                        null
                )
        );
        jsonSettings.put(
                createSettingObject(
                        "android.settings.MANAGE_OVERLAY_PERMISSION",
                        "Mostrar encima de otras aplicaciones",
                        false,
                        null
                )
        );
        jsonSettings.put(
                createSettingObject(
                        "android.settings.REQUEST_MANAGE_MEDIA",
                        "Configuración de permisos de gestión de medios",
                        false,
                        null
                )
        );
        jsonSettings.put(
                createSettingObject(
                        "android.settings.APP_NOTIFICATION_SETTINGS",
                        "Configuración de notificaciones",
                        true,
                        null
                )
        );
        jsonSettings.put(
                createSettingObject(
                        "android.settings.APP_NOTIFICATION_BUBBLE_SETTINGS",
                        "Configuración de burbujas de notificaciones",
                        true,
                        null
                )
        );
        jsonSettings.put(
                createSettingObject(
                        "android.settings.APPLICATION_DETAILS_SETTINGS",
                        "Configuración general de la aplicación",
                        true,
                        null
                )
        );
        jsonSettings.put(
                createSettingObject(
                        "android.settings.MANAGE_UNKNOWN_APP_SOURCES",
                        "Configuración de permisos para instalar aplicaciones de fuentes desconocidas",
                        true,
                        null
                )
        );
        jsonSettings.put(
                createSettingObject(
                        "android.settings.APP_OPEN_BY_DEFAULT_SETTINGS",
                        "Abrir enlaces permitidos",
                        true,
                        null
                )
        );
        jsonSettings.put(
                createSettingObject(
                        "android.settings.MANAGE_APP_ALL_FILES_ACCESS_PERMISSION",
                        "Acceso a todos los archivos",
                        true,
                        "android.permission.MANAGE_EXTERNAL_STORAGE"
                )
        );

        String errorMessage1 =
                "Please provide one of the following values as a parameter or any other value from android.settings";
        PluginResult errorResult = new PluginResult(
                PluginResult.Status.ERROR,
                errorMessage1
        );
        errorResult.setKeepCallback(true);
        callbackContext.sendPluginResult(errorResult);
        callbackContext.sendPluginResult(
                new PluginResult(PluginResult.Status.ERROR, jsonSettings)
        );
        return true;
    }

    private JSONObject createSettingObject(
            String action,
            String description,
            boolean app,
            String permission
    ) {
        JSONObject settingObject = new JSONObject();
        try {
            settingObject.put("value", action);
            settingObject.put("description", description);
            settingObject.put("permission", permission);
            settingObject.put("app", app);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return settingObject;
    }
    // ****************** sin paquete *****************

    // Settings.ACTION_DISPLAY_SETTINGS                         -> config general de pantalla
    // Settings.ACTION_LOCALE_SETTINGS                          -> config regional
    // Settings.ACTION_INTERNAL_STORAGE_SETTINGS                -> resumen almacenamiento
    // Settings.ACTION_APPLICATION_SETTINGS                     -> lista de aplicaciones
    // Settings.ACTION_SOUND_SETTINGS                           -> config sonido
    // Settings.ACTION_WIFI_SETTINGS
    // Settings.ACTION_WIFI_IP_SETTINGS
    // Settings.ACTION_BLUETOOTH_SETTINGS
    // Settings.ACTION_LOCATION_SOURCE_SETTINGS
    // Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM             -> programar alarmas
    // Settings.ACTION_USAGE_ACCESS_SETTINGS
    // Settings.ACTION_APP_SEARCH_SETTINGS                      -> buscar en la configuracion de la app
    // Settings.ACTION_BATTERY_SAVER_SETTINGS                   -> uso bateria
    // Settings.ACTION_DREAM_SETTINGS                           -> salva pantallas
    // AlarmClock.ACTION_SHOW_ALARMS                            -> configuracion de alarma    <uses-permission android:name="com.android.alarm.permission.SET_ALARM" />
    // Settings.ACTION_AIRPLANE_MODE_SETTINGS                   -> modo avión
    // Settings.ACTION_ACCESSIBILITY_SETTINGS                   -> accesivilidad
    // Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS             -> aplicaciones predeterminadas en Android
    // Settings.ACTION_MANAGE_ALL_APPLICATIONS_SETTINGS         -> Todas las apps
    // Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS         -> Opciones de sesarrollador
    // Settings.ACTION_ZEN_MODE_PRIORITY_SETTINGS               -> No molestar
    // Settings.ACTION_MANAGE_OVERLAY_PERMISSION                -> Mostrar encima de otras apps
    // Settings.ACTION_REQUEST_MANAGE_MEDIA                     -> Aplicacion con permiso gestion multimedia

    //************** necesitan paquete de app **************
    // Settings.ACTION_APP_NOTIFICATION_SETTINGS               -> (Extra)  notificaciones
    // Settings.ACTION_APP_NOTIFICATION_BUBBLE_SETTINGS        -> (Extra)  burbujas de notificaciones
    // Settings.ACTION_APPLICATION_DETAILS_SETTINGS            -> (URI)    config general de la app
    // Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES              -> (URI)    permisos instalar apps fuentes desconocidas
    // Settings.ACTION_APP_OPEN_BY_DEFAULT_SETTINGS            -> (URI)    Abrir enlaces permitidos
    // Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION  -> (URI)    Acceso a todos los archivos necestita   <uses-permission android:name="android.permission.MANAGE_EXTERNAL_STORAGE" />

}
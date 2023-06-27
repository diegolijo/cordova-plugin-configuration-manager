package com.vayapedal.configuration;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import androidx.annotation.RequiresApi;
import java.util.Calendar;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ConfigurationManager extends CordovaPlugin {

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
    if (action.equals("launchSettings")) {
      String settingAction = args.optString(0);

      if (settingAction != null && !settingAction.isEmpty()) {
        launchSettings(callbackContext, settingAction);
        callbackContext.success();
      } else {
        return getAndroidSettings(callbackContext);
      }
      return true;
    }
    return false;
  }

  @RequiresApi(api = Build.VERSION_CODES.O)
  private void launchSettings(
    CallbackContext callbackContext,
    String settingAction
  ) {
    boolean success = false;
    StringBuilder exceptionMessages = new StringBuilder();
    String successMessage = "";
    for (int attempt = 0; attempt <= 2; attempt++) {
      try {
        Intent intent = new Intent(settingAction);
        if (attempt == 1) {
          intent.putExtra(
            Settings.EXTRA_APP_PACKAGE,
            cordova.getActivity().getPackageName()
          );
        } else if (attempt == 2) {
          intent.setData(
            Uri.fromParts(
              "package",
              cordova.getActivity().getPackageName(),
              null
            )
          );
        }
        cordova.getActivity().startActivity(intent);
        success = true;

        if (attempt == 0) {
          successMessage = "Llamada sin extras";
        } else if (attempt == 1) {
          successMessage = "Llamada con extras";
        } else {
          successMessage = "Llamada con URI";
        }

        break;
      } catch (Exception e) {
        exceptionMessages.append(e.getMessage()).append("\n");
      }
    }
    if (success) {
      callbackContext.success();
    } else {
      callbackContext.error(
        "Error al abrir la configuración: " + exceptionMessages.toString()
      );
    }
  }

  private boolean getAndroidSettings(CallbackContext callbackContext) {
    JSONArray jsonSettings = new JSONArray();

    jsonSettings.put(
      createSettingObject(
        "android.settings.ACTION_DISPLAY_SETTINGS",
        "Configuración general de pantalla",
        false,
        null
      )
    );
    jsonSettings.put(
      createSettingObject(
        "android.settings.ACTION_LOCALE_SETTINGS",
        "Configuración regional",
        false,
        null
      )
    );
    jsonSettings.put(
      createSettingObject(
        "android.settings.ACTION_INTERNAL_STORAGE_SETTINGS",
        "Resumen de almacenamiento interno",
        false,
        null
      )
    );
    jsonSettings.put(
      createSettingObject(
        "android.settings.ACTION_APPLICATION_SETTINGS",
        "Lista de aplicaciones",
        false,
        null
      )
    );
    jsonSettings.put(
      createSettingObject(
        "android.settings.ACTION_SOUND_SETTINGS",
        "Configuración de sonido",
        false,
        null
      )
    );
    jsonSettings.put(
      createSettingObject(
        "android.settings.ACTION_WIFI_SETTINGS",
        "Configuración de Wi-Fi",
        false,
        null
      )
    );
    jsonSettings.put(
      createSettingObject(
        "android.settings.ACTION_WIFI_IP_SETTINGS",
        "Configuración de IP de Wi-Fi",
        false,
        null
      )
    );
    jsonSettings.put(
      createSettingObject(
        "android.settings.ACTION_BLUETOOTH_SETTINGS",
        "Configuración de Bluetooth",
        false,
        null
      )
    );
    jsonSettings.put(
      createSettingObject(
        "android.settings.ACTION_LOCATION_SOURCE_SETTINGS",
        "Configuración de ubicación",
        false,
        null
      )
    );
    jsonSettings.put(
      createSettingObject(
        "android.settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM",
        "Programar alarmas",
        false,
        "com.android.alarm.permission.SET_ALARM"
      )
    );
    jsonSettings.put(
      createSettingObject(
        "android.settings.ACTION_USAGE_ACCESS_SETTINGS",
        "Configuración de acceso a uso",
        false,
        null
      )
    );
    jsonSettings.put(
      createSettingObject(
        "android.settings.ACTION_APP_SEARCH_SETTINGS",
        "Búsqueda en la configuración de la aplicación",
        false,
        null
      )
    );
    jsonSettings.put(
      createSettingObject(
        "android.settings.ACTION_BATTERY_SAVER_SETTINGS",
        "Configuración del ahorro de batería",
        false,
        null
      )
    );
    jsonSettings.put(
      createSettingObject(
        "android.settings.ACTION_DREAM_SETTINGS",
        "Configuración de la pantalla de inicio",
        false,
        null
      )
    );
    jsonSettings.put(
      createSettingObject(
        "android.provider.AlarmClock.ACTION_SHOW_ALARMS",
        "Configuración de la alarma",
        false,
        "com.android.alarm.permission.SET_ALARM"
      )
    );
    jsonSettings.put(
      createSettingObject(
        "android.settings.ACTION_AIRPLANE_MODE_SETTINGS",
        "Configuración del modo avión",
        false,
        null
      )
    );
    jsonSettings.put(
      createSettingObject(
        "android.settings.ACTION_ACCESSIBILITY_SETTINGS",
        "Configuración de accesibilidad",
        false,
        null
      )
    );
    jsonSettings.put(
      createSettingObject(
        "android.settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS",
        "Configuración de aplicaciones predeterminadas",
        false,
        null
      )
    );
    jsonSettings.put(
      createSettingObject(
        "android.settings.ACTION_MANAGE_ALL_APPLICATIONS_SETTINGS",
        "Todas las aplicaciones",
        false,
        null
      )
    );
    jsonSettings.put(
      createSettingObject(
        "android.settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS",
        "Opciones de desarrollo de aplicaciones",
        false,
        null
      )
    );
    jsonSettings.put(
      createSettingObject(
        "android.settings.ACTION_ZEN_MODE_PRIORITY_SETTINGS",
        "Configuración de prioridad del modo No molestar",
        false,
        null
      )
    );
    jsonSettings.put(
      createSettingObject(
        "android.settings.ACTION_MANAGE_OVERLAY_PERMISSION",
        "Mostrar encima de otras aplicaciones",
        false,
        null
      )
    );
    jsonSettings.put(
      createSettingObject(
        "android.settings.ACTION_REQUEST_MANAGE_MEDIA",
        "Configuración de permisos de gestión de medios",
        false,
        null
      )
    );
    jsonSettings.put(
      createSettingObject(
        "android.settings.ACTION_APP_NOTIFICATION_SETTINGS",
        "Configuración de notificaciones",
        true,
        null
      )
    );
    jsonSettings.put(
      createSettingObject(
        "android.settings.ACTION_APP_NOTIFICATION_BUBBLE_SETTINGS",
        "Configuración de burbujas de notificaciones",
        true,
        null
      )
    );
    jsonSettings.put(
      createSettingObject(
        "android.settings.ACTION_APPLICATION_DETAILS_SETTINGS",
        "Configuración general de la aplicación",
        true,
        null
      )
    );
    jsonSettings.put(
      createSettingObject(
        "android.settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES",
        "Configuración de permisos para instalar aplicaciones de fuentes desconocidas",
        true,
        null
      )
    );
    jsonSettings.put(
      createSettingObject(
        "android.settings.ACTION_APP_OPEN_BY_DEFAULT_SETTINGS",
        "Abrir enlaces permitidos",
        true,
        null
      )
    );
    jsonSettings.put(
      createSettingObject(
        "android.settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION",
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


}

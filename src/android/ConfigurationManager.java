package com.vayapedal.ConfigurationManager;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;

public class ConfigurationManager extends CordovaPlugin {

    private Context context;

    @Override
    protected void pluginInitialize() {
        super.pluginInitialize();
        this.context = cordova.getActivity().getApplicationContext();
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("launchSettings")) {
            launchSettings(callbackContext);
            return true;
        }
        // Agrega aquí otros métodos de ejecución según tus necesidades
        return false;
    }

    private void launchSettings(CallbackContext callbackContext) {
        try {
            Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                    .putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
            cordova.getActivity().startActivity(intent);
            callbackContext.success();
        } catch (Exception e) {
            callbackContext.error("Error al abrir la configuración de notificaciones: " + e.getMessage());
        }
    }

    // Agrega aquí otros métodos relacionados con la gestión de la configuración y notificaciones

}

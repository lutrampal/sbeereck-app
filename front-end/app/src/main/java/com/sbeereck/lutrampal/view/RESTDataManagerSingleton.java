package com.sbeereck.lutrampal.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.sbeereck.lutrampal.controller.RESTDataManager;
import com.sbeereck.lutrampal.controller.RESTfulDataManager;
import com.yakivmospan.scytale.Crypto;
import com.yakivmospan.scytale.Options;
import com.yakivmospan.scytale.Store;

import javax.crypto.SecretKey;

/**
 * Created by lutrampal on 15/02/18 for S'Beer Eck.
 */

class RESTDataManagerSingleton {
    private static RESTDataManager instance = null;
    private RESTDataManagerSingleton() { }
    
    static RESTDataManager getDataManager(Context context) {
        if (instance != null) {
            return instance;
        }
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.app_preferences), Context.MODE_PRIVATE);
        String serverAddress = sharedPref.getString(
                context.getString(R.string.saved_server_address), "");
        if (serverAddress.trim().isEmpty()) {
            Toast.makeText(context, context.getString(R.string.auth_not_configured_error),
                    Toast.LENGTH_SHORT).show();
            return null;
        }

        Store store = new Store(context);
        if (!store.hasKey(context.getString(R.string.sbeereck_key))) {
            Toast.makeText(context, context.getString(R.string.auth_not_configured_error),
                    Toast.LENGTH_SHORT).show();
            return null;
        }

        SecretKey key = store.getSymmetricKey(
                context.getString(R.string.sbeereck_key), null);
        Crypto crypto = new Crypto(Options.TRANSFORMATION_SYMMETRIC);
        String decrPass = crypto.decrypt(sharedPref.getString(
                context.getString(R.string.saved_server_password), ""), key);

        return new RESTfulDataManager(serverAddress, decrPass);
    } 
}

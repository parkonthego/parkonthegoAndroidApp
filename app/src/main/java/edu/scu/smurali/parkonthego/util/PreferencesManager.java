package edu.scu.smurali.parkonthego.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by chshi on 5/22/2016.
 */
public class PreferencesManager {

    private static PreferencesManager preferencesManager = null;
    private Context mContext;
    private SharedPreferences sharedPreferences;
    private String NAME_OF_SHARED_PERFERENCE = "PARKONTHEGO";

    public PreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(NAME_OF_SHARED_PERFERENCE, Context.MODE_PRIVATE);
    }

    public static PreferencesManager getInstance(Context context) {
        if (preferencesManager == null) {
            preferencesManager = new PreferencesManager(context);
        }
        return preferencesManager;
    }

    public void updateUserId(int id) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("Id", id);
        editor.commit();
    }

    public int getUserId() {
        return sharedPreferences.getInt("Id", -1);
    }

    public void updateUserName(String name) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userName", name);
        editor.commit();
    }

    public String getUserName(String name) {
        return sharedPreferences.getString("name", null);
    }


}

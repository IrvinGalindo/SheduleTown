package com.iogb.sheduletown;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by L55-C5211R on 07/11/2017.
 */

public class Session {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context context;
    public static final String INFO="info";
    public static final String LOGGED="logged";

    public Session(Context context) {
        this.context = context;
        prefs=context.getSharedPreferences("base",Context.MODE_PRIVATE);
        editor=prefs.edit();

    }

    public void setLoggedIn(boolean loggedIn,String employee)
    {
        editor.putBoolean(LOGGED,loggedIn);
        editor.putString(INFO,employee);
        editor.commit();
    }

    public boolean loggedIn()
    {
        return prefs.getBoolean(LOGGED,false);
    }
    public void clear(){ prefs.edit().clear().commit();  }
    public String getFullInfo()
    {
        return prefs.getString(INFO,null);
    }

}

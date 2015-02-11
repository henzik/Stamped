package com.yan.stamped;

import android.app.Application;

/**
 * Created by Henrik on 18/09/2014.
 */
public class Main extends Application {

    private String broadcast = "This is a message from a: "+android.os.Build.MODEL;

    public String getBroadcast() {
        return broadcast;
    }

    public void setBroadcast(String someVariable) {
        this.broadcast = someVariable;
    }
}


package com.zprocessapps.mycustomcoupon.objects;

import android.app.Application;

import com.zprocessapps.mycustomcoupon.MCCDataStore;

/**
 * Created by jasonwolfe on 3/9/16.
 */
public class C_Application extends Application{

    @Override
    public void onCreate()
    {
        super.onCreate();

        // Initialize the singletons so their instances
        // are bound to the application process.
        initSingletons();
    }

    protected void initSingletons()
    {
        // Initialize the instance of MySingleton
        MCCDataStore.initInstance();
    }

    public void customAppMethod()
    {
        // Custom application method
    }

}

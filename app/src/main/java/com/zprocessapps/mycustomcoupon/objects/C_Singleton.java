package com.zprocessapps.mycustomcoupon.objects;

/**
 * Created by jasonwolfe on 3/9/16.
 */

public class C_Singleton
{
    private static C_Singleton instance;

    public String customVar;

    public static void initInstance()
    {
        if (instance == null)
        {
            // Create the instance
            instance = new C_Singleton();
        }
    }

    public static C_Singleton getInstance()
    {
        // Return the instance
        return instance;
    }

    private C_Singleton()
    {
        // Constructor hidden because this is a singleton
    }

    public void customSingletonMethod()
    {
        // Custom method
    }
}
package com.cloudpos.jniinterface;

import android.util.Log;

import java.io.File;

import dalvik.system.DexClassLoader;

public class JNILoad {

    private static final String TAG = "JNILoad";

    static boolean checkClassPath() {
        ClassLoader classLoader = JNILoad.class.getClassLoader();
        if (classLoader instanceof DexClassLoader) {
            DexClassLoader dex = (DexClassLoader)classLoader;
            String dexListStr = dex.toString(), jarPath = "/data/cloudpossdk/cloudpossdkimpl.jar";
            String [] tests = dexListStr.split(jarPath);
            if (tests!= null && tests.length > 1)
                return true;
            else {
                Log.e(TAG, "length : " + tests.length);
                Log.i(TAG, "dex : " + dex);
            }
        }
        Log.i(TAG, "classloader" + classLoader);
        return false;
    }

    public static void jniLoad(String libname) {
        String external = "/data/cloudpossdk/libs/lib" + libname + ".so";
        File file = new File (external);
        try {
            if (checkClassPath() && file.exists()) {
                Log.i(TAG, "load " + external);
                System.load(external);
            }
            else {
                Log.i(TAG, "load " + libname);
                System.loadLibrary(libname);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "load library failed!");
        }
    }
}

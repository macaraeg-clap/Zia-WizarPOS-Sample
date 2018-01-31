package com.cloudpos.jniinterface;

public class CashDrawerInterface {

    static {
        String fileName = "jni_cloudpos_cashdrawer";
        JNILoad.jniLoad(fileName);
    }

    public synchronized native static int open();

    public synchronized native static int close();

    public synchronized native static int kickOut();
}

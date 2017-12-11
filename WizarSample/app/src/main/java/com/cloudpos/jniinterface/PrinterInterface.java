package com.cloudpos.jniinterface;

public class PrinterInterface {

    static {
        String fileName = "jni_cloudpos_printer";
        JNILoad.jniLoad(fileName);
    }

    public synchronized native static int open();

    public native static int close();

    public synchronized native static int begin();

    public synchronized native static int end();

    public synchronized native static int write(byte arryData[], int nDataLength);

    public synchronized native static int read(byte arryData[], int nDataLength, int nTimeout);

    public synchronized native static int write(byte arryData[], int offset, int nDataLength);

    public synchronized native static int queryStatus();

    public synchronized native static int queryVoltage(int[] pCapacity, int[] pVoltage);
}

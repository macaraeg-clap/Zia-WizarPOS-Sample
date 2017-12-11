package com.cloudpos.jniinterface;

public class FingerPrintInterface {

    static {
        String fileName = "jni_cloudpos_fingerprint";
        JNILoad.jniLoad(fileName);
    }

    public synchronized native static int open();

    public native static int close();

    public synchronized native static int getFea(byte[] arryFea, int nFeaLength, int[] pRealFeaLength, int n_TimeOut_S);

    public synchronized native static int getLastImage(byte[] pImgBuffer, int nImgLength, int[] pRealImaLength, int[] pImgWidth, int[] pImgHeight);

    public synchronized native static int match(byte[] pFeaBuffer1, int nFea1Length, byte[] pFealBuffer2, int nFea2Length);

    public native static int cancel();
}

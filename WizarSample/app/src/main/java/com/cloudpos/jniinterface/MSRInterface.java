package com.cloudpos.jniinterface;

import android.util.Log;

public class MSRInterface {

    static {
        String fileName = "jni_cloudpos_msr";
        JNILoad.jniLoad(fileName);
    }

    public static int MSR_CARD_EVENT_FOUND_CARD = 0, MSR_CARD_EVENT_USER_CANCEL = 3;

    public synchronized native static int open();

    public native static int close();

    public synchronized native static int poll(int nTimeout_MS);

    public native static int getTrackError(int nTrackIndex);

    public native static int getTrackDataLength(int nTrackIndex);

    public native static int getTrackData(int nTrackIndex, byte byteArry[], int nLength);

    /*public static Object object = new Object();
    public static int eventID = -4;

    public static void callBack(int nEventID) {
        synchronized (object) {
            Log.i("MSRCard", "notify");
            eventID = nEventID;
            object.notifyAll();
        }
    }*/

    public static final int EVENT_NONE = -1;
    public static Object object = new Object();
    public static int eventID = EVENT_NONE;

    public static void callBack(int nEventID) {
        synchronized (object) {
            Log.i("MSRCard", "notify");
            eventID = nEventID;
            object.notifyAll();
        }
    }

    public static void waitForSwipe(int timeout) throws InterruptedException {
        clear();
        synchronized (object) {
            object.wait(timeout);
        }
    }

    public static void cancelWait() {
        synchronized (object) {
            object.notifyAll();
            eventID = MSR_CARD_EVENT_USER_CANCEL;
        }
    }

    public static void clear() {
        eventID = EVENT_NONE;
    }
}

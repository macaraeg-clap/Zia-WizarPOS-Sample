package com.cloudpos.jniinterface;

import android.util.Log;

public class SmartCardInterface {

    static {
        String fileName = "jni_cloudpos_smartcard";
        JNILoad.jniLoad(fileName);
    }

    public static class NotifyEvent {

        public NotifyEvent(int eventID, int slotIndex) {
            this.eventID = eventID;
            this.slotIndex = slotIndex;
        }

        public int eventID, slotIndex;
    }

    public static final String TAG = "SmartCardInterface";
    public static final int[] LOGICAL_ID = new int[] { 0, 1, 2, 3 }, LOGICAL_ID_Q1 = new int[] { 0, 1 };
    public static final int MAX_NUMBER = 4, DEFAULT_SLOT = 0, EVENT_INSERT_CARD = 0, EVENT_REMOVE_CARD = 1, EVENT_ERROR = 2, EVENT_CANCEL = 3, EVENT_NONE = -1, SLOT_INDEX_NONE = -1;

    public native static int queryMaxNumber();

    public native static int queryPresence(int nSlotIndex);

    public synchronized native static int open(int nSlotIndex);

    public native static int close(int handle);

    public synchronized native static int  setCardInfo(int handle, int nBuadrate , int nVoltage);

    public synchronized native static int powerOn(int handle, byte byteArrayATR[], SmartCardSlotInfo info);

    public synchronized native static int powerOff(int handle);

    public synchronized native static int setSlotInfo(int handle, SmartCardSlotInfo info);

    public synchronized native static int transmit(int handle, byte byteArrayAPDU[], byte byteArrayResponse[]);

    public synchronized native static int read(int handle, int nAreaType, byte[] byteArryData, int nStartAddress);

    public synchronized native static int write(int handle, int nAreaType, byte[] byteArryData, int nStartAddress);

    public synchronized native static int verify(int handle, byte byteArrayAPDU[]);

    public synchronized native static int verify_extend(int handle, byte byteArrayAPDU[], int nAddress);

    public synchronized native static int touch(int handle);

    /*public static Object objPresent = new Object(), objAbsent = new Object();
    public static SmartCardEvent event;

    public static void callBack(int slotIndex, int eventID) {
        event = new SmartCardEvent();
        event.nEventID = eventID;
        event.nSlotIndex = slotIndex;
        Log.e("DEUBG", "nEventID = " + eventID);
        Log.e("DEUBG", "nSlotIndex = " + slotIndex);
        if (eventID == SmartCardEvent.SMART_CARD_EVENT_INSERT_CARD) {
            synchronized (objPresent) {
                objPresent.notifyAll();
            }
        }
        else if (eventID == SmartCardEvent.SMART_CARD_EVENT_REMOVE_CARD) {
            synchronized (objAbsent) {
                objAbsent.notifyAll();
            }
        }
    }*/

    public static Object objPresent = new Object(), objAbsent = new Object();
    public static NotifyEvent notifyEvent;
    public static boolean isCardPresent = false;

    public static void callBack(int slotIndex, int eventID) {
        Log.i(TAG, "slotIndex = " + slotIndex + ", eventID = " + eventID);
        notifyEvent = new NotifyEvent(eventID, slotIndex);
        //notifyEvent.eventID = eventID;
        //notifyEvent.slotIndex = slotIndex;
        isCardPresent = false;
        if (eventID == EVENT_INSERT_CARD) {
            isCardPresent = true;
            notifyPresent();
        }
        else if (eventID == EVENT_REMOVE_CARD)
            notifyAbsent();
        else {
            notifyPresent();
            notifyAbsent();
        }
    }

    public static void notifyCancel() {
        notifyEvent = new NotifyEvent(EVENT_CANCEL, SLOT_INDEX_NONE);
        //notifyEvent.eventID = EVENT_CANCEL;
        notifyPresent();
        notifyAbsent();
    }

    private static void notifyPresent() {
        synchronized (objPresent) {
            objPresent.notifyAll();
        }
    }

    private static void notifyAbsent() {
        synchronized (objAbsent) {
            objAbsent.notifyAll();
        }
    }

    public static void waitForCardPresent(int timeout) throws InterruptedException {
        synchronized (objPresent) {
            notifyEvent = null;
            objPresent.wait(timeout);
        }
    }

    public static void waitForCardAbsent(int timeout) throws InterruptedException {
        synchronized (objAbsent) {
            notifyEvent = null;
            objAbsent.wait(timeout);
        }
    }

    public static void clear() {
        notifyEvent = null;
    }
}

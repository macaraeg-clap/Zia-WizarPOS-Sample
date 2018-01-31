package com.cloudpos.jniinterface;

import android.util.Log;

public class RFCardInterface {

    static {
        String fileName = "jni_cloudpos_rfcard";
        JNILoad.jniLoad(fileName);
    }

    public static int CONTACTLESS_CARD_MODE_AUTO = 0, CONTACTLESS_CARD_MODE_TYPE_A = 1, CONTACTLESS_CARD_MODE_TYPE_B = 2, CONTACTLESS_CARD_MODE_TYPE_C = 3,
            RC500_COMMON_CMD_GET_READER_VERSION = 0x40, RC500_COMMON_CMD_RF_CONTROL = 0x38, // command data : 0x01(turn on), 0x00(turn off)
            CONTACTLESS_CARD_EVENT_FOUND_CARD = 0, CONTACTLESS_CARD_EVENT_TIME_OUT = 1, CONTACTLESS_CARD_EVENT_COMM_ERROR = 2,
            CONTACTLESS_CARD_EVENT_USER_CANCEL = 3, EVENT_NONE = -1;

    public synchronized native static int open();

    public native static int close();

    public synchronized native static int searchBegin(int nCardMode, int nFlagSearchAll, int nTimeout_MS);

    public native static int searchEnd();

    public synchronized native static int attach(byte byteArrayATR[]);

    public native static int detach();

    public synchronized native static int transmit(byte byteArrayAPDU[], int nAPDULength, byte byteArrayResponse[]);

    public synchronized native static int sendControlCommand(int nCmdID, byte byteArrayCmdData[], int nDataLength);

    public synchronized native static int verify(int nSectorIndex, int nPinType, byte[] strPin, int nPinLength);

    public synchronized native static int read(int nSectorIndex, int nBlockIndex, byte[] pDataBuffer, int nDataBufferLength);

    public synchronized native static int write(int nSectorIndex, int nBlockIndex, byte[] pData, int nDataLength);

    public native static int queryInfo(int[] pHasMoreCards, int[] pCardType);

    public synchronized native static int readValue(int nSectorIndex, int nBlockIndex, byte[] pValue, int nValueBufLength, byte[] pAddrData);

    public synchronized native static int writeValue(int nSectorIndex, int nBlockIndex, int pValue, int nValueLength, byte pAddrData);

    public synchronized native static int increment(int nSectorIndex, int nBlockIndex, int value);

    public synchronized native static int decrement(int nSectorIndex, int nBlockIndex, int value);

    public synchronized native static int transfer(int nSectorIndex, int nBlockIndex);

    public synchronized native static int restore(int nSectorIndex, int nBlockIndex);

    public native static int touch();

    /*public static final int NONE = -1;
    public static Object object = new Object();
    public static int nEventID = NONE;
    public static byte[] arryEventData = null;

    public static void callBack(int nEvent, byte[] nData) {

        nEventID = nEvent;

        if (nData != null) {
            arryEventData = new byte[nData.length];
            for (int i = 0; i < nData.length; i++)
                arryEventData[i] = nData[i];
        }

        synchronized (object) {
            Log.i("RFCard", "notify");
            object.notifyAll();
        }
    }*/

    public static Object object = new Object();
    public static NotifyEvent notifyEvent = null;
    public static boolean isCallBackCalled = false;

    public static void callBack(int nEvent, byte[] nData) {
        synchronized (object) {
            notifyEvent = new NotifyEvent(nEvent, nData);
            Log.d("DEBUG", "callBack event = " + nEvent);
            isCallBackCalled = true;
            object.notifyAll();
        }
    }

    public static void waitForCardPresent(int nCardMode, int nFlagSearchAll, int nTimeout_MS) throws InterruptedException {
        synchronized (object) {
            notifyEvent = null;
            searchBegin(nCardMode,nFlagSearchAll,nTimeout_MS);
            object.wait();
        }
    }

    public static void notifyCancel() {
        synchronized (object) {
            notifyEvent = new NotifyEvent(CONTACTLESS_CARD_EVENT_USER_CANCEL, null);
            object.notify();
        }
    }
    public static void clear() {
        isCallBackCalled = false;
        Log.d("DEBUG", "clear status");
        notifyEvent = null;
    }

    public static class NotifyEvent {
        public int eventID;
        public byte[] eventData;
        public NotifyEvent(int eventID, byte[] eventData){
            this.eventID = eventID;
            this.eventData = eventData;
        }
    }
}

package com.cloudpos.jniinterface;

public class PINPadInterface {

    static {
        String fileName = "jni_cloudpos_pinpad";
        JNILoad.jniLoad(fileName);
    }

    public static final int[] MASTER_KEY_ID = new int[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09 }, USER_KEY_ID = new int[] { 0x00, 0x01 };
    public static final int ALGO_CHECK_VALUE_DEFAULT = 0, ALGO_CHECK_VALUE_SE919 = 1, KEY_TYPE_DUKPT = 0, KEY_TYPE_TDUKPT = 1, KEY_TYPE_MASTER = 2, KEY_TYPE_PUBLIC = 3,
            KEY_TYPE_FIX = 4, ALGORITH_3DES = 1, ALGORITH_DES = 0, ALGORITH_SM4 = 2, MAC_METHOD_X99 = 0, MAC_METHOD_ECB_FIRST = 1, MAC_METHOD_SE919 = 2, MAC_METHOD_ECB = 3,
            PINPAD_ENCRYPT_STRING_MODE_EBC = 0, PINPAD_ENCRYPT_STRING_MODE_CBC = 1, PINPAD_ENCRYPT_STRING_MODE_CFB = 2, PINPAD_ENCRYPT_STRING_MODE_OFB = 3;

    public synchronized native static int open();

    public native static int close();

    public native static int showText(int nLineIndex, byte arryText[], int nTextLength, int nFlagSound);

    public native static int selectKey(int nKeyType, int nMasterKeyID, int nUserKeyID, int nAlgorith);

    public native static int setPinLength(int nLength, int nFlag);

    public native static int encrypt(byte arryPlainText[], int nTextLength, byte arryCipherTextBuffer[]);

    public native static int calculatePINBlock(byte arryASCIICardNumber[], int nCardNumberLength, byte arryPinBlockBuffer[], int nTimeout_MS, int nFlagSound);

    public native static int calculateMac(byte arryData[], int nDataLength, int nMACFlag, byte arryMACOutBuffer[]);

    public native static int updateUserKey(int nMasterKeyID, int nUserKeyID, byte arryCipherNewUserKey[], int nCipherNewUserKeyLength);

    public native static int updateCipherMasterKey(int nMasterKeyID, byte[] arryCipherNewMasterKey, int nCipherNewMasterKeyLength, byte[] pCheckValue, int nCheckValueLen);

    public native static int updateUserKeyWithCheck(int nMasterKeyID, int nUserKeyID, byte[] arryCipherNewUserKey, int nCipherNewUserKeyLength, int nUserKeyType, byte[] checkValue,
                                                    int checkValueLength);

    public native static int updateMasterKey(int nMasterKeyID, byte arrayOldKey[], int nOldKeyLength, byte arrayNewKey[], int nNewKeyLength);

    public native static int updateUserKeyWithCheckE(int nMasterKeyID, int nUserKeyID, byte[] arryCipherNewUserKey, int nCipherNewUserKeyLength, int nUserKeyType, byte[] checkValue,
                                                     int checkValueLength, int algoCheckValue);

    public native static int updateCipherMasterKeyE(int nMasterKeyID, byte[] arryCipherNewMasterKey, int nCipherNewMasterKeyLength, byte[] pCheckValue, int nCheckValueLen,
                                                    int algoCheckValue);

    public native static int getSerialNo(byte arrySerialNo[]);

    public native static int setPinblockCallback();

    public native static int setAllowBypassPinFlag(int flag);

    public native static int encryptWithMode(byte[] arrayPlainText, byte[] arrayCipherTextBuffer, int nMode, byte[] arrayIV, int nIVLen);

    public native static int getHwserialno(byte[] buffer);

    public native static int getMacForSnk(byte[] uniqueSNbuffer, byte[] randomBuffer, byte[] resultBuffer);

    private static PinPadCallbackHandler callbackHandler;

    public static int setupCallbackHandler(PinPadCallbackHandler handler) {
        callbackHandler = handler;
        if (handler != null)
            return setPinblockCallback();
        return 0;
    }

    public static void pinpadCallback(byte[] data) {
        if (callbackHandler != null)
            callbackHandler.processCallback(data);
    }
}

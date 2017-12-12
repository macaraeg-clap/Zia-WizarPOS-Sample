package com.ziaplex.wizarsample.errorcode;

public class ErrorCodeTransfer {

    public static int transfer2HardwareErrorCode(int errorCode) {
        return -errorCode & 0x0000FF;
    }

    public static int transfer2SoftwareErrorCode(int errorCode) {
        return ((-errorCode) >> 8) & 0x00FF00;
    }
}

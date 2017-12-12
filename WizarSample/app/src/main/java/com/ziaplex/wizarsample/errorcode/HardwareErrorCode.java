package com.ziaplex.wizarsample.errorcode;

public interface HardwareErrorCode {

    /**
     * PINPAD HARDWARE ERRORCODE
     */
    int SUCCESS = 0x00;
    int EACCES = 0x11;
    int CMD_MISMATCH = 0x12;
    int PACKAGE_LENGTH = 0x13;
    int USER_CANCEL = 0x01;
    int FIELD_LENGTH = 0x15;
    int NOT_SPPORT = 0x20;
    int PIN_LENGTH = 0x21;
    int AUTH = 0x22;
    int KEY_LENGTH = 0x23;
    int CHECK_VALUE = 0x24;
    int WRITE_FLASH = 0x25;
    int READ_FLASH = 0x26;
    int NO_KEY = 0x27;
    int OUT_RANGE = 0x28;
    int KEY_ERROR_INTEGRITY = 0x29;
    int AES_ENCRYPT = 0x2A;
    int AES_DECRYPT = 0x2B;
    int BREAK_SENSITIVE_RULES = 0x2C;
    int MAC = 0x2D;
    int DATA_ALIGN = 0x2E;
}

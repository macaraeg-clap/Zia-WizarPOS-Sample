package com.cloudpos.jniinterface;

public class SmartCardSlotInfo {

    public SmartCardSlotInfo() {
        FIDI = -1;
        EGT = -1;
        WI = -1;
        WTX = -1;
        EDC = -1;
        protocol = -1;
        power = -1;
        conv = -1;
        IFSC = -1;
        cwt = -1;
        bwt = -1;
        nSlotInfoItem = 0;
    }

    public short FIDI, EGT, WI, WTX, EDC, protocol, power, conv, IFSC;
    public long cwt, bwt, nSlotInfoItem;
}

package com.ziaplex.wizarsample.utility;

import com.cloudpos.scanserver.aidl.ScanParameter;

public class QRRActivity extends SActivity {

    @Override
    public String getButtonText() {
        return "Start QR Scan";
    }

    @Override
    public String getBottomText() {
        return "QR " + super.getBottomText();
    }

    @Override
    public ScanParameter getScanParameter() {
        ScanParameter parameter = super.getScanParameter();
        parameter.set(ScanParameter.KEY_DECODEFORMAT, "QR, Code128");
        return parameter;
    }
}

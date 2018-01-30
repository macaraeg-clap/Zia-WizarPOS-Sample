package com.ziaplex.wizarsample.utility;

public class BCRActivity extends SActivity {

    @Override
    public String getButtonText() {
        return "Start Bar Code Scan";
    }

    @Override
    public String getBottomText() {
        return "Bar Code " + super.getBottomText();
    }
}

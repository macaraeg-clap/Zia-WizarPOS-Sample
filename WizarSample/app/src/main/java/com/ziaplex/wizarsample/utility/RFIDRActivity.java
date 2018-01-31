package com.ziaplex.wizarsample.utility;

import android.os.Bundle;
import android.view.View;

import com.cloudpos.jniinterface.RFCardInterface;
import com.ziaplex.wizarsample.R;
import com.ziaplex.wizarsample.UI;

public class RFIDRActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateMessage() {
        return UI.createMessageView(this, R.drawable.ic_tap, "Please Tap Card", null);
    }

    @Override
    public void exit() {
        RFCardInterface.close();
        super.exit();
    }
}

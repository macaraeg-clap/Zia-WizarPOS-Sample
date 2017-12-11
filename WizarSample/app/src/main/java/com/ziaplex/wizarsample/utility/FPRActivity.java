package com.ziaplex.wizarsample.utility;

import android.os.Bundle;

import com.ziaplex.wizarsample.R;
import com.ziaplex.wizarsample.UI;

public class FPRActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addBaseContentView(UI.createMessageView(this, R.drawable.ic_finger_print, "Please Press Your Finger", null));
        addBaseContentView(UI.createFingerPrintView(this));
    }
}

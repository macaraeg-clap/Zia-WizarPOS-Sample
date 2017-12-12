package com.ziaplex.wizarsample.utility;

import android.os.Bundle;
import android.view.View;

import com.ziaplex.wizarsample.R;
import com.ziaplex.wizarsample.UI;

public class BCRActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addBaseContentView(UI.createMessageView(this, -1, "Bar Code Value will be displayed here...", null));
    }

    @Override
    public View onCreateMessage() {
        return UI.createMessageView(this, R.drawable.ic_barcode, "Please Scan Bar Code", null);
    }
}

package com.ziaplex.wizarsample.utility;

import android.content.Intent;
import android.os.Bundle;

import com.ziaplex.wizarsample.R;
import com.ziaplex.wizarsample.UI;

public class BCRActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addBaseContentView(UI.createMessageView(this, R.drawable.ic_barcode, "Please Scan Bar Code", null));
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    private void exit() {
        Intent intent = new Intent("baseActivity");
        intent.putExtra("closeAll", 1);
        sendBroadcast(intent);
    }
}

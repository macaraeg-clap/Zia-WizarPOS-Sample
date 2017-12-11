package com.ziaplex.wizarsample.utility;

import android.os.Bundle;
import android.view.View;

import com.ziaplex.wizarsample.R;
import com.ziaplex.wizarsample.UI;

public class PActivity extends BaseActivity implements UI.CustomButtonViewListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addBaseContentView(UI.createMessageView(this, R.drawable.ic_printer, "Print", this));
    }

    @Override
    public void onButtonClick(View view, String text) {
        UI.showToastMessage(this, text);
    }
}

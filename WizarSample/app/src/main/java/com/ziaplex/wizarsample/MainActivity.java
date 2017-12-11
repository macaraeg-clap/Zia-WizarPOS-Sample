package com.ziaplex.wizarsample;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.ziaplex.wizarsample.utility.BCRActivity;
import com.ziaplex.wizarsample.utility.FPRActivity;
import com.ziaplex.wizarsample.utility.ICCRActivity;
import com.ziaplex.wizarsample.utility.MSRActivity;
import com.ziaplex.wizarsample.utility.PActivity;
import com.ziaplex.wizarsample.utility.QRRActivity;

public class MainActivity extends AppCompatActivity implements UI.CustomButtonViewListener {

    private View mView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addButton(UI.createCustomButtonView(this, R.drawable.ic_msc, getString(R.string.mscr_title), this));
        addButton(UI.createCustomView(this, LinearLayout.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen._10sdp)));
        addButton(UI.createCustomButtonView(this, R.drawable.ic_icc, getString(R.string.iccr_title), this));
        addButton(UI.createCustomView(this, LinearLayout.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen._10sdp)));
        addButton(UI.createCustomButtonView(this, R.drawable.ic_barcode, getString(R.string.bcr_title), this));
        addButton(UI.createCustomView(this, LinearLayout.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen._10sdp)));
        addButton(UI.createCustomButtonView(this, R.drawable.ic_qr, getString(R.string.qrr_title), this));
        addButton(UI.createCustomView(this, LinearLayout.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen._10sdp)));
        addButton(UI.createCustomButtonView(this, R.drawable.ic_finger_print, getString(R.string.fpr_title), this));
        addButton(UI.createCustomView(this, LinearLayout.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen._10sdp)));
        addButton(UI.createCustomButtonView(this, R.drawable.ic_printer, getString(R.string.p_title), this));
        showLoadingView(false);
    }



    private void addButton(View view) {
        LinearLayout v = findViewById(R.id.action_container);
        if (v != null)
            v.addView(view);
    }

    @Override
    public void onButtonClick(View view, String text) {
        showLoadingView(true);
        Class activity;
        if (getString(R.string.mscr_title).equals(text))
            activity = MSRActivity.class;
        else if (getString(R.string.iccr_title).equals(text))
            activity = ICCRActivity.class;
        else if (getString(R.string.bcr_title).equals(text))
            activity = BCRActivity.class;
        else if (getString(R.string.qrr_title).equals(text))
            activity = QRRActivity.class;
        else if (getString(R.string.fpr_title).equals(text))
            activity = FPRActivity.class;
        else
            activity = PActivity.class;
        mView = view;
        startActivityForResult(new Intent(this, activity), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            showLoadingView(false);
            if (mView != null)
                mView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
        }
    }

    private void showLoadingView(boolean status) {
        LinearLayout v = findViewById(R.id.progress_container);
        if (v != null) {
            if (status)
                v.setVisibility(View.VISIBLE);
            else
                v.setVisibility(View.INVISIBLE);
        }
    }
}

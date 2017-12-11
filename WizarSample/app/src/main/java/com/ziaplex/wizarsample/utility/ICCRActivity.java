package com.ziaplex.wizarsample.utility;

import android.content.Intent;
import android.os.Bundle;

import com.ziaplex.wizarsample.CardDetails;
import com.ziaplex.wizarsample.R;
import com.ziaplex.wizarsample.UI;

public class ICCRActivity extends BaseActivity {

    private CardDetails cardDetails;

    public ICCRActivity() {
        cardDetails = new CardDetails();
        cardDetails.setPan("xxxx xxxx xxxx 0000");
        cardDetails.setCardHolderName("Card Holder Name");
        cardDetails.setExpiryDate("0000");
        cardDetails.setServiceCode("12345");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addBaseContentView(UI.createCardDetailsView(this, cardDetails));
        addBaseContentView(UI.createMessageView(this, R.drawable.ic_icc, "Please Insert Card", null));
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

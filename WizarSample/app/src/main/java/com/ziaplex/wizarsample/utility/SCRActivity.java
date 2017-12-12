package com.ziaplex.wizarsample.utility;

import android.os.Bundle;
import android.view.View;

import com.ziaplex.wizarsample.CardDetails;
import com.ziaplex.wizarsample.R;
import com.ziaplex.wizarsample.UI;

public class SCRActivity extends BaseActivity {

    public SCRActivity() {
        cardDetails = new CardDetails();
        cardDetails.setPan("xxxxxxxxxxxx0000");
        cardDetails.setCardHolderName("Card Holder Name");
        cardDetails.setExpiryDate("0000");
        cardDetails.setServiceCode("12345");
    }

    private CardDetails cardDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addBaseContentView(UI.createCardDetailsView(this, cardDetails));
    }

    @Override
    public View onCreateMessage() {
        return UI.createMessageView(this, R.drawable.ic_sc, "Please Insert Card", null);
    }
}

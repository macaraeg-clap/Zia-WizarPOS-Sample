package com.ziaplex.wizarsample.utility;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.cloudpos.jniinterface.MSRInterface;
import com.ziaplex.wizarsample.CardDetails;
import com.ziaplex.wizarsample.R;
import com.ziaplex.wizarsample.UI;
import com.ziaplex.wizarsample.Util;

import static com.cloudpos.jniinterface.MSRInterface.getTrackData;
import static com.cloudpos.jniinterface.MSRInterface.getTrackDataLength;
import static com.cloudpos.jniinterface.MSRInterface.getTrackError;

public class MSRActivity extends BaseActivity {

    private static final int MSGID_SHOW_MESSAGE = 0;
    private Handler handler;
    private CardDetails cardDetails;

    public MSRActivity() {
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
        addBaseContentView(UI.createMessageView(this, R.drawable.ic_msc, "Please Swipe Card", null));
        initializeHandler();
    }

    private void initializeHandler() {
        handler = new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case MSGID_SHOW_MESSAGE:
                        displayCardDetails(msg.obj.toString());
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        Thread thread = new Thread() {

            @Override
            public void run() {
                try {
                    synchronized (this) {
                        wait(5000);
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                handler.obtainMessage(MSGID_SHOW_MESSAGE, "Please Swipe Card").sendToTarget();
                                boolean retry = true;
                                while (retry) {
                                    retry = false;
                                    int MSRopenresult = MSRInterface.open();
                                    if (MSRopenresult >= 0) {
                                        try {
                                            MSRInterface.waitForSwipe(60000);
                                        }
                                        catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        if (MSRInterface.eventID == MSRInterface.MSR_CARD_EVENT_FOUND_CARD) {
                                            if (!parseTrackData())
                                                retry = true;
                                            else
                                                handler.obtainMessage(MSGID_SHOW_MESSAGE, "Magnetic Stripe Card Details reading successful...").sendToTarget();
                                        }
                                        else
                                            retry = true;
                                        MSRInterface.close();
                                    }
                                    else
                                        handler.obtainMessage(MSGID_SHOW_MESSAGE, "Device open failed... Code:" + Integer.toHexString(MSRopenresult)).sendToTarget();
                                }
                            }
                        });

                    }
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    private void displayCardDetails(String message) {
        {
            TextView v = findViewById(R.id.txt_pan);
            if (v != null)
                v.setText(cardDetails.getPan());
        }
        {
            TextView v = findViewById(R.id.txt_card_holder_name);
            if (v != null)
                v.setText(cardDetails.getCardHolderName());
        }
        {
            TextView v = findViewById(R.id.txt_expiry_date);
            if (v != null)
                v.setText(cardDetails.getExpiryDate());
        }
        {
            TextView v = findViewById(R.id.txt_service_code);
            if (v != null)
                v.setText(cardDetails.getServiceCode());
        }
        {
            TextView v = findViewById(R.id.txt_message);
            if (v != null)
                v.setText(message);
        }
    }

    private boolean parseTrackData() {
        String Info = "Please Swipe Again!";
        int result, trackNo = 0;
        result = getTrackError(trackNo);
        if (result < 0) {
            handler.obtainMessage(MSGID_SHOW_MESSAGE, Info).sendToTarget();
            return false;
        }
        result = getTrackDataLength(trackNo);
        if (result < 0) {
            handler.obtainMessage(MSGID_SHOW_MESSAGE, Info).sendToTarget();
            return false;
        }
        byte[] stripInfo = new byte[result];
        result = getTrackData(trackNo, stripInfo, stripInfo.length);
        if (result < 0) {
            handler.obtainMessage(MSGID_SHOW_MESSAGE, Info).sendToTarget();
            return false;
        }
        cardDetails.setPan(Util.getStripInfoPAN(stripInfo));
        cardDetails.setCardHolderName(Util.getStripInfoDetails(stripInfo, 1, 2));
        cardDetails.setExpiryDate(Util.getStripInfoDetails(stripInfo, 2, -1, 0, 4));
        cardDetails.setServiceCode(Util.getStripInfoDetails(stripInfo, 2, -1, 5, 3));
        return true;
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    private void exit() {
        MSRInterface.close();
        Intent intent = new Intent("baseActivity");
        intent.putExtra("closeAll", 1);
        sendBroadcast(intent);
    }
}

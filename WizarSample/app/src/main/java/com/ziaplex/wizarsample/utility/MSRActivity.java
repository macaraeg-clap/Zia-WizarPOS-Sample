package com.ziaplex.wizarsample.utility;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.cloudpos.jniinterface.MSRInterface;
import com.ziaplex.wizarsample.CardDetails;
import com.ziaplex.wizarsample.R;
import com.ziaplex.wizarsample.UI;
import com.ziaplex.wizarsample.Util;

public class MSRActivity extends BaseActivity {

    public MSRActivity() {
        cardDetails = new CardDetails();
        cardDetails.setPan("xxxxxxxxxxxx0000");
        cardDetails.setCardHolderName("Card Holder Name");
        cardDetails.setExpiryDate("0000");
        cardDetails.setServiceCode("12345");
    }

    private static final int MSG_ID_SHOW_MESSAGE = 0;
    private Handler handler;
    private CardDetails cardDetails;
    private Thread thread;
    volatile boolean stop = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addBaseContentView(UI.createCardDetailsView(this, cardDetails));
        handler = new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_ID_SHOW_MESSAGE:
                        displayCardDetails(msg.obj.toString());
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        executeMSReader();
    }

    @Override
    public View onCreateMessage() {
        return UI.createMessageView(this, R.drawable.ic_msc, "Please Swipe Card", null);
    }

    @Override
    public void exit() {
        stop = true;
        if (thread != null) {
            if (thread.isAlive())
                thread.interrupt();
        }
        MSRInterface.close();
        super.exit();
    }

    private void executeMSReader() {
        thread = new Thread() {

            @Override
            public void run() {
                while (!stop) {
                    try {
                        synchronized (this) {
                            wait(1000);
                            boolean retry = true;
                            while (retry) {
                                retry = false;
                                int MSRopenresult = MSRInterface.open();
                                if (MSRopenresult >= 0) {
                                    try {
                                        MSRInterface.waitForSwipe(50000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    if (MSRInterface.eventID == MSRInterface.CONTACTLESS_CARD_EVENT_FOUND_CARD) {
                                        if (!parseTrackData())
                                            retry = true;
                                        else
                                            handler.obtainMessage(MSG_ID_SHOW_MESSAGE, "Magnetic Stripe Card Details Reading Successful").sendToTarget();
                                    } else
                                        retry = true;
                                    MSRInterface.close();
                                } else
                                    handler.obtainMessage(MSG_ID_SHOW_MESSAGE, "Code:" + Integer.toHexString(MSRopenresult) + " (Device Open failed)").sendToTarget();
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
    }

    private void displayCardDetails(String message) {
        {
            TextView v = findViewById(R.id.txt_message);
            if (v != null)
                v.setText(message);
        }
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
    }

    private boolean parseTrackData() {
        String Info = "Please Swipe Again!";
        int result, trackNo = 0;
        result = MSRInterface.getTrackError(trackNo);
        if (result < 0) {
            handler.obtainMessage(MSG_ID_SHOW_MESSAGE, Info).sendToTarget();
            return false;
        }
        result = MSRInterface.getTrackDataLength(trackNo);
        if (result < 0) {
            handler.obtainMessage(MSG_ID_SHOW_MESSAGE, Info).sendToTarget();
            return false;
        }
        byte[] stripInfo = new byte[result];
        result = MSRInterface.getTrackData(trackNo, stripInfo, stripInfo.length);
        if (result < 0) {
            handler.obtainMessage(MSG_ID_SHOW_MESSAGE, Info).sendToTarget();
            return false;
        }
        cardDetails.setPan(Util.getStripInfoPAN(stripInfo));
        cardDetails.setCardHolderName(Util.getStripInfoDetails(stripInfo, 1, 2));
        cardDetails.setExpiryDate(Util.getStripInfoDetails(stripInfo, 2, -1, 0, 4));
        cardDetails.setServiceCode(Util.getStripInfoDetails(stripInfo, 2, -1, 5, 3));
        return true;
    }
}

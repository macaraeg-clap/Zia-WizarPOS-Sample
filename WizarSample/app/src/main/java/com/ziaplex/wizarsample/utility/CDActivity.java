package com.ziaplex.wizarsample.utility;

import android.os.Bundle;
import android.view.View;

import com.cloudpos.jniinterface.CashDrawerInterface;
import com.ziaplex.wizarsample.R;
import com.ziaplex.wizarsample.UI;

public class CDActivity extends BaseActivity implements UI.CustomButtonViewListener {

    private UI.MessageView messageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addBaseContentView(messageView = UI.createMessageView(this, -1, "Status", null));
        open();
    }

    @Override
    public View onCreateMessage() {
        return UI.createMessageView(this, R.drawable.ic_cash_drawer, "Open Cash Drawer", this);
    }

    @Override
    public void onButtonClick(View view, String text) {
        kickOut();
    }

    @Override
    public void exit() {
        CashDrawerInterface.close();
        super.exit();
    }

    private void open() {
        int result = CashDrawerInterface.open();
        if (result < 0) {
            if (messageView != null)
                messageView.setMessage("Cash Drawer Not Found in this device");
        } else {
            if (messageView != null)
                messageView.setMessage("Cash Drawer Available");
        }
    }

    private void kickOut() {
        int result = CashDrawerInterface.kickOut();
        if (result < 0) {
            if (messageView != null)
                messageView.setMessage("KickOut: FAILED");
        } else {
            if (messageView != null)
                messageView.setMessage("KickOut: SUCCESS");
        }
    }
}

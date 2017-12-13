package com.ziaplex.wizarsample.utility;

import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;

import com.cloudpos.scanserver.aidl.AidlController;
import com.cloudpos.scanserver.aidl.IAIDLListener;
import com.cloudpos.scanserver.aidl.IScanService;
import com.cloudpos.scanserver.aidl.ScanParameter;
import com.cloudpos.scanserver.aidl.ScanResult;
import com.ziaplex.wizarsample.R;
import com.ziaplex.wizarsample.UI;

public class QRRActivity extends BaseActivity implements UI.CustomButtonViewListener, IAIDLListener {

    private UI.MessageView messageView;
    private IScanService scanService;
    private ServiceConnection scanConn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addBaseContentView(UI.createMessageView(this, -1, "Start QR Scan", this));
        addBaseContentView(UI.createCustomHorizontalSeparator(this));
        addBaseContentView(messageView = UI.createMessageView(this, -1, "QR Value will be displayed here...", null));
        AidlController.getInstance().startScanService(this, this);
    }

    @Override
    public void onButtonClick(View view, String text) {
        ScanParameter parameter = new ScanParameter();
        parameter.set(ScanParameter.KEY_DECODEFORMAT, "QR, Code128");
        try {
            ScanResult result = scanService.scanBarcode(parameter);
            if (messageView != null)
                messageView.setMessage("RESULT\n--------------------------------\n" + result + "\n--------------------------------");
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateMessage() {
        return UI.createMessageView(this, R.drawable.ic_qr, "Please Scan QR", null);
    }

    @Override
    public void exit() {
        if (scanService != null) {
            this.unbindService(scanConn);
            scanService = null;
            scanConn = null;
        }
        super.exit();
    }

    @Override
    public void serviceConnected(Object objService, ServiceConnection connection) {
        if (objService instanceof IScanService) {
            scanService = (IScanService) objService;
            scanConn = connection;
        }
    }
}

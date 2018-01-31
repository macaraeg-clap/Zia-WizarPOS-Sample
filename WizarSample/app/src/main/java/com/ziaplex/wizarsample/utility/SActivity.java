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

public class SActivity extends BaseActivity implements UI.CustomButtonViewListener, IAIDLListener {

    private UI.MessageView messageView;
    private IScanService scanService;
    private ServiceConnection scanConn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addBaseContentView(messageView = UI.createMessageView(this, -1, getBottomText(), null));
        AidlController.getInstance().startScanService(this, this);
    }

    public String getButtonText() {
        return "Start Scan";
    }

    public String getBottomText() {
        return "Result will be displayed here";
    }

    public ScanParameter getScanParameter() {
        ScanParameter parameter = new ScanParameter();
        parameter.set(ScanParameter.KEY_UI_WINDOW_TOP, (int) getResources().getDimension(R.dimen._20sdp));
        parameter.set(ScanParameter.KEY_UI_WINDOW_LEFT, (int) getResources().getDimension(R.dimen._20sdp));
        parameter.set(ScanParameter.KEY_UI_WINDOW_WIDTH, (int) getResources().getDimension(R.dimen._250sdp));
        parameter.set(ScanParameter.KEY_UI_WINDOW_HEIGHT, (int) getResources().getDimension(R.dimen._200sdp));
        parameter.set(ScanParameter.KEY_SCAN_SECTION_WIDTH, (int) getResources().getDimension(R.dimen._125sdp));
        parameter.set(ScanParameter.KEY_SCAN_SECTION_HEIGHT, (int) getResources().getDimension(R.dimen._100sdp));
        return parameter;
    }

    @Override
    public void onButtonClick(View view, String text) {
        try {
            ScanResult result = scanService.scanBarcode(getScanParameter());
            if (messageView != null)
                messageView.setMessage("RESULT\n\n" + result + "\n");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateMessage() {
        return UI.createMessageView(this, R.drawable.ic_barcode, getButtonText(), this);
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

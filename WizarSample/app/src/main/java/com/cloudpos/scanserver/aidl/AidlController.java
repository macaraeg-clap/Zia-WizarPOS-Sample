package com.cloudpos.scanserver.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class AidlController {

    private AidlController() {
    }

    public static final String TAG = "AidlController", DESC_SCAN_SERVICE = "com.cloudpos.scanserver.aidl.IScanService";
    private static AidlController instance;
    private IAIDLListener aidlListener ;
    private ServiceConnection connection;

    public static AidlController getInstance() {
        if (instance == null)
            instance = new AidlController();
        return instance;
    }

    protected class ServiceConnectionImpl implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                String temp = service.getInterfaceDescriptor();
                Log.d(TAG, "onServiceConnected : " + temp);
                Object objService = null;
                if (temp.equals(DESC_SCAN_SERVICE))
                    objService = IScanService.Stub.asInterface(service);
                if (objService != null)
                    aidlListener.serviceConnected(objService, connection);
            }
            catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            connection = null;
        }
    }

    protected void setListener(IAIDLListener aidlListener ) {
        this.aidlListener = aidlListener;
    }

    protected boolean startConnectService(Context host, String packageName , String className, IAIDLListener aidlListener) {
        return startConnectService(host, new ComponentName(packageName, className), aidlListener);
    }

    protected boolean startConnectService(Context host, ComponentName comp,IAIDLListener aidlListener) {
        setListener(aidlListener);
        Intent intent = new Intent();
        intent.setComponent(comp);
        connection = new ServiceConnectionImpl();
        boolean isSuccess = host.bindService(intent, connection, Context.BIND_AUTO_CREATE);
        host.startService(intent);
        return isSuccess;
    }

    public boolean startScanService(Context host,IAIDLListener aidlListener) {
        return startConnectService(host, new ComponentName("com.cloudpos.scanserver", "com.cloudpos.scanserver.service.ScannerService"), aidlListener);
    }
}

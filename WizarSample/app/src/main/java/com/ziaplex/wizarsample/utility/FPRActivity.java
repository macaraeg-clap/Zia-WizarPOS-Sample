package com.ziaplex.wizarsample.utility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudpos.jniinterface.FingerPrintInterface;
import com.ziaplex.wizarsample.R;
import com.ziaplex.wizarsample.UI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FPRActivity extends BaseActivity {

    private static final int MSG_ID_SHOW_MESSAGE = 0, MSG_ID_SHOW_IMAGE = 1;
    private Handler handler;
    private Thread thread;
    private String path0, path1;
    volatile boolean stop = false;

    public FPRActivity() {
        path0 = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/tmp";
        path1 = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/tmp/fingerprint.txt";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addBaseContentView(UI.createFingerPrintView(this));
        handler = new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_ID_SHOW_MESSAGE:
                        UI.showToastMessage(getBaseContext(), msg.obj.toString());
                        displayNotSupported();
                        break;
                    case MSG_ID_SHOW_IMAGE:
                        displayFingerPrint((Bitmap) msg.obj);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        executeFPReader();
    }

    private void displayNotSupported() {
        TextView v = findViewById(R.id.txt_message);
        if (v != null)
            v.setText("Finger Print Reading is not Supported in this device");
    }

    private void displayFingerPrint(Bitmap image) {
        if (image != null) {
            {
                ImageView v = findViewById(R.id.img_finger_print);
                if (v != null)
                    v.setImageBitmap(image);
            }
            {
                TextView v = findViewById(R.id.txt_message);
                if (v != null)
                    v.setText("Finger Print Reading Successful");
            }
        }
    }

    @Override
    public View onCreateMessage() {
        return UI.createMessageView(this, R.drawable.ic_finger_print, "Please Press Your Finger on the Finger Print Reader", null);
    }

    @Override
    public void exit() {
        stop = true;
        if (thread != null) {
            if (thread.isAlive())
                thread.interrupt();
        }
        FingerPrintInterface.close();
        super.exit();
    }

    private void executeFPReader() {
        thread = new Thread() {

            @Override
            public void run() {
                try {
                    int FPRopenresult = FingerPrintInterface.open();
                    if (FPRopenresult >= 0) {
                        byte[] arryFea = new byte[4096];
                        int[] length = new int[1];
                        int result = FingerPrintInterface.getFea(arryFea, arryFea.length, length, -1);
                        if (result >= 0) {
                            handler.obtainMessage(MSG_ID_SHOW_MESSAGE, "Processing").sendToTarget();
                            File dir = new File(path0);
                            if (!dir.exists())
                                dir.mkdir();
                            File file = new File(path1);
                            try {
                                FileOutputStream fos = new FileOutputStream(file);
                                try {
                                    fos.write(arryFea, 0, length[0]);
                                    fos.close();
                                    byte[] pImgBuffer = new byte[100 * 1024];
                                    int[] pRealImaLength = new int[1], pImgWidth = new int[1], pImgHeight = new int[1];
                                    int nImgLength = 100 * 1024;
                                    result = FingerPrintInterface.getLastImage(pImgBuffer, nImgLength, pRealImaLength, pImgWidth, pImgHeight);
                                    if (result < 0)
                                        handler.obtainMessage(MSG_ID_SHOW_MESSAGE, "Get Finger Print Image Failed").sendToTarget();
                                    else {
                                        BitmapFactory.Options opts = new BitmapFactory.Options();
                                        opts.outWidth = pImgHeight[0];
                                        opts.outHeight = pImgHeight[0];
                                        handler.obtainMessage(MSG_ID_SHOW_IMAGE, BitmapFactory.decodeByteArray(pImgBuffer, 0, pRealImaLength[0], opts)).sendToTarget();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            FingerPrintInterface.close();
                        }
                    } else
                        handler.obtainMessage(MSG_ID_SHOW_MESSAGE, "Code:" + Integer.toHexString(FPRopenresult) + " (Device Open failed)").sendToTarget();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
}

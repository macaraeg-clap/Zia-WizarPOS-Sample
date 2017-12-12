package com.ziaplex.wizarsample.utility;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.cloudpos.jniinterface.PINPadInterface;
import com.ziaplex.wizarsample.R;
import com.ziaplex.wizarsample.UI;
import com.ziaplex.wizarsample.errorcode.ErrorCodeTransfer;
import com.ziaplex.wizarsample.errorcode.HardwareErrorCode;
import com.ziaplex.wizarsample.errorcode.SoftwareErrorCode;
import com.ziaplex.wizarsample.Util;

public class PPActivity extends BaseActivity implements UI.CustomButtonViewListener { // FIXME

    private static final int MSG_ID_SHOW_MESSAGE = 0, USER_CANCEL_SHOW_MESSAGE = 1, TIMEOUT_SHOW_MESSAGE = -1, SUCCESS = 2;
    private final String pan = "4834422003052719";
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addBaseContentView(UI.createMessageView(this, -1, "PAN : " + pan, null));
        handler = new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_ID_SHOW_MESSAGE:
                        displayResult("PINPad open failed！ Please Try Again");
                        break;
                    case USER_CANCEL_SHOW_MESSAGE:
                        displayResult("User Cancelled! Please Try Again");
                        break;
                    case TIMEOUT_SHOW_MESSAGE:
                        displayResult("Timeout! Please Try Again");
                    case SUCCESS:
                        //displayResult(msg.obj.toString());
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public View onCreateMessage() {
        return UI.createMessageView(this, R.drawable.ic_pad, "Start", this);
    }

    @Override
    public void onButtonClick(View view, String text) {
        executePinPad();
    }

    @Override
    public void exit() {
        PINPadInterface.close();
        super.exit();
    }

    private void displayResult(String result) {
        UI.showToastMessage(this, result);
    }

    private void executePinPad() {
        new DialogInterface.OnClickListener() {

            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                new Thread() {

                    @Override
                    public void run() {
                        int masterKeyID = 0, userKeyID, calculatePINBlockResult = 0, hardWareErrorCode = 0, softWareErrorCode = 0;
                        byte[] arryPINblock = new byte[18];
                        String PINBlockResult = null;
                        int result = PINPadInterface.open();
                        if (result >= 0) {
                            userKeyID = 0;
                            selectKey(masterKeyID, userKeyID);
                            System.out.println("************************************************ calculatePINBlockResult: " + calculatePINBlockResult);
                            calculatePINBlockResult = PINPadInterface.calculatePINBlock(pan.getBytes(), pan.length(), arryPINblock, -1, 0);
                            PINBlockResult = Util.byteArrayToString(arryPINblock, calculatePINBlockResult);
                            hardWareErrorCode = ErrorCodeTransfer.transfer2HardwareErrorCode(calculatePINBlockResult);
                            softWareErrorCode = ErrorCodeTransfer.transfer2SoftwareErrorCode(calculatePINBlockResult);
                        }
                        else
                            handler.obtainMessage(MSG_ID_SHOW_MESSAGE).sendToTarget();
                        if (calculatePINBlockResult >= 0) {
                            PINPadInterface.close();
                            System.out.println("************************************************ Pin Block: " + PINBlockResult);
                            //handler.obtainMessage(SUCCESS, PINBlockResult).sendToTarget();
                        }
                        else if (hardWareErrorCode == HardwareErrorCode.USER_CANCEL) {
                            PINPadInterface.close();
                            handler.obtainMessage(USER_CANCEL_SHOW_MESSAGE).sendToTarget();
                        }
                        else if (softWareErrorCode == SoftwareErrorCode.ETIMEDOUT) {
                            PINPadInterface.close();
                            handler.obtainMessage(TIMEOUT_SHOW_MESSAGE).sendToTarget();
                        }
                    }
                }.start();
            }
        };
    }

    private void selectKey(final int masterKeyID, final int userKeyID) {
        PINPadInterface.selectKey(PINPadInterface.KEY_TYPE_MASTER, masterKeyID, userKeyID, 0);
    }
}

package com.ziaplex.wizarsample.utility;

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

public class PPActivity extends BaseActivity implements UI.CustomButtonViewListener {

    private static final int MSG_ID_ERROR_MESSAGE = 0, USER_CANCEL_SHOW_MESSAGE = 1, TIMEOUT_SHOW_MESSAGE = -1, SUCCESS = 2, MSG_ID_OPEN_MESSAGE = 3;
    private UI.MessageView messageView;
    private UI.MessageView buttonView;
    private Handler handler;
    private Thread thread;
    private final String pan = "4834422003052719";
    volatile boolean stop = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addBaseContentView(messageView = UI.createMessageView(this, -1, "PAN : " + pan, null));
        handler = new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_ID_OPEN_MESSAGE:
                        if (buttonView != null) {
                            buttonView.setMessage("Please enter PIN in the provided Pad...");
                            buttonView.setDisabled(true);
                        }
                        break;
                    case MSG_ID_ERROR_MESSAGE:
                        displayResult("PINPad open failed！ Please Try Again");
                        break;
                    case USER_CANCEL_SHOW_MESSAGE:
                        displayResult("User Cancelled! Please Try Again");
                        break;
                    case TIMEOUT_SHOW_MESSAGE:
                        displayResult("Timeout! Please Try Again");
                    case SUCCESS:
                        if (buttonView != null) {
                            Object result = msg.obj;
                            if (result != null) {
                                if (result instanceof String && messageView != null)
                                    messageView.setMessage("RESULT\n\n" + result.toString() + "\n");
                                buttonView.setMessage("Start");
                                buttonView.setDisabled(false);
                            } else {
                                buttonView.setMessage("PIN Pad is not Supported in this device...");
                                buttonView.setDisabled(true);
                            }
                        }
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
        return buttonView = UI.createMessageView(this, R.drawable.ic_pad, "Start", this);
    }

    @Override
    public void onButtonClick(View view, String text) {
        if (messageView != null)
            messageView.setMessage("PAN : " + pan);
        executePinPad();
    }

    @Override
    public void exit() {
        stop = true;
        if (thread != null) {
            if (thread.isAlive())
                thread.interrupt();
        }
        PINPadInterface.close();
        super.exit();
    }

    private void displayResult(String result) {
        UI.showToastMessage(this, result);
    }

    private void executePinPad() {
        thread = new Thread() {

            @Override
            public void run() {
                int masterKeyID = 0, userKeyID, calculatePINBlockResult = 0, hardWareErrorCode = 0, softWareErrorCode = 0;
                byte[] arryPINblock = new byte[18];
                String PINBlockResult = null;
                int result = PINPadInterface.open();
                if (result >= 0) {
                    handler.obtainMessage(MSG_ID_OPEN_MESSAGE).sendToTarget();
                    userKeyID = 0;
                    selectKey(masterKeyID, userKeyID);
                    calculatePINBlockResult = PINPadInterface.calculatePINBlock(pan.getBytes(), pan.length(), arryPINblock, -1, 0);
                    PINBlockResult = Util.byteArrayToString(arryPINblock, calculatePINBlockResult);
                    hardWareErrorCode = ErrorCodeTransfer.transfer2HardwareErrorCode(calculatePINBlockResult);
                    softWareErrorCode = ErrorCodeTransfer.transfer2SoftwareErrorCode(calculatePINBlockResult);
                } else
                    handler.obtainMessage(MSG_ID_ERROR_MESSAGE).sendToTarget();
                if (calculatePINBlockResult >= 0)
                    handler.obtainMessage(SUCCESS, PINBlockResult).sendToTarget();
                else if (hardWareErrorCode == HardwareErrorCode.USER_CANCEL)
                    handler.obtainMessage(USER_CANCEL_SHOW_MESSAGE).sendToTarget();
                else if (softWareErrorCode == SoftwareErrorCode.ETIMEDOUT)
                    handler.obtainMessage(TIMEOUT_SHOW_MESSAGE).sendToTarget();
                PINPadInterface.close();
            }
        };
        thread.start();
    }

    private void selectKey(final int masterKeyID, final int userKeyID) {
        PINPadInterface.selectKey(PINPadInterface.KEY_TYPE_MASTER, masterKeyID, userKeyID, 0);
    }
}

package com.ziaplex.wizarsample.utility;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import com.cloudpos.jniinterface.PrinterInterface;
import com.ziaplex.wizarsample.R;
import com.ziaplex.wizarsample.UI;
import com.ziaplex.wizarsample.utility.printer.util.PrinterBitmapUtil;
import com.ziaplex.wizarsample.utility.printer.util.PrinterCommand;

import java.io.UnsupportedEncodingException;

public class PActivity extends BaseActivity implements UI.CustomButtonViewListener {

    private String text = "--------------------------------" +
            "\n\tWizar POS Sample\n\t  Ziaplex Inc." +
            "\n--------------------------------";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addBaseContentView(UI.createMessageView(this, R.drawable.ic_printer, "Print", this));
        addBaseContentView(UI.createMessageView(this, R.drawable.ic_logo, text, null));
    }

    @Override
    public void onButtonClick(View view, String text) {
        new Thread(print).start();
    }

    private Runnable print = new Runnable() {

        @Override
        public void run() {
            int print = PrinterInterface.open();
            if (print >= 0) {
                Bitmap bitmap = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.ic_logo);
                byte[] header = null;
                try {
                    header = text.getBytes("UTF-8");
                }
                catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                PrinterInterface.begin();
                PrinterBitmapUtil.printBitmap(bitmap, 120, 0, true);
                write(header);
                writeLineBreak(3);
                PrinterInterface.end();
            }
            else
                UI.showToastMessage(getBaseContext(), "Print failed please try again!");
            PrinterInterface.close();
        }
    };

    private void writeLineBreak(int lineNumber) {
        write(PrinterCommand.getCmdEscDN(lineNumber));
    }

    private void write(final byte[] arryData) {
        if (arryData == null)
            PrinterInterface.write(null, 0);
        else
            PrinterInterface.write(arryData, arryData.length);
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    private void exit() {
        PrinterInterface.close();
        Intent intent = new Intent("baseActivity");
        intent.putExtra("closeAll", 1);
        sendBroadcast(intent);
    }
}

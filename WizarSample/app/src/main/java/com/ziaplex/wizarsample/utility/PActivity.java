package com.ziaplex.wizarsample.utility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cloudpos.jniinterface.PrinterInterface;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.ziaplex.wizarsample.R;
import com.ziaplex.wizarsample.UI;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Vector;

public class PActivity extends BaseActivity implements UI.CustomButtonViewListener {

    public static class PrinterBitmapUtil {

        public static final int BIT_WIDTH = 384;
        private static final int WIDTH = 48, DOT_LINE_LIMIT = 200, DC2V_HEAD = 4, GSV_HEAD = 8;

        private static String getSystemProperty(String name) {
            try {
                Process du = Runtime.getRuntime().exec("getprop " + name);
                BufferedReader in = new BufferedReader(new InputStreamReader(du.getInputStream()));
                String value = in.readLine();
                in.close();
                return value;
            } catch (Exception e) {
                return null;
            }
        }

        public static void printBitmap(Bitmap bm, int bitMarginLeft, int bitMarginTop) {
            printBitmap(bm, bitMarginLeft, bitMarginTop, false);
        }

        public static void printBitmap(Bitmap bm, int bitMarginLeft, int bitMarginTop, boolean alreadyOpen) {
            if ("WIZARPOS 1".equals(android.os.Build.MODEL) || "WIZARPOS_1".equals(android.os.Build.MODEL)) {
                String printerBaud = getSystemProperty("wp.printer.baud");
                if ("115200".equals(printerBaud)) {
                    Log.d("PrintUI", "GSV " + printerBaud);
                    printBitmapGSVMSB(bm, bitMarginLeft, bitMarginTop, alreadyOpen);
                } else if ("9600".equals(printerBaud)) {
                    Log.d("PrintUI", "DC2V" + printerBaud);
                    printBitmapDV2VMSB(bm, bitMarginLeft, bitMarginTop, alreadyOpen);
                } else {
                    Log.d("PrintUI", "GSV MSB" + printerBaud);
                    printBitmapGSVMSB(bm, bitMarginLeft, bitMarginTop, alreadyOpen);
                }
            } else
                printBitmapGSVMSB(bm, bitMarginLeft, bitMarginTop, alreadyOpen);
        }

        private static void printBitmapDV2VMSB(Bitmap bm, int bitMarginLeft, int bitMarginTop, boolean alreadyOpen) {
            byte[] result = generateBitmapArrayDC2V_MSB(bm, bitMarginLeft, bitMarginTop);
            if (!alreadyOpen) {
                PrinterInterface.open();
                PrinterInterface.begin();
            }
            PrinterInterface.write(new byte[] { 0x1B, 0x37, 7, (byte) 240, 2 }, 5);
            int lines = (result.length - DC2V_HEAD) / WIDTH;
            System.arraycopy(new byte[] { 0x12, 0x56, (byte) (lines & 0xff), (byte) ((lines >> 8) & 0xff) }, 0, result, 0, DC2V_HEAD);
            PrinterInterface.write(result, result.length);
            PrinterInterface.write(new byte[] { 0x1B, 0x37, 7, (byte) 0x80, 2 }, 5);
            if (!alreadyOpen) {
                PrinterInterface.end();
                PrinterInterface.close();
            }
        }

        private static void printBitmapDV2VMSBslow(Bitmap bm, int bitMarginLeft, int bitMarginTop, boolean alreadyOpen) {
            byte[] result = generateBitmapArrayDC2V_MSB(bm, bitMarginLeft, bitMarginTop);
            if (!alreadyOpen) {
                PrinterInterface.open();
                PrinterInterface.begin();
            }
            PrinterInterface.write(new byte[] { 0x1B, 0x37, 7, (byte) 240, 2 }, 5);
            Vector<byte[]> vData = checkBufferLength(result);
            for (int i = 0; i < vData.size(); i++) {
                byte[] temp = vData.elementAt(i);
                int lines = temp.length / WIDTH;
                PrinterInterface.write(new byte[] { 0x12, 0x56, (byte) (lines & 0xff), (byte) ((lines >> 8) & 0xff) }, 4);
                PrinterInterface.write(temp, temp.length);
            }
            PrinterInterface.write(new byte[] { 0x1B, 0x37, 7, (byte) 0x80, 2 }, 5);
            if (!alreadyOpen) {
                PrinterInterface.end();
                PrinterInterface.close();
            }
        }

        private static void printBitmapGSVMSB(Bitmap bm, int bitMarginLeft, int bitMarginTop, boolean alreadyOpen) {
            byte[] result = generateBitmapArrayGSV_MSB(bm, bitMarginLeft, bitMarginTop);
            if (!alreadyOpen) {
                PrinterInterface.open();
                PrinterInterface.begin();
            }
            int lines = (result.length - GSV_HEAD) / WIDTH;
            System.arraycopy(new byte[] { 0x1D, 0x76, 0x30, 0x00, 0x30, 0x00, (byte) (lines & 0xff), (byte) ((lines >> 8) & 0xff) }, 0, result, 0, GSV_HEAD);
            PrinterInterface.write(result, result.length);
            if (!alreadyOpen) {
                PrinterInterface.end();
                PrinterInterface.close();
            }
        }

        private static byte[] generateBitmapArrayDC2V_MSB(Bitmap bm, int bitMarginLeft, int bitMarginTop) {
            byte[] result = null;
            int n = bm.getHeight() + bitMarginTop;
            result = new byte[n * WIDTH + DC2V_HEAD];
            for (int y = 0; y < bm.getHeight(); y++) {
                for (int x = 0; x < bm.getWidth(); x++) {
                    if (x + bitMarginLeft < BIT_WIDTH) {
                        int color = bm.getPixel(x, y), alpha = Color.alpha(color), red = Color.red(color), green = Color.green(color), blue = Color.blue(color);
                        if (alpha > 128 && (red < 128 || green < 128 || blue < 128)) {
                            int bitX = bitMarginLeft + x, byteX = bitX / 8, byteY = y + bitMarginTop;
                            result[DC2V_HEAD + byteY * WIDTH + byteX] |= (0x80 >> (bitX - byteX * 8));
                        }
                    } else
                        break;
                }
            }
            return result;
        }

        private static byte[] generateBitmapArrayGSV_MSB(Bitmap bm, int bitMarginLeft, int bitMarginTop) {
            byte[] result = null;
            int n = bm.getHeight() + bitMarginTop, offset = GSV_HEAD;
            result = new byte[n * WIDTH + offset];
            for (int y = 0; y < bm.getHeight(); y++) {
                for (int x = 0; x < bm.getWidth(); x++) {
                    if (x + bitMarginLeft < BIT_WIDTH) {
                        int color = bm.getPixel(x, y), alpha = Color.alpha(color), red = Color.red(color), green = Color.green(color), blue = Color.blue(color);
                        if (alpha > 128 && (red < 128 || green < 128 || blue < 128)) {
                            int bitX = bitMarginLeft + x, byteX = bitX / 8, byteY = y + bitMarginTop;
                            result[offset + byteY * WIDTH + byteX] |= (0x80 >> (bitX - byteX * 8));
                        }
                    } else
                        break;
                }
            }
            return result;
        }

        public static void printBitmapESCStar(Bitmap bm, int bitMarginLeft, int bitMarginTop) {
            Vector<byte[]> vData = generateCmdBitmapArrayESCStar(bm, bitMarginLeft, bitMarginTop);
            PrinterInterface.open();
            PrinterInterface.begin();
            for (int i = 0; i < vData.size(); i++) {
                byte[] temp = vData.elementAt(i);
                if (i > 0) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                PrinterInterface.write(temp, temp.length);
            }
            String text = "This is a text tset...";
            byte[] bufText = text.getBytes();
            PrinterInterface.write(bufText, bufText.length);
            PrinterInterface.write("\n".getBytes(), 1);
            PrinterInterface.end();
            PrinterInterface.close();
        }

        private static Vector<byte[]> generateCmdBitmapArrayESCStar(Bitmap bm, int bitMarginLeft, int bitMarginTop) {
            Vector<byte[]> v = new Vector<>();
            byte[] block = null;
            int line = 0, n = bm.getHeight() + bitMarginTop, pxHeight = bm.getHeight(), pxWidth = bm.getWidth(), blockWidth = BIT_WIDTH;
            while (line < pxHeight) {
                int blockHeight = 0;
                blockHeight = 24;
                v.add(new byte[] { 0x1B, 0x2A, 0x21, (byte) (blockWidth & 0xff), (byte) ((blockWidth >> 8) & 0xff) });
                block = new byte[3 * blockWidth];
                for (int y = 0; y + line < pxHeight && y < blockHeight; y++) {
                    for (int x = 0; x < pxWidth; x++) {
                        int posBit = x * blockHeight + y, posByte = posBit / 8, posBitInByteLeft = posBit % 8;
                        if (x < BIT_WIDTH) {
                            int color = bm.getPixel(x, y + line), alpha = Color.alpha(color), red = Color.red(color), green = Color.green(color), blue = Color.blue(color);
                            if (red < 128 || green < 128 || blue < 128)
                                block[posByte] |= (0x80 >> posBitInByteLeft);
                        } else
                            break;
                    }
                }
                v.add(block);
                v.add(new byte[] { 0x0A });
                line += blockHeight;
            }
            return v;
        }

        private static void tracelogCmdBitmapArrayESCStar(Vector<byte[]> vData) {
            StringBuffer[] arysbBlock = null;
            int blockWidth = 0, m = 0;
            for (int v = 0; v < vData.size(); v++) {
                byte[] buffer = vData.elementAt(v);
                if (buffer.length < 5)
                    continue;
                else if (buffer.length == 5) {
                    if (arysbBlock != null) {
                        for (int i = 0; i < arysbBlock.length; i++)
                            Log.d("PrintPNG", arysbBlock[i].toString());
                    }
                    m = buffer[2];
                    blockWidth = (buffer[3] & 0xff) + (buffer[4] & 0xff) * 256;
                    if (m == 0x01)
                        arysbBlock = new StringBuffer[8];
                    else if (m == 0x21)
                        arysbBlock = new StringBuffer[24];
                    for (int i = 0; i < arysbBlock.length; i++)
                        arysbBlock[i] = new StringBuffer();
                    continue;
                }
                int i = 0;
                while (i < buffer.length) {
                    byte b = buffer[i];
                    for (int pos = 0; pos < 8; pos++) {
                        if ((b & (0x80 >> pos)) != 0)
                            arysbBlock[pos].append('*');
                        else
                            arysbBlock[pos].append(' ');
                    }
                    i++;
                    if (m == 0x21) {
                        b = buffer[i];
                        for (int pos = 0; pos < 8; pos++) {
                            if ((b & (0x80 >> pos)) != 0)
                                arysbBlock[pos + 8].append('*');
                            else
                                arysbBlock[pos + 8].append(' ');
                        }
                        i++;
                        b = buffer[i];
                        for (int pos = 0; pos < 8; pos++) {
                            if ((b & (0x80 >> pos)) != 0)
                                arysbBlock[pos + 16].append('*');
                            else
                                arysbBlock[pos + 16].append(' ');
                        }
                        i++;
                    }
                }
            }
            if (arysbBlock != null) {
                for (int i = 0; i < arysbBlock.length; i++)
                    Log.d("PrintPNG", arysbBlock[i].toString());
            }
        }

        private static byte[] generateBitmapArrayDot8(Bitmap bm, int bitMarginLeft, int bitMarginTop) {
            return null;
        }

        private static void tracelogMSB(byte[] bufMSB, int widthBytes) {
            StringBuffer sbline = new StringBuffer();
            for (int i = 0; i < bufMSB.length; i++) {
                if (i % widthBytes == 0) {
                    Log.d("PrintPNG", sbline.toString());
                    sbline = new StringBuffer();
                }
                byte b = bufMSB[i];
                for (int pos = 0; pos < 8; pos++) {
                    if ((b & (0x80 >> pos)) != 0)
                        sbline.append('*');
                    else
                        sbline.append(' ');
                }
            }
        }

        private static Vector<byte[]> checkBufferLength(byte[] buffer) {
            Vector<byte[]> v = new Vector<>();
            int byteLimit = DOT_LINE_LIMIT * WIDTH;
            if (buffer.length <= byteLimit) {
                v.add(buffer);
                return v;
            } else {
                int offset = DC2V_HEAD;
                while (offset < buffer.length) {
                    byte[] buftemp = new byte[offset + byteLimit < buffer.length ? byteLimit : (buffer.length - offset)];
                    System.arraycopy(buffer, offset, buftemp, 0, buftemp.length);
                    v.add(buftemp);
                    offset += buftemp.length;
                }
                return v;
            }
        }
    }

    public static class PrinterCommand {

        static public byte[] getCmdLf() {
            return new byte[] { (byte) 0x0A };
        }

        static public byte[] getCmdHt() {
            return new byte[] { (byte) 0x09 };
        }

        static public byte[] getCmdFf() {
            return new byte[] { (byte) 0x0c };
        }

        static public byte[] getCmdEscJN(int n) {
            return new byte[] { (byte) 0x1B, (byte) 0x4A, (byte) n };
        }

        static public byte[] getCmdEscFf() {
            return new byte[] { (byte) 0x1b, (byte) 0x0c };
        }

        static public byte[] getCmdEscDN(int n) {
            return new byte[] { (byte) 0x1B, (byte) 0x64, (byte) n };
        }

        static public byte[] getCmdSetSmallFont_CN() {
            return new byte[] { (byte) 0x1C, (byte) 0x21, (byte) 0x01 };
        }

        static public byte[] getCmdCancelSmallFont_CN() {
            return new byte[] { (byte) 0x1C, (byte) 0x21, (byte) 0x00 };
        }

        static public byte[] getCmdSetSmallFont_EN() {
            return new byte[] { (byte) 0x1B, (byte) 0x21, (byte) 0x01 };
        }

        static public byte[] getCmdCancelSmallFont_EN() {
            return new byte[] { (byte) 0x1B, (byte) 0x21, (byte) 0x00 };
        }

        static public byte[] getCmdEscN(int n) {
            return new byte[] { (byte) 0x1B, (byte) 0x3d, (byte) n };
        }

        static public byte[] getCmdEsc2() {
            return new byte[] { (byte) 0x1B, (byte) 0x32 };
        }

        static public byte[] getCmdEsc3N(int n) {
            return new byte[] { (byte) 0x1B, (byte) 0x33, (byte) n };
        }

        static public byte[] getCmdEscAN(int n) {
            return new byte[] { (byte) 0x1B, (byte) 0x61, (byte) n };
        }

        static public byte[] getCmdGsLNlNh(int nL, int nH) {
            return new byte[] { (byte) 0x1D, (byte) 0x4c, (byte) nL, (byte) nH };
        }

        static public byte[] getCmdEsc$NlNh(int nL, int nH) {
            return new byte[] { (byte) 0x1B, (byte) 0x24, (byte) nL, (byte) nH };
        }

        static public byte[] getCmdEsc_N(int n) {
            return new byte[] { (byte) 0x1B, (byte) 0x21, (byte) n };
        }

        static public byte[] getCmdGs_N(int n) {
            return new byte[] { (byte) 0x1D, (byte) 0x21, (byte) n };
        }

        static public byte[] getCmdEscEN(int n) {
            return new byte[] { (byte) 0x1B, (byte) 0x45, (byte) n };
        }

        static public byte[] getCmdEscSpN(int n) {
            return new byte[] { (byte) 0x1B, (byte) 0x20, (byte) n };
        }

        static public byte[] getCmdEscSo() {
            return new byte[] { (byte) 0x1B, (byte) 0x0E };
        }

        static public byte[] getCmdEscDc4() {
            return new byte[] { (byte) 0x1B, (byte) 0x14 };
        }

        static public byte[] getCmdEsc__N(int n) {
            return new byte[] { (byte) 0x1B, (byte) 0x7B, (byte) n };
        }

        static public byte[] getCmdGsBN(int n) {
            return new byte[] { (byte) 0x1D, (byte) 0x42, (byte) n };
        }

        static public byte[] getCmdEsc___N(int n) {
            return new byte[] { (byte) 0x1B, (byte) 0x2D, (byte) n };
        }

        static public byte[] getCmdEsc____N(int n) {
            return new byte[] { (byte) 0x1B, (byte) 0x25, (byte) n };
        }

        static public byte[] getCmdEsc_SNMW() {
            return null;
        }

        static public byte[] getCmdEsc_____N(int n) {
            return new byte[] { (byte) 0x1B, (byte) 0x25, (byte) n };
        }

        static public byte[] getCmdEscRN(int n) {
            return new byte[] { (byte) 0x1B, (byte) 0x52, (byte) n };
        }

        static public byte[] getCmdEscTN(int n) {
            return new byte[] { (byte) 0x1B, (byte) 0x74, (byte) n };
        }

        static public byte[] getCmdEscC5N(int n) {
            return new byte[] { (byte) 0x1B, (byte) 0x63, (byte) 0x35, (byte) n };
        }

        static public byte[] getCmdEsc_() {
            return new byte[] { (byte) 0x1B, (byte) 0x40 };
        }

        static public byte[] getCmdEscVN(int n) {
            return new byte[] { (byte) 0x1B, (byte) 0x76, (byte) n };
        }

        static public byte[] getCmdGsAN(int n) {
            return new byte[] { (byte) 1D, (byte) 61, (byte) n };
        }

        static public byte[] getCmdEscUN(int n) {
            return new byte[] { (byte) 0x1B, (byte) 0x75, (byte) n };
        }

        static public byte[] getCustomTabs() {
            return "  ".getBytes();
        }

        static public byte[] aaa() {
            return new byte[] { (byte) 0x1B, (byte) 0x69 };
        }
    }

    private static final int MSG_ID_SHOW_MESSAGE = 0;
    private Handler handler;
    private String text = "--------------------------------" +
            "\n\tWizar POS Sample\n\t  Ziaplex Inc." +
            "\n--------------------------------" +
            "\n\n" + new Date().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addBaseContentView(UI.createCustomView(this, LinearLayout.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen._10sdp)));
        addBaseContentView(UI.createCustomQRCodeImageView(this));
        addBaseContentView(UI.createMessageView(this, -1, text, null));
        addBaseContentView(UI.createCustomHorizontalSeparator(this));
        handler = new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_ID_SHOW_MESSAGE:
                        UI.showToastMessage(getBaseContext(), msg.obj.toString());
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
        return UI.createMessageView(this, R.drawable.ic_printer, "Print", this);
    }

    @Override
    public void onButtonClick(View view, String text) {
        new Thread(print).start();
    }

    @Override
    public void exit() {
        PrinterInterface.close();
        super.exit();
    }

    private Runnable print = new Runnable() {

        @Override
        public void run() {
            try {
                int print = PrinterInterface.open();
                if (print >= 0) {
                    Bitmap qrBitmap = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.static_qr_code_without_logo);
                    byte[] value = null;
                    try {
                        value = text.getBytes("UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    PrinterInterface.begin();
                    PrinterBitmapUtil.printBitmap(qrBitmap, 75, 0, true);
                    write(value);
                    writeLineBreak(5);
                    PrinterInterface.end();
                } else
                    handler.obtainMessage(MSG_ID_SHOW_MESSAGE, "No Printer Detected").sendToTarget();
                PrinterInterface.close();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
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
}

package com.ziaplex.wizarsample;

import android.content.Context;

import java.text.NumberFormat;
import java.util.StringTokenizer;

public class Util {

    public static String convertToCurrency(Context context, double value) {
        String currencyValue = "0.00";
        if (value > 0.0) {
            String nValue = NumberFormat.getCurrencyInstance().format((int) value).substring(1);
            String nnValue = nValue.substring(0, nValue.length() - 3);
            currencyValue =  nnValue + "." + getDecimalValue(value);
        }
        return context.getResources().getString(R.string.peso_sign) + " " + currencyValue;
    }

    private static String getDecimalValue(double value) {
        String str = "00";
        if (value > 0.0) {
            String nValue = String.valueOf(value);
            StringTokenizer tokens = new StringTokenizer(nValue, ".");
            tokens.nextToken();
            str = tokens.nextToken();
            if (str.length() > 2)
                str = str.substring(0, 2);
            if (str.length() == 1)
                str = str + "0";
        }
        return str;
    }

    private static boolean isEmptyString(String value) {
        if (value != null) {
            for (int i = 0; i < value.length(); i++) {
                char c = value.charAt(i);
                if (Character.getNumericValue(c) != -1)
                    return false;
            }
        }
        return true;
    }

    public static String getStripInfoDetails(byte[] stripInfo, int separatorCountStart, int separatorCountEnd) {
        return getStripInfoDetails(stripInfo, separatorCountStart, separatorCountEnd, -1, -1);
    }

    public static String getStripInfoDetails(byte[] stripInfo, int separatorCountStart, int separatorCountEnd, int offset, int length) {
        StringBuilder str = new StringBuilder();
        String stripInfoValue = new String(stripInfo);
        int start = 0, end = 0, ofst = 0, lth = 0;
        for (int i = 0; i < stripInfoValue.length(); i++) {
            char c = stripInfoValue.charAt(i);
            if (c == 94) {
                if (separatorCountStart > 0)
                    start++;
                if (separatorCountEnd > 0)
                    end++;
            }
            if (end >= separatorCountEnd && separatorCountEnd != -1)
                break;
            if (start == separatorCountStart && c != 94) {
                if (offset != -1) {
                    ofst++;
                    if (ofst >= offset) {
                        str.append(c);
                        if (length > 0) {
                            lth++;
                            if (lth >= length)
                                break;
                        }
                    }
                }
                else {
                    str.append(c);
                    if (length > 0) {
                        lth++;
                        if (lth >= length)
                            break;
                    }
                }
            }
        }
        String value = str.toString();
        if (isEmptyString(value))
            return "";
        return value;
    }

    public static String getStripInfoPAN(byte[] stripInfo) {
        StringBuilder str = new StringBuilder();
        String pan = getStripInfoDetails(stripInfo, 0, 1);
        if (pan != null) {
            for (int i = 0; i < pan.length(); i++) {
                char c = pan.charAt(i);
                if (c > 47 && c < 58)
                    str.append(c);
            }
        }
        return str.toString();
    }
}

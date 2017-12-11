package com.ziaplex.wizarsample.utility.printer.util;

public class PrinterCommand {

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

package com.cloudpos.scanserver.aidl;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class ScanParameter implements Parcelable {

	static final String TAG = ScanParameter.class.getSimpleName();
	public static final String KEY_UI_WINDOW_TOP = "window_top", KEY_UI_WINDOW_LEFT = "window_left", KEY_UI_WINDOW_WIDTH = "window_width", KEY_UI_WINDOW_HEIGHT = "window_height",
			KEY_ENABLE_SCAN_SECTION = "enable_scan_section", KEY_SCAN_SECTION_WIDTH = "scan_section_width", KEY_SCAN_SECTION_HEIGHT = "scan_section_height",
			KEY_DISPLAY_SCAN_LINE = "display_scan_line", KEY_ENABLE_FLASH_ICON = "enable_flash_icon", KEY_ENABLE_SWITCH_ICON = "enable_switch_icon",
			KEY_ENABLE_INDICATOR_LIGHT = "enable_indicator_light", KEY_DECODEFORMAT = "decodeformat", KEY_DECODER_MODE = "decoder_mode",
			KEY_ENABLE_RETURN_IMAGE = "enable_return_image", KEY_CAMERA_INDEX = "camera_index", KEY_SCAN_TIME_OUT = "scan_time_out",
			KEY_SCAN_SECTION_BORDER_COLOR = "scan_section_border_color", KEY_SCAN_SECTION_CORNER_COLOR = "scan_section_corner_color",
			KEY_SCAN_SECTION_LINE_COLOR = "scan_section_line_color", KEY_SCAN_TIP_TEXT = "scan_tip_text", KEY_SCAN_TIP_TEXTSIZE = "scan_tip_textSize",
			KEY_SCAN_TIP_TEXTCOLOR = "scan_tip_textColor", KEY_SCAN_TIP_TEXTMARGIN = "scan_tip_textMargin", KEY_SCAN_WITH_EXPOSURE = "scan_with_exposure",
			KEY_SCAN_MODE = "scan_mode", KEY_FLASH_LIGHT_STATE = "flash_light_state", KEY_INDICATOR_LIGHT_STATE = "indicator_light_state",
            BROADCAST_SET_CAMERA = "com.wizarpos.scanner.setcamera", BROADCAST_SET_FLASHLIGHT = "com.wizarpos.scanner.setflashlight",
            BROADCAST_SET_INDICATOR = "com.wizarpos.scanner.setindicator", BROADCAST_VALUE = "overlay_config";
	private final Bundle bundle;

	public ScanParameter() {
		bundle = new Bundle();
	}

	private ScanParameter(Parcel source) {
		bundle = source.readBundle();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeBundle(bundle);
	}

	public static final Parcelable.Creator<ScanParameter> CREATOR = new Parcelable.Creator<ScanParameter>() {

		@Override
		public ScanParameter createFromParcel(Parcel source) {
			return new ScanParameter(source);
		}

		@Override
		public ScanParameter[] newArray(int size) {
			return new ScanParameter[size];
		}
	};
	
	public String toString() {
		return bundle.toString();
	}
	
	public void set(String key, String value) {
		bundle.putString(key, value);
	}

	public void set(String key, boolean value) {
		bundle.putBoolean(key, value);
	}

	public void set(String key, int value) {
		bundle.putInt(key, value);
	}

	public Object get(String key) {
		return bundle.get(key);
	}

	public String getString(String key) {
		return bundle.getString(key);
	}

	public Bundle getBundle() {
		return bundle;
	}
}

package org.jtb.utwidget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public enum Mode {
	UPTIME(new UptimeBuilder(), R.string.UPTIME, R.string.uptime), WAKETIME(
			new WaketimeBuilder(), R.string.WAKETIME, R.string.waketime), WAKEPERCENT(new WakePercentBuilder(), R.string.WAKEPERCENT, R.string.wakepercent);

	private int titleId;
	private int shortTitleId;
	private ViewBuilder builder;

	private Mode(ViewBuilder builder, int titleId, int shortTitleId) {
		this.builder = builder;
		this.titleId = titleId;
		this.shortTitleId = shortTitleId;
	}

	String getTitle(Context context) {
		return context.getResources().getString(titleId);
	}

	String getShortTitle(Context context) {
		return context.getResources().getString(shortTitleId);
	}

	DisplayMode toDisplayMode(Context context) {
		return new DisplayMode(context, this);
	}

	static List<DisplayMode> toDisplayModes(Context context) {
		List<DisplayMode> dms = new ArrayList<DisplayMode>();
		for (Mode mode : values()) {
			DisplayMode dm = mode.toDisplayMode(context);
			dms.add(dm);
		}
		return dms;
	}

	ViewBuilder getBuilder() {
		return builder;
	}
}

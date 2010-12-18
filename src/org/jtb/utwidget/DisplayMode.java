package org.jtb.utwidget;

import android.content.Context;

class DisplayMode {
	private Context context;
	private Mode mode;
	
	DisplayMode(Context context, Mode mode) {
		this.context = context;
		this.mode = mode;
	}

	Mode getMode() {
		return mode;
	}

	@Override
	public String toString() {
		return mode.getTitle(context);
	}

	
}

package org.jtb.utwidget;

import android.content.Context;

class DisplayTheme {
	private Context context;
	private Theme theme;

	DisplayTheme(Context context, Theme theme) {
		this.context = context;
		this.theme = theme;
	}
	
	Theme getTheme() {
		return theme;
	}

	@Override
	public String toString() {
		return theme.getTitle(context);
	}
}

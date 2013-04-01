package org.jtb.utwidget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

enum Theme {
	COLDMETAL(R.string.COLDMETAL);

	private int titleId;

	private Theme(int titleId) {
		this.titleId = titleId;
	}

	String getTitle(Context context) {
		return context.getResources().getString(titleId);
	}

	DisplayTheme toDisplayTheme(Context context) {
		return new DisplayTheme(context, this);
	}

	static List<DisplayTheme> toDisplayThemes(Context context) {
		List<DisplayTheme> dts = new ArrayList<DisplayTheme>();
		for (Theme theme : values()) {
			DisplayTheme dt = theme.toDisplayTheme(context);
			dts.add(dt);
		}
		return dts;
	}
}

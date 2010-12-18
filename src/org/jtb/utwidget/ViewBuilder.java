package org.jtb.utwidget;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.widget.RemoteViews;

abstract class ViewBuilder {
	abstract RemoteViews build(Context context, int id);
}

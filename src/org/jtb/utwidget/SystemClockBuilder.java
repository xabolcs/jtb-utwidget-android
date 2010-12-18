package org.jtb.utwidget;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.RemoteViews;

abstract class SystemClockBuilder extends ViewBuilder {
	protected abstract long getTime();
	
	@Override
	RemoteViews build(Context context, int id) {
		Prefs prefs = new Prefs(context);
		Theme theme = prefs.getTheme(id);
		Mode mode = prefs.getMode(id);
		
		Layout layout = Layout.valueOf(mode, theme);
		Log.d("utwidget", "using layout: " + layout);
		int layoutId = layout.getLayoutId();
		RemoteViews updateViews = new RemoteViews(context.getPackageName(),
				layoutId);

		
		long time = getTime();
		long ntime = time;

		long days = ntime / DateUtils.DAY_IN_MILLIS;
		// long days = 789;
		ntime -= days * DateUtils.DAY_IN_MILLIS;

		long hours = ntime / DateUtils.HOUR_IN_MILLIS;
		ntime -= hours * DateUtils.HOUR_IN_MILLIS;

		long mins = ntime / DateUtils.MINUTE_IN_MILLIS;
		ntime -= mins * DateUtils.MINUTE_IN_MILLIS;

		// days = 888;
		// hours = 59;
		// mins = 59;

		updateViews.setTextViewText(R.id.days_text, String.format("%3d", days));
		updateViews.setTextViewText(R.id.hours_text,
				String.format("%3d", hours));
		updateViews.setTextViewText(R.id.mins_text, String.format("%3d", mins));

		long mtime = prefs.getMax(mode);
		if (time > mtime) {
			prefs.setMax(time, mode);
			mtime = time;
		}

		long mdays = mtime / DateUtils.DAY_IN_MILLIS;
		// long mdays = 123;
		mtime -= mdays * DateUtils.DAY_IN_MILLIS;

		long mhours = mtime / DateUtils.HOUR_IN_MILLIS;
		// long mhours = 24;
		mtime -= mhours * DateUtils.HOUR_IN_MILLIS;

		long mmins = mtime / DateUtils.MINUTE_IN_MILLIS;
		// long mmins = 55;
		mtime -= mmins * DateUtils.MINUTE_IN_MILLIS;

		// mdays = 888;
		// mhours = 59;
		// mmins = 59;

		updateViews.setTextViewText(R.id.maxdays_text,
				String.format("%3d", mdays));
		updateViews.setTextViewText(R.id.maxhours_text,
				String.format("%3d", mhours));
		updateViews.setTextViewText(R.id.maxmins_text,
				String.format("%3d", mmins));

		updateViews.setTextViewText(R.id.uptime_text, mode.getShortTitle(context));
		
		Intent i = UptimeWidget.getUpdateIntent(context, id);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
		updateViews.setOnClickPendingIntent(R.id.widget, pi);
		
		return updateViews;
	}
	
}

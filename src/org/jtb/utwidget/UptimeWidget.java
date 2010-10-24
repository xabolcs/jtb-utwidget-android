package org.jtb.utwidget;

import java.util.HashMap;
import java.util.Map;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.os.SystemClock;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.RemoteViews;

public class UptimeWidget extends AppWidgetProvider {
	private static Map<String, Integer> THEME_MAP = new HashMap<String, Integer>() {
		{
			put("darktransparent", R.layout.widget_darktransparent);
			put("lighttransparent", R.layout.widget_lighttransparent);
			put("darktranslucent", R.layout.widget_darktranslucent);
			put("lighttranslucent", R.layout.widget_lighttranslucent);
		}
	};

	@Override
	public void onUpdate(Context context, AppWidgetManager mgr,
			int[] appWidgetIds) {
		for (int id : appWidgetIds) {
			Log.d("UptimeWidget", "updating: " + id);
			update(context, mgr, id);
			Log.d("UptimeWidget", "scheduling: " + id);
			schedule(context, id);
		}
	}

	@Override
	public void onDeleted(Context context, int[] ids) {
		for (int id : ids) {
			Log.d("UptimeWidget", "cancelled: " + id);
			cancel(context, id);
		}		
	}

	private static Intent getUpdateIntent(Context context, int id) {
		Intent updateIntent = new Intent();
		updateIntent.setClass(context, UptimeWidget.class);
		updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
		updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[] { id });
		updateIntent.setData(Uri.parse("org.jtb.utwidget/" + id));

		return updateIntent;
	}
	
	@Override
	public void onReceive(Context context, Intent i) {
		Log.d("UptimeWidget", "received intent: " + i);
		super.onReceive(context, i);
	}
	
	private void schedule(Context context, int id) {
		Intent updateIntent = getUpdateIntent(context, id);
		
		PendingIntent pendingIntent = PendingIntent.getBroadcast(
			    context, 0, updateIntent, 
			    PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
				SystemClock.elapsedRealtime() + getUpdateInterval(), getUpdateInterval(),
				pendingIntent);
		/*
		alarmManager.set(AlarmManager.ELAPSED_REALTIME,
				SystemClock.elapsedRealtime() + 10000,
				pendingIntent);
		*/
	}

	private void cancel(Context context, int id) {
		Intent updateIntent = getUpdateIntent(context, id);
		
		PendingIntent pendingIntent = PendingIntent.getBroadcast(
			    context, 0, updateIntent, 
			    PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(pendingIntent);
	}

	private static long getUpdateInterval() {
		long ert = SystemClock.elapsedRealtime();

		long days = ert / DateUtils.DAY_IN_MILLIS;
		if (days > 7) {
			return AlarmManager.INTERVAL_HOUR;
		}
		return AlarmManager.INTERVAL_FIFTEEN_MINUTES;
	}

	public static void update(Context context, AppWidgetManager mgr, int id) {
		RemoteViews updateViews = buildUpdate(context, id);
		mgr.updateAppWidget(id, updateViews);
	}

	private static RemoteViews buildUpdate(Context context, int id) {
		Prefs prefs = new Prefs(context);
		String theme = prefs.getTheme(id);
		Log.d("UptimeWidget", "using theme: " + theme);

		int layoutId = THEME_MAP.get(theme);
		RemoteViews updateViews = new RemoteViews(context.getPackageName(),
				layoutId);

		long ert = SystemClock.elapsedRealtime();
		long nert = ert;

		long days = nert / DateUtils.DAY_IN_MILLIS;
		// long days = 789;
		nert -= days * DateUtils.DAY_IN_MILLIS;

		long hours = nert / DateUtils.HOUR_IN_MILLIS;
		nert -= hours * DateUtils.HOUR_IN_MILLIS;

		long mins = nert / DateUtils.MINUTE_IN_MILLIS;
		nert -= mins * DateUtils.MINUTE_IN_MILLIS;

		//days = 888;
		//hours = 59;
		//mins = 59;

		updateViews.setTextViewText(R.id.days_text,
				String.format("%3d", days));
		updateViews.setTextViewText(R.id.hours_text,
				String.format("%3d", hours));
		updateViews.setTextViewText(R.id.mins_text,
				String.format("%3d", mins));

		long mert = prefs.getMaxUptime();
		if (ert > mert) {
			prefs.setMaxUptime(ert);
			mert = ert;
		}

		long mdays = mert / DateUtils.DAY_IN_MILLIS;
		// long mdays = 123;
		mert -= mdays * DateUtils.DAY_IN_MILLIS;

		long mhours = mert / DateUtils.HOUR_IN_MILLIS;
		// long mhours = 24;
		mert -= mhours * DateUtils.HOUR_IN_MILLIS;

		long mmins = mert / DateUtils.MINUTE_IN_MILLIS;
		// long mmins = 55;
		mert -= mmins * DateUtils.MINUTE_IN_MILLIS;

		//mdays = 888;
		//mhours = 59;
		//mmins = 59;
		
		updateViews.setTextViewText(R.id.maxdays_text,
				String.format("%3d", mdays));
		updateViews.setTextViewText(R.id.maxhours_text,
				String.format("%3d", mhours));
		updateViews.setTextViewText(R.id.maxmins_text,
				String.format("%3d", mmins));

		return updateViews;
	}
}

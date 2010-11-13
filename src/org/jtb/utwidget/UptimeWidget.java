package org.jtb.utwidget;

import java.util.HashMap;
import java.util.Map;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
			put("coldmetal", R.layout.widget_coldmetal);
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
		updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
				new int[] { id });
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

		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
				updateIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
				SystemClock.elapsedRealtime() + getUpdateInterval(),
				getUpdateInterval(), pendingIntent);
		/*
		 * alarmManager.set(AlarmManager.ELAPSED_REALTIME,
		 * SystemClock.elapsedRealtime() + 10000, pendingIntent);
		 */
	}

	private void cancel(Context context, int id) {
		Intent updateIntent = getUpdateIntent(context, id);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
				updateIntent, PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(pendingIntent);
	}

	private static long getUpdateInterval() {
		return AlarmManager.INTERVAL_FIFTEEN_MINUTES;
	}

	public static void update(Context context, AppWidgetManager mgr, int id) {
		RemoteViews updateViews = buildUpdate(context, id);
		mgr.updateAppWidget(id, updateViews);
	}

	private static RemoteViews buildUpdate(Context context, int id) {
		Prefs prefs = new Prefs(context);
		String theme = prefs.getTheme(id);
		Mode mode = prefs.getMode(id);
		
		Log.d("UptimeWidget", "using theme: " + theme);

		int layoutId = THEME_MAP.get(theme);
		RemoteViews updateViews = new RemoteViews(context.getPackageName(),
				layoutId);

		
		long time;
		if (mode == Mode.UPTIME) {
			time = SystemClock.elapsedRealtime();
		} else {
			time = SystemClock.uptimeMillis();
		}
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

		updateViews.setTextViewText(R.id.uptime_text, context.getResources()
				.getString(mode.getStringId()));
		
		Intent i = getUpdateIntent(context, id);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
		updateViews.setOnClickPendingIntent(R.id.widget, pi);
		
		return updateViews;
	}
}

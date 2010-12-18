package org.jtb.utwidget;

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

	static Intent getUpdateIntent(Context context, int id) {
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
		Prefs prefs = new Prefs(context);
		Mode mode = prefs.getMode(id);
		ViewBuilder builder = mode.getBuilder();
		RemoteViews views = builder.build(context, id);
		mgr.updateAppWidget(id, views);
	}
}

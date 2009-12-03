package org.jtb.utwidget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.RemoteViews;

public class UptimeWidget extends AppWidgetProvider {
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		context.startService(new Intent(context, UpdateService.class));
		schedule(context);
	}

	private void schedule(Context context) {
		Intent updateIntent = new Intent();
		updateIntent.setClass(context, UpdateService.class);
		PendingIntent pendingIntent = PendingIntent.getService(context, 0,
				updateIntent, 0);
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
				SystemClock.elapsedRealtime(),
				AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);
	}

	public static class UpdateService extends Service {
		@Override
		public void onStart(Intent intent, int startId) {
			Log.d("UptimeWidget", "updating widget");

			// Build the widget update for today
			RemoteViews updateViews = buildUpdate(this);

			// Push update for this widget to the home screen
			ComponentName thisWidget = new ComponentName(this,
					UptimeWidget.class);
			AppWidgetManager manager = AppWidgetManager.getInstance(this);
			manager.updateAppWidget(thisWidget, updateViews);

			stopSelf();
		}

		public RemoteViews buildUpdate(Context context) {
			RemoteViews updateViews = new RemoteViews(context.getPackageName(),
					R.layout.widget_uptime);

			long ert = SystemClock.elapsedRealtime();

			long days = ert / DateUtils.DAY_IN_MILLIS;
			ert -= days * DateUtils.DAY_IN_MILLIS;

			long hours = ert / DateUtils.HOUR_IN_MILLIS;
			ert -= hours * DateUtils.HOUR_IN_MILLIS;

			long mins = ert / DateUtils.MINUTE_IN_MILLIS;
			ert -= hours * DateUtils.MINUTE_IN_MILLIS;

			updateViews.setTextViewText(R.id.days_text, String.format("%3d",
					days));
			updateViews.setTextViewText(R.id.hours_text, String.format("%3d",
					hours));
			updateViews.setTextViewText(R.id.mins_text, String.format("%3d",
					mins));

			return updateViews;
		}

		@Override
		public IBinder onBind(Intent intent) {
			// We don't need to bind to this service
			return null;
		}
	}

}

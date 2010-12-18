package org.jtb.utwidget;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.preference.PreferenceManager;

public class Prefs {
	private Context context = null;

	public Prefs(Context context) {
		this.context = context;
	}

	private String getString(String key, String def) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		String s = prefs.getString(key, def);
		return s;
	}

	private long getLong(String key, long def) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		long l = Long.parseLong(prefs.getString(key, Long.toString(def)));
		return l;
	}

	private void setString(String key, String val) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor e = prefs.edit();
		e.putString(key, val);
		e.commit();
	}

	private void setLong(String key, long val) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor e = prefs.edit();
		e.putString(key, Long.toString(val));
		e.commit();
	}
	
	private void remove(String key) {
		SharedPreferences prefs = PreferenceManager
		.getDefaultSharedPreferences(context);
		Editor e = prefs.edit();
		e.remove(key);
		e.commit();
	}

	public long getMax(Mode mode) {
		// backwards compatibility
		// if the user has the legacy preference
		// "maxUptime", set it in the new format
		// and remove the old legacy pref
		
		if (mode == Mode.UPTIME) {
			long maxUptime = getLong("maxUptime", -1);
			if (maxUptime != -1) {
				setMax(maxUptime, Mode.UPTIME);
				remove("maxUptime");
			}
		}
		
		return getLong("max." + mode.toString(), 0);
	}

	public void setMax(long max, Mode mode) {
		setLong("max." + mode.toString(), max);
	}

	public Theme getTheme(int id) {
		String s = getString("theme." + id, "lighttransparent");
		s = s.toUpperCase();
		return Theme.valueOf(s);
	}

	public void setTheme(Theme theme, int id) {
		setString("theme." + id, theme.toString());
	}

	public Mode getMode(int id) {
		String s = getString("mode." + id, "UPTIME");
		return Mode.valueOf(s);
	}

	public void setMode(Mode mode, int id) {
		setString("mode." + id, mode.toString());
	}
}

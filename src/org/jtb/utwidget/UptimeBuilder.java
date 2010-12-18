package org.jtb.utwidget;

import android.os.SystemClock;

public class UptimeBuilder extends SystemClockBuilder {

	@Override
	protected long getTime() {
		return SystemClock.elapsedRealtime();
	}
}

package org.jtb.utwidget;

import android.os.SystemClock;

public class WaketimeBuilder extends SystemClockBuilder {

	@Override
	protected long getTime() {
		return SystemClock.uptimeMillis();
	}

}

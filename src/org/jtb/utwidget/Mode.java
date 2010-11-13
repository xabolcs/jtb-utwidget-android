package org.jtb.utwidget;

public enum Mode {
	UPTIME(R.string.uptime), WAKETIME(R.string.waketime);
	
	private int stringId;
	
	private Mode(int stringId) {
		this.stringId = stringId;
	}
	
	public int getStringId() {
		return stringId;
	}
}

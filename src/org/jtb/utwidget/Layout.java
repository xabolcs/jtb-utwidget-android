package org.jtb.utwidget;

enum Layout {
	UPTIME_COLDMETAL(Mode.UPTIME, Theme.COLDMETAL, R.layout.widget_coldmetal), WAKETIME_COLDMETAL(
			Mode.WAKETIME, Theme.COLDMETAL, R.layout.widget_coldmetal), WAKEPERCENT_COLDMETAL(
			Mode.WAKEPERCENT, Theme.COLDMETAL,
			R.layout.widget_wkprcnt_coldmetal), ;

	private Mode mode;
	private Theme theme;
	private int layoutId;

	Layout(Mode mode, Theme theme, int layoutId) {
		this.mode = mode;
		this.theme = theme;
		this.layoutId = layoutId;
	}

	static Layout valueOf(Mode mode, Theme theme) {
		for (Layout layout : values()) {
			if (layout.mode == mode && layout.theme == theme) {
				return layout;
			}
		}
		return null;
	}

	int getLayoutId() {
		return layoutId;
	}
}

package org.jtb.utwidget;

enum Layout {
	UPTIME_COLDMETAL(Mode.UPTIME, Theme.COLDMETAL, R.layout.widget_coldmetal),
	WAKETIME_COLDMETAL(Mode.WAKETIME, Theme.COLDMETAL, R.layout.widget_coldmetal),
	WAKEPERCENT_COLDMETAL(Mode.WAKEPERCENT, Theme.COLDMETAL, R.layout.widget_wkprcnt_coldmetal),

	UPTIME_DARKTRANSPARENT(Mode.UPTIME, Theme.DARKTRANSPARENT, R.layout.widget_darktransparent),
	WAKETIME_DARKTRANSPARENT(Mode.WAKETIME, Theme.DARKTRANSPARENT, R.layout.widget_darktransparent),
	WAKEPERCENT_DARKTRANSPARENT(Mode.WAKEPERCENT, Theme.DARKTRANSPARENT, R.layout.widget_wkprcnt_darktransparent),

	UPTIME_DARKTRANSLUCENT(Mode.UPTIME, Theme.DARKTRANSLUCENT, R.layout.widget_darktranslucent),
	WAKETIME_DARKTRANSLUCENT(Mode.WAKETIME, Theme.DARKTRANSLUCENT, R.layout.widget_darktranslucent),
	WAKEPERCENT_DARKTRANSLUCENT(Mode.WAKEPERCENT, Theme.DARKTRANSLUCENT, R.layout.widget_wkprcnt_darktranslucent),

	UPTIME_LIGHTTRANSPARENT(Mode.UPTIME, Theme.LIGHTTRANSPARENT, R.layout.widget_lighttransparent),
	WAKETIME_LIGHTTRANSPARENT(Mode.WAKETIME, Theme.LIGHTTRANSPARENT, R.layout.widget_lighttransparent),
	WAKEPERCENT_LIGHTTRANSPARENT(Mode.WAKEPERCENT, Theme.LIGHTTRANSPARENT, R.layout.widget_wkprcnt_lighttransparent),

	UPTIME_LIGHTTRANSLUCENT(Mode.UPTIME, Theme.LIGHTTRANSLUCENT, R.layout.widget_lighttranslucent),
	WAKETIME_LIGHTTRANSLUCENT(Mode.WAKETIME, Theme.LIGHTTRANSLUCENT, R.layout.widget_lighttranslucent),
	WAKEPERCENT_LIGHTTRANSLUCENT(Mode.WAKEPERCENT, Theme.LIGHTTRANSLUCENT, R.layout.widget_wkprcnt_lighttranslucent),
	;
	
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

package org.jtb.utwidget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;

public class ConfigActivity extends Activity {
	private Prefs mPrefs;
	private RadioButton mDarkTransparentRadioButton;
	private RadioButton mLightTransparentRadioButton;
	private RadioButton mDarkTranslucentRadioButton;
	private RadioButton mLightTranslucentRadioButton;
	private RadioButton mColdMetalRadioButton;
	private RadioButton mUptimeRadioButton;
	private RadioButton mWaketimeRadioButton;
	private Button mFinishedButton;
	
	private Mode mMode = Mode.UPTIME;
	private String mTheme = "darktransparent";
	
	private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.config);
		
	    setResult(RESULT_CANCELED);
		
        // Find the widget id from the intent. 
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If they gave us an intent without the widget id, just bail.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }
		mPrefs = new Prefs(this);

		OnClickListener themeOcl = new OnClickListener() {

			@Override
			public void onClick(View v) {
				mTheme = (String)v.getTag();
			}
		};

		mDarkTransparentRadioButton = (RadioButton) findViewById(R.id.darkTransparentRadioButton);
		mDarkTransparentRadioButton.setOnClickListener(themeOcl);
		mDarkTransparentRadioButton.setChecked(true);
		
		mDarkTranslucentRadioButton = (RadioButton) findViewById(R.id.darkTranslucentRadioButton);
		mDarkTranslucentRadioButton.setOnClickListener(themeOcl);

		mLightTransparentRadioButton = (RadioButton) findViewById(R.id.lightTransparentRadioButton);
		mLightTransparentRadioButton.setOnClickListener(themeOcl);

		mLightTranslucentRadioButton = (RadioButton) findViewById(R.id.lightTranslucentRadioButton);
		mLightTranslucentRadioButton.setOnClickListener(themeOcl);

		mColdMetalRadioButton = (RadioButton) findViewById(R.id.coldMetalRadioButton);
		mColdMetalRadioButton.setOnClickListener(themeOcl);
		
		OnClickListener modeOcl = new OnClickListener() {

			@Override
			public void onClick(View v) {
				mMode = Mode.valueOf((String)v.getTag());
			}
		};

		mUptimeRadioButton = (RadioButton) findViewById(R.id.uptimeRadioButton);
		mUptimeRadioButton.setOnClickListener(modeOcl);
		mUptimeRadioButton.setChecked(true);
		
		mWaketimeRadioButton = (RadioButton) findViewById(R.id.waketimeRadioButton);
		mWaketimeRadioButton.setOnClickListener(modeOcl);
		
		mFinishedButton = (Button) findViewById(R.id.config_finished_button);
		mFinishedButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finished();
			}
		});
	}
	
	private void finished() {
		mPrefs.setTheme(mTheme, mAppWidgetId);
		mPrefs.setMode(mMode, mAppWidgetId);

		AppWidgetManager mgr = AppWidgetManager.getInstance(ConfigActivity.this);				
		UptimeWidget.update(ConfigActivity.this, mgr, mAppWidgetId);
		
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
		finish();		
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}
}

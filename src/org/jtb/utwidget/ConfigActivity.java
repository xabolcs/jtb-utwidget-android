package org.jtb.utwidget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;

public class ConfigActivity extends Activity {
	private Prefs mPrefs;
	private RadioButton mDarkTransparentRadioButton;
	private RadioButton mLightTransparentRadioButton;
	private RadioButton mDarkTranslucentRadioButton;
	private RadioButton mLightTranslucentRadioButton;
	private RadioButton mColdMetalRadioButton;
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

		OnClickListener ocl = new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish((String)v.getTag());
			}
		};

		mDarkTransparentRadioButton = (RadioButton) findViewById(R.id.darkTransparentRadioButton);
		mDarkTransparentRadioButton.setOnClickListener(ocl);

		mDarkTranslucentRadioButton = (RadioButton) findViewById(R.id.darkTranslucentRadioButton);
		mDarkTranslucentRadioButton.setOnClickListener(ocl);

		mLightTransparentRadioButton = (RadioButton) findViewById(R.id.lightTransparentRadioButton);
		mLightTransparentRadioButton.setOnClickListener(ocl);

		mLightTranslucentRadioButton = (RadioButton) findViewById(R.id.lightTranslucentRadioButton);
		mLightTranslucentRadioButton.setOnClickListener(ocl);

		mColdMetalRadioButton = (RadioButton) findViewById(R.id.coldMetalRadioButton);
		mColdMetalRadioButton.setOnClickListener(ocl);
	}
	
	private void finish(String theme) {
		mPrefs.setTheme(theme, mAppWidgetId);

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

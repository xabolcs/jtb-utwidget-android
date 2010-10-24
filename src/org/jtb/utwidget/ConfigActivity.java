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
	    
		mDarkTransparentRadioButton = (RadioButton) findViewById(R.id.darkTransparentRadioButton);
		OnClickListener ocl = new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish("darktransparent");
			}
		};
		mDarkTransparentRadioButton.setOnClickListener(ocl);

		mDarkTranslucentRadioButton = (RadioButton) findViewById(R.id.darkTranslucentRadioButton);
		ocl = new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish("darktranslucent");
			}
		};
		mDarkTranslucentRadioButton.setOnClickListener(ocl);

		mLightTransparentRadioButton = (RadioButton) findViewById(R.id.lightTransparentRadioButton);
		ocl = new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish("lighttransparent");
			}
		};
		mLightTransparentRadioButton.setOnClickListener(ocl);

		mLightTranslucentRadioButton = (RadioButton) findViewById(R.id.lightTranslucentRadioButton);
		ocl = new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish("lighttranslucent");
			}
		};
		mLightTranslucentRadioButton.setOnClickListener(ocl);
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

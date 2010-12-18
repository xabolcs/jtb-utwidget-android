package org.jtb.utwidget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;

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
	private Theme mTheme = Theme.DARKTRANSPARENT;
	
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

		Spinner themeSpinner = (Spinner) findViewById(R.id.theme_spinner);
		ArrayAdapter<DisplayTheme> dtAdapter = new ArrayAdapter<DisplayTheme>(
				this, android.R.layout.simple_spinner_item, Theme
						.toDisplayThemes(this));
		dtAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		themeSpinner.setAdapter(dtAdapter);
		themeSpinner.setSelection(0);
		themeSpinner.setSelected(false);
		themeSpinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					public void onItemSelected(AdapterView parent, View v,
							int position, long id) {
						DisplayTheme dt = (DisplayTheme) parent
								.getSelectedItem();
						mTheme = dt.getTheme();
					}

					public void onNothingSelected(AdapterView parent) {
					}
				});

		Spinner modeSpinner = (Spinner) findViewById(R.id.mode_spinner);
		ArrayAdapter<DisplayMode> dmAdapter = new ArrayAdapter<DisplayMode>(
				this, android.R.layout.simple_spinner_item, Mode
						.toDisplayModes(this));
		dmAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		modeSpinner.setAdapter(dmAdapter);
		modeSpinner.setSelection(0);
		themeSpinner.setSelected(false);
		modeSpinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					public void onItemSelected(AdapterView parent, View v,
							int position, long id) {
						DisplayMode dm = (DisplayMode) parent
								.getSelectedItem();
						mMode = dm.getMode();
					}

					public void onNothingSelected(AdapterView parent) {
					}
				});

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

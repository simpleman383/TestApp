package com.simpleman383.testapp;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private boolean isURLValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this,
                new String[] { Manifest.permission.RECEIVE_SMS,
                        Manifest.permission.READ_SMS,
                        Manifest.permission.INTERNET }, 10);

        SMSLogger.createLogger();
        initializeControls();
        initLog();
        loadLastState();
        startService(new Intent(this, EmptyService.class)); // пустой сервис, чтобы поддерживать приложение в списке Running
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence("URL", ((EditText)findViewById(R.id.editText)).getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String url = savedInstanceState.getString("URL");
        ((EditText)findViewById(R.id.editText)).setText(url);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveLastState();
        PackageManager pm  = MainActivity.this.getPackageManager();
        ComponentName componentName = new ComponentName(MainActivity.this, SMSMonitor.class);
        pm.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        stopService(new Intent(this, EmptyService.class));
    }

    private void loadLastState(){
        String url = DataSaver.getInstance().getLastState(getApplicationContext(), DataSaver.STATE.URL);
        Boolean enabled = Boolean.parseBoolean(DataSaver.getInstance().getLastState(getApplicationContext(), DataSaver.STATE.SWITCH));
        ((EditText)findViewById(R.id.editText)).setText(url);
        ((ToggleButton)findViewById(R.id.toggleButton)).setChecked(enabled);
        isURLValid = enabled;
    }

    private void saveLastState(){
        DataSaver.getInstance().SaveState(getApplicationContext(), ((EditText)findViewById(R.id.editText)).getText().toString(), DataSaver.STATE.URL);
        DataSaver.getInstance().SaveState(getApplicationContext(), String.valueOf(((ToggleButton)findViewById(R.id.toggleButton)).isChecked()), DataSaver.STATE.SWITCH);
    }

    private void initializeControls(){
        final ToggleButton toggleButton = (ToggleButton)findViewById(R.id.toggleButton);
        toggleButton.setEnabled(isURLValid);

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PackageManager pm  = MainActivity.this.getPackageManager();
                ComponentName componentName = new ComponentName(MainActivity.this, SMSMonitor.class);
                int enabled = isChecked ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED :
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
                pm.setComponentEnabledSetting(componentName,enabled,PackageManager.DONT_KILL_APP);
            }
        });

        final EditText urlEditor = (EditText)findViewById(R.id.editText);
        urlEditor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove("ForwardTo");
                editor.commit();

                String text = s.toString();
                try {
                    String url_pattern = "^(?i)(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
                    Pattern pattern = Pattern.compile(url_pattern);
                    Matcher matcher = pattern.matcher(text);
                    isURLValid = matcher.matches();
                } catch (Exception ex) {
                    Log.e("MainActivity", "initializeControls.onTextChanged Error: " + ex);
                    isURLValid = false;
                }

                editor.putString("ForwardTo", text);
                editor.commit();
            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean prevButtonState = toggleButton.isChecked();
                toggleButton.setEnabled(isURLValid);
                toggleButton.setChecked(prevButtonState && !isURLValid ? false : prevButtonState);
            }
        });
    }

    private void initLog(){
        ImageButton btn = (ImageButton)findViewById(R.id.log_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LogActivity.class);
                startActivity(intent);
            }
        });

    }
}

package com.simpleman383.testapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.logging.Logger;

public class LogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        initWidgets();
    }

    private void initWidgets(){
        String data = SMSLogger.createLogger().readAll(getApplicationContext());

        Button clearButton = (Button)findViewById(R.id.clear_button);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SMSLogger.createLogger().clearLogger(getApplicationContext());
                TextView logWindow = (TextView)findViewById(R.id.log_window);
                logWindow.setText("");
            }
        });

        TextView logWindow = (TextView)findViewById(R.id.log_window);
        logWindow.setText("");
        logWindow.setText(data);
    }
}

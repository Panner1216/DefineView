package com.panner.myswitchbutton;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private CustomSwitchView mSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSwitch = (CustomSwitchView) findViewById(R.id.custom_switch_view);
        mSwitch.setswitchBackgroundBitmap(R.drawable.switch_background);
        mSwitch.setswitchForegroundBitmap(R.drawable.switch_foreground);

    }
}

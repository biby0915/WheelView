package com.zby.wheeldemo;

import android.app.Activity;
import android.os.Bundle;

import com.zby.wheelview.WheelView;

/**
 * @author ZhuBingYang
 * @date 2019-06-11.
 */
public class circleActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_wheel);

        ((WheelView) findViewById(R.id.wheel)).setDataInRange(50, 66, 1, true);
        ((WheelView) findViewById(R.id.wheel2)).setIntegerFormat("%d");
        ((WheelView) findViewById(R.id.wheel2)).setDataInRange(50, 66, 1, true);
    }
}

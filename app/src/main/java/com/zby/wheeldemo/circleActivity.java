package com.zby.wheeldemo;

import android.app.Activity;
import android.os.Bundle;

import com.zby.wheelview.WheelView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZhuBingYang
 * @date 2019-06-11.
 */
public class circleActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_circle);

        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(i);
        }
        WheelView<Integer> wheelView = findViewById(R.id.wheel);
        wheelView.setData(list);
    }
}

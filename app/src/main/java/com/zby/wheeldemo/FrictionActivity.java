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
public class FrictionActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_demo);

        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            list.add(i);
        }

        WheelView<Integer> w1 = findViewById(R.id.w1);
        WheelView<Integer> w2 = findViewById(R.id.w2);

        w1.setData(list);
        w2.setData(list);

        w2.setFriction(0.015f);
    }
}

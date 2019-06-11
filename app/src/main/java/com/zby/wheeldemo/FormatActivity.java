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
public class FormatActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_format);

        WheelView<Integer> integerWheelView = findViewById(R.id.integerWheel);
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            list.add(i);
        }
        integerWheelView.setData(list);

        WheelView<Double> wheelView = findViewById(R.id.wheel);
        List<Double> doubleList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            doubleList.add(i + i / 123d);
        }
        wheelView.setData(doubleList);
    }
}

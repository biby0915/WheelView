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
        integerWheelView.setDataInRange(0, 100, 1, true);

        WheelView<Float> wheelView = findViewById(R.id.wheel);
//        List<Float> doubleList = new ArrayList<>();
//        for (int i = 0; i < 100; i++) {
//            doubleList.add(i + 1000 + i / 123d);d
//        }
        wheelView.setDataInRange(0f, 9999f, 0.01f, true);
        wheelView.setCyclic(true);
        wheelView.setDecimalDigitNumber(2);
        wheelView.setOnItemSelectedListener(new WheelView.OnItemSelectedListener<Float>() {
            @Override
            public void onItemSelected(Float data, int position) {

            }

            @Override
            public void onWheelSelecting(Float data, int position) {
                System.out.println(data);
            }
        });
    }
}

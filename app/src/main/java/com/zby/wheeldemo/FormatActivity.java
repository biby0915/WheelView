package com.zby.wheeldemo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import com.zby.wheelview.WheelView;
import com.zby.wheelview.extention.WheelMaskLayer;
import com.zby.wheelview.extention.WheelSuffixLayer;

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

//        WheelView<Integer> integerWheelView = findViewById(R.id.integerWheel);
//        List<Integer> list = new ArrayList<>();
//        for (int i = 0; i < 1000; i++) {
//            list.add(i);
//        }
//        integerWheelView.setDataInRange(0, 100, 1, true);

        WheelView<Float> wheelView = findViewById(R.id.wheel);
//        List<Float> doubleList = new ArrayList<>();
//        for (int i = 0; i < 100; i++) {
//            doubleList.add(i + 1000 + i / 123d);d
//        }
        wheelView.setDataInRange(0f, 100f, 0.1f, true);
        wheelView.setCyclic(true);
//        wheelView.setVisibleItemNum(9);
        wheelView.setDecimalDigitNumber(2);
        wheelView.addWheelLayer(new WheelSuffixLayer("%", 12, Color.BLACK, 4));
        wheelView.addWheelLayer(new WheelMaskLayer(new int[]{0xFFF0FF0F, 0x00FFFFFF, 0xFFFFFFFF}, new float[]{0, .5f, 1}));
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

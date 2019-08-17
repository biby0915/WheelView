package com.zby.wheeldemo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import com.zby.wheelview.WheelView;
import com.zby.wheelview.extention.WheelMaskLayer;
import com.zby.wheelview.extention.WheelSuffixLayer;

/**
 * @author ZhuBingYang
 * @date 2019-06-11.
 */
public class FormatActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_wheel);

        WheelView<Integer> integerWheelView = findViewById(R.id.wheel2);
        integerWheelView.setDataInRange(0, 1000, 1, true);

        WheelView<Double> wheelView = findViewById(R.id.wheel);
//        List<Double> doubleList = new ArrayList<>();
//        for (int i = 0; i < 100; i++) {
//            doubleList.add(i * 12345 + 1000 + i / 123d);
//        }
        wheelView.setDataInRange(1.0d,100d,0.1d,true);
        wheelView.setDefaultSelectItem(48d);
//        wheelView.setDataInRange(0f, 100f, 0.1f, true);
        wheelView.setCyclic(true);
//        wheelView.setVisibleItemNum(9);
//        wheelView.setDecimalDigitNumber(2);
        wheelView.addWheelLayer(new WheelSuffixLayer("%", 12, Color.BLACK, 4));
        wheelView.addWheelLayer(new WheelMaskLayer(new int[]{0xFFFFFFFF, 0x00FFFFFF, 0xFFFFFFFF}, new float[]{0, .5f, 1}));
    }
}

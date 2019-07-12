package com.zby.wheeldemo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import com.zby.wheelview.extention.WheelMaskLayer;
import com.zby.wheelview.extention.WheelSuffixLayer;
import com.zby.wheelview.WheelView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZhuBingYang
 * @date 2019-06-11.
 */
public class MixActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mix);

        WheelView<Integer> wheelView = findViewById(R.id.wheel);

        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            list.add(i);
        }
        wheelView.setData(list);

        wheelView.addWheelLayer(new WheelMaskLayer(new int[]{0xffffff00, 0x00ffffff, 0xffffff00}, new float[]{0, .5f, 1}));
        wheelView.addWheelLayer(new WheelSuffixLayer("℃", 16, Color.BLACK, 10));

    }
}

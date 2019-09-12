package com.zby.wheeldemo;

import android.app.Activity;
import android.os.Bundle;

import com.zby.wheelview.WheelView;
import com.zby.wheelview.extention.WheelMaskLayer;

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

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            list.add("杭州");
        }

        WheelView<String> w1 = findViewById(R.id.w1);
        WheelView<String> w2 = findViewById(R.id.w2);
        WheelView<String> w3 = findViewById(R.id.w3);

        w1.setData(list);
        w2.setData(list);
        w3.setData(list);

        WheelMaskLayer layer = new WheelMaskLayer(new int[]{0xFFFFFFFF, 0x00FFFFFF, 0xFFFFFFFF}, new float[]{0, .5f, 1});
        w1.addWheelLayer(layer);
        w2.addWheelLayer(layer);
        w3.addWheelLayer(layer, WheelView.LAYER_POSITION_BOTTOM);


        w2.setFriction(0.015f);
    }
}

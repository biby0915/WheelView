package com.zby.wheeldemo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.zby.wheelview.WheelMaskLayer;
import com.zby.wheelview.WheelSuffixLayer;
import com.zby.wheelview.WheelView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WheelView<Integer> wheelView = findViewById(R.id.wheel);
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            list.add(i);
        }
        wheelView.setData(list);
        wheelView.addWheelLayer(new WheelMaskLayer(new int[]{0xFFFFFFFF,0x88FFFFFF,0xFFFFFFFF},new float[]{0,.5f,1}));
        wheelView.addWheelLayer(new WheelSuffixLayer("%%",16, Color.BLACK,10));
    }
}

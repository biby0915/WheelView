package com.zby.wheeldemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZhuBingYang
 * @date 2019-06-11.
 */
public class MenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu);


        final List<String> list = new ArrayList<>();
        list.add("main");
        list.add("format");
        list.add("friction");
        list.add("circle");
        list.add("mix");

        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public Object getItem(int i) {
                return list.get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.textview, null);
                TextView tv = view.findViewById(R.id.tv);
                tv.setText(list.get(i));
                return view;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Class clazz = null;
                switch (i) {
                    case 0:
                        clazz = MainActivity.class;
                        break;
                    case 1:
                        clazz = FormatActivity.class;
                        break;
                    case 2:
                        clazz = FrictionActivity.class;
                        break;
                    case 3:
                        clazz = circleActivity.class;
                        break;
                    case 4:
                        clazz = MixActivity.class;
                        break;
                }

                startActivity(new Intent(MenuActivity.this, clazz));
            }
        });
    }
}

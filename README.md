# WheelView
Android wheel view.安卓滚轮组件 (选择器).


# Usage

root build.gradle
```
allprojects {
    repositories {
		...
		maven { url 'https://www.jitpack.io' }
	}
}
```

app gradle
```
dependencies {
  implementation 'com.github.biby0915:WheelView:1.0.2'
}
```

# Snapshot

<img src="https://github.com/biby0915/WheelView/blob/master/preview/circle.gif" width ="300"/>    <img src="https://github.com/biby0915/WheelView/blob/master/preview/format.gif" width ="300"/>
<img src="https://github.com/biby0915/WheelView/blob/master/preview/friction.gif" width ="300"/>    <img src="https://github.com/biby0915/WheelView/blob/master/preview/resize_pin.gif" width ="300"/>
<img src="https://github.com/biby0915/WheelView/blob/master/preview/layout.gif" width ="600"/>

# Set up
```
WheelView<Double> wheelView = findViewById(R.id.wheel);

//1  Set up with a list
ArrayList<Double> dataList = new ArrayList<>();
for (double i = 0d; i < 100d; i+=0.1d) {
    dataList.add(i);
}
wheelView.setData(dataList);

//2  Setting Continuous Numbers and Step Length
//   Real-time data calculation, saving memory space and speeds up loading
wheelView.setDataInRange(0d,100d,0.1d,true);

//3  Setting up custom data sources
wheelView.setDataSource(DataHolder holder);


//add mask
wheelView.addWheelLayer(new WheelMaskLayer(new int[]{0xFFFFFFFF, 0x00FFFFFF, 0xFFFFFFFF}, new float[]{0, .5f, 1}));

//add suffix
wheelView.addWheelLayer(new WheelSuffixLayer("H", 12, Color.parseColor("#FF666666"), 9));

//add custom implementation
wheelView.addWheelLayer(WheelLayer layer);
```

# Add listener
```
wheelView.setOnItemSelectedListener(new WheelView.OnItemSelectedListener<Double>() {
      @Override
      public void onItemSelected(Double data, int position) {

      }

      @Override
      public void onWheelSelecting(Double data, int position) {

      }
});
```

# Attribute List
| attribute | type | description |
| ------ | ------ | ----- |
| selectedTextSize | dimension | 选中中间项字体大小 |
| selectedTextColor | color | 选中项字体颜色 |
| normalTextSize | dimension | 普通项字体大小 |
| normalTextColor | color | 普通项字体颜色 |
| autoAdjustTextSize | dimension | 是否需要自动调整字体大小,避免字体过大时文字重叠,缩小控件尺寸时文本能正常显示 |
| integerFormat | integer | 数据类型为整形时，显示格式(001 - 100，显示更统一) |
| decimalDigitsNumber | integer | 数据类型为浮点型时，小数部分需要保留的位数，自动四舍五入 |
| visibleItemNum | dimension | 一页显示的数据项数目，设置双数的时候会转换成单数 |
| cyclic | boolean | 是否循环滚动 |
| dividerColor | color | 分割线颜色 |
| dividerHeight | dimension | 分割线高度 |
| selectedItemBackgroundColor | color | 选中项背景色 |
| friction | float | 摩擦系数，影响滚轮快速滑动时停止快慢，数值越大，停止越快，划过的项数更少 |
| fixSpringBack | boolean | 快速滑动时是否需要回弹效果，true会修正滚动距离，最后平滑停止 |

# LICENSE

```
Copyright 2019 biby0915

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

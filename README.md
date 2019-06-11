# WheelView
Android wheel view.安卓滚轮组件 (选择器)，快速使用，简单接入自定义效果

# 功能支持

* 支持设置字体大小和颜色
* 设置选中区域背景颜色，上下分割线
* 播放滚动音效
* 添加遮罩和文字后缀，外挂实现
* 支持自动调整文字大小，防止文字重叠
* 可以设置为禁止滚动，view尺寸变化时保持选中项不改变
* 滚动速率设置
* 支持循环滚动
* 泛型实现，自动格式化数字，降低接入成本

# 如何使用

root build.gradle添加以下地址
```
allprojects {
    repositories {
		...
		maven { url 'https://www.jitpack.io' }
	}
}
```

添加依赖
```
dependencies {
  implementation 'com.github.biby0915:WheelView:1.0.0'
}
```

# 示例

支持循环滚动和单列表滚动  

<img src="https://github.com/biby0915/WheelView/blob/master/preview/circle.gif" width ="300"/>

格式化数值类型数据，使界面看起来更统一

<img src="https://github.com/biby0915/WheelView/blob/master/preview/format.gif" width ="300"/>

滚轮的滑动速度可自由设置，数据量大的时候可以增加单次滑动划过的项目数，快速查找想要的数据。

<img src="https://github.com/biby0915/WheelView/blob/master/preview/friction.gif" width ="300"/>

设置自动调整字体字号，防止在控件尺寸变化和进行动画时文本溢出重叠，设置froze时可以保证选中项位置不变，避免位置错乱。

<img src="https://github.com/biby0915/WheelView/blob/master/preview/resize_pin.gif" width ="300"/>

实现WheelLayer并添加到WheelView中自定义绘制内容，下图中颜色遮罩和后缀通过此方式实现  
WheelMaskLayer和WheelSuffixLayer为内建的基本实现，具体特殊需求可自行扩展

```
wheelView.addWheelLayer(new WheelMaskLayer(new int[]{0xff00ff00, 0x00ffffff, 0xff0000ff}, new float[]{0, .5f, 1}));
wheelView.addWheelLayer(new WheelSuffixLayer("😀",16, Color.BLACK,10));
```

<img src="https://github.com/biby0915/WheelView/blob/master/preview/mix.gif" width ="300"/>

WheelView支持泛型，显示自定义数据类型时，可以直接传入数据，无需进行数据组装，只要实现WheelDataSource即可  
滚动监听时，也会返回具体的实体类
```
public class Pojo implements WheelDataSource {
    private String name;
    private String nickname;

    @Override
    public String getDisplayText() {
        return TextUtils.isEmpty(nickname) ? name : nickname;
    }
}
```

```
wheelView.setOnItemSelectedListener(new WheelView.OnItemSelectedListener<Pojo>() {
     @Override
     public void onItemSelected(Pojo data, int position) {
                
     }

     @Override
     public void onWheelSelecting(Pojo data, int position) {

     }
});
```

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

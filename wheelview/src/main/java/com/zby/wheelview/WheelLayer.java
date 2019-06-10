package com.zby.wheelview;

import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * @author ZhuBingYang
 */
public interface WheelLayer {
    /**
     * 在画布上自定义绘制
     * WheelView会在绘制完文字，分割线（可选），选中项背景（可选）后调用
     * 注意绘制区域重叠和绘制顺序，后面绘制的会覆盖内容
     *
     * @param wheelView 滚轮组件
     * @param canvas    画布
     * @param drawArea  画布可绘制区域
     */
    void onDraw(WheelView wheelView, Canvas canvas, Rect drawArea);
}

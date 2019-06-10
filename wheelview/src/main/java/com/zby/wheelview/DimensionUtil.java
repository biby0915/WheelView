package com.zby.wheelview;

import android.content.res.Resources;
import android.util.TypedValue;

/**
 * @author ZhuBingYang
 */
public class DimensionUtil {

    /**
     * dp宽度转像素值
     */
    public static float dip2px(float dpValue) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, Resources.getSystem().getDisplayMetrics());
    }

    /**
     * 文字大小转像素值
     */
    public static float sp2px(float spValue) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, Resources.getSystem().getDisplayMetrics());
    }
}

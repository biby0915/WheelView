package com.zby.wheelview;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * @author ZhuBingYang
 * 在滚动文字后添加后缀文字
 */
public class WheelSuffixLayer implements WheelLayer {
    private String mSuffix;
    private float mTextPadding;
    private float mTextSize;
    private int mTextColor;

    private float mTextContentMaxWidth;

    public WheelSuffixLayer(String suffix, float textSize, int textColor, int textPadding) {
        this.mSuffix = suffix;
        this.mTextPadding = DimensionUtil.dip2px(textPadding);
        this.mTextSize = DimensionUtil.dip2px(textSize);
        this.mTextColor = textColor;
    }

    @Override
    public void onDraw(WheelView wheelView, Canvas canvas, Rect drawArea) {
        Paint paint = wheelView.getPaint();
        if (mTextContentMaxWidth == 0) {
            String s;
            paint.setTextSize(wheelView.getSelectedItemTextSize());
            for (Object o : wheelView.getData()) {
                s = wheelView.getItemDisplayText(o);
                mTextContentMaxWidth = Math.max(mTextContentMaxWidth, paint.measureText(s));
            }
        }

        paint.setTextSize(mTextSize);
        paint.setColor(mTextColor);
        float textBottom = Math.abs(paint.getFontMetrics().ascent + paint.getFontMetrics().descent) / 2 + drawArea.centerY();
        canvas.drawText(mSuffix, drawArea.centerX() + mTextContentMaxWidth / 2 + mTextPadding, textBottom, paint);
    }
}

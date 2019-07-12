package com.zby.wheelview.extention;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.zby.wheelview.DimensionUtil;
import com.zby.wheelview.WheelView;

/**
 * @author ZhuBingYang
 * 在滚动文字后添加后缀文字
 */
public class WheelSuffixLayer implements WheelLayer {
    public String mSuffix;
    public float mTextPadding;
    public float mTextSize;
    public int mTextColor;

    public float mTextContentMaxWidth;

    private WheelView mWheelView;

    public WheelSuffixLayer(String suffix, float textSize, int textColor, int textPadding) {
        this.mSuffix = suffix;
        this.mTextPadding = DimensionUtil.dip2px(textPadding);
        this.mTextSize = DimensionUtil.dip2px(textSize);
        this.mTextColor = textColor;
    }

    public void setSuffix(String suffix) {
        mSuffix = suffix;
        if (mWheelView != null) {
            mWheelView.invalidate();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onDraw(WheelView wheelView, Canvas canvas, Rect drawArea) {
        if (mSuffix == null) {
            return;
        }
        if (mWheelView == null) {
            mWheelView = wheelView;
        }

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
        canvas.drawText(mSuffix, drawArea.centerX() + ((wheelView.getPaddingLeft() - wheelView.getPaddingRight()) >> 1) + mTextContentMaxWidth / 2 + mTextPadding, textBottom, paint);
    }
}

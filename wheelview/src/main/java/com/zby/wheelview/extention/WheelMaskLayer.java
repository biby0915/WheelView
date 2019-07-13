package com.zby.wheelview.extention;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import androidx.annotation.NonNull;

import com.zby.wheelview.WheelView;

/**
 * @author ZhuBingYang
 * 遮罩，上下部分阴影效果
 */
public class WheelMaskLayer implements WheelLayer {
    private Shader mMask;
    private int mMaskHeight;

    private int[] colors;
    private float[] positions;

    public WheelMaskLayer(@NonNull int[] colors, @NonNull float[] positions) {
        this.colors = colors;
        this.positions = positions;
    }

    @Override
    public void onDraw(WheelView wheel, Canvas canvas, Rect drawArea) {
        if (drawArea.height() != mMaskHeight) {
            mMask = new LinearGradient(drawArea.centerX(), drawArea.top, drawArea.centerX(), drawArea.bottom, colors, positions, Shader.TileMode.CLAMP);
            mMaskHeight = drawArea.height();
        }

        Paint paint = wheel.getPaint();
        paint.setShader(mMask);
        canvas.drawRect(drawArea, paint);
        paint.setShader(null);
    }
}

package com.zby.wheelview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * author ZhuBingYang
 * date   2019-09-07
 */
public class WheelView3D extends WheelView {
    private Camera mCamera;
    private Matrix mMatrix;

    private float mCurveRotationFactor;

    public WheelView3D(Context context) {
        this(context, null);
    }

    public WheelView3D(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WheelView3D(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mCamera = new Camera();
        mMatrix = new Matrix();

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.WheelView3D);
        mCurveRotationFactor = ta.getFloat(R.styleable.WheelView3D_curveRotationFactor, 0);
        ta.recycle();
    }

    @Override
    protected void drawItemText(Canvas canvas, String text, int item2CenterOffsetY, int textHeightHalf) {
        canvas.save();

        float radius = (getMeasuredHeight() - getPaddingBottom() - getPaddingTop()) / 2;
        double angle = item2CenterOffsetY / radius;

        mCamera.save();
        mCamera.translate(0, 0, (float) ((1 - Math.cos(angle)) * radius));
        mCamera.rotateX((float) Math.toDegrees(-angle));
        mCamera.getMatrix(mMatrix);
        mCamera.restore();

        float centerX = mDrawRect.centerX() * (1 + mCurveRotationFactor);
        float centerY = (float) (mDrawRect.centerY() + Math.sin(angle) * radius);

        mMatrix.preTranslate(-centerX, -centerY);
        mMatrix.postTranslate(centerX, centerY);

        canvas.concat(mMatrix);
        canvas.clipRect(mDrawRect);
        //ignore text gravity
        canvas.drawText(text, mDrawRect.centerX(), mDrawRect.centerY() + item2CenterOffsetY - textHeightHalf, getPaint());

        canvas.restore();
    }

    public float getCurveRotationFactor() {
        return mCurveRotationFactor;
    }

    public void setCurveRotationFactor(float curveRotationFactor) {
        this.mCurveRotationFactor = curveRotationFactor;
    }
}

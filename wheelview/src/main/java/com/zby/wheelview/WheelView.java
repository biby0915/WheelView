package com.zby.wheelview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import com.zby.wheelview.extention.WheelLayer;
import com.zby.wheelview.source.DataHolder;
import com.zby.wheelview.source.ListDataHolder;
import com.zby.wheelview.source.NumberDataHolder;
import com.zby.wheelview.source.WheelDataSource;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author ZhuBingYang
 */
public class WheelView<T> extends View implements Runnable {

    private static final String TAG = "WheelView";

    private static final float DEFAULT_SELECTED_TEXT_SIZE = DimensionUtil.sp2px(18);
    private static final float DEFAULT_NORMAL_TEXT_SIZE = DimensionUtil.sp2px(14);
    private static final float DEFAULT_DIVIDER_HEIGHT = DimensionUtil.dip2px(1);
    private static final int DEFAULT_NORMAL_TEXT_COLOR = Color.GRAY;
    private static final int DEFAULT_SELECTED_TEXT_COLOR = Color.BLACK;
    private static final int DEFAULT_VISIBLE_ITEM = 5;
    private static final String DEFAULT_INTEGER_FORMAT = "%d";
    private static final int DEFAULT_RESERVED_DECIMAL_DIGITS = 2;
    private static final float DEFAULT_FRICTION = 0.015f;
    public static final int LAYER_POSITION_TOP = 0;
    public static final int LAYER_POSITION_BOTTOM = 1;

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * 每个选项的高度
     */
    private int mItemHeight = 1;

    /**
     * 滚轮可见项数目
     */
    private int mVisibleItemNum;

    /**
     * 是否循环滚动
     */
    private boolean mIsCyclic;

    /**
     * 默认文字颜色
     */
    private int mNormalTextColor;

    /**
     * 自动调整显示文字大小
     * textSize from 1 to mSelectedItemTextSize
     */
    private boolean mAutoAdjustTextSize;

    /**
     * 选中项文字颜色
     */
    private int mSelectedItemTextColor;

    /**
     * 选中项字体大小，显示文字大小
     * 设置了mAutoAdjustTextSize为true 文字大小会变化
     */
    private float mSelectedItemTextSize;

    /**
     * 默认文字大小，显示文字大小
     * 设置了mAutoAdjustTextSize为true 文字大小会变化
     */
    private float mNormalTextSize;

    /**
     * 原始设置的选中文字大小
     */
    private float _selectTextSizeOrigin;

    /**
     * 原始设置的默认文字大小
     */
    private float _normalTextSizeOrigin;

    /**
     * 分割线的颜色
     */
    private int mDividerColor;
    /**
     * 分割线高度
     */
    private float mDividerHeight;

    /**
     * 选中区域颜色
     */
    private int mSelectedRectColor;

    private int mGravity;

    /**
     * 去掉内边距后的控件区域
     * 绘制操作区域
     */
    protected Rect mDrawRect;

    protected Rect mOuterRect;

    /**
     * 数据格式为Integer时显示转换规则
     * 如固定三位数长度，不足补0 可设置为%03d
     * 默认为%d，按原数据显示
     */
    private String mIntegerFormat;

    /**
     * 数据类型为浮点数时，保留小数位数
     */
    private int mDecimalDigitNumber;
    private DecimalFormat mDecimalFormat;

    /**
     * 数据列表
     */
    @NonNull
    private DataHolder<T> mDataHolder = new DataHolder.EmptyHolder<>();

    /**
     * 手势滚动辅助类
     */
    private VelocityTracker mVelocityTracker;
    private Scroller mScroller;

    /**
     * 允许手势滑动的最小速度值
     */
    private int mMinimumVelocity;
    /**
     * 手势滑动的最大速度值
     */
    private int mMaximumVelocity;

    /**
     * 滚轮滑动阻尼系数
     * 越大停止越快
     */
    private float mFriction = DEFAULT_FRICTION;

    /**
     * 是否有快速滚动回弹效果
     * 关闭会修正滚动停止位置到停止item的中心，平滑停止
     */
    private boolean mSpringBackEffect;

    /**
     * 滚动最小值
     */
    private int mMinScrollY;

    /**
     * 滚动最大值
     */
    private int mMaxScrollY;

    /**
     * Y轴滚动偏移值
     */
    private int mScrollOffsetY;

    /**
     * 手指触摸位置的Y坐标
     */
    private float mTouchY;

    /**
     * 手指按下时间，根据按下抬起时间差处理点击滚事件
     */
    private long mTouchDownTime;

    /**
     * 单击判断时长
     */
    private long mClickTimeout;

    /**
     * 是否手指滑动状态
     */
    private boolean mIsDragging = false;

    /**
     * 是否手势滚动
     */
    private boolean mIsFlingScroll = false;

    /**
     * 当前选中项位置
     */
    private int mSelectedItemPosition;

    /**
     * 当前滚动经过的下标
     */
    private int mCurrentScrollPosition;


    /**
     * 选择事件回调
     */
    private OnItemSelectedListener<T> mOnItemSelectedListener;

    /**
     * 滚动音效
     */
    private SoundPlayer mSoundPlayer;

    /**
     * 冻结滚动事件
     * 改变控件尺寸时选中数值不变
     */
    private boolean mFroze = false;

    /**
     * 自定义绘制图层
     */
    private List<WheelLayer> mBottomLayers = new ArrayList<>();
    private List<WheelLayer> mTopWheelLayers = new ArrayList<>();

    @Retention(RetentionPolicy.RUNTIME)
    @IntDef({LAYER_POSITION_TOP, LAYER_POSITION_BOTTOM})
    @interface LayerPosition {

    }

    /**
     * 是否打印日志
     */
    public static boolean mEnableLog = false;

    public WheelView(Context context) {
        this(context, null);
    }

    public WheelView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WheelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainAttrs(context, attrs);
        init(context);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mSoundPlayer != null) {
            mSoundPlayer.release();
        }
    }

    /**
     * 获取并初始化属性
     */
    private void obtainAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.WheelView);
        _selectTextSizeOrigin = mSelectedItemTextSize = ta.getDimension(R.styleable.WheelView_selectedTextSize, DEFAULT_SELECTED_TEXT_SIZE);
        _normalTextSizeOrigin = mNormalTextSize = ta.getDimension(R.styleable.WheelView_normalTextSize, DEFAULT_NORMAL_TEXT_SIZE);
        mAutoAdjustTextSize = ta.getBoolean(R.styleable.WheelView_autoAdjustTextSize, false);
        mNormalTextColor = ta.getColor(R.styleable.WheelView_normalTextColor, DEFAULT_NORMAL_TEXT_COLOR);
        mSelectedItemTextColor = ta.getColor(R.styleable.WheelView_selectedTextColor, DEFAULT_SELECTED_TEXT_COLOR);
        mGravity = ta.getInt(R.styleable.WheelView_gravity, 1);
        mDecimalDigitNumber = ta.getInt(R.styleable.WheelView_decimalDigitsNumber, DEFAULT_RESERVED_DECIMAL_DIGITS);
        mIntegerFormat = ta.getString(R.styleable.WheelView_integerFormat);
        if (TextUtils.isEmpty(mIntegerFormat)) {
            mIntegerFormat = DEFAULT_INTEGER_FORMAT;
        }

        //调整可见item为单数
        mVisibleItemNum = adjustVisibleItemNum(ta.getInt(R.styleable.WheelView_visibleItemNum, DEFAULT_VISIBLE_ITEM));
        mIsCyclic = ta.getBoolean(R.styleable.WheelView_cyclic, false);

        mDividerHeight = ta.getDimension(R.styleable.WheelView_dividerHeight, DEFAULT_DIVIDER_HEIGHT);
        mDividerColor = ta.getColor(R.styleable.WheelView_dividerColor, Color.TRANSPARENT);

        mSelectedRectColor = ta.getColor(R.styleable.WheelView_selectedItemBackgroundColor, Color.TRANSPARENT);
        mFriction = ta.getFloat(R.styleable.WheelView_friction, DEFAULT_FRICTION);
        mSpringBackEffect = !ta.getBoolean(R.styleable.WheelView_fixSpringBack, true);
        ta.recycle();
    }

    /**
     * 初始化
     */
    private void init(Context context) {
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        mMinimumVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        mClickTimeout = ViewConfiguration.getTapTimeout();
        mScroller = new Scroller(context);
        mScroller.setFriction(mFriction);
        mDrawRect = new Rect();
        mOuterRect = new Rect();
        if (!isInEditMode()) {
            mSoundPlayer = new SoundPlayer();
            mDataHolder = new DataHolder.EmptyHolder<>();
        } else {
            mDataHolder = new DataHolder.DebugHolder<>();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getMeasuredWidth(getSuggestedMinimumWidth(), widthMeasureSpec),
                getMeasuredHeight(getSuggestedMinimumHeight(), heightMeasureSpec));
    }

    private int getMeasuredWidth(int size, int measureSpec) {
        int result = size;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                result = size;

                break;
            case MeasureSpec.AT_MOST:
                float textWidth = 0;
                mPaint.setTextSize(mSelectedItemTextSize);
                List<T> list = mDataHolder.toList();
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        textWidth = Math.max(textWidth, mPaint.measureText(getItemDisplayText(list.get(i))));
                    }
                }

                result = (int) Math.max(textWidth + getPaddingLeft() + getPaddingRight(), size);
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }
        return result;
    }

    private int getMeasuredHeight(int size, int measureSpec) {
        int result = size;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                result = size;
                break;
            case MeasureSpec.AT_MOST:
                mPaint.setTextSize(mSelectedItemTextSize);
                float textHeight = mPaint.getFontMetrics().descent - mPaint.getFontMetrics().ascent;

                result = (int) Math.max(textHeight * 2 * mVisibleItemNum + getPaddingTop() + getPaddingBottom(), size);
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }

        mItemHeight = Math.max(MeasureSpec.getSize(result) / mVisibleItemNum, 1);
        return result;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //设置内容可绘制区域
        mDrawRect.set(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
        mOuterRect.set(0, 0, getWidth(), getHeight());
        mItemHeight = Math.max(1, mDrawRect.height() / mVisibleItemNum);
        setBoundary();

        if (mAutoAdjustTextSize) {
            resizeTextSize(mItemHeight);
        }

        mScrollOffsetY = mSelectedItemPosition * mItemHeight;
    }

    /**
     * 重新计算文字大小，避免文字超出单元格重叠
     * 最大到item高度的90%，留出间隙
     *
     * @param itemHeight 单个显示项高度
     */
    private void resizeTextSize(int itemHeight) {
        mSelectedItemTextSize = _selectTextSizeOrigin;
        mNormalTextSize = _normalTextSizeOrigin;

        if (mSelectedItemTextSize > itemHeight * 0.9f) {
            mSelectedItemTextSize = itemHeight * 0.9f;
            mNormalTextSize = mSelectedItemTextSize * _normalTextSizeOrigin / _selectTextSizeOrigin;
        }
    }

    /**
     * 设置滚动上下边界
     */
    private void setBoundary() {
        mMinScrollY = mIsCyclic ? Integer.MIN_VALUE : 0;
        mMaxScrollY = mIsCyclic ? Integer.MAX_VALUE : (mDataHolder.size() - 1) * mItemHeight;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mFroze) {
            Log("frozen.  skip touch events");
            return false;
        }

        initVelocityTracker(event);

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                //拦截触摸事件
                if (getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                //如果未滚动完成，强制滚动完成
                if (!mScroller.isFinished()) {
                    mScroller.forceFinished(true);
                }

                mIsDragging = true;
                mTouchY = event.getY();
                mTouchDownTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_MOVE:
                float currentY = event.getY();
                float dy = currentY - mTouchY;

                //不是y轴移动忽略
                if (Math.abs(dy) < 1) {
                    break;
                }

                scroll((int) -dy);
                mTouchY = currentY;

                break;
            case MotionEvent.ACTION_UP:
                mIsDragging = false;
                mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                float velocityY = mVelocityTracker.getYVelocity();

                //滑动手势
                if (Math.abs(velocityY) > mMinimumVelocity) {
                    //快速滑动
                    mScroller.forceFinished(true);
                    mIsFlingScroll = true;
                    mScroller.fling(0, mScrollOffsetY, 0, (int) -velocityY, 0, 0,
                            mMinScrollY, mMaxScrollY);

                    if (!mSpringBackEffect) {
                        fixBounceEffect();
                    }

                } else {
                    //点击位置和中心点的y轴距离
                    int clickOffset = 0;
                    if (System.currentTimeMillis() - mTouchDownTime <= mClickTimeout) {
                        clickOffset = (int) (event.getY() - mDrawRect.centerY());
                    }
                    int scrollDistance = clickOffset + calculateDistanceNeedToScroll((mScrollOffsetY + clickOffset) % mItemHeight);

                    if (!mIsCyclic) {
                        if (scrollDistance <= 0) {
                            scrollDistance = Math.max(scrollDistance, -mScrollOffsetY);
                        } else {
                            scrollDistance = Math.min(scrollDistance, mMaxScrollY - mScrollOffsetY);
                        }
                    }

                    mScroller.startScroll(0, mScrollOffsetY, 0, scrollDistance);
                }

                invalidateAndCheckItemChange();
                ViewCompat.postOnAnimation(this, this);

                recycleVelocityTracker();
                break;
            case MotionEvent.ACTION_CANCEL:
                recycleVelocityTracker();
                break;
        }
        return true;
    }

    /**
     * 修正快速滚动回弹
     */
    private void fixBounceEffect() {
        //修正快速滑动最后停止位置，没有回弹效果
        int stopOffset = mScroller.getFinalY();
        int itemScrollOffset = Math.abs(stopOffset % mItemHeight);

        //如果滚动偏移超过半个item高度，停止位置加一item高度
        if (itemScrollOffset > mItemHeight >> 1) {
            if (stopOffset < 0) {
                stopOffset = stopOffset / mItemHeight * mItemHeight - mItemHeight;
            } else {
                stopOffset = stopOffset / mItemHeight * mItemHeight + mItemHeight;
            }
        } else {
            stopOffset = stopOffset / mItemHeight * mItemHeight;

        }
        mScroller.setFinalY(stopOffset);
    }

    /**
     * 初始化 VelocityTracker
     *
     * @param event 触摸事件
     */
    private void initVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    /**
     * 回收 VelocityTracker
     */
    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    /**
     * 滚动一段距离
     */
    private void scroll(int distance) {
        mScrollOffsetY += distance;
        if (!mIsCyclic) {
            mScrollOffsetY = Math.min(mMaxScrollY, Math.max(mMinScrollY, mScrollOffsetY));
        }
        invalidateAndCheckItemChange();
    }

    /**
     * 重绘并观察中间项是否有变化
     */
    private void invalidateAndCheckItemChange() {
        invalidate();
        checkIfSelectItemChange();
    }

    /**
     * 检查中间项是否变化
     */
    private void checkIfSelectItemChange() {
        int currentPosition = getCurrentPosition();
        //item改变回调
        if (mCurrentScrollPosition != currentPosition) {
            if (mOnItemSelectedListener != null) {
                mOnItemSelectedListener.onWheelSelecting(mDataHolder.get(currentPosition), currentPosition);
            }
            //播放音效
            playSoundEffect();
            //更新选中项下标
            mCurrentScrollPosition = currentPosition;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (WheelLayer mBottomLayer : mBottomLayers) {
            mBottomLayer.onDraw(this, canvas, mDrawRect);
        }

        //绘制选中区域背景色
        drawSelectedItemBackground(canvas);
        //绘制分割线
        drawDivider(canvas);

        //已滚动item数
        int scrolledItem;
        //没有滚动完的item偏移值
        int scrolledOffset;

        //禁止滚动时不计算新位置
        if (mFroze) {
            scrolledItem = mSelectedItemPosition;
            scrolledOffset = 0;
        } else {
            scrolledItem = mScrollOffsetY / mItemHeight;
            scrolledOffset = mScrollOffsetY % mItemHeight;
        }

        int half = (mVisibleItemNum + 1) / 2;
        //绘制的第一个选项下标
        int firstItemIndex = scrolledItem - half + (mScrollOffsetY > 0 ? 1 : 0);
        //绘制的最后一个选项下标
        int lastItemIndex = scrolledItem + half;

        for (int i = firstItemIndex; i <= lastItemIndex; i++) {
            drawItem(canvas, i, scrolledOffset);
        }

        for (WheelLayer layer : mTopWheelLayers) {
            layer.onDraw(this, canvas, mOuterRect);
        }
    }

    /**
     * 绘制选中区域背景色
     *
     * @param canvas 画布
     */
    private void drawSelectedItemBackground(Canvas canvas) {
        //没有设置选中值背景颜色时跳过绘制
        if (mSelectedRectColor != Color.TRANSPARENT) {
            mPaint.setColor(mSelectedRectColor);
            canvas.drawRect(mDrawRect.left, mDrawRect.centerY() - (mItemHeight >> 1), mDrawRect.right, mDrawRect.centerY() + (mItemHeight >> 1), mPaint);
        }
    }

    /**
     * 绘制分割线
     *
     * @param canvas 画布
     */
    private void drawDivider(Canvas canvas) {
        //没有设置分割线颜色时跳过绘制
        if (mDividerColor == Color.TRANSPARENT) {
            return;
        }

        mPaint.setColor(mDividerColor);
        mPaint.setStrokeWidth(mDividerHeight);
        canvas.drawLine(mDrawRect.left, mDrawRect.centerY() - (mItemHeight >> 1), mDrawRect.right, mDrawRect.centerY() - (mItemHeight >> 1), mPaint);
        canvas.drawLine(mDrawRect.left, mDrawRect.centerY() + (mItemHeight >> 1), mDrawRect.right, mDrawRect.centerY() + (mItemHeight >> 1), mPaint);
    }

    /**
     * 绘制滚轮选项
     *
     * @param canvas         画布
     * @param index          下标
     * @param scrolledOffset 滚动偏移
     */
    protected void drawItem(Canvas canvas, int index, int scrolledOffset) {
        String text = getTextByIndex(index);
        if (text == null) {
            return;
        }

        //item 与中间项的偏移
        int item2CenterOffsetY;
        if (mFroze) {
            item2CenterOffsetY = (index - mSelectedItemPosition) * mItemHeight;
        } else {
            item2CenterOffsetY = (index - mScrollOffsetY / mItemHeight) * mItemHeight - scrolledOffset;
        }

        //文本一半高度数值
        int textHeightHalf;

        //绘制中间项
        if (Math.abs(item2CenterOffsetY) < mItemHeight) {
            float textSize = mNormalTextSize + (mItemHeight - Math.abs(item2CenterOffsetY)) / (mItemHeight * 1.0f) * (mSelectedItemTextSize - mNormalTextSize);
            int textColor = evaluate((mItemHeight - Math.abs(item2CenterOffsetY)) / (mItemHeight * 1.0f), mNormalTextColor, mSelectedItemTextColor);

            mPaint.setTextSize(textSize);
            mPaint.setColor(textColor);

            textHeightHalf = (int) ((mPaint.getFontMetrics().descent + mPaint.getFontMetrics().ascent) / 2);

        }
        //绘制其他项
        else {
            mPaint.setTextSize(mNormalTextSize);
            mPaint.setColor(mNormalTextColor);

            textHeightHalf = (int) ((mPaint.getFontMetrics().descent + mPaint.getFontMetrics().ascent) / 2);
        }

        mPaint.setTextAlign(Paint.Align.CENTER);

        drawItemText(canvas, text, item2CenterOffsetY, textHeightHalf);
    }

    protected void drawItemText(Canvas canvas, String text, int item2CenterOffsetY, int textHeightHalf) {
        int left = 0;
        if (mGravity == 0) {
            left = mDrawRect.left;
            mPaint.setTextAlign(Paint.Align.LEFT);
        } else if (mGravity == 1) {
            left = mDrawRect.centerX();
            mPaint.setTextAlign(Paint.Align.CENTER);
        } else if (mGravity == 2) {
            left = mDrawRect.right;
            mPaint.setTextAlign(Paint.Align.RIGHT);
        }

        canvas.save();
        canvas.clipRect(mDrawRect);
        canvas.drawText(text, left, mDrawRect.centerY() + item2CenterOffsetY - textHeightHalf, mPaint);
        canvas.restore();
    }

    /**
     * 截取渐变颜色某一阶段的色值
     *
     * @param fraction   取值点
     * @param startValue 起始颜色色值
     * @param endValue   结束颜色色值
     * @return 颜色int
     */
    public int evaluate(float fraction, int startValue, int endValue) {
        float startA = ((startValue >> 24) & 0xff) / 255.0f;
        float startR = ((startValue >> 16) & 0xff) / 255.0f;
        float startG = ((startValue >> 8) & 0xff) / 255.0f;
        float startB = (startValue & 0xff) / 255.0f;

        float endA = ((endValue >> 24) & 0xff) / 255.0f;
        float endR = ((endValue >> 16) & 0xff) / 255.0f;
        float endG = ((endValue >> 8) & 0xff) / 255.0f;
        float endB = (endValue & 0xff) / 255.0f;

        // convert from sRGB to linear
        startR = (float) Math.pow(startR, 2.2);
        startG = (float) Math.pow(startG, 2.2);
        startB = (float) Math.pow(startB, 2.2);

        endR = (float) Math.pow(endR, 2.2);
        endG = (float) Math.pow(endG, 2.2);
        endB = (float) Math.pow(endB, 2.2);

        // compute the interpolated color in linear space
        float a = startA + fraction * (endA - startA);
        float r = startR + fraction * (endR - startR);
        float g = startG + fraction * (endG - startG);
        float b = startB + fraction * (endB - startB);

        // convert back to sRGB in the [0..255] range
        a = a * 255.0f;
        r = (float) Math.pow(r, 1.0 / 2.2) * 255.0f;
        g = (float) Math.pow(g, 1.0 / 2.2) * 255.0f;
        b = (float) Math.pow(b, 1.0 / 2.2) * 255.0f;

        return Math.round(a) << 24 | Math.round(r) << 16 | Math.round(g) << 8 | Math.round(b);
    }

    /**
     * 根据下标获取显示文字
     */
    private String getTextByIndex(int index) {
        if (mDataHolder.isEmpty()) {
            Log("data is empty");
            return null;
        }

        if (!mIsCyclic && (index < 0 || index >= mDataHolder.size())) {
            return null;
        }

        int i = index % mDataHolder.size();
        if (i < 0) {
            i += mDataHolder.size();
        }
        return getItemDisplayText(mDataHolder.get(i));
    }

    /**
     * 获取滚轮项显示文字
     */
    public String getItemDisplayText(T item) {
        if (item == null) {
            return "";
        } else if (item instanceof WheelDataSource) {
            return ((WheelDataSource) item).getDisplayText();
        } else if (item instanceof Integer) {
            return !TextUtils.isEmpty(mIntegerFormat) ? String.format(Locale.getDefault(), mIntegerFormat, item) : String.valueOf(item);
        } else if (item instanceof Float || item instanceof Double) {
            if (mDecimalFormat == null) {
                initDecimalFormat();
            }

            return mDecimalFormat.format(item);
        }

        return item.toString();
    }

    /**
     * 初始化浮点数格式化规则
     */
    private void initDecimalFormat() {
        StringBuilder pattern = new StringBuilder("0.");
        for (int i = 0; i < mDecimalDigitNumber; i++) {
            pattern.append("0");
        }
        mDecimalFormat = new DecimalFormat(pattern.toString());
        mDecimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        mDecimalFormat.setGroupingSize(3);
        mDecimalFormat.setGroupingUsed(true);
    }


    /**
     * 播放滚动音效
     */
    public void playSoundEffect() {
        if (mSoundPlayer != null) {
            mSoundPlayer.play();
        }
    }

    /**
     * 直接滚动到最终位置
     */
    public void finishScroll() {
        if (!mScroller.isFinished()) {
            mScroller.abortAnimation();

            mSelectedItemPosition = (int) (mScroller.getFinalY() / mItemHeight + (mScrollOffsetY > 0 ? 0.5f : -0.5f));
            updateAfterStop();
        }
    }

    /**
     * 停止快速滚动
     */
    public void stopScroll() {
        if (!mScroller.isFinished()) {
            mScroller.forceFinished(true);

            mSelectedItemPosition = (int) (mScrollOffsetY / mItemHeight + (mScrollOffsetY > 0 ? 0.5f : -0.5f));
            updateAfterStop();
        }
    }

    private void updateAfterStop() {
        if (mSelectedItemPosition < 0) {
            mSelectedItemPosition = mSelectedItemPosition % mDataHolder.size() + mDataHolder.size();
        }

        if (mSelectedItemPosition >= mDataHolder.size()) {
            mSelectedItemPosition = mSelectedItemPosition % mDataHolder.size();
        }

        Log("stop position:" + mSelectedItemPosition);
        mScrollOffsetY = mItemHeight * mSelectedItemPosition;

        mIsFlingScroll = false;
        invalidateAndCheckItemChange();
    }

    /**
     * 计算滚动到中心点所需的距离
     */
    private int calculateDistanceNeedToScroll(int offset) {
        //超过item高度一半，需要滚动一个item
        if (Math.abs(offset) > mItemHeight / 2) {
            if (mScrollOffsetY < 0) {
                return -mItemHeight - offset;
            } else {
                return mItemHeight - offset;
            }
        }
        //当前item回到中心距离
        else {
            return -offset;
        }
    }

    @Override
    public void run() {
        //停止滚动后更新状态
        if (mScroller.isFinished() && !mIsDragging && !mIsFlingScroll) {
            int currentItemPosition = getCurrentPosition();
            if (currentItemPosition == mSelectedItemPosition) {
                return;
            }

            mCurrentScrollPosition = mSelectedItemPosition = currentItemPosition;

            //选中监听回调
            if (mOnItemSelectedListener != null) {
                mOnItemSelectedListener.onItemSelected(mDataHolder.get(mSelectedItemPosition), mSelectedItemPosition);
            }
        }

        if (mScroller.computeScrollOffset()) {
            mScrollOffsetY = mScroller.getCurrY();
            invalidateAndCheckItemChange();
            ViewCompat.postOnAnimation(this, this);
        } else if (mIsFlingScroll) {
            //快速滚动后调整选中位置到中心
            mIsFlingScroll = false;
            mScroller.startScroll(0, mScrollOffsetY, 0, calculateDistanceNeedToScroll(mScrollOffsetY % mItemHeight));
            invalidateAndCheckItemChange();
            ViewCompat.postOnAnimation(this, this);
        }
    }

    /**
     * 获取当前中间项的下标
     */
    private int getCurrentPosition() {

        //onMeasure未完成
        if (mItemHeight == 0) {
            return 0;
        }

        if (mDataHolder.isEmpty()) {
            return 0;
        }

        int itemPosition;
        if (mScrollOffsetY < 0) {
            itemPosition = (mScrollOffsetY - mItemHeight / 2) / mItemHeight;
        } else {
            itemPosition = (mScrollOffsetY + mItemHeight / 2) / mItemHeight;
        }
        int currentPosition = itemPosition % mDataHolder.size();
        if (currentPosition < 0) {
            currentPosition += mDataHolder.size();
        }

        return currentPosition;
    }

    /**
     * 设置滚动音效
     */
    public void setSoundEffectResource(@RawRes int rawResId) {
        if (mSoundPlayer != null) {
            mSoundPlayer.load(getContext(), rawResId);
        }
    }

    /**
     * 获取当前选中的item数据
     *
     * @return 当前选中的item数据
     */
    public T getSelectedItemData() {
        return mDataHolder.get(mSelectedItemPosition);
    }

    /**
     * 获取数据列表
     *
     * @return 数据列表
     */
    public List<T> getData() {
        return mDataHolder.toList();
    }

    public Paint getPaint() {
        return mPaint;
    }

    /**
     * 设置数据
     *
     * @param dataList 数据列表
     */
    public void setData(List<T> dataList) {
        if (dataList == null) {
            return;
        }
        setDataSource(new ListDataHolder<>(dataList));

    }

    /**
     * 设置滚轮数据，数值类型数据，每项内容根据步长和位置计算得出
     *
     * @param min         最小值
     * @param max         最大值
     * @param step        步长
     * @param includeLast 不能整除时，是否包含最大值
     */
    @SuppressWarnings("unchecked")
    public void setDataInRange(Number min, Number max, Number step, boolean includeLast) {
        setDataSource((DataHolder<T>) new NumberDataHolder<>(min, max, step, includeLast));
    }

    public void setDataSource(DataHolder<T> dataSource) {
        mScroller.forceFinished(true);
        mDataHolder = dataSource;

        reset();
    }

    private void reset() {
        setBoundary();
        mCurrentScrollPosition = mSelectedItemPosition = mScrollOffsetY = 0;

        invalidateAndCheckItemChange();
    }

    public float getSelectedItemTextSize() {
        return mSelectedItemTextSize;
    }

    /**
     * 设置选中字体大小
     *
     * @param textSize 字体大小
     */
    public void setSelectedTextSize(float textSize) {
        float tempTextSize = DimensionUtil.sp2px(textSize);
        if (tempTextSize == mSelectedItemTextSize) {
            return;
        }
        mSelectedItemTextSize = tempTextSize;
        requestLayout();
        invalidate();
    }

    /**
     * 获取是否自动调整字体大小
     */
    public boolean isAutoAdjustTextSize() {
        return mAutoAdjustTextSize;
    }

    /**
     * 设置是否自动调整字体大小
     */
    public void setAutoAdjustTextSize(boolean isAutoFitTextSize) {
        this.mAutoAdjustTextSize = isAutoFitTextSize;
        requestLayout();
        invalidate();
    }

    /**
     * 设置未选中条目颜色
     */
    public void setNormalItemTextColor(@ColorInt int textColor) {
        if (mNormalTextColor == textColor) {
            return;
        }
        mNormalTextColor = textColor;
        invalidate();
    }

    public float getNormalTextSize() {
        return mNormalTextSize;
    }

    /**
     * 设置未选中字体大小
     */
    public void setNormalTextSize(float mNormalTextSize) {
        this.mNormalTextSize = mNormalTextSize;
        invalidate();
    }

    public int getDecimalDigitNumber() {
        return mDecimalDigitNumber;
    }

    /**
     * 设置浮点数保留小数点位数
     */
    public void setDecimalDigitNumber(int mDecimalDigitNumber) {
        this.mDecimalDigitNumber = mDecimalDigitNumber;
        invalidate();
    }

    public boolean isSpringBackEffect() {
        return mSpringBackEffect;
    }

    /**
     * 设置快速滚动是否有回弹效果
     */
    public void setSpringBackEffect(boolean mSpringBackEffect) {
        this.mSpringBackEffect = mSpringBackEffect;
    }

    /**
     * 设置选中条目颜色
     *
     * @param selectedItemTextColor 选中条目颜色
     */
    public void setSelectedItemTextColor(@ColorInt int selectedItemTextColor) {
        if (mSelectedItemTextColor == selectedItemTextColor) {
            return;
        }
        mSelectedItemTextColor = selectedItemTextColor;
        invalidate();
    }

    /**
     * 获取Integer类型转换格式
     */
    public String getIntegerFormat() {
        return mIntegerFormat;
    }

    /**
     * 设置Integer类型转换格式
     */
    public void setIntegerFormat(String integerFormat) {
        if (TextUtils.isEmpty(integerFormat) || integerFormat.equals(mIntegerFormat)) {
            return;
        }
        mIntegerFormat = integerFormat;
        invalidate();
    }

    public float getFriction() {
        return mFriction;
    }

    /**
     * 设置摩擦系数
     */
    public void setFriction(float mFriction) {
        this.mFriction = mFriction;
        mScroller.setFriction(mFriction);
    }

    /**
     * 获取可见项目数
     */
    public int getVisibleItemNum() {
        return mVisibleItemNum;
    }

    /**
     * 设置可见的条目数
     */
    public void setVisibleItemNum(int visibleItemNum) {
        if (mVisibleItemNum == visibleItemNum) {
            return;
        }
        mVisibleItemNum = adjustVisibleItemNum(visibleItemNum);
        mScrollOffsetY = 0;
        invalidate();
    }

    /**
     * 调整可见项目数为奇数
     * 奇数不变，偶数值加一
     *
     * @return 调整后的可见条目数
     */
    private int adjustVisibleItemNum(int visibleItemNum) {
        return visibleItemNum / 2 * 2 + 1;
    }

    /**
     * 是否是循环滚动
     */
    public boolean isCyclic() {
        return mIsCyclic;
    }

    /**
     * 设置是否循环滚动
     */
    public void setCyclic(boolean isCyclic) {
        if (this.mIsCyclic == isCyclic) {
            return;
        }
        this.mIsCyclic = isCyclic;

        setBoundary();
        invalidate();
    }

    /**
     * 获取当前选中下标
     *
     * @return 当前选中的下标
     */
    public int getSelectedItemPosition() {
        return mSelectedItemPosition;
    }

    public void setDefaultSelectItem(T item) {
        setDefault(mDataHolder.indexOf(item));
    }

    /**
     * 设置当前选中项
     *
     * @param position 选中项下标
     */
    public void setDefault(int position) {
        if (position < 0) {
            position = 0;
            Log("index not in range:" + position);
        } else if (position > mDataHolder.size() - 1) {
            position = mDataHolder.size() - 1;
            Log("index not in range:" + position);
        }


        //滚动偏移值
        int scrollOffset = position * mItemHeight - mScrollOffsetY;
        if (scrollOffset == 0) {
            return;
        }
        finishScroll();


        scroll(scrollOffset);
        mSelectedItemPosition = position;
        //选中条目回调
        if (mOnItemSelectedListener != null) {
            mOnItemSelectedListener.onItemSelected(mDataHolder.get(mSelectedItemPosition), mSelectedItemPosition);
        }
    }

    private void Log(String msg) {
        if (mEnableLog) {
            Log.d(TAG, msg);
        }
    }

    public static boolean isEnableLog() {
        return mEnableLog;
    }

    public static void setEnableLog(boolean enableLog) {
        mEnableLog = enableLog;
    }

    /**
     * 获取分割线颜色
     */
    public int getDividerColor() {
        return mDividerColor;
    }

    /**
     * 设置分割线颜色
     */
    public void setDividerColor(@ColorInt int dividerColor) {
        if (mDividerColor == dividerColor) {
            return;
        }
        mDividerColor = dividerColor;
        invalidate();
    }

    /**
     * 获取分割线高度
     */
    public float getDividerHeight() {
        return mDividerHeight;
    }

    /**
     * 设置分割线高度
     */
    public void setDividerHeight(float dividerHeight) {
        float tempDividerHeight = DimensionUtil.dip2px(dividerHeight);
        if (tempDividerHeight == mDividerHeight) {
            return;
        }
        mDividerHeight = tempDividerHeight;
        invalidate();
    }

    /**
     * 获取选中区域颜色
     */
    public int getSelectedRectColor() {
        return mSelectedRectColor;
    }

    /**
     * 设置选中区域颜色
     */
    public void setSelectedRectColor(@ColorInt int selectedRectColor) {
        if (mSelectedRectColor == selectedRectColor) {
            return;
        }
        mSelectedRectColor = selectedRectColor;
        invalidate();
    }

    public OnItemSelectedListener<T> getOnItemSelectedListener() {
        return mOnItemSelectedListener;
    }

    /**
     * 设置滚动回调
     */
    public void setOnItemSelectedListener(OnItemSelectedListener<T> onItemSelectedListener) {
        mOnItemSelectedListener = onItemSelectedListener;
    }

    /**
     * 获取绘制叠加层
     */
    public List<WheelLayer> getWheelLayers() {
        return mTopWheelLayers;
    }

    /**
     * 添加绘制层
     *
     * @param layer 绘制内容实现
     */
    public void addWheelLayer(WheelLayer layer) {
        addWheelLayer(layer, LAYER_POSITION_TOP);
    }

    public void addWheelLayer(WheelLayer layer, @LayerPosition int position) {
        if (position == LAYER_POSITION_BOTTOM) {
            mBottomLayers.add(layer);
        } else if (position == LAYER_POSITION_TOP) {
            mTopWheelLayers.add(layer);
        }
    }

    /**
     * 是否禁止滚动
     */
    public boolean isFroze() {
        return mFroze;
    }

    /**
     * 设置是否禁止滚动
     */
    public void setFroze(boolean froze) {
        this.mFroze = froze;
    }

    /**
     * 滚动事件监听器
     */
    public interface OnItemSelectedListener<T> {

        /**
         * 条目选中回调
         *
         * @param data     选中的数据
         * @param position 选中的下标
         */
        void onItemSelected(T data, int position);

        /**
         * WheelView 快速滚动经过数据项回调
         */
        void onWheelSelecting(T data, int position);
    }

    /**
     * 音效播放器
     */
    private static class SoundPlayer {

        private SoundPool mSoundPool;
        private int mSoundId;

        public SoundPlayer() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mSoundPool = new SoundPool.Builder().build();
            } else {
                mSoundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 0);
            }
        }

        /**
         * 加载音频资源
         */
        public void load(Context context, @RawRes int resId) {
            if (mSoundPool != null) {
                mSoundId = mSoundPool.load(context, resId, 1);
            }
        }

        /**
         * 播放声音效果
         */
        public void play() {
            if (mSoundPool != null && mSoundId != 0) {
                mSoundPool.play(mSoundId, 1, 1, 1, 0, 1);
            }
        }

        /**
         * 释放SoundPool
         */
        public void release() {
            if (mSoundPool != null) {
                mSoundPool.release();
            }
        }
    }
}

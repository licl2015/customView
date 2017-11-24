package com.licl.custom.customview.trapezoid;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.licl.custom.customview.R;
import com.licl.custom.customview.utils.AndroidUtils;

/**
 * 梯形绘制
 * Created by licl on 2017/11/24.
 */

public class TrapezoidView extends View {

    private Paint mBgPaint;
    private Paint mTextPaint;
    private Context mContext;
    private Path mPath;
    private float traHeight;//梯形高

    private String titleName;
    private Rect mTextBounds;
    private Rect mBounds;

    public TrapezoidView(Context context) {
        this(context, null);
    }

    public TrapezoidView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TrapezoidView);
        traHeight = typedArray.getDimension(R.styleable.TrapezoidView_trapHeight, AndroidUtils.dip2px(mContext, 20));
        typedArray.recycle();
        initPaint();
    }

    private void initPaint() {
        mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBgPaint.setColor(Color.RED);
        mBgPaint.setAntiAlias(true);
        mBgPaint.setStyle(Paint.Style.FILL);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.STROKE);
        mTextPaint.setTextSize(AndroidUtils.dip2px(mContext, 14));
        mTextPaint.setTypeface(Typeface.DEFAULT);//字体样式Typeface.BOLD

        mPath = new Path();
        mTextBounds = new Rect();
        mBounds = new Rect();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec((int) AndroidUtils.dip2px(mContext, 100), MeasureSpec.EXACTLY);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) AndroidUtils.dip2px(mContext, 100), MeasureSpec.EXACTLY);
        } else if (widthMode == MeasureSpec.AT_MOST) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec((int) AndroidUtils.dip2px(mContext, 100), MeasureSpec.EXACTLY);
        } else if (heightMode == MeasureSpec.AT_MOST) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) AndroidUtils.dip2px(mContext, 100), MeasureSpec.EXACTLY);
        }
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        mBounds.set(0, 0, width, height);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制背景
        mPath.moveTo(0, mBounds.height());
        mPath.lineTo(traHeight, mBounds.height() - traHeight);
        mPath.lineTo(mBounds.width() - traHeight, mBounds.height() - traHeight);
        mPath.lineTo(mBounds.width(), mBounds.height());
        mPath.lineTo(0, mBounds.height());
        canvas.drawPath(mPath, mBgPaint);

        //绘制文字
        if (!TextUtils.isEmpty(titleName)) {
            mTextPaint.getTextBounds(titleName, 0, titleName.length(), mTextBounds);
            float leftTitleBounds = traHeight + (mBounds.width() - 2 * traHeight - mTextBounds.width()) / 2;
            float topTitleBounds = mBounds.height() - traHeight + (traHeight) * 0.5f + mTextBounds.height() * 0.5f - mTextBounds.bottom;

            canvas.drawText(titleName, leftTitleBounds, topTitleBounds, mTextPaint);
        }
    }
}

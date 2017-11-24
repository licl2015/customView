package com.licl.custom.customview.trapezoid;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
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

public class TrapezoidView2 extends View {

    private Paint mBgPaint;
    private Paint mTextPaint;
    private Context mContext;
    private Path mPath;
    private Path mTextPath;
    private float traHeight;//梯形高

    private String titleName = "测试";
    private Rect mTextBounds;
    private Rect mBounds;
    private RectF mTextPathBounds;

    public TrapezoidView2(Context context) {
        this(context, null);
    }

    public TrapezoidView2(Context context, @Nullable AttributeSet attrs) {
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
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.STROKE);
        mTextPaint.setTextSize(AndroidUtils.dip2px(mContext, 14));
        mTextPaint.setTypeface(Typeface.DEFAULT);//字体样式Typeface.BOLD

        mPath = new Path();
        mTextPath = new Path();
        mTextBounds = new Rect();
        mBounds = new Rect();
        mTextPathBounds = new RectF();
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

    /**
     * 绘制文字
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {

        mPath.moveTo(0, 0);
        mPath.lineTo((int) Math.sqrt(2) * traHeight, 0);
        mPath.lineTo(mBounds.width(), mBounds.height() - (int) Math.sqrt(2) * traHeight);
        mPath.lineTo(mBounds.width(), mBounds.height());
        mPath.lineTo(0, 0);
        canvas.drawPath(mPath, mBgPaint);


        if (!TextUtils.isEmpty(titleName)) {
            mTextPaint.getTextBounds(titleName, 0, titleName.length(), mTextBounds);
            float topTitleBounds = getTopTitleBounds();
            float leftTitleBounds = (int) Math.sqrt(2) * traHeight - (int) Math.sqrt(0.5) * mTextBounds.height();

            drawText(canvas, titleName, leftTitleBounds, topTitleBounds, mTextPaint, 45);

        }
    }

    private float getTopTitleBounds() {
        int basH = (int) (traHeight - mTextBounds.height()) / 2;

        int xBwidth = (int) Math.sqrt(Math.pow(mBounds.width(), 2) + Math.pow(mBounds.height(), 2));
        int basW = (int) (xBwidth - 2 * traHeight - mTextBounds.width()) / 2;

        float topTitleBounds = (int) Math.sqrt(Math.pow(basH, 2) + Math.pow(basW, 2));

        topTitleBounds += Math.sqrt(0.5) * mTextBounds.height();
        return topTitleBounds;
    }

    private void drawText(Canvas canvas, String text, float x, float y, Paint paint, float angle) {
        if (angle != 0) {
            canvas.rotate(angle, x, y);
        }
        canvas.drawText(text, x, y, paint);
        if (angle != 0) {
            canvas.rotate(-angle, x, y);
        }
    }
}

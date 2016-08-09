package com.mersens.numberprogressbar.numberprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

/**
 * Created by Mersens on 2016/8/9.
 */
public class RoundNumberProgressBar extends NumberProgressBar {
    private int radius = dp2px(30);
    private int mMaxPaintWidth;

    public RoundNumberProgressBar(Context context) {
        this(context, null);
    }

    public RoundNumberProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundNumberProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public void init(AttributeSet attrs) {
        mReachHeight=(int)(mUnreachHeight*2.5f);
        final TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.RoundNumberProgressBar);
        radius = (int) ta.getDimension(R.styleable.RoundNumberProgressBar_round_radius, radius);
        ta.recycle();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        mMaxPaintWidth=Math.max(mReachHeight,mUnreachHeight);
        int expect=getPaddingRight() + getPaddingLeft() + radius * 2 + mMaxPaintWidth;
        int weight = resolveSize(expect,widthMeasureSpec);
        int height = resolveSize(expect,heightMeasureSpec);
        int readWidth;
        readWidth=Math.min(weight,height);
        radius=(readWidth-getPaddingLeft()-getPaddingRight()-mMaxPaintWidth)/2;
        setMeasuredDimension(readWidth, readWidth);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        String text=getProgress()+"%";
        float textWeight=mPaint.measureText(text);
        float textHeight=(mPaint.descent()+mPaint.ascent())/2;
        //绘制unreach
        canvas.save();
        canvas.translate(getPaddingLeft()+mMaxPaintWidth/2,getPaddingRight()+mMaxPaintWidth/2);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mUnreachColor);
        mPaint.setStrokeWidth(mUnreachHeight);
        canvas.drawCircle(radius,radius,radius,mPaint);
        //绘制文字
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mTextColor);
        canvas.drawText(text,radius-textWeight/2,radius-textHeight,mPaint);

        //绘制reach
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mReachColor);
        mPaint.setStrokeWidth(mReachHeight);
        float sweepAngle=getProgress()*1.0f/getMax()*360;
        canvas.drawArc(new RectF(0,0,radius*2,radius*2),-90,sweepAngle,false,mPaint);





canvas.restore();
    }
}

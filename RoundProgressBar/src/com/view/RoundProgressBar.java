package com.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.example.demo.R;

/**
 * 
 * @author tangjiabing
 * 
 * @see 开源时间：2016年04月01日
 * 
 *      记得给我个star哦~
 * 
 */
public class RoundProgressBar extends View {

	/** 进度的风格：空心 */
	public static final int STYLE_STROKE = 0;
	/** 进度的风格：实心 */
	public static final int STYLE_FILL = 1;
	/** 画笔对象的引用 */
	private Paint mPaint = null;
	/** 圆环的颜色 */
	private int mRoundColor = 0;
	/** 圆环进度的颜色 */
	private int mRoundProgressColor = 0;
	/** 中间进度百分比的字符串的颜色 */
	private int mTextColor = 0;
	/** 中间进度百分比的字符串的大小 */
	private float mTextSize = 0;
	/** 圆环的宽度 */
	private float mRoundWidth = 0;
	/** 最大进度 */
	private int mMax = 0;
	/** 当前进度 */
	private int mProgress = 0;
	/** 是否显示中间的进度 */
	private boolean mTextIsDisplayable = false;
	/** 进度的风格：实心或者空心 */
	private int mStyle = 0;

	public RoundProgressBar(Context context) {
		this(context, null);
	}

	public RoundProgressBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RoundProgressBar(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mPaint = new Paint();
		TypedArray ta = context.obtainStyledAttributes(attrs,
				R.styleable.RoundProgressBar);
		// 获取自定义属性和默认值
		mRoundColor = ta.getColor(R.styleable.RoundProgressBar_roundColor,
				Color.RED);
		mRoundProgressColor = ta.getColor(
				R.styleable.RoundProgressBar_roundProgressColor, Color.GREEN);
		mTextColor = ta.getColor(R.styleable.RoundProgressBar_textColor,
				Color.GREEN);
		mTextSize = ta.getDimension(R.styleable.RoundProgressBar_textSize, 24);
		mRoundWidth = ta.getDimension(R.styleable.RoundProgressBar_roundWidth,
				6);
		mMax = ta.getInteger(R.styleable.RoundProgressBar_max, 100);
		mTextIsDisplayable = ta.getBoolean(
				R.styleable.RoundProgressBar_textIsDisplayable, true);
		mStyle = ta.getInt(R.styleable.RoundProgressBar_style, STYLE_STROKE);
		ta.recycle();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		/**
		 * 画最外层的大圆环
		 */
		int centre = Math.min(getWidth(), getHeight()) / 2; // 获取圆心的x坐标
		int radius = (int) (centre - mRoundWidth / 2); // 圆环的半径
		mPaint.setColor(mRoundColor); // 设置圆环的颜色
		mPaint.setStyle(Paint.Style.STROKE); // 设置空心
		mPaint.setStrokeWidth(mRoundWidth); // 设置圆环的宽度
		mPaint.setAntiAlias(true); // 消除锯齿
		canvas.drawCircle(centre, centre, radius, mPaint); // 画出圆环

		/**
		 * 画进度百分比
		 */
		mPaint.setStrokeWidth(0);
		mPaint.setColor(mTextColor);
		mPaint.setTextSize(mTextSize);
		mPaint.setFakeBoldText(true); // 设置粗体
		int percent = (int) (mProgress / 1.0 / mMax * 100); // 中间的进度百分比
		float textWidth = mPaint.measureText(percent + "%"); // 测量字体宽度，我们需要根据字体的宽度设置在圆环中间
		if (mTextIsDisplayable && mStyle == STYLE_STROKE)
			canvas.drawText(percent + "%", centre - textWidth / 2, centre
					+ mTextSize / 2, mPaint); // 画出进度百分比

		/**
		 * 画圆弧，画圆环的进度
		 */
		mPaint.setStrokeWidth(mRoundWidth); // 设置圆环的宽度
		mPaint.setColor(mRoundProgressColor); // 设置进度的颜色
		RectF oval = new RectF(centre - radius, centre - radius, centre
				+ radius, centre + radius); // 用于定义的圆弧的形状和大小的界限
		switch (mStyle) {
		case STYLE_STROKE:
			mPaint.setStyle(Paint.Style.STROKE);
			canvas.drawArc(oval, 270, 360 * mProgress / mMax, false, mPaint); // 根据进度画圆弧
			break;
		case STYLE_FILL:
			if (mProgress > 0) {
				mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
				canvas.drawArc(oval, 0, 360 * mProgress / mMax, true, mPaint); // 根据进度画圆弧
			}
			break;
		}

	}

	public synchronized int getMax() {
		return mMax;
	}

	/**
	 * 设置进度的最大值
	 * 
	 * @param max
	 */
	public synchronized void setMax(int max) {
		if (max < 0)
			throw new IllegalArgumentException("max not less than 0");
		mMax = max;
	}

	/**
	 * 获取进度
	 * 
	 * @return
	 */
	public synchronized int getProgress() {
		return mProgress;
	}

	/**
	 * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步，刷新界面调用postInvalidate()能在非UI线程刷新
	 * 
	 * @param progress
	 */
	public synchronized void setProgress(int progress) {
		if (progress < 0)
			throw new IllegalArgumentException("progress not less than 0");
		if (progress > mMax)
			progress = mMax;
		if (progress <= mMax) {
			mProgress = progress;
			postInvalidate();
		}
	}

	public int getRoundColor() {
		return mRoundColor;
	}

	public void setRoundColor(int roundColor) {
		mRoundColor = roundColor;
	}

	public int getRoundProgressColor() {
		return mRoundProgressColor;
	}

	public void setRoundProgressColor(int roundProgressColor) {
		mRoundProgressColor = roundProgressColor;
	}

	public int getTextColor() {
		return mTextColor;
	}

	public void setTextColor(int textColor) {
		mTextColor = textColor;
	}

	public float getTextSize() {
		return mTextSize;
	}

	public void setTextSize(float textSize) {
		mTextSize = textSize;
	}

	public float getRoundWidth() {
		return mRoundWidth;
	}

	public void setRoundWidth(float roundWidth) {
		mRoundWidth = roundWidth;
	}

	public boolean getTextIsDisplayable() {
		return mTextIsDisplayable;
	}

	public void setTextIsDisplayable(boolean textIsDisplayable) {
		mTextIsDisplayable = textIsDisplayable;
	}

	public int getStyle() {
		return mStyle;
	}

	public void setStyle(int style) {
		mStyle = style;
	}

}

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
 * @see ��Դʱ�䣺2016��04��01��
 * 
 *      �ǵø��Ҹ�starŶ~
 * 
 */
public class RoundProgressBar extends View {

	/** ���ȵķ�񣺿��� */
	public static final int STYLE_STROKE = 0;
	/** ���ȵķ��ʵ�� */
	public static final int STYLE_FILL = 1;
	/** ���ʶ�������� */
	private Paint mPaint = null;
	/** Բ������ɫ */
	private int mRoundColor = 0;
	/** Բ�����ȵ���ɫ */
	private int mRoundProgressColor = 0;
	/** �м���Ȱٷֱȵ��ַ�������ɫ */
	private int mTextColor = 0;
	/** �м���Ȱٷֱȵ��ַ����Ĵ�С */
	private float mTextSize = 0;
	/** Բ���Ŀ�� */
	private float mRoundWidth = 0;
	/** ������ */
	private int mMax = 0;
	/** ��ǰ���� */
	private int mProgress = 0;
	/** �Ƿ���ʾ�м�Ľ��� */
	private boolean mTextIsDisplayable = false;
	/** ���ȵķ��ʵ�Ļ��߿��� */
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
		// ��ȡ�Զ������Ժ�Ĭ��ֵ
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
		 * �������Ĵ�Բ��
		 */
		int centre = Math.min(getWidth(), getHeight()) / 2; // ��ȡԲ�ĵ�x����
		int radius = (int) (centre - mRoundWidth / 2); // Բ���İ뾶
		mPaint.setColor(mRoundColor); // ����Բ������ɫ
		mPaint.setStyle(Paint.Style.STROKE); // ���ÿ���
		mPaint.setStrokeWidth(mRoundWidth); // ����Բ���Ŀ��
		mPaint.setAntiAlias(true); // �������
		canvas.drawCircle(centre, centre, radius, mPaint); // ����Բ��

		/**
		 * �����Ȱٷֱ�
		 */
		mPaint.setStrokeWidth(0);
		mPaint.setColor(mTextColor);
		mPaint.setTextSize(mTextSize);
		mPaint.setFakeBoldText(true); // ���ô���
		int percent = (int) (mProgress / 1.0 / mMax * 100); // �м�Ľ��Ȱٷֱ�
		float textWidth = mPaint.measureText(percent + "%"); // ���������ȣ�������Ҫ��������Ŀ��������Բ���м�
		if (mTextIsDisplayable && mStyle == STYLE_STROKE)
			canvas.drawText(percent + "%", centre - textWidth / 2, centre
					+ mTextSize / 2, mPaint); // �������Ȱٷֱ�

		/**
		 * ��Բ������Բ���Ľ���
		 */
		mPaint.setStrokeWidth(mRoundWidth); // ����Բ���Ŀ��
		mPaint.setColor(mRoundProgressColor); // ���ý��ȵ���ɫ
		RectF oval = new RectF(centre - radius, centre - radius, centre
				+ radius, centre + radius); // ���ڶ����Բ������״�ʹ�С�Ľ���
		switch (mStyle) {
		case STYLE_STROKE:
			mPaint.setStyle(Paint.Style.STROKE);
			canvas.drawArc(oval, 270, 360 * mProgress / mMax, false, mPaint); // ���ݽ��Ȼ�Բ��
			break;
		case STYLE_FILL:
			if (mProgress > 0) {
				mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
				canvas.drawArc(oval, 0, 360 * mProgress / mMax, true, mPaint); // ���ݽ��Ȼ�Բ��
			}
			break;
		}

	}

	public synchronized int getMax() {
		return mMax;
	}

	/**
	 * ���ý��ȵ����ֵ
	 * 
	 * @param max
	 */
	public synchronized void setMax(int max) {
		if (max < 0)
			throw new IllegalArgumentException("max not less than 0");
		mMax = max;
	}

	/**
	 * ��ȡ����
	 * 
	 * @return
	 */
	public synchronized int getProgress() {
		return mProgress;
	}

	/**
	 * ���ý��ȣ���Ϊ�̰߳�ȫ�ؼ������ڿ��Ƕ��ߵ����⣬��Ҫͬ����ˢ�½������postInvalidate()���ڷ�UI�߳�ˢ��
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

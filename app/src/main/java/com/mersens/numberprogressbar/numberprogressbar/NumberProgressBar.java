package com.mersens.numberprogressbar.numberprogressbar;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

/**
 * Created by Mersens on 2016-8-3.
 */
public class NumberProgressBar extends ProgressBar {
	// Ĭ������ֵ
	private static final int DEFAULT_TEXT_COLOR = 0XFFFC00D1;
	private static final int DEFAULT_TEXT_SIZE = 10;// SP
	private static final int DEFAULT_UNREACH_COLOR = 0XFFD3D6DA;
	private static final int DEFAULT_UNREACH_HEIGHT = 2;// DP
	private static final int DEFAULT_REACH_COLOR = DEFAULT_TEXT_COLOR;
	private static final int DEFAULT_REACH_HEIGHT = 2;// DP
	private static final int DEFAULT_TEXT_OFFSET = 10;//

	protected int mTextColor = DEFAULT_TEXT_COLOR;
	protected int mTextSize = sp2px(DEFAULT_TEXT_SIZE);
	protected int mUnreachColor = DEFAULT_UNREACH_COLOR;
	protected int mUnreachHeight = dp2px(DEFAULT_UNREACH_HEIGHT);
	protected int mReachColor = DEFAULT_REACH_COLOR;
	protected int mReachHeight = dp2px(DEFAULT_REACH_HEIGHT);
	protected int mOffset = dp2px(DEFAULT_TEXT_OFFSET);
	protected Paint mPaint = new Paint();
	// �ؼ��Ŀ��
	protected int mRealWidth;

	public NumberProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(attrs);
	}

	public NumberProgressBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public NumberProgressBar(Context context) {
		this(context, null);
	}
	/**
	 * ��ʼ���Զ�������
	 * 
	 * @param attrs
	 */
	public void init(AttributeSet attrs) {
		final TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.NumberProgressBar);
		mTextColor = ta.getColor(R.styleable.NumberProgressBar_progress_text_color, mTextColor);
		mTextSize = (int) ta.getDimension(R.styleable.NumberProgressBar_progress_text_size, mTextSize);
		mReachColor = ta.getColor(R.styleable.NumberProgressBar_progress_reache_color, mReachColor);
		mReachHeight = (int) ta.getDimension(R.styleable.NumberProgressBar_progress_reache_height, mReachHeight);
		mUnreachColor = ta.getColor(R.styleable.NumberProgressBar_progress_unreache_color, mUnreachColor);
		mUnreachHeight = (int) ta.getDimension(R.styleable.NumberProgressBar_progress_unreache__height, mUnreachHeight);
		mOffset = (int) ta.getDimension(R.styleable.NumberProgressBar_progress_offset, mOffset);
		ta.recycle();
		mPaint.setTextSize(mTextSize);

	}

	@Override
	protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		//��ȡ���ֵ
		int widthVal = MeasureSpec.getSize(widthMeasureSpec);
		//��ȡ�߶�ֵ
		int heightVal = measureHeight(heightMeasureSpec);
		setMeasuredDimension(widthVal, heightVal);
		//��ȡ�ؼ�����ʵ���
		mRealWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
	}

	private int measureHeight(int heightMeasureSpec) {
		int result = 0;
		//��ȡ�߶ȵ�ģʽ
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		//��ȡ�߶ȵ�ֵ
		int heightVal = MeasureSpec.getSize(heightMeasureSpec);

		// �������ģʽ�Ǿ�ȷֵ��MeasureSpec.EXACTLY��heightValֵΪ�ؼ���ʵ�߶�
		if (heightMode == MeasureSpec.EXACTLY) {
			result = heightVal;
		} else {
			// �����MeasureSpec.UNSPECIFIED��������û�ж��ӿؼ�����κ�Լ����
			//�����������Ҫ��ȡ���壬reachbar�ĸ߶���unreachbar�߶ȵ����ֵ�������ؼ�����ĸ߶�
			int textHeight = (int) (mPaint.descent() - mPaint.ascent());
			result = getPaddingBottom() + getPaddingTop()
					+ Math.max(Math.max(mReachHeight, mUnreachHeight), textHeight);

			// �����MeasureSpec.AT_MOST����ģʽ����Ҫ�����ӿؼ������߶�Ϊ���������heightVal����Сֵ��
			if (heightMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, heightVal);
			}
		}
		return result;
	}

	@Override
	protected synchronized void onDraw(Canvas canvas) {
		canvas.save();
		canvas.translate(getPaddingLeft(), getHeight() / 2);
		// ����Reach
		//��ʶλ��ȷ���Ƿ����unreachbar
		boolean noNeedUnreach = false;
		//radio��ʾ��ǰ������ռ�ܽ��ȵ�һ������
		float radio = getProgress() * 1.0f / getMax();
		//��������
		String text = getProgress() + "%";
		//��ȡ����Ŀ��
		int textWidth = (int) mPaint.measureText(text);
		//���������ܽ���
		float progressX = radio * mRealWidth;
		//����ܽ���+���ֵĿ��>��ʵ���ֵĿ�ȣ���ʾ�Ѿ����ƽ���
		if (progressX + textWidth > mRealWidth) {
			//���ٽ��л��ƣ�progressX��Ҫ�̶�
			progressX = mRealWidth - textWidth;
			//��ʱ��ʾUnreachbar����Ҫ�ٽ��л���
			noNeedUnreach = true;
		}
        //���������ȵĽ���λ��
		float endx = progressX - mOffset / 2;
		//���ƽ���
		if (endx > 0) {
			mPaint.setColor(mReachColor);
			mPaint.setStrokeWidth(mReachHeight);
			canvas.drawLine(0, 0, endx, 0, mPaint);
		}

		// ����Text
		mPaint.setColor(mTextColor);
		int y = (int) (-(mPaint.descent() + mPaint.ascent()) / 2);
		canvas.drawText(text, progressX, y, mPaint);

		// ����Unreach
		if (!noNeedUnreach) {
			float startX = progressX + mOffset / 2 + textWidth;
			mPaint.setColor(mUnreachColor);
			mPaint.setStrokeWidth(mUnreachHeight);
			canvas.drawLine(startX, 0, mRealWidth, 0, mPaint);
		}

		canvas.restore();
	}

	/**
	 * spת��Ϊpx
	 * 
	 * @param spVal
	 * @return
	 */
	public int sp2px(int spVal) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, getResources().getDisplayMetrics());
	}

	/**
	 * dpת��Ϊpx
	 * 
	 * @param dpVal
	 * @return
	 */
	public int dp2px(int dpVal) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, getResources().getDisplayMetrics());

	}

}

package com.alaric.norris.widget.imagebutton ;

import android.content.Context ;
import android.graphics.Matrix ;
import android.graphics.drawable.BitmapDrawable ;
import android.os.Handler ;
import android.util.AttributeSet ;
import android.util.Log ;
import android.view.MotionEvent ;
import android.widget.ImageButton ;

public class Win8ImageButton extends ImageButton {

	private static final int SCALING = 1 ;

	private static final int ZOOM_IN = 0 ;

	private static final int ZOOM_OUT = 6 ;

	private int mWidth ;

	private int mHeight ;

	private int mCenterWidth ;

	private int mCenterHeight ;

	private float mMinScale = 0.81f ;

	private boolean isScaleFinished = true ;

	public Win8ImageButton(Context context) {
		this(context , null) ;
	}

	public Win8ImageButton(Context context , AttributeSet attrs) {
		this(context , attrs , 0) ;
	}

	public Win8ImageButton(Context context , AttributeSet attrs , int defStyle) {
		super(context , attrs , defStyle) ;
		if(null != getTag()) {
			mMinScale = Float.parseFloat((String) getTag()) / 100 ;
		}
	}

	@ Override
	protected void onLayout(boolean changed , int left , int top , int right , int bottom) {
		super.onLayout(changed , left , top , right , bottom) ;
		Log.i("tag" , "" + getTag()) ;
		if(changed) {
			mWidth = getWidth() - getPaddingLeft() - getPaddingRight() ;
			mHeight = getHeight() - getPaddingTop() - getPaddingBottom() ;
			mCenterWidth = mWidth / 2 ;
			mCenterHeight = mHeight / 2 ;
			try {
				((BitmapDrawable) getDrawable()).setAntiAlias(true) ;
			}
			catch(Exception e) {
				e.printStackTrace() ;
			}
		}
	}

	@ Override
	public boolean onTouchEvent(MotionEvent event) {
		switch(event.getAction()) {
			case MotionEvent.ACTION_DOWN :
				mScaleHandler.sendEmptyMessage(ZOOM_IN) ;
				break ;
			case MotionEvent.ACTION_UP :
				mScaleHandler.sendEmptyMessage(ZOOM_OUT) ;
				break ;
		}
		return true ;
	}

	/**
	 * 控制缩放的Handler
	 */
	private Handler mScaleHandler = new Handler() {

		private Matrix matrix = new Matrix() ;

		private int count = 0 ;

		private float scaleDegree ;

		/**
		 * 是否已经调用了点击事件
		 */
		private boolean isClicked ;

		public void handleMessage(android.os.Message msg) {
			matrix.set(getImageMatrix()) ;
			switch(msg.what) {
				case ZOOM_IN :
					if( ! isScaleFinished) {
						mScaleHandler.sendEmptyMessage(ZOOM_IN) ;
					}
					else {
						isScaleFinished = false ;
						count = 0 ;
						scaleDegree = (float) Math.sqrt(mMinScale) ;
						beginScale(matrix , scaleDegree) ;
						mScaleHandler.sendEmptyMessage(SCALING) ;
					}
					break ;
				case SCALING :
					beginScale(matrix , scaleDegree) ;
					if(count < 2) {
						mScaleHandler.sendEmptyMessage(SCALING) ;
					}
					else {
						isScaleFinished = true ;
						if(Win8ImageButton.this.mOnViewClickListener != null && ! isClicked) {
							isClicked = true ;
							Win8ImageButton.this.mOnViewClickListener
									.onViewClick(Win8ImageButton.this) ;
						}
						else {
							isClicked = false ;
						}
					}
					count ++ ;
					break ;
				case 6 :
					if( ! isScaleFinished) {
						mScaleHandler.sendEmptyMessage(ZOOM_OUT) ;
					}
					else {
						isScaleFinished = false ;
						count = 0 ;
						scaleDegree = (float) Math.sqrt(1.0f / mMinScale) ;
						beginScale(matrix , scaleDegree) ;
						mScaleHandler.sendEmptyMessage(SCALING) ;
					}
					break ;
			}
		}
	} ;

	/**
	 * 缩放
	 * 
	 * @param matrix
	 * @param scale
	 */
	private synchronized void beginScale(Matrix matrix , float scale) {
		matrix.postScale(scale , scale , mCenterWidth , mCenterHeight) ;
		setImageMatrix(matrix) ;
	}

	/**
	 * 回调接口
	 */
	private OnViewClickListener mOnViewClickListener ;

	public void setOnClickIntent(OnViewClickListener onViewClickListener) {
		this.mOnViewClickListener = onViewClickListener ;
	}

	public interface OnViewClickListener {

		void onViewClick(Win8ImageButton view) ;
	}
}

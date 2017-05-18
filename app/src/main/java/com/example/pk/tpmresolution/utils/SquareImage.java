package com.example.pk.tpmresolution.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class SquareImage extends ImageView {

	public SquareImage(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public SquareImage(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public SquareImage(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
		int originalWidth = MeasureSpec.getSize(heightMeasureSpec);
		
		super.onMeasure(
                MeasureSpec.makeMeasureSpec(originalWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(originalWidth, MeasureSpec.EXACTLY));
	}

	

}

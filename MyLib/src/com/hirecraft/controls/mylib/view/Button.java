package com.hirecraft.controls.mylib.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.hirecraft.controls.R;
import com.hirecraft.controls.mylib.utils.Utils;

public abstract class Button extends RippleView {

	public Button(Context context, AttributeSet attrs) {
		super(context, attrs);
		onInitAttributes(attrs);
	}
	
	@Override
	protected void onInitDefaultValues() {
		backgroundColor = Color.parseColor("#2196f3");
		///beforeBackground = backgroundColor;// error
	}
	
	protected void onInitAttributes(AttributeSet attrs) {
		setAttributes(attrs);
	}
	
	// ### RIPPLE EFFECT ###
	
	/**
	 * @return 涟漪的bitmap
	 */
	public Bitmap makeCircle() {
		
		Bitmap output = Bitmap.createBitmap(
				getWidth() - Utils.dpToPx(6, getResources()), 
				getHeight() - Utils.dpToPx(7, getResources()), Config.ARGB_8888);
		return makeCircleFromBitmap(output);
	}
	
	// Set color of background
	@Override
	public void setBackgroundColor(int color) {
		backgroundColor = color;
		if (isEnabled()) {
			beforeBackground = backgroundColor;
		}
		try {
			LayerDrawable layer = (LayerDrawable) getBackground();
		
			GradientDrawable shape = (GradientDrawable) layer.findDrawableByLayerId(R.id.shape_bacground);
			
			shape.setColor(backgroundColor);
			
			if (!settedRippleColor) {
				rippleColor = makePressColor(255);
			}
		} catch (Exception ex) {
			// Without bacground
		}
	}

	abstract public TextView getTextView();
	
}

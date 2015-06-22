package com.hirecraft.controls.mylib.view;

import android.content.Context;
import android.util.AttributeSet;



public class ButtonFloatSmall extends ButtonFloat {

	public ButtonFloatSmall(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected void onInitDefaultValues() {
		super.onInitDefaultValues();
		
		sizeRadius = 20;
		rippleSize = 8;
		minWidth = sizeRadius * 2;// 40dp
		minHeight = sizeRadius * 2;// 40dp
	}


}

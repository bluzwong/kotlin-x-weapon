package com.github.bluzwong.swipeback;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;
import com.github.bluzwong.kotlin_x_weapon.R;

/**
 * Created by wangzhijie on 2015/10/9.
 */
public class ShadowView extends View {
    public ShadowView(Context context) {
        super(context);
    }

    private final static int SHADOW_WIDTH = 80;

    private final Drawable leftShadow = getResources().getDrawable(R.drawable.shadow_left_code);
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        canvas.save();
        int right = getWidth();
        int left = right - SHADOW_WIDTH;
        int top = 0;
        int bot = getHeight();
        leftShadow.setBounds(left, top, right, bot);
        canvas.restore();
    }
}

package com.example.textdecorator.textdecorator.textcolordialog;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.example.textdecorator.textdecorator.R;

/**
 * Created by libo on 2017/7/10.
 */

public class SmoothView extends View{
    private int[] colors = {R.color.total_transparent,R.color.total_transparent};
    private ICalldoubleListener listener;
    private Paint paint;
    private int leftLocation;
    private int fullLocation;  //最大长度尺寸

    public SmoothView(Context context) {
        super(context);
    }

    public SmoothView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPaint();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                leftLocation = (int) event.getX();
                if(leftLocation % 2  == 0 && 0 < leftLocation && getMeasuredWidth() > leftLocation){
                    float persent = event.getX()/getMeasuredWidth();
                    listener.callBack(persent);
                    invalidate();
                }
                break;
        }
        return true;
    }

    private void setPaint(){
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(CommonUtil.dip2px(getContext(), 2));
        paint.setAntiAlias(true);
        paint.setColor(getResources().getColor(R.color.gray_deep));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        fullLocation = getMeasuredWidth()-CommonUtil.dip2px(getContext(),6);
        leftLocation = fullLocation;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRoundRect(leftLocation,0,leftLocation+CommonUtil.dip2px(getContext(),6),getMeasuredHeight(),8,8,paint);
    }

    public void setGradiantColor(int color){
        GradientDrawable gradientDrawable = (GradientDrawable) getBackground();
        colors[0] = Color.TRANSPARENT;
        colors[1] = color;
        gradientDrawable.setColors(colors);
    }

    /**
     * 设置滑块位置
     */
    public void setBlocklocationFull(){
        leftLocation = fullLocation;
    }

    public void setListener(ICalldoubleListener listener){
        this.listener = listener;
    }

    public interface ICalldoubleListener {
        void callBack(double num);
    }

}

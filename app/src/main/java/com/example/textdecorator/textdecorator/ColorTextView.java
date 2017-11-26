package com.example.textdecorator.textdecorator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by libo on 2017/11/26.
 */

public class ColorTextView extends AppCompatTextView {

    private TextView borderText = null;///用于描边的TextView
    private TextPaint strokePaint;

    public ColorTextView(Context context) {
        super(context);
        borderText = new TextView(context);
        init();
    }

    public ColorTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        borderText = new TextView(context, attrs);
        init();
    }

    public ColorTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        borderText = new TextView(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        strokePaint = borderText.getPaint();
        strokePaint.setStrokeWidth(2); //设置描边宽度
        strokePaint.setStyle(Paint.Style.STROKE); //对文字只描边
        borderText.setGravity(getGravity());
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        super.setLayoutParams(params);
        borderText.setLayoutParams(params);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        CharSequence tt = borderText.getText();

        //两个TextView上的文字必须一致
        if (tt == null || !tt.equals(this.getText())) {
            borderText.setText(getText());
            this.postInvalidate();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        borderText.measure(widthMeasureSpec, heightMeasureSpec);
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        borderText.layout(left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        borderText.draw(canvas);
        super.onDraw(canvas);
    }

    public void setStrokeColor(int color){
        borderText.setTextColor(color);
        invalidate();
    }

    public void setStrokeWidth(int width){
        strokePaint.setStrokeWidth(width);
        invalidate();
    }

}

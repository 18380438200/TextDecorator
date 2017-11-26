package com.example.textdecorator.textdecorator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import com.example.textdecorator.textdecorator.textcolordialog.TextColorDialog;

public class MainActivity extends AppCompatActivity {
    private RelativeLayout rlContainer;
    private ColorTextView textView;
    private int shadowColor;
    private int shadowSize = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rlContainer = (RelativeLayout) findViewById(R.id.rl_container);
        textView = (ColorTextView) findViewById(R.id.tv);

        TextColorDialog dialog = new TextColorDialog(this, new TextColorDialog.OnClick() {

            @Override
            public void onColorChanged(int newColor) {
                textView.setTextColor(newColor);
            }

            @Override
            public void onChooseStrokeColor(int color) {
                textView.setStrokeColor(color);
            }

            @Override
            public void onChooseStrokeSize(int size) {
                textView.setStrokeWidth(size);
            }

            @Override
            public void onChooseShadowColor(int color) {
                textView.setShadowLayer(1,shadowSize,shadowSize,color);
                shadowColor = color;
            }

            @Override
            public void onChooseShadowSize(int size) {
                textView.setShadowLayer(1,size,size,shadowColor);
                shadowSize = size;
            }

            @Override
            public void onChooseShadowAngle(int angle) {

            }
        });
        rlContainer.addView(dialog);

    }
}

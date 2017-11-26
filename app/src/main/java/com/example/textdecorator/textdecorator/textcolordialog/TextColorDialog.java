
package com.example.textdecorator.textdecorator.textcolordialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.textdecorator.textdecorator.R;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import cn.carbswang.android.numberpickerview.library.NumberPickerView;

public class TextColorDialog extends LinearLayout implements View.OnClickListener {
    private Context mContext;
    private View rootView;
    private List<ColorRecordBean> list = null;
    private LinearLayout llColorPicker,llShadowPicker,llshadowNum;
    private ColorPickerView colorPickerView,colorPickerStroke,colorPickerShadow;
    private RadioGroup rgCheckFunc;
    private SmoothView smoothViewStroke,smoothViewShadow;
    private TextView tvAdjustAngle;
    private int pickNum = 10;
    private NumberPickerView pickerView,pickerviewShadow;
    private int strokeColor,shadowColor;
    private int curPage;
    private int curColorPos = -1;  //当前选中颜色项
    private int curAnglePos;  //当前角度选中位置
    private int[] angles = {45,90,135,180,225,270,315,0};
    private int[] defaultColors = {R.color.Magenta,R.color.Skyblue,R.color.orange,R.color.colorAccent};
    private int blackColor;

    public TextColorDialog(Context context,OnClick onClick) {
        super(context);
        this.mContext = context;
        rootView = inflate(context,R.layout.dialog_edit_textcolor,null);
        addView(rootView);
        this.mOnClick = onClick;
        initDialog(rootView);
    }

    private void initDialog(View v) {
        blackColor = mContext.getResources().getColor(R.color.black);
        RelativeLayout rlRoll = (RelativeLayout) v.findViewById(R.id.rl_roll);
        rgCheckFunc = (RadioGroup) v.findViewById(R.id.rg_textcolor);
        llColorPicker = (LinearLayout) v.findViewById(R.id.ll_strokecolor_picker);
        llShadowPicker = (LinearLayout) v.findViewById(R.id.ll_shadowcolor_picker);
        llshadowNum = (LinearLayout) v.findViewById(R.id.ll_shadow_num);

        View screen = v.findViewById(R.id.view_screen);
        RecyclerView rvColor = (RecyclerView) v.findViewById(R.id.rv_textcolor);

        colorPickerView = (ColorPickerView) v.findViewById(R.id.color_picker_content);
        colorPickerStroke = (ColorPickerView) v.findViewById(R.id.color_picker_stroke);
        colorPickerShadow = (ColorPickerView) v.findViewById(R.id.color_picker_shadow);
        smoothViewStroke = (SmoothView) v.findViewById(R.id.smoothview);
        smoothViewShadow = (SmoothView) v.findViewById(R.id.smoothview_shadow);
        pickerView = (NumberPickerView) v.findViewById(R.id.pickerview_color);
        pickerviewShadow = (NumberPickerView) v.findViewById(R.id.pickerview_color_shadow);
        tvAdjustAngle = (TextView) v.findViewById(R.id.tv_adjust_angle);

        screen.setOnClickListener(this);

        setColorPicker();
        setPickerData();
        setPickerShadowData();
        setRecordColor(rvColor);
        setRgChecked();
        setShadowAngle();
    }

    private void setRgChecked(){
        rgCheckFunc.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton btn;
                for(int i=0;i<rgCheckFunc.getChildCount();i++){
                    btn = (RadioButton) rgCheckFunc.getChildAt(i);
                    if(checkedId == btn.getId()){
                        btn.setBackgroundResource(R.drawable.round_bg_green_small);
                        btn.setTextColor(mContext.getResources().getColor(R.color.white));

                        colorPickerView.setVisibility(View.GONE);
                        llColorPicker.setVisibility(View.GONE);
                        llShadowPicker.setVisibility(View.GONE);
                        if(i == 0){
                            colorPickerView.setVisibility(View.VISIBLE);
                            tvAdjustAngle.setVisibility(View.GONE);
                            curPage = 0;
                        }else if(i == 1){
                            llColorPicker.setVisibility(View.VISIBLE);
                            tvAdjustAngle.setVisibility(View.GONE);
                            curPage = 1;
                        }else if(i == 2){
                            llShadowPicker.setVisibility(View.VISIBLE);
                            curPage = 2;
                        }
                    }else{
                        btn.setBackgroundResource(R.color.white);
                        btn.setTextColor(mContext.getResources().getColor(R.color.common_title_text));
                    }
                }
            }
        });
        ((RadioButton)rgCheckFunc.getChildAt(0)).setChecked(true);
    }

    private void setColorPicker(){
        colorPickerView.setOnColorChangedListener(new ColorPickerView.OnColorChangedListener() {    //文字颜色
            @Override
            public void onColorChanged(int newColor) {
                //此为ARGB,在jni层面改为RGBA
                if (mOnClick != null){
                    mOnClick.onColorChanged(newColor);
                }
            }
        });

        colorPickerStroke.setOnColorChangedListener(new ColorPickerView.OnColorChangedListener() {    //边框颜色
            @Override
            public void onColorChanged(int newColor) {
                smoothViewStroke.setGradiantColor(newColor);
                mOnClick.onChooseStrokeColor(newColor);
                strokeColor = newColor;

                smoothViewStroke.setBlocklocationFull();
            }
        });

        colorPickerShadow.setOnColorChangedListener(new ColorPickerView.OnColorChangedListener() {    //阴影颜色
            @Override
            public void onColorChanged(int newColor) {
                smoothViewShadow.setGradiantColor(newColor);
                mOnClick.onChooseShadowColor(newColor);
                shadowColor = newColor;

                smoothViewShadow.setBlocklocationFull();
            }
        });

        smoothViewStroke.setListener(new SmoothView.ICalldoubleListener() {
            @Override
            public void callBack(double num) {
                int red = Color.red(strokeColor);
                int green = Color.green(strokeColor);
                int blue = Color.blue(strokeColor);
                mOnClick.onChooseStrokeColor(Color.argb((int) (num * 255),red,green,blue));
            }
        });

        smoothViewShadow.setListener(new SmoothView.ICalldoubleListener() {
            @Override
            public void callBack(double num) {
                int red = Color.red(shadowColor);
                int green = Color.green(shadowColor);
                int blue = Color.blue(shadowColor);
                int newColor = Color.argb((int) (num * 255),red,green,blue);
                mOnClick.onChooseShadowColor(newColor);

            }
        });
    }

    private void setPickerData(){
        String[] nums = new String[pickNum];
        for(int i=0;i<pickNum;i++){
            nums[i] = i + "";
        }
        pickerView.setDisplayedValues(nums);
        pickerView.setMinValue(0);
        pickerView.setMaxValue(nums.length - 1);
        pickerView.setValue(2);   //默认边框宽度
        mOnClick.onChooseStrokeSize(2);
        pickerView.setOnValueChangeListenerInScrolling(new NumberPickerView.OnValueChangeListenerInScrolling() {
            @Override
            public void onValueChangeInScrolling(NumberPickerView picker, int oldVal, int newVal) {
                mOnClick.onChooseStrokeSize(oldVal);
            }
        });
    }

    private void setPickerShadowData(){
        String[] nums = new String[pickNum];
        for(int i=0;i<pickNum;i++){
            nums[i] = i + "";
        }
        pickerviewShadow.setDisplayedValues(nums);
        pickerviewShadow.setMinValue(0);
        pickerviewShadow.setMaxValue(nums.length - 1);
        pickerviewShadow.setValue(5);   //默认边框宽度
        mOnClick.onChooseShadowSize(5);
        pickerviewShadow.setOnValueChangeListenerInScrolling(new NumberPickerView.OnValueChangeListenerInScrolling() {
            @Override
            public void onValueChangeInScrolling(NumberPickerView picker, int oldVal, int newVal) {
                mOnClick.onChooseShadowSize(oldVal);
            }
        });
    }

    private void setShadowAngle(){
        tvAdjustAngle.setText(angles[curAnglePos] + "°");
        tvAdjustAngle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(curAnglePos == angles.length-1){
                    curAnglePos = -1;
                }
                curAnglePos++;
                tvAdjustAngle.setText(angles[curAnglePos] + "°");
                mOnClick.onChooseShadowAngle(angles[curAnglePos]);
            }
        });
    }

    private void setRecordColor(RecyclerView rvColor){
            list = new ArrayList<>();

            Collections.reverse(list);

            if(list.size() <= 6){
                for(int i=0;i<defaultColors.length;i++){
                    ColorRecordBean colorRecordBean = new ColorRecordBean();
                    colorRecordBean.setColor(mContext.getResources().getColor(defaultColors[i]));
                    list.add(colorRecordBean);
                }
            }
            if(list.size() >= 10) list = list.subList(0,10);

        final CommonAdapter adapter = new CommonAdapter<ColorRecordBean>(mContext,R.layout.item_colorview,list) {
            @Override
            protected void convert(ViewHolder holder, ColorRecordBean colorRecordBean, int position) {
                GradientDrawable gradientDrawable = (GradientDrawable) holder.getView(R.id.view_color).getBackground();
                gradientDrawable.setColor(colorRecordBean.getColor());

                RelativeLayout rlColor = holder.getView(R.id.rl_color_choose);
                if(curColorPos == position){
                    rlColor.setBackgroundResource(R.drawable.round_stroke_green_shape);
                }else{
                    rlColor.setBackgroundResource(R.color.total_transparent);
                }
            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                int changedColor = list.get(position).getColor();
                if(curPage == 0){
                    mOnClick.onColorChanged(changedColor);
                }else if(curPage == 1){
                    mOnClick.onChooseStrokeColor(changedColor);
                }else if(curPage == 2){
                    mOnClick.onChooseShadowColor(changedColor);

                }

                curColorPos = position;
                adapter.notifyDataSetChanged();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        rvColor.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false));
        rvColor.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if (mOnClick != null) {
            switch (v.getId()) {
                case R.id.view_screen:
                    break;
            }
        }
    }

    private OnClick mOnClick;

    public interface OnClick {

        void onColorChanged(int newColor);

        void onChooseStrokeColor(int color);

        void onChooseStrokeSize(int size);

        void onChooseShadowColor(int color);

        void onChooseShadowSize(int size);

        void onChooseShadowAngle(int angle);
    }
}

package com.example.jack.bottompopupwindowdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2017/6/16.
 */

public class BottomPopupWindowView extends LinearLayout{

    private FrameLayout frameLayout;
    private LinearLayout content_view;
    private RelativeLayout popup_bg;
    private boolean mDrawable=true;
    private View bottomPopouView;
    private View contextView;
    private View baseView;

    public void setBaseView(View baseView){
        this.baseView=baseView;
    }

    public void setContextView(View view){
        this.contextView=view;
    }

    public void setContentView(int id){
        this.contextView=LayoutInflater.from(getContext()).inflate(id,null);
    }

    public BottomPopupWindowView(Context context) {
        this(context,null);
    }

    public BottomPopupWindowView(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public BottomPopupWindowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bottomPopouView= LayoutInflater.from(getContext()).inflate(R.layout.layout_bottom_popup,null);
        frameLayout=(FrameLayout)bottomPopouView.findViewById(R.id.bottom_view);
        content_view=(LinearLayout)bottomPopouView.findViewById(R.id.content_view);
        popup_bg=(RelativeLayout)bottomPopouView.findViewById(R.id.popup_bg);
        addView(bottomPopouView);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mDrawable&&baseView!=null){
            frameLayout.addView(baseView);
            mDrawable=false;
        }
    }

    public void showPopouView(BottomPopupWindowView bottomPopupWindowView){
        if(contextView!=null){
            popup_bg.setVisibility(View.VISIBLE);
            bottomPopupWindowView.setLayoutParams(new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT));
            content_view.addView(contextView,0);
        }
    }

    public void dismssPopupView(BottomPopupWindowView bottomPopupWindowView){
        content_view.removeAllViews();
        popup_bg.setVisibility(View.GONE);
        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,getViewHeight(bottomPopupWindowView));
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,-1);
        bottomPopupWindowView.setLayoutParams(layoutParams);
    }

    public int getViewHeight(View view){
        int width =View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        int height =View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        view.measure(width,height);
        return view.getMeasuredHeight();
    }

    


}

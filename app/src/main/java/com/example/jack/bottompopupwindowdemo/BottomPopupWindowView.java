package com.example.jack.bottompopupwindowdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/6/16.
 */

public class BottomPopupWindowView extends LinearLayout{

    private FrameLayout frameLayout;
    private FrameLayout content_view;
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
        content_view=(FrameLayout)bottomPopouView.findViewById(R.id.content_view);
        popup_bg=(RelativeLayout)bottomPopouView.findViewById(R.id.popup_bg);
        addView(bottomPopouView);
        popup_bg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"text",Toast.LENGTH_SHORT).show();
            }
        });
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
            popup_bg.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.bp_bottom_bg_in));
            bottomPopupWindowView.setLayoutParams(new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT));
            content_view.addView(contextView,0);
            content_view.setVisibility(View.VISIBLE);
            content_view.setAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.bp_bottom_view_in));
        }
    }

    public void dismssPopupView(BottomPopupWindowView bottomPopupWindowView){
        content_view.setVisibility(View.GONE);
        Animation animation=AnimationUtils.loadAnimation(getContext(),R.anim.bp_bottom_view_out);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {}
            @Override
            public void onAnimationRepeat(Animation animation) {
                content_view.removeAllViews();
            }
        });
        content_view.setAnimation(animation);
        popup_bg.setVisibility(View.GONE);
        popup_bg.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.bp_bottom_bg_out));
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

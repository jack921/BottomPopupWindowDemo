package com.example.jack.bottompopupwindowdemo;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2017/6/16.
 */

public class BottomPopupWindowView extends LinearLayout{

    private FrameLayout frameLayout;
    private ContentView content_view;
    private RelativeLayout popup_bg;
    private boolean mDrawable=true;
    private View bottomPopouView;
    private View contextView;
    private View baseView;
    private float y1;
    private float y2;
    private float minVelocity=0;
    private AnimatorListener animatorListener;

    public void setAnimatorListener(AnimatorListener animatorListener) {
        this.animatorListener = animatorListener;
    }

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
        minVelocity=ViewConfiguration.get(getContext()).getScaledTouchSlop();
        bottomPopouView= LayoutInflater.from(getContext()).inflate(R.layout.layout_bottom_popup,null);
        frameLayout=(FrameLayout)bottomPopouView.findViewById(R.id.bottom_view);
        content_view=(ContentView)bottomPopouView.findViewById(R.id.content_view);
        popup_bg=(RelativeLayout)bottomPopouView.findViewById(R.id.popup_bg);
        addView(bottomPopouView);

        popup_bg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismssPopupView();
            }
        });

        content_view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view){}
        });

        frameLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view){}
        });

        content_view.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    y1 = motionEvent.getY();
                }
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    y2 = motionEvent.getY();
                    if((y2-y1)>minVelocity){
                        dismssPopupView();
                    }
                }
                return false;
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

    public void showPopouView(){
        if(contextView!=null){
            startAnimation();
            popup_bg.setVisibility(View.VISIBLE);
            popup_bg.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.bp_bottom_bg_in));
            ((BottomPopupWindowView)this).setLayoutParams(new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT));
            content_view.addView(contextView,0);
            content_view.setVisibility(View.VISIBLE);
            content_view.setAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.bp_bottom_view_in));

        }
    }

    public void dismssPopupView(){
        endAnimation();
        content_view.setVisibility(View.GONE);
        Animation animation=AnimationUtils.loadAnimation(getContext(),R.anim.bp_bottom_view_out);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationRepeat(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                content_view.removeAllViews();
                popup_bg.setVisibility(View.GONE);
                popup_bg.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.bp_bottom_bg_out));
                RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,getViewHeight((BottomPopupWindowView)BottomPopupWindowView.this));
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,-1);
                ((BottomPopupWindowView)BottomPopupWindowView.this).setLayoutParams(layoutParams);
            }
        });
        content_view.setAnimation(animation);
    }

    public int getViewHeight(View view){
        int width =View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        int height =View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        view.measure(width,height);
        return view.getMeasuredHeight();
    }

    public void startAnimation(){
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0,50);
        valueAnimator.setDuration(250);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                if(animatorListener!=null){
                    animatorListener.startValue((int) valueAnimator.getAnimatedValue());
                }
            }
        });
        valueAnimator.start();
    }

    public void endAnimation() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(50, 0);
        valueAnimator.setDuration(250);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                if(animatorListener!=null){
                    animatorListener.endValue((int) valueAnimator.getAnimatedValue());
                }
            }
        });
        valueAnimator.start();
    }

    interface AnimatorListener{
        void startValue(int value);
        void endValue(int value);
    }

}

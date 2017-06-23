package com.jack.bottompopupwindowview;

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
 * Created by Jack on 2017/6/16.
 */

public class BottomPopupWindowView extends LinearLayout{

    private AnimatorListener animatorListener;
    //底部内容的View
    private FrameLayout base_view;
    //内容的View
    private FrameLayout content_view;
    //背景的View
    private RelativeLayout popup_bg;
    //xml加载的View
    private View bottomPopouView;
    //外部加载的内容View
    private View contentView;
    //外部加载的底部内容View
    private View baseView;
    //手势的最小值
    private float minVelocity=0;
    //加载一次的判断值
    private boolean mDrawable=true;

    public void setAnimatorListener(AnimatorListener animatorListener) {
        this.animatorListener = animatorListener;
    }

    public void setBaseView(View baseView){
        this.baseView=baseView;
    }

    public void setContextView(View view){
        this.contentView=view;
    }

    public void setContentView(int id){
        this.contentView=LayoutInflater.from(getContext()).inflate(id,null);
    }

    public BottomPopupWindowView(Context context) {
        this(context,null);
    }

    public BottomPopupWindowView(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public BottomPopupWindowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //初始化各种数值
        minVelocity=ViewConfiguration.get(getContext()).getScaledTouchSlop();
        bottomPopouView= LayoutInflater.from(getContext()).inflate(R.layout.layout_bottom_popup,null);
        base_view=(FrameLayout)bottomPopouView.findViewById(R.id.bottom_view);
        content_view=(FrameLayout)bottomPopouView.findViewById(R.id.content_view);
        popup_bg=(RelativeLayout)bottomPopouView.findViewById(R.id.popup_bg);
        //把整个View都加载在LinearLayout里以显示出来
        addView(bottomPopouView);
        //背景颜色监听
        popup_bg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                disMissPopupView();
            }
        });

        //屏蔽内容区域点击事件
        content_view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view){}
        });

        //屏蔽底部内容区域点击事件
        base_view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view){}
        });

        //内容区域判断是否向下，手势向下就关闭弹框
        content_view.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float y1=0,y2=0;
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    y1 = motionEvent.getY();
                }
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    y2 = motionEvent.getY();
                    if((y2-y1)>minVelocity){
                        disMissPopupView();
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
            //刚开始加载底部内容区域，只需一次就行，多次报错
            base_view.addView(baseView);
            mDrawable=false;
        }
    }

    public void showPopouView(){
        if(contentView!=null){
            //开始动画数据
            startAnimation();
            //开启背景颜色的渐变动画
            popup_bg.setVisibility(View.VISIBLE);
            popup_bg.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.bp_bottom_bg_in));
            //把这个区域全部显示出来
            ((BottomPopupWindowView)this).setLayoutParams(new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT));
            //假如内容区域
            content_view.addView(contentView,0);
            content_view.setVisibility(View.VISIBLE);
            //开启内容区域动画
            content_view.setAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.bp_bottom_view_in));
        }
    }

    public void disMissPopupView(){
        //开始关闭动画数据
        endAnimation();
        //开启内容区域动画
        content_view.setVisibility(View.GONE);
        Animation animation=AnimationUtils.loadAnimation(getContext(),R.anim.bp_bottom_view_out);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationRepeat(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                //等内容区域动画结束后，清楚所有View
                content_view.removeAllViews();
                //开启背景颜色的渐变动画
                popup_bg.setVisibility(View.GONE);
                popup_bg.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.bp_bottom_bg_out));
                //把整个控件的大小恢复到底部View区域的大小
                RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,getViewHeight((BottomPopupWindowView)BottomPopupWindowView.this));
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,-1);
                ((BottomPopupWindowView)BottomPopupWindowView.this).setLayoutParams(layoutParams);
            }
        });
        //开始动画
        content_view.setAnimation(animation);
    }

    //获取View的高度
    public int getViewHeight(View view){
        int width =View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        int height =View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        view.measure(width,height);
        return view.getMeasuredHeight();
    }

    //开始动画数据变化
    public void startAnimation(){
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0,40);
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

    //结束动画数值变化
    public void endAnimation() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(40,0);
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


}

package com.ferris.menu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * Created by ferris on 2016/5/12.
 */


public class BottomMenuLayout extends RelativeLayout implements IMenu {
    private boolean isShowing=false;
    private int mWidth=0;
    private int mHeight=0;
    private final long delayTime=100L;
    private BlurringView mBlurringView;
    public BottomMenuLayout(Context context) {
        super(context);
    }

    public BottomMenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BottomMenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        ViewTreeObserver vto = getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
                mHeight=getHeight();
                mHeight=getWidth();

                setTranslationY(mHeight);
            }
        });
    }


    @Override
    public void show() {
        if(isShowing){
            return;
        }
        AnimatorSet mAnimatorSet=new AnimatorSet();
        //执行容器平移
        ObjectAnimator translationSelf=ObjectAnimator.ofFloat(this, "translationY", mHeight,0).setDuration(300);

        translationSelf.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if(getVisibility()!=View.VISIBLE){
                    setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mAnimatorSet.playSequentially(translationSelf);
        //为子控件单独设置设置对应动画
        long deyTime=0;
        for(int i=0;i<getChildCount();i++){
            getChildAt(i).setAlpha(0f);
            final ObjectAnimator translationChild=ObjectAnimator.ofFloat(getChildAt(i), "translationY", mHeight,0).setDuration(300);
            deyTime+=delayTime;
            translationChild.setStartDelay(deyTime);
            translationChild.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    ((View)translationChild.getTarget()).setAlpha(1f);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    isShowing=true;
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            mAnimatorSet.playSequentially(translationChild);
        }

        mAnimatorSet.start();



        mBlurringView.refreshBlur();
        mBlurringView.animate().alpha(1f).setDuration(200).setListener(new android.animation.Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(android.animation.Animator animation) {
                if(mBlurringView.getVisibility()!=View.VISIBLE)
                    mBlurringView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(android.animation.Animator animation) {

            }

            @Override
            public void onAnimationCancel(android.animation.Animator animation) {

            }

            @Override
            public void onAnimationRepeat(android.animation.Animator animation) {

            }
        }).start();


        tv_bottom_bar.setText("关闭菜单");

    }



    @Override
    public void hide() {
        if(!isShowing){
            return;
        }
        ObjectAnimator translationSelf=ObjectAnimator.ofFloat(this, "translationY", 0,mHeight).setDuration(300);
        translationSelf.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(getVisibility()!=View.INVISIBLE){
                    setVisibility(View.INVISIBLE);
                }
                isShowing=false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        translationSelf.start();


        mBlurringView.animate().alpha(0f).setDuration(200).setListener(new android.animation.Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(android.animation.Animator animation) {

            }

            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                if(mBlurringView.getVisibility()!=View.INVISIBLE)
                    mBlurringView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(android.animation.Animator animation) {

            }

            @Override
            public void onAnimationRepeat(android.animation.Animator animation) {

            }
        }).start();


        tv_bottom_bar.setText("打开菜单");

    }

    @Override
    public boolean isShowing() {
        return isShowing;
    }

    public void setBlurView(BlurringView mBlurringView) {
        this.mBlurringView=mBlurringView;
    }
    TextView tv_bottom_bar;
    public void setBottomBar(TextView tv_bottom_bar) {
        this.tv_bottom_bar=tv_bottom_bar;
    }
}

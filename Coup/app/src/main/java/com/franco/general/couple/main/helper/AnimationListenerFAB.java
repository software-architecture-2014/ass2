package com.franco.general.couple.main.helper;

import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.animation.Animation;

public class AnimationListenerFAB implements Animation.AnimationListener {

    public FloatingActionButton to_show_ = null;
    public FloatingActionButton to_fade_ = null;

    private Animation before_;
    private Animation after_;

    public void startAnimation(FloatingActionButton to_fade)
    {
        to_fade_ = to_fade;
        to_fade.startAnimation(before_);
    }

    public AnimationListenerFAB(Animation befor, Animation after)
    {
        after_ = after;
        before_ = befor;
    }

    @Override
    public void onAnimationStart(Animation animation) {}

    @Override
    public void onAnimationEnd(Animation animation) {

        to_fade_.setVisibility(View.INVISIBLE);
        to_show_.setVisibility(View.VISIBLE);
        to_show_.startAnimation(after_);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {}
}


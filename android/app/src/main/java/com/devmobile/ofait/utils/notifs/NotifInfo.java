package com.devmobile.ofait.utils.notifs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.devmobile.ofait.R;
import com.devmobile.ofait.ui.mainmenu.MainActivity;

/**
 * Created by MicroStop on 24/10/2016.
 */

public class NotifInfo {

    private View itemView;

    public NotifInfo(MainActivity mainActivity, int resItemLayout) {
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        itemView = layoutInflater.inflate(resItemLayout, null);
    }

    public void setSrc(MainActivity mainActivity, int resImageView, int resDrawable) {
        ImageView imageView = (ImageView) itemView.findViewById(resImageView);
        imageView.setImageDrawable(mainActivity.getResources().getDrawable(resDrawable));
    }

    public void startAnimation(final MainActivity mainActivity) {
        final FrameLayout frameLayout = (FrameLayout) mainActivity.findViewById(R.id.frame_content_notif_received);
        frameLayout.addView(itemView);
        Animation animation = AnimationUtils.loadAnimation(mainActivity, R.anim.anim_like_dislike);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                itemView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        itemView.startAnimation(animation);
    }
}

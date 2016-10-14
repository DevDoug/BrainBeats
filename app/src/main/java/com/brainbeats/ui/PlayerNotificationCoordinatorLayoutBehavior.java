package com.brainbeats.ui;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.brainbeats.R;

/**
 * Created by douglas on 9/7/2016.
 * should allow the FAB to translate on the y axis to allow for the notification bar
 */
public class PlayerNotificationCoordinatorLayoutBehavior extends CoordinatorLayout.Behavior<FloatingActionButton> {

    public PlayerNotificationCoordinatorLayoutBehavior(Context context, AttributeSet attrs) {
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        RelativeLayout audioNotificationBar = (RelativeLayout) parent.findViewById(R.id.current_track_container);
        return audioNotificationBar != null && audioNotificationBar.getVisibility() == View.VISIBLE;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        float translationY = Math.min(0, dependency.getTranslationY() - ((dependency.getHeight()/2) + 30));
        child.setTranslationY(translationY);
        return true;
    }
}
package app.simone.shared.utils;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;

import java.util.List;

import app.simone.shared.main.FullscreenBaseGameActivity;
import app.simone.singleplayer.view.GameActivity;
import app.simone.R;
import app.simone.shared.styleable.SimoneTextView;
import app.simone.singleplayer.model.SimonColorImpl;

/**
 * AnimationHandler class.
 * Used to animate the fabs and the textviews.
 * @author Michele Sapignoli
 */

public class AnimationHandler {
    private static Animation gameButtonAnimation;


    public static void initAnimations(final GameActivity context, final FloatingActionButton gameFab, final SimoneTextView simoneTextView) {
        final Animation rotate = AnimationUtils.loadAnimation(context, R.anim.rotate);
        final Animation zoomIn = AnimationUtils.loadAnimation(context, R.anim.zoom_in);
        final Animation zoomOut = AnimationUtils.loadAnimation(context, R.anim.zoom_out);
        gameButtonAnimation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        gameButtonAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                if (context.isPlayerBlinking()) {
                    gameFab.startAnimation(zoomIn);
                    simoneTextView.startAnimation(zoomIn);

                } else {
                    simoneTextView.startAnimation(zoomOut);
                    gameFab.startAnimation(zoomOut);
                }
                simoneTextView.startAnimation(rotate);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    public static Animation getGameButtonAnimation() {
        return gameButtonAnimation;
    }

    public static void performColorSwapAnimation(FullscreenBaseGameActivity context, Integer[] shuffle, List<Button> buttons, FrameLayout[] layouts) {
        for (int i = 0; i < buttons.size(); i++) {
            int index = shuffle[i];
            layouts[i].addView(buttons.get(index));
            ObjectAnimator objectAnimator = ObjectAnimator.ofObject(buttons.get(i), "backgroundColor",
                    new ArgbEvaluator(),
                    ContextCompat.getColor(context, SimonColorImpl.getColorIdFromButtonId(buttons.get(index).getId())),
                    ContextCompat.getColor(context, SimonColorImpl.getColorIdFromButtonId(buttons.get(i).getId())));
            objectAnimator.setRepeatCount(0);
            objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
            objectAnimator.setDuration(300);
            objectAnimator.start();
        }
    }
}

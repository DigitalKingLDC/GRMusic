package soft.me.ldc.base;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ldc45 on 2017/11/14.
 */

public abstract class RootRecyclerViewAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {


    @SuppressLint("NewApi")
    protected void setAnimator(View view) {
        addInAnimation(view);
    }

    /**
     * 将动画对象加入集合中  根据左右滑动加入不同
     */
    private void addInAnimation(View view) {
        List<Animator> list = new ArrayList<>();

        list.add(ObjectAnimator.ofFloat(view, "alpha", 0f, 1f));
        list.add(ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.1f, 1f));
        list.add(ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.1f, 1f));
        startAnimation(list);
    }

    /**
     * 开启动画
     */
    private void startAnimation(List<Animator> list) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(list);
        animatorSet.setDuration(1000);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.start();
    }
}

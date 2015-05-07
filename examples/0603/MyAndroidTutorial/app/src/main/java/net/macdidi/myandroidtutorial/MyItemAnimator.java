package net.macdidi.myandroidtutorial;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MyItemAnimator extends RecyclerView.ItemAnimator {

    // 儲存動畫的畫面包裝元件
    private List<RecyclerView.ViewHolder> viewHolders = new ArrayList<>();

    @Override
    public void runPendingAnimations() {

        if (!viewHolders.isEmpty()) {

            // 動畫物件
            AnimatorSet animator;
            // 執行動畫的元件
            View target;

            for (final RecyclerView.ViewHolder viewHolder : viewHolders) {
                // 取得執行動畫的元件
                target = viewHolder.itemView;
                // 建立動畫物件
                animator = new AnimatorSet();

                // 設定動畫效果
                // 由左側出現與淡入效果
                animator.playTogether(
                        ObjectAnimator.ofFloat(target, "translationX", -target.getMeasuredWidth(), 0.0f),
                        ObjectAnimator.ofFloat(target, "alpha", 0.5F, 1.0F)
                );

                // 設定動畫套用的元件與時間
                animator.setTarget(target);
                animator.setDuration(1000);

                // 動畫結束監聽事件
                animator.addListener(new AnimatorListenerWrapper() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        // 移除完成動畫的元件
                        viewHolders.remove(viewHolder);

                        if (!isRunning()) {
                            dispatchAnimationsFinished();
                        }
                    }
                });

                animator.start();
            }
        }
    }

    @Override
    public boolean animateAdd(RecyclerView.ViewHolder viewHolder) {
        return viewHolders.add(viewHolder);
    }

    @Override
    public boolean animateRemove(RecyclerView.ViewHolder viewHolder) {
        return false;
    }

    @Override
    public boolean animateMove(RecyclerView.ViewHolder viewHolder,
                               int i, int i2, int i3, int i4) {
        return false;
    }

    @Override
    public boolean animateChange(RecyclerView.ViewHolder oldHolder,
                                 RecyclerView.ViewHolder newHolder,
                                 int fromLeft, int fromTop,
                                 int toLeft, int toTop) {
        return false;
    }

    @Override
    public void endAnimation(RecyclerView.ViewHolder viewHolder) {

    }

    @Override
    public void endAnimations() {

    }

    @Override
    public boolean isRunning() {
        return !viewHolders.isEmpty();
    }

    public class AnimatorListenerWrapper implements Animator.AnimatorListener {

        @Override
        public void onAnimationStart(Animator animation) {

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
    }

}

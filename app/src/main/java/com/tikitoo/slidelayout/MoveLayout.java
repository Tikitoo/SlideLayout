package com.tikitoo.slidelayout;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by tikitoo on 7/24/15.
 */
public class MoveLayout extends RelativeLayout implements View.OnClickListener {
    private Context mCtx;
    private int downX;
    private TextView topTv, deleteTv, moreTv;

    public MoveLayout(Context context) {
        this(context, null);
    }

    public MoveLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MoveLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mCtx = context;
        View rootView = LayoutInflater.from(context).inflate(R.layout.move_layout, this, true);
        deleteTv = (TextView) rootView.findViewById(R.id.delete_tv);
        moreTv = (TextView) rootView.findViewById(R.id.more_tv);
        topTv = (TextView) rootView.findViewById(R.id.top_tv);
        topTv.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return handlerTouch(view, motionEvent);
            }
        });

        deleteTv.setOnClickListener(this);
        moreTv.setOnClickListener(this);
        topTv.setOnClickListener(this);

    }

    /* ---- deal touch --- */
    boolean result = false;
    boolean isOpen = false;

    protected boolean handlerTouch(View v, MotionEvent event) {
        int bottomWidth = deleteTv.getWidth() + moreTv.getWidth();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = (int) (event.getRawX() - downX);
                if (isOpen) {
                    // 打开状态
                    // 向右滑动
                    if (dx > 0 && dx < bottomWidth) {
                        v.setTranslationX(dx - bottomWidth);
                        // 允许移动，阻止点击
                        result = true;
                    }
                } else {
                    // 闭合状态
                    // 向左移动
                    if (dx < 0 && Math.abs(dx) < bottomWidth) {
                        v.setTranslationX(dx);
                        // 允许移动，阻止点击
                        result = true;
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                float ddx = v.getTranslationX();
                // 判断是打开还是关闭
                if (ddx <= 0 && ddx > -(bottomWidth / 2)) {
                    // 关闭
                    ObjectAnimator oa1 = ObjectAnimator.ofFloat(v, "translationX", ddx, 0).setDuration(100);
                    oa1.start();
                    oa1.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            isOpen = false;
                            result = false;
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                            isOpen = false;
                            result = false;
                        }
                    });
                }
                if (ddx <= -(bottomWidth / 2) && ddx > -bottomWidth) {
                    // 打开
                    ObjectAnimator oa1 = ObjectAnimator.ofFloat(v, "translationX", ddx, -bottomWidth)
                            .setDuration(100);
                    oa1.start();
                    result = true;
                    isOpen = true;
                }
                break;
        }
        return result;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.delete_tv:
                Toast.makeText(mCtx, "delete", Toast.LENGTH_SHORT).show();
                break;
            case R.id.more_tv:
                Toast.makeText(mCtx, "more", Toast.LENGTH_SHORT).show();
                break;
            case R.id.top_tv:
                Toast.makeText(mCtx, "item", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}

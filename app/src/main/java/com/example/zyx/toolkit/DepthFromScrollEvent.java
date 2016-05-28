package com.example.zyx.toolkit;

import android.view.MotionEvent;
import android.view.View;

/**
 * Created by wangx on 28/05/2016.
 */
public class DepthFromScrollEvent {
    public interface Listener {
        void onDepthChanged(double normalized_depth);
    }

    private View view;

    private Listener mListener;

    public void startListening(Listener listener) {
        if (mListener == listener) {
            return;
        }
        mListener = listener;
    }

    public void stopListening() {
        mListener = null;
    }

    public DepthFromScrollEvent(View v)
    {
        view = v;
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                int pointerCount = event.getPointerCount();
                float x = event.getX(0);
                float y = event.getY(0);
                float w = view.getWidth();
                float h = view.getHeight();
                mListener.onDepthChanged(y / h);
                return true;
            }
        });

    }
}

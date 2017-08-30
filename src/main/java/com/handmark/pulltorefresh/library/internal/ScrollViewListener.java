package com.handmark.pulltorefresh.library.internal;


import com.handmark.pulltorefresh.library.ObservableScrollView;

/**
 * Created by Administrator on 2017/1/4.
 */

public interface ScrollViewListener {
    void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy);
}

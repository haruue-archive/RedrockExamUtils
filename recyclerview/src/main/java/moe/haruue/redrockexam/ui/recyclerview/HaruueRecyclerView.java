package moe.haruue.redrockexam.ui.recyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.LayoutRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class HaruueRecyclerView extends FrameLayout {

    protected View view;
    protected SwipeRefreshLayout swipeRefreshLayout;
    protected SwipeRefreshLayout.OnRefreshListener onRefreshListener;
    protected RecyclerView recyclerView;
    protected FrameLayout reuseLayout;

    View errorView;
    View emptyView;


    public HaruueRecyclerView(Context context) {
        super(context);
        initView();
    }

    public HaruueRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        initAttrs(attrs);
    }

    public HaruueRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        initAttrs(attrs);
    }

    public void initView() {
        if (isInEditMode()) {
            return;
        }
        view = View.inflate(getContext(), R.layout.haruue_recycler_view, this);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        reuseLayout = (FrameLayout) view.findViewById(R.id.reuse_view);
    }

    public void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.HaruueRecyclerView);
        setErrorView(typedArray.getResourceId(R.styleable.HaruueRecyclerView_layout_error, 0));
        setEmptyView(typedArray.getResourceId(R.styleable.HaruueRecyclerView_layout_empty, 0));
    }

    public void showProgress() {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
    }

    public void refresh() {
        showProgress();
        onRefreshListener.onRefresh();
    }

    public void onLoadFinish() {
        swipeRefreshLayout.setRefreshing(false);
    }

    public View getErrorView() {
        return errorView;
    }

    public void setErrorView(View errorView) {
        this.errorView = errorView;
    }

    public void setErrorView(@LayoutRes int errorViewRes) {
        if (errorViewRes == 0) {
            return;
        }
        setErrorView(View.inflate(getContext(), errorViewRes, null));
    }

    public void showError() {
        showReuseView(errorView);
    }

    public View getEmptyView() {
        return emptyView;
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
    }

    public void setEmptyView(@LayoutRes int emptyViewRes) {
        if (emptyViewRes == 0) {
            return;
        }
        setEmptyView(View.inflate(getContext(), emptyViewRes, null));
    }

    public void showEmpty() {
        showReuseView(emptyView);
    }

    protected void showReuseView(View view) {
        if (view == null) {
            return;
        }
        reuseLayout.removeAllViews();
        reuseLayout.addView(view);
        recyclerView.setVisibility(GONE);
        reuseLayout.setVisibility(VISIBLE);
    }

    public void showRecyclerView() {
        reuseLayout.setVisibility(GONE);
        recyclerView.setVisibility(VISIBLE);
    }

    public void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return swipeRefreshLayout;
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {

        recyclerView.setLayoutManager(layoutManager);
    }

    public void setItemAnimator(RecyclerView.ItemAnimator itemAnimator) {
        recyclerView.setItemAnimator(itemAnimator);
    }

}

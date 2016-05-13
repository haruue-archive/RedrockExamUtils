package moe.haruue.redrockexam.ui.recyclerview;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public abstract class HaruueAdapter<T> extends RecyclerView.Adapter<HaruueViewHolder> {

    protected final static int VIEW_TYPE_HEADER = 56115;
    protected final static int VIEW_TYPE_NORMAL = 0;
    protected final static int VIEW_TYPE_FOOTER = 89455;

    protected List<T> models;

    protected final Object threadLock = new Object();

    protected Context context;

    protected ViewGroup header;
    protected ViewGroup footer;

    protected boolean isAutoNotify;

    protected OnItemClickListener<T> onItemClickListener;

    protected OnItemLongClickListener<T> onItemLongClickListener;

    protected View moreView;
    protected View noMoreView;
    protected OnLoadMoreListener onLoadMoreListener;
    protected boolean hasMore = true;

    public HaruueAdapter(Context context) {
        this(context, (List<T>) null);
    }

    public HaruueAdapter(Context context, T[] models) {
        this(context, Arrays.asList(models));
    }

    public HaruueAdapter(Context context, @Nullable Collection<? extends T> models) {
        this.context = context;
        if (models == null) {
            this.models = new ArrayList<>(0);
        } else {
            this.models = new ArrayList<>(models);
        }
        header = new LinearLayout(context);
        header.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        footer = new LinearLayout(context);
        footer.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    @Override
    public HaruueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_HEADER) {
            return new HaruueViewHolder(header) {
                @Override
                public void setData(Object data) {

                }
            };
        }
        if (viewType == VIEW_TYPE_FOOTER) {
            return new HaruueViewHolder(footer) {
                @Override
                public void setData(Object data) {

                }
            };
        }

        final HaruueViewHolder viewHolder = onCreateHaruueViewHolder(parent, viewType);

        if (onItemClickListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = viewHolder.getModelPosition();
                    onItemClickListener.onItemClick(position, v, models.get(position));
                }
            });
        }

        if (onItemLongClickListener != null) {
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = viewHolder.getModelPosition();
                    return onItemLongClickListener.onItemLongClick(position, v, models.get(position));
                }
            });
        }

        return viewHolder;
    }

    public abstract HaruueViewHolder<T> onCreateHaruueViewHolder(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(HaruueViewHolder holder, int position) {
        // Skip header and footer
        if (position == 0 || position > models.size()) return;
        holder.setData(models.get(adapterPositionToModelPosition(position)));
    }

    public void add(T model) {
        synchronized (threadLock) {
            models.add(model);
            if (isAutoNotify) {
                notifyItemInserted(modelPositionToAdapterPosition(models.size() - 1));
            }
        }
    }

    public void addAll(T[] models) {
        synchronized (threadLock) {
            int before = this.models.size();
            this.models.addAll(Arrays.asList(models));
            if (isAutoNotify) {
                notifyItemRangeInserted(modelPositionToAdapterPosition(before), models.length);
            }
        }
    }

    public void addAll(Collection<? extends T> models) {
        synchronized (threadLock) {
            int before = this.models.size();
            this.models.addAll(models);
            if (isAutoNotify) {
                notifyItemRangeInserted(modelPositionToAdapterPosition(before), models.size());
            }
        }
    }

    public void insert(int position, T model) {
        synchronized (threadLock) {
            this.models.add(position, model);
            if (isAutoNotify) {
                notifyItemInserted(modelPositionToAdapterPosition(position));
            }
        }
    }

    public void insertAll(int position, T[] models) {
        synchronized (threadLock) {
            this.models.addAll(position, Arrays.asList(models));
            if (isAutoNotify) {
                notifyItemRangeInserted(modelPositionToAdapterPosition(position), models.length);
            }
        }
    }

    public void insertAll(int position, Collection<? extends T> models) {
        synchronized (threadLock) {
            this.models.addAll(position, models);
            if (isAutoNotify) {
                notifyItemRangeInserted(modelPositionToAdapterPosition(position), models.size());
            }
        }
    }

    public void change(int position, T model) {
        synchronized (threadLock) {
            this.models.remove(position);
            this.models.add(position, model);
            if (isAutoNotify) {
                notifyItemChanged(modelPositionToAdapterPosition(position));
            }
        }
    }

    public void change(T before, T after) {
        synchronized (threadLock) {
            int position = models.indexOf(before);
            while (position != -1) {
                change(position, after);
                position = models.indexOf(before);
            }
        }
    }

    public void remove(int position) {
        synchronized (threadLock) {
            models.remove(position);
            if (isAutoNotify) {
                notifyItemRemoved(modelPositionToAdapterPosition(position));
            }
        }
    }

    public void remove(T model) {
        synchronized (threadLock) {
            int position = this.models.indexOf(model);
            while (position != -1) {
                remove(position);
                position = this.models.indexOf(model);
            }
        }
    }

    public void removeAll(Collection<? extends T> models) {
        synchronized (threadLock) {
            for (T m : models) {
                remove(m);
            }
        }
    }

    public void sort(Comparator<? super T> comparator) {
        synchronized (threadLock) {
            Collections.sort(models, comparator);
            if (isAutoNotify) {
                notifyDataSetChanged();
            }
        }
    }

    public void clear() {
        synchronized (threadLock) {
            models.clear();
            if (isAutoNotify) {
                notifyDataSetChanged();
            }
        }
    }

    public T getItem(int position) {
        return models.get(position);
    }

    public void setAutoNotify(boolean autoNotify) {
        isAutoNotify = autoNotify;
    }

    @Deprecated
    public List<T> getModelArray() {
        return models;
    }

    public List<T> getModelArrayCopy() {
        return new ArrayList<>(models);
    }

    public interface OnItemClickListener<T> {
        void onItemClick(int position, View view, T model);
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemLongClickListener<T> {
        boolean onItemLongClick(int position, View view, T model);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<T> onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public static int adapterPositionToModelPosition(int adapterPosition) {
        return adapterPosition - 1;
    }

    public static int modelPositionToAdapterPosition(int modelPosition) {
        return modelPosition + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_HEADER;
        } else if (position == models.size() + 1) {
            return VIEW_TYPE_FOOTER;
        } else {
            return super.getItemViewType(position);
        }
    }

    public void addHeader(View headerView) {
        header.addView(headerView);
    }

    public void addFooter(View footerView) {
        footer.addView(footerView);
    }

    public void removeHeader(View headerView) {
        header.removeView(headerView);
    }

    public void removeFooter(View footerView) {
        footer.removeView(footerView);
    }

    public void removeAllHeader() {
        header.removeAllViews();
    }

    public void removeAllFooter() {
        footer.removeAllViews();
    }

    public View getHeader(int position) {
        return header.getChildAt(position);
    }

    public View getFooter(int position) {
        return footer.getChildAt(position);
    }

    public ViewGroup getHeaderGroup() {
        return header;
    }

    public ViewGroup getFooterGroup() {
        return footer;
    }

    public View getMoreView() {
        return moreView;
    }

    public void setMoreView(View moreView) {
        this.moreView = moreView;
    }

    public void setMoreView(@LayoutRes int moreViewRes) {
        if (moreViewRes == 0) {
            return;
        }
        setMoreView(View.inflate(context, moreViewRes, null));
    }

    public void showMore() {
        removeFooter(noMoreView);
        addFooter(moreView);
        if (onLoadMoreListener != null) {
            moreView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onLoadMoreListener.onLoadMore(models.size());
                }
            });
        }
        // Event delegate
    }

    public View getNoMoreView() {
        return noMoreView;
    }

    public void setNoMoreView(View noMoreView) {
        this.noMoreView = noMoreView;
    }

    public void setNoMoreView(int noMoreViewRes) {
        if (noMoreViewRes == 0) {
            return;
        }
        setNoMoreView(View.inflate(context, noMoreViewRes, null));
    }

    public void showNoMore() {
        removeFooter(moreView);
        addFooter(noMoreView);
        hasMore = false;
        if (onLoadMoreListener != null) {
            moreView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hasMore = true;
                    onLoadMoreListener.onLoadMore(models.size());
                }
            });
        }
    }

    public interface OnLoadMoreListener {
        void onLoadMore(int currentPosition);
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public int getItemCount() {
        return models.size() + 2;
    }
}

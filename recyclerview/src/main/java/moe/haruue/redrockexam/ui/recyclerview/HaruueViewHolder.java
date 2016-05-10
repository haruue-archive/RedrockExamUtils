package moe.haruue.redrockexam.ui.recyclerview;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public abstract class HaruueViewHolder<M> extends RecyclerView.ViewHolder {

    public HaruueViewHolder(View itemView) {
        super(itemView);
    }

    protected HaruueViewHolder(ViewGroup parent, @LayoutRes int layoutResource) {
        super(LayoutInflater.from(parent.getContext()).inflate(layoutResource, parent, false));
    }

    public abstract void setData(M data);

    protected <T extends View> T $(@IdRes int idResource) {
        return (T) itemView.findViewById(idResource);
    }

    protected Context getContext() {
        return itemView.getContext();
    }

}

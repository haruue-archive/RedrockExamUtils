package moe.haruue.redrockexam.util.test;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collection;

import moe.haruue.redrockexam.ui.recyclerview.HaruueAdapter;
import moe.haruue.redrockexam.ui.recyclerview.HaruueViewHolder;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class MenuItemAdapter extends HaruueAdapter<String> {

    public MenuItemAdapter(Context context) {
        super(context);
    }

    public MenuItemAdapter(Context context, @Nullable Collection<? extends String> models) {
        super(context, models);
    }

    @Override
    public HaruueViewHolder<String> onCreateHaruueViewHolder(ViewGroup parent, int viewType) {
        return new MenuItemViewHolder(parent);
    }

    public class MenuItemViewHolder extends HaruueViewHolder<String> {

        TextView titleView;

        public MenuItemViewHolder(View itemView) {
            super((ViewGroup) itemView, R.layout.item_main_menu);
            titleView = $(R.id.item_title);
        }

        @Override
        public void setData(String data) {
            titleView.setText(data);
        }
    }

}

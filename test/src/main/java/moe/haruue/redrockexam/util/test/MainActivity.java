package moe.haruue.redrockexam.util.test;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import java.util.ArrayList;

import moe.haruue.redrockexam.ui.recyclerview.HaruueAdapter;
import moe.haruue.redrockexam.ui.recyclerview.HaruueRecyclerView;
import moe.haruue.redrockexam.util.abstracts.HaruueActivity;

public class MainActivity extends HaruueActivity {

    HaruueRecyclerView recyclerView;
    MenuItemAdapter menuItemAdapter;
    Listener listener = new Listener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = $(R.id.recycler_view_main_menu);
        recyclerView.setAdapter(menuItemAdapter = new MenuItemAdapter(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        menuItemAdapter.setAutoNotify(true);
        final ArrayList<String> menuItems = new ArrayList<>(0);
        menuItems.add("database");
        menuItems.add("file");
        menuItems.add("human unit");
        menuItems.add("image provider");
        menuItems.add("network");
        menuItems.add("notification");
        menuItems.add("permission");
        menuItems.add("recycler view");
        menuItems.add("reflect");
        menuItems.add("util");
        menuItems.add("widget");
        menuItemAdapter.addAll(menuItems);
        menuItemAdapter.setOnItemClickListener(listener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected class Listener implements HaruueAdapter.OnItemClickListener<String> {

        @Override
        public void onItemClick(int position, View view, String model) {
            switch (model) {
                case "notification":
                    NotificationTestActivity.start(MainActivity.this);
                    break;
                case "image provider":
                    ImageProviderActivity.start(MainActivity.this);
            }
        }
    }

}

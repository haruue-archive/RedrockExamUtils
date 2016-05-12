package moe.haruue.redrockexam.util.test;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import java.util.ArrayList;

import moe.haruue.redrockexam.ui.recyclerview.HaruueRecyclerView;
import moe.haruue.redrockexam.util.abstracts.HaruueActivity;

public class MainActivity extends HaruueActivity {

    HaruueRecyclerView recyclerView;
    MenuItemAdapter menuItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = $(R.id.recycler_view_main_menu);
        recyclerView.setAdapter(menuItemAdapter = new MenuItemAdapter(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        menuItemAdapter.setAutoNotify(true);
        ArrayList<String> menuItems = new ArrayList<>(0);
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
        menuItems.add("oth0");
        menuItems.add("oth1");
        menuItems.add("oth2");
        menuItems.add("oth3");
        menuItems.add("oth4");
        menuItems.add("oth5");
        menuItems.add("oth6");
        menuItems.add("oth7");
        menuItems.add("oth8");
        menuItems.add("oth9");
        menuItemAdapter.addAll(menuItems);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

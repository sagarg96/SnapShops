package com.example.sagar.popupshops_buyerside;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class recycle extends view {

    private List<list> lists;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.recycle);

        rv=(RecyclerView)findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        initializeData();
        initializeAdapter();
    }

    private void initializeData(){
        lists = new ArrayList<>();
        lists.add(new list("1200", "store 1", R.drawable.alpha));
        lists.add(new list("600", "store 2", R.drawable.alpha));
        lists.add(new list("200", "store 2", R.drawable.alpha));

        //connect to DB
    }

    private void initializeAdapter(){
        RVAdapter adapter = new RVAdapter(lists);
        rv.setAdapter(adapter);
    }
}
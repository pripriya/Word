package com.geval6.word.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.geval6.word.Listener.Listener;
import com.geval6.word.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ListviewAdapter extends BaseAdapter implements Listener {
    ArrayList arrayList;
    Context context;
    HashMap hashMap;
    Typeface typeface;


    public ListviewAdapter(Context context, HashMap hashMap, ArrayList arrayList) {
        this.context = context;
        this.hashMap = hashMap;
        this.arrayList = arrayList;
    }

    public int getCount() {
        return this.arrayList.size();
    }

    public long getItemId(int i) {
        return 1;
    }

    public Object getItem(int position){
        return this.hashMap.get(this.arrayList.get(position));
    }

    public void recyclerViewListClicked(View v, int position) {
    }

    public View getView(int position, View itemView, ViewGroup parent) {
        TextView textView;
        RecyclerView recyclerView;
        LinearLayoutManager linearLayoutManager;
        if (position == 1) {
            typeface= Typeface.createFromAsset(context.getAssets(),"WorkSans-Medium.otf");

            itemView = ((LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_listview_item, parent, false);
            textView = (TextView) itemView.findViewById(R.id.trending);
            textView.setTypeface(typeface);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerView);
            linearLayoutManager = new LinearLayoutManager(this.context);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            textView.setText((String) ((HashMap) getItem(position)).get("label"));
            recyclerView.setAdapter(new TrendingAdapter(this.context, this.hashMap ));
            return itemView;
        } else if (position == 0) {
            typeface= Typeface.createFromAsset(context.getAssets(),"WorkSans-Medium.otf");

            itemView = ((LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_listview_item, parent, false);
            textView = (TextView) itemView.findViewById(R.id.trending);
            textView.setTypeface(typeface);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerView);
            linearLayoutManager = new LinearLayoutManager(this.context);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            textView.setText((String) ((HashMap) getItem(position)).get("label"));
            recyclerView.setAdapter(new GospelAdapter(this.context, this.hashMap));
            return itemView;
        }
        else if(position==2){
            typeface= Typeface.createFromAsset(context.getAssets(),"WorkSans-Medium.otf");

            itemView = ((LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_listview_item, parent, false);
            textView = (TextView) itemView.findViewById(R.id.trending);
            textView.setTypeface(typeface);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerView);
            linearLayoutManager = new LinearLayoutManager(this.context);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            textView.setText((String) ((HashMap) getItem(position)).get("label"));
            recyclerView.setAdapter(new SuggestedAdapter(this.context, this.hashMap));
            recyclerView.setPadding(0,0,0,10);
            return itemView;
        }
        return itemView;
    }
}

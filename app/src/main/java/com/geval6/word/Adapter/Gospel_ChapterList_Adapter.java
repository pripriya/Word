package com.geval6.word.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.geval6.word.Core.Audio_Activity;
import com.geval6.word.Core.SampleAudio;
import com.geval6.word.R;
import com.geval6.word.RequestManager.RequestIdentifier;
import com.geval6.word.Singleton.SingletonClass;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class Gospel_ChapterList_Adapter extends BaseAdapter {
    Context context;
    ArrayList gospelList;
    HashMap items;

    public Gospel_ChapterList_Adapter(Context context, HashMap items) {
        this.context = context;
        this.items = items;
        this.gospelList = (ArrayList) items.get("items");
    }

    public int getCount() {
        return this.gospelList.size();
    }

    public Object getItem(int position) {
        return gospelList.get(position);
    }

    public long getItemId(int i) {
        return 0;
    }

    public View getView(int position, View view, ViewGroup viewGroup) {

        ImageView imageView;
        view = ((LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.gospel_chapter_list, viewGroup, false);
        final HashMap item = (HashMap) this.gospelList.get(position);
        ((TextView) view.findViewById(R.id.chapterList)).setText(item.get("audioTitle").toString());
        imageView=(ImageView)view.findViewById(R.id.imageView);

        String artist= item.get("artist").toString();
        Picasso.with(context).load(RequestIdentifier.url + "/Content/" + artist.replaceAll(" ", "_").toLowerCase()  + ".jpg").into(imageView);

        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("FILES......", items.toString());
                Intent intent = new Intent(Gospel_ChapterList_Adapter.this.context, SampleAudio.class);
                SingletonClass.getInstance().itemsForGospel = item;
                Gospel_ChapterList_Adapter.this.context.startActivity(intent);
            }
        });
        return view;
    }
}

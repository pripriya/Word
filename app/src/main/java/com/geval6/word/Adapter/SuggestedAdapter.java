package com.geval6.word.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geval6.word.Core.SampleAudio;
import com.geval6.word.Listener.Listener;
import com.geval6.word.R;
import com.geval6.word.RequestManager.RequestIdentifier;
import com.geval6.word.Singleton.SingletonClass;
import com.geval6.word.ViewHolder.RecyclerViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class SuggestedAdapter extends Adapter<RecyclerViewHolder> {
    HashMap content;
    Context context;
    public ArrayList items;
    public String url= "http://70.35.199.150";


    public SuggestedAdapter(Context context, HashMap content) {
        this.context = context;
        this.content = content;
        this.items = (ArrayList) ((HashMap) content.get("Suggested")).get("items");
    }

    /* renamed from: com.geval6.word.Adapter.SuggestedAdapter.1 */
    class C02921 implements Listener {
        final /* synthetic */ HashMap val$item;

        C02921(HashMap hashMap) {
            this.val$item = hashMap;
        }

        public void recyclerViewListClicked(View v, int position) {
            Intent intent = new Intent(SuggestedAdapter.this.context, SampleAudio.class);
            SingletonClass.getInstance().itemsForGospel = this.val$item;
            SuggestedAdapter.this.context.startActivity(intent);
        }
    }



    public int getItemCount() {
        return this.items.size();
    }

    public int getItemViewType(int position) {
        return position;
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.trending_recycler_view, parent, false));
    }

    public void onBindViewHolder(RecyclerViewHolder holder, int position) {

        HashMap item = (HashMap) this.items.get(position);
        String artist= item.get("artist").toString();

        holder.title.setText(item.get("audioTitle").toString());
        holder.gospel.setText(item.get("gospel").toString());
        Picasso.with(context).load(RequestIdentifier.url+ "/Content/" + artist.replaceAll("", "_").toLowerCase()  + ".jpg").into(holder.imageView);
        holder.setClickListener(new C02921(item));
    }
}

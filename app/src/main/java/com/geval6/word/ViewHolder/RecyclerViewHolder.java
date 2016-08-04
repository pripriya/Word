package com.geval6.word.ViewHolder;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.geval6.word.Listener.Listener;
import com.geval6.word.R;

public class RecyclerViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
    public TextView gospel,title;
    public ImageView imageView;
    Listener listener;
    Typeface typeface ;

    public RecyclerViewHolder(View itemView ) {
        super(itemView);

        typeface= Typeface.createFromAsset(itemView.getContext().getAssets(),"WorkSans-Medium.otf");

        this.title = (TextView) itemView.findViewById(R.id.title);
        this.title.setTypeface(typeface);
        this.gospel = (TextView) itemView.findViewById(R.id.gospel);
        this.gospel.setTypeface(typeface);

        this.imageView=(ImageView)itemView.findViewById(R.id.imageView);


        itemView.setOnClickListener(this);
    }

    public void setClickListener(Listener itemClickListener) {
        this.listener = itemClickListener;
    }

    public void onClick(View view) {
        this.listener.recyclerViewListClicked(view, getPosition());
    }
}

package com.geval6.word.Core;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;

import com.geval6.word.Adapter.Gospel_ChapterList_Adapter;
import com.geval6.word.R;
import com.geval6.word.Singleton.SingletonClass;


public class Activity_Gospel_List extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView( R.layout.activity_listview_gospel_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((ListView) findViewById(R.id.listview)).setAdapter(new Gospel_ChapterList_Adapter(this, SingletonClass.getInstance().itemsForGospel));
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    public boolean onOptionsItemSelected(MenuItem paramMenuItem) {
        onBackPressed();
        return true;
    }
}

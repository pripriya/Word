package com.geval6.word.Core;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.geval6.word.Adapter.ListviewAdapter;
import com.geval6.word.BuildConfig;
import com.geval6.word.R;
import com.geval6.word.RequestManager.RequestFunctions;
import com.geval6.word.RequestManager.RequestIdentifier;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;

public class MainActivity extends AppCompatActivity {
    ArrayList arrayList;
    HashMap hashMap;
    ListView listView;
    LinearLayout progressCircle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ActionBar actionBar= getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled( true );

        LayoutInflater inflater = LayoutInflater.from( this );
        View view = inflater.inflate( R.layout.actionbar_layout, null );
        actionBar.setCustomView(view);
        setContentView(R.layout.activity_listview);

        CheckInterenetConnectivity checkInterenetConnectivity = new CheckInterenetConnectivity();

        this.listView = (ListView) findViewById(R.id.listview);
        this.progressCircle = (LinearLayout) findViewById(R.id.progressCircle);


        if(checkInterenetConnectivity.getConnectivityStatus(this)==true){
            AsyncCallWS task = new AsyncCallWS();
            task.execute();
        }

       else if(checkInterenetConnectivity.getConnectivityStatus(this)==false){
            Toast.makeText(this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
            finish();
        }



    }

    private class AsyncCallWS extends AsyncTask<String, Void, HashMap> {
        private AsyncCallWS() {
        }

        protected void onPreExecute() {
            progressCircle.setVisibility(View.VISIBLE);
        }

        protected HashMap doInBackground(String... params) {
            Log.i("do", "doInBackground");
            SoapObject Request = new SoapObject(RequestIdentifier.NAMESPACE, RequestIdentifier.METHOD_NAME);
            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.setOutputSoapObject(Request);
            soapEnvelope.dotNet = true;
            HttpTransportSE transport = new HttpTransportSE(RequestIdentifier.URL);
            try {
                transport.debug = true;
                transport.call(RequestIdentifier.SOAP_ACTION, soapEnvelope);
                SoapObject response = (SoapObject) soapEnvelope.bodyIn;


                return (HashMap) RequestFunctions.objectFromJson(soapEnvelope.getResponse().toString());
            } catch (Exception ex) {
                Log.e(BuildConfig.FLAVOR, "Error: " + ex.getMessage());
                return null;
            }
        }

        protected void onPostExecute(HashMap result) {

            if(result!=null) {
                HashMap result1 = (HashMap) result.get("response");
                MainActivity.this.arrayList = (ArrayList) result1.get("order");
                MainActivity.this.hashMap = (HashMap) result1.get("content");
                MainActivity.this.showView(MainActivity.this.hashMap, MainActivity.this.arrayList);
                MainActivity.this.progressCircle.setVisibility(View.INVISIBLE);
            }

            else if (result == null) {
                Toast.makeText(getApplicationContext(), "Connection timed out", Toast.LENGTH_SHORT).show();
                MainActivity.this.progressCircle.setVisibility(View.INVISIBLE);

            }
        }
    }


    public void showView(HashMap content, ArrayList order) {
        ((ListView) findViewById(R.id.listview)).setAdapter(new ListviewAdapter(this, content, order));
    }

    protected void onPostResume() {
        super.onPostResume();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

                return super.onOptionsItemSelected(item);
        }


    }


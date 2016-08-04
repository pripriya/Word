package com.geval6.word.Core;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.geval6.word.BuildConfig;
import com.geval6.word.R;
import com.geval6.word.RequestManager.RequestFunctions;
import com.geval6.word.Singleton.SingletonClass;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class SampleAudio extends AppCompatActivity implements OnErrorListener, OnCompletionListener {
    TextView endtime;
    boolean flag;
    Intent intent;
    boolean isPlaying;
    HashMap item;
    final Handler mHandler;
    private MediaPlayer mediaPlayer;
    private Runnable moveSeekBarThread;
    ArrayList pars;
    ImageView play;
    public SeekBar seekbar;
    String selectedSpan;
    TextView starttime;
    WebView webView;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        setContentView(R.layout.activity_musicplayer);
        intent = getIntent();

        TextView artist = (TextView) findViewById(R.id.artistname);
        TextView gospel = (TextView) findViewById(R.id.gospelname);
        TextView audioTitle = (TextView) findViewById(R.id.audioTitle);
        this.seekbar = (SeekBar) findViewById(R.id.seekBar);
        this.starttime = (TextView) findViewById(R.id.starttime);
        this.endtime = (TextView) findViewById(R.id.endtime);
        this.webView = (WebView) findViewById(R.id.webview);

        this.webView.getSettings().setJavaScriptEnabled(true);

        this.item = SingletonClass.getInstance().itemsForGospel;

        artist.setText(this.item.get("artist").toString());
        gospel.setText(this.item.get("gospel").toString());
        audioTitle.setText(this.item.get("audioTitle") + " :");
        this.webView.loadUrl(this.item.get("HTML").toString());

        new AsyncHttpTask().execute(new String[]{this.item.get("SMIL").toString()});

        this.play = (ImageView) findViewById(R.id.imageView);
        this.play.setImageResource(R.drawable.pausehiddden);
        startMediaPlayer();

        this.play.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
               togglePlayPause();
            }
        });
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Configuration config=getResources().getConfiguration();
        if(config.orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            setContentView(R.layout.activity_musicplayer);
            
        }
        else if(config.orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            setContentView(R.layout.activity_musicplayer);
        }
    }



    private void togglePlayPause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            play.setImageResource(R.drawable.play);
        } else {
            mediaPlayer.start();
            mediaPlayer.seekTo(seekbar.getProgress());
            seekbar.setProgress(mediaPlayer.getCurrentPosition());
            seekbar.setMax(mediaPlayer.getDuration());
            mHandler.removeCallbacks(moveSeekBarThread);
            mHandler.postDelayed(moveSeekBarThread, 1000);
            play.setImageResource(R.drawable.pause);
        }
    }

    private void startMediaPlayer() {
        Uri uri = Uri.parse(this.item.get("fileLink").toString());
        this.mediaPlayer = new MediaPlayer();
        this.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            this.mediaPlayer.setDataSource(getApplicationContext(), uri);
            this.mediaPlayer.prepareAsync();
            mediaPlayer.start();
        }
         catch (IOException e3) {
            e3.printStackTrace();
        }

            this.mediaPlayer.setOnPreparedListener(new OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    togglePlayPause();
                    mediaPlayer.seekTo(seekbar.getProgress());
                    seekbar.setProgress(mediaPlayer.getCurrentPosition());
                    seekbar.setMax(mediaPlayer.getDuration());
                    mHandler.removeCallbacks(moveSeekBarThread);
                    mHandler.postDelayed(moveSeekBarThread, 1000);
                }
            });

        this.mediaPlayer.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
                seekbar.setSecondaryProgress(i);
            }

        });
    }

    public SampleAudio() {
        this.mHandler = new Handler();
        this.isPlaying = true;
        this.moveSeekBarThread = new Runnable() {
            int Duration;
            int Durationminutes;
            int Durationseconds;
            int Posminutes;
            int Posseconds;

            @Override
            public void run() {

                    int currentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    String spanToHighlight = getSpanId(currentPosition);
                    if (spanToHighlight != selectedSpan) {
                        if (selectedSpan != null) {
                            dehighlight(selectedSpan);
                        }
                        highlight(spanToHighlight);
                    }
                    seekbar.setProgress(mediaPlayer.getCurrentPosition());
                    this.Posseconds = currentPosition % 60;
                    this.Posminutes = (currentPosition / 60) % 60;
                    this.Duration = mediaPlayer.getDuration() - mediaPlayer.getCurrentPosition();
                    this.Durationseconds = (this.Duration / 1000) % 60;
                    this.Durationminutes = (this.Duration / 60000) % 60;
                    starttime.setText(String.format("%02d:%02d", new Object[]{Integer.valueOf(this.Posminutes), Integer.valueOf(this.Posseconds)}));
                    endtime.setText(String.format("%02d:%02d", new Object[]{Integer.valueOf(this.Durationminutes), Integer.valueOf(this.Durationseconds)}));

                    mHandler.postDelayed(this, 1000);
                }

        };
    }
//    @Override
//    public void onDestroy(){
//        super.onDestroy();
//
//        mediaPlayer.stop();
//        mediaPlayer.reset();
//        mediaPlayer.release();
//        mediaPlayer = null;
//
//    }
//

    protected void onStop() {
        super.onStop();
        if (mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        else
            mediaPlayer.release();

    }

    private void highlight(String spanId) {
        this.webView.evaluateJavascript("document.getElementById('" + spanId + "').className = 'highlight';", null);
        this.selectedSpan = spanId;
    }

    private void dehighlight(String spanId) {
        this.webView.evaluateJavascript("document.getElementById('" + spanId + "').className = '';", null);
        this.selectedSpan = null;
    }

    private String getSpanId(int seconds) {
        if (this.pars != null) {
            Iterator it = this.pars.iterator();
            while (it.hasNext()) {
                Object span = it.next();
                float _start = Float.valueOf(((HashMap) span).get("beginClip").toString()).floatValue();
                float _end = Float.valueOf(((HashMap) span).get("endClip").toString()).floatValue();
                if (_start < ((float) seconds) && _end > ((float) seconds)) {
                    return ((HashMap) span).get("spanId").toString();
                }
            }
        }
        return null;
    }

    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        mediaPlayer.reset();
        return false;
    }

    public void onCompletion(MediaPlayer mediaPlayer)
    {
        play.setImageResource(R.drawable.play);
        startMediaPlayer();
    }

    public void onBackPressed() {
        super.onBackPressed();
//        if (mediaPlayer != null) {
//            mediaPlayer.stop();
//            mediaPlayer.release();
//            mediaPlayer = null;
//        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.zoom, menu);
        invalidateOptionsMenu();
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        boolean z = false;
        switch (item.getItemId()) {
            case 16908332:
                onBackPressed();
                break;
            case R.id.zoom /*2131493034*/:
                if (this.flag) {
                    this.webView.setInitialScale(0);
                    item.setTitle("A+");
                } else {
                    this.webView.setInitialScale(Callback.DEFAULT_DRAG_ANIMATION_DURATION);
                    item.setTitle("A");
                }
                if (!this.flag) {
                    z = true;
                }
                this.flag = z;
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {
        protected Integer doInBackground(String... params) {
            InputStream inputStream;
            Exception e;
            try {
                HttpURLConnection urlConnection = (HttpURLConnection) new URL(params[0]).openConnection();
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestMethod("GET");
                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream2 = new BufferedInputStream(urlConnection.getInputStream());
                    try {
                        parseResult(convertInputStreamToString(inputStream2));
                        inputStream = inputStream2;
                    } catch (Exception e2) {
                        e = e2;
                        inputStream = inputStream2;
                        Log.d("Exception", e.getLocalizedMessage());
                        return null;
                    }
                }
            } catch (Exception e3) {
                e = e3;
                Log.d("Exception", e.getLocalizedMessage());
                return null;
            }
            return null;
        }



        private String convertInputStreamToString(InputStream inputStream) throws IOException {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = BuildConfig.FLAVOR;
            String result = BuildConfig.FLAVOR;
            while (true) {
                line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                result = result + line;
            }
            if (inputStream != null) {
                inputStream.close();
            }
            return result;
        }

        public void parseResult(String result) {
            try {
                HashMap result1 = (HashMap) RequestFunctions.objectFromJson(result);
                pars = (ArrayList) result1.get("pars");
                Log.i("Response",pars.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}


package com.geval6.word.Core;

import android.content.Intent;
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

public class Audio_Activity extends AppCompatActivity implements OnErrorListener, OnCompletionListener {
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
        this.intent = getIntent();
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
                if (mediaPlayer.isPlaying()) {
                    isPlaying = false;
                    mediaPlayer.pause();
                    play.setImageResource(R.drawable.play);
                    return;
                }
                mediaPlayer.start();
                mediaPlayer.seekTo(Audio_Activity.this.seekbar.getProgress());
                seekbar.setProgress(Audio_Activity.this.mediaPlayer.getCurrentPosition());
                seekbar.setMax(Audio_Activity.this.mediaPlayer.getDuration());
                mHandler.removeCallbacks(Audio_Activity.this.moveSeekBarThread);
                mHandler.postDelayed(Audio_Activity.this.moveSeekBarThread, 1000);
                play.setImageResource(R.drawable.pause);

            }
        });
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
                if (urlConnection.getResponseCode() == Callback.DEFAULT_DRAG_ANIMATION_DURATION) {
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
                Audio_Activity.this.pars = (ArrayList) result1.get("pars");
                Log.i("Response", Audio_Activity.this.pars.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Audio_Activity() {
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

                    int currentPosition = Audio_Activity.this.mediaPlayer.getCurrentPosition() / 1000;
                    Log.i("SECONDS", Integer.toString(currentPosition));
                    String spanToHighlight = Audio_Activity.this.getSpanId(currentPosition);
                    if (spanToHighlight != Audio_Activity.this.selectedSpan) {
                        if (Audio_Activity.this.selectedSpan != null) {
                            Audio_Activity.this.dehighlight(Audio_Activity.this.selectedSpan);
                        }
                        Audio_Activity.this.highlight(spanToHighlight);
                    }
                    Audio_Activity.this.seekbar.setProgress(Audio_Activity.this.mediaPlayer.getCurrentPosition());
                    this.Posseconds = currentPosition % 60;
                    this.Posminutes = (currentPosition / 60) % 60;
                    this.Duration = Audio_Activity.this.mediaPlayer.getDuration() - Audio_Activity.this.mediaPlayer.getCurrentPosition();
                    this.Durationseconds = (this.Duration / 1000) % 60;
                    this.Durationminutes = (this.Duration / 60000) % 60;
                    Audio_Activity.this.starttime.setText(String.format("%02d:%02d", new Object[]{Integer.valueOf(this.Posminutes), Integer.valueOf(this.Posseconds)}));
                    Audio_Activity.this.endtime.setText(String.format("%02d:%02d", new Object[]{Integer.valueOf(this.Durationminutes), Integer.valueOf(this.Durationseconds)}));
                    Audio_Activity.this.mHandler.postDelayed(this, 1000);

            }
        };
    }


    private void startMediaPlayer() {
        Uri uri = Uri.parse(this.item.get("fileLink").toString());
        this.mediaPlayer = new MediaPlayer();
        this.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            this.mediaPlayer.setDataSource(getApplicationContext(), uri);
        } catch (IllegalArgumentException e1) {
            e1.printStackTrace();
        } catch (SecurityException e1) {
            e1.printStackTrace();
        } catch (IllegalStateException e2) {
            e2.printStackTrace();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
        if (this.isPlaying) {
            try {
                this.mediaPlayer.prepareAsync();
            } catch (IllegalStateException e4) {
                e4.printStackTrace();
            }
            this.mediaPlayer.setOnPreparedListener(new OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                        mediaPlayer.start();
                        play.setImageResource(R.drawable.pause);
                        play.setClickable(true);
                        mediaPlayer.seekTo(Audio_Activity.this.seekbar.getProgress());
                        seekbar.setProgress(Audio_Activity.this.mediaPlayer.getCurrentPosition());
                        seekbar.setMax(Audio_Activity.this.mediaPlayer.getDuration());
                        mHandler.removeCallbacks(Audio_Activity.this.moveSeekBarThread);
                        mHandler.postDelayed(Audio_Activity.this.moveSeekBarThread, 1000);
                }
            });
        }
        this.mediaPlayer.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {

            }
        });
    }

    protected void onStop() {

        if(mediaPlayer!=null && mediaPlayer.isPlaying())
        {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        else if(mediaPlayer==null)
        {
            mediaPlayer.release();

        }
        super.onStop();
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

    public void onCompletion(MediaPlayer mediaPlayer) {
        startMediaPlayer();
    }

    public void onBackPressed() {
        super.onBackPressed();
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
            case R.id.zoom:
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
}

package com.tomvarga.androidproject2;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MediaPlayerActivity extends AppCompatActivity {

    private FloatingActionButton player;
    private boolean playPause = false;
    private boolean initialStage = true;
    private android.media.MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private Handler handler;
    private ProgressDialog progressDialog;

    private String username;
    private String token;

    SharedPrefs modSharedPrefs;

    Long albumId;
    Long id;
    String author;
    String album;
    String genre;
    String songName;

    TextView authorTXV;
    TextView albumTXV;
    TextView genreTXV;
    TextView songNameTXV;
    ImageView imageAlbum;
    ImageView btnBack;

    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        modSharedPrefs = new SharedPrefs(this);
        if (modSharedPrefs.loadDarkModeState() == true) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.song_player);

        Bundle b = getIntent().getExtras();

        handler = new Handler();
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        authorTXV = findViewById(R.id.authorTXV);
        albumTXV = findViewById(R.id.albumTXV);
        songNameTXV = findViewById(R.id.songTXV);
        genreTXV = findViewById(R.id.genreTXV);
        imageAlbum = findViewById(R.id.imageAlbum);
        btnBack = findViewById(R.id.buttonBack);
        player = findViewById(R.id.playOrPause);


        songNameTXV.setSelected(true);


        albumId = b.getLong("albumId");
        id = b.getLong("id");
        author = b.getString("author");
        album = b.getString("album");
        genre = b.getString("genre");
        songName = b.getString("songName");

        authorTXV.setText(author);
        albumTXV.setText(album);
        songNameTXV.setText(songName);
        genreTXV.setText(genre);

        new SendHttpRequestTask(albumId,imageAlbum).execute();
        mediaPlayer = new android.media.MediaPlayer();
        progressDialog = new ProgressDialog(this);


        player.setImageResource(R.drawable.ic_action_play);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setAudioAttributes(
                new AudioAttributes
                        .Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build());

        player.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (!playPause){   //ak nehra ta ideme spustat a prepneme obrazok na hraje        vetva na spustenie
                    player.setImageResource(R.drawable.ic_action_pause);

                    if (initialStage) { // len raz spustit asyncTask request
                        new Player().execute(modSharedPrefs.getIP()+"/streamSong?id="+id);

                    } else {
                        if (!mediaPlayer.isPlaying()){  //ak uz asynTask presiel a nehra spusti
                            mediaPlayer.start();
                            changeSeekbar();
                        }
                    }

                    playPause = true;
                } else {  //playPause je true    vetva na zastavenie
                    player.setImageResource(R.drawable.ic_action_play);

                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    }

                    playPause = false;
                }
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b)
                {
                    mediaPlayer.seekTo(i);  // ak sa zmenil stav nastav playler na current time
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        username = getSharedPreferences("user", MODE_PRIVATE).getString("username","default");
        token = getSharedPreferences("user", MODE_PRIVATE).getString("token","default");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mediaPlayer != null){
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        finish();
    }

//    @Override
//    public void finish() {
//        super.finish();
//        overridePendingTransition(0,R.anim.slide_out_left);
//    }

//    @Override
//    protected void onPause() { //Pause means shuting down I thing
//        super.onPause();
//
//        Log.i("onPause","Time "+mediaPlayer.getCurrentPosition());
//
//    }

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putInt("timePlayed", mediaPlayer.getCurrentPosition());
//        Log.i("onSaveInstanceState", String.valueOf(outState.getInt("timePlayed")));
//
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        int time = savedInstanceState.getInt("timePlayed");
//        mediaPlayer.seekTo(time);
//        changeSeekbar();
//        Log.i("onRestoreInstanceState", String.valueOf(savedInstanceState.getInt("timePlayed")));
//
//    }



    private void  changeSeekbar(){
        seekBar.setProgress(mediaPlayer.getCurrentPosition());
        if (mediaPlayer.isPlaying())
        {
            runnable = new Runnable() {
                @Override
                public void run() {
                    changeSeekbar();
                }
            };
            handler.postDelayed(runnable, 300); //handler je na to ze invokne runnable na threade napr tu kazdu sekundu
        }
    }



    class Player extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {

            Map<String,String> headers = new HashMap<String, String>();
            headers.put("username",username);
            headers.put("token",token);

            Uri uri = Uri.parse(strings[0]);


            Log.d("URI",uri.toString());
            Log.d("Username",headers.get("username"));
            Log.d("Token",headers.get("token"));

            try {
                mediaPlayer.setDataSource(MediaPlayerActivity.this.getApplicationContext(),uri,headers);
                mediaPlayer.setOnCompletionListener(new android.media.MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(android.media.MediaPlayer mp) {
                        initialStage = true;
                        playPause = false;
                        player.setImageResource(R.drawable.ic_action_play);
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                        Log.i("SONG DURATION", String.valueOf(mediaPlayer.getDuration()));
                    }
                });

                mediaPlayer.prepare();

            } catch (Exception e){
                Log.e("MyAudioStreamApp", e.getMessage());
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (progressDialog.isShowing()){
                progressDialog.cancel();
            }
            seekBar.setMax(mediaPlayer.getDuration()); //30mins in milliseconds
            mediaPlayer.start();
            initialStage = false;
            changeSeekbar();

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.setMessage("Buffering...");
            progressDialog.show();
        }
    }
    
    //Only Cover Photo
    private class SendHttpRequestTask extends AsyncTask<String, Void, Bitmap> {

        Long idAlbum;
        ImageView imageView;

        SendHttpRequestTask(Long idAlbum, ImageView imageView) {
            this.idAlbum =idAlbum;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                //ipconfig
                URL url = new URL(modSharedPrefs.getIP()+"/getAlbumCover?id="+idAlbum);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            }catch (Exception e){
                Log.d("Exception Message",e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }
}

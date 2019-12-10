package com.tomvarga.androidproject2;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MediaPlayer extends AppCompatActivity {

    private FloatingActionButton player;
    private boolean playPause;
    private boolean initialStage = true;
    private android.media.MediaPlayer mediaPlayer;
    private ProgressDialog progressDialog;

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

        albumId = b.getLong("albumId");
        id = b.getLong("id");
        author = b.getString("author");
        album = b.getString("album");
        genre = b.getString("genre");
        songName = b.getString("songName");

        authorTXV = findViewById(R.id.authorTXV);
        authorTXV.setText(author);
        albumTXV = findViewById(R.id.albumTXV);
        albumTXV.setText(album);
        songNameTXV = findViewById(R.id.songTXV);
        songNameTXV.setText(songName);
        genreTXV = findViewById(R.id.genreTXV);
        genreTXV.setText(genre);
        imageAlbum = findViewById(R.id.imageAlbum);
        btnBack = findViewById(R.id.buttonBack);

        new SendHttpRequestTask(albumId,imageAlbum).execute();

        player = findViewById(R.id.playOrPause);
        player.setImageResource(R.drawable.ic_action_play);
        mediaPlayer = new android.media.MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//        mediaPlayer.setAudioAttributes(AudioManager.STREAM_MUSIC);
        progressDialog = new ProgressDialog(this);
        player.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (!playPause){   //ak nehra ta ideme spustat a prepneme obrazok na hraje
                    player.setImageResource(R.drawable.ic_action_pause);

                    if (initialStage) {
                        new Player().execute("http://192.168.137.1:8080/streamSong?id="+id);
//                        new Player().execute("https://www.ssaurel.com/tmp/mymusic.mp3");

                    } else {
                        if (!mediaPlayer.isPlaying()){
                            mediaPlayer.start();
                        }
                    }

                    playPause = true;
                } else {
                    player.setImageResource(R.drawable.ic_action_play);

                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    }

                    playPause = false;
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,R.anim.slide_out_left);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mediaPlayer != null){
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    class Player extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            Boolean prepared = false;

            try {
                mediaPlayer.setDataSource(strings[0]);
                mediaPlayer.setOnCompletionListener(new android.media.MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(android.media.MediaPlayer mp) {
                        initialStage = true;
                        playPause = false;
                        player.setImageResource(R.drawable.ic_action_play);
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                    }
                });

                mediaPlayer.prepare();
                prepared = true;

            } catch (Exception e){
                Log.e("MyAudioStreamApp", e.getMessage());
                prepared = false;
            }

            return prepared;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (progressDialog.isShowing()){
                progressDialog.cancel();
            }

            mediaPlayer.start();
            initialStage = false;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.setMessage("Buffering...");
            progressDialog.show();
        }
    }

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
                URL url = new URL("http://192.168.43.123:8080/getAlbumCover?id="+idAlbum);
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

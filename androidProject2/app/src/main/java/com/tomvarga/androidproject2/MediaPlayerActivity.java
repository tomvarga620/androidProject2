package com.tomvarga.androidproject2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tomvarga.androidproject2.POJO.Song;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MediaPlayerActivity extends AppCompatActivity {

    private FloatingActionButton player;
    private FloatingActionButton previousSong;
    private FloatingActionButton nextSong;
    private FloatingActionButton favSong;

    private boolean playPause = false;
    private boolean initialStage = true;
    private android.media.MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private Handler handler;
    private ProgressDialog progressDialog;

    private String username;
    private String token;

    SharedPrefs modSharedPrefs;
    int currentSongIndex;


    ArrayList<Song> currentSongList = new ArrayList<>();

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

    TextView currentTime;
    TextView limitTime;

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
        previousSong = findViewById(R.id.rewindBack);
        nextSong = findViewById(R.id.rewindForward);
        favSong = findViewById(R.id.favoriteSong);

        currentTime = findViewById(R.id.currentTime);
        limitTime = findViewById(R.id.limitTime);


        songNameTXV.setSelected(true);


        albumId = b.getLong("albumId");
        id = b.getLong("id");
        author = b.getString("author");
        album = b.getString("album");
        genre = b.getString("genre");
        songName = b.getString("songName");

        loadCurrentSongList();

        authorTXV.setText(author);
        albumTXV.setText(album);
        songNameTXV.setText(songName);
        genreTXV.setText(genre);

        new SendHttpRequestTask(albumId,imageAlbum).execute();
        mediaPlayer = new android.media.MediaPlayer();
        progressDialog = new ProgressDialog(this);


        nextSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playNextSong();
            }
        });

        previousSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent player = new Intent(MediaPlayerActivity.this, MediaPlayerActivity.class);
                Song song;
                if (currentSongIndex==0) {
                    song = currentSongList.get(currentSongList.size()-1);
                } else {
                    song = currentSongList.get(currentSongIndex-1);
                }
                player.putExtra("id",song.getId());
                player.putExtra("author",song.getAuthor());
                player.putExtra("album",album);
                player.putExtra("albumId",albumId);
                player.putExtra("genre",song.getGenre());
                player.putExtra("songName",song.getSongName());
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    handler.removeCallbacks(runnable);
                    mediaPlayer.release();
                }
                finish();
                startActivity(player);
            }
        });

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

        favSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddingDialog();
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
        initialClick();
        isSongLiked();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mediaPlayer != null){
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
            handler.removeCallbacks(runnable);
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

        int currentPosition = mediaPlayer.getCurrentPosition();

        String time = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(currentPosition),
                TimeUnit.MILLISECONDS.toSeconds(currentPosition) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(currentPosition))
        );
        seekBar.setProgress(currentPosition);
        currentTime.setText(time);
        if (mediaPlayer.isPlaying())
        {
            runnable = new Runnable() {
                @Override
                public void run() {
                    changeSeekbar();
                }
            };
            handler.postDelayed(runnable, 1000); //handler je na to ze invokne runnable na threade napr tu kazdu sekundu
        }


        if (currentPosition == mediaPlayer.getDuration()) {
            playNextSong();
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
            int duration = mediaPlayer.getDuration();
            seekBar.setMax(duration); //30mins in milliseconds
            String time = String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(duration),
                    TimeUnit.MILLISECONDS.toSeconds(duration) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
            );
            limitTime.setText(time);
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

    public void loadCurrentSongList() {
       SharedPreferences sharedPreferences = getSharedPreferences("songListPreferences", MODE_PRIVATE);
       Gson gson = new Gson();
       String json = sharedPreferences.getString("currentListSong",null);
       Type type = new TypeToken<ArrayList<Song>>() {}.getType();
       currentSongList = gson.fromJson(json,type);

       for (int i=0;i<currentSongList.size();i++) {
           if (id == currentSongList.get(i).getId()) {
               currentSongIndex = i;
               break;
           }
       }
    }

    public void initialClick() {
        player.setImageResource(R.drawable.ic_action_pause);
        new Player().execute(modSharedPrefs.getIP()+"/streamSong?id="+id);
        playPause = true;
    }

    public void playNextSong() {
        Intent player = new Intent(MediaPlayerActivity.this, MediaPlayerActivity.class);
        Song song;
        if (currentSongIndex==(currentSongList.size()-1)) {
            song = currentSongList.get(0);
        } else {
            song = currentSongList.get(currentSongIndex+1);
        }
        player.putExtra("id",song.getId());
        player.putExtra("author",song.getAuthor());
        player.putExtra("album",album);
        player.putExtra("albumId",albumId);
        player.putExtra("genre",song.getGenre());
        player.putExtra("songName",song.getSongName());
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            handler.removeCallbacks(runnable);
            mediaPlayer.release();
//            mediaPlayer.release();
        }
        finish();
        startActivity(player);
    }

    public void openAddingDialog() {
        AddToFavListDialog dialog = new AddToFavListDialog(id,token);
        dialog.show(getSupportFragmentManager(),"add_song_to_favlist_dialog");
    }

    public void isSongLiked() {
        String url = modSharedPrefs.getIP()+"/isSongLiked?idSong="+id+"&token="+token;

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equalsIgnoreCase("notliked")){
                            favSong.setImageResource(R.drawable.ic_action_notliked);
                        }
                        if (response.equalsIgnoreCase("liked")) {
                            favSong.setImageResource(R.drawable.ic_action_liked);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);
    }
}

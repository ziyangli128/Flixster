package com.example.flixter.models;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixter.R;
import com.example.flixter.databinding.ActivityMainBinding;
import com.example.flixter.databinding.ActivityMovieTrailerBinding;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Headers;

public class MovieTrailerActivity extends YouTubeBaseActivity {

    public static final String TAG = "MovieTrailerActicity";
    String key;
    private ActivityMovieTrailerBinding movieTrailerBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_movie_trailer);
        movieTrailerBinding = ActivityMovieTrailerBinding.inflate(getLayoutInflater());
        // layout of activity is stored in a special property called root
        View view = movieTrailerBinding.getRoot();
        setContentView(view);

        // test video id
        final String videoId = getIntent().getStringExtra("key");

        // resolve the player view from the layout
        //YouTubePlayerView playerView = (YouTubePlayerView) findViewById(R.id.player);
        Log.i(TAG, "onCreate: " + key);
        // initialize with API key
        movieTrailerBinding.player.initialize(getString(R.string.youtube_api_key),
                new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                YouTubePlayer youTubePlayer, boolean b) {
                // do any work here to cue video, play video, etc.
                youTubePlayer.loadVideo(videoId);
                Log.i(TAG, "onInitializationSuccess: " + videoId);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                YouTubeInitializationResult youTubeInitializationResult) {
                // log the error
                Log.e("MovieTrailerActivity", "Error initializing YouTube player");
            }
        });
    }
}
package com.example.flixter.models;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixter.R;
import com.example.flixter.databinding.ActivityMovieDetailsBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Headers;

public class MovieDetailsActivity extends AppCompatActivity {

    // the movie to display
    Movie movie;

    private ActivityMovieDetailsBinding movieDetailsBinding;

    public static final String TAG = "MovieTrailerActicity";
    String key;

    // the view objects
//    TextView tvTitle;
//    TextView tvOverview;
//    RatingBar rbVoteAverage;
//    ImageView ivVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_movie_details);
        movieDetailsBinding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        // layout of activity is stored in a special property called root
        View view = movieDetailsBinding.getRoot();
        setContentView(view);


        // resolve the view objects
//        tvTitle = (TextView) findViewById(R.id.tvTitle);
//        tvOverview = (TextView) findViewById(R.id.tvOverview);
//        rbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);
//        ivVideo = (ImageView) findViewById(R.id.ivVideo);

        // unwrap the movie passed in via intent, using its simple name as a key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity",
                String.format("Showing details for '%s'", movie.getTitle()));

        // set the title and overview
        movieDetailsBinding.tvTitle.setText(movie.getTitle());
        movieDetailsBinding.tvOverview.setText(movie.getOverview());
        movieDetailsBinding.tvReleaseDate.setText(movie.getReleaseDate());

        // vote average is 0..10, convert to 0..5 by dividing by 2
        float voteAverage = movie.getVoteAverage().floatValue();
        movieDetailsBinding.rbVoteAverage.setRating(voteAverage = voteAverage > 0 ? voteAverage / 2.0f: voteAverage);

        String imageUrl;
        int radius = 30; // corner radius, higher value = more rounded
        int margin = 10; // crop margin, set to 0 for corners with no crop
        imageUrl = movie.getBackdropPath();
        // load the Poster view with the accessed image, use a placeholder gif while loading
        Glide.with(this).load(imageUrl)
                .transform(new RoundedCornersTransformation(radius, margin))
                .placeholder(R.drawable.flicks_movie_placeholder)
                .into(movieDetailsBinding.ivVideo);

        String videosUrl = "https://api.themoviedb.org/3/movie/"
                + movie.getId().toString() +"/videos";
        // create an AsyncHttpClient to send request
        AsyncHttpClient client = new AsyncHttpClient();
        // params to add to the request URL
        RequestParams params = new RequestParams();
        params.put("api_key", "59bd053a7b1ff6be5b98584fb5924b9a");
        // send the request from client
        client.get(videosUrl, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                // called when response HTTP status is "200 OK"
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.i(TAG, "Results: " + results.toString());
                    key = results.getJSONObject(0).getString("key");
                    Log.i(TAG, key);

                    //Log.i(TAG, "Movies: " + movies.size());
                } catch (JSONException e) {
                    Log.e(TAG, "Hit json exception", e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d(TAG, "onFailure");
            }
        });

        // launches MovieTrailerActivity when the backdrop image is tapped
        movieDetailsBinding.ivVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (key != null) {
                    // create intent for the new activity
                    Intent i = new Intent(MovieDetailsActivity.this, MovieTrailerActivity.class);
                    // serialize the movie using parceler, use its short name as a key
                    i.putExtra("key", key);
                    // start the activity
                    MovieDetailsActivity.this.startActivity(i);
                }
            }
        });
    }
}
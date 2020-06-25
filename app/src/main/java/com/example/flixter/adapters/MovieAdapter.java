package com.example.flixter.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
//import android.os.Parcel;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flixter.R;
import com.example.flixter.models.Movie;
import com.example.flixter.models.MovieDetailsActivity;

import org.parceler.Parcels;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    Context context;
    List<Movie> movies;

    // MovieAdapter Constructor
    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    // involves inflating a layout from XML and returning the holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("MovieAdapter", "onCreateViewHolder");
        View movieView = LayoutInflater.from(context).inflate(
                R.layout.item_movie, parent, false);
        return new ViewHolder(movieView);
    }

    // involves populating data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("MovieAdapter", "onBindViewHolder: " + position);
        // get the movie at the passed in position
        Movie movie = movies.get(position);
        // bind the movie data into the ViewHolder
        holder.bind(movie);
    }

    // returns the total count of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    // subclass ViewHolder binding to a Movie item
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;

        // ViewHolder Constructor
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById((R.id.tvTitle));
            tvOverview = itemView.findViewById((R.id.tvOverview));
            ivPoster = itemView.findViewById((R.id.ivPoster));
            // add this as the itemView's OnClickListener
            itemView.setOnClickListener(this);
        }

        // bind a movie item into a ViewHolder
        public void bind(Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());

            // bind the poster image from API to ViewHolder
            String imageUrl;
            // if phone is in landscape
            if (context.getResources().getConfiguration().orientation
                    == Configuration.ORIENTATION_LANDSCAPE) {
                // then imageUrl = back drop image
                imageUrl = movie.getBackdropPath();
                // load the Poster view with the accessed image, use a placeholder gif while loading
                Glide.with(context).load(imageUrl)
                        .placeholder(R.drawable.flicks_backdrop_placeholder).into(ivPoster);
            } else {
                // else imageUrl = poster image
                imageUrl = movie.getPosterPath();
                // load the Poster view with the accessed image, use a placeholder gif while loading
                Glide.with(context).load(imageUrl)
                        .placeholder(R.drawable.flicks_movie_placeholder).into(ivPoster);
            }

        }

        // when the user clicks on a row, show MovieDetailsActivity for the selected movie
        @Override
        public void onClick(View v) {
            // gets item position
            int position = getAdapterPosition();
            // make sure the position is valid, actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the movie at the position
                Movie movie = movies.get(position);
                // create intent for the new activity
                Intent i = new Intent(context, MovieDetailsActivity.class);
                // serialize the movie using parceler, use its short name as a key
                i.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                // start the activity
                context.startActivity(i);
            }
        }
    }
}

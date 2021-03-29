package com.noelrmrz.popularmovies.movie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.noelrmrz.popularmovies.R;
import com.noelrmrz.popularmovies.utilities.PicassoClient;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private MovieList mMovieList;

    private final MovieAdapterOnClickHandler mClickHandler;

    @NonNull
    @Override
    public MovieAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapterViewHolder movieAdapterViewHolder, int position) {
        String imageUrl = mMovieList.getMovieList().get(position).getMPosterPath();
        PicassoClient.downloadImage(imageUrl, movieAdapterViewHolder.mImageView);
    }

    @Override
    public int getItemCount() {
        if (null == mMovieList) {
            return 0;
        }
        else {
            return mMovieList.getMovieList().size();
        }
    }

    public interface MovieAdapterOnClickHandler {
        void onClick(Movie movie);
    }

    public MovieAdapter(MovieAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView mImageView;

        public MovieAdapterViewHolder(View view) {
            super(view);
            mImageView = view.findViewById(R.id.iv_movie);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Movie movie = mMovieList.getMovieList().get(adapterPosition);
            mClickHandler.onClick(movie);
        }
    }
    /**
  d   *
     * @param movieList The new movie data to be shown
     */
    public void setMovieList(MovieList movieList) {
        mMovieList = movieList;
        notifyDataSetChanged();
    }
}

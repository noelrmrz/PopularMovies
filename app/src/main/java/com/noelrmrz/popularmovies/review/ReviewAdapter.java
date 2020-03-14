package com.noelrmrz.popularmovies.review;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.noelrmrz.popularmovies.R;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {

    private ReviewList mReviewList;
    private final ReviewAdapterOnClickHandler mClickHandler;

    @NonNull
    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.review_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapterViewHolder reviewAdapterViewHolder, int position) {
        Review review = mReviewList.getmReviewList().get(position);
        reviewAdapterViewHolder.mUsername.setText(review.getmAuthor());
        reviewAdapterViewHolder.mContent.setText(review.getmContent());
    }

    @Override
    public int getItemCount() {
        if (null == mReviewList) {
            return 0;
        }
        else {
            return mReviewList.getmReviewList().size();
        }
    }

    public interface ReviewAdapterOnClickHandler {
        void onClick(Review review);
    }

    public ReviewAdapter(ReviewAdapter.ReviewAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mUsername;
        public TextView mContent;

        public ReviewAdapterViewHolder(View view) {
            super(view);
            mUsername = view.findViewById(R.id.tv_review_name);
            mContent = view.findViewById(R.id.tv_review_content);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
        }
    }

    /**
     *
     * @param reviewList The new review data to be shown
     */
    public void setmReviewList(ReviewList reviewList) {
        mReviewList = reviewList;
        notifyDataSetChanged();
    }
}

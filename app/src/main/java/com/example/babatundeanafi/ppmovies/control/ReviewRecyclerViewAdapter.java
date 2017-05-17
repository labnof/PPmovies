package com.example.babatundeanafi.ppmovies.control;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.babatundeanafi.ppmovies.R;
import com.example.babatundeanafi.ppmovies.model.Review;


import java.util.List;


public class ReviewRecyclerViewAdapter extends RecyclerView.Adapter<ReviewRecyclerViewAdapter.MyViewHolder> {

    private List<Review> reviewList;
    private Context context;





    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView author;
        private TextView content;


        public MyViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            author = (TextView) view.findViewById(R.id.reviewer);
            content = (TextView) view.findViewById(R.id.review_content);

        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            Review review = reviewList.get(position);
            String url = review.getUrl();//returns review URL
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            context = v.getContext();
            context.startActivity(i);

        }
    }


    public ReviewRecyclerViewAdapter (List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_item, parent, false);





        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Review review = reviewList.get(position);
        holder.author.setText(String.format("%s: ", review.getAuthor()));
        holder.content.setText(review.getContent());


    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

}
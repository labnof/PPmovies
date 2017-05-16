package com.example.babatundeanafi.ppmovies.control;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.babatundeanafi.ppmovies.R;
import com.example.babatundeanafi.ppmovies.model.Video;

import java.util.List;

public class VideoRecyclerViewAdapter extends RecyclerView.Adapter<VideoRecyclerViewAdapter .MyViewHolder> {

    private List<Video> moviesList;
    private Context context;





    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title;


        public MyViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            title = (TextView) view.findViewById(R.id.txtTrailerItem);

        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            Video video = moviesList.get(position);
            String url = NetworkUtils.TRAILERS_PREFIX + video.getKey();//returns the Trailer URL
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            context = v.getContext();
            context.startActivity(i);

        }
    }


    public VideoRecyclerViewAdapter (List<Video> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_item, parent, false);





        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.title.setText(String.format("Trailer %d", position + 1));


    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

}
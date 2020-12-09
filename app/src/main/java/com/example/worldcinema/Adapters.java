package com.example.worldcinema;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.zip.Inflater;

public class Adapters {
}

class FilmsRecyclerAdapter extends RecyclerView.Adapter<FilmsRecyclerAdapter.ViewHolder> {

    List<Films> filmsList;
    LayoutInflater inflater;

    public FilmsRecyclerAdapter(Context context, List<Films> films){
        this.filmsList = films;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_film_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Films film = filmsList.get(position);
        Picasso.get().load(film.getPhoto()).into(holder.image);
        holder.image.setTag(film.getTag());

    }

    @Override
    public int getItemCount() {
        return filmsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imFilm);
        }
    }
}

class GallaryAdapter extends RecyclerView.Adapter<GallaryAdapter.ViewHolder>{

    List<GallaryFrame> frameList;
    LayoutInflater inflater;

    public GallaryAdapter(List<GallaryFrame> frameList, Context context){
        this.frameList = frameList;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.gallary_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GallaryFrame frame = frameList.get(position);
        Picasso.get().load(frame.getPhoto()).into(holder.imFrame);
    }

    @Override
    public int getItemCount() {
        return frameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imFrame;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imFrame = itemView.findViewById(R.id.imFrame);
        }
    }
}

class EpisodesAdapter extends RecyclerView.Adapter<EpisodesAdapter.ViewHolder>{

    List<Episode> episodes;
    LayoutInflater inflater;

    public EpisodesAdapter(List<Episode> episodes, Context context){
        this.episodes = episodes;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.episode_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Episode episode = episodes.get(position);
        holder.tvDescription.setText(episode.getDescription());
        holder.tvName.setText(episode.getName());
        holder.tvYear.setText(episode.getYear());
        if (!episode.getPreview().equals("")){
            holder.videoView.setVideoURI(Uri.parse(episode.getPreview()));
            holder.videoView.seekTo(100);
        }
        else {
            holder.videoView.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return episodes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        VideoView videoView;
        TextView tvName;
        TextView tvDescription;
        TextView tvYear;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.videoEpisode);
            tvName = itemView.findViewById(R.id.tvEpisodeName);
            tvDescription = itemView.findViewById(R.id.tvEpisodeDescription);
            tvYear = itemView.findViewById(R.id.tvEpisodeYear);
        }
    }
}


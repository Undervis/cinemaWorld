package com.example.worldcinema;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MovieScreen extends AppCompatActivity {
    int movieId;
    List<GallaryFrame> gallaryFrameList = new ArrayList<>();
    List<Episode> episodes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_screen);

        Intent intent = getIntent();

        movieId = Integer.parseInt(intent.getStringExtra("tag"));
        getMovie(movieId);
        getMovieEpisode(2);
    }

    private void getMovie(int movieId){
        String url = "http://cinema.areas.su/movies/" + movieId;
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onResponse(JSONObject response) {
                TextView tvMovieName = findViewById(R.id.tvMovieName);
                TextView tvYears = findViewById(R.id.tvYears);
                ImageView imPoster = findViewById(R.id.imPoster);
                TextView tvDescription = findViewById(R.id.tvDescription);
                try {
                    tvDescription.setText(response.getString("description"));
                    tvMovieName.setText(response.getString("name"));
                    tvYears.setText(response.getString("age") + "+");
                    switch (Integer.parseInt(response.getString("age"))){
                        case 18: tvYears.setTextColor(getColor(R.color.secondary_color)); break;
                        case 12: tvYears.setTextColor(getColor(R.color.years_12)); break;
                        case 16: tvYears.setTextColor(getColor(R.color.years_16)); break;
                        case 6: tvYears.setTextColor(getColor(R.color.years_6)); break;
                        case 0: tvYears.setTextColor(getColor(R.color.white)); break;
                    }
                    Picasso.get().load("http://cinema.areas.su/up/images/" + response.getString("poster")).into(imPoster);

                    RecyclerView recyclerGallery = findViewById(R.id.recyclerGallary);
                    GallaryAdapter adapter = new GallaryAdapter(gallaryFrameList, getApplicationContext());
                    adapter.notifyDataSetChanged();
                    recyclerGallery.setAdapter(adapter);
                    recyclerGallery.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                    JSONArray imageArray = response.getJSONArray("images");
                    for (int i = 0; i < imageArray.length(); i++){
                        gallaryFrameList.add(new GallaryFrame("http://cinema.areas.su/up/images/" + imageArray.getString(i)));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
    }

    private void getMovieEpisode(int movieId){
        String url = "http://cinema.areas.su/movies/" + movieId + "/episodes";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject object = response.getJSONObject(0);
                    String videoName = object.getString("preview");
                    VideoView videoView = findViewById(R.id.videoView);
                    videoView.setVideoURI(Uri.parse("http://cinema.areas.su/up/video/" + videoName));
                    //videoView.setMediaController(new MediaController(MovieScreen.this));
                    videoView.requestFocus(0);
                    videoView.seekTo(100);
                    ImageView imageView = findViewById(R.id.btPlay);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(videoView.isPlaying()){
                                videoView.pause();
                            } else {
                                videoView.start();
                            }
                        }
                    });

                    RecyclerView recyclerEpisode = findViewById(R.id.recyclerEpisodes);
                    EpisodesAdapter adapter = new EpisodesAdapter(episodes, getApplicationContext());
                    adapter.notifyDataSetChanged();
                    recyclerEpisode.setAdapter(adapter);
                    recyclerEpisode.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                    for(int i = 0; i < response.length(); i++){
                        JSONObject objectEp = response.getJSONObject(i);
                        episodes.add(new Episode(
                                objectEp.getString("name"),
                                objectEp.getString("description"),
                                "http://cinema.areas.su/up/video/" + objectEp.getString("preview"),
                                objectEp.getString("year")
                        ));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        queue.add(request);
    }
}
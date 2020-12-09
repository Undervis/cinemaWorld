package com.example.worldcinema;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String token;
    private List<Films> filmsList = new ArrayList<>();

    RecyclerView recyclerView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        token = intent.getStringExtra("token");

        recyclerView = findViewById(R.id.recyclerTrands);

        NestedScrollView scrollView = findViewById(R.id.scrollTrands);
        scrollView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MovieScreen.class).putExtra("tag", "3"));
            }
        });


        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(openFileOutput("token", MODE_PRIVATE)));
            writer.write(token);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        TextView tvBanner = findViewById(R.id.btWatch);
        tvBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MovieScreen.class).putExtra("tag", tvBanner.getTag().toString()));
            }
        });

        getBannerCover();
        filmsList.clear();
        getMovies(0);

        TabLayout tabLayout = findViewById(R.id.tabCatalog);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0: getMovies(0); break;
                    case 1: getMovies(1); break;
                    case 2: getMovies(2); break;
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    private void getBannerCover(){
        Uri uri = Uri.parse("http://cinema.areas.su/movies/cover");
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, uri.toString(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                TextView btWatch = findViewById(R.id.btWatch);
                ImageView imBanner = findViewById(R.id.imBanner);
                ImageView imForeground = findViewById(R.id.imForeground);
                try {
                    btWatch.setTag(response.getString("movieId"));
                    Picasso.get().load("http://cinema.areas.su/up/images/" + response.getString("foregroundImage")).into(imForeground);
                    Picasso.get().load("http://cinema.areas.su/up/images/" + response.getString("backgroundImage")).into(imBanner);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                SignInScreen.showDialog(error.getMessage(), "Error", MainActivity.this);
            }
        });
        queue.add(request);
    }

    private void getMovies(int index){
        Uri uri = null;
        switch (index){
            case 0: uri = Uri.parse("http://cinema.areas.su/movies").buildUpon().appendQueryParameter("filter", "inTrend").build(); break;
            case 1: uri = Uri.parse("http://cinema.areas.su/movies").buildUpon().appendQueryParameter("filter", "new").build(); break;
            case 2: uri = Uri.parse("http://cinema.areas.su/movies").buildUpon().appendQueryParameter("filter", "forMe").build(); break;
        }

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, uri.toString(), null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {


                filmsList.clear();
                FilmsRecyclerAdapter adapter = new FilmsRecyclerAdapter(getApplicationContext(), filmsList);
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

                try {
                    for(int i = 0; i <= 9; i++){
                        JSONObject object = response.getJSONObject(i);
                        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                        filmsList.add(new Films( "http://cinema.areas.su/up/images/" + object.getString("poster"), object.getString("name"), object.getString("movieId")));
                    }
                } catch (JSONException e) {
                    SignInScreen.showDialog(e.getMessage(), "error", MainActivity.this);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                SignInScreen.showDialog(error.getMessage(), "error", MainActivity.this);
            }
        });

        queue.add(request);
    }

    private void getLastView(){
        Uri uri = Uri.parse("http://cinema.areas.su/usermovies").buildUpon().appendQueryParameter("filter", "lastView").appendQueryParameter("token", token).build();
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, uri.toString(), null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject object = response.getJSONObject(0);
                    SignInScreen.showDialog("token", object.getString("name"), MainActivity.this);

                } catch (JSONException e) {
                    SignInScreen.showDialog("token", e.getMessage() + "stack", MainActivity.this);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                SignInScreen.showDialog(token, error.getMessage() + "Volley", MainActivity.this);
            }
        });
        queue.add(request);
    }

    private void getUserId(){
        Uri uri = Uri.parse("http://cinema.areas.su/user").buildUpon().build();

    }
}
package com.example.worldcinema;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class SignInScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_screen);

        TextView btSignIn = findViewById(R.id.btSignIn);
        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edEmail = findViewById(R.id.edSignIn);
                EditText edPassword = findViewById(R.id.edSignUp);

                if(edEmail.getText().toString().isEmpty() || edPassword.getText().toString().isEmpty()){
                    showDialog("Один или все из полей ввода пусты", "Ошибка", SignInScreen.this);
                }
                else {
                    sendSignIn(edEmail, edPassword);
                }
            }
        });
    }

    private void sendSignIn(EditText email, EditText password){
        Uri uri = Uri.parse("http://cinema.areas.su/auth/login").buildUpon()
                .appendQueryParameter("email", email.getText().toString())
                .appendQueryParameter("password", password.getText().toString()).build();
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, uri.toString(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                    try {
                        String token = response.get("token").toString();
                        if (!response.get("token").equals("")){
                            startActivity(new Intent(SignInScreen.this, MainActivity.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    .putExtra("token", token));
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showDialog("Неверный логин или пароль", "Ошибка авторизации", SignInScreen.this);
            }
        });
        queue.add(request);
    }

    public static void showDialog(String message, String title, Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setTitle(title).setPositiveButton("Ок", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
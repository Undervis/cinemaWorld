package com.example.worldcinema;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.JsonReader;
import android.view.View;
import android.view.ViewStructure;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.w3c.dom.Document;

import java.util.Objects;

public class SignUpScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen);

        TextView btSignIn = findViewById(R.id.btSignUpApply);
        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText edEmail = findViewById(R.id.edSignUpEmail);
                EditText edFirstName = findViewById(R.id.edSignName);
                EditText edLastName = findViewById(R.id.edSignLastName);
                EditText edPassword = findViewById(R.id.edSignUpPassword);
                EditText edPasswordConfirm = findViewById(R.id.edSignUpPasswordConfirm);

                if(edEmail.getText().toString().isEmpty()
                        || edPassword.getText().toString().isEmpty()
                        || edFirstName.getText().toString().isEmpty()
                        || edLastName.getText().toString().isEmpty()
                        || edPassword.getText().toString().isEmpty()){
                    SignInScreen.showDialog("Один или все из полей ввода пусты", "Ошибка", SignUpScreen.this);
                }
                /*if (!edPassword.getText().toString().equals(edPasswordConfirm.getText().toString())) {
                    SignInScreen.showDialog("Пароли не совпадают", "Ошибка", SignUpScreen.this);

                }*/
                else {
                    sendSignUp(edEmail.getText().toString(), edPassword.getText().toString(), edFirstName.getText().toString(), edLastName.getText().toString());
                }
            }
        });

        TextView btSignUp = findViewById(R.id.btSignInApply);
        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpScreen.this, SignInScreen.class));
            }
        });
    }

    private void sendSignUp(String email, String password, String firstName, String lastName){
        Uri uri = Uri.parse("http://cinema.areas.su/auth/register").buildUpon()
                .appendQueryParameter("email", email)
                .appendQueryParameter("password", password)
                .appendQueryParameter("firstName", firstName)
                .appendQueryParameter("lastName", lastName).build();

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, uri.toString(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                /*try {
                    String token = response.get("token").toString();
                    if (!response.get("token").equals("")){
                        startActivity(new Intent(SignInScreen.this, MainActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                .putExtra("token", token));
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(Objects.requireNonNull(error.getMessage()).contains("Успешная")){
                    Toast.makeText(getApplicationContext(), "Успешная регистрация", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("email", email);
                    intent.putExtra("password", password);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else {
                    SignInScreen.showDialog("Ошибка регистрации", "Ошибка", SignUpScreen.this);
                }
            }
        });
        queue.add(request);
    }
}
package org.tensorflow.lite.examples.classification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toolbar;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
public class Login extends AppCompatActivity{
    private String url = "http://192.168.43.251:5000";//*Put your  URL here***
    private String POST = "POST";
    private String PUT = "PUT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.login);
        Button btnlogin,btnregister;
        TextView username, password,error;
        CheckBox remember;
        username = (TextView) findViewById(R.id.inemail);
        password = (TextView) findViewById(R.id.inpswd);
        btnlogin = (Button) findViewById(R.id.btnLogin);
        btnregister = (Button) findViewById(R.id.btntoRegister);
        error = (TextView) findViewById(R.id.logineror);
        btnlogin.setOnClickListener(view -> {
            String txtusername = username.getText().toString();
            String txtpassword = password.getText().toString();
            if(txtusername.isEmpty()&&txtpassword.isEmpty()){
                error.setError("");
                error.setText("email & password kosong");
            }
            else if(txtusername.isEmpty()){
                error.setError("");
                error.setText("email kosong");
            }
            else if(txtpassword.isEmpty()){
                error.setError("");
                error.setText("password kosong");
            }
            else {
                //if name text is not empty,then call the function to make the post request/
                sendRequest(PUT, "login", "username", "password",txtusername,txtpassword);

            }
        });
        btnregister.setOnClickListener(view -> {
            Login.this.startActivity(new Intent((Context)Login.this, Register.class));
            Login.this.finish();
        });
    }



    void sendRequest(String type,String method,String paramname,String paramname2,String value1,String value2){

        TextView username;
        username = (TextView) findViewById(R.id.inemail);
        String txtusername = username.getText().toString();
        TextView respon;
        respon = (TextView) findViewById(R.id.responlogin);
        /* if url is of our get request, it should not have parameters according to our implementation.
         * But our post request should have 'name' parameter. */
        String fullURL=url+"/"+method;
        Request request;

        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS).build();

        /* If it is a post request, then we have to pass the parameters inside the request body*/
        if(type.equals(PUT)){
            RequestBody formBody = new FormBody.Builder()
                    .add(paramname, value1)
                    .add(paramname2,value2)
                    .build();
            request=new Request.Builder()
                    .url(fullURL)
                    .put(formBody)
                    .build();
        }else{
            // If it's our get request, it doen't require parameters, hence just sending with the url/
            request=new Request.Builder()
                    .url(fullURL)
                    .build();
        }

        /* this is how the callback get handled */
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                Login.this.runOnUiThread(() -> respon.setText("api tidak merespon"));
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                // Read data on the worker thread
                final String responseData = response.body().string();
                // Run view-related code back on the main thread.
                // Here we display the response message in our text view
                Login.this.runOnUiThread(() -> respon.setText(responseData));
                if (responseData.equals("halo(('" + txtusername + "',),)")){
                    Login.this.runOnUiThread(() -> respon.setText("login benar"));
                    Intent menu = new Intent((Context) Login.this, Menu.class);
                    menu.putExtra("Extra_name", txtusername);
                    Login.this.startActivity(menu);
                    Login.this.finish();
                }else{
                    Login.this.runOnUiThread(() -> respon.setText("login salah"));
                }
            }
        });
    }
}
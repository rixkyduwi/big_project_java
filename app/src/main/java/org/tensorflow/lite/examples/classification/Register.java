package org.tensorflow.lite.examples.classification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Register extends AppCompatActivity {
    private String url = "http://192.168.43.251:5000";//*Put your  URL here***
    private String POST = "POST";
    private String PUT = "PUT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.register);
        TextView nama_lengkap, email, password, alamat, kontak, error;
        Button register,back;
        register = (Button) findViewById(R.id.btnRegister);
        back = (Button) findViewById(R.id.back);
        nama_lengkap = (TextView) findViewById(R.id.regname);
        email = (TextView) findViewById(R.id.regemail);
        password = (TextView) findViewById(R.id.regpswd);
        alamat = (TextView) findViewById(R.id.regnorumah);
        kontak = (TextView) findViewById(R.id.regkontak);
        error = (TextView) findViewById(R.id.regeror);
        register.setOnClickListener(view -> {
            String txtnama_lengkap = nama_lengkap.getText().toString();
            String txtemail = email.getText().toString();
            String txtpassword = password.getText().toString();
            String txtalamat = alamat.getText().toString();
            String txtkontak = kontak.getText().toString();
            if (txtnama_lengkap.isEmpty() && txtemail.isEmpty() && txtpassword.isEmpty() && txtalamat.isEmpty() && txtkontak.isEmpty()) {
                error.setError("");
                error.setText("harap isi semua");
            }
            else if (txtnama_lengkap.isEmpty() ) {
                error.setError("");
                error.setText("harap isi semua");
            }
            else if (txtemail.isEmpty()) {
                error.setError("");
                error.setText("harap isi semua");
            }
            else {
                //if name text is not empty,then call the function to make the post request/
                sendRequest(PUT, "reguser", "nama", "email", "password", "no_rumah", "kontak", txtnama_lengkap, txtemail, txtpassword, txtalamat, txtkontak);

            }
        });

        back.setOnClickListener(view -> {
            Register.this.startActivity(new Intent((Context)Register.this, Login.class));
            Register.this.finish();
        });

    }

    void sendRequest(String type,String method,String nama,String email,String password, String no_rumah,String kontak,String value1,String value2, String value3, String value4, String value5){
        TextView username;
        username = (TextView) findViewById(R.id.regname);
        String txtusername = username.getText().toString();
        TextView respon;
        respon = (TextView) findViewById(R.id.responregister);
        /* if url is of our get request, it should not have parameters according to our implementation.
         * But our post request should have 'name' parameter. */
        String fullURL=url+"/"+method+(value3==null?"":"/"+value3);
        Request request;

        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS).build();

        /* If it is a post request, then we have to pass the parameters inside the request body*/
        if(type.equals(PUT)){
            RequestBody formBody = new FormBody.Builder()
                    .add(nama, value1)
                    .add(email,value2)
                    .add(password,value3)
                    .add(no_rumah,value4)
                    .add(kontak,value5)
                    .build();
            request=new Request.Builder()
                    .url(fullURL)
                    .post(formBody)
                    .build();
        }else{
            //If it's our get request, it doen't require parameters, hence just sending with the url/
            request=new Request.Builder()
                    .url(fullURL)
                    .build();
        }

        /* this is how the callback get handled */
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                Register.this.runOnUiThread(() -> respon.setText("api tidak merespon"));
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                // Read data on the worker thread
                final String responseData = response.body().string();
                // Run view-related code back on the main thread.
                // Here we display the response message in our text view
                Register.this.runOnUiThread(() -> respon.setText(responseData));
                if (responseData.equals("halo" + txtusername)){
                    Register.this.runOnUiThread(() -> respon.setText("Register benar"));
                    Register.this.startActivity(new Intent((Context)Register.this, Menu.class));
                    Register.this.finish();
                }else{
                    Register.this.runOnUiThread(() -> respon.setText(responseData));
                }
            }
        });
    }
}
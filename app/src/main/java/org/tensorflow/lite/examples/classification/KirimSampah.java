package org.tensorflow.lite.examples.classification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.CheckBox;
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

public class KirimSampah extends AppCompatActivity {
    private String url = "http://192.168.43.251:5000";//*Put your  URL here***
    private String POST = "POST";
    private String PUT = "PUT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.kirimsampah);
        Button btnkirim;
        TextView username, berat,jenis,error;
        CheckBox remember;
        username = (TextView) findViewById(R.id.user);
        String etuser = getIntent().getStringExtra("Extra_user");
        username.setText("username = "+ etuser);
        jenis = (TextView) findViewById(R.id.jenissampah);
        String etjenis = getIntent().getStringExtra("Extra_jenis");
        jenis.setText("Jenis Sampah = "+ etjenis);
        berat =(TextView)findViewById(R.id.berat);
        if (etjenis.equals("organik")) {
            berat.setText("200");
            String txtberat = berat.getText().toString();
            int intberat = Integer.parseInt(txtberat);
            sendRequest(POST, "atributuser", "jenis", etjenis, "user", etuser, "berat(KG)", txtberat);
        }
        else if (etjenis.equals("anorganik")) {
            berat.setText("700");
            String txtberat = berat.getText().toString();
            int intberat = Integer.parseInt(txtberat);
            sendRequest(POST, "atributuser", "jenis", etjenis, "user", etuser, "berat(KG)", txtberat);
        }
        else if (etjenis.equals("b3")) {
            berat.setText("100");
            String txtberat = berat.getText().toString();
            int intberat = Integer.parseInt(txtberat);
            sendRequest(POST, "atributuser", "jenis", etjenis, "user", etuser, "berat(KG)", txtberat);
        }

    }
    void sendRequest(String type, String method, String paramname, String param,String paramname2,String param2,String paramname3,String param3){

        /* if url is of our get request, it should not have parameters according to our implementation.
         * But our post request should have 'name' parameter. */
        String fullURL=url+"/"+method+(param==null?"":"/"+param);
        Request request;
        TextView username,respon;
        respon = (TextView) findViewById(R.id.respon);
        username = (TextView) findViewById(R.id.user);
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS).build();

        /* If it is a post request, then we have to pass the parameters inside the request body*/
        if(type.equals(POST)){
            RequestBody formBody = new FormBody.Builder()
                    .add(paramname, param)
                    .add(paramname2, param2)
                    .add(paramname3, param3)
                    .build();
            request=new Request.Builder()
                    .url(fullURL)
                    .post(formBody)
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
                 KirimSampah.this.runOnUiThread(() -> respon.setText("api tidak merespon"));
             }
             @Override
             public void onResponse(Call call, final Response response) throws IOException {

                                                // Read data on the worker thread
                 final String responseData = response.body().string();
                                                // Run view-related code back on the main thread.
                                                // Here we display the response message in our text view

                 String txtusername = username.getText().toString();
                 if (responseData.equals("Hasil Scan Telah Disimpan")){
                     (new Handler(Looper.getMainLooper())).postDelayed((Runnable)(new Runnable() {
                         public final void run() {
                             Intent menu = new Intent((Context) KirimSampah.this, Menu.class);
                             menu.putExtra("Extra_name", txtusername);
                             KirimSampah.this.startActivity(menu);
                             KirimSampah.this.finish();
                         }
                     }), 3000L);

                 }else{
                     KirimSampah.this.runOnUiThread(() -> respon.setText("Data gagal terkirim"));
                 }
             }
        }
        );
    }
}

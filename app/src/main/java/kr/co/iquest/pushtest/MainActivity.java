package kr.co.iquest.pushtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import kr.co.iquest.pushtest.gps.GpsTracker;
import kr.co.iquest.pushtest.ip.IpChecker;
import kr.co.iquest.pushtest.scraping.ScrapActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    Button btnMap;
    Button btnScrap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fireBaseMessagingListener();
       //  Toast.makeText(getApplicationContext(), value,Toast.LENGTH_LONG ).show();
        getGpsInfo();

        //
        getPublicIpFromAWS();

        btnMap = findViewById(R.id.btnMap);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myintent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(myintent);
                finish();
            }
        });

        btnScrap = findViewById(R.id.btnScrap);
        btnScrap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myintent1 = new Intent(MainActivity.this, ScrapActivity.class);
                startActivity(myintent1);
                //finish();
            }
        });

        settingMagicVKeypad();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void settingMagicVKeypad() {


    }

    private void getPublicIpFromAWS() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://checkip.amazonaws.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        APIService api = retrofit.create(APIService.class);
        Call<String> call = api.getPublicIp();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String result = response.body().toString();

                    Toast.makeText(getApplicationContext(), "publicIp : " +  result, Toast.LENGTH_LONG ).show();
                } else {
                    String result = "실패!";

                    Log.e("retrofit", "response 실패~!");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("retrofit", "onnFailure!");
                Log.e("retrofit", t.getMessage());



            }
        });


    }



    private void getGpsInfo() {

        GpsTracker gpsTracker = new GpsTracker(MainActivity.this);
        double latitude = gpsTracker.getLatitude(); // 위도
        double longitude = gpsTracker.getLongitude();   //경도
        //필요시  String address = getCurrentAddress(latitude, longitude); 대한민국 서울시 종로구 ~~
        Log.d(TAG, "latitude(위도) : " + String.valueOf(latitude));
        Log.d(TAG, "longitude(경도) : " +  String.valueOf(longitude));

        Toast.makeText(getApplicationContext()
                , "latitude(위도) : " + String.valueOf(latitude) + "longitude(경도) : " +  String.valueOf(longitude)
                ,Toast.LENGTH_LONG ).show();

    }


    private void fireBaseMessagingListener() {
        //
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        String msg = "token:" + token;
                        Log.d(TAG, msg);
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
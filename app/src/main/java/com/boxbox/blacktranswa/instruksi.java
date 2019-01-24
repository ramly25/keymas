package com.boxbox.blacktranswa;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class instruksi extends AppCompatActivity {
    private InterstitialAd mInterstitialAd;
    private boolean isRunning;
    private boolean negara = true;
    ScheduledExecutorService scheduler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instruksi);

        final SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);

        View adContainer = findViewById(R.id.adMobView);

        AdView mAdView = new AdView(this);
        mAdView.setAdSize(AdSize.BANNER);
        mAdView.setAdUnitId(pref.getString("banner",null));
        ((RelativeLayout)adContainer).addView(mAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);




        int mul=Integer.parseInt(pref.getString("mul","5"));
        int peri=Integer.parseInt(pref.getString("period","20"));



        scheduler = Executors.newSingleThreadScheduledExecutor();

        if(pref.getString("negara",null).equals("0")) {
            negara=false;
        }else if(pref.getString("negara", null).equals("1")) {
            prepareAd(pref.getString("inter", null));
            scheduler.scheduleAtFixedRate(new Runnable() {
                public void run() {

                    Log.i("hello", "world");
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (isRunning) {
                                if (mInterstitialAd.isLoaded()) {
                                    mInterstitialAd.show();
                                } else {
                                    Log.d("TAG", " Interstitial not loaded");
                                }
                                prepareAd(pref.getString("inter", null));
                            }

                        }
                    });

                }
            }, mul, peri, TimeUnit.SECONDS);
        }

    }

    public void prepareAd(String inter) {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(inter);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRunning = false;
        scheduler.shutdown();

    }





    @Override
    protected void onStart() {
        super.onStart();
        isRunning = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isRunning = false;
    }
    @Override
    public void onBackPressed() {
        if(negara==true) {

        }
        super.onBackPressed();
    }


}

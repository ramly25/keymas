package com.boxbox.blacktranswa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.AdSize;


import com.boxbox.blacktranswa.android.ImePreferences;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;





public class MainActivity extends AppCompatActivity
        implements View.OnClickListener {


    private InterstitialAd mInterstitialAd;
    private boolean isRunning;
    private boolean negara = true;
    ScheduledExecutorService scheduler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);



        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, "ca-app-pub-6820575160965343~2413621213");

        View adContainer = findViewById(R.id.adMobView);

        AdView mAdView = new AdView(this);
        mAdView.setAdSize(AdSize.BANNER);
        mAdView.setAdUnitId(pref.getString("banner",null));
        ((RelativeLayout)adContainer).addView(mAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



        /** Add banner programmatically (within Java code, instead of within the layout xml) **/
        RelativeLayout mainLayout = findViewById(R.id.relativeLayout);





        LinearLayout enableSetting = findViewById(R.id.layout_EnableSetting);
        LinearLayout addKeyboards = findViewById(R.id.layout_AddLanguages);
        LinearLayout chooseInputMethod = findViewById(R.id.layout_ChooseInput);
        LinearLayout chooseTheme = findViewById(R.id.layout_ChooseTheme);
        LinearLayout manageDictionaries = findViewById(R.id.layout_ManageDictionary);
        LinearLayout about = findViewById(R.id.layout_about);

        enableSetting.setOnClickListener(this);
        addKeyboards.setOnClickListener(this);
        chooseInputMethod.setOnClickListener(this);
        chooseTheme.setOnClickListener(this);
        manageDictionaries.setOnClickListener(this);
        about.setOnClickListener(this);


        int mul=Integer.parseInt(pref.getString("mul","5"));
        int peri=Integer.parseInt(pref.getString("period","20"));


        scheduler = Executors.newSingleThreadScheduledExecutor();

        if(pref.getString("negara",null).equals("0")) {

            negara = false;
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


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_EnableSetting:
                startActivityForResult(
                        new Intent(android.provider.Settings.ACTION_INPUT_METHOD_SETTINGS), 0);
                if(negara==true){

                }
                break;
            case R.id.layout_AddLanguages:
                //lunchPreferenceActivity();
                startActivity(new Intent(MainActivity.this, instruksi.class));
                if(negara==true){

                }

                break;
            case R.id.layout_ChooseInput:
                if (isInputEnabled()) {
                    ((InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .showInputMethodPicker();
                    if(negara==true){

                    }
                } else {
                    Toast.makeText(this, "Please enable keyboard first.", Toast.LENGTH_SHORT).show();
                    if(negara==true){

                    }
                }
                break;
            case R.id.layout_ChooseTheme:
                startActivity(new Intent(this, ThemeActivity.class));
                if(negara==true){

                }
                break;
            case R.id.layout_ManageDictionary:
                startActivity(new Intent(this, DictionaryActivity.class));
                if(negara==true){
                }
                break;
            case R.id.layout_about:
                startActivity(new Intent(this, AboutActivity.class));
                if(negara==true){

                }
                break;
            default:
                break;
        }
    }


    public boolean isInputEnabled() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        List<InputMethodInfo> mInputMethodProperties = imm.getEnabledInputMethodList();

        final int N = mInputMethodProperties.size();
        boolean isInputEnabled = false;

        for (int i = 0; i < N; i++) {

            InputMethodInfo imi = mInputMethodProperties.get(i);
            Log.d("INPUT ID", String.valueOf(imi.getId()));
            if (imi.getId().contains(getPackageName())) {
                isInputEnabled = true;
            }
        }

        if (isInputEnabled) {
            return true;
        } else {
            return false;
        }
    }

    public void lunchPreferenceActivity() {
        if (isInputEnabled()) {
            Intent intent = new Intent(this, ImePreferences.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Please enable keyboard first.", Toast.LENGTH_SHORT).show();
        }
    }
}
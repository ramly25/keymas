package com.boxbox.blacktranswa;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.androidfung.geoip.IpApiService;
import com.androidfung.geoip.ServicesManager;
import com.androidfung.geoip.model.GeoIpResponseModel;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;

public class mulai extends AppCompatActivity {
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    ArrayList<HashMap<String, String>> list_data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mulai);
        final TextView tulis = (TextView) findViewById(R.id.textView);

        final SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        final SharedPreferences.Editor editor = pref.edit();


        String url = "https://undangankekinian.com/iklan.php";
        requestQueue = Volley.newRequestQueue(mulai.this);
        list_data = new ArrayList<HashMap<String, String>>();

        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    editor.putString("banner",jsonObject.get("banner").toString());
                    editor.putString("inter",jsonObject.get("inter").toString());
                    editor.putString("mul",jsonObject.get("mul").toString());
                    editor.putString("period",jsonObject.get("period").toString());
                    //editor.commit();
                    //tulis.setText(pref.getString("banner", null));

                    IpApiService ipApiService = ServicesManager.getGeoIpService();
                    ipApiService.getGeoIp().enqueue(new Callback<GeoIpResponseModel>() {
                        @Override
                        public void onResponse(Call<GeoIpResponseModel> call, retrofit2.Response<GeoIpResponseModel> response) {
                            String country = response.body().getCountry();


                            if(country.equals("Indonesia")||country.equals("India")||country.equals("Malaysia")) {

                                editor.putString("negara","1");
                                editor.commit();
                                Intent myIntent = new Intent(mulai.this, MainActivity.class);
                                startActivity(myIntent);
                                finish();
                            }else {

                                editor.putString("negara","0");
                                editor.commit();
                                Intent myIntent = new Intent(mulai.this, MainActivity.class);
                                startActivity(myIntent);
                                finish();
                            }



                        }

                        @Override
                        public void onFailure(Call<GeoIpResponseModel> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mulai.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(stringRequest);











    }






}


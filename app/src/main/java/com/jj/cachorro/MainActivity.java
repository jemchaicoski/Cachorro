package com.jj.cachorro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ImageView fotoCachorroInicio;
    private ListView lista;
    private ArrayList<String> racas;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        racas = new ArrayList<String>();
        fotoCachorroInicio = (ImageView) findViewById(R.id.fotoCachorroInicio);
        lista = (ListView) findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, racas);


        lista.setAdapter(arrayAdapter);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String cachorro = racas.get(position);
                Log.e("cachorro", cachorro);
                Intent intent = new Intent(getApplicationContext(), /*Activity Que Vai Ser Chamada*/ Racas.class);
                intent.putExtra("Racas", cachorro);
                startActivity(intent);

            }
        });


        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://dog.ceo/api/breeds/list";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String cap;
                            JSONArray jsonArray = response.getJSONArray("message");
                            for(int i = 0; i < jsonArray.length(); i++){
                                cap = capitalize(jsonArray.get(i).toString());
                                racas.add(cap);
                            }
                            arrayAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }


                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });

        // Access the RequestQueue through your singleton class.
        queue.add(jsonObjectRequest);
    }

    @Override
    protected void onResume() {
        SharedPreferences preferences = getSharedPreferences("user_preferences", MODE_PRIVATE);
        String urlImagem = preferences.getString("Url", "");

        if (!urlImagem.isEmpty()){
            Picasso.get().load(urlImagem).into(fotoCachorroInicio);
        }

        super.onResume();
    }

    public static String capitalize(String str) {
        if(str== null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}



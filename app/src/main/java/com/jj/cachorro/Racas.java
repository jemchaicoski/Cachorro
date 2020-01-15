package com.jj.cachorro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

import static com.jj.cachorro.MainActivity.capitalize;

public class Racas extends AppCompatActivity {
    ImageView fotoCachorro;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_racas);



        fotoCachorro = findViewById(R.id.fotoCachorro);
        final ListView listView = (ListView) findViewById(R.id.listView);
        final ArrayList<String> arrayList = new ArrayList<String>();
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);

        Intent intent = getIntent();
        final String nomeCachorro = intent.getStringExtra("Racas");

        String inicio = "https://dog.ceo/api/breed/";
        String inicioImagem = "https://dog.ceo/api/breed/";

        String imagem = inicioImagem + nomeCachorro.toLowerCase() +"/images/random";
        String url = inicio + nomeCachorro.toLowerCase() + "/list";




        // Instantiate the RequestQueue.
        queue = Volley.newRequestQueue(this);
        urlFoto(imagem);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String cap;
                            JSONArray jsonArray = response.getJSONArray("message");
                            for(int i = 0; i < jsonArray.length(); i++){
                                cap = capitalize(jsonArray.get(i).toString());
                                arrayList.add(cap);
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

    public void urlFoto(String url){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String urlImagem = response.getString("message");
                            Picasso.get().load(urlImagem).into(fotoCachorro);

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
        queue.add(jsonObjectRequest);
    }
}


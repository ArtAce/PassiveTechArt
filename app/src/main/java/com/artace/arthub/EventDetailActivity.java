package com.artace.arthub;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.artace.arthub.adapter.ListSenimanDetailEventAdapter;
import com.artace.arthub.connection.DatabaseConnection;
import com.artace.arthub.controller.AppController;
import com.artace.arthub.pojo.PojoEvent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EventDetailActivity extends AppCompatActivity {

    RequestQueue queue;
    String urlRead;
    Toolbar mToolbar;
    Button btnListSeniman;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        btnListSeniman = (Button) findViewById(R.id.event_detail_btnListSeniman);

        Bundle extras = getIntent().getExtras();
        final int id_event = extras.getInt("id_event");
        urlRead = DatabaseConnection.getReadEventDetail() + "?id_event=" + String.valueOf(id_event);

        setToolbar();

        getDetailEvent();

        btnListSeniman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventDetailActivity.this, ListSenimanDetailEventActivity.class);
                intent.putExtra("id_event",id_event);
                startActivity(intent);
                finish();

            }
        });

    }

    private void getDetailEvent(){
        //Getting Instance of Volley Request Queue
        queue = AppController.getInstance().getRequestQueue();
        //Volley's inbuilt class to make Json array request
        JsonArrayRequest newsReq = new JsonArrayRequest(urlRead, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try{
                    JSONArray jr = response.getJSONArray(0);
                    for (int i = 0; i < jr.length(); i++) {
                        try {

                            JSONObject obj = (JSONObject) jr.get(i);

                            int idEvent = obj.getInt("id_event");
                            int idEventOrganizer = obj.getInt("id_event_organizer");

                            TextView namaEvent = (TextView) findViewById(R.id.event_detail_namaevent);
                            namaEvent.setText(obj.getString("nama"));

                            EditText tanggalEvent = (EditText) findViewById(R.id.event_detail_tanggalevent);
                            tanggalEvent.setText(obj.getString("tanggal"));

                            EditText lokasiEvent = (EditText) findViewById(R.id.event_detail_lokasi);
                            lokasiEvent.setText(obj.getString("lokasi"));

                            EditText keteranganEvent = (EditText) findViewById(R.id.event_detail_keterangan);
                            keteranganEvent.setText(obj.getString("keterangan"));

                            NetworkImageView imageEvent = (NetworkImageView) findViewById(R.id.event_detail_imageEvent);
                            imageEvent.setImageUrl(DatabaseConnection.getBaseUrl() + obj.getString("foto"), AppController.getInstance().getImageLoader());


                            EditText namaEo = (EditText) findViewById(R.id.event_detail_namaeo);
                            namaEo.setText(obj.getString("nama_eo"));

                        } catch (Exception e) {
                            System.out.println("LOG gamao! = " + e.getMessage());
                        }
                    }
                }catch (Exception e){
                    System.out.println("LOG gamao diluar! = " + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("LOG_OrganizerEventsFragment : "+error.getMessage());

            }
        });
        //Adding JsonArrayRequest to Request Queue
        queue.add(newsReq);
    }

    private void setToolbar(){
        mToolbar = (Toolbar) findViewById(R.id.event_detail_toolbar);
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Detail Event");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

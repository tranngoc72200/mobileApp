package com.example.demosqlite.Actitvities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.demosqlite.R;
import com.example.demosqlite.models.TripModel;
import com.example.demosqlite.services.DatabaseHelper;
import com.example.demosqlite.services.ListTripItemAdapter;

import java.util.List;

public class SearchTripActivity extends AppCompatActivity {

    ListView listView;
    EditText editT_tripName;
    ImageButton butt_searchTrip, butt_tripDetail;
    List<TripModel> listTrips;
    ListTripItemAdapter tripItemAdapter;

    TripModel selectTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_trip);

        editT_tripName = findViewById(R.id.et_trip_name);
        listView = findViewById(R.id.lv_list_all_trips);
        butt_searchTrip = findViewById(R.id.btn_search_trip);
        butt_tripDetail = findViewById(R.id.btn_trip_detail);

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            selectTrip = (TripModel) adapterView.getItemAtPosition(i);
        });

        butt_tripDetail.setOnClickListener(view -> {
            if (selectTrip != null) {
                buttdetail();
            } else {
                Toast.makeText(this, "No selected Trip", Toast.LENGTH_SHORT).show();
            }
        });

        butt_searchTrip.setOnClickListener(view -> {
            if (!editT_tripName.getText().equals("")) {
                selectTrip = null;
                buttsearch();
            } else {
                Toast.makeText(this, "No search value input", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void buttdetail() {
        Intent i = new Intent(this, TripDetailActivity.class);
        i.putExtra(getString(R.string.TripID), selectTrip.gTripId().toString());
        finish();
        startActivity(i);
    }

    private void buttsearch() {
        DatabaseHelper db = new DatabaseHelper(this);
        listTrips = db.searchTripByName(editT_tripName.getText().toString());

        if (listTrips != null) {

            tripItemAdapter = new ListTripItemAdapter(this, listTrips);

            //ArrayAdapter<TripModel> tripArrayAdapter = new ArrayAdapter<TripModel>(ListTripActivity.this, android.R.layout.simple_list_item_1, listTrips);
            try {
                listView.setAdapter(tripItemAdapter);
            }
            catch (Exception ex){
                ex.printStackTrace();
                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, R.string.NoTripDataMsg, Toast.LENGTH_LONG).show();
        }
    }
}
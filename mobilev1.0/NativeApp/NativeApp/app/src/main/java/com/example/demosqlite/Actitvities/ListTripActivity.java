package com.example.demosqlite.Actitvities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.demosqlite.R;
import com.example.demosqlite.databinding.ActivityMainBinding;
import com.example.demosqlite.models.TripModel;
import com.example.demosqlite.services.DatabaseHelper;
import com.example.demosqlite.services.ListTripItemAdapter;

import java.util.List;

public class ListTripActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    AlertDialog globalDialog;

    ImageButton butt_AddTrip, butt_delete, butt_tripDetail, butt_resetTableTrip;
    ListView lview_listAllTrips;
    TripModel select_Trip;

    ListTripItemAdapter tripItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_list_trips);

        lview_listAllTrips = findViewById(R.id.lv_list_all_trips);
        butt_AddTrip = findViewById(R.id.btn_add_trip);
        butt_delete = findViewById(R.id.btn_delete_trip);
        butt_tripDetail = findViewById(R.id.btn_trip_detail);
        butt_resetTableTrip = findViewById(R.id.btn_reset_table_trip);



        //Activity method
        ListTrip();

        butt_resetTableTrip.setOnClickListener(view -> {
            ResetConfirmDialog();
        });

        lview_listAllTrips.setOnItemClickListener((adapterView, view, i, l) -> {
            select_Trip = (TripModel) adapterView.getItemAtPosition(i);
        });

        butt_delete.setOnClickListener(view -> {
            DeleteDialog();
        });

        butt_AddTrip.setOnClickListener(view -> {
            Intent i = new Intent(ListTripActivity.this, FormActivity.class);
            finish();
            startActivity(i);
        });

        butt_tripDetail.setOnClickListener(view -> {
            goToTripDetailScreen();
        });
    }

    private void DeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Trip");
        builder.setMessage("Do you want to delete trip: " + select_Trip.gTripName() + "?");
        builder.setPositiveButton("Confirm", (dialogInterface, i) -> {
            buttDelete();
        });
        builder.setNegativeButton(R.string.cancel, (dialogInterface, i) -> {
           dialogInterface.cancel();
        });

        globalDialog = builder.create();
        globalDialog.show();
    }

    private void buttDelete() {
        if (select_Trip != null){
            boolean isSucceed = deletedTrip(select_Trip);
            if (isSucceed) {
                reloadActivity();
                Toast.makeText(ListTripActivity.this, R.string.TripDeletedMsg, Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(ListTripActivity.this, R.string.TripDeleteErrorMsg, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(ListTripActivity.this, R.string.NoSelectedTripMsg, Toast.LENGTH_SHORT).show();
        }
    }


    private void ResetConfirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reset Trip Database");
        builder.setMessage("Do you want to delete all trips?");
        builder.setPositiveButton("Confirm", (dialogInterface, i) -> {
            rstTableTrip();
            dialogInterface.dismiss();
        });
        builder.setNegativeButton(R.string.cancel, (dialogInterface, i) -> {
           dialogInterface.cancel();
        });

        globalDialog = builder.create();
        globalDialog.show();

    }

    private void reloadActivity(){
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

    private void goToTripDetailScreen() {
        Intent i = new Intent(ListTripActivity.this, TripDetailActivity.class);
        if (select_Trip != null) {
            i.putExtra(getString(R.string.TripID), select_Trip.gTripId().toString());
            finish();
            startActivity(i);
        } else {
            Toast.makeText(ListTripActivity.this, R.string.NoSelectedTripMsg, Toast.LENGTH_SHORT).show();
        }
    }

    private void rstTableTrip() {
        DatabaseHelper db = new DatabaseHelper(ListTripActivity.this);
        boolean isSucceed = db.resetTableTrip();
        if (isSucceed) {
            Toast.makeText(ListTripActivity.this, "Table Trip Clear!", Toast.LENGTH_SHORT).show();
            reloadActivity();
        }
        else {
            Toast.makeText(ListTripActivity.this, "Error resetting table", Toast.LENGTH_LONG).show();
        }
    }

    private boolean deletedTrip(TripModel selectedTrip){
        DatabaseHelper db = new DatabaseHelper(ListTripActivity.this);

        try {
            return db.DeleteTrip(selectedTrip);
        }
        catch (Exception ex){
            ex.printStackTrace();
            Toast.makeText(ListTripActivity.this, R.string.TripDeleteErrorMsg, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void ListTrip() {
        DatabaseHelper db = new DatabaseHelper(ListTripActivity.this);
        List<TripModel> listTrips = db.GetAllTrips();

        if (listTrips.size() != 0) {

            tripItemAdapter = new ListTripItemAdapter(ListTripActivity.this, listTrips);

            //ArrayAdapter<TripModel> tripArrayAdapter = new ArrayAdapter<TripModel>(ListTripActivity.this, android.R.layout.simple_list_item_1, listTrips);
            try {
                lview_listAllTrips.setAdapter(tripItemAdapter);
            }
            catch (Exception ex){
                ex.printStackTrace();
                Toast.makeText(ListTripActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(ListTripActivity.this, R.string.NoTripDataMsg, Toast.LENGTH_LONG).show();
        }

    }


}
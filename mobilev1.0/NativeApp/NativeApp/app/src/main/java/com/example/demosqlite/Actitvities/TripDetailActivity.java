package com.example.demosqlite.Actitvities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demosqlite.R;
import com.example.demosqlite.models.TripModel;
import com.example.demosqlite.services.DatabaseHelper;
import com.example.demosqlite.services.DateConversionHelper;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class TripDetailActivity extends AppCompatActivity {

    DatePickerDialog SDatePickerDialog, EDatePickerDialog;

    TextView tview_tripName, tview_tripDestination, tview_tripDesc;
    CheckBox checkb_tripAssessment;
    ImageView iv_tripImage;
    ImageButton butt_editTrip, butt_addTrip, butt_deleteTrip, butt_addExpense;

    AlertDialog.Builder builder;
    AlertDialog editDialog;

    EditText popup_editt_tripName, popup_editt_tripDestination, popup_editt_tripDescription;
    AppCompatButton popup_butt_tripStartDate, popup_butt_tripEndDate, butt_tripStartDate, butt_tripEndDate;
    CheckBox popup_checkb_triprequireassessment;


    String selectedTripID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);

        butt_addExpense = findViewById(R.id.btn_list_expense);
        butt_addTrip = findViewById(R.id.btn_add_trip);
        butt_editTrip = findViewById(R.id.btn_edit_trip);
        butt_deleteTrip = findViewById(R.id.btn_delete_trip);
        tview_tripName = findViewById(R.id.tv_trip_name);
        tview_tripDestination = findViewById(R.id.tv_trip_dest);
        tview_tripDesc = findViewById(R.id.tv_trip_desc);
        checkb_tripAssessment = findViewById(R.id.cb_risk_assessment);
        butt_tripEndDate = findViewById(R.id.btn_trip_end);
        butt_tripStartDate = findViewById(R.id.btn_trip_start);



        //Init
        BundleExtract();
        LoadTripDetail();


        //Button function
        butt_addTrip.setOnClickListener(view -> {
            goToTripForm();
        });

        butt_deleteTrip.setOnClickListener(view -> {
            ppDelete();
        });

        butt_editTrip.setOnClickListener(view -> {
            editThisTrip();
        });

        butt_addExpense.setOnClickListener(view -> {
            startListExpenseActivity();
        });
    }

    private void startListExpenseActivity() {
        Intent i = new Intent(this, ListExpenseActivity.class);
        i.putExtra(getString(R.string.TripID), selectedTripID);
        startActivity(i);
    }

    private void editThisTrip() {
        ppEdit();
    }

    private void ppDelete(){
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure?");
        builder.setMessage("Do you want to delete trip " + "'" + tview_tripName.getText().toString() + "'?");
        builder.setPositiveButton("Confirm", ((dialogInterface, i) -> {
            deleteThisTrip();
        }));
        builder.setNegativeButton(R.string.cancel, (dialogInterface, i) -> {
           dialogInterface.cancel();
        });

        AlertDialog deleteDialog = builder.create();
        deleteDialog.show();
    }

    private void ppEdit(){
        builder = new AlertDialog.Builder(this);
        final View editPopupView = getLayoutInflater().inflate(R.layout.popup_edit, null);
        popup_editt_tripName = editPopupView.findViewById(R.id.et_trip_name);
        popup_editt_tripDestination = editPopupView.findViewById(R.id.et_trip_destination);
        popup_editt_tripDescription = editPopupView.findViewById(R.id.et_trip_desc);
        popup_butt_tripStartDate = editPopupView.findViewById(R.id.btn_trip_start);
        popup_butt_tripEndDate = editPopupView.findViewById(R.id.btn_trip_end);
        popup_checkb_triprequireassessment = editPopupView.findViewById(R.id.cb_risk_assessment);
        builder.setPositiveButton("Confirm", (dialog, which) -> {
            updTripDetail();
            editDialog.dismiss();
        });
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
            dialog.cancel();
        });

        builder.setView(editPopupView);


        InitDatePicker();
        initPopupData();


        editDialog = builder.create();
        editDialog.show();
    }

    private void updTripDetail() {
        DatabaseHelper db = new DatabaseHelper(this);

        byte[] emptyByte = new byte[10];

        TripModel newTrip = new TripModel(
                UUID.fromString(selectedTripID),
                Objects.requireNonNull(popup_editt_tripName.getText()).toString(),
                popup_editt_tripDestination.getText().toString(),
                popup_butt_tripStartDate.getText().toString(),
                popup_butt_tripEndDate.getText().toString(),
                popup_checkb_triprequireassessment.isChecked(),
                popup_editt_tripDescription.getText().toString(),
                emptyByte
        );


        boolean isSucceed = db.updateTripModel(newTrip);
        if (isSucceed) {
            Intent i = new Intent(this, TripDetailActivity.class);
            i.putExtra(getString(R.string.TripID), selectedTripID);
            finish();
            startActivity(i);
        } else {
            Toast.makeText(this, "Error update trip", Toast.LENGTH_SHORT).show();
        }
    }

    private void initPopupData() {
        popup_editt_tripName.setText(tview_tripName.getText());
        popup_editt_tripDestination.setText(tview_tripDestination.getText());
        popup_editt_tripDescription.setText(tview_tripDesc.getText());
        popup_checkb_triprequireassessment.setChecked(checkb_tripAssessment.isChecked());
        popup_butt_tripStartDate.setText(butt_tripStartDate.getText());
        popup_butt_tripEndDate.setText(butt_tripEndDate.getText());

        popup_butt_tripStartDate.setOnClickListener(view -> {
            SDatePickerDialog.show();
        });
        popup_butt_tripEndDate.setOnClickListener(view -> {
            EDatePickerDialog.show();
        });
    }

    public  void InitDatePicker() {

        DateConversionHelper dateConversionHelper = new DateConversionHelper();

        DatePickerDialog.OnDateSetListener StartDateSetListener = (datePicker, year, month, day) -> {
            month++;
            String date = dateConversionHelper.makeDateString(day, month, year);
            popup_butt_tripStartDate.setText(date);
        };

        DatePickerDialog.OnDateSetListener EndDateSetListener = (datePicker, year, month, day) -> {
            month++;
            String endDate = dateConversionHelper.makeDateString(day, month, year);
            popup_butt_tripEndDate.setText(endDate);
        };

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int Style = android.app.AlertDialog.THEME_HOLO_LIGHT;

        SDatePickerDialog = new DatePickerDialog(this, Style, StartDateSetListener, year, month, day);
        EDatePickerDialog = new DatePickerDialog(this, Style, EndDateSetListener, year, month, day);
    }

    private void deleteThisTrip() {
        DatabaseHelper db = new DatabaseHelper(TripDetailActivity.this);
        TripModel existTrip = db.GetTripDetailByID(selectedTripID);
        Intent i = new Intent(TripDetailActivity.this, ListTripActivity.class);

        db.DeleteTrip(existTrip);
        finish();
        startActivity(i);

    }

    private void goToTripForm() {
        Intent i = new Intent(TripDetailActivity.this, FormActivity.class);
        finish();
        startActivity(i);
    }

    private void LoadTripDetail() {
        DatabaseHelper db = new DatabaseHelper(TripDetailActivity.this);
        DateConversionHelper dateConversionHelper = new DateConversionHelper();
        //call trip detail
        TripModel existTrip = db.GetTripDetailByID(selectedTripID);
        tview_tripName.setText(existTrip.gTripName());
        tview_tripDestination.setText(existTrip.gTripDest());
        tview_tripDesc.setText(existTrip.gTripDesc());
        checkb_tripAssessment.setChecked(existTrip.isRiskAssessmentChecked());

        Date startDate = dateConversionHelper.convertToDateTime(existTrip.gStartDateTrip());
        butt_tripStartDate.setText(dateConversionHelper.convertToUIDateString(startDate));

        Date endDate = dateConversionHelper.convertToDateTime(existTrip.gEndDateTrip());
        butt_tripEndDate.setText(dateConversionHelper.convertToUIDateString(endDate));



    }

    private void BundleExtract() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            selectedTripID = bundle.getString(getString(R.string.TripID));
        }
    }
}
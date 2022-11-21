package com.example.demosqlite.Actitvities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;

import com.example.demosqlite.R;
import com.example.demosqlite.models.TripModel;
import com.example.demosqlite.services.DatabaseHelper;
import com.example.demosqlite.services.DateConversionHelper;

import java.util.Calendar;
import java.util.Objects;
import java.util.UUID;

public class FormActivity extends AppCompatActivity {

    private DatePickerDialog SDatePickerDialog, EPickerDialog;
    AppCompatEditText editT_tripName, editT_tripDestination, editT_tripDesc;
    AppCompatCheckBox checkb_isRequiredRA;
    AppCompatButton butt_addTrip, editT_tripStartDate, editT_tripEndDate;
    AppCompatImageButton butt_backToMain;
    byte[] imageByteArray;
    ImageView iv_tripImage;
    AlertDialog confirmDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_trip_form);


        editT_tripName = findViewById(R.id.et_trip_name);
        editT_tripDesc = findViewById(R.id.et_trip_desc);
        editT_tripDestination = findViewById(R.id.et_trip_destination);
        checkb_isRequiredRA = findViewById(R.id.cb_risk_assessment);
        butt_addTrip = findViewById(R.id.bt_add_trip);
        editT_tripStartDate = findViewById(R.id.et_trip_date);
        editT_tripEndDate = findViewById(R.id.bt_trip_end_date);
        butt_backToMain = findViewById(R.id.bt_back_to_main);

        editT_tripStartDate.setText(R.string.defaultDate);
        editT_tripStartDate.setOnClickListener(view -> {
            OpenDatePicker(1);
        });


        DateConversionHelper dateConversionHelper = new DateConversionHelper();
        editT_tripEndDate.setText(dateConversionHelper.GetTodayDate());
        editT_tripEndDate.setOnClickListener(view -> {
            OpenDatePicker(2);
        });


        //Initialization
        InitDatePicker();
        bundleExtract();


        editT_tripName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                editT_tripName.setHintTextColor(getResources().getColor(R.color.black));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        editT_tripDestination.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                editT_tripDestination.setHintTextColor(getResources().getColor(R.color.black));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        editT_tripStartDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                editT_tripStartDate.setTextColor(getResources().getColor(R.color.black));
            }
        });

        editT_tripEndDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                editT_tripEndDate.setTextColor(getResources().getColor(R.color.black));
            }
        });


        butt_backToMain.setOnClickListener(view -> {
            Intent i = new Intent(FormActivity.this, MainActivity.class);
            finish();
            startActivity(i);
        });

        butt_addTrip.setOnClickListener(view -> {
            boolean requirementCheck = check_Requirements();
            if (!requirementCheck) {
                Toast.makeText(FormActivity.this, R.string.requiredFieldMissing, Toast.LENGTH_SHORT).show();
            } else {
                cf_Popup();
                confirmDialog.show();
            }
        });
    }

    private void cf_Popup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(FormActivity.this);
        builder.setCancelable(true);
        builder.setTitle("Trip Confirmation");
        builder.setMessage("Do you want to add this trip detail\n" +
                        "\n\tTrip Name: " + editT_tripName.getText().toString() +
                        "\n\tDestination: " + editT_tripDestination.getText().toString() +
                        "\n\tRequire Risk Assessment: " + (checkb_isRequiredRA.isChecked() ? "Yes!" : "No!") +
                        "\n\tStart Date: " + editT_tripStartDate.getText().toString() +
                        "\n\tEnd Date: " + editT_tripEndDate.getText().toString());
        builder.setPositiveButton("Confirm", (dialog, which) -> {
            button_AddTrip();
        });
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
            dialog = confirmDialog;
            dialog.cancel();
        });

        confirmDialog = builder.create();

    }

    private boolean check_Requirements() {
        boolean result = true;

        if (validateEmptyString(editT_tripName.getText().toString())){
            editT_tripName.setHintTextColor(getResources().getColor(R.color.red));
            result = false;
        }
        if (validateEmptyString(editT_tripDestination.getText().toString())){
            editT_tripDestination.setHintTextColor(getResources().getColor(R.color.red));
            result = false;
        }
        if (editT_tripStartDate.getText().toString().equals(getString(R.string.defaultDate))){
            editT_tripStartDate.setTextColor(getResources().getColor(R.color.red));
            result = false;
        }
        return result;
    }

    private boolean validateEmptyString(String string){
        return string.equals("") || string == null;
    }

    private void button_AddTrip() {
        TripModel newTrip = new TripModel(
                UUID.randomUUID(),
                Objects.requireNonNull(editT_tripName.getText()).toString(),
                Objects.requireNonNull(editT_tripDestination.getText()).toString(),
                editT_tripStartDate.getText().toString(),
                editT_tripEndDate.getText().toString(),
                checkb_isRequiredRA.isChecked(),
                editT_tripDesc.getText().toString().equals("") ? "" : editT_tripDesc.getText().toString(),
                imageByteArray
        );

        Isrt_NewTripModel(newTrip);
    }

    private void bundleExtract() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            imageByteArray = bundle.getByteArray("imageByteArray");
            Toast.makeText(FormActivity.this, "Image Accepted", Toast.LENGTH_SHORT).show();

            Bitmap bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
            iv_tripImage.setImageBitmap(bitmap);
        }
    }

    public  void InitDatePicker() {

        DateConversionHelper dateConversionHelper = new DateConversionHelper();
        OnDateSetListener StartDateSetListener = (datePicker, year, month, day) -> {
            month++;
            String date = dateConversionHelper.makeDateString(day, month, year);
            editT_tripStartDate.setText(date);
        };

        OnDateSetListener EndDateSetListener = (datePicker, year, month, day) -> {
            month++;
            String endDate = dateConversionHelper.makeDateString(day, month, year);
            editT_tripEndDate.setText(endDate);
        };

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int Style = AlertDialog.THEME_HOLO_LIGHT;

        SDatePickerDialog = new DatePickerDialog(FormActivity.this, Style, StartDateSetListener, year, month, day);
        EPickerDialog = new DatePickerDialog(FormActivity.this, Style, EndDateSetListener, year, month, day);
    }

    private void OpenDatePicker(int DateType) {
        switch (DateType){
            case 1: SDatePickerDialog.show(); break;
            case 2: EPickerDialog.show(); break;
        }
    }

    private void Isrt_NewTripModel(TripModel TripModel) {
        try {
            DatabaseHelper db = new DatabaseHelper(FormActivity.this);

            if (!db.checkEndDateValue(TripModel.gStartDateTrip(), TripModel.gEndDateTrip())){
                editT_tripEndDate.setTextColor(getResources().getColor(R.color.red));
                Toast.makeText(FormActivity.this, R.string.endDateInvalid, Toast.LENGTH_SHORT).show();
            } else {
                boolean isSucceed = db.addOneToTripTable(TripModel);
                if (!isSucceed) {
                    Toast.makeText(FormActivity.this, R.string.ERROR_ADD_TRIP_MSG, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(FormActivity.this, R.string.TOAST_TRIP_ADDED, Toast.LENGTH_SHORT).show();
                }
            }

        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
package com.example.demosqlite.Actitvities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demosqlite.R;
import com.example.demosqlite.models.ExpenseModel;
import com.example.demosqlite.models.TripModel;
import com.example.demosqlite.services.DatabaseHelper;
import com.example.demosqlite.services.DateConversionHelper;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class ExpenseFormActivity extends AppCompatActivity {


    EditText editT_expenseType, editT_expenseAmount, editT_expenseComment;
    AppCompatButton butt_expenseDate, butt_add_expense, ppbutt_expenseDate, butt_expenseTime, ppbutt_expenseTime;
    TextView tview_tripName, pptview_expenseType, pptview_expenseAmount, pptview_expenseComment;

    DatePickerDialog globalDatePicker;
    AlertDialog globalAlertDialog;

    String tripID;
    TripModel selectedTrip;

    int selectedHour, selectedMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_form);

        variables();
        bundleExtract();

        DateConversionHelper dateConversionHelper = new DateConversionHelper();
        Date defaultTripStartDate = dateConversionHelper.convertToDateTime(selectedTrip.gStartDateTrip());
        butt_expenseDate.setText(dateConversionHelper.convertToUIDateString(defaultTripStartDate));
        butt_expenseDate.setOnClickListener(view -> {
            DatePickerDialog();
        });

        butt_expenseTime.setOnClickListener(view -> {
            TimePickerDialog();
        });

        butt_add_expense.setOnClickListener(view -> {
            boolean isSucceed = requirementCheck();
            if (isSucceed) {
                AddDialog();
            }
        });

        editT_expenseType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                editT_expenseType.setHintTextColor(getResources().getColor(R.color.black));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        editT_expenseAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                editT_expenseAmount.setHintTextColor(getResources().getColor(R.color.black));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void TimePickerDialog() {

        TimePickerDialog.OnTimeSetListener builder = (timePicker, hour, minute) -> {
            selectedHour = hour;
            selectedMinute = minute;
            butt_expenseTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
        };

        int style = AlertDialog.THEME_HOLO_LIGHT;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, style, builder, selectedHour, selectedMinute, true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    private boolean requirementCheck() {

        return edittextCheck() && timeButton();

    }

    private boolean timeButton() {
        DateConversionHelper dateConversionHelper = new DateConversionHelper();

        Date startDate = dateConversionHelper.convertToDateTime(selectedTrip.gStartDateTrip());
        Date endDate = dateConversionHelper.convertToDateTime(selectedTrip.gEndDateTrip());

        long startDateLong = startDate.getTime();
        long endDateLong = endDate.getTime();
        long expenseDate = dateConversionHelper.convertToDate(butt_expenseDate.getText().toString()).getTime();

        if (expenseDate >= startDateLong && expenseDate <= endDateLong) {
            return true;
        } else {
            butt_expenseDate.setTextColor(getResources().getColor(R.color.red));
            Toast.makeText(this, "Date must be between " + dateConversionHelper.convertToUIDateString(startDate)
                    + " and " + dateConversionHelper.convertToUIDateString(endDate), Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private boolean edittextCheck() {
        String expenseTypeContent = editT_expenseType.getText().toString();
        String expenseAmount = editT_expenseAmount.getText().toString();

        if (!expenseAmount.equals("") && !expenseTypeContent.equals("")) {
            return true;
        }

        editT_expenseType.setHintTextColor(getResources().getColor(R.color.red));
        editT_expenseAmount.setHintTextColor(getResources().getColor(R.color.red));

        Toast.makeText(this, R.string.requiredFieldMissing, Toast.LENGTH_SHORT).show();
        return false;
    }

    private void bundleExtract() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            tripID = bundle.getString(getString(R.string.TripID));
        }

        GetTripName();
    }

    private void GetTripName() {
        DatabaseHelper db = new DatabaseHelper(this);
        String tripName = db.getTripNameByID(UUID.fromString(tripID));
        if (tripName == null) {
            finish();
            Toast.makeText(this, R.string.cannotFindTrip, Toast.LENGTH_SHORT).show();
        }

        tview_tripName.setText(tripName);
        selectedTrip = db.GetTripDetailByID(tripID);

    }

    private void AddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        @SuppressLint("InflateParams")
        final View layout = getLayoutInflater().inflate(R.layout.popup_expense, null);

        pptview_expenseType = layout.findViewById(R.id.popup_tv_expense_type);
        pptview_expenseComment = layout.findViewById(R.id.popup_tv_expense_comment);
        pptview_expenseAmount = layout.findViewById(R.id.popup_tv_expense_amount);
        ppbutt_expenseDate = layout.findViewById(R.id.popup_btn_expense_date);
        ppbutt_expenseTime = layout.findViewById(R.id.popup_btn_expense_time);

        pptview_expenseType.setText(editT_expenseType.getText());
        pptview_expenseComment.setText(editT_expenseComment.getText());
        pptview_expenseAmount.setText(editT_expenseAmount.getText());
        ppbutt_expenseDate.setText(butt_expenseDate.getText());
        ppbutt_expenseTime.setText(butt_expenseTime.getText());

        builder.setView(layout);

        builder.setPositiveButton(R.string.confirm, (dialogInterface, i) -> {
            Button_Add();
        });
        builder.setNegativeButton(R.string.cancel, (dialogInterface, i) -> {
           dialogInterface.cancel();
        });

        globalAlertDialog = builder.create();
        globalAlertDialog.show();
    }

    private void Button_Add() {

        boolean isSucceed = addOneExpenseToDB();
        if (isSucceed){
            Toast.makeText(this, R.string.expenseAdded, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.errorAddExpense, Toast.LENGTH_LONG).show();
        }
    }

    private boolean addOneExpenseToDB() {

        //reformat expense amount
        String expenseAmountContent = editT_expenseAmount.getText().toString();
        String[] arrayString = expenseAmountContent.split(" ");

        ExpenseModel newExpense = new ExpenseModel(
                UUID.randomUUID(),
                editT_expenseType.getText().toString(),
                Integer.parseInt(arrayString[0]),
                butt_expenseDate.getText().toString() + " " + butt_expenseTime.getText().toString(),
                editT_expenseComment.getText().toString()
        );

        //add to db
        DatabaseHelper db = new DatabaseHelper(this);
        return db.addOneToTableExpense(newExpense, tripID);
    }

    private void DatePickerDialog() {

        DateConversionHelper dateConversionHelper = new DateConversionHelper();
        DatePickerDialog.OnDateSetListener DatePickerListener = (datePicker, year, month, day) -> {
            month++;
            String Date = dateConversionHelper.makeDateString(day, month, year);
            butt_expenseDate.setText(Date);
            butt_expenseDate.setTextColor(getResources().getColor(R.color.black));
        };

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int Style = AlertDialog.THEME_HOLO_LIGHT;

        globalDatePicker = new DatePickerDialog(this, Style, DatePickerListener, year, month, day);
        globalDatePicker.show();
    }

    private void variables() {
        butt_expenseTime = findViewById(R.id.btn_expense_time);
        tview_tripName = findViewById(R.id.tv_trip_name);
        editT_expenseAmount = findViewById(R.id.et_expense_amount);
        editT_expenseComment = findViewById(R.id.et_expense_comment);
        editT_expenseType = findViewById(R.id.et_expense_type);
        butt_expenseDate = findViewById(R.id.btn_expense_date);
        butt_add_expense = findViewById(R.id.btn_add_expense);
    }
}
package com.example.demosqlite.services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.demosqlite.models.ExpenseModel;
import com.example.demosqlite.models.TripModel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TABLE_TRIP = "TABLE_TRIP";
    public static final String TRIP_ID = "TRIP_ID";
    public static final String TRIP_NAME = "TRIP_NAME";
    public static final String TRIP_DEST = "TRIP_DEST";
    public static final String TRIP_DATE = "TRIP_DATE";
    public static final String IS_RISK_ASSESSMENT_CHECKED = "IS_RISK_ASSESSMENT_CHECKED";
    public static final String TRIP_DESC = "TRIP_DESC";
    public static final String TABLE_EXPENSES = "TABLE_EXPENSES";
    public static final String EX_ID = "EX_ID";
    public static final String EX_TYPE = "EX_TYPE";
    public static final String EX_AMOUNT = "EX_AMOUNT";
    public static final String EX_TIME = "EX_TIME";
    public static final String EX_COMMENT = "EX_COMMENT";
    public static final String EX_TRIP_ID = "EX_TRIP_ID";
    public static final String TRIP_END = "TRIP_END";
    public static final String TRIP_IMAGE_BYTE = "TRIP_IMAGE_BYTE";
    public static final String EQUAL = " = ";
    public static final String NHAY_DON = "'";

    public DatabaseHelper(@Nullable Context context) {
        super(context, "DemoSQLite.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTripTableStatement =
                "CREATE TABLE " + TABLE_TRIP + " " + "(" +
                        TRIP_ID + " UNIQUEIDENTIFIER PRIMARY KEY," +
                        TRIP_NAME + " NVARCHAR(20)," +
                        TRIP_DEST + " NVARCHAR(20)," +
                        TRIP_DATE + " DATETIME," +
                        TRIP_END + " DATETIME," +
                        IS_RISK_ASSESSMENT_CHECKED + " INTEGER," +
                        TRIP_DESC + " NVARCHAR(300)," +
                        TRIP_IMAGE_BYTE + " BYTE )";

        String createExpenseTableStatement =
                "CREATE TABLE " + TABLE_EXPENSES + " (" +
                        EX_ID + " UNIQUEIDENTIFIER PRIMARY KEY," +
                        EX_TYPE + " NVARCHAR(20)," +
                        EX_AMOUNT + " INT," +
                        EX_TIME + " DATETIME," +
                        EX_COMMENT + " NVARCHAR(3000)," +
                        EX_TRIP_ID + " UNIQUEIDENTIFIER," +
                        "FOREIGN KEY(" + EX_TRIP_ID + ") REFERENCES " + TABLE_TRIP + "(" + TRIP_ID + ")" + ")";


        db.execSQL(createTripTableStatement);
        db.execSQL(createExpenseTableStatement);
    }

    public List<ExpenseModel> getAllExpenseByTripID(String tripID) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<ExpenseModel> listResult = new ArrayList<ExpenseModel>();
        String queryString = " SELECT * FROM " + TABLE_EXPENSES + " WHERE " + EX_TRIP_ID + EQUAL + NHAY_DON + tripID + NHAY_DON;

        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()){

            do {
                UUID expenseID = UUID.fromString(cursor.getString(0));
                String expenseType = cursor.getString(1);
                int expenseAmount = cursor.getInt(2);
                String expenseTime = cursor.getString(3);
                String expenseComment = cursor.getString(4);

                ExpenseModel expenseModel = new ExpenseModel(
                        expenseID,
                        expenseType,
                        expenseAmount,
                        expenseTime,
                        expenseComment
                );

                listResult.add(expenseModel);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return listResult;
    }

    public List<TripModel> searchTripByName(String tripName) {
        String queryString = " SELECT * FROM " + TABLE_TRIP +
                " WHERE " + TRIP_NAME + " LIKE " + NHAY_DON + "%" + tripName + "%" + NHAY_DON;
        SQLiteDatabase db = this.getReadableDatabase();
        List<TripModel> listResult = new ArrayList<>();
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()) {
            do {
                TripModel existTrip = new TripModel(
                        UUID.fromString(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getInt(5) == 1,
                        cursor.getString(6),
                        cursor.getBlob(7)
                );
                listResult.add(existTrip);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return listResult;
    }

    public boolean addOneToTableExpense(ExpenseModel newExpense, String tripID){
        ContentValues cv = new ContentValues();
        DateConversionHelper dateConversionHelper = new DateConversionHelper();
        cv.put(EX_TYPE, newExpense.GExpense_Type());
        cv.put(EX_AMOUNT, newExpense.GExpense_Amount());
        cv.put(EX_COMMENT, newExpense.GExpense_Comment());
        cv.put(EX_ID, newExpense.GExpense_Id().toString());
        cv.put(EX_TRIP_ID, tripID);
        cv.put(EX_TIME, dateConversionHelper.convertToSQLiteDateTimeFormat(newExpense.GExpense_Time()));

        SQLiteDatabase db = this.getWritableDatabase();
        long insert = db.insert(TABLE_EXPENSES, null, cv);

        return insert != -1;
    }

    public boolean resetTableTrip(){
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(TABLE_EXPENSES, null, null);
            db.delete(TABLE_TRIP, null, null);
            return true;
        }
        catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean updateExpense(ExpenseModel newExpenseDetail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(EX_TYPE, newExpenseDetail.GExpense_Type());
        cv.put(EX_AMOUNT, Integer.toString(newExpenseDetail.GExpense_Amount()));
        cv.put(EX_TIME, newExpenseDetail.GExpense_Time());
        cv.put(EX_COMMENT, newExpenseDetail.GExpense_Comment());

        try {
            db.update(TABLE_EXPENSES, cv, EX_ID + " = ?", new String[] {newExpenseDetail.GExpense_Id().toString()});
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean deleteExpense(UUID expenseID) {
       SQLiteDatabase db = this.getWritableDatabase();
       try  {
           db.delete(TABLE_EXPENSES, EX_ID + " = ?", new String[]{expenseID.toString()});
           return true;
       } catch (Exception ex) {
           ex.printStackTrace();
           return false;
       }

    }

    public String getTripNameByID(UUID tripID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "SELECT * FROM " + TABLE_TRIP +  " WHERE " + TRIP_ID + EQUAL + NHAY_DON + tripID.toString() + NHAY_DON;
        Cursor cursor = db.rawQuery(queryString, null);

        String result = "";
        if (cursor.moveToFirst()) {
            do {
                try {
                    result = cursor.getString(1);
                } catch (Exception ex){
                    ex.printStackTrace();
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        return result;
    }

    public boolean updateTripModel(TripModel newTripModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        DateConversionHelper dateConversionHelper = new DateConversionHelper();

        cv.put(TRIP_NAME, newTripModel.gTripName());
        cv.put(TRIP_DEST, newTripModel.gTripDest());
        cv.put(TRIP_DESC, newTripModel.gTripDesc());
        cv.put(TRIP_DATE, dateConversionHelper.ConvertToSQLiteDateFormat(newTripModel.gStartDateTrip()));
        cv.put(TRIP_END, dateConversionHelper.ConvertToSQLiteDateFormat(newTripModel.gEndDateTrip()));
        cv.put(IS_RISK_ASSESSMENT_CHECKED, newTripModel.isRiskAssessmentChecked());

        try {
            db.update(TABLE_TRIP, cv, TRIP_ID + EQUAL + NHAY_DON + newTripModel.gTripId() + NHAY_DON, null);
            return true;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }


    }

    public TripModel GetTripDetailByID(String InputTripID){
        TripModel tripResult = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "SELECT * FROM " + TABLE_TRIP +
                " WHERE " + TRIP_ID + " = " + "'" + InputTripID + "'";

        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()){
            do{
                try {
                    String tripID = cursor.getString(0);
                    String tripName = cursor.getString(1);
                    String tripDestination = cursor.getString(2);
                    String tripStartDate = cursor.getString(3);
                    String tripEndDate = cursor.getString(4);
                    boolean isRiskAssessmentChecked = cursor.getInt(5) == 1;
                    String tripDesc = cursor.getString(6);
                    byte[] imageByte = cursor.getBlob(7);

                    tripResult = new TripModel(
                            UUID.fromString(tripID),
                            tripName,
                            tripDestination,
                            tripStartDate,
                            tripEndDate,
                            isRiskAssessmentChecked,
                            tripDesc,
                            imageByte
                    );
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return tripResult;
    }

    public List<ExpenseModel> GetAllExpenses() {
        List<ExpenseModel> listResult = new ArrayList<>();
        String queryString = " SELECT * FROM " + TABLE_EXPENSES;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                ExpenseModel expenseModel = new ExpenseModel(
                        UUID.fromString(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getString(3),
                        cursor.getString(4)
                );
                listResult.add(expenseModel);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return listResult;
    }

    public List<TripModel> GetAllTrips(){

        List<TripModel> result = new ArrayList<>();

        String queryString = "SELECT * FROM " + TABLE_TRIP;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {

            do {

                try {
                    String tripID = cursor.getString(0);
                    String tripName = cursor.getString(1);
                    String tripDestination = cursor.getString(2);
                    String tripStartDate = cursor.getString(3);
                    String tripEndDate = cursor.getString(4);
                    boolean isRiskAssessmentChecked = cursor.getInt(5) == 1;
                    String tripDesc = cursor.getString(6);
                    byte[] imageByte = cursor.getBlob(7);

                    TripModel newModelTrip = new TripModel(
                            UUID.fromString(tripID),
                            tripName,
                            tripDestination,
                            tripStartDate,
                            tripEndDate,
                            isRiskAssessmentChecked,
                            tripDesc,
                            imageByte);

                    result.add(newModelTrip);
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            } while (cursor.moveToNext());

        } else {
            cursor.close();
        }

        return result;
    }

    public boolean addOneToTripTable(TripModel newTrip){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        DateConversionHelper dateConversionHelper = new DateConversionHelper();

        cv.put(TRIP_ID, newTrip.gTripId().toString());
        cv.put(TRIP_NAME, newTrip.gTripName());
        cv.put(TRIP_DEST, newTrip.gTripDest());
        cv.put(TRIP_DESC, newTrip.gTripDesc());
        cv.put(TRIP_DATE, dateConversionHelper.ConvertToSQLiteDateFormat(newTrip.gStartDateTrip()));
        cv.put(TRIP_END, dateConversionHelper.ConvertToSQLiteDateFormat(newTrip.gEndDateTrip()));
        cv.put(IS_RISK_ASSESSMENT_CHECKED, newTrip.isRiskAssessmentChecked());


        long insert = db.insert(TABLE_TRIP, null, cv);
        return insert != -1;
    }

    public boolean DeleteTrip(TripModel existTrip){
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.delete(TABLE_EXPENSES, EX_TRIP_ID + " = ?", new String[] {existTrip.gTripId().toString()});
            db.delete(TABLE_TRIP,  TRIP_ID + " = ?", new String[]{existTrip.gTripId().toString()});
            return true;

        }
        catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean checkEndDateValue(String startDateString, String endDateString) {
        DateConversionHelper dateConversionHelper = new DateConversionHelper();
        long startDate = dateConversionHelper.convertToDate(startDateString).getTime();
        long endDate = dateConversionHelper.convertToDate(endDateString).getTime();
        if (endDate < startDate) {
            return false;
        }
        return true;
    }


}

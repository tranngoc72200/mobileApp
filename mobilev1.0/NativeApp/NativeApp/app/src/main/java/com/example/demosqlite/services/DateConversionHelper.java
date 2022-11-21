package com.example.demosqlite.services;

import android.annotation.SuppressLint;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateConversionHelper {
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat UIDateFormat = new SimpleDateFormat("MMddyyyy");

    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat UIDateTimeFormat = new SimpleDateFormat("MMddyyyy HH:mm");

    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat sqliteDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat sqliteDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    Calendar cal = Calendar.getInstance();

    public DateConversionHelper() {
    }

    /**
     * Convert from SQL date string format to ui date string format
     * @param Date sql date string format
     * @return ui date string format
     */
    public String convertToUIDateFormat(String Date) {
        try {
            cal.setTime(sqliteDateFormat.parse(Date));
            return makeDateString(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * Convert from UI date format to Date Time "yyyy-MM-dd HH:mm:ss"
     * @param dateString UI date format String value
     * @return Date value
     */
    public Date convertToDateTime(String dateString){
        try {
            return sqliteDateTimeFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Convert from SQL Date Time type to UI date format
     * @param date Date value
     * @return String UI date format
     */
    public String convertToUIDateString(Date date) {
        cal.setTime(date);
        return makeDateString(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR));
    }

    /**
     * Get today date and return as UI date format string
     * @return UI date format string
     */
    public String GetTodayDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month++;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    /**
     * Convert from UI date format 'MMddyyyy' to normal Date value
     * @param dateString UI date format
     * @return Date value
     */
    public Date convertToDate(String dateString) {
        dateString = reformatDateString(dateString);
        try {
            assert dateString != null;
            return UIDateFormat.parse(dateString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Convert UI Date format to SQL Date Time string format, this is to store into Database
     * @param dateTime UI Date format "MMddyyyy HH:mm"
     * @return SQL DateTime String format "yyyy-MM-dd HH:mm:ss"
     */
    public String convertToSQLiteDateTimeFormat(String dateTime) {
        dateTime = reformatDateString(dateTime);
        try {
            assert dateTime != null;
            java.util.Date Date = UIDateTimeFormat.parse(dateTime);

            assert Date != null;
            return sqliteDateTimeFormat.format(Date);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Convert from UI Date to Mongo Date
     * @param Datetime ui Date Format
     * @return Date type value
     */
//    public Date convertToMongoDate(String Datetime){
//
//    }

    public String reformatDateString (String Date){
        Date = Date.replace(" / ", "").trim();
        String month = Date.substring(0, 3);
        String dayYear = Date.substring(3);
        if (month.contentEquals("JAN")){ month = "01"; return month + dayYear; }
        if (month.contentEquals("FEB")){ month = "02"; return month + dayYear; }
        if (month.contentEquals("MAR")){ month = "03"; return month + dayYear; }
        if (month.contentEquals("APR")){ month = "04"; return month + dayYear; }
        if (month.contentEquals("MAY")){ month = "05"; return month + dayYear; }
        if (month.contentEquals("JUN")){ month = "06"; return month + dayYear; }
        if (month.contentEquals("JUL")){ month = "07"; return month + dayYear; }
        if (month.contentEquals("AUG")){ month = "08"; return month + dayYear; }
        if (month.contentEquals("SEP")){ month = "09"; return month + dayYear; }
        if (month.contentEquals("OCT")){ month = "10"; return month + dayYear; }
        if (month.contentEquals("NOV")){ month = "11"; return month + dayYear; }
        if (month.contentEquals("DEC")){ month = "12"; return month + dayYear; }
        return null;
    }

    public String ConvertToSQLiteDateFormat (String Date){
        Date = reformatDateString(Date);
        try {
            assert Date != null;
            java.util.Date date = UIDateFormat.parse(Date);
            assert date != null;
            return sqliteDateTimeFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String makeDateString(int day, int month, int year) {
        Format dayFormatter = new DecimalFormat("00");
        return  getMonthFormat(month) + " / " + dayFormatter.format(day) + " / " + year;
    }

    public String getMonthFormat(int month) {
        switch (month) {
            case 1: return "JAN";
            case 2: return "FEB";
            case 3: return "MAR";
            case 4: return "APR";
            case 5: return "MAY";
            case 6: return "JUN";
            case 7: return "JUL";
            case 8: return "AUG";
            case 9: return "SEP";
            case 10: return "OCT";
            case 11: return "NOV";
            case 12: return "DEC";
        }
        return "N/A";
    }
}

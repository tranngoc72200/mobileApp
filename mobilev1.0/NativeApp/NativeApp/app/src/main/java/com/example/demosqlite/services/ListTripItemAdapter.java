package com.example.demosqlite.services;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.demosqlite.R;
import com.example.demosqlite.models.TripModel;

import java.util.List;

public class ListTripItemAdapter extends ArrayAdapter<TripModel>{

    private final Context _context;

    public ListTripItemAdapter(Context context, List<TripModel> tripModelArrayList) {
        super(context, 0, tripModelArrayList);
        _context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        TripModel tripModel = getItem(position);
        LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ViewHolder")
        View rowView = inflater.inflate(R.layout.activity_custom_list_view_layout, parent, false);

        ImageView cv_tripImage = rowView.findViewById(R.id.cv_trip_image);
        TextView tv_tripName = rowView.findViewById(R.id.tv_trip_name);
        TextView tv_tripDestination = rowView.findViewById(R.id.tv_trip_dest);
        TextView tv_tripStartDate = rowView.findViewById(R.id.tv_trip_date);

        //missing trip image and trip's total expense

        tv_tripName.setText(tripModel.gTripName());
        tv_tripDestination.setText(tripModel.gTripDest());
        tv_tripStartDate.setText(customDate(tripModel.gStartDateTrip()));

        return rowView;
    }

    private String customDate(String dateTime){
        String date = dateTime.substring(8, 10);
        int month = Integer.parseInt(dateTime.substring(5, 7));
        String year = dateTime.substring(0, 4);
        DateConversionHelper dateConversionHelper = new DateConversionHelper();

        return dateConversionHelper.getMonthFormat(month) + " / " + date + " / " + year;
    }
}

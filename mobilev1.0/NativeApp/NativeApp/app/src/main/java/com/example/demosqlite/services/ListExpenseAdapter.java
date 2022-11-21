package com.example.demosqlite.services;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.demosqlite.R;
import com.example.demosqlite.models.ExpenseModel;

import java.util.List;

public class ListExpenseAdapter extends ArrayAdapter<ExpenseModel> {

    private final Context context;

    public ListExpenseAdapter(@NonNull Context context, List<ExpenseModel> arrayAdapter) {

        super(context, -1, arrayAdapter);

        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ExpenseModel expenseModel = getItem(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ViewHolder")
        View rowView = inflater.inflate(R.layout.layout_expense_item, parent, false);

        TextView tv_expenseType = rowView.findViewById(R.id.tv_expense_type);
        TextView tv_expenseTime = rowView.findViewById(R.id.tv_expense_time);
        TextView tv_expenseAmount = rowView.findViewById(R.id.tv_expense_amount);
        TextView tv_expenseComment = rowView.findViewById(R.id.tv_expense_comment);

        tv_expenseType.setText(expenseModel.GExpense_Type());
        tv_expenseAmount.setText(String.valueOf(expenseModel.GExpense_Amount()));
        tv_expenseComment.setText(expenseModel.GExpense_Comment());

        tv_expenseTime.setText(customDate(expenseModel.GExpense_Time()));

        return rowView;
    }

    private String customDate(String dateTime){
        String time = dateTime.substring(11);
        String date = dateTime.substring(8, 10);
        String month = dateTime.substring(5, 7);
        String year = dateTime.substring(0, 4);

        if (time.equals("00:00:00")){
            return date + " / " + month + " / " + year;
        }
        return date + " / " + month + " / " + year + " at " + time;
    }
}

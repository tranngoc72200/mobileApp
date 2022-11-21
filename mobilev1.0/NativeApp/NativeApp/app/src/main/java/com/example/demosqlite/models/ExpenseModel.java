package com.example.demosqlite.models;

import java.util.UUID;

public class ExpenseModel {

    //region $private properties
    private UUID expense_Id;
    private String expense_Type;
    private int expense_Amount;
    private String expense_Time;
    private String expense_Comment;

    //endregion

    //region $Getter and Setter

    public String GExpense_Comment() {
        return expense_Comment;
    }

    public void SExpense_Comment(String expense_Comment) {
        this.expense_Comment = expense_Comment;
    }

    public UUID GExpense_Id() {
        return expense_Id;
    }

    public void SExpense_Id(UUID expense_Id) {
        this.expense_Id = expense_Id;
    }

    public String GExpense_Type() {
        return expense_Type;
    }

    public void SExpense_Type(String expense_Type) {
        this.expense_Type = expense_Type;
    }

    public int GExpense_Amount() {
        return expense_Amount;
    }

    public void SExpense_Amount(int expense_Amount) {
        this.expense_Amount = expense_Amount;
    }

    public String GExpense_Time() {
        return expense_Time;
    }

    public void SExpense_Time(String expense_Time) {
        this.expense_Time = expense_Time;
    }

    //endregion

    //region $Constructor

    public ExpenseModel(UUID expenseID, String expenseType, int expenseAmount, String expenseTime, String expenseComment) {
        this.expense_Id = expenseID;
        this.expense_Type = expenseType;
        this.expense_Amount = expenseAmount;
        this.expense_Time = expenseTime;
        this.expense_Comment = expenseComment;
    }

    //endregion

}

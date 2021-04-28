package com.example;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;


import com.google.android.material.datepicker.DateSelector;

import java.text.DateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

@RequiresApi(api = Build.VERSION_CODES.O)
public class DatabaseHandler extends SQLiteOpenHelper {
    private final ArrayList<Food> foodList = new ArrayList<>();
    String date = LocalDate.now().toString();
    public DatabaseHandler(@Nullable Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + Constants.TABLE_NAME + "("
                + Constants.KEY_ID + " INTEGER PRIMARY KEY, "
                + Constants.FOOD_NAME + " TEXT, "
                + Constants.CALORIES + " INT, "
                + Constants.DATE_ADDED + " LONG );";

        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);

        onCreate(db);
    }

    //add food method
    public void addFood(Food food) {
        SQLiteDatabase dba = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.FOOD_NAME, food.getFoodName());
        values.put(Constants.CALORIES, food.getCalories());
        values.put(Constants.DATE_ADDED, System.currentTimeMillis());

        dba.insert(Constants.TABLE_NAME, null, values);
        Log.d("Added com.example.Food Item", "YES");
        dba.close();
    }
    public ArrayList<Food> getAllFood(){
        foodList.clear();
        SQLiteDatabase dba=this.getReadableDatabase();
        Cursor cursor=dba.query(Constants.TABLE_NAME,new String[]{Constants.KEY_ID,Constants.FOOD_NAME,Constants.CALORIES,Constants.DATE_ADDED},
                null,null,null,null,Constants.DATE_ADDED+" DESC ");
        if(cursor.moveToFirst()){
            do{
                Food food=new Food();
                food.setFoodName(cursor.getString((cursor.getColumnIndex(Constants.FOOD_NAME))));
                food.setCalories(cursor.getString(cursor.getColumnIndex(Constants.CALORIES)));
                food.setFoodId(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID)));
                DateFormat dateFormat=DateFormat.getDateInstance();
                String date=dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.DATE_ADDED))).getTime());
                food.setRecordDate(date);
                foodList.add(food);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return foodList;
    }

}
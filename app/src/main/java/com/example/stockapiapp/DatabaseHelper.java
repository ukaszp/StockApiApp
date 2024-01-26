package com.example.stockapiapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "stock_tracker.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "symbols";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_SYMBOL = "symbol";

    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " ("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_SYMBOL + " TEXT)";

    private Context context;


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public long insertRecord(String symbol){

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_SYMBOL, symbol.toUpperCase(Locale.ROOT));

        long id = db.insert(TABLE_NAME, null, values);

        db.close();

        return id;
    }

    public ArrayList<Symbol> getAllRecords(){
        ArrayList<Symbol> stocksList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        if (cursor.moveToFirst()){
            do {
                @SuppressLint("Range") Symbol symbolRecord = new Symbol(
                        "" + cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                        "" + cursor.getString(cursor.getColumnIndex(COLUMN_SYMBOL))
                );
                //add record to list
                stocksList.add(symbolRecord);
            }while (cursor.moveToNext());
        }
        db.close();

        return stocksList;
    }


    public ArrayList<Symbol> searchRecords(String query){

        ArrayList<Symbol> recordsList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " +
                COLUMN_SYMBOL + " LIKE '%" + query + "%'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        if (cursor.moveToFirst()){
            do {
                @SuppressLint("Range") Symbol symbolRecord = new Symbol(
                        "" + cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                        "" + cursor.getString(cursor.getColumnIndex(COLUMN_SYMBOL))
                );
                recordsList.add(symbolRecord);
            }while (cursor.moveToNext());
        }

        db.close();
        return recordsList;
    }
}

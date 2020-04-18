package com.example.shoppinglist.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class ShoppingDbHelper extends SQLiteOpenHelper {
    public ShoppingDbHelper(Context context) {
        super(context, ShoppingContract.DB_NAME, null, ShoppingContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + ShoppingContract.ShoppingEntry.TABLE + " ( " +
                ShoppingContract.ShoppingEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ShoppingContract.ShoppingEntry.COL_TASK_PRICE + " DECIMAL(12,2) NOT NULL, " +
                ShoppingContract.ShoppingEntry.COL_TASK_TITLE + " TEXT NOT NULL);";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ShoppingContract.ShoppingEntry.TABLE);
        onCreate(db);
    }
}

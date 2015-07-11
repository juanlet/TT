package com.example.juan.traspositiontrainer;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.database.sqlite.SQLiteQueryBuilder;


import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by Juan on 11/07/2015.
 */
public class DBManager extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "TraspositionTrainer";
    private static final int DATABASE_VERSION = 1;



    public DBManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    public Cursor getKeys(){


        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("Select * from scales_description", null);

        return c;
    }

}

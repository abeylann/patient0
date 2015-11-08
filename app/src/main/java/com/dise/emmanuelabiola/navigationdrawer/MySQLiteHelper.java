package com.dise.emmanuelabiola.navigationdrawer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by josh on 08/11/2015.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "telhack.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_PATIENT = "patient";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_BLOOD_PRESSURE = "blood_pressure";
    public static final String COLUMN_TEMPERATURE = "temperature";
    public static final String COLUMN_CONDITION_ID = "condition_id";
    public static final String COLUMN_PATIENT_ID = "patient_id";

    private static final String PATIENT_CREATE = "create table "
            + TABLE_PATIENT + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text , "
            + COLUMN_BLOOD_PRESSURE + " integer , "
            + COLUMN_TEMPERATURE + " integer, "
            + COLUMN_CONDITION_ID + " integer, "
            + COLUMN_PATIENT_ID + " integer);";

    public static final String TABLE_CONDITIONS = "conditions";
    public static final String COLUMN_CONDITION_NAME = "condition_name";

    private static final String CONDITIONS_CREATE = "create table "
            + TABLE_CONDITIONS + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_CONDITION_ID + " integer, "
            + COLUMN_CONDITION_NAME + " text);";


    public MySQLiteHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase database)
    {
        database.execSQL(PATIENT_CREATE);
        database.execSQL(CONDITIONS_CREATE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PATIENT);
        onCreate(db);
    }
}

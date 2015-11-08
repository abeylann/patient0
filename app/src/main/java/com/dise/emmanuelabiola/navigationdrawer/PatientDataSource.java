package com.dise.emmanuelabiola.navigationdrawer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by josh on 08/11/2015.
 */
public class PatientDataSource {

    private SQLiteDatabase database;
    private MySQLiteHelper helper;
    private String[] allColumns = {MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_NAME,
            MySQLiteHelper.COLUMN_BLOOD_PRESSURE, MySQLiteHelper.COLUMN_TEMPERATURE, MySQLiteHelper.COLUMN_CONDITION_ID,
            MySQLiteHelper.COLUMN_PATIENT_ID};

    public PatientDataSource(Context context) {
        helper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = helper.getWritableDatabase();
    }

    public void close() {
        helper.close();
    }

    public String newPatient(String name, int pressure, int temperature, int conditionId, int patientId) {
        database.execSQL("delete from " + MySQLiteHelper.TABLE_PATIENT);

        ContentValues cv = new ContentValues();
        cv.put(MySQLiteHelper.COLUMN_NAME, name);
        cv.put(MySQLiteHelper.COLUMN_BLOOD_PRESSURE, pressure);
        cv.put(MySQLiteHelper.COLUMN_TEMPERATURE, temperature);
        cv.put(MySQLiteHelper.COLUMN_CONDITION_ID, conditionId);
        cv.put(MySQLiteHelper.COLUMN_PATIENT_ID, patientId);

        database.insert(MySQLiteHelper.TABLE_PATIENT, null, cv);
        Cursor cursor = getPatient();

        Log.d("Patient:", "" + cursor.getInt(0));
        Log.d("Patient:", cursor.getString(1));

        return cursor.getString(1);
    }

    public Cursor getPatient()
    {
        Cursor cursor = database.rawQuery("select * from " + MySQLiteHelper.TABLE_PATIENT, null);

        cursor.moveToFirst();
        return cursor;
    }

    public int getId() { return getPatient().getInt(0); }

    public String getName()
    {
        return getPatient().getString(1);
    }

    public int getBloodPressure()
    {
        return getPatient().getInt(2);
    }

    public int getTemperature()
    {
        return getPatient().getInt(3);
    }

    public int getConditionId()
    {
        return getPatient().getInt(4);
    }

    public int getConditionFromName(String name)
    {
        Cursor cursor = database.rawQuery("SELECT * FROM " + MySQLiteHelper.TABLE_CONDITIONS + " WHERE " + MySQLiteHelper.COLUMN_CONDITION_NAME + "='" + name + "'", null);

        cursor.moveToFirst();

        return cursor.getInt(1);
    }

    public void deleteAllConditions()
    {
        database.execSQL("delete from " + MySQLiteHelper.TABLE_CONDITIONS);
    }

    public ArrayList<String> getAllConditions()
    {
        Cursor cursor = database.rawQuery("select * from " + MySQLiteHelper.TABLE_CONDITIONS, null);

        ArrayList<String> conditions = new ArrayList<>();
        while(cursor.moveToNext())
        {
            conditions.add(cursor.getString(2));
        }

        return conditions;
    }

    public void addConditionToDatabase(int id, String condition)
    {
        ContentValues cv = new ContentValues();
        cv.put(MySQLiteHelper.COLUMN_CONDITION_ID, id);
        cv.put(MySQLiteHelper.COLUMN_CONDITION_NAME, condition);
        database.insert(MySQLiteHelper.TABLE_CONDITIONS, null, cv);
        Log.d("Inserted","...");
    }


}

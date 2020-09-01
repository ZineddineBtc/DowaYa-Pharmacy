package com.example.dowaya_pharmacy.daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.dowaya_pharmacy.models.Medicine;

import java.util.ArrayList;

public class CreatedMedicineHistoryDAO extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "create_medicine-history.db";
    private static final String CREATE_MEDICINE_HISTORY_TABLE_NAME = "create_medicine-history";
    private static final String CREATE_MEDICINE_HISTORY_ID = "id";
    private static final String CREATE_MEDICINE_HISTORY_NAME = "name";
    private static final String CREATE_MEDICINE_HISTORY_DESCRIPTION = "description";
    private static final String CREATE_MEDICINE_HISTORY_DOSE = "dose";
    private static final String CREATE_MEDICINE_HISTORY_PRICE = "price";
    private static final String CREATE_MEDICINE_HISTORY_TIME = "time";

    public CreatedMedicineHistoryDAO(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table "+ CREATE_MEDICINE_HISTORY_TABLE_NAME +
                        " ("+ CREATE_MEDICINE_HISTORY_ID +" text primary key, " +
                        CREATE_MEDICINE_HISTORY_NAME +" text, "+
                        CREATE_MEDICINE_HISTORY_DESCRIPTION +" text, "+
                        CREATE_MEDICINE_HISTORY_DOSE +" text, "+
                        CREATE_MEDICINE_HISTORY_PRICE +" text, "+
                        CREATE_MEDICINE_HISTORY_TIME +" text)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS "+ CREATE_MEDICINE_HISTORY_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertCreateMedicineHistory(Medicine medicine) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CREATE_MEDICINE_HISTORY_ID, medicine.getId());
        contentValues.put(CREATE_MEDICINE_HISTORY_NAME, medicine.getName());
        contentValues.put(CREATE_MEDICINE_HISTORY_DESCRIPTION, medicine.getDescription());
        contentValues.put(CREATE_MEDICINE_HISTORY_DOSE, medicine.getDose());
        contentValues.put(CREATE_MEDICINE_HISTORY_PRICE, medicine.getPriceRange());
        contentValues.put(CREATE_MEDICINE_HISTORY_TIME, medicine.getSearchHistoryTime());
        db.insert(CREATE_MEDICINE_HISTORY_TABLE_NAME, null, contentValues);
        return true;
    }

    public void deleteCreateMedicineHistory(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CREATE_MEDICINE_HISTORY_TABLE_NAME,
                CREATE_MEDICINE_HISTORY_ID +" = ? ",
                new String[] {id});
    }

    public ArrayList<Medicine> getAllCreateMedicineHistory() {
        ArrayList<Medicine> medicineList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from "+ CREATE_MEDICINE_HISTORY_TABLE_NAME,
                null );
        cursor.moveToLast();
        while(!cursor.isBeforeFirst()){
            Medicine medicine = new Medicine();
            medicine.setId(cursor.getString(cursor.getColumnIndex(CREATE_MEDICINE_HISTORY_ID)));
            medicine.setName(cursor.getString(cursor.getColumnIndex(CREATE_MEDICINE_HISTORY_NAME)));
            medicine.setDescription(
                    cursor.getString(cursor.getColumnIndex(CREATE_MEDICINE_HISTORY_DESCRIPTION)));
            medicine.setDose(
                    cursor.getString(cursor.getColumnIndex(CREATE_MEDICINE_HISTORY_DOSE)));
            medicine.setPriceRange(
                    cursor.getString(cursor.getColumnIndex(CREATE_MEDICINE_HISTORY_PRICE)));
            medicine.setCreatedHistoryTime(
                    cursor.getString(cursor.getColumnIndex(CREATE_MEDICINE_HISTORY_TIME)));
            medicineList.add(medicine);
            cursor.moveToPrevious();
        }
        return medicineList;
    }

    
    
    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CREATE_MEDICINE_HISTORY_TABLE_NAME);
        return numRows;
    }
    
}
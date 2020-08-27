package com.example.dowaya_pharmacy.daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.dowaya_pharmacy.models.Medicine;
import com.example.dowaya_pharmacy.models.User;

import java.util.ArrayList;

public class UserHistoryDAO extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "user_history_0.db";
    private static final String USER_HISTORY_TABLE_NAME = "user_history_0";
    private static final String USER_HISTORY_ID = "id";
    private static final String USER_HISTORY_NAME = "name";
    private static final String USER_HISTORY_TIME = "time";

    public UserHistoryDAO(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table "+ USER_HISTORY_TABLE_NAME +
                        " ("+ USER_HISTORY_ID +" text primary key, " +
                        USER_HISTORY_NAME +" text, "+
                        USER_HISTORY_TIME +" text)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS "+ USER_HISTORY_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertUserHistory(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_HISTORY_ID, user.getId());
        contentValues.put(USER_HISTORY_NAME, user.getUsername());
        contentValues.put(USER_HISTORY_TIME, user.getSearchHistoryTime());
        db.insert(USER_HISTORY_TABLE_NAME, null, contentValues);
        return true;
    }

    public void deleteUserHistory(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(USER_HISTORY_TABLE_NAME,
                USER_HISTORY_ID +" = ? ",
                new String[] {id});
    }

    public ArrayList<User> getAllUserHistory() {
        ArrayList<User> userList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from "+ USER_HISTORY_TABLE_NAME,
                null );
        cursor.moveToLast();
        while(!cursor.isBeforeFirst()){
            User user = new User();
            user.setId(cursor.getString(cursor.getColumnIndex(USER_HISTORY_ID)));
            user.setUsername(cursor.getString(cursor.getColumnIndex(USER_HISTORY_NAME)));
            user.setSearchHistoryTime(
                    cursor.getString(cursor.getColumnIndex(USER_HISTORY_TIME)));
            userList.add(user);
            cursor.moveToPrevious();
        }
        return userList;
    }

    
    
    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, USER_HISTORY_TABLE_NAME);
        return numRows;
    }
    
}
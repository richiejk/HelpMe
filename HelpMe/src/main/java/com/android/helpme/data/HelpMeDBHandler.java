package com.android.helpme.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.android.helpme.common.Finals;
import com.android.helpme.models.ContactModel;

import java.util.ArrayList;

/**
 * Created by richie on 8/19/13.
 */
public class HelpMeDBHandler {
    SQLiteHelper helper;
    Context context;
    SQLiteDatabase db;
    final static String TABLE_CALL_NUMBER="tblUserToBeCalled";
    final static String TABLE_TEXT_NUMBER="tblUserToBeTexted";

    private static class tblUserToBeCalled{
        final static String NUMBER="number";
        final static String NAME="name";
        final static String PRIORITY="priority";
        final static String ID="_id";
    }

    private static class tblUserToBeTexted{
        final static String NUMBER="number";
        final static String NAME="name";
        final static String PRIORITY="priority";
        final static String ID="_id";
    }


    public HelpMeDBHandler(Context context) {
        this.context = context;
        this.helper=new SQLiteHelper(context, Finals.DATABASE_NAME,null,Finals.DB_VERSION);
        this.db=this.helper.getWritableDatabase();
    }

    public ArrayList<ContactModel> fetchContacts(boolean isForCall){
        String selectQuery="";
        ArrayList<ContactModel> list=new ArrayList<ContactModel>();
        Cursor cursor=null;
        if(isForCall){
            selectQuery="SELECT "+tblUserToBeCalled.ID+" , "+tblUserToBeCalled.NAME+" , "+tblUserToBeCalled.NUMBER+" , "+tblUserToBeCalled.PRIORITY+" FROM "+TABLE_CALL_NUMBER;
        }else{
            selectQuery="SELECT "+tblUserToBeTexted.ID+" , "+tblUserToBeTexted.NAME+" , "+tblUserToBeTexted.NUMBER+" , "+tblUserToBeTexted.PRIORITY+" FROM "+TABLE_TEXT_NUMBER;
        }
        cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                list.add(new ContactModel(cursor.getString(2),cursor.getString(1),cursor.getInt(3)));
            }while (cursor.moveToNext());
        }
        return  list;
    }

    public ContactModel fetchContact(boolean isForCall,int priority){
        String selectQuery="";
        ContactModel contact=null;
        Cursor cursor=null;
        if(isForCall){
            selectQuery="SELECT "+tblUserToBeCalled.ID+" , "+tblUserToBeCalled.NAME+" , "+tblUserToBeCalled.NUMBER+" , "+tblUserToBeCalled.PRIORITY+" FROM "+TABLE_CALL_NUMBER+" WHERE "+tblUserToBeCalled.PRIORITY+" ="+priority;
        }else{
            selectQuery="SELECT "+tblUserToBeTexted.ID+" , "+tblUserToBeTexted.NAME+" , "+tblUserToBeTexted.NUMBER+" , "+tblUserToBeTexted.PRIORITY+" FROM "+TABLE_TEXT_NUMBER+" WHERE "+tblUserToBeCalled.PRIORITY+" ="+priority;
        }
        cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                contact=new ContactModel(cursor.getString(2),cursor.getString(1),cursor.getInt(3));
            }while (cursor.moveToNext());
        }
        return  contact;
    }

    public void deleteCallTable(){
        db.delete(TABLE_CALL_NUMBER,null,null);
    }

    public void deleteTextTable(){
        db.delete(TABLE_TEXT_NUMBER,null,null);
    }


    public void deleteCallTablePrimary(){
        db.delete(TABLE_CALL_NUMBER, tblUserToBeCalled.PRIORITY + "=?", new String [] { "1" } );
    }
    public void deleteCallTableSecondary(){
        db.delete(TABLE_CALL_NUMBER, tblUserToBeCalled.PRIORITY + "=?", new String [] { "2" } );
    }

    public void deleteTextTablePrimary(){
        db.delete(TABLE_TEXT_NUMBER, tblUserToBeTexted.PRIORITY + "=?", new String [] { "1" } );
    }
    public void deleteTextTableSecondary(){
        db.delete(TABLE_TEXT_NUMBER, tblUserToBeTexted.PRIORITY + "=?", new String [] { "2" } );
    }

    public void addContact(boolean isForCall,ContactModel contactModels){
        Cursor cursor=null;

        ContentValues values=new ContentValues();
            values.put(tblUserToBeCalled.NAME,contactModels.getName());
            values.put(tblUserToBeCalled.NUMBER,contactModels.getPhone());
            values.put(tblUserToBeCalled.PRIORITY,contactModels.getPriority());

        if(isForCall){
            db.insert(TABLE_CALL_NUMBER,null,values);
        }else{
            db.insert(TABLE_TEXT_NUMBER,null,values);
        }


    }

    public class SQLiteHelper extends SQLiteOpenHelper {

        //Context context;
        public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }




        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            Cursor cursor = null;
            String selectQueryCall = "CREATE  TABLE \""+TABLE_CALL_NUMBER+"\" (\"_id\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE ,  \"number\" TEXT NOT NULL, \"name\" TEXT, \"priority\" INTEGER NOT NULL  )";
            sqLiteDatabase.execSQL(selectQueryCall);

            String selectQueryText = "CREATE  TABLE \""+TABLE_TEXT_NUMBER+"\" (\"_id\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE ,  \"number\" TEXT NOT NULL, \"name\" TEXT, \"priority\" INTEGER NOT NULL  )";
            sqLiteDatabase.execSQL(selectQueryText);
            // Create TIPS table
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

        }
    }
}

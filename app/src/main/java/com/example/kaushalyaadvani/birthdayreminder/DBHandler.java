package com.example.kaushalyaadvani.birthdayreminder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by kaushalyaadvani on 05/07/16.
 */
public class DBHandler
{
    //fields for db table
    public static final String DB_UID="uid";
    public static final String DB_UNAME="name";
    public static final String DB_PHONE="phone";
    public static final String DB_DAY="day";
    public static final String DB_MONTH="month";
    public static final String DB_YEAR="year";
    public static final String DB_PHOTO="photoid";

    //constants for DB_Table

    public static final String DB_NAME="reminders";
    public static final String DB_TABLE="reminders";
    public static final String DB_CREATE="CREATE TABLE "+DB_TABLE+" ("+DB_UID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+DB_UNAME+ " TEXT, "+DB_PHONE+ " TEXT, "+DB_DAY+ " TEXT, "+DB_MONTH+ " TEXT, "+DB_YEAR+ " TEXT, "+DB_PHOTO+ " TEXT)";
    public static final int DB_VERSION=1;

    public SQLiteDatabase db;
    public SQLHelper helper;
    public Context context;


    public DBHandler(Context context)
    {
        this.context=context;
        helper =new SQLHelper();
        db= helper.getWritableDatabase();
    }

    public DBHandler openReadable() throws android.database.sqlite.SQLiteException
    {
        helper=new SQLHelper();
        db=helper.getReadableDatabase();
        return this;
    }

    public void close()
    {
        db.close();
    }

    //class open helper

    public class SQLHelper extends SQLiteOpenHelper
    {

        public SQLHelper()
        {
            super(context,DB_NAME,null,DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DB_CREATE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS "+DB_TABLE);

            Log.d("upgrade","DATABASE TABLE UPGRADED FROM VERSION "+oldVersion+"TO "+newVersion);

            onCreate(db);
        }
    }

    //insertion
    public long addBday(String uname,String phone,String day,String month,String year,String photoid)
    {
        ContentValues cv=new ContentValues();

        cv.put("name",uname);
        cv.put("phone",phone);
        cv.put("day",day);
        cv.put("month",month);
        cv.put("year",year);
        cv.put("photoid",photoid);

        return db.insert(DB_TABLE,null,cv);


    }


    public Cursor retrieve()
    {
        String [] columns={DB_UID, DB_UNAME, DB_PHONE, DB_DAY, DB_MONTH, DB_YEAR, DB_PHOTO};
        Cursor cursor=db.query(DB_TABLE, columns,null,null,null,null,null);
        return cursor;
    }

    public Cursor retrieveUnique(int uid)
    {
        String [] columns={DB_UID, DB_UNAME, DB_PHONE, DB_DAY, DB_MONTH, DB_YEAR, DB_PHOTO};

        Cursor cursor=db.query(DB_TABLE, columns,DB_UID+"="+uid,null,null,null,null); //this will bring the cursor to the given id
        return cursor;
    }

    //Made to find the image in any Activity - Rohit Creation B)
    public Cursor retrieveImageid(String phone)
    {
        String [] columns={DB_UID,DB_PHONE ,DB_PHOTO};

        Cursor cursor=db.query(DB_TABLE, columns,DB_PHONE+"="+phone,null,null,null,null); //this will bring the cursor to the given id
        return cursor;
    }

    public ArrayList<SingleRow> fillList()
    {
        String [] columns={DB_UID, DB_UNAME,DB_PHONE, DB_DAY, DB_MONTH, DB_YEAR, DB_PHOTO};
        Cursor cursor=db.query(DB_TABLE, columns,null,null,null,null,null);
        ArrayList<SingleRow> al=new ArrayList<SingleRow>();
        String str;
        cursor.moveToFirst();
        while(cursor.isAfterLast()==false)
        {
            str=Integer.parseInt(cursor.getString(0))+"-"+cursor.getString(1)+"-"+cursor.getString(2)+"-"+cursor.getString(3)+"-"+cursor.getString(4)+"-"+cursor.getString(5)+"-"+cursor.getString(6);
            String[] s = str.split("-");
            SingleRow si = new SingleRow(Integer.parseInt(s[0]),s[1],s[2],s[3],s[4],s[5],s[6]);
            al.add(si);
            cursor.moveToNext();
        }
        return al;
    }


    public int updateUser(int uid,String uname,String phone,String day,String month,String year,String photoid)
    {
        ContentValues cv =new ContentValues();
        cv.put("name",uname);
        cv.put("phone",phone);
        cv.put("day",day);
        cv.put("month",month);
        cv.put("year",year);
        cv.put("photoid",photoid);


        return db.update(DB_TABLE,cv,DB_UID+"="+uid,null);

    }

    public int deleteUser(int uid)
    {
        return db.delete(DB_TABLE,DB_UID+"="+uid,null);
    }

    public int deleteUser(String phone)
    {
        return db.delete(DB_TABLE,DB_PHONE+"="+phone,null);
    }


    /*public Cursor login(String username,String password)
    {
        Cursor cursor = db.query(DB_TABLE,new String[]{DB_UNAME,DB_PASS},DB_UNAME+ "='"+username+"'and " +DB_PASS+ "='" +password+"'",null,null,null,null);

        return cursor;
    }*/



}

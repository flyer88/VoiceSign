package me.shyboy.mengma.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by foul on 14/11/16.
 */
public class SignDatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    public static final String CREATE_USER =
            "create table User ("
            + "name text,"
            + "sno text,"
            + "password text,"
            + "pid integer,"
            + "access_token text )";
    public static final String CREATE_SIGN =
            "create table Sign ("
            +"id integer,"
            +"started_at text,"
            +"ended_at text,"
            +"description text)";
    public static final String CREATE_DETAIL =
            "create table SignDetail ("
            +"sno text,"
            +"sign_id integer,"
            +"description text,"
            +"name text,"
            +"created_at text,"
            +"state integer )";
    public static final String CREATE_MEMBER =
            "create table members ("
            +"sno text,"
            +"name text,"
            +"pid integer )";
    public static final String DEFAULTDBNAME = "sign.db";

    public SignDatabaseHelper(Context context,String name,SQLiteDatabase.CursorFactory factory,int version)
    {
        super(context,name,factory,version);

        this.context = context;
    }

    public SignDatabaseHelper(Context context,int version)
    {
        super(context,DEFAULTDBNAME,null,version);

        this.context = context;
    }

    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_USER);
        db.execSQL(CREATE_SIGN);
        db.execSQL(CREATE_DETAIL);
        db.execSQL(CREATE_MEMBER);
        Log.i("database create","create Database User");
    }

    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion)
    {
        db.execSQL("drop table if exists User");
        db.execSQL("drop table if exists Sign");
        db.execSQL("drop table if exists SignDetail");
        db.execSQL("drop table if exists members");
        onCreate(db);
    }
}

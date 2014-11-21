package me.shyboy.mengma.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import me.shyboy.mengma.Common.Sign;
import me.shyboy.mengma.Common.SignConfig;
import me.shyboy.mengma.Common.User;

/**
 * Created by foul on 14/11/20.
 */
public class SignHelper {
    private SignDatabaseHelper helper;
    public SignHelper(Context context)
    {
        helper = new SignDatabaseHelper(context, SignConfig.DBVERSION);
    }

    public void update(List<Sign> list)
    {
        delete();
        SQLiteDatabase db = helper.getWritableDatabase();
        for(int i = 0 ; i < list.size(); i++) {
            Sign sign = list.get(i);
            ContentValues values = new ContentValues();
            values.put("id", sign.getId());
            values.put("started_at", sign.getStarted_at());
            values.put("ended_at", sign.getEnded_at());
            values.put("description", sign.getDescription());
            db.insert(SignConfig.TABLESIGN, null, values);
        }
    }

    public List<Sign> getSigns()
    {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(SignConfig.TABLESIGN,null,null,null,null,null,"id desc",null);
        List<Sign> list = new ArrayList<Sign>();
        if(cursor.moveToFirst())
        {
           do {
               list.add(new Sign(cursor.getInt(cursor.getColumnIndex("id")),
                       cursor.getString(cursor.getColumnIndex("started_at")),
                       cursor.getString(cursor.getColumnIndex("ended_at")),
                       cursor.getString(cursor.getColumnIndex("description"))
               ));
           }while(cursor.moveToNext());
            return list;
        }
        return null;
    }
    public void delete()
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(SignConfig.TABLESIGN,"1 = 1",null);
    }
}

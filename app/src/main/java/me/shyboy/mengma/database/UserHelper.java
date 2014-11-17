package me.shyboy.mengma.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import me.shyboy.mengma.Common.SignConfig;
import me.shyboy.mengma.Common.User;

/**
 * Created by foul on 14/11/16.
 */
public class UserHelper {
    private SignDatabaseHelper helper;
    public UserHelper(Context context)
    {
        helper = new SignDatabaseHelper(context, SignConfig.DBVERSION);
    }

    public void update(User user)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(SignConfig.TABLEUSER,"1 = 1",null);
        ContentValues values = new ContentValues();
        values.put("sno",user.getSno());
        values.put("name",user.getName());
        values.put("password",user.getPassword());
        values.put("access_token",user.getAccess_token());
        values.put("pid",user.getPid());
        db.insert(SignConfig.TABLEUSER,null,values);
    }

    public User getUser()
    {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(SignConfig.TABLEUSER,null,null,null,null,null,null);

        if(cursor.moveToFirst())
        {
            return new User(cursor.getString(cursor.getColumnIndex("sno")),
                            cursor.getString(cursor.getColumnIndex("name")),
                            cursor.getString(cursor.getColumnIndex("password")),
                            cursor.getString(cursor.getColumnIndex("access_token")),
                            cursor.getInt(cursor.getColumnIndex("pid"))
            );
        }
        return null;
    }
}

package me.shyboy.mengma.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaMetadata;

import java.util.ArrayList;
import java.util.List;

import me.shyboy.mengma.Common.SignConfig;
import me.shyboy.mengma.Common.SignDetail;
import me.shyboy.mengma.Common.User;

/**
 * Created by foul on 14/11/23.
 */
public class MemberHelper {
    private SignDatabaseHelper helper;
    public MemberHelper(Context context)
    {
        helper = new SignDatabaseHelper(context, SignConfig.DBVERSION);
    }

    public void update(List<User> list)
    {
        delete();
        SQLiteDatabase db = helper.getWritableDatabase();
        for(int i = 0 ; i < list.size(); i++) {
            User user = list.get(i);
            ContentValues values = new ContentValues();
            values.put("sno", user.getSno());
            values.put("name", user.getName());
            values.put("pid", user.getPid());
            db.insert(SignConfig.TABLEMEMBER, null, values);
        }
    }

    public List<User> getMembers()
    {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(SignConfig.TABLEMEMBER,null,null,null,null,null,null,null);
        List<User> list = new ArrayList<User>();
        if(cursor.moveToFirst())
        {
            do {
                list.add(new User(cursor.getString(cursor.getColumnIndex("sno")),
                        cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getInt(cursor.getColumnIndex("pid"))
                        ));
            }while(cursor.moveToNext());
            return list;
        }
        return null;
    }
    public void delete()
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(SignConfig.TABLEMEMBER,"1 = 1",null);
    }
}

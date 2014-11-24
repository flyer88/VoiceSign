package me.shyboy.mengma.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import me.shyboy.mengma.Common.Sign;
import me.shyboy.mengma.Common.SignConfig;
import me.shyboy.mengma.Common.SignDetail;

/**
 * Created by foul on 14/11/21.
 */
public class SignDetailHelper {
    private SignDatabaseHelper helper;
    public SignDetailHelper(Context context)
    {
        helper = new SignDatabaseHelper(context, SignConfig.DBVERSION);
    }

    public void update(List<SignDetail> list)
    {
        delete();
        SQLiteDatabase db = helper.getWritableDatabase();
        for(int i = 0 ; i < list.size(); i++) {
            SignDetail signDetail = list.get(i);
            ContentValues values = new ContentValues();
            values.put("sno", signDetail.getSno());
            values.put("sign_id", signDetail.getSign_id());
            values.put("description", signDetail.getDescription());
            values.put("name", signDetail.getName());
            values.put("created_at",signDetail.getCreated_at());
            values.put("state",signDetail.getState());
            db.insert(SignConfig.TABLEDETAIL, null, values);
        }
    }

    public List<SignDetail> getDetails()
    {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(SignConfig.TABLEDETAIL,null,null,null,null,null,null,null);
        List<SignDetail> list = new ArrayList<SignDetail>();
        if(cursor.moveToFirst())
        {
            do {
                list.add(new SignDetail(cursor.getString(cursor.getColumnIndex("sno")),
                        cursor.getInt(cursor.getColumnIndex("sign_id")),
                        cursor.getString(cursor.getColumnIndex("description")),
                        cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getString(cursor.getColumnIndex("created_at")),
                        cursor.getInt(cursor.getColumnIndex("state"))
                ));
            }while(cursor.moveToNext());
            return list;
        }
        return null;
    }
    public void delete()
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(SignConfig.TABLEDETAIL,"1 = 1",null);
    }
}

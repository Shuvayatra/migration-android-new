package com.taf.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.taf.data.database.dao.DaoMaster;
import com.taf.data.utils.Logger;


public class MyOpenHelper extends DaoMaster.DevOpenHelper {

    public MyOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        if (oldVersion < newVersion) {
            Logger.e("OpenHelper", "Update Schema version: " + Integer.toString(oldVersion) +
                    "->" + Integer.toString(newVersion));
            switch (oldVersion) {
                case 1:

            }
        }
    }


}

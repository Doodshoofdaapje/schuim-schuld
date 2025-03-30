package com.boris.schuimschuld.dataservices;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.boris.schuimschuld.dataservices.contracts.ContractAccounts;
import com.boris.schuimschuld.dataservices.contracts.ContractGroups;
import com.boris.schuimschuld.dataservices.contracts.ContractTransaction;

public class DatabaseService extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "database_schuimschuld.db";

    public DatabaseService(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ContractAccounts.CREATE_TABLE);
        db.execSQL(ContractGroups.CREATE_TABLE);
        db.execSQL(ContractTransaction.CREATE_TABLE);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(ContractAccounts.DROP_TABLE);
        db.execSQL(ContractGroups.DROP_TABLE);
        db.execSQL(ContractTransaction.DROP_TABLE);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}

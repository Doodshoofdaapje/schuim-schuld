package com.boris.schuimschuld.dataservices.managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.boris.schuimschuld.account.Account;
import com.boris.schuimschuld.dataservices.contracts.ContractTransaction;
import com.boris.schuimschuld.dataservices.DatabaseService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TransactionManagerSQL implements ITransactionManager {

    private DatabaseService databaseServce;
    private Context context;

    public TransactionManagerSQL(Context context) {
        this.databaseServce = new DatabaseService(context);
        this.context = context;
    }

    public void create(Account account, double amount) {
        SQLiteDatabase db = databaseServce.getWritableDatabase();

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(currentTime);

        ContentValues values = new ContentValues();
        values.put(ContractTransaction.TransactionEntry.ACCOUNT_UUID, account.getUuid().toString());
        values.put(ContractTransaction.TransactionEntry.AMOUNT, amount);
        values.put(ContractTransaction.TransactionEntry.DATE, formattedDate);

        db.insert(ContractTransaction.TransactionEntry.TABLE_NAME, null, values);
    }

    public int count(Account account) {
        SQLiteDatabase db = databaseServce.getReadableDatabase();

        String QUERY = "SELECT SUM(A." + ContractTransaction.TransactionEntry.AMOUNT + ") AS transaction_count" +
                " FROM " + ContractTransaction.TransactionEntry.TABLE_NAME + " A " +
                " WHERE A." + ContractTransaction.TransactionEntry.ACCOUNT_UUID + " = ?" +
                " GROUP BY A."+ ContractTransaction.TransactionEntry.ACCOUNT_UUID;

        Cursor cursor = db.rawQuery(QUERY, new String[]{account.getUuid().toString()});

        int count = 0;
        if (cursor.moveToNext()) {
            count = cursor.getInt(cursor.getColumnIndexOrThrow("transaction_count"));
        }

        cursor.close();
        return count;
    }
}

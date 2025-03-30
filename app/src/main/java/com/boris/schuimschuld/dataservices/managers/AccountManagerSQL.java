package com.boris.schuimschuld.dataservices.managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.boris.schuimschuld.account.Account;
import com.boris.schuimschuld.account.Group;
import com.boris.schuimschuld.dataservices.DatabaseService;
import com.boris.schuimschuld.dataservices.contracts.ContractAccounts;
import com.boris.schuimschuld.dataservices.contracts.ContractGroups;

import java.util.ArrayList;
import java.util.UUID;

public class AccountManagerSQL implements IAccountManager{

    private DatabaseService databaseServce;
    private Context context;

    public AccountManagerSQL(Context context) {
        this.databaseServce = new DatabaseService(context);
        this.context = context;
    }

    public void create(Account account) {
        SQLiteDatabase db = databaseServce.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ContractAccounts.AccountEntry.UUID, account.getUuid().toString());
        values.put(ContractAccounts.AccountEntry.NAME, account.getName());
        values.put(ContractAccounts.AccountEntry.BALANCE, account.getBalance());

        long newRowId = db.insert(ContractAccounts.AccountEntry.TABLE_NAME, null, values);

        for (Group group : account.getGroups()) {
            values = new ContentValues();
            values.put(ContractGroups.GroupEntry.ACCOUNT_UUID, account.getUuid().toString());
            values.put(ContractGroups.GroupEntry.GROUP, group.toString());
            newRowId = db.insert(ContractGroups.GroupEntry.TABLE_NAME, null, values);
        }

    }

    public Account get(UUID uuid) {
        SQLiteDatabase db = databaseServce.getReadableDatabase();
        String[] SELECT = {
                ContractAccounts.AccountEntry.UUID,
                ContractAccounts.AccountEntry.NAME,
                ContractAccounts.AccountEntry.BALANCE
        };

        String WHERE = ContractAccounts.AccountEntry.UUID + " = ?";
        String[] WHERE_ARGS = { uuid.toString() };

        Cursor cursor = db.query(ContractAccounts.AccountEntry.TABLE_NAME, SELECT, WHERE, WHERE_ARGS, null, null, null);

        cursor.moveToNext();
        String uuidString = cursor.getString(cursor.getColumnIndexOrThrow(ContractAccounts.AccountEntry.UUID));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(ContractAccounts.AccountEntry.NAME));
        Double balance = cursor.getDouble(cursor.getColumnIndexOrThrow(ContractAccounts.AccountEntry.BALANCE));
        cursor.close();

        String[] SELECT_GROUP = {ContractGroups.GroupEntry.GROUP};
        String WHERE_GROUP = ContractGroups.GroupEntry.ACCOUNT_UUID + " = ?";
        String[] WHERE_GROUP_ARGS = { uuid.toString() };

        ArrayList<Group> groups = new ArrayList<>();
        cursor = db.query(ContractGroups.GroupEntry.TABLE_NAME, SELECT_GROUP, WHERE_GROUP, WHERE_GROUP_ARGS, null, null, null);
        while (cursor.moveToNext()) {
            String group = cursor.getString(cursor.getColumnIndexOrThrow(ContractGroups.GroupEntry.GROUP));
            groups.add(Group.valueOf(group));
        }
        cursor.close();

        return new Account(context, UUID.fromString(uuidString), name, balance, 0.0, groups);
    }

    public ArrayList<Account> getAll(){
        SQLiteDatabase db = databaseServce.getReadableDatabase();

        String QUERY = "SELECT A." + ContractAccounts.AccountEntry.UUID + ", A." + ContractAccounts.AccountEntry.NAME + ", A." + ContractAccounts.AccountEntry.BALANCE + ", B." + ContractGroups.GroupEntry.GROUP +
                " FROM " + ContractAccounts.AccountEntry.TABLE_NAME + " A INNER JOIN " + ContractGroups.GroupEntry.TABLE_NAME + " B" +
                " ON A." + ContractAccounts.AccountEntry.UUID + " = B." + ContractGroups.GroupEntry.ACCOUNT_UUID +
                " ORDER BY A."+ ContractAccounts.AccountEntry.UUID;

        Cursor cursor = db.rawQuery(QUERY, null);

        ArrayList<Account> accounts = new ArrayList<>();

        ArrayList<Group> groups = new ArrayList<>();
        String name = "";
        Double balance = 0.0;
        UUID uuid = null;

        while (cursor.moveToNext()) {
            String uuidString = cursor.getString(cursor.getColumnIndexOrThrow(ContractAccounts.AccountEntry.UUID));
            UUID nUuid = UUID.fromString(uuidString);

            if (!nUuid.equals(uuid) && uuid != null) {
                accounts.add(new Account(context, uuid, name, balance, 0.0, groups));
                groups = new ArrayList<>();
            }

            String group = cursor.getString(cursor.getColumnIndexOrThrow(ContractGroups.GroupEntry.GROUP));
            groups.add(Group.valueOf(group));
            name = cursor.getString(cursor.getColumnIndexOrThrow(ContractAccounts.AccountEntry.NAME));
            balance = cursor.getDouble(cursor.getColumnIndexOrThrow(ContractAccounts.AccountEntry.BALANCE));
            uuid = nUuid;
        }
        if (uuid != null) {
            accounts.add(new Account(context, uuid, name, balance, 0.0, groups)); // Final account
        }
        cursor.close();
        return accounts;
    }

    public void update(Account account){
        SQLiteDatabase db = databaseServce.getWritableDatabase();

        String WHERE = ContractAccounts.AccountEntry.UUID + " = ?";
        String[] WHERE_ARGS = { account.getUuid().toString() };

        ContentValues values = new ContentValues();
        values.put(ContractAccounts.AccountEntry.UUID, account.getUuid().toString());
        values.put(ContractAccounts.AccountEntry.NAME, account.getName());
        values.put(ContractAccounts.AccountEntry.BALANCE, account.getBalance());

        int newRowId = db.update(ContractAccounts.AccountEntry.TABLE_NAME, values, WHERE, WHERE_ARGS);

        // Delete old groups
        WHERE = ContractGroups.GroupEntry.ACCOUNT_UUID + " = ?";
        WHERE_ARGS = new String[]{account.getUuid().toString()};
        int deletedRows = db.delete(ContractGroups.GroupEntry.TABLE_NAME, WHERE, WHERE_ARGS);

        // Create new groups
        for (Group group : account.getGroups()) {
            values = new ContentValues();
            values.put(ContractGroups.GroupEntry.ACCOUNT_UUID, account.getUuid().toString());
            values.put(ContractGroups.GroupEntry.GROUP, group.toString());
            long newRow = db.insert(ContractGroups.GroupEntry.TABLE_NAME, null, values);
        }
    }

    public void delete(Account account){
        SQLiteDatabase db = databaseServce.getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys=ON;");

        String WHERE = ContractAccounts.AccountEntry.UUID + " = ?";
        String[] WHERE_ARGS = { account.getUuid().toString() };
        int deletedRows = db.delete(ContractAccounts.AccountEntry.TABLE_NAME, WHERE, WHERE_ARGS);
    }

    public int size(){
        SQLiteDatabase db = databaseServce.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, ContractAccounts.AccountEntry.TABLE_NAME);
    }

    public void close() {
        databaseServce.close();
    }
}

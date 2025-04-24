package com.boris.schuimschuld.dataservices.contracts;

import android.provider.BaseColumns;

public final class ContractGroups {
    private ContractGroups() {}

    /* Inner class that defines the table contents */
    public static class GroupEntry implements BaseColumns {
        public static final String TABLE_NAME = "groups";
        public static final String ACCOUNT_UUID = "account_uuid";
        public static final String GROUP = "group_name";
    }

    public static final String CREATE_TABLE =
            "CREATE TABLE " + GroupEntry.TABLE_NAME + " (" +
                    GroupEntry._ID + " INTEGER PRIMARY KEY, " +
                    GroupEntry.ACCOUNT_UUID + " TEXT, " +
                    GroupEntry.GROUP + " TEXT, " +
                    "FOREIGN KEY(" + GroupEntry.ACCOUNT_UUID + ") REFERENCES " +
                    ContractAccounts.AccountEntry.TABLE_NAME + "(" + ContractAccounts.AccountEntry.UUID + ") " +
                    "ON DELETE CASCADE)";

    public static final String DROP_TABLE =
            "DROP TABLE IF EXISTS " + GroupEntry.TABLE_NAME;

}
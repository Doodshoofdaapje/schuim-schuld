package com.boris.schuimschuld.dataservices;

import android.content.Context;

import com.boris.schuimschuld.R;
import com.boris.schuimschuld.account.Account;
import com.boris.schuimschuld.serializers.AccountListSerializer;
import com.boris.schuimschuld.util.JsonFileHandler;

import org.json.simple.JSONObject;

import java.util.ArrayList;

public class AccountListDataService {
    private Context context;
    private final String ACCOUNTS_FILE_NAME = "accounts.json";

    public AccountListDataService(Context context) {
        this.context = context;
    }

    public void save(ArrayList<Account> accounts) {
        JsonFileHandler writerJson = new JsonFileHandler(context);
        writerJson.writeFile(ACCOUNTS_FILE_NAME, AccountListSerializer.toJson(accounts));
    }

    public ArrayList<Account> load() {
        JsonFileHandler writer = new JsonFileHandler(context);
        if (writer.createFile(ACCOUNTS_FILE_NAME)) {
            writer.copyFromRaw(ACCOUNTS_FILE_NAME, R.raw.config, "accounts");
        }
        JSONObject accountsAsJson = writer.readFileFromInternal(ACCOUNTS_FILE_NAME);
        ArrayList<Account> list = AccountListSerializer.fromJson(context, accountsAsJson);
        return list;
    }
}

package com.boris.schuimschuld.dataservices;

import android.content.Context;

import com.boris.schuimschuld.R;
import com.boris.schuimschuld.account.Account;
import com.boris.schuimschuld.account.AccountRegister;
import com.boris.schuimschuld.serializers.AccountRegisterSerializer;
import com.boris.schuimschuld.util.JsonFileHandler;

import org.json.simple.JSONObject;

import java.util.ArrayList;

public class AccountRegisterDataService {
    private Context context;
    private final String ACCOUNTS_FILE_NAME = "accounts.json";

    public AccountRegisterDataService(Context context) {
        this.context = context;
    }

    public void save(AccountRegister accountRegister) {
        JsonFileHandler writerJson = new JsonFileHandler(context);
        writerJson.writeFile(ACCOUNTS_FILE_NAME, AccountRegisterSerializer.toJson(accountRegister));
    }

    public ArrayList<Account> load() {
        JsonFileHandler writer = new JsonFileHandler(context);
        if (writer.createFile(ACCOUNTS_FILE_NAME)) {
            writer.copyFromRaw(ACCOUNTS_FILE_NAME, R.raw.config, "accounts");
        }
        JSONObject accountsAsJson = writer.readFileFromInternal(ACCOUNTS_FILE_NAME);
        ArrayList<Account> register = AccountRegisterSerializer.fromJson(context, accountsAsJson);
        return register;
    }
}

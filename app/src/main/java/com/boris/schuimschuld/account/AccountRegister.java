package com.boris.schuimschuld.account;

import android.content.Context;

import com.boris.schuimschuld.R;
import com.boris.schuimschuld.serializers.AccountRegisterSerializer;
import com.boris.schuimschuld.util.JsonWriterReader;

import org.json.simple.JSONObject;

import java.util.ArrayList;

public class AccountRegister {
    private ArrayList<Account> register;
    private Context context;

    public AccountRegister(Context context) {
        this.context = context;
        JsonWriterReader writer = new JsonWriterReader(context);
        if (writer.createFile("accounts.json")) {
            writer.copyFromRaw("accounts.json", R.raw.config, "accounts");
        }
        JSONObject accountsAsJson = writer.readFileFromInternal("accounts.json");
        register = AccountRegisterSerializer.fromJson(context, accountsAsJson);
    }

    public void register(Account account) {
        register.add(account);
        save();
    }

    public void deregister(Account account) {
        if (register.contains(account)) {
            register.remove(account);
            account.delete(context);
            save();
        }
    }

    public Integer size() {
        return register.size();
    }

    public boolean has(Account account) {
        return register.contains(account);
    }

    public ArrayList<Account> getAccounts() {
        return new ArrayList<>(this.register);
    }

    public void save() {
        JsonWriterReader writerJson = new JsonWriterReader(context);
        writerJson.writeFile("accounts.json", AccountRegisterSerializer.toJson(this));
    }
}

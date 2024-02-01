package com.boris.bier.account;

import android.content.Context;

import com.boris.bier.R;
import com.boris.bier.util.ImageWriterReader;
import com.boris.bier.util.JsonWriterReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class AccountRegister {
    private ArrayList<Account> register;
    private Context context;

    public AccountRegister(Context context) {
        register = new ArrayList<>();
        this.context = context;
        JsonWriterReader writer = new JsonWriterReader(context);
        if (writer.createFile("accounts.json")) {
            writer.copyFromRaw("accounts.json", R.raw.config, "accounts");
        }
        JSONObject accountsAsJson = writer.readFileFromInternal("accounts.json");
        loadFromJson(accountsAsJson);
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

    public boolean has(Account account) {
        return register.contains(account);
    }

    public ArrayList<Account> getAccounts() {
        return this.register;
    }

    public void save() {
        JsonWriterReader writerJson = new JsonWriterReader(context);
        writerJson.writeFile("accounts.json", toJson());
    }

    public JSONObject toJson() {
        JSONObject container = new JSONObject();
        JSONArray accounts = new JSONArray();
        for (Account account : register) {
            accounts.add(account.toJson());
        }
        container.put("accounts", accounts);
        return container;
    }

    public void loadFromJson(JSONObject accountsContainer) throws NullPointerException {
        if (accountsContainer == null) {
            throw new NullPointerException("Json accounts returned null");
        }

        register = new ArrayList<>();
        JSONArray accounts = (JSONArray) accountsContainer.get("accounts");
        for (Object account : accounts) {
            JSONObject accountAsJson = (JSONObject) account;
            String name = (String) accountAsJson.get("name");
            Double balance = (Double) accountAsJson.get("balance");
            AgeGroup group = AgeGroup.valueOf((String) accountAsJson.get("group"));
            register.add(new Account(name, balance, group, context));
        }

    }
}

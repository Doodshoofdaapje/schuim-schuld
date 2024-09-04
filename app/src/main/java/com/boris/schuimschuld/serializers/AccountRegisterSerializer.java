package com.boris.schuimschuld.serializers;

import android.content.Context;
import android.util.Log;

import com.boris.schuimschuld.account.Account;
import com.boris.schuimschuld.account.AccountRegister;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class AccountRegisterSerializer {

    private static final String ACCOUNTS_KEY = "accounts";

    public static JSONObject toJson(AccountRegister register) {
        JSONObject container = new JSONObject();
        JSONArray accounts = new JSONArray();

        for (Account account : register.getAccounts()) {
            accounts.add(AccountSerializer.toJson(account));
        }
        container.put(ACCOUNTS_KEY, accounts);

        return container;
    }

    public static ArrayList<Account> fromJson(Context context, JSONObject accountsContainer) throws NullPointerException {
        if (accountsContainer == null) {
            throw new NullPointerException("Json accounts returned null");
        }

        try {
            JSONArray accounts = (JSONArray) accountsContainer.get(ACCOUNTS_KEY);
            ArrayList<Account> register = new ArrayList<>();

            for (Object accountObject : accounts) {
                Account account = AccountSerializer.fromJson(context, (JSONObject) accountObject);
                register.add(account);
            }

            return register;
        } catch (ClassCastException | NullPointerException e) {
            e.printStackTrace();
            Log.e("AccountRegisterSerializer", e.getMessage());
            return new ArrayList<>();
        } catch (AccountParseException e) {
            e.printStackTrace();
            Log.e("AccountRegisterSerializer", e.getMessage());
            return new ArrayList<>();
        }
    }
}

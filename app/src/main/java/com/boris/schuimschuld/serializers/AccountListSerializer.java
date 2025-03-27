package com.boris.schuimschuld.serializers;

import android.content.Context;
import android.util.Log;

import com.boris.schuimschuld.account.Account;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class AccountListSerializer {

    private static final String ACCOUNTS_KEY = "accounts";

    public static JSONObject toJson(ArrayList<Account> accounts) {
        JSONObject container = new JSONObject();
        JSONArray accountsArray = new JSONArray();

        for (Account account : accounts) {
            accountsArray.add(AccountSerializer.toJson(account));
        }
        container.put(ACCOUNTS_KEY, accountsArray);

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
            Log.e("AccountSerializer", e.getMessage());
            return new ArrayList<>();
        } catch (AccountParseException e) {
            e.printStackTrace();
            Log.e("AccountSerializer", e.getMessage());
            return new ArrayList<>();
        }
    }
}

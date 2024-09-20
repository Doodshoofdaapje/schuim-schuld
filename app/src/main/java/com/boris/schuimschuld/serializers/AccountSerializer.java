package com.boris.schuimschuld.serializers;

import android.content.Context;
import android.util.Log;

import com.boris.schuimschuld.account.Account;
import com.boris.schuimschuld.account.Group;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Optional;

public class AccountSerializer {

    private static final String NAME_KEY = "name";
    private static final String BALANCE_KEY = "balance";
    private static final String CONSUMPTION_COUNT_KEY = "consumptionCount";
    private static final String GROUPS_KEY = "groups";

    public static JSONObject toJson(Account account) {
        JSONObject accountDetails = new JSONObject();

        accountDetails.put(NAME_KEY, account.getName());
        accountDetails.put(BALANCE_KEY, account.getBalance());
        accountDetails.put(CONSUMPTION_COUNT_KEY, account.getConsumptionCount());

        JSONArray groupsArray = new JSONArray();
        for (Group group : account.getGroups()) {
            groupsArray.add(group.toString());
        }
        accountDetails.put(GROUPS_KEY, groupsArray);

        return accountDetails;
    }

    public static Account fromJson(Context context, JSONObject accountAsJson) throws AccountParseException {
        try {
            String name = (String) accountAsJson.getOrDefault(NAME_KEY, "");
            Double balance = (Double) accountAsJson.getOrDefault(BALANCE_KEY, 0.0);
            Double consumptionCount = (Double) accountAsJson.getOrDefault(CONSUMPTION_COUNT_KEY, 0.0);

            JSONArray groupsAsJson = (JSONArray) accountAsJson.get(GROUPS_KEY);
            ArrayList<Group> groups = new ArrayList<>();

            if (groupsAsJson != null) {
                for (Object group : groupsAsJson) {
                    groups.add(Group.valueOf((String) group));
                }
            }

            return new Account(context, name, balance, consumptionCount, groups);
        } catch (ClassCastException | NullPointerException e) {
            e.printStackTrace();
            Log.e("AccountRegisterSerializer", e.getMessage());
            throw new AccountParseException("Error parsing account JSON", e);
        }
    }
}

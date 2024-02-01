package com.boris.bier.util;

import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.boris.bier.R;
import com.boris.bier.account.AgeGroup;

import java.util.ArrayList;
import java.util.Collection;

public class ageGroupSpinner {

    public static ArrayList<String> fillSpinner(Spinner groupSpinner, Activity activity, ArrayList<String> groupList) {
        for (AgeGroup group : AgeGroup.values()) {
            groupList.add(group.toString());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(activity, R.layout.custom_spinner_simple, groupList);
        arrayAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_simple);
        groupSpinner.setAdapter(arrayAdapter);
        return groupList;
    }

    public static ArrayList<String> fillSpinner(Spinner groupSpinner, Activity activity) {
        return fillSpinner(groupSpinner, activity, new ArrayList<>());
    }
}

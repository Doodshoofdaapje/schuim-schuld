package com.boris.schuimschuld.util;

import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.boris.schuimschuld.R;
import com.boris.schuimschuld.account.AgeGroup;

import java.util.ArrayList;

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

package com.boris.schuimschuld.util;

import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.boris.schuimschuld.R;
import com.boris.schuimschuld.account.AgeGroup;

import java.util.ArrayList;

public class DynamicSpinnerFiller {

    public static ArrayList<String> fill(Spinner spinner, Activity activity, ArrayList<String> items) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(activity, R.layout.custom_spinner_simple, items);
        arrayAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_simple);
        spinner.setAdapter(arrayAdapter);
        return items;
    }

    public static ArrayList<String> fill(Spinner groupSpinner, Activity activity) {
        return fill(groupSpinner, activity, new ArrayList<>());
    }
}

package com.boris.schuimschuld.util;

import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.boris.schuimschuld.R;
import com.boris.schuimschuld.account.AgeGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DynamicSpinnerFiller {


    public static List<String> fill(Spinner spinner, Activity activity, List<String> items) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(activity, R.layout.custom_spinner_simple, items);
        arrayAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_simple);
        spinner.setAdapter(arrayAdapter);
        return items;
    }

    public static List<String> fill(Spinner spinner, Activity activity) {
        return fill(spinner, activity, new ArrayList<>());
    }

    public static List<String> age(Spinner spinner, Activity activity, List<String> items) {
        // Fill group selector
        for (AgeGroup group : AgeGroup.values()) {
            items.add(group.toString());
        }
        return fill(spinner, activity, items);
    }

    public static List<String> age(Spinner spinner, Activity activity) {
        return age(spinner, activity, new ArrayList<>());
    }

}

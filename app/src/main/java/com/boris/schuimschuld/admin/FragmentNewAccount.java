package com.boris.schuimschuld.admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.boris.schuimschuld.MainActivity;
import com.boris.schuimschuld.R;
import com.boris.schuimschuld.account.Account;
import com.boris.schuimschuld.account.Group;
import com.boris.schuimschuld.accountoverview.GroupTag;
import com.boris.schuimschuld.util.DynamicSpinnerFiller;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;

public class FragmentNewAccount extends Fragment {

    private com.boris.schuimschuld.databinding.FragmentAccountNewBinding binding;
    private MainActivity mainActivity;
    private ArrayList<Group> selectedGroups;
    private FlexboxLayout flexboxLayout;
    private Spinner groupSpinner;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = com.boris.schuimschuld.databinding.FragmentAccountNewBinding.inflate(inflater, container, false);
        mainActivity = (MainActivity) getActivity();
        selectedGroups = new ArrayList<>();
        flexboxLayout = binding.flexTagsAdd;
        groupSpinner = binding.spinnerGroupAdd;

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Populate spinner
        DynamicSpinnerFiller.age(groupSpinner, getActivity());

        // Event add button
        binding.buttonAdd.setOnClickListener(view1 -> addActionPerformed(view1));

        // Event cancel button
        binding.buttonCancel.setOnClickListener(view1 -> NavHostFragment.findNavController(FragmentNewAccount.this).popBackStack());

        // Event item selected in the spinner dropdown
        groupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectSpinnerItem(parent, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void selectSpinnerItem(AdapterView<?> parent, int position) {
        // Get selected item
        String selectedItem = (String) parent.getItemAtPosition(position);
        Group selectedGroup = Group.valueOf(selectedItem);

        // Check if already selected
        if (selectedGroups.contains(selectedGroup)) {
            return;
        }

        // Store selection
        selectedGroups.add(selectedGroup);

        // Create UI component
        flexboxLayout.addView(createGroupTag(selectedGroup));
    }

    private GroupTag createGroupTag(Group group) {
        GroupTag tag = new GroupTag(getContext(), group);
        Button deleteButton = (Button) tag.findViewById(R.id.tagGroupDeleteButton);
        deleteButton.setOnClickListener(v -> {
            // Remove selection
            Group selectedGroup = tag.getGroup();
            selectedGroups.remove(selectedGroup);

            // Remove from UI
            flexboxLayout.removeView(tag);
        });
        return tag;
    }

    private void addActionPerformed(View view) {
        try {
            String name = binding.textInputNameAdd.getText().toString();
            Double newBalance =  Double.parseDouble(binding.textInputBalanceAdd.getText().toString());

            if (name.isEmpty()) {
                Snackbar errorMessage = Snackbar.make(view, "Geen naam ingevult lul", BaseTransientBottomBar.LENGTH_LONG);
                errorMessage.show();
                return;
            }

            Account newAccount = new Account(name, newBalance, selectedGroups, getContext());
            mainActivity.accountRegister.register(newAccount);
            NavHostFragment.findNavController(FragmentNewAccount.this).popBackStack();
        } catch (NumberFormatException exception) {
            Snackbar errorMessage = Snackbar.make(view, "Bas vul a.u.b. alleen getallen in", BaseTransientBottomBar.LENGTH_LONG);
            errorMessage.show();
        }
    }
}
package com.boris.schuimschuld.adminfragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

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

public class FragmentEditAccount extends Fragment {

    private com.boris.schuimschuld.databinding.FragmentAccountEditBinding binding;
    private FlexboxLayout flexboxLayout;

    private ArrayList<Group> selectedGroups;
    private final String bundleKey = "ACCOUNT_DETAILS";

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = com.boris.schuimschuld.databinding.FragmentAccountEditBinding.inflate(inflater, container, false);
        flexboxLayout = binding.flexTagsEdit;

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = this.getArguments();
        Account account = (Account) bundle.getSerializable(bundleKey);
        selectedGroups = account.getGroups();

        // Populate UI
        DynamicSpinnerFiller.age(binding.spinnerGroupEdit, getActivity());
        binding.textInputNameEdit.setText(account.getName());
        binding.textInputBalanceEdit.setText(account.getBalance().toString());
        binding.textInputLifetime.setText(account.getConsumptionCount().toString());
        for (Group group : selectedGroups) {
            flexboxLayout.addView(createGroupTag(group));
        }

        // Event handlers
        binding.buttonChangeEdit.setOnClickListener(view1 -> editActionPerformed(account, view1));

        binding.buttonCancelEdit.setOnClickListener(view1 -> NavHostFragment.findNavController(FragmentEditAccount.this).popBackStack());

        binding.spinnerGroupEdit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    private void editActionPerformed(Account account, View view) {
        try {
            String name = binding.textInputNameEdit.getText().toString();
            Double newBalance =  Double.parseDouble(binding.textInputBalanceEdit.getText().toString());
            Double newConsumptionCount =  Double.parseDouble(binding.textInputLifetime.getText().toString());

            if (name.isEmpty()) {
                Snackbar errorMessage = Snackbar.make(view, "Geen naam ingevult", BaseTransientBottomBar.LENGTH_LONG);
                errorMessage.show();
                return;
            }

            if (selectedGroups.isEmpty()) {
                Snackbar errorMessage = Snackbar.make(view, "Geen groep geselecteerd", BaseTransientBottomBar.LENGTH_LONG);
                errorMessage.show();
                return;
            }

            account.setName(name);
            account.setBalance(newBalance);
            account.setConsumptionCount(newConsumptionCount);
            account.setGroups(selectedGroups);
            ((MainActivity) getActivity()).accountRegister.save();
            NavHostFragment.findNavController(FragmentEditAccount.this).popBackStack();

        } catch (NumberFormatException exception) {
            Snackbar errorMessage = Snackbar.make(view, "Vul a.u.b. alleen getallen in", BaseTransientBottomBar.LENGTH_LONG);
            errorMessage.show();
        }
    }
}
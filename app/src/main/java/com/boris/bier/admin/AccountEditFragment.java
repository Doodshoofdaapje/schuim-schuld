package com.boris.bier.admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.boris.bier.MainActivity;
import com.boris.bier.R;
import com.boris.bier.account.Account;
import com.boris.bier.account.AgeGroup;
import com.boris.bier.util.ageGroupSpinner;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class AccountEditFragment extends Fragment {

    private com.boris.bier.databinding.FragmentAccountEditBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = com.boris.bier.databinding.FragmentAccountEditBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity mainActivity = (MainActivity) getActivity();

        ArrayList<String> groupList = ageGroupSpinner.fillSpinner(binding.spinnerGroupEdit, getActivity());
        Bundle bundle = this.getArguments();
        Account account = (Account) bundle.getSerializable("ACCOUNT_DETAILS");

        binding.textInputNameEdit.setText(account.getName());
        binding.textInputBalanceEdit.setText(account.getBalance().toString());
        binding.spinnerGroupEdit.setSelection(groupList.indexOf(account.getGroup().toString()));

        binding.buttonChangeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editActionPerformed(account, view);
            }
        });

        binding.buttonCancelEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(AccountEditFragment.this).popBackStack();
            }
        });
    }

    private void editActionPerformed(Account account, View view) {
        try {
            String name = binding.textInputNameEdit.getText().toString();
            Double newBalance =  Double.parseDouble(binding.textInputBalanceEdit.getText().toString());
            AgeGroup group = AgeGroup.valueOf((String) binding.spinnerGroupEdit.getSelectedItem());

            if (!name.isEmpty()) {
                account.setName(name);
                account.setBalance(newBalance);
                account.setGroup(group);
                ((MainActivity) getActivity()).accountRegister.save();
                NavHostFragment.findNavController(AccountEditFragment.this).popBackStack();
            } else {
                Snackbar errorMessage = Snackbar.make(view, "Geen naam ingevult lul", BaseTransientBottomBar.LENGTH_LONG);
                errorMessage.show();
            }
        } catch (NumberFormatException exception) {
            Snackbar errorMessage = Snackbar.make(view, "Bas vul a.u.b. alleen getallen in", BaseTransientBottomBar.LENGTH_LONG);
            errorMessage.show();
        }
    }
}
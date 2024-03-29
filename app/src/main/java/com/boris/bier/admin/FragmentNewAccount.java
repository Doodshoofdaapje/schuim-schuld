package com.boris.bier.admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.boris.bier.MainActivity;
import com.boris.bier.R;
import com.boris.bier.account.Account;
import com.boris.bier.account.AgeGroup;
import com.boris.bier.util.ageGroupSpinner;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class FragmentNewAccount extends Fragment {

    private com.boris.bier.databinding.FragmentAccountNewBinding binding;
    private MainActivity mainActivity;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = com.boris.bier.databinding.FragmentAccountNewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainActivity = (MainActivity) getActivity();

        ageGroupSpinner.fillSpinner(binding.spinnerGroupAdd, getActivity());

        binding.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addActionPerformed(view);
            }
        });

        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FragmentNewAccount.this).popBackStack();
            }
        });
    }

    private void addActionPerformed(View view) {
        try {
            String name = binding.textInputNameAdd.getText().toString();
            Double newBalance =  Double.parseDouble(binding.textInputBalanceAdd.getText().toString());
            String group = (String) binding.spinnerGroupAdd.getSelectedItem();

            if (name.isEmpty()) {
                Snackbar errorMessage = Snackbar.make(view, "Geen naam ingevult lul", BaseTransientBottomBar.LENGTH_LONG);
                errorMessage.show();
                return;
            }

            Account newAccount = new Account(name, newBalance, AgeGroup.valueOf(group), getContext());
            mainActivity.accountRegister.register(newAccount);
            NavHostFragment.findNavController(FragmentNewAccount.this).popBackStack();
        } catch (NumberFormatException exception) {
            Snackbar errorMessage = Snackbar.make(view, "Bas vul a.u.b. alleen getallen in", BaseTransientBottomBar.LENGTH_LONG);
            errorMessage.show();
        }
    }
}
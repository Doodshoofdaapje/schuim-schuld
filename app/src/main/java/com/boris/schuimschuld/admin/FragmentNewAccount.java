package com.boris.schuimschuld.admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boris.schuimschuld.MainActivity;
import com.boris.schuimschuld.account.Account;
import com.boris.schuimschuld.account.Group;
import com.boris.schuimschuld.util.DynamicSpinnerFiller;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;

public class FragmentNewAccount extends Fragment {

    private com.boris.schuimschuld.databinding.FragmentAccountNewBinding binding;
    private MainActivity mainActivity;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = com.boris.schuimschuld.databinding.FragmentAccountNewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainActivity = (MainActivity) getActivity();

        DynamicSpinnerFiller.age(binding.spinnerGroupAdd, getActivity());

        binding.buttonAdd.setOnClickListener(view1 -> addActionPerformed(view1));

        binding.buttonCancel.setOnClickListener(view1 -> NavHostFragment.findNavController(FragmentNewAccount.this).popBackStack());
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

            Account newAccount = new Account(name, newBalance, new ArrayList<>(Arrays.asList(Group.valueOf(group))), getContext());
            mainActivity.accountRegister.register(newAccount);
            NavHostFragment.findNavController(FragmentNewAccount.this).popBackStack();
        } catch (NumberFormatException exception) {
            Snackbar errorMessage = Snackbar.make(view, "Bas vul a.u.b. alleen getallen in", BaseTransientBottomBar.LENGTH_LONG);
            errorMessage.show();
        }
    }
}
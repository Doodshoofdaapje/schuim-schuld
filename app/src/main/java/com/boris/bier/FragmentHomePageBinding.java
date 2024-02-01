package com.boris.bier;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.boris.bier.account.Account;
import com.boris.bier.account.AgeGroup;
import com.boris.bier.accountoverview.AccountCard;
import com.boris.bier.accountoverview.AccountOverviewHome;
import com.boris.bier.accountoverview.IOnBackPressed;
import com.boris.bier.util.ageGroupSpinner;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;

public class FragmentHomePageBinding extends Fragment implements IOnBackPressed {

    private com.boris.bier.databinding.FragmentHomePageBinding binding;
    private AccountOverviewHome accountOverview;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        binding = com.boris.bier.databinding.FragmentHomePageBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity mainActivity = (MainActivity) getActivity();
        TableLayout layout = (TableLayout) view.findViewById(R.id.layoutAccountsHome);
        ageGroupSpinner.fillSpinner(binding.spinnerGroupHome, getActivity(), new ArrayList<>(Arrays.asList("Iedereen")));

        accountOverview = new AccountOverviewHome(getContext(), mainActivity, this);
        accountOverview.fillLayout(layout);

        binding.payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAlert(mainActivity, view);
            }
        });

        binding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FragmentHomePageBinding.this)
                        .navigate(R.id.action_fragmentHomePageBinding2_to_fragmentLogIn);
            }
        });

        binding.spinnerGroupHome.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                onSelectionActionPerformed(layout, mainActivity);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
    }

    @Override
    public boolean onBackPressed() {
        return accountOverview.exitSelectionMode();
    }

    private void onSelectionActionPerformed(TableLayout layout, MainActivity mainActivity ) {
        String selectedGroupAsString = (String) binding.spinnerGroupHome.getSelectedItem();
        ArrayList<Account> filterdAccounts = new ArrayList<>();
        if (selectedGroupAsString.equals("Iedereen")) {
            accountOverview.fillLayout(layout);
            return;
        }

        AgeGroup selectedGroup = AgeGroup.valueOf(selectedGroupAsString);
        for (Account account : mainActivity.accountRegister.getAccounts()) {
            if (account.getGroup() == selectedGroup) {
                filterdAccounts.add(account);
            }
        }
        accountOverview.fillLayout(layout, filterdAccounts);
    }

    private void chargeSelectedAccounts(MainActivity activity) {
        TableLayout layout = (TableLayout) activity.findViewById(R.id.layoutAccountsHome);
        for (int i = 0; i < layout.getChildCount(); i++) {
            TableRow tableRow = (TableRow) layout.getChildAt(i);
            for (int j = 0; j < tableRow.getChildCount(); j++) {
                if (tableRow.getChildAt(j) instanceof AccountCard) {
                    AccountCard accountCard = (AccountCard) tableRow.getChildAt(j);
                    if (accountCard.isSelected()) {
                        accountCard.getAccount().pay();
                        activity.accountRegister.save();
                        accountCard.setSelected(false);
                    }
                }
            }
        }
        accountOverview.exitSelectionMode();
    }

    private void createAlert(MainActivity activity, View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Bevestig");
        builder.setMessage("Weet je zeker dat je door wilt gaan?");

        builder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                chargeSelectedAccounts(activity);
                Snackbar message = Snackbar.make(view, "Betaling succesvol!", BaseTransientBottomBar.LENGTH_SHORT);
                message.show();
            }
        });

        builder.setNegativeButton("Nee", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public AccountOverviewHome getAccountOverview() {
        return this.accountOverview;
    }

}
package com.boris.schuimschuld;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TableLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.fragment.NavHostFragment;

import com.boris.schuimschuld.account.Account;
import com.boris.schuimschuld.account.AccountRegister;
import com.boris.schuimschuld.account.AgeGroup;
import com.boris.schuimschuld.accountoverview.AccountCard;
import com.boris.schuimschuld.accountoverview.BaseAccountOverviewFragment;
import com.boris.schuimschuld.accountoverview.IOnBackPressed;
import com.boris.schuimschuld.util.DynamicLayoutFillers;
import com.boris.schuimschuld.util.DynamicSpinnerFiller;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class FragmentHomepage extends BaseAccountOverviewFragment implements IOnBackPressed {

    private com.boris.schuimschuld.databinding.FragmentHomePageBinding binding;
    private boolean selectionMode = false;
    private AccountRegister register;
    private MainActivity mainActivity;
    private TableLayout layout;

    private Boolean[] selectionStates;
    private ArrayList<Account> filteredAccounts;

    private final String sortInitialValue = "Sorteer";
    private final String sortAscendingValue = "A-Z";
    private final String sortDescendingValue = "Z-A";
    private final String filterInitialValue = "Filter";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = com.boris.schuimschuld.databinding.FragmentHomePageBinding.inflate(inflater, container, false);

        this.mainActivity = (MainActivity) getActivity();
        this.register = mainActivity.accountRegister;
        this.filteredAccounts = register.getAccounts();
        this.layout = binding.layoutAccountsHome;

        this.selectionStates = new Boolean[register.size()];
        Arrays.fill(selectionStates, Boolean.FALSE);

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Fill group selector
        DynamicSpinnerFiller.age(binding.spinnerGroupHome, getActivity(), new ArrayList<>(Arrays.asList(filterInitialValue)));

        // Fill sorting selector
        DynamicSpinnerFiller.fill(binding.spinnerSortingHome, getActivity(), new ArrayList<>(
                Arrays.asList(sortInitialValue, sortAscendingValue, sortDescendingValue)));

        // Fill account overview
        loadLayout();

        // Event Listeners
        binding.payButton.setOnClickListener(view1 -> createConfirmationAlert(mainActivity, view1));

        binding.spinnerGroupHome.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                loadLayout();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        binding.spinnerSortingHome.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                loadLayout();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public boolean onBackPressed() {
        return exitSelectionMode();
    }

    /* Event handlers */

    private void selectAccount(AccountCard accountCard) {
        // Select Account
        accountCard.setSelected(!accountCard.isSelected());
        selectionStates[register.getAccounts().indexOf(accountCard.getAccount())] = !selectionStates[register.getAccounts().indexOf(accountCard.getAccount())];

        // Update selection count
        long count = Arrays.stream(selectionStates).mapToInt(b -> b ? 1 : 0).sum();
        binding.selectionCountView.setText(String.valueOf(count));
    }

    private void chargeSelectedAccounts(MainActivity activity) {
        ArrayList<Account> accounts = register.getAccounts();
        for (int i = 0; i< selectionStates.length; i++) {
            if(selectionStates[i]) {
                accounts.get(i).pay();
                register.save();
            }
        }

        resetFragment();
        exitSelectionMode();
    }

    private void createConfirmationAlert(MainActivity activity, View view) {
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

    /* Helper Functions */

    private boolean exitSelectionMode() {
        if (selectionMode) {
            selectionMode = false;
            getView().findViewById(R.id.selectionToolBar).setVisibility(View.INVISIBLE);
            return true;
        } else {
            return false;
        }
    }

    private void loadLayout() {
        filterAccounts();
        sortAccounts();
        ArrayList<AccountCard> cards = createCards(filteredAccounts);
        DynamicLayoutFillers.Table(getContext(), layout, cards, 4);
    }

    private void resetFragment() {
        // Reload overview
        loadLayout();

        // Reset selection
        Arrays.fill(selectionStates, Boolean.FALSE);
    }

    private void filterAccounts() {
        String selectedGroupAsString = (String) binding.spinnerGroupHome.getSelectedItem();
        filteredAccounts = new ArrayList<>();

        if (selectedGroupAsString.equals(filterInitialValue)) {
            filteredAccounts = register.getAccounts();
        }
        else {
            AgeGroup selectedGroup = AgeGroup.valueOf(selectedGroupAsString);
            for (Account account : register.getAccounts()) {
                if (account.getGroup() == selectedGroup) {
                    filteredAccounts.add(account);
                }
            }
        }
    }

    private void sortAccounts() {
        String selectedSortAsString = (String) binding.spinnerSortingHome.getSelectedItem();
        if (selectedSortAsString.equals(sortInitialValue)) {
            filterAccounts();
        } else if (selectedSortAsString.equals(sortAscendingValue)) {
            Collections.sort(filteredAccounts, Comparator.comparing(Account::getName));
        } else if (selectedSortAsString.equals(sortDescendingValue)) {
            Collections.sort(filteredAccounts, Comparator.comparing(Account::getName).reversed());
        }
    }

    @Override
    protected void configureCard(AccountCard accountCard) {
        accountCard.setOnLongClickListener(view -> {
            selectionMode = true;
            selectAccount(accountCard);

            getView().findViewById(R.id.selectionToolBar).setVisibility(View.VISIBLE);
            return true;
        });

        accountCard.setOnClickListener(view -> {
            if (selectionMode) {
                selectAccount(accountCard);
            } else {
                Bundle bundle = new Bundle();
                bundle.putSerializable("ACCOUNT_DETAILS", accountCard.getAccount());
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_fragmentHomePageBinding2_to_accountDetailFragmentNew, bundle);
            }
        });
    }
}
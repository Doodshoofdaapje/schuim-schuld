package com.boris.schuimschuld;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.fragment.NavHostFragment;

import com.boris.schuimschuld.account.Account;
import com.boris.schuimschuld.account.Group;
import com.boris.schuimschuld.accountoverview.AccountCard;
import com.boris.schuimschuld.accountoverview.BaseAccountOverviewFragment;
import com.boris.schuimschuld.accountoverview.IOnBackPressed;
import com.boris.schuimschuld.dataservices.managers.IAccountManager;
import com.boris.schuimschuld.dataservices.managers.ITransactionManager;
import com.boris.schuimschuld.dataservices.managers.TransactionFactory;
import com.boris.schuimschuld.services.PaymentService;
import com.boris.schuimschuld.util.DynamicSpinnerFiller;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FragmentHomepage extends BaseAccountOverviewFragment implements IOnBackPressed {

    private com.boris.schuimschuld.databinding.FragmentHomePageBinding binding;
    private boolean selectionMode = false;
    private IAccountManager accountManager;
    private MainActivity mainActivity;
    private FlexboxLayout layout;

    private HashMap<UUID, Boolean> accountSelectionMap;
    private HashMap<UUID, Integer> accountCountMap;
    private ArrayList<Account> filteredAccounts;

    private final String sortInitialValue = "Sorteer";
    private final String sortAscendingValue = "A-Z";
    private final String sortDescendingValue = "Z-A";
    private final String filterInitialValue = "Filter";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = com.boris.schuimschuld.databinding.FragmentHomePageBinding.inflate(inflater, container, false);

        this.mainActivity = (MainActivity) getActivity();
        this.accountManager = mainActivity.accountManager;
        this.filteredAccounts = accountManager.getAll();
        this.layout = binding.layoutAccountsHome;

        this.accountSelectionMap = new HashMap<>();
        this.accountCountMap = new HashMap<>();
        for (Account account : accountManager.getAll()) {
            accountSelectionMap.put(account.getUuid(), false);
            accountCountMap.put(account.getUuid(), 1);
        }

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
        UUID accountUuid = accountCard.getAccount().getUuid();
        accountSelectionMap.put(accountUuid, !accountSelectionMap.get(accountUuid));

        // Update selection count
        int count = 0;
        for (Boolean selection : accountSelectionMap.values()) {
            if (selection == true) {
                count +=1;
            }
        }
        binding.selectionCountView.setText(String.valueOf(count));
    }

    private void updateCounterAccount(AccountCard accountCard, int difference) {
        UUID accountUuid = accountCard.getAccount().getUuid();

        if (accountCountMap.get(accountUuid) + difference >= 0) {
            accountCountMap.put(accountUuid, accountCountMap.get(accountUuid) + difference);
        }

        accountCard.setCounter(accountCountMap.get(accountUuid));
    }

    private void chargeSelectedAccounts(MainActivity activity) {
        PaymentService paymentService = new PaymentService(getContext(), accountManager);

        for (Map.Entry<UUID, Boolean> kv : accountSelectionMap.entrySet()) {
            if (kv.getValue()) {
                int drinkCount = accountCountMap.get(kv.getKey());
                paymentService.chargeAccount(accountManager.get(kv.getKey()), drinkCount);
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
        layout.removeAllViews();
        filterAccounts();
        sortAccounts();
        ArrayList<AccountCard> cards = createCards(filteredAccounts);
        for (AccountCard card : cards) {
            layout.addView(card);
        }
    }

    private void resetFragment() {
        // Reload overview
        loadLayout();

        // Reset selection
        for (Account account : accountManager.getAll()) {
            accountSelectionMap.put(account.getUuid(), false);
            accountCountMap.put(account.getUuid(), 1);
        }
    }

    private void filterAccounts() {
        String selectedGroupAsString = (String) binding.spinnerGroupHome.getSelectedItem();
        filteredAccounts = new ArrayList<>();

        if (selectedGroupAsString.equals(filterInitialValue)) {
            filteredAccounts = accountManager.getAll();
        }
        else {
            Group selectedGroup = Group.valueOf(selectedGroupAsString);
            for (Account account : accountManager.getAll()) {
                if (account.getGroups().contains(selectedGroup)) {
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
            Collections.sort(filteredAccounts, (a1, a2) -> a1.getName().compareTo(a2.getName()));
        } else if (selectedSortAsString.equals(sortDescendingValue)) {
            Collections.sort(filteredAccounts, (a1, a2) -> a2.getName().compareTo(a1.getName()));
        }
    }

    @Override
    protected void configureCard(AccountCard accountCard) {
        ITransactionManager manager = TransactionFactory.create(getContext());
        ArrayList<UUID> biggestDrinkers = manager.getHighestCount();

        if (biggestDrinkers.size() > 0 && accountCard.getAccount().getUuid().equals(biggestDrinkers.get(0)))
            accountCard.assignCrown(0);
        else if (biggestDrinkers.size() > 1 && accountCard.getAccount().getUuid().equals(biggestDrinkers.get(1)))
            accountCard.assignCrown(1);
        else if (biggestDrinkers.size() > 2 && accountCard.getAccount().getUuid().equals(biggestDrinkers.get(2)))
            accountCard.assignCrown(2);

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
                bundle.putSerializable("ACCOUNT_PICTURE", accountCard.getAccount().getPictureURI(getContext()).toString());
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_fragmentHomePageBinding2_to_accountDetailFragmentNew, bundle);
            }
        });

        accountCard.findViewById(R.id.accountCardPlus).setOnClickListener(view -> {
            updateCounterAccount(accountCard, 1);
        });

        accountCard.findViewById(R.id.accountCardMinus).setOnClickListener(view -> {
            updateCounterAccount(accountCard, -1);
        });
    }
}
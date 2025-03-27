package com.boris.schuimschuld.adminfragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.boris.schuimschuld.MainActivity;
import com.boris.schuimschuld.R;
import com.boris.schuimschuld.account.Account;
import com.boris.schuimschuld.accountoverview.AccountCard;
import com.boris.schuimschuld.accountoverview.BaseAccountOverviewFragment;
import com.boris.schuimschuld.util.DynamicSpinnerFiller;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class FragmentEditOverview extends BaseAccountOverviewFragment {

    private com.boris.schuimschuld.databinding.FragmentEditOverviewBinding binding;
    private MainActivity mainActivity;
    private FlexboxLayout layout;
    private final String BUNDLE_KEY = "ACCOUNT_DETAILS";

    private final String sortInitialValue = "Sorteer";
    private final String sortAscendingValue = "L-H";
    private final String sortDescendingValue = "H-L";

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        binding = com.boris.schuimschuld.databinding.FragmentEditOverviewBinding.inflate(inflater, container, false);
        mainActivity = (MainActivity) getActivity();
        layout = binding.layoutAccountsEdit;
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Fill sorting selector
        DynamicSpinnerFiller.fill(binding.spinnerSortingEdit, getActivity(), new ArrayList<>(
                Arrays.asList(sortInitialValue, sortAscendingValue, sortDescendingValue)));

        // Fill layout;
        loadLayout();

        // Event Handlers
        binding.buttonCancelEdit.setOnClickListener(view1 -> NavHostFragment.findNavController(FragmentEditOverview.this).popBackStack());

        binding.spinnerSortingEdit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
    protected void configureCard(AccountCard accountCard) {
        double balance = accountCard.getAccount().getBalance();
        if (balance >= 0) {
            accountCard.setColor(R.color.soft_green);
        }
        if (balance < 0) {
            accountCard.setColor(R.color.button_color_orange);
        }
        if (balance < -25) {
            accountCard.setColor(R.color.button_color_red);
        }

        accountCard.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(BUNDLE_KEY, accountCard.getAccount());
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_fragmentEditAccount_to_accountEditFragment, bundle);
        });
    }

    private void loadLayout() {
        layout.removeAllViews();
        ArrayList<Account> accounts = mainActivity.accountManager.getAll();
        sortAccounts(accounts);
        ArrayList<AccountCard> cards = createCards(accounts);
        for (AccountCard card : cards) {
            layout.addView(card);
        }
    }

    private void sortAccounts(ArrayList<Account> accounts) {
        String selectedSortAsString = (String) binding.spinnerSortingEdit.getSelectedItem();
        if (selectedSortAsString.equals(sortAscendingValue)) {
            Collections.sort(accounts, (a1, a2) -> a1.getBalance().compareTo(a2.getBalance()));
        } else if (selectedSortAsString.equals(sortDescendingValue)) {
            Collections.sort(accounts, (a1, a2) -> a2.getBalance().compareTo(a1.getBalance()));
        }
    }

}
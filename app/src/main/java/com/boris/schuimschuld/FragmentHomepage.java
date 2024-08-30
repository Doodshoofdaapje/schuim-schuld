package com.boris.schuimschuld;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TableLayout;
import android.widget.TableRow;

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
import com.boris.schuimschuld.util.ageGroupSpinner;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;

public class FragmentHomepage extends BaseAccountOverviewFragment implements IOnBackPressed {

    private com.boris.schuimschuld.databinding.FragmentHomePageBinding binding;
    private boolean selectionMode = false;
    private AccountRegister register;
    private MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = com.boris.schuimschuld.databinding.FragmentHomePageBinding.inflate(inflater, container, false);

        this.mainActivity = (MainActivity) getActivity();
        this.register = mainActivity.accountRegister;

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TableLayout layout = (TableLayout) view.findViewById(R.id.layoutAccountsHome);
        ageGroupSpinner.fillSpinner(binding.spinnerGroupHome, getActivity(), new ArrayList<>(Arrays.asList("Iedereen")));

        ArrayList<AccountCard> cards = createCards(register.getAccounts());
        DynamicLayoutFillers.Table(getContext(), layout, cards, 4);

        binding.payButton.setOnClickListener(view1 -> createAlert(mainActivity, view1));

        binding.buttonLogin.setOnClickListener(view1 -> NavHostFragment.findNavController(FragmentHomepage.this)
                .navigate(R.id.action_fragmentHomePageBinding2_to_fragmentLogIn));

        binding.spinnerGroupHome.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                filterLayout(layout);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
    }

    @Override
    public boolean onBackPressed() {
        return exitSelectionMode();
    }

    private boolean exitSelectionMode() {
        if (selectionMode) {
            selectionMode = false;
            getView().findViewById(R.id.selectionToolBar).setVisibility(View.INVISIBLE);
            return true;
        } else {
            return false;
        }
    }

    private void filterLayout(TableLayout layout) {
        String selectedGroupAsString = (String) binding.spinnerGroupHome.getSelectedItem();
        ArrayList<Account> filterdAccounts = new ArrayList<>();

        if (selectedGroupAsString.equals("Iedereen")) {
            filterdAccounts = register.getAccounts();
        }
        else {
            AgeGroup selectedGroup = AgeGroup.valueOf(selectedGroupAsString);
            for (Account account : register.getAccounts()) {
                if (account.getGroup() == selectedGroup) {
                    filterdAccounts.add(account);
                }
            }
        }

        ArrayList<AccountCard> cards = createCards(filterdAccounts);
        DynamicLayoutFillers.Table(getContext(), layout, cards, 4);
    }

    @Override
    protected void configureCard(AccountCard accountCard) {
        accountCard.setOnLongClickListener(view -> {
            accountCard.setSelected(!accountCard.isSelected());
            selectionMode = true;
            getView().findViewById(R.id.selectionToolBar).setVisibility(View.VISIBLE);
            return true;
        });

        accountCard.setOnClickListener(view -> {
            if (selectionMode) {
                accountCard.setSelected(!accountCard.isSelected());
            } else {
                Bundle bundle = new Bundle();
                bundle.putSerializable("ACCOUNT_DETAILS", accountCard.getAccount());
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_fragmentHomePageBinding2_to_accountDetailFragmentNew, bundle);
            }
        });
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
        exitSelectionMode();
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
}
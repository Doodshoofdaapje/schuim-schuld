package com.boris.schuimschuld.adminfragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.fragment.NavHostFragment;

import com.boris.schuimschuld.MainActivity;
import com.boris.schuimschuld.R;
import com.boris.schuimschuld.account.Account;
import com.boris.schuimschuld.accountoverview.AccountCard;
import com.boris.schuimschuld.accountoverview.BaseAccountOverviewFragment;
import com.boris.schuimschuld.util.DynamicLayoutFillers;

import java.util.ArrayList;

public class FragmentDeleteOverview extends BaseAccountOverviewFragment {

    private com.boris.schuimschuld.databinding.FragmentDeleteOverviewBinding binding;
    private MainActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = com.boris.schuimschuld.databinding.FragmentDeleteOverviewBinding.inflate(inflater, container, false);
        activity = (MainActivity) getActivity();

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        ArrayList<AccountCard> cards = createCards(activity.accountRegister.getAccounts());
        DynamicLayoutFillers.Table(getContext(), getView().findViewById(R.id.layoutAccountsDelete), cards, 4);

        binding.buttonCancelRemove.setOnClickListener(view1 -> NavHostFragment.findNavController(FragmentDeleteOverview.this).popBackStack());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    protected void configureCard(AccountCard accountCard) {
        accountCard.setOnClickListener(view -> createDeletionAlert(accountCard.getAccount()));
    }

    private void createDeletionAlert(Account account) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Bevestig");
        builder.setMessage("Weet je zeker dat je door wilt gaan?");

        builder.setPositiveButton("Ja", (dialog, which) -> {
            activity.accountRegister.deregister(account);
            dialog.dismiss();
            NavHostFragment.findNavController(this).popBackStack();
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
}
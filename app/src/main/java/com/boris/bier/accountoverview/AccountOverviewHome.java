package com.boris.bier.accountoverview;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.boris.bier.MainActivity;
import com.boris.bier.R;

public class AccountOverviewHome extends AccountOverviewBase {

    private boolean selectionMode = false;

    public AccountOverviewHome(Context context, MainActivity activity, Fragment fragment) {
        super(context, activity, fragment);
    }

    @Override
    protected void configureCard(AccountCard accountCard) {
        accountCard.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                accountCard.setSelected(!accountCard.isSelected());
                selectionMode = true;
                fragment.getView().findViewById(R.id.selectionToolBar).setVisibility(View.VISIBLE);
                return true;
            }
        });

        accountCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectionMode) {
                    accountCard.setSelected(!accountCard.isSelected());
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("ACCOUNT_DETAILS", accountCard.getAccount());
                    NavHostFragment.findNavController(fragment)
                            .navigate(R.id.action_fragmentHomePageBinding2_to_accountDetailFragmentNew, bundle);
                }
            }
        });
    }

    public boolean exitSelectionMode() {
        if (selectionMode) {
            selectionMode = false;
            fragment.getView().findViewById(R.id.selectionToolBar).setVisibility(View.INVISIBLE);
            return true;
        } else {
            return false;
        }
    }
}

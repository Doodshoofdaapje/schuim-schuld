package com.boris.schuimschuld.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.boris.schuimschuld.MainActivity;
import com.boris.schuimschuld.R;
import com.boris.schuimschuld.accountoverview.AccountCard;
import com.boris.schuimschuld.accountoverview.BaseAccountOverviewFragment;
import com.boris.schuimschuld.util.DynamicLayoutFillers;

import java.util.ArrayList;

public class FragmentEditOverview extends BaseAccountOverviewFragment {

    private com.boris.schuimschuld.databinding.FragmentEditOverviewBinding binding;
    private final String bundleKey = "ACCOUNT_DETAILS";

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        binding = com.boris.schuimschuld.databinding.FragmentEditOverviewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity mainActivity = (MainActivity) getActivity();
        TableLayout layout = getView().findViewById(R.id.layoutAccountsEdit);

        binding.buttonCancelEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FragmentEditOverview.this).popBackStack();
            }
        });

        ArrayList<AccountCard> cards = createCards(mainActivity.accountRegister.getAccounts());
        DynamicLayoutFillers.Table(getContext(), layout, cards, 4);
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
            bundle.putSerializable(bundleKey, accountCard.getAccount());
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_fragmentEditAccount_to_accountEditFragment, bundle);
        });
    }

}
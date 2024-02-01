package com.boris.bier.oldcode;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.boris.bier.R;
import com.boris.bier.account.Account;

public class AccountDetailFragmentOld extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RelativeLayout fragmentContainer = new RelativeLayout(getActivity());

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setDividerDrawable(getResources().getDrawable(R.drawable.empty_tall_divider));
        layout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        layout.setOrientation(LinearLayout.VERTICAL);

        Bundle bundle = this.getArguments();

        if (bundle != null) {
            Account account = (Account) bundle.getSerializable("ACCOUNT_DETAILS");

            layout.addView(createInformationCard("Account naam: ", account.getName()));
            layout.addView(createInformationCard("Balans: ", account.getBalance().toString()));
        }

        fragmentContainer.addView(layout);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)layout.getLayoutParams();
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.setLayoutParams(params);

        return fragmentContainer;
    }

    public ViewGroup createInformationCard(String labelText, String valueText) {
        LinearLayout container = new LinearLayout(getContext());
        container.setOrientation(LinearLayout.VERTICAL);

        TextView label = new TextView(getActivity());
        TextView value = new TextView(getActivity());

        label.setText(labelText);
        label.setTextColor(Color.WHITE);
        label.setPadding(30, 20, 30, 5);
        label.setWidth(600);
        label.setTextSize(24);
        label.setBackgroundResource(R.color.button_green);

        value.setText(valueText);
        value.setTextColor(Color.WHITE);
        value.setPadding(30, 5, 30, 20);
        value.setWidth(600);
        value.setTextSize(24);
        value.setBackgroundResource(R.color.button_green);

        container.addView(label);
        container.addView(value);

        return container;
    }

}
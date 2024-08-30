package com.boris.schuimschuld.accountoverview;

import androidx.fragment.app.Fragment;

import com.boris.schuimschuld.account.Account;

import java.util.ArrayList;

public abstract class BaseAccountOverviewFragment extends Fragment {

    public ArrayList<AccountCard> createCards(ArrayList<Account> accounts) {
        ArrayList<AccountCard> cards = new ArrayList<>();

        for (Account account : accounts) {
            AccountCard card = new AccountCard(getContext(), account.getName(), account);
            configureCard(card);
            cards.add(card);
        }

        return cards;
    }

    protected abstract void configureCard(AccountCard accountCard);
}

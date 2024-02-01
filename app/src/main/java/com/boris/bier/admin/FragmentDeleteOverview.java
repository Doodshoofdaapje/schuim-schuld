package com.boris.bier.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.boris.bier.MainActivity;
import com.boris.bier.R;
import com.boris.bier.accountoverview.AccountOverviewDelete;

public class FragmentDeleteOverview extends Fragment {

    private com.boris.bier.databinding.FragmentDeleteOverviewBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        binding = com.boris.bier.databinding.FragmentDeleteOverviewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity mainActivity = (MainActivity) getActivity();

        binding.buttonCancelRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FragmentDeleteOverview.this).popBackStack();
            }
        });

        AccountOverviewDelete accountOverview = new AccountOverviewDelete(getContext(), mainActivity, this);
        accountOverview.fillLayout(getView().findViewById(R.id.layoutAccountsDelete));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
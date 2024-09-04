package com.boris.schuimschuld.adminfragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.boris.schuimschuld.R;
import com.boris.schuimschuld.databinding.FragmentAdminPanelBinding;

public class FragmentAdminPanel extends Fragment {

private FragmentAdminPanelBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentAdminPanelBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FragmentAdminPanel.this)
                                .navigate(R.id.action_fragmentAdminOverview2_to_fragmentLogIn);
            }
        });

        binding.buttonNewAccountAdminPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FragmentAdminPanel.this)
                        .navigate(R.id.action_fragmentAdminOverview2_to_fragmentNewAccount);
            }
        });
        binding.buttonEditAccountAdminPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FragmentAdminPanel.this)
                        .navigate(R.id.action_fragmentAdminOverview2_to_fragmentEditAccount);
            }
        });
        binding.buttonRemoveAccountAdminPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FragmentAdminPanel.this)
                        .navigate(R.id.action_fragmentAdminOverview2_to_fragmentDeleteAccount);
            }
        });

        binding.buttonChangeCredentialsAdminPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FragmentAdminPanel.this)
                        .navigate(R.id.action_fragmentAdminOverview2_to_fragmentChangePassword);
            }
        });
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
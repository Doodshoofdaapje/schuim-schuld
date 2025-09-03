package com.boris.schuimschuld.adminfragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.boris.schuimschuld.R;
import com.boris.schuimschuld.databinding.FragmentSettingsBinding;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class FragmentSettings extends Fragment {

    private FragmentSettingsBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences sharedPref = getActivity().getSharedPreferences(
                getString(R.string.settings_filename), Context.MODE_PRIVATE);
        String defaultValue = getResources().getString(R.string.settings_default_consumption_price);
        String consumptionPrice = sharedPref.getString(getString(R.string.settings_consumption_price), defaultValue);
        binding.consumptiomPrice.setText(consumptionPrice);

        binding.buttonConfirmSettings.setOnClickListener(view1 -> {
            try {
                String newConsumptionPrice = binding.consumptiomPrice.getText().toString();
                Double newConsumptionPriceDouble = Double.parseDouble(newConsumptionPrice);

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.settings_consumption_price), newConsumptionPrice);
                editor.apply();

                NavHostFragment.findNavController(FragmentSettings.this).popBackStack();
            } catch (NumberFormatException exception) {
                Snackbar errorMessage = Snackbar.make(view, "Vul a.u.b. alleen getallen in", BaseTransientBottomBar.LENGTH_LONG);
                errorMessage.show();
            }
        });

        binding.buttonCancelSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FragmentSettings.this).popBackStack();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
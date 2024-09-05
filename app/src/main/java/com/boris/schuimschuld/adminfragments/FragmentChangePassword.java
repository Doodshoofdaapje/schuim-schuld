package com.boris.schuimschuld.adminfragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.boris.schuimschuld.databinding.FragmentChangePasswordBinding;
import com.boris.schuimschuld.util.JsonFileHandler;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.json.simple.JSONObject;

public class FragmentChangePassword extends Fragment {

    private FragmentChangePasswordBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentChangePasswordBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonConfirmPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPassword = binding.newPasswordField.getText().toString();
                String confirmPassword = binding.confirmPasswordField.getText().toString();

                if (newPassword.equals(confirmPassword)) {
                    JSONObject passwordAsJson = new JSONObject();
                    passwordAsJson.put("password", newPassword);

                    JsonFileHandler writer = new JsonFileHandler(getContext());
                    writer.writeFile("password.json", passwordAsJson);
                    NavHostFragment.findNavController(FragmentChangePassword.this).popBackStack();
                } else {
                    Snackbar errorMessage = Snackbar.make(view, "Wachtwoorden komen niet overeen", BaseTransientBottomBar.LENGTH_LONG);
                    errorMessage.show();
                }
            }
        });

        binding.buttonCancelPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FragmentChangePassword.this).popBackStack();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
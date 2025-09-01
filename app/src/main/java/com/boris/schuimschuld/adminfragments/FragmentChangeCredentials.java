package com.boris.schuimschuld.adminfragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.boris.schuimschuld.databinding.FragmentChangeCredentialsBinding;
import com.boris.schuimschuld.util.JsonFileHandler;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.json.simple.JSONObject;

import java.io.FileNotFoundException;

public class FragmentChangeCredentials extends Fragment {

    private FragmentChangeCredentialsBinding binding;
    private final String CREDENTIALS_FILE = "credentials.json";
    private final String CREDENTIALS_KEY = "credentials";
    private final String PASSWORD_KEY = "password";
    private final String USERNAME_KEY = "username";

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentChangeCredentialsBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        JsonFileHandler fileHandler = new JsonFileHandler(getContext());
        JSONObject storedCredentialsAsJson = fileHandler.readFileFromInternal(CREDENTIALS_FILE);

        String storedUsername = storedCredentialsAsJson.get(USERNAME_KEY).toString();
        String storedPassword = storedCredentialsAsJson.get(PASSWORD_KEY).toString();

        binding.newUsernameField.setText(storedUsername);
        binding.confirmUsernameField.setText(storedUsername);
        binding.newPasswordField.setText(storedPassword);
        binding.confirmPasswordField.setText(storedPassword);

        binding.buttonConfirmPassword.setOnClickListener(view1 -> {
            String newUsername = binding.newUsernameField.getText().toString();
            String confirmUsername = binding.confirmUsernameField.getText().toString();
            String newPassword = binding.newPasswordField.getText().toString();
            String confirmPassword = binding.confirmPasswordField.getText().toString();

            if (!newUsername.equals(confirmUsername)) {
                Snackbar errorMessage = Snackbar.make(view1, "Gebruikersnamen komen niet overeen", BaseTransientBottomBar.LENGTH_LONG);
                errorMessage.show();
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                Snackbar errorMessage = Snackbar.make(view1, "Wachtwoorden komen niet overeen", BaseTransientBottomBar.LENGTH_LONG);
                errorMessage.show();
                return;
            }

            storedCredentialsAsJson.put(USERNAME_KEY, newUsername);
            storedCredentialsAsJson.put(PASSWORD_KEY, newPassword);
            fileHandler.writeFile(CREDENTIALS_FILE, storedCredentialsAsJson);

            NavHostFragment.findNavController(FragmentChangeCredentials.this).popBackStack();
        });

        binding.buttonCancelPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FragmentChangeCredentials.this).popBackStack();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
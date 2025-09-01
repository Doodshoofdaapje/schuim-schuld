package com.boris.schuimschuld.adminfragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.boris.schuimschuld.R;
import com.boris.schuimschuld.databinding.FragmentLogInBinding;
import com.boris.schuimschuld.util.JsonFileHandler;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.json.simple.JSONObject;

public class FragmentLogIn extends Fragment {

    private FragmentLogInBinding binding;
    private final String CREDENTIALS_FILE = "credentials.json";
    private final String CREDENTIALS_KEY = "credentials";
    private final String PASSWORD_KEY = "password";
    private final String USERNAME_KEY = "username";

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentLogInBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String username = binding.textInputUserNameLogIn.getText().toString();
                    String password = binding.textInputPasswordLogIn.getText().toString();

                    // Get credentials from file, or set default values if this file does not exists already
                    JsonFileHandler fileHandler = new JsonFileHandler(getContext());
                    if (fileHandler.createFile(CREDENTIALS_FILE)) {
                        fileHandler.copyFromRaw(CREDENTIALS_FILE, R.raw.config, CREDENTIALS_KEY);
                    }
                    JSONObject storedCredentialsAsJson = fileHandler.readFileFromInternal(CREDENTIALS_FILE);
                    String storedUsername = storedCredentialsAsJson.get(USERNAME_KEY).toString();
                    String storedPassword = storedCredentialsAsJson.get(PASSWORD_KEY).toString();

                    // Check credentials
                    if (!username.equals(storedUsername) || !password.equals(storedPassword)) {
                        Snackbar errorMessage = Snackbar.make(view, "Verkeerde gebruikersnaam en of wachtwoord", BaseTransientBottomBar.LENGTH_LONG);
                        errorMessage.show();
                        return;
                    }

                    // Navigate to admin menu
                    NavHostFragment.findNavController(FragmentLogIn.this)
                            .navigate(R.id.action_fragmentLogIn_to_fragmentAdminOverview2);
                } catch (NullPointerException e) {
                    Snackbar errorMessage = Snackbar.make(view, "Vul beide velden in", BaseTransientBottomBar.LENGTH_LONG);
                    errorMessage.show();
                }
            }
        });

        binding.buttonCancelLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FragmentLogIn.this).popBackStack();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
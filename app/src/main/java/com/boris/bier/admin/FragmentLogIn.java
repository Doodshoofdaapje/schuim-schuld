package com.boris.bier.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.boris.bier.R;
import com.boris.bier.databinding.FragmentLogInBinding;
import com.boris.bier.util.JsonWriterReader;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.json.simple.JSONObject;

public class FragmentLogIn extends Fragment {

    private FragmentLogInBinding binding;

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
                String username = binding.textInputUserNameLogIn.getText().toString();
                String password = binding.textInputPasswordLogIn.getText().toString();

                JsonWriterReader writerReader = new JsonWriterReader(getContext());
                if (writerReader.createFile("password.json")) {
                    writerReader.copyFromRaw("password.json",  R.raw.config, "password");
                }
                JSONObject storedPasswordAsJson = writerReader.readFileFromInternal("password.json");
                String storedPassword = storedPasswordAsJson.get("password").toString();

                if (username.equals("Bas") && password.equals(storedPassword)) {
                    NavHostFragment.findNavController(FragmentLogIn.this)
                            .navigate(R.id.action_fragmentLogIn_to_fragmentAdminOverview2);
                } else {
                    Snackbar errorMessage = Snackbar.make(view, "Verkeerde gebruikersnaam en of wachtwoord", BaseTransientBottomBar.LENGTH_LONG);
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
package com.boris.schuimschuld.adminfragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.boris.schuimschuld.BuildConfig;
import com.boris.schuimschuld.R;
import com.boris.schuimschuld.databinding.FragmentAdminPanelBinding;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

        binding.buttonLogout.setOnClickListener(view1 -> NavHostFragment.findNavController(FragmentAdminPanel.this)
                        .navigate(R.id.action_fragmentAdminOverview2_to_fragmentLogIn));

        binding.buttonNewAccountAdminPanel.setOnClickListener(view12 -> NavHostFragment.findNavController(FragmentAdminPanel.this)
                .navigate(R.id.action_fragmentAdminOverview2_to_fragmentNewAccount));

        binding.buttonEditAccountAdminPanel.setOnClickListener(view13 -> NavHostFragment.findNavController(FragmentAdminPanel.this)
                .navigate(R.id.action_fragmentAdminOverview2_to_fragmentEditAccount));

        binding.buttonRemoveAccountAdminPanel.setOnClickListener(view14 -> NavHostFragment.findNavController(FragmentAdminPanel.this)
                .navigate(R.id.action_fragmentAdminOverview2_to_fragmentDeleteAccount));

        binding.buttonChangeCredentialsAdminPanel.setOnClickListener(view15 -> NavHostFragment.findNavController(FragmentAdminPanel.this)
                .navigate(R.id.action_fragmentAdminOverview2_to_fragmentChangePassword));

        binding.buttonBackup.setOnClickListener(view16 -> {
            File file;
            String backupFileName;

            // Get date
            Date currentTime = Calendar.getInstance().getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = dateFormat.format(currentTime);

            if (BuildConfig.DB_SQL) {
                file = getContext().getDatabasePath("database_schuimschuld.db");
                backupFileName = "accounts_" + formattedDate + ".db";
            } else {
                file = new File(getContext().getFilesDir(), "accounts.json");
                backupFileName = "accounts_" + formattedDate + ".json";
            }

            File backupFile = new File(getContext().getFilesDir(), backupFileName);

            // Copy file contents
            try {
                FileChannel src = new FileInputStream(file).getChannel();
                FileChannel dest = new FileOutputStream(backupFile).getChannel();
                dest.transferFrom(src, 0, src.size());
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            Intent intent = new Intent(Intent.ACTION_SEND);
            if (BuildConfig.DB_SQL) {
                intent.setType("application/db");
            } else {
                intent.setType("application/json");
            }

            Uri fileUri = FileProvider.getUriForFile(getContext(), "com.boris.schuimschuld.fileprovider", backupFile);
            intent.putExtra(Intent.EXTRA_STREAM, fileUri);
            intent.setPackage("com.google.android.apps.docs"); // Opens Google Drive directly

            startActivity(Intent.createChooser(intent, "Save file to"));
        });
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
package com.boris.schuimschuld.adminfragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.activity.result.ActivityResultLauncher;

import com.boris.schuimschuld.BuildConfig;
import com.boris.schuimschuld.R;
import com.boris.schuimschuld.databinding.FragmentAdminPanelBinding;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FragmentAdminPanel extends Fragment {

private FragmentAdminPanelBinding binding;
private ActivityResultLauncher<String[]> restoreLauncher;

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

        restoreLauncher = registerForActivityResult(
                new ActivityResultContracts.OpenDocument(),
                uri -> {
                    if (uri == null) return;
                    restoreBackupFromUri(uri, getContext());
                }
        );

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

        binding.buttonSettings.setOnClickListener(view17 -> NavHostFragment.findNavController(FragmentAdminPanel.this)
                .navigate(R.id.action_fragmentAdminOverview2_to_fragmentSettings));

        binding.buttonBackup.setOnClickListener(view16 -> {
            File file = BuildConfig.DB_SQL
                ? getContext().getDatabasePath("database_schuimschuld.db")
                : new File(getContext().getCacheDir(), "accounts.json");

            // Get date
            Date currentTime = Calendar.getInstance().getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = dateFormat.format(currentTime);

            String backupFileName = "accounts_" + formattedDate + (BuildConfig.DB_SQL ? ".db" : ".json");
            File backupFile = new File(getContext().getCacheDir(), backupFileName);

            // Copy file contents
            try (FileChannel src = new FileInputStream(file).getChannel();
                 FileChannel dest = new FileOutputStream(backupFile).getChannel()){
                dest.transferFrom(src, 0, src.size());
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Backup maken gefaald", Toast.LENGTH_SHORT).show();
                return;
            }

            Uri fileUri = FileProvider.getUriForFile(getContext(), "com.boris.schuimschuld.fileprovider", backupFile);

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType(BuildConfig.DB_SQL ? "application/db" : "application/json");
            intent.putExtra(Intent.EXTRA_STREAM, fileUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            startActivity(Intent.createChooser(intent, "Save file to"));
        });

        binding.restoreBackupButton.setOnClickListener(view18 -> {
            String mime = BuildConfig.DB_SQL ? "application/db" : "application/json";
            restoreLauncher.launch(new String[]{mime});
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void restoreBackupFromUri(Uri uri, Context context) {
        File dest = BuildConfig.DB_SQL
                ? context.getDatabasePath("database_schuimschuld.db")
                : new File(context.getFilesDir(), "accounts.json");

        try (InputStream in = context.getContentResolver().openInputStream(uri);
             OutputStream out = new FileOutputStream(dest)) {
            byte[] buf = new byte[8192];
            int len;
            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
            Toast.makeText(getContext(), "Backup hersteld", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }
}
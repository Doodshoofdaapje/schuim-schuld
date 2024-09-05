package com.boris.schuimschuld.accountoverview;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.boris.schuimschuld.R;
import com.boris.schuimschuld.account.Account;
import com.boris.schuimschuld.account.Group;
import com.boris.schuimschuld.util.PictureUtil;
import com.boris.schuimschuld.util.SerialBitmap;

public class AccountDetailFragment extends Fragment {

    private com.boris.schuimschuld.databinding.AccountDetailViewTestBinding binding;
    private final String bundleKey = "ACCOUNT_DETAILS";
    private Account account;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        //binding = com.boris.bier.databinding.FragmentAccountDetailBinding.inflate(inflater, container, false);
        binding = com.boris.schuimschuld.databinding.AccountDetailViewTestBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = this.getArguments();
        account = (Account) bundle.getSerializable(bundleKey);

        // Populate UI
        binding.textOutputNameDetail.setText(account.getName());
        binding.textOutputBalanceDetail.setText("€ " + account.getBalance().toString());

        String groupString = "";
        for (Group group : account.getGroups()) {
            groupString += group.toString() + " ";
        }
        binding.textOutputGroupDetail.setText(groupString);

        ImageView profilePictureView = (ImageView) view.findViewById(R.id.imageOutputAccountDetail);
        profilePictureView.setImageBitmap(account.getPicture());
        PictureUtil.roundPicture(profilePictureView);

        // Event Handlers
        binding.buttonReturnDetail2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(AccountDetailFragment.this).popBackStack();
            }
        });

        profilePictureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAlert();
            }
        });
    }

    private void imageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        imagePickerLauncher.launch(intent);
    }

    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() != Activity.RESULT_OK) {
                    Log.e("Image Chooser", "Result not OK");
                    return;
                }

                Intent data = result.getData();
                if (data == null) {
                    Log.e("Image Chooser", "Data == null");
                    return;
                }

                Uri selectedImageUri = data.getData();
                if (selectedImageUri == null) {
                    Log.e("Image Chooser", "URI == null");
                    return;
                }

                // update the preview image in the layout
                binding.imageOutputAccountDetail.setImageURI(selectedImageUri);
                Bitmap pfp = ((BitmapDrawable) binding.imageOutputAccountDetail.getDrawable()).getBitmap();
                account.setPicture(getContext(), pfp);
            }
    );

    private void createAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Bevestig");
        builder.setMessage("Weet je zeker dat je een nieuwe foto wilt kiezen?");

        builder.setPositiveButton("Ja", (dialog, which) -> imageChooser());
        builder.setNegativeButton("Nee", (dialog, which) -> dialog.dismiss());

        AlertDialog alert = builder.create();
        alert.show();
    }
}
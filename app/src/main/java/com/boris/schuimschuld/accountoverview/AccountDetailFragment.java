package com.boris.schuimschuld.accountoverview;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.yalantis.ucrop.UCrop;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.boris.schuimschuld.account.Account;
import com.boris.schuimschuld.account.Group;
import com.boris.schuimschuld.util.PictureUtil;

public class AccountDetailFragment extends Fragment {

    private com.boris.schuimschuld.databinding.AccountDetailViewBinding binding;
    private ActivityResultLauncher<Intent> imageSelectionLauncher;
    private ActivityResultLauncher<Intent> imageCropLauncher;

    private final String bundleKey = "ACCOUNT_DETAILS";
    private final String bundlePictureKey = "ACCOUNT_PICTURE";

    private Account account;
    private Uri pfpUri;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = com.boris.schuimschuld.databinding.AccountDetailViewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = this.getArguments();
        account = (Account) bundle.getSerializable(bundleKey);
        pfpUri = Uri.parse((String) bundle.getSerializable(bundlePictureKey));

        // Populate UI
        binding.textOutputNameDetail.setText(account.getName());
        binding.textOutputBalanceDetail.setText("€ " + account.getBalance().toString());
        binding.textOutputConsumptionCount.setText(String.valueOf(account.getConsumptionCount().intValue()));

        String groupString = "";
        for (Group group : account.getGroups()) {
            groupString += group.toString() + " ";
        }
        binding.textOutputGroupDetail.setText(groupString);

        // Set picture
        ImageView profilePictureView = binding.imageOutputAccountDetail;
        profilePictureView.setImageURI(pfpUri);
        PictureUtil.roundPicture(profilePictureView);

        // Event Handlers
        binding.buttonReturnDetail2.setOnClickListener(view1 ->
                NavHostFragment.findNavController(AccountDetailFragment.this).popBackStack());

        profilePictureView.setOnClickListener(view12 -> createChangeImageAlert());

        imageSelectionLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> imageSelectionIntentHandler(result)
        );

        imageCropLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> imageCropIntentHandler(result)
        );
    }

    private void createChangeImageAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Bevestig");
        builder.setMessage("Weet je zeker dat je een nieuwe foto wilt kiezen?");

        builder.setPositiveButton("Ja", (dialog, which) -> launchImageSelectionIntent());
        builder.setNegativeButton("Nee", (dialog, which) -> dialog.dismiss());

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void launchImageSelectionIntent() {
        try {
            Intent intent = new Intent();
            intent.setType("image/jpeg");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            imageSelectionLauncher.launch(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Afbeelding laden gefaald", Toast.LENGTH_SHORT);
        }

    }

    private void imageSelectionIntentHandler(ActivityResult result) {
        try{
            if (result.getResultCode() != Activity.RESULT_OK) {
                Log.e("ImageChooser", "Result not OK");
                return;
            }

            Intent data = result.getData();
            if (data == null) {
                Log.e("ImageChooser", "Data == null");
                return;
            }

            Uri selectedImageUri = data.getData();
            if (selectedImageUri == null) {
                Log.e("ImageChooser", "URI == null");
                return;
            }

            launchCropIntent(selectedImageUri);

        } catch (Exception e) {
            Log.e("ImageChooser", "Failed to load image", e);
            Toast.makeText(getContext(), "Setting image failed", Toast.LENGTH_SHORT);
        }
    }

    private void launchCropIntent(Uri sourceUri) {
        try {
            Intent uCropIntent = UCrop.of(sourceUri, pfpUri)
                    .withAspectRatio(1, 1)
                    .withMaxResultSize(250, 250)
                    .getIntent(getActivity());

            imageCropLauncher.launch(uCropIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void imageCropIntentHandler(ActivityResult result) {
        if (result.getResultCode() == UCrop.RESULT_ERROR) {
            Log.e("ImageCrop", "Error");
            return;
        }

        if (result.getResultCode() != Activity.RESULT_OK) {
            Log.d("ImageCrop", "Result not OK");
            return;
        }

        Uri croppedImageUri = UCrop.getOutput(result.getData());
        if (croppedImageUri == null) {
            Log.d("ImageCrop", "Uri is null");
            return;
        }

        binding.imageOutputAccountDetail.setImageURI(croppedImageUri);
        Bitmap pfp = ((BitmapDrawable) binding.imageOutputAccountDetail.getDrawable()).getBitmap();
        account.setPicture(getContext(), pfp);
    }
}
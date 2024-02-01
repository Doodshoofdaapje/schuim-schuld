package com.boris.bier.accountoverview;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.boris.bier.MainActivity;
import com.boris.bier.R;
import com.boris.bier.account.Account;
import com.boris.bier.util.PictureUtil;

public class AccountDetailFragment extends Fragment {

    //private com.boris.bier.databinding.FragmentAccountDetailBinding binding;
    private com.boris.bier.databinding.AccountDetailViewTestBinding binding;
    private Account account;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        //binding = com.boris.bier.databinding.FragmentAccountDetailBinding.inflate(inflater, container, false);
        binding = com.boris.bier.databinding.AccountDetailViewTestBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = this.getArguments();
        account = (Account) bundle.getSerializable("ACCOUNT_DETAILS");

        binding.textOutputNameDetail.setText(account.getName());
        binding.textOutputBalanceDetail.setText("€ " + account.getBalance().toString());
        binding.textOutputGroupDetail.setText(account.getGroup().toString());

        /*binding.buttonReturnDetail*/
        binding.buttonReturnDetail2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(AccountDetailFragment.this).popBackStack();
            }
        });

        ImageView profilePictureView = (ImageView) view.findViewById(R.id.imageOutputAccountDetail);
        profilePictureView.setImageBitmap(account.loadProfilePicture(getContext()).getBitmap());
        PictureUtil.roundPicture(binding.imageOutputAccountDetail);

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
        someActivityResultLauncher.launch(intent);
    }

    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        Uri selectedImageUri = data.getData();
                        if (selectedImageUri != null) {
                            // update the preview image in the layout
                            binding.imageOutputAccountDetail.setImageURI(selectedImageUri);
                            Bitmap pfp = ((BitmapDrawable) binding.imageOutputAccountDetail.getDrawable()).getBitmap();
                            account.setPicture(getContext(), pfp);
                        }
                    }
                } else {
                    return;
                }
            });

    private void createAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Bevestig");
        builder.setMessage("Weet je zeker dat je een nieuwe foto wilt kiezen?");

        builder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                imageChooser();
            }
        });

        builder.setNegativeButton("Nee", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
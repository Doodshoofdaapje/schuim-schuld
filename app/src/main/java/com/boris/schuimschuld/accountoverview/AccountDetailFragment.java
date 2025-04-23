package com.boris.schuimschuld.accountoverview;

import com.boris.schuimschuld.R;

import android.annotation.SuppressLint;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.*;
import com.boris.schuimschuld.dataservices.managers.ITransactionManager;
import com.boris.schuimschuld.dataservices.managers.TransactionFactory;
import com.boris.schuimschuld.util.Tuple;
import com.yalantis.ucrop.UCrop;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.boris.schuimschuld.account.Account;
import com.boris.schuimschuld.account.Group;
import com.boris.schuimschuld.util.PictureUtil;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

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

        ITransactionManager transactionManager = TransactionFactory.create(getContext());

        Bundle bundle = this.getArguments();
        account = (Account) bundle.getSerializable(bundleKey);
        pfpUri = Uri.parse((String) bundle.getSerializable(bundlePictureKey));

        // Populate UI
        binding.textOutputNameDetail.setText(account.getName());
        binding.textOutputBalanceDetail.setText("€ " + account.getBalance().toString());
        binding.textOutputConsumptionCount.setText(String.valueOf(transactionManager.count(account)));
        binding.accountDetailMonthly.setText(String.valueOf(transactionManager.countPastMonth(account)));

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.ENGLISH);
        String month = monthFormat.format(calendar.getTime());
        binding.accountDetailLabelMonth.setText("Consumpties: " + month);

        String groupString = "";
        for (Group group : account.getGroups()) {
            groupString += group.toString() + " ";
        }
        binding.textOutputGroupDetail.setText(groupString);

        // Set picture
        ImageView profilePictureView = binding.imageOutputAccountDetail;
        profilePictureView.setImageURI(pfpUri);
        PictureUtil.roundPicture(profilePictureView);

        // Populate Chart
        createPlot(transactionManager);

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

    @SuppressLint("ClickableViewAccessibility")
    private void createPlot(ITransactionManager transactionManager) {
        XYPlot plot = binding.accountDetailChart;

        // Add data
        Tuple<ArrayList<String>, ArrayList<Integer>> data = transactionManager.countPerMonth(account);
        XYSeries series = new SimpleXYSeries(data._y(), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Maand");
        LineAndPointFormatter seriesFormat =
                new LineAndPointFormatter(getContext(), R.drawable.line_point_formatter_with_labels);
        plot.addSeries(series, seriesFormat);

        // Styling
        plot.getLegend().setVisible(false);
        plot.setPlotPadding(10, 10, 10, 10);
        plot.getGraph().setMargins(50, 20, 50, 30);

        plot.setDomainStep(StepMode.INCREMENT_BY_VAL, 1);
        plot.getGraph().getLineLabelInsets().setLeft(-20f);
        plot.getGraph().getLineLabelInsets().setBottom(-20f);
        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new Format() {
            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
                int i = Math.round(((Number) obj).floatValue());
                return toAppendTo.append(data._x().get(i));
            }
            @Override
            public Object parseObject(String source, ParsePosition pos) {
                return null;
            }
        });
        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.LEFT).setFormat(new Format() {
            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
                int i = Math.round(((Number) obj).floatValue());
                return toAppendTo.append(i);
            }
            @Override
            public Object parseObject(String source, ParsePosition pos) {
                return null;
            }
        });

        // Set zoom restrictions and starting point
        int windowSize = 5;
        int dataSize = data._x().size();
        int startDomain = Math.max(0, dataSize - windowSize);
        int endDomain = startDomain + windowSize - 1;
        plot.setRangeBoundaries(0, Collections.max(data._y()), BoundaryMode.FIXED);
        plot.setDomainBoundaries(startDomain, endDomain, BoundaryMode.FIXED);
        plot.getOuterLimits().set(0, data._x().size() - 1, 0, Collections.max(data._y()));
        PanZoom.attach(plot, PanZoom.Pan.HORIZONTAL, PanZoom.Zoom.NONE);

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
package com.boris.bier.oldcode;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import com.boris.bier.R;
import com.boris.bier.account.Account;
import com.boris.bier.databinding.ActivityDetailBinding;

public class AccountDetailActivity extends AppCompatActivity {

    private ActivityDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AccountDetailFragmentOld details = new AccountDetailFragmentOld();

        Account account = (Account) getIntent().getSerializableExtra("ACCOUNT_DETAILS");
        Bundle bundle = new Bundle();
        bundle.putSerializable("ACCOUNT_DETAILS", account);

        details.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .add(android.R.id.content, details).commit();

    }
}
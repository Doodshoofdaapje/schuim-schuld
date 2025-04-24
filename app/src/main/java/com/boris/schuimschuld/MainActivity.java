package com.boris.schuimschuld;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.boris.schuimschuld.accountoverview.IOnBackPressed;
import com.boris.schuimschuld.databinding.ActivityMainBinding;
import com.boris.schuimschuld.dataservices.managers.AccountFactory;
import com.boris.schuimschuld.dataservices.managers.AccountManagerSQL;
import com.boris.schuimschuld.dataservices.managers.IAccountManager;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    public IAccountManager accountManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.accountManager = AccountFactory.create(this);

        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button loginButton = toolbar.findViewById(R.id.button_login);
        loginButton.setOnClickListener(view1 -> Navigation.findNavController(this, R.id.nav_host_fragment_content_main)
                .navigate(R.id.fragmentLogIn));

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override public void onBackPressed() {
        Fragment navHost = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
        Fragment fragment = navHost.getChildFragmentManager().getFragments().get(0);
        if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onDestroy() {
        if (accountManager instanceof AccountManagerSQL) {
            accountManager.close();
        }
        super.onDestroy();
    }
}
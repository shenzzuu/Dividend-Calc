package com.example.dividendcalc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.content.pm.PackageManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AboutActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        // Bind and highlight bottom navigation
        bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setSelectedItemId(R.id.nav_about);

        // Handle bottom nav item clicks
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.nav_history) {
                startActivity(new Intent(this, HistoryActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.nav_about) {
                Toast.makeText(this, "You're already on the About page", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });

        // Set app version
        TextView versionText = findViewById(R.id.versionText);
        try {
            String version = getPackageManager()
                    .getPackageInfo(getPackageName(), 0).versionName;
            versionText.setText("App Version: " + version);
        } catch (PackageManager.NameNotFoundException e) {
            versionText.setText("App Version: Unknown");
        }

        // GitHub clickable link
        TextView githubText = findViewById(R.id.githubText);
        githubText.setOnClickListener(v -> {
            String url = "https://github.com/shenzzuu/Dividend-Calc.git";
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        });

        // Email link
        ImageView emailLink = findViewById(R.id.emailLink);
        emailLink.setOnClickListener(v -> {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:2023149239@student.uitm.edu.my"));
            startActivity(emailIntent);
        });

        // Phone link
        ImageView phoneLink = findViewById(R.id.phoneLink);
        phoneLink.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:01161538600"));
            startActivity(intent);
        });
    }
}
package com.example.dividendcalc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextInputEditText investedAmount, dividendRate, months;
    MaterialButton calculateButton, clearButton;
    TextView result;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().setDecorFitsSystemWindows(false);
            getWindow().setNavigationBarColor(getColor(android.R.color.transparent));
        } else {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
            getWindow().setNavigationBarColor(getResources().getColor(android.R.color.transparent));
        }

        // Bind views
        investedAmount = findViewById(R.id.investedAmount);
        dividendRate = findViewById(R.id.dividendRate);
        months = findViewById(R.id.months);
        calculateButton = findViewById(R.id.calculateButton);
        clearButton = findViewById(R.id.clearButton);
        result = findViewById(R.id.result);
        bottomNavigationView = findViewById(R.id.bottomNav);

        // Highlight the current navigation item
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        // Calculate button logic
        calculateButton.setOnClickListener(v -> {
            try {
                double amount = Double.parseDouble(investedAmount.getText().toString());
                double rate = Double.parseDouble(dividendRate.getText().toString());
                int month = Integer.parseInt(months.getText().toString());

                if (month > 12 || month <= 0 || amount <= 0 || rate <= 0) {
                    result.setText("Please enter valid values.\nMonths: 1–12, Rate: must be positive");
                    return;
                }

                double monthlyDividend = (rate / 100 / 12) * amount;
                double totalDividend = monthlyDividend * month;

                String resultText = String.format(
                        "\uD83D\uDCCA Monthly Dividend: RM %.2f\n\uD83D\uDCB0 Total Dividend:   RM %.2f",
                        monthlyDividend, totalDividend);
                result.setText(resultText);

                String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                String historyEntry = String.format(
                        "Data Created: %s, Amount Invested: RM %.2f, Rate: %.2f%%, Month Invested: %d, Monthly Dividend: RM %.2f, Total Dividend: RM %.2f",
                        timestamp, amount, rate, month, monthlyDividend, totalDividend);
                HistoryStorage.getInstance(this).saveCalculation(historyEntry);

            } catch (Exception e) {
                result.setText("Please fill in all fields correctly.");
            }
        });

        // Clear button logic
        clearButton.setOnClickListener(v -> {
            investedAmount.setText("");
            dividendRate.setText("");
            months.setText("");
            result.setText("Monthly Dividend:\nTotal Dividend:");
        });

        // Bottom navigation actions
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                Toast.makeText(this, "You're already on the Home page", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.nav_history) {
                startActivity(new Intent(this, HistoryActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.nav_about) {
                startActivity(new Intent(this, AboutActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });
    }
}
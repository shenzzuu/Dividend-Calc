package com.example.dividendcalc;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    LinearLayout historyContainer;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        // Load and highlight bottom navigation
        bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setSelectedItemId(R.id.nav_history);

        // Bottom nav item handler
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.nav_about) {
                startActivity(new Intent(this, AboutActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.nav_history) {
                Toast.makeText(this, "You're already on the History page", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });

        // Load history into cards
        historyContainer = findViewById(R.id.historyContainer);
        ArrayList<String> historyItems = HistoryStorage.getInstance(this).getHistory();

        for (String item : historyItems) {
            String[] parts = item.split(", ");
            StringBuilder formatted = new StringBuilder();
            for (String part : parts) {
                String[] keyValue = part.split(":", 2);
                if (keyValue.length == 2) {
                    String label = keyValue[0].trim();
                    String value = keyValue[1].trim();
                    formatted.append(String.format("• %s: %s\n", label, value));
                }
            }

            // Create CardView dynamically
            MaterialCardView cardView = new MaterialCardView(this);
            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            cardParams.setMargins(0, 0, 0, dpToPx(16));
            cardView.setLayoutParams(cardParams);
            cardView.setCardElevation(dpToPx(4));
            cardView.setRadius(dpToPx(12));
            cardView.setCardBackgroundColor(ContextCompat.getColor(this, android.R.color.white));
            cardView.setUseCompatPadding(true);

            // Inner layout for content
            LinearLayout innerLayout = new LinearLayout(this);
            innerLayout.setOrientation(LinearLayout.VERTICAL);
            innerLayout.setPadding(dpToPx(16), dpToPx(16), dpToPx(16), dpToPx(16));

            // Text content
            TextView entry = new TextView(this);
            entry.setText(formatted.toString().trim());
            entry.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            entry.setTextColor(Color.BLACK);

            // Build card
            innerLayout.addView(entry);
            cardView.addView(innerLayout);
            historyContainer.addView(cardView);
        }

        // Clear history
        findViewById(R.id.btnClearHistory).setOnClickListener(v -> {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Confirm Clear")
                    .setMessage("Are you sure you want to clear all history?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        HistoryStorage.getInstance(this).clearHistory();
                        historyContainer.removeAllViews();
                        Toast.makeText(this, "History cleared!", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }
    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }
}
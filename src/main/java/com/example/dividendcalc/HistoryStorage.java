package com.example.dividendcalc;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class HistoryStorage {

    private static final String PREFS_NAME = "dividend_history";
    private static final String KEY_HISTORY = "history";

    private static HistoryStorage instance;
    private SharedPreferences prefs;

    public static HistoryStorage getInstance(Context context) {
        if (instance == null) {
            instance = new HistoryStorage(context.getApplicationContext());
        }
        return instance;
    }

    private HistoryStorage(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveCalculation(String data) {
        Set<String> currentHistory = new HashSet<>(getHistory());
        currentHistory.add(data);
        prefs.edit().putStringSet(KEY_HISTORY, currentHistory).apply();
    }

    public ArrayList<String> getHistory() {
        Set<String> historySet = prefs.getStringSet(KEY_HISTORY, new HashSet<>());
        return new ArrayList<>(historySet);
    }

    public void clearHistory() {
        prefs.edit().remove(KEY_HISTORY).apply();
    }
}
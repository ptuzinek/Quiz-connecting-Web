package com.example.quiz.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {
    private SharedPreferences preferences;

    public Prefs(Activity activity) {
        this.preferences = activity.getPreferences(activity.MODE_PRIVATE);
    }

    public void saveHighestScore(int score) {
        int lastScore = getHighScore();

        if (score > lastScore) {
            preferences.edit() .putInt("high_score", score).apply();
        }
    }

    public int getHighScore() {
        return preferences.getInt("high_score", 0);
    }

    public void setState(int index) {
        preferences.edit().putInt("index_state", index).apply();
    }

    public int getState() {
        return preferences.getInt("index_state", 0);
    }

    public void setScore(int score) {
        preferences.edit().putInt("score_state", score).apply();
    }

    public int getScore() {
        return preferences.getInt("score_state", 0);
    }


}

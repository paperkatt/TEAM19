package com.apockestafe.team19;

/**
 * Created by JLee on 10/30/16.
 */
import android.annotation.SuppressLint;
import android.content.SharedPreferences;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

class SharedPreferencesEditor {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    public SharedPreferencesEditor(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        editor = sharedPreferences.edit();
    }

    public void addEvents(ArrayList<String> eventNumbers) {
        Set<String> set = new HashSet<>();
        set.addAll(eventNumbers);
        editor.putStringSet("myEvents", set);
        editor.apply();
    }



    public void addMyEmail(String email) {
        editor.putString("myEmail", email);
        editor.apply();
    }

    public void addMarker(LatLng latlng) {
        String ll = latlng.toString();
        editor.putString("prevMarker", ll);
        editor.apply();
    }

    public void addLoginSkip(String skipper) {
        editor.putString("loginSkip", skipper);
        editor.apply();
    }

    public void addToken(String token) {
        if(getToken() != null) {
            editor.remove("fbtoken");
        }
        editor.putString("fbtoken", "");
        editor.apply();
    }

    public void deleteMyEmail(){
        editor.remove("myEmail");
        editor.apply();
    }

    public void deleteLoginSkip(){
        editor.remove("loginSkip");
        editor.apply();
    }

    public String getToken() {return sharedPreferences.getString("fbtoken", ""); }

    public String getMyEmail() {
        return sharedPreferences.getString("myEmail", "");
    }

    public String getLoginSkip() {
        return sharedPreferences.getString("loginSkip", "");
    }

    public String getMarker() { return sharedPreferences.getString("prevMarker", ""); }

    public Set<String> getEvents() {

        return sharedPreferences.getStringSet("myEvents", null); }

}

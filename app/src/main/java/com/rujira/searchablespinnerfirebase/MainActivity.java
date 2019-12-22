package com.rujira.searchablespinnerfirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

public class MainActivity extends AppCompatActivity {

    SearchableSpinner searchableSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchableSpinner = (SearchableSpinner) findViewById(R.id.searchableSpinner);

    }
}

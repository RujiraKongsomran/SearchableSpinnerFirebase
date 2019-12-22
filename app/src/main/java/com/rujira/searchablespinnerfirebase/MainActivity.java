package com.rujira.searchablespinnerfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rujira.searchablespinnerfirebase.Interface.IFirebaseLoadDone;
import com.rujira.searchablespinnerfirebase.Model.Movie;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IFirebaseLoadDone {

    SearchableSpinner searchableSpinner;

    DatabaseReference movieRef;
    IFirebaseLoadDone iFirebaseLoadDone;
    List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchableSpinner = (SearchableSpinner) findViewById(R.id.searchableSpinner);

        // Init DB
        movieRef = FirebaseDatabase.getInstance().getReference("Movies");
        // Init interface
        iFirebaseLoadDone = this;
        // Get data
        movieRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Movie> movies = new ArrayList<>();
                for (DataSnapshot movieSnapshot : dataSnapshot.getChildren()) {
                    movies.add(movieSnapshot.getValue(Movie.class));
                }
                iFirebaseLoadDone.onFirebaseLoadSuccess(movies);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                iFirebaseLoadDone.onFirebaseLoadFailed(databaseError.getMessage());
            }
        });


    }

    @Override
    public void onFirebaseLoadSuccess(List<Movie> movieList) {
        movies = movieList;

        // Get all name
        List<String> nameList = new ArrayList<>();
        for (Movie movie : movieList) {
            nameList.add(movie.getName());
        }
        // Create Adapter and set for Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nameList);
        searchableSpinner.setAdapter(adapter);

    }

    @Override
    public void onFirebaseLoadFailed(String message) {

    }
}

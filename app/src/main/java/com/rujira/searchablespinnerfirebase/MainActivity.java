package com.rujira.searchablespinnerfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rujira.searchablespinnerfirebase.Interface.IFirebaseLoadDone;
import com.rujira.searchablespinnerfirebase.Model.Movie;
import com.squareup.picasso.Picasso;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IFirebaseLoadDone {

    SearchableSpinner searchableSpinner;

    DatabaseReference movieRef;
    IFirebaseLoadDone iFirebaseLoadDone;
    List<Movie> movies;
    BottomSheetDialog bottomSheetDialog;

    // View
    TextView movieTitle, movieDescription;
    ImageView movieImage;
    FloatingActionButton btnFav;

    boolean isFirstTimeClick = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init Dialog
        bottomSheetDialog = new BottomSheetDialog(this);
        View bottom_sheet_dialog = getLayoutInflater().inflate(R.layout.layout_movie, null);
        movieDescription = (TextView) bottom_sheet_dialog.findViewById(R.id.movieDescription);
        movieTitle = (TextView) bottom_sheet_dialog.findViewById(R.id.movieTitle);
        movieImage = (ImageView) bottom_sheet_dialog.findViewById(R.id.movieImage);
        btnFav = (FloatingActionButton) bottom_sheet_dialog.findViewById(R.id.btnFav);

        // Event
        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Close bottomSheet
                bottomSheetDialog.dismiss();
                Toast.makeText(MainActivity.this, "Add Fav !!!", Toast.LENGTH_SHORT).show();
            }
        });

        // Set content
        bottomSheetDialog.setContentView(bottom_sheet_dialog);

        searchableSpinner = (SearchableSpinner) findViewById(R.id.searchableSpinner);
        searchableSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!isFirstTimeClick) {
                    Movie movie = movies.get(i);
                    movieTitle.setText(movie.getName());
                    movieDescription.setText(movie.getDescription());
                    Picasso.get().load(movie.getImage()).into(movieImage);

                    bottomSheetDialog.show();
                } else {
                    isFirstTimeClick = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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

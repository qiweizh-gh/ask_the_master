package com.atm.askthemaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

//    FirebaseDatabase database;
    private NavController navController;
    private DatabaseReference usersRef;
    private FirebaseAuth mAuth;
    private String currentUserId;
    private String calledBy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // test firebase connection
//        DatabaseReference myref = database.getInstance().getReference("test");
//        myref.setValue("test message_ms_519");

        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(navView, navController);
       // NavigationUI.setupActionBarWithNavController(this, navController);
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null)
            currentUserId = currentUser.getUid();
    }

    @Override
    public boolean onSupportNavigateUp(){
        return navController.navigateUp();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser != null){
//            Intent registrationIntent = new Intent(MainActivity.this, FeedbackActivity.class);
//            startActivity(registrationIntent);
//            finish();
            checkForReceivingCall();

        }
        else{
            Intent registrationIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(registrationIntent);
            finish();
        }
    }

    private void checkForReceivingCall() {
        usersRef.child(currentUserId)
                .child("Ringing")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.hasChild("ringing")){
                            calledBy = dataSnapshot.child("ringing").getValue().toString();
                            Intent callingIntent = new Intent(MainActivity.this, CallingActivity.class);
                            callingIntent.putExtra("visit_user_id", calledBy);
                            callingIntent.putExtra("role","master");

                            startActivity(callingIntent);
                            finish();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}

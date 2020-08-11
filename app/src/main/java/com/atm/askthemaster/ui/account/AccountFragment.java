package com.atm.askthemaster.ui.account;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.atm.askthemaster.LoginActivity;
import com.atm.askthemaster.MainActivity;
import com.atm.askthemaster.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {
    private Button mLogout;
    private Button mSave;
    private DatabaseReference usersRef;
    private EditText userName , userCategory;

    private String userId;
    private FirebaseAuth  mAuth;
    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLogout = getView().findViewById(R.id.Logout);
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        userId = mAuth.getInstance().getCurrentUser().getUid();

        userName = getView().findViewById(R.id.user_name);
        userCategory = getView().findViewById(R.id.master_category);

        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });

        mSave = getView().findViewById(R.id.Setting_save);
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String getUserName = userName.getText().toString();
                final String getUserCategory = userCategory.getText().toString();
//                DatabaseReference fromPath = FirebaseDatabase.getInstance().getReference().child("Users").child("user05");
                DatabaseReference fromPath = FirebaseDatabase.getInstance().getReference().child("Users").child(getUserName);
                DatabaseReference toPath = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);


                Log.d("SaveBtn", "here!");

                moveRecord(fromPath, toPath, userId);
/**
 * Below commented code is to create a User node with uid equals mauth id, using a HashMap.
 * If you can not use the above moveRecord function for the first time, please use below code to generate a node,
 * then deepcopy an existing dummy users`s data to your own node.
 *
 */
//                HashMap<String, Object> profileMap = new HashMap<>();
//                profileMap.put("userId",FirebaseAuth.getInstance().getCurrentUser().getUid());
//                profileMap.put("name",getUserName);
//                if(getUserCategory.equals("")){
//                    profileMap.put("asker",true);
//                }
//                else{
//                    profileMap.put("asker",false);
//                    profileMap.put("masterSpecialties",getUserCategory);
//                }
//
//
//                usersRef.child(userId).updateChildren(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if(task.isSuccessful()){
//                            Log.d("SaveBtn","saved");
//
//                        }
//                    }
//                });

            }
        });
    }

    private void finish() {
    }



    private void moveRecord(DatabaseReference fromPath, final DatabaseReference toPath, String userId) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                toPath.setValue(dataSnapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isComplete()) {
                            Log.d("SaveBtn", "Success!");
                        } else {
                            Log.d("SaveBtn", "Copy failed!");
                        }
                    }
                });
                // changed by Jiawen
                // i thought userId key should match true auth userId, it might be used later
                toPath.child("userId").setValue(userId);
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        fromPath.addListenerForSingleValueEvent(valueEventListener);

    }
}
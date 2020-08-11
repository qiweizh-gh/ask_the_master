package com.atm.askthemaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class CallingActivity extends AppCompatActivity {


    private TextView nameContact;
    private ImageView profileImage;
    private ImageView cancelCallBtn, acceptCallBtn;

    /**  Please first refer to layout files to understand variables;
     * sender can be treated as asker since asker is the one to make call
     * receiver can be treated as master since master is the to accept call
     * when enter CallingActivity, receiverId and question id must be brought in
     * through getIntent().getExtras()
     *
     *
     *
     *
     */
    private String questionId;

    DatabaseReference userRef;

    private String receiverUserId ="", receiverUserImage ="", receiverUserName ="";
    private String senderUserId ="", senderUserImage ="", senderUserName ="";

    private DatabaseReference usersRef;

    private String checker = "";
    private String callingID = "";
    private String ringingID ="";

    private String role = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calling);
        checker = "";
        nameContact =findViewById(R.id.name_calling);
        profileImage =findViewById(R.id.profile_image_calling);
        cancelCallBtn =findViewById(R.id.cancel_call);
        acceptCallBtn = findViewById(R.id.make_call);


        senderUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        role = getIntent().getExtras().get("role").toString();
//        userRef = FirebaseDatabase.getInstance().getReference().child("Users");

        receiverUserId = getIntent().getExtras().get("visit_user_id").toString();

//        userRef.child(senderUserId).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.hasChild("Ringing")){
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
        questionId = getIntent().getExtras().get("curQuestionId") == null? "":getIntent().getExtras().get("curQuestionId").toString();
        Log.d("questionId", questionId);

        Toast.makeText(this, "ReceiverId is "+ receiverUserId, Toast.LENGTH_SHORT).show();
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");

//        mediaPlayer = MediaPlayer.create(this,R.raw.ringing);
//        checker = "";



        getAndSetUserProfileInfo();

        cancelCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mediaPlayer.stop();
                checker = "clicked";
                cancelCallingUser();
            }
        });
        acceptCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                mediaPlayer.stop();
                final HashMap<String ,Object> callingPickUpMap = new HashMap<>();
                callingPickUpMap.put("picked", "picked");
                usersRef.child(senderUserId).child("Ringing")
                        .updateChildren(callingPickUpMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isComplete()){

                                    Intent intent = new Intent(CallingActivity.this,VideoChatActivity.class);
                                    intent.putExtra("visit_user_id", receiverUserId);
                                    intent.putExtra("role",role);
                                    intent.putExtra("curQuestionId", questionId);


                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        });
    }



    private void getAndSetUserProfileInfo() {
        //replace with info you want to display
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(receiverUserId).exists()){
//                    receiverUserImage = dataSnapshot.child(receiverUserId).child("image")
//                            .getValue().toString();
                    receiverUserName = dataSnapshot.child(receiverUserId).child("askerId")
                            .getValue().toString();
                    nameContact.setText(receiverUserName);
//                    Picasso.get().load(receiverUserImage).placeholder(R.drawable.profile_image).into(profileImage);

                }
                if(dataSnapshot.child(senderUserId).exists()){
//                    senderUserImage = dataSnapshot.child(senderUserId).child("image")
//                            .getValue().toString();
                    senderUserName = dataSnapshot.child(senderUserId).child("masterId")
                            .getValue().toString();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
//        mediaPlayer.start();

        usersRef.child(receiverUserId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(!checker.equals("clicked")&&!dataSnapshot.hasChild("Calling") &&
                                !dataSnapshot.hasChild("Ringing"))
                        {

                            final HashMap<String, Object> callingInfo = new HashMap<>();
//                            callingInfo.put("uid", senderUserId);
//                            callingInfo.put("name", senderUserName);
//                            callingInfo.put("image", senderUserImage);
                            callingInfo.put("calling", receiverUserId);

                            usersRef.child(senderUserId).child("Calling")
                                    .updateChildren(callingInfo)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                final HashMap<String, Object> ringingInfo = new HashMap<>();
//                                                ringingInfo.put("uid", receiverUserId);
//                                                ringingInfo.put("name", receiverUserName);
//                                                ringingInfo.put("image", receiverUserImage);
                                                ringingInfo.put("ringing", senderUserId);
                                                usersRef.child(receiverUserId).
                                                        child("Ringing")
                                                        .updateChildren(ringingInfo);
                                            }
                                        }
                                    });
                        }


                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(senderUserId).hasChild("Ringing")
                        && !dataSnapshot.child(senderUserId).hasChild("Calling")){
                    acceptCallBtn.setVisibility(View.VISIBLE);
                }

                if(dataSnapshot.child(receiverUserId).child("Ringing")
                        .hasChild("picked")){
                    Intent intent = new Intent(CallingActivity.this,VideoChatActivity.class);
                    intent.putExtra("visit_user_id", receiverUserId);
                    intent.putExtra("role",role);
                    intent.putExtra("curQuestionId", questionId);

                    startActivity(intent);
                    finish(); //add finish 6/10
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void cancelCallingUser() {
        //from sending side
        usersRef.child(senderUserId).child("Calling")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists()&& dataSnapshot.hasChild("calling")){
                            callingID = dataSnapshot.child("calling").getValue().toString();

                            usersRef.child(callingID)
                                    .child("Ringing")
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                usersRef.child(senderUserId)
                                                        .child("Calling")
                                                        .removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                startActivity(new Intent(CallingActivity.this,MainActivity.class));
                                                                finish();
                                                            }
                                                        });
                                            }
                                        }
                                    });
                        }
                        else{
                            startActivity(new Intent(CallingActivity.this,MainActivity.class));
                            finish();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        startActivity(new Intent(CallingActivity.this,MainActivity.class));
                        finish();
                    }
                });


        usersRef.child(senderUserId).child("Ringing")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists()&& dataSnapshot.hasChild("ringing")){
                            ringingID = dataSnapshot.child("ringing").getValue().toString();
                            usersRef.child(ringingID)
                                    .child("Calling")
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                usersRef.child(senderUserId)
                                                        .child("Ringing")
                                                        .removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                startActivity(new Intent(CallingActivity.this,MainActivity.class));
                                                                finish();
                                                            }
                                                        });

                                            }
                                        }
                                    });
                        }
                        else{
                            startActivity(new Intent(CallingActivity.this,MainActivity.class));
                            finish();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        startActivity(new Intent(CallingActivity.this,MainActivity.class));
                        finish();
                    }
                });

    }


}

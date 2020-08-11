package com.atm.askthemaster;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class FeedbackActivity extends AppCompatActivity {

    private Button submitFeedback,gobackFeedback;
    private String currentUserId;
    private DatabaseReference contactsRef ,usersRef;
    private FirebaseAuth mAuth;
    private RatingBar ratingBar;

    private TextView moreText;
    private Button  addMore;
    private Button  noMore;
    private EditText feedbackText;

    private boolean moreMaster = false;
    private String feedback;

    private String questionId;

    private String role;
    private String opp_id;
    private String comments;
    float  val_rate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        moreText = findViewById(R.id.ask_for_more);
        addMore = findViewById(R.id.another);
        noMore = findViewById(R.id.no_another);

        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        ratingBar = (RatingBar) findViewById(R.id.rating);
        currentUserId = mAuth.getCurrentUser().getUid();
        submitFeedback =findViewById(R.id.submit_feedback);
        gobackFeedback = findViewById(R.id.go_back);
        opp_id = getIntent().getExtras().get("visit_user_id").toString();
        role = getIntent().getExtras().get("role").toString();
        feedbackText = (EditText) findViewById(R.id.feedback_comment);

        comments = feedbackText.getText().toString();
        val_rate = ratingBar.getRating();

        addMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreMaster = true;
                Toast.makeText(FeedbackActivity.this, "System will notify your question to other master!", Toast.LENGTH_SHORT).show();
            }
        });

        noMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreMaster = false;
                allGone();
            }
        });


        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(rating < 3 && role.equals("asker")){
                    allShow();

                }
                else{
                    allGone();
                    moreMaster = false;
                }
            }
        });




        questionId = getIntent().getExtras().get("curQuestionId") == null? "" :getIntent().getExtras().get("curQuestionId").toString();
        Log.d("questionId", questionId);
        submitFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sentCommentInfo(ratingBar.getRating(),null);
                Intent intent = new Intent(FeedbackActivity.this,MainActivity.class);
                startActivity(intent);

//                Toast.makeText(FeedbackActivity.this, "User"+ currentUserId+ "rating is" + ratingBar.getRating(), Toast.LENGTH_SHORT).show();
            }
        });

        gobackFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                val_rate = (float)5;
                sentCommentInfo(val_rate,null);
                Toast.makeText(FeedbackActivity.this, "User"+ currentUserId+ " rating is " + val_rate, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(FeedbackActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });


    }

    private void sentCommentInfo(float val_rate, String comments) {


        HashMap<String, Object> profileMap = new HashMap<>();
        profileMap.put("Rating",Float.toString(val_rate));


        FirebaseDatabase.getInstance().getReference().child("Users").child(opp_id)
                .updateChildren(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
//                    Intent intent = new Intent(FeedbackActivity.this,MainActivity.class);
//                    startActivity(intent);
//                    finish();
//                    progressDialog.dismiss();
                    if(!questionId.equals("")){
                        HashMap<String,Object> q_status = new HashMap<>();
                        q_status.put("answered","true");
                        if(!moreMaster ) q_status.put("status","solved");
                        FirebaseDatabase.getInstance().getReference().child("Questions").child(questionId)
                                .updateChildren(q_status).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(FeedbackActivity.this,"All update successful", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

                    }
                    Toast.makeText(FeedbackActivity.this,"update successful", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
    private void allShow(){
        moreText.setVisibility(View.VISIBLE);
        addMore.setVisibility(View.VISIBLE);
        noMore.setVisibility(View.VISIBLE);
    }

    private void allGone(){
        moreText.setVisibility(View.GONE);
        addMore.setVisibility(View.GONE);
        noMore.setVisibility(View.GONE);
    }
}

package com.atm.askthemaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class VideoChatActivity extends AppCompatActivity implements Session.SessionListener, PublisherKit.PublisherListener {

    private static String API_KEY = "46757132";
    private static String SESSION_ID ="2_MX40Njc1NzEzMn5-MTU5MDEzNzMwNjUxMn5DYU5COWZLTW9QOEZvcVpUdXJIVGRRaEN-fg";
    private static String TOKEN = "T1==cGFydG5lcl9pZD00Njc1NzEzMiZzaWc9OTM2MjM1ODc4YTA2Y2I0YzQxNzBmM2M5NjQ3ZWIyYzEwZDhkYTVjYTpzZXNzaW9uX2lkPTJfTVg0ME5qYzFOekV6TW41LU1UVTVNREV6TnpNd05qVXhNbjVEWVU1Q09XWkxUVzlRT0VadmNWcFVkWEpJVkdSUmFFTi1mZyZjcmVhdGVfdGltZT0xNTkwMTM3NDA1Jm5vbmNlPTAuNzYzOTY2ODQwMDkxMDcwOSZyb2xlPXB1Ymxpc2hlciZleHBpcmVfdGltZT0xNTkyNzI5NDA0JmluaXRpYWxfbGF5b3V0X2NsYXNzX2xpc3Q9";
    private static final String LOG_TAG = VideoChatActivity.class.getSimpleName();
    private static final int RC_VIDEO_APP_PERM =124;
    private String userID ="";

    private FrameLayout mPublisherViewController;
    private FrameLayout mSubscriberViewController;
    private Session mSession;
    private Publisher mpublisher;
    private Subscriber msubscriber;
    private String questionId;

    private String role;
    private String opp_id;
    private ImageView closeVideoChatBtn;
    private DatabaseReference usersRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_chat);


        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        role = getIntent().getExtras().get("role").toString();
        opp_id = getIntent().getExtras().get("visit_user_id").toString();
        questionId = getIntent().getExtras().get("curQuestionId") == null? "":getIntent().getExtras().get("curQuestionId").toString();

        closeVideoChatBtn = findViewById(R.id.close_video_chat_btn);
        closeVideoChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(userID).hasChild("Ringing")){
                            String calID = dataSnapshot.child(userID).child("Ringing")
                                    .child("ringing").getValue().toString();
                            String callerName = usersRef.child(calID).child("name").getKey().toString();
                            String receiverName   = usersRef.child(userID).child("name").toString();
                            Log.d("users","caller is"+ callerName);
                            Log.d("users","receiver is"+receiverName);

//                            usersRef.child(calID).child("Calling").removeValue();
//                            usersRef.child(userID).child("Ringing").removeValue();

                            usersRef.child(calID).child("Calling").removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful()){
                                                usersRef.child(userID).child("Ringing").removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    if(mpublisher != null){
                                                                        mpublisher.destroy();
                                                                    }
                                                                    if(msubscriber != null){
                                                                        msubscriber.destroy();
                                                                    }
                                                                    Intent intent = new Intent(VideoChatActivity.this,FeedbackActivity.class);
                                                                    intent.putExtra("visit_user_id", opp_id);
                                                                    intent.putExtra("role",role);
                                                                    intent.putExtra("curQuestionId", questionId);

                                                                    startActivity(intent);
                                                                    finish();
                                                                }
                                                            }
                                                        });
                                            }
                                        }
                                    });
//                            if(mpublisher != null){
//                                mpublisher.destroy();
//                            }
//                            if(msubscriber != null){
//                                msubscriber.destroy();
//                            }
//                            usersRef.child(calID).child("Calling").removeValue();
//                            usersRef.child(userID).child("Ringing").removeValue();
//                            startActivity(new Intent(VideoChatActivity.this,FeedBackActivity.class));
//                            finish();
                        }
                        if(dataSnapshot.child(userID).hasChild("Calling")){
                            String recID = dataSnapshot.child(userID).child("Calling")
                                    .child("calling").getValue().toString();
//                            usersRef.child(recID).child("Ringing").removeValue();
//                            usersRef.child(userID).child("Calling").removeValue();
                            usersRef.child(recID).child("Ringing").removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                usersRef.child(userID).child("Calling").removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    if(mpublisher != null){
                                                                        mpublisher.destroy();
                                                                    }
                                                                    if(msubscriber != null){
                                                                        msubscriber.destroy();
                                                                    }
                                                                    Intent intent = new Intent(VideoChatActivity.this,FeedbackActivity.class);
                                                                    intent.putExtra("visit_user_id", opp_id);
                                                                    intent.putExtra("role",role);
                                                                    intent.putExtra("curQuestionId", questionId);

                                                                    startActivity(intent);
                                                                    finish();
                                                                }
                                                            }
                                                        });
                                            }
                                        }
                                    });
                            //                            startActivity(new Intent(VideoChatActivity.this,RegistrationActivity.class));
//                            if(mpublisher != null){
//                                mpublisher.destroy();
//                            }
//                            if(msubscriber != null){
//                                msubscriber.destroy();
//                            }
//                            usersRef.child(recID).child("Ringing").removeValue();
//                            usersRef.child(userID).child("Calling").removeValue();
//                            startActivity(new Intent(VideoChatActivity.this,FeedBackActivity.class));
//                            finish();
                        }
                        else {
                            //                            startActivity(new Intent(VideoChatActivity.this, RegistrationActivity.class));
                            if(mpublisher != null){
                                mpublisher.destroy();
                            }
                            if(msubscriber != null){
                                msubscriber.destroy();
                            }
                            Intent intent = new Intent(VideoChatActivity.this,FeedbackActivity.class);
                            intent.putExtra("visit_user_id", opp_id);
                            intent.putExtra("role",role);
                            intent.putExtra("curQuestionId", questionId);
                            startActivity(intent);
                            finish();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                        usersRef.child(userID).child("Ringing").removeValue();
//                        usersRef.child(userID).child("Calling").removeValue();
//                        startActivity(new Intent(VideoChatActivity.this,FeedbackActivity.class));
//                        finish();
                    }
                });
            }

        });
        requestPermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,VideoChatActivity.this);
    }
    @AfterPermissionGranted(RC_VIDEO_APP_PERM)
    private void requestPermissions(){
        String[] perms = {Manifest.permission.INTERNET, Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO};

        if(EasyPermissions.hasPermissions(this,perms)){
            mPublisherViewController = findViewById(R.id.publisher_container);
            mSubscriberViewController = findViewById(R.id.subscriber_container);

            //init and connect to the Session
            mSession = new Session.Builder(this,API_KEY,SESSION_ID).build();
            mSession.setSessionListener(VideoChatActivity.this);
            mSession.connect(TOKEN);
        }
        else {
            EasyPermissions.requestPermissions(this,"Hey this app needs Mic and Camera, please alow",RC_VIDEO_APP_PERM,perms);
        }
    }

    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {

    }
    //2. Publising a stream to the session
    @Override
    public void onConnected(Session session) {
        Log.i(LOG_TAG,"Session Connected");
        mpublisher = new Publisher.Builder(this).build();
        mpublisher.setPublisherListener(VideoChatActivity.this);
        mPublisherViewController.addView(mpublisher.getView());

        if(mpublisher.getView() instanceof GLSurfaceView){
            ((GLSurfaceView) mpublisher.getView()).setZOrderOnTop(true);
        }
        mSession.publish(mpublisher);
    }

    @Override
    public void onDisconnected(Session session) {
        Log.i(LOG_TAG,"Stream Disconnected");

    }

    //3. Substribing to the stream
    @Override
    public void onStreamReceived(Session session, Stream stream) {
        Log.i(LOG_TAG,"Stream Received");
        if(msubscriber == null) {
            msubscriber = new Subscriber.Builder(this, stream).build();
            mSession.subscribe(msubscriber);
            mSubscriberViewController.addView(msubscriber.getView());
        }

    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {
        Log.i(LOG_TAG,"Stream Dropped");
        if(msubscriber != null){
            msubscriber = null;
            mSubscriberViewController.removeAllViews();
        }
    }

    @Override
    public void onError(Session session, OpentokError opentokError) {
        Log.i(LOG_TAG,"Stream Error");

    }
}

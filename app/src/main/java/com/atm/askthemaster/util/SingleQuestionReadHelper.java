package com.atm.askthemaster.util;

import android.util.Log;

import com.atm.askthemaster.model.Question;
import com.atm.askthemaster.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * you can query single question by questionId in this class
 */

public class SingleQuestionReadHelper {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;

    public interface DataStatus {
        void dataIsLoaded(Question question);

    }

    /**
     * constructor
     * set reference path, usually you can pass in "Questions/" + questionId
     * @param path
     */
    public SingleQuestionReadHelper(String path) {
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference(path);;
    }

    /**
     * By calling this function, you will get question object of questionId you set
     * and it will also call dataIsLoaded which you can implement in outside class.
     * Then you can set other param in outside class using node object.
     * By using more helper classes, it will create a motion series, and operate one after another
     * read question -> dataIsLoaded -> read user -> dataIsLoaded -> ...
     * @param dataStatus
     */
    public void readData(final SingleQuestionReadHelper.DataStatus dataStatus, boolean continuous) {
        if (continuous) {
            mReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Question node = dataSnapshot.getValue(Question.class);
                    dataStatus.dataIsLoaded(node);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w("test", "listener canceled", databaseError.toException());
                }
            });
        } else {
            mReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Question node = dataSnapshot.getValue(Question.class);
                    dataStatus.dataIsLoaded(node);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w("test", "listener canceled", databaseError.toException());
                }
            });
        }
    }
}

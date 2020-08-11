package com.atm.askthemaster.repository;

import android.app.Activity;

import com.atm.askthemaster.callback.CallBack;
import com.atm.askthemaster.contants.Constants;
import com.atm.askthemaster.firebase.FirebaseRepository;
import com.atm.askthemaster.model.Question;
import com.atm.askthemaster.utility.Utility;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;

import static com.atm.askthemaster.firebase.FirebaseConstants.QUESTIONS;
import static com.atm.askthemaster.firebase.FirebaseDatabaseReference.DATABASE;

public class QuestionRepositoryImpl extends FirebaseRepository implements QuestionRepository {

    private Activity activity;
    private DatabaseReference questionDatabaseRef;

    public QuestionRepositoryImpl(Activity activity) {
        this.activity = activity;
        questionDatabaseRef = DATABASE.getReference(QUESTIONS);
    }

    @Override
    public void createQuestion(Question question, CallBack callBack) {
        String pushKey = questionDatabaseRef.push().getKey();
        if (question != null && !Utility.isEmptyOrNull(pushKey)) {
            question.setQuestionKey(pushKey);
            DatabaseReference databaseReference = questionDatabaseRef.child(pushKey);
            fireBaseCreate(databaseReference, question, new CallBack() {
                @Override
                public void onSuccess(Object object) {
                    callBack.onSuccess(Constants.SUCCESS);
                }

                @Override
                public void onError(Object object) {
                    callBack.onError(object);
                }
            });
        }
    }

    @Override
    public void updateQuestion(String questionKey, HashMap map, CallBack callBack) {
        if (!Utility.isEmptyOrNull(questionKey)) {
            DatabaseReference databaseReference = questionDatabaseRef.child(questionKey);
            fireBaseUpdateChildren(databaseReference, map, new CallBack() {
                @Override
                public void onSuccess(Object object) {
                    callBack.onSuccess(Constants.SUCCESS);
                }

                @Override
                public void onError(Object object) {
                    callBack.onError(object);
                }
            });
        }
    }
}

package com.atm.askthemaster.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.atm.askthemaster.model.MasterCard;
import com.atm.askthemaster.model.Question;
import com.atm.askthemaster.model.User;
import com.atm.askthemaster.ui.list.QuestionListAdapter;
import com.atm.askthemaster.ui.master.SavedAskerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * helper class
 * you can get matched active questions for master and set recycle view by getMatchedQuestions function
 *
 */

public class MatchHelper {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private final int MASTERNOTIFYNUM = 1;

    public MatchHelper() {
        this.mDatabase = FirebaseDatabase.getInstance();
    }

    /**
     * Supposed to be called by working switch button
     * it will query user by userId and extract matched questions
     * by using matched questions, it will call checkValidQuestions to filter valid questions
     * @param userId
     * @param questionListAdapter
     *
     */
    public void getMatchedQuestions(String userId, QuestionListAdapter questionListAdapter) {
        List<Question> matchedQuestions = new ArrayList<>();
        Map<String, Question> existQuestions = new HashMap<>();
        new SingleUserReadHelper("Users/" + userId).readData(new SingleUserReadHelper.DataStatus() {
            @Override
            public void dataIsLoaded(User user) {
                if (user == null)
                    return;
                List<String> activeQuestions = user.getMasterQuestions();
                if (activeQuestions == null)
                    return;
                checkValidQuestions(activeQuestions, matchedQuestions, questionListAdapter, existQuestions);
            }
        }, true);
    }

    /**
     * by using this function, it will select recent unsolved questions within 30 days
     * if question was solved, it will trigger recycler view to delete specific question
     * then add valid questions to recycle view adapter to update view
     * @param activeQuestions
     * @param matchedQuestions
     * @param questionListAdapter
     * @param existQuestions
     */
    private void checkValidQuestions(List<String> activeQuestions, List<Question> matchedQuestions, QuestionListAdapter questionListAdapter, Map<String, Question> existQuestions) {
        for (String questionKey : activeQuestions) {
            if (existQuestions.containsKey(questionKey)) {
                continue;
            }
            new SingleQuestionReadHelper("Questions/" + questionKey).readData(new SingleQuestionReadHelper.DataStatus() {
                @Override
                public void dataIsLoaded(Question question) {
                    if (question == null)
                        return;
                    Date curTime = Calendar.getInstance().getTime();
                    if ((question.getCreateTime().getTime() >= curTime.getTime() - (1000 * 3600 * 24 * 30L)) && question.getStatus().equals("unsolved")) {
                        matchedQuestions.add(question);
                        existQuestions.put(questionKey, question);
                    }
                    questionListAdapter.setQuestions(matchedQuestions);
                }
            }, false);
        }
    }

    /**
     * It will traverse matchedQuestion list, and find out question to be removed
     * @param questionKey
     * @param matchedQuestions
     * @param existQuestions
     * @return
     */
    private void removeQuestion(String questionKey, List<Question> matchedQuestions, Map<String, Question> existQuestions) {
        if (matchedQuestions == null || questionKey == null) {
            return;
        }
        for (int i = 0; i < matchedQuestions.size(); i++) {
            if (matchedQuestions.get(i).getQuestionKey().equals(questionKey)) {
                matchedQuestions.remove(i);
                existQuestions.remove(questionKey);
            }
        }
    }

    /**
     * Supposed to be called by submit button
     * This function will match master with the same category you passed in.
     * By default, it will notify 1 master.
     * After notified, master user table will get new questionId on firebase,
     * then master recycler view could show all these active questions.
     * @param category
     * @param questionKey
     * @param mContext
     */
    public void notifyMatchedMasters(String category, String questionKey, Context mContext) {
        DatabaseReference userRef = mDatabase.getReference("Users");
        // do as filter, select first master with category matched
        userRef.orderByChild("masterSpecialties/0").equalTo(category).limitToFirst(MASTERNOTIFYNUM).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User curUser = dataSnapshot.getValue(User.class);
                List<String> masterQuestions = curUser.getMasterQuestions();
                DatabaseReference curRef = userRef.child(curUser.getUserId());

                // initially, masterQuestion might be null, checked here
                if (masterQuestions == null) {
                    curRef.child("masterQuestions").setValue(Arrays.asList(questionKey));
                    Toast.makeText(mContext, "Notified successfully(null).", Toast.LENGTH_SHORT).show();
                    return;
                }

                // use hashset to check whether there already exist current master
                Set<String> questionsSet = new HashSet<>(masterQuestions);
                if (!questionsSet.contains(questionKey)) {
                    curUser.getMasterQuestions().add(questionKey);
                    curRef.setValue(curUser);
                    Toast.makeText(mContext, "Notified successfully.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "Already notified", Toast.LENGTH_SHORT).show();
                }

                Log.d("matched master id", dataSnapshot.getValue(User.class).getUserId().toString());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * This function supposed to be called by accept button.
     * After pressed accept btn, it will add new masterId in question-recommendMasterList table.
     * Then, asker could show all accepted master.
     * @param questionKey
     * @param userId
     * @param mContext
     */
    public void notifyAsker(String questionKey, String userId, Context mContext) {
        new SingleQuestionReadHelper("Questions/" + questionKey).readData(new SingleQuestionReadHelper.DataStatus() {
            @Override
            public void dataIsLoaded(Question question) {
                DatabaseReference curRef = mDatabase.getReference("Questions").child(questionKey);
                List<String> recommendMasterList = question.getRecommendMasterList();

                // Initially, recommendMasterList might be null, check null here
                if (recommendMasterList == null) {
                    curRef.child("recommendMasterList").setValue(Arrays.asList(userId));
                    Toast.makeText(mContext, "Accept successfully.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // check whether master has already pressed accpet btn
                // in case there exist two same master in recommendMasterList.
                Set<String> mastersSet = new HashSet<>(recommendMasterList);
                if (!mastersSet.contains(userId)) {
                    recommendMasterList.add(userId);
                    curRef.setValue(question);
                    Toast.makeText(mContext, "Accept successfully.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "Already accepted", Toast.LENGTH_SHORT).show();
                }
            }
        }, false);
    }

    /**
     * Supposed to be called by saveFragment. It will add new matched master to recycler view.
     * @param questionKey
     * @param savedAskerAdapter
     */
    public void showMatchedMasters(String questionKey, SavedAskerAdapter savedAskerAdapter) {
        List<MasterCard> mDataList = new ArrayList<>();
        Set<String> existMasters = new HashSet<>();
        // get question object, inorder to get recommend master list
        new SingleQuestionReadHelper("Questions/" + questionKey).readData(new SingleQuestionReadHelper.DataStatus() {
            @Override
            public void dataIsLoaded(Question question) {
                List<String> recommendMasterList = question.getRecommendMasterList();
                if (recommendMasterList == null) {
                    savedAskerAdapter.setMasterCards(mDataList);
                    return;
                }
                // traverse all recommend masters, get their info and set masterCard
                for (String userId : recommendMasterList) {
                    if (existMasters.contains(userId)) {
                        continue;
                    }
                    new SingleUserReadHelper("Users/" + userId).readData(new SingleUserReadHelper.DataStatus() {
                        @Override
                        public void dataIsLoaded(User user) {
//                            if (!user.isMaster()) {
//                                return;
//                            }
                            MasterCard masterCard = new MasterCard.MasterCardBuilder()
                                    .setMasterId(user.getMasterId())
                                    .setQTitle(question.getTitle())
                                    .setCategory(question.getCategories().get(0).getName())
                                    .setDescription("I'm good at " + user.getMasterSpecialties().get(0))
                                    .setMasterUserId(user.getUserId())
                                    .build();
                            mDataList.add(masterCard);
                            existMasters.add(user.getUserId());
                            savedAskerAdapter.setMasterCards(mDataList);
                        }
                    }, false);
                }
            }
        }, true);
    }

}

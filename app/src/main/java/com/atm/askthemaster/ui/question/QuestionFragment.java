package com.atm.askthemaster.ui.question;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.atm.askthemaster.R;
import com.atm.askthemaster.callback.CallBack;
import com.atm.askthemaster.firebase.FirebaseConstants;
import com.atm.askthemaster.firebase.FirebaseDatabaseReference;
import com.atm.askthemaster.model.Category;
import com.atm.askthemaster.model.Question;
import com.atm.askthemaster.model.User;
import com.atm.askthemaster.repository.QuestionRepository;
import com.atm.askthemaster.repository.QuestionRepositoryImpl;
import com.atm.askthemaster.ui.master.SaveFragment;
import com.atm.askthemaster.util.MatchHelper;
import com.atm.askthemaster.util.SingleUserReadHelper;
import com.atm.askthemaster.utility.Utility;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static java.lang.Double.parseDouble;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionFragment extends Fragment {

    TextView textView;

    private QuestionRepository questionRepository = new QuestionRepositoryImpl(this.getActivity());

    private EditText questionTitle;
    private EditText questionDescription;
    private EditText estimateDuration;
    private Button submitQuestion;
    private DatabaseReference database;
    private List<Category> categoryList;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public QuestionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_question, container, false);
        textView = v.findViewById(R.id.categoryData);
        Bundle bundle = this.getArguments();
        String data = bundle.getString("key");
        textView.setText(data);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // submit question button to upload questions to firebase
        database  = FirebaseDatabase.getInstance().getReference();
        submitQuestion = (Button) getView().findViewById(R.id.addBtn);
        textView = (TextView) getView().findViewById(R.id.categoryData);

        String chosenCategory = textView.getText().toString();
        findCategory(chosenCategory);

        submitQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MatchHelper mMatchHelper = new MatchHelper();
                Question questionModel = fillQuestionModel(database);
                saveData(questionModel);

//                uploadQuestion(database);
                //waiting..keep question ID
//                String key = database.push().getKey();
                String questionKey = questionModel.getQuestionKey();
                mMatchHelper.notifyMatchedMasters(chosenCategory, questionKey, getContext());
                addQuestionToUser(questionKey);
//                if(questionId==null) {
//                    System.out.println("key is null!");
//                }
                SaveFragment saveFragment = new SaveFragment();
                Bundle bundle = new Bundle();
                bundle.putString("questionKey", questionKey);
                saveFragment.setArguments(bundle);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace( R.id.LinearLayout2, saveFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

    }

    private void addQuestionToUser(String questionKey) {
        String userId = mAuth.getInstance().getCurrentUser().getUid();
        new SingleUserReadHelper("Users/" + userId).readData(new SingleUserReadHelper.DataStatus() {
            @Override
            public void dataIsLoaded(User user) {
                List<String> questionList = user.getAskerQuestions() == null? new ArrayList<>() : user.getAskerQuestions();
                questionList.add(questionKey);
                FirebaseDatabase.getInstance().getReference("Users/" + userId + "/askerQuestions").setValue(questionList);
            }
        }, false);
    }

    private Question fillQuestionModel(DatabaseReference databaseReference) {
        String key = databaseReference.push().getKey();
        questionTitle = (EditText) getView().findViewById(R.id.search_view);
        questionDescription = (EditText) getView().findViewById(R.id.search_view2);
        estimateDuration = (EditText) getView().findViewById(R.id.search_view3);

        Question question = new Question.QuestionBuilder().setQuestionId(Utility.getNewId())
                .setTitle(questionTitle.getText().toString())
                .setContent(questionDescription.getText().toString())
                .setDuration(parseDouble(estimateDuration.getText().toString()))
                .setCategories(categoryList)
                .setQuestionId(key)
                .setCreateTime(Calendar.getInstance().getTime())
                .setAskerId(mAuth.getCurrentUser().getUid())
                .setStatus("unsolved")
                .build();

        return question;
    }

    private void saveData(Question question) {
        questionRepository.createQuestion(question, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                Utility.showMessage(getContext(), "Question submitted.");
            }

            @Override
            public void onError(Object object) {
                Utility.showMessage(getContext(), "Some thing went wrong");
            }
        });
    }

    private void findCategory(final String chosenCategory) {
        DatabaseReference categoryRef = FirebaseDatabaseReference.DATABASE.getReference("Categories");
        categoryList = new ArrayList<>();

        Query ref = categoryRef.orderByChild("name").equalTo(chosenCategory);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Category category = postSnapshot.getValue(Category.class);
                    categoryList.add(category);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
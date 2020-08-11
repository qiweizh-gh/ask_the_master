package com.atm.askthemaster.ui.list;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.atm.askthemaster.databinding.FragmentListBinding;
import com.atm.askthemaster.model.Question;
import com.atm.askthemaster.model.ReadHelper;
import com.atm.askthemaster.util.MatchHelper;
import com.atm.askthemaster.utility.Utility;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {

    private FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    private FragmentListBinding binding;
    private List<Question> mDataList;
    private FirebaseAuth mAuth;

    public ListFragment() {
        mDatabase = FirebaseDatabase.getInstance();
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_list, container, false);
        binding = FragmentListBinding.inflate(inflater,container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final boolean[] masterWork = {false};

        final QuestionListAdapter questionListAdapter = new QuestionListAdapter();
        binding.recyclerView.setAdapter(questionListAdapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        binding.simpleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                String userId = mAuth.getInstance().getCurrentUser().getUid();
                mDataList = new ArrayList<>();
                mReference = mDatabase.getReference("Users/" + userId + "/isMaster");
                if(isChecked) {
                    mReference.setValue(true);
                    MatchHelper matchHelper = new MatchHelper();
                    matchHelper.getMatchedQuestions(userId, questionListAdapter);
//                    matchHelper.notifyMatchedMasters("Interview", "-M9GtgMZHKcEOegz21T-", getActivity());

//                    new ReadHelper("Questions").readData(new ReadHelper.DataStatus() {
//                        @Override
//                        public void DataIsLoaded(List<Question> nodes, List<String> keys) {
//                            mDataList.clear();
//                            mDataList.addAll(nodes);
//                            questionListAdapter.setQuestions(mDataList);
//
//                        }
//                    });
                } else {
                    questionListAdapter.setQuestions(mDataList);
                    mReference.setValue(false);
                }
            }
        });


    }


}

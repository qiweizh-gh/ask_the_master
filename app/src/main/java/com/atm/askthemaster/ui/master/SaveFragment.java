package com.atm.askthemaster.ui.master;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.atm.askthemaster.databinding.FragmentSaveBinding;
import com.atm.askthemaster.firebase.FirebaseConstants;
import com.atm.askthemaster.util.MatchHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SaveFragment extends Fragment {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private String questionId;

    private FragmentSaveBinding binding;

    // SwipeRefreshLayout swipeRefreshLayout;
    public SaveFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_save, container, false);
//        Log.d("321","for onCreate");
        binding = FragmentSaveBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        Log.d("456","for onCreate");
        super.onViewCreated(view, savedInstanceState);
        final SavedAskerAdapter savedAskerAdapter = new SavedAskerAdapter();
//        String key = getArguments().getString("questionId");
//        String key = "-M8xJC_X0m-M1z29XulQ";
        Bundle bundle = this.getArguments();
        String questionKey = bundle.getString("questionKey");


        binding.recyclerView.setAdapter(savedAskerAdapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.simpleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MatchHelper matchHelper = new MatchHelper();
                matchHelper.showMatchedMasters(questionKey, savedAskerAdapter);
            }
        });
    }
}
package com.atm.askthemaster.model;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReadHelper {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private List<Question> nodesList = new ArrayList<>();

    public interface DataStatus {
        void DataIsLoaded(List<Question> nodes, List<String> keys);

    }

    public ReadHelper(String tableName) {
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference(tableName);;
    }

    public void readData(final DataStatus dataStatus) {
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nodesList.clear();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Question node = keyNode.getValue(Question.class);
                    nodesList.add(node);
                }
                dataStatus.DataIsLoaded(nodesList, keys);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("test", "listener canceled", databaseError.toException());
            }
        });


    }


}

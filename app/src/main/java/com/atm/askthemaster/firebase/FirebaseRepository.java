package com.atm.askthemaster.firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.atm.askthemaster.callback.CallBack;
import com.atm.askthemaster.contants.Constants;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

public class FirebaseRepository {

    /**
     * Insert data on FireBase
     *
     * @param databaseReference Database reference of data to be add
     * @param model Model to insert into database
     * @param callBack callback for event handling
     */
    protected final void fireBaseCreate(final DatabaseReference databaseReference, final Object model, final CallBack callBack) {
        databaseReference.keepSynced(true);
        databaseReference.setValue(model, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError == null) {
                    callBack.onSuccess(Constants.SUCCESS);
                } else {
                    callBack.onError(databaseError);
                }
            }
        });

    }

    /**
     * Update data to FireBase
     *
     * @param databaseReference  Database reference of data to update
     * @param map Data map to update
     * @param callBack callback for event handling
     */
    protected final void fireBaseUpdateChildren(final DatabaseReference databaseReference, final Map map, final CallBack callBack) {
        databaseReference.keepSynced(true);
        databaseReference.updateChildren(map, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError == null) {
                    callBack.onSuccess(Constants.SUCCESS);
                } else {
                    callBack.onError(databaseError);
                }
            }
        });
    }
}

package com.atm.askthemaster.firebase;

import com.google.firebase.database.FirebaseDatabase;

public class FirebaseDatabaseReference {
    private FirebaseDatabaseReference() {
    }

    public static final FirebaseDatabase DATABASE = FirebaseDatabase.getInstance();
}

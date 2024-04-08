package com.vertex.io;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivityBuilder {
    private FirebaseAuth mFirebaseAuth;

    public MainActivityBuilder setmFrebaseAuth(FirebaseAuth mFirebaseAuth) {
        this.mFirebaseAuth = mFirebaseAuth;
        return this;
    }

//    public MainActivity createMainActivity() {
//        MainActivity mainActivity = new MainActivity(mFrebaseAuth);
//        return mainActivity;

}
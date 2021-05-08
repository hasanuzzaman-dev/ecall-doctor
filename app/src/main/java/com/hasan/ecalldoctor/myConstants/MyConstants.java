package com.hasan.ecalldoctor.myConstants;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyConstants {

    public static final DatabaseReference DB_REF = FirebaseDatabase.getInstance().getReference("ecall");
}

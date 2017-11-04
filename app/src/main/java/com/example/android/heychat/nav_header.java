package com.example.android.heychat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class nav_header extends AppCompatActivity {

    //Initialising the layout variable
    private TextView mUsernameTextView;

    //Initialising the firebase Elemnts
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_header);

        //referencing gthe layout variable
        mUsernameTextView = (TextView)findViewById(R.id.Username);

        //refernce the firebase elements
        mAuth= FirebaseAuth.getInstance();
        mUsernameTextView.setText(mAuth.getCurrentUser().getDisplayName());

    }
}

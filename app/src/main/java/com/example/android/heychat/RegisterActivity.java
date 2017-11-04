package com.example.android.heychat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.R.attr.data;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static android.os.Build.VERSION_CODES.M;

public class RegisterActivity extends AppCompatActivity {
    //Initialising the layout views
    private EditText mUserNameEditText,mEmailEditText,mPasswordEditText,mFirstName,mLastName;
    private Button  mRegisterButton;
    private Context mContext;
    private String UserEmail,UserPassword,UserName,UserFirstName,UserLastName;
    private Toolbar mToolbar;
    private ProgressDialog mRegProgressDialogue;
    private boolean userNamePresent;
    private List<String> UserName1;

    //Initialising Firebase element
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database;
    private DatabaseReference myRef,mDeviceToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("REGISTRATION");
        //referense the views Element

        mUserNameEditText = (EditText)findViewById(R.id.Username);
        mEmailEditText = (EditText)findViewById(R.id.Email);
        mPasswordEditText = (EditText)findViewById(R.id.Password);
        mFirstName = (EditText)findViewById(R.id.FirstName);
        mLastName = (EditText)findViewById(R.id.LastName);
        mRegisterButton = (Button) findViewById(R.id.Register);
        UserName1  = new ArrayList<String>();
        mToolbar = (Toolbar)findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("REGISTRATION");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRegProgressDialogue = new ProgressDialog(this);


        // reference the firebase Elements
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        mDeviceToken = database.getReference();

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateFields();

            }
        };
        //onClick Register button

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                UserEmail = mEmailEditText.getText().toString().trim();
                UserPassword = mPasswordEditText.getText().toString().trim();
                UserName = mUserNameEditText.getText().toString().trim();
                UserFirstName = mFirstName.getText().toString().trim();
                UserLastName =  mLastName.getText().toString().trim();

                       if(UserName.contains("@") || UserName.contains(".") || UserName.contains("$")|| UserName.contains("[") || UserName.contains("]") || UserName.contains("#"))
                           {
                               Toast.makeText(RegisterActivity.this, "Invalid Character Used-'.$[]#@'", Toast.LENGTH_SHORT).show();
                       }
                       else {

                               if (!TextUtils.isEmpty(UserEmail) && !TextUtils.isEmpty(UserPassword) && !TextUtils.isEmpty(UserName) && !TextUtils.isEmpty(UserFirstName) && !TextUtils.isEmpty(UserLastName)) {


                                   mRegProgressDialogue.setTitle("Registering User");
                                   mRegProgressDialogue.setMessage("Please wait while we create your Account!");
                                   mRegProgressDialogue.setCanceledOnTouchOutside(false);
                                   mRegProgressDialogue.show();
                                   CreateAccount(UserEmail, UserPassword);
                               } else {
                                   Toast.makeText(RegisterActivity.this, "All fields are Mandatory", Toast.LENGTH_SHORT).show();
                               }



                       }


            }
        });


    }



    private void validateFields() {

        if(mUserNameEditText.getText().toString().contains("@"))
        {
            Toast.makeText(mContext, "Please remove @", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void CreateAccount(String Email,String Password)
    {

        mAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("successful",""+task.isSuccessful());
                if(!task.isSuccessful())
                {
                    mRegProgressDialogue.hide();
                    if(task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(RegisterActivity.this, "User Exists! Please Login", Toast.LENGTH_SHORT).show();

                    }
                    else {

                        Toast.makeText(RegisterActivity.this, "Check you entered every thing correctly", Toast.LENGTH_SHORT).show();
                    }
                }

                else
                {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(UserName)
                            .build();
                    if(user!=null) {
                        user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("Account creation", "Account is created");
                                HashMap<String, String> data = new HashMap<String, String>();
                                data.put("Email", UserEmail);
                                data.put("Username", UserName);
                                data.put("FirstName", UserFirstName);
                                data.put("LastName", UserLastName);
                                data.put("Password", UserPassword);
                                data.put("image", "https://firebasestorage.googleapis.com/v0/b/heychat-25011.appspot.com/o/chat_photos%2Fdefault_dp.png?alt=media&token=9f569ff2-34f6-486e-8778-3916469ab126");
                                if(FirebaseInstanceId.getInstance().getToken()!=null)
                                {
                                    data.put("device_token", FirebaseInstanceId.getInstance().getToken());
                                }
                                else
                                {
                                    data.put("device_token", "");
                                }


                               /* HashMap<String, String> data1 = new HashMap<String, String>();
                                data1.put("UserName", UserName);
                                data1.put("image", "https://firebasestorage.googleapis.com/v0/b/heychat-25011.appspot.com/o/chat_photos%2Fdefault_dp.png?alt=media&token=9f569ff2-34f6-486e-8778-3916469ab126");*/
                                // myRef.child("Users").push().setValue(data1);
                                myRef.child("Users").push().setValue(UserName);
                                myRef.child("UsersEmail").push().setValue(UserEmail);
                                myRef.child("group_users").orderByChild("name").equalTo("Hey! Techie").addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                        String key = dataSnapshot.getKey();
                                        myRef.child("group_users").child(key).child(UserName).setValue(true);
                                    }

                                    @Override
                                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                    }

                                    @Override
                                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                                    }

                                    @Override
                                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                myRef.child("group_users").orderByChild("name").equalTo("Hey! Recipe").addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                        String key = dataSnapshot.getKey();
                                        myRef.child("group_users").child(key).child(UserName).setValue(true);
                                    }

                                    @Override
                                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                    }

                                    @Override
                                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                                    }

                                    @Override
                                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                myRef.child("Users_Info").child(UserName).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            mRegProgressDialogue.dismiss();
                                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                            startActivity(intent);

                                        }

                                    }
                                });

                            }
                        });

                    }
                    else
                    {
                        mRegProgressDialogue.dismiss();
                        Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });
    }

}

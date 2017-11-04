package com.example.android.heychat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.android.heychat.Fragments.GroupChatScreenFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GroupCreation extends AppCompatActivity {
//**
    //Initialising the layout variables

    private ListView mMemberListView;
    private Toolbar mGroupCreationToolbar;
    private EditText mGroupName;
    private Button mCreateGroupButton;
    private ArrayList<String> mUserList;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> Users;
    private String groupName;
    public static  final int DEFAULT_GROUPNAME_SIZE=15;


    //Initialising the Firebase elements


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private DatabaseReference myRef1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_creation);

       //Referencing the Layout Elements
        mGroupName = (EditText)findViewById(R.id.Groupname);
        mMemberListView=(ListView)findViewById(R.id.UsersListForGroup);
        mCreateGroupButton=(Button)findViewById(R.id.CreateGroup);
        mGroupCreationToolbar = (Toolbar)findViewById(R.id.group_creation_toolbar);
        setSupportActionBar(mGroupCreationToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("CREATE GROUP");
        mCreateGroupButton.setEnabled(false);
        Users = new ArrayList<String>();
        mUserList = new ArrayList<>();
        adapter = new  ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,mUserList);
        mMemberListView.setAdapter(adapter);

        //referencing the Firebase Elements
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();


        //Enable button only if group name is not empty
        mGroupName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.toString().trim().length()>0)
                {
                    mCreateGroupButton.setEnabled(true);
                }
                else
                {
                    mCreateGroupButton.setEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        mCreateGroupButton.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_GROUPNAME_SIZE)});

        //Displaying whole users list

        myRef.child("Users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                  String value =dataSnapshot.getValue(String.class);
                mUserList.add(value);
                if(mAuth.getCurrentUser() != null)
                {
                    mUserList.remove(mAuth.getCurrentUser().getDisplayName());
                }
                adapter.notifyDataSetChanged();

            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

            //If item is clicked then it is addaed to a users database
        mMemberListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               String abc =mMemberListView.getItemAtPosition(position).toString();
                Users.add(abc);
                if(mMemberListView.getChildAt(position).isEnabled())
                {
                    mMemberListView.getChildAt(position).setEnabled(false);
                }
                else
                {
                    mMemberListView.getChildAt(position).setEnabled(true);
                    Users.remove(mMemberListView.getItemAtPosition(position).toString());
                }
            }
        });

        //When create button is clicked following happens
       mCreateGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Users are",""+Users);
                //Group name that user gave
                groupName= mGroupName.getText().toString().trim();
                // User thaat is creating the group
                String Admin=mAuth.getCurrentUser().getDisplayName();

                //Create hashmap for users status on this group
                HashMap<String, Boolean> map = new HashMap<String, Boolean>();
                for(String s : Users)
                {
                    map.put(s,true);
                }
                map.put(Admin,true);

                //Add group name to groups node
                myRef.child("Groups").push().setValue(groupName);

                // Get the push() id for the group
                String Key  = myRef.child("group_users").push().getKey();

                //Add the data to the required node
                myRef1 = myRef.child("group_users").child(Key);
                myRef1.setValue(map);

                //Set the Admin name and group name in data base
                  myRef1.child("Admin").setValue(Admin);
                myRef1.child("name").setValue(groupName);

                mGroupName.setText(" ");



                Intent intent = new Intent(getApplicationContext(),GroupChat.class);
                intent.putExtra("GROUP_NAME",groupName);
                startActivity(intent);


            }
        });

    }
}

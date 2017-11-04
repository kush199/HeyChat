package com.example.android.heychat;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddUserToGroup extends AppCompatActivity {

    //Initialise the layout Views
    private ListView mAddListView;
    private ArrayList<String> mAddMemberList,mAlreadyMemberList,mMemberSelectedList;
    private Toolbar mAddUserToolbar;
    private Button mAddUserButton,mCancelButton;
    private ArrayAdapter<String> mListAdapter;
    private String abc,key;
    private ImageView mIAddUserButton;

    //Initialise the Firebase elements
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myref,myref1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_to_group);

        //referencee the layout views
        mAddListView = (ListView)findViewById(R.id.add_member_list);
        mAddUserButton = (Button)findViewById(R.id.AddUserToGroup);
        mCancelButton = (Button)findViewById(R.id.CancelUserAdd);
        mIAddUserButton = (ImageView)findViewById(R.id.addIUserToGroup);
        mAddMemberList = new ArrayList<String>();
        mMemberSelectedList = new ArrayList<String>();
        mListAdapter= new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,mAddMemberList);
        mAlreadyMemberList = new ArrayList<String>();
        mAddUserToolbar = (Toolbar)findViewById(R.id.add_user_toolbar);
        setSupportActionBar(mAddUserToolbar);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAddListView.setAdapter(mListAdapter);

        //reference the firebase elements
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myref = database.getReference();

        //Extract the list from previous activity
        Intent intent = getIntent();
        mAlreadyMemberList = intent.getStringArrayListExtra("LIST");

        //Get the Group namme
      Intent intent1 = getIntent();
        abc = intent.getStringExtra("GROUP_NAME");
        getSupportActionBar().setTitle(abc);
        Log.d("abc",""+abc);




        //Display the list of Users

        myref.child("Users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String abc=dataSnapshot.getValue(String.class);
                mAddMemberList.add(abc);
                getWholeList();
                mListAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        //On clicking list member

        mAddListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String user = mAddListView.getItemAtPosition(position).toString();
                mMemberSelectedList.add(user);
                if(mAddListView.getChildAt(position).isEnabled())
                {

                    mAddListView.getChildAt(position).setEnabled(false);
                }
                else
                {
                    mAddListView.getChildAt(position).setEnabled(true);
                    mMemberSelectedList.remove(mAddListView.getItemAtPosition(position).toString());
                }
            }
        });


        //On clicking add button

        mIAddUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMemberSelectedList.size()>0) {
                    myref.child("group_users").orderByChild("name").equalTo(abc).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            key = dataSnapshot.getKey();
                            for (int i = 0; i < mMemberSelectedList.size(); i++) {
                                String Member = mMemberSelectedList.get(i);

                                Log.d("Member", "" + Member);
                                Log.d("key", "" + key);
                                myref1 = myref.child("group_users").child(key).child(Member);
                                myref1.setValue(true);
                            }
                            Toast.makeText(AddUserToGroup.this, "Congrats!You have successfully added member.", Toast.LENGTH_SHORT).show();
                            HideSelectedItem();
                            for(int i=0;i<mListAdapter.getCount();i++) {
                                mAddListView.getChildAt(i).setEnabled(true);
                            }
                            mListAdapter.notifyDataSetChanged();
                        }
                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {}
                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    });
                }
                else{
                    Toast.makeText(AddUserToGroup.this, "You have not selected any member", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //On clicking the cancel button

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getApplicationContext(),GroupChat.class);
                intent2.putExtra("GROUP_NAME",abc);
                startActivity(intent2);

            }
        });



    }
    private ArrayList<String> HideSelectedItem() {

        mAddMemberList.removeAll(mMemberSelectedList);
        return  mAddMemberList;

    }

    public ArrayList<String> getWholeList()
    {
        mAddMemberList.removeAll(mAlreadyMemberList);
        Log.d("array",""+mAddMemberList);
        return mAddMemberList;
    }





}

package com.example.android.heychat.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.heychat.GroupChat;
import com.example.android.heychat.GroupCreation;
import com.example.android.heychat.Model.GroupName;
import com.example.android.heychat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment extends Fragment {

    //Initialising the layut views
    private TextView mCreateGoup;
    private ListView mGroupChatList;
    private ArrayList<String> mGroupList;
    private ArrayAdapter<String> adapter;

    //Initialising Firebase Elements
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database;
    private DatabaseReference myRef,myRef1;

    public FriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
     View view = inflater.inflate(R.layout.fragment_friends,container,false);

        //Referencing the layout Views
        mGroupList = new ArrayList<>();
        mCreateGoup = (TextView)view.findViewById(R.id.CreateGroup);
        mGroupChatList  = (ListView)view.findViewById(R.id.groupList);
        adapter= new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_list_item_1,mGroupList);
        mGroupChatList.setAdapter(adapter);

        //Referencing the Firebase elements

        mAuth=FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();
        myRef=database.getReference();


        // Retrives the whole group list
        Log.d("username",""+mAuth.getCurrentUser().getDisplayName());
        myRef1 = myRef.child("group_users");
        myRef1.orderByChild(mAuth.getCurrentUser().getDisplayName()).equalTo(true).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d:dataSnapshot.getChildren())
                {
                    GroupName groupName = d.getValue(GroupName.class);
                    mGroupList.add(groupName.getName());

                }
                adapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mGroupChatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String abc =mGroupChatList.getItemAtPosition(position).toString();
                Intent intent = new Intent(getActivity().getApplicationContext(),GroupChat.class);
                intent.putExtra("GROUP_NAME",abc);
                startActivity(intent);
            }
        });

        mCreateGoup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(),GroupCreation.class);
                startActivity(intent);
            }
        });



        return view;

    }






}

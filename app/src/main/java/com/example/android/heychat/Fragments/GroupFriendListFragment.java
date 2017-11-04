package com.example.android.heychat.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.android.heychat.AddUserToGroup;
import com.example.android.heychat.GroupChat;
import com.example.android.heychat.Model.MemberName;
import com.example.android.heychat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.R.attr.value;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupFriendListFragment extends Fragment {

    //Initialising the layout views
    private ListView mGroupList;
    private Button mAddFriendButton;
    private ArrayList<String> memberList;
    private ArrayAdapter<String> adapter;
    private String abc;
    private FloatingActionButton mFAddFriendButton;



    //Initialising the Firebase elements
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myref,myref1;



    public GroupFriendListFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_group_friend_list, container, false);

        //referencing the layut elements
         mAddFriendButton =  (Button) view.findViewById(R.id.addGroupMemeber);
         mGroupList = (ListView) view.findViewById(R.id.groupMemberlist);
         mFAddFriendButton=(FloatingActionButton)view.findViewById(R.id.fab);
         memberList = new ArrayList<>();
         adapter = new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_list_item_1,memberList);
         mGroupList.setAdapter(adapter);

        //referncing the firebase eleents

        mAuth= FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myref = mFirebaseDatabase.getReference();


        //retrieve the group name

        GroupChat activty = (GroupChat)getActivity();
        abc = activty.getAbc();
        Log.d("",""+abc);
        //get List of members of the group
        myref1 = myref.child("group_users");
        myref1.orderByChild("name").equalTo(abc).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d("datasnapshot",""+dataSnapshot.toString());
                for(DataSnapshot d :dataSnapshot.getChildren())
                {
                    Map<String,Boolean> map = (Map<String,Boolean>)d.getValue();
                    Log.d("map value",""+map);
                    for(String key: map.keySet() )
                    {
                        memberList.add(key);
                    }
                    memberList.remove("name");
                    memberList.remove("Admin");
                    adapter.notifyDataSetChanged();


                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mFAddFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddUserToGroup.class);
                intent.putExtra("LIST",memberList);
                intent.putExtra("GROUP_NAME",abc);
                startActivity(intent);


            }
        });



        return  view;
    }

}

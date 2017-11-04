package com.example.android.heychat.Fragments;


import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;


import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import com.example.android.heychat.Adapter.UsernamewithPicAdapter;
import com.example.android.heychat.ChatScreen;

import com.example.android.heychat.Model.UserNameWithPic;
import com.example.android.heychat.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.R.attr.value;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {
    //Initialize layout views


   // private ListView mListView;
    private ArrayList<String> mArrayValue,mSearchUserList,mFilteredUserList;
    private List<UserNameWithPic> userNameWithPics;
    private ListView mListView;
    private String[] Url;
    private String log_in,from_user_login,from_user_reg, ClickedItem;
    private EditText mSearchEditText;


    //Initialize firebase element
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database;
    private DatabaseReference myRef;



    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat,container,false);

        //reference the layout views
        mArrayValue = new ArrayList<String>();
        mSearchUserList = new ArrayList<String>();
        mFilteredUserList = new ArrayList<String>();
        //userNameWithPics = new ArrayList<UserNameWithPic>();
        mListView = (ListView)view.findViewById(R.id.List_Friends);
        mSearchEditText  = (EditText)view.findViewById(R.id.searchName);

        // mListView =(ListView)view.findViewById(R.id.List_Friends);
      //  final UsernamewithPicAdapter  usernamewithPicAdapter = new UsernamewithPicAdapter(view.getContext(),userNameWithPics);
        final  ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_list_item_1,mArrayValue);
        final  ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_list_item_1,mSearchUserList);
       //  mListView.setAdapter(usernamewithPicAdapter);

        
        //referencing firebase Instance

        mAuth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();

        //On Entering the edit text
        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               String abc = mSearchEditText.getText().toString();
                mSearchUserList.clear();
                for(String user : mArrayValue)
                {
                    if(user.contains(abc))
                    {

                        mSearchUserList.add(user);

                    }

                }
                adapter1.notifyDataSetChanged();
                mListView.setAdapter(adapter1);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        //Adding users to the arraylist and displaying on main page
        // Use add Child event listener for reading list of data
        myRef.child("Users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
               Log.d("datasnapshots",""+dataSnapshot.toString());

              String user  = dataSnapshot.getValue(String.class);
                mArrayValue.add(user);

                if(mAuth.getCurrentUser() != null) {
                    mArrayValue.remove(mAuth.getCurrentUser().getDisplayName());

                }

                adapter.notifyDataSetChanged();
                mListView.setAdapter(adapter);


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
        // Clicking list item opoens the new chat window

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                ClickedItem =mListView.getItemAtPosition(i).toString();
                Intent intent = new Intent(getActivity().getApplicationContext(),ChatScreen.class);
                intent.putExtra("TO_USER",ClickedItem);
                startActivity(intent);
            }
        });



        return view;
    }

}

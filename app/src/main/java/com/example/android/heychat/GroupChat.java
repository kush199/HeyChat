package com.example.android.heychat;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.android.heychat.Adapter.ChatMessageViewHolder;
import com.example.android.heychat.Adapter.SectionGrooupPagerAdapter;
import com.example.android.heychat.Fragments.GroupChatScreenFragment;
import com.example.android.heychat.Model.Messages;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
//import com.firebase.ui.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class GroupChat extends AppCompatActivity {

    //Initialise the layout elements

    private ViewPager mGroupViewPager;
    private Toolbar mGroupToolbar;
    private TabLayout mGroupTabLayout;
    private SectionGrooupPagerAdapter mSectionGroupPageAdapter;
    GroupChatScreenFragment groupChatScreenFragment;
    public String abc;

    //Initialize the firebase Elements

    private FirebaseAuth mAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);


        //Referencing the layout elements

         mGroupToolbar = (Toolbar)findViewById(R.id.groupchat_toolbar);
         mGroupTabLayout = (TabLayout)findViewById(R.id.group_TabLayout);
         mGroupViewPager = (ViewPager)findViewById(R.id.groupchat_viewpager);
         mSectionGroupPageAdapter = new SectionGrooupPagerAdapter(getSupportFragmentManager());
         mGroupViewPager.setAdapter(mSectionGroupPageAdapter);
         mGroupTabLayout.setupWithViewPager(mGroupViewPager);
         setSupportActionBar(mGroupToolbar);
         getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        //referencing the Firebase elemts

        mAuth = FirebaseAuth.getInstance();
        Firebase.setAndroidContext(this);

        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //Retrieve the data from previous activity
        Intent intent = getIntent();
        abc = intent.getStringExtra("GROUP_NAME");
        Log.d("value is",""+abc);
        getSupportActionBar().setTitle(abc );
        //Send data to fragment
     /*   groupChatScreenFragment =new GroupChatScreenFragment();
        Bundle bundle = new Bundle();
        bundle.putString("GROUP_NAME",abc);
        groupChatScreenFragment.setArguments(bundle);*/






    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.sign_out_menu:
                Intent intent = new Intent(GroupChat.this,StartActivity.class);
                startActivity(intent);
                FirebaseAuth.getInstance().signOut();
                return  true;
            case R.id.AccountSetting:
                Intent intent1 = new Intent(GroupChat.this,AccountSetting.class);
                startActivity(intent1);
                return  true;
            case R.id.AboutUs:
                Intent intent2 = new Intent(GroupChat.this,AboutUs.class);
                startActivity(intent2);
                return  true;
            case R.id.NewsFeed:
                Intent intent3 = new Intent(GroupChat.this,NewsFeed.class);
                startActivity(intent3);
                return  true;
            case R.id.FAQs:
                Intent intent4 = new Intent(GroupChat.this,FAQs.class);
                startActivity(intent4);
                return  true;
            default:
                return super.onOptionsItemSelected(item);


        }

    }

    public String getAbc()
    {
        return abc;
    }
}










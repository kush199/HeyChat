package com.example.android.heychat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.heychat.Adapter.SectionPagerAdapter;
import com.example.android.heychat.Model.Users_Info;
import com.firebase.client.Firebase;
import com.google.android.gms.internal.pi;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.data;
import static android.os.Build.VERSION_CODES.M;

public class MainActivity extends AppCompatActivity {
    //Initialize layout views

    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private SectionPagerAdapter mSectionPagerAdapter;
    private TabLayout mTablayout;
    private TextView mUsernameTextView;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNLayout;
    private ActionBarDrawerToggle mDraweToogle;


    //Initialize firebase element
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);

        //reference the layout views

        mToolbar = (Toolbar)findViewById(R.id.main_page_toolbar);
        mToolbar.setNavigationIcon(R.drawable.nav_icon);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("HEY CHAT");
        mViewPager =(ViewPager) findViewById(R.id.viewPager);
        mSectionPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionPagerAdapter);
        mTablayout=(TabLayout)findViewById(R.id.Main_Tablayout);
        mNLayout = (NavigationView)findViewById(R.id.nView);
        mUsernameTextView = (TextView)mNLayout.getHeaderView(0).findViewById(R.id.Username);

        mDrawerLayout =(DrawerLayout)findViewById(R.id.activity_main);
        setupDrawerContent(mNLayout);
        mTablayout.setupWithViewPager(mViewPager);

        //Set navigation onClick Listener

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Username",""+mAuth.getCurrentUser().getDisplayName());


                mDrawerLayout.openDrawer(GravityCompat.START);

            }
        });





        //referencing firebase Instance

        mAuth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();





    }

    private void setupDrawerContent(NavigationView mNLayout) {

        mNLayout.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                onOptionsItemSelected(item);
                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser cureentUser = mAuth.getCurrentUser();
                if( cureentUser == null)
                {

                    Intent intent = new Intent(MainActivity.this,StartActivity.class);
                    startActivity(intent);
                    finish();
                }
            else {
                    mUsernameTextView.setText(mAuth.getCurrentUser().getDisplayName());
                    Log.d("User id", mAuth.getCurrentUser().getUid());
                    Log.d("Username",mAuth.getCurrentUser().getDisplayName());
                }
    }
    @Override
    protected void onStop() {
        super.onStop();
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.nav_sign_out_menu:
                Intent intent = new Intent(MainActivity.this,StartActivity.class);
                startActivity(intent);
                FirebaseAuth.getInstance().signOut();
                return true;
            case R.id.nav_AccountSetting:
                Intent intent1 = new Intent(MainActivity.this,AccountSetting.class);
                startActivity(intent1);
                return  true;
            case R.id.nav_AboutUs:
                Intent intent2 = new Intent(MainActivity.this,AboutUs.class);
                startActivity(intent2);
                return  true;
            case R.id.nav_NewsFeed:
                Intent intent3 = new Intent(MainActivity.this,NewsFeed.class);
                startActivity(intent3);
                return  true;
            case R.id.nav_FAQs:
                Intent intent4 = new Intent(MainActivity.this,FAQs.class);
                startActivity(intent4);
                return  true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}

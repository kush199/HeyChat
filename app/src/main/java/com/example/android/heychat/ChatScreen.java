package com.example.android.heychat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
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
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.android.heychat.Adapter.ChatMessageViewHolder;
import com.example.android.heychat.Adapter.MessagesAdapter;
import com.example.android.heychat.Model.Messages;
import com.example.android.heychat.Model.UserName;
import com.example.android.heychat.Model.Users_Info;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;


//import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;

import java.util.HashMap;
import java.util.List;

import static android.R.attr.key;
import static android.R.attr.layout;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static com.example.android.heychat.R.id.Username;
import static com.example.android.heychat.R.id.nameTextView;
import static com.example.android.heychat.R.id.visible;

public class ChatScreen extends AppCompatActivity {

    //Initialize the layout views

    private EditText msgEditText;
    private Button mSendButton;
    private RecyclerView mRecyclerView;
    private ImageView mUserProfilePic;
    private TextView mDisplayName;
    ArrayList<Messages> chatMsgList = new ArrayList<Messages>();
    private String fromUser, toUser;
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;
    private Toolbar mToolbar;
    private ImageButton mImagePickerButton;
    private static  final int SELECT_PICTURE=100;
    private ProgressDialog mProgressDialogue;

    //Initialise the firebase Element

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private DatabaseReference mNotification;
    private StorageReference mStorageRef;
    private FirebaseRecyclerAdapter<Messages, ChatMessageViewHolder> mFirebaseAdapter1 = null;
    private FirebaseRecyclerAdapter<Messages, ChatMessageViewHolder> mFirebaseAdapter2 = null;
    Firebase firebase_chatnode = new Firebase("https://heychat-25011.firebaseio.com/Messages");
    Firebase ref_chatchildnode1 = null;
    Firebase ref_chatchildnode2 = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);
        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


        //Reference the Layout Elements

        msgEditText = (EditText) findViewById(R.id.messageEditText);
        mSendButton = (Button) findViewById(R.id.sendButton);
        mImagePickerButton =(ImageButton) findViewById(R.id.photoPickerButton);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutmgr = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(layoutmgr);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mToolbar = (Toolbar)findViewById(R.id.main_page_toolbar);
        mUserProfilePic = (ImageView)findViewById(R.id.UserProfilePic);
        mDisplayName = (TextView)findViewById(R.id.UserDisplayName);
       // mUserProfilePic.setImageResource(R.drawable.default_dp);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);





        //Referencing the Firebase Elements
        Firebase.setAndroidContext(this);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        mNotification=database.getReference();
        //On click of photo button
        mImagePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent,"SELECT iMAGE"),SELECT_PICTURE);
              /*  CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(AccountSetting.this);*/
            }
        });





        //Getting the present user

        fromUser = mAuth.getCurrentUser().getDisplayName();
        Log.d("Email for sender", "" + fromUser);


        //getting the receivers email

        Intent intent = getIntent();
        toUser = intent.getStringExtra("TO_USER");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
       // getSupportActionBar().setTitle(toUser);
        mDisplayName.setText(toUser);
        Log.d("Email for rec", "" + toUser);

        //fetcing the user image
        myRef.child("Users_Info").child(toUser).child("image").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String UserPic = dataSnapshot.getValue(String.class);
                Uri uri1 = Uri.parse(UserPic);
                Picasso.with(ChatScreen.this).load(uri1).into(mUserProfilePic);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ref_chatchildnode1 = firebase_chatnode.child(fromUser + "" + toUser);
        ref_chatchildnode2 = firebase_chatnode.child(toUser + "" + fromUser);

        //Enabling the button when we have msg
        msgEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        msgEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});

        //Click button to write on database
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = msgEditText.getText().toString().trim();
                String sender = fromUser;
                Messages message = new Messages();
                message.setName(sender);
                message.setText(msg);
                HashMap<String,String> map = new HashMap<String, String>();
                map.put("from_user",mAuth.getCurrentUser().getDisplayName());
                map.put("notification","you have new Message from"+fromUser);
                mNotification.child("Notifications").child(toUser).push().setValue(map);
                ref_chatchildnode1.push().setValue(message);
                ref_chatchildnode2.push().setValue(message);
                msgEditText.setText("");


            }
        });

        ref_chatchildnode1.addChildEventListener(new com.firebase.client.ChildEventListener() {
            @Override
            public void onChildAdded(com.firebase.client.DataSnapshot dataSnapshot, String s) {
                Messages chatmsg = dataSnapshot.getValue(Messages.class);
                chatMsgList.add(chatmsg);
            }

            @Override
            public void onChildChanged(com.firebase.client.DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(com.firebase.client.DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(com.firebase.client.DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });
        ref_chatchildnode2.addChildEventListener(new com.firebase.client.ChildEventListener() {
            @Override
            public void onChildAdded(com.firebase.client.DataSnapshot dataSnapshot, String s) {
                Messages chatmsg = dataSnapshot.getValue(Messages.class);
                chatMsgList.add(chatmsg);
            }

            @Override
            public void onChildChanged(com.firebase.client.DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(com.firebase.client.DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(com.firebase.client.DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAdapter1 = new FirebaseRecyclerAdapter<Messages, ChatMessageViewHolder>(Messages.class,
                R.layout.item_message,
                ChatMessageViewHolder.class,
                ref_chatchildnode1) {
            @Override
            protected void populateViewHolder(ChatMessageViewHolder chatMessageViewHolder, Messages m, int i) {

                boolean a = m.getName().toString().equals( mAuth.getCurrentUser().getDisplayName().toString());
                Log.d("value is",""+a);

                if(a)

                {
                    Log.d("inside","hi i am inside");
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.weight = 1.0f;
                    params.gravity = Gravity.RIGHT;
                    chatMessageViewHolder.mImageView.setLayoutParams(params);
                    chatMessageViewHolder.nameTextView.setText(m.getName());
                    chatMessageViewHolder.nameTextView.setLayoutParams(params);
                    chatMessageViewHolder.messageTextView.setLayoutParams(params);
                    chatMessageViewHolder.messageTextView.setText(m.getText());

                }
                else {
                    Log.d("auther",""+m.getName());
                    Log.d("auther1",""+mAuth.getCurrentUser().getDisplayName());
                    Log.d("inside","hi i am inside else");
                    LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params1.weight = 1.0f;
                    params1.gravity = Gravity.LEFT;
                    //      chatMessageViewHolder.mImageView.setImageURI(Uri.parse(m.getImage()));
                    chatMessageViewHolder.mImageView.setLayoutParams(params1);

                    chatMessageViewHolder.nameTextView.setText(m.getName());
                    chatMessageViewHolder.nameTextView.setLayoutParams(params1);
                    chatMessageViewHolder.messageTextView.setLayoutParams(params1);
                    chatMessageViewHolder.messageTextView.setText(m.getText());
                }


            }
        };
        mFirebaseAdapter2 = new FirebaseRecyclerAdapter<Messages, ChatMessageViewHolder>(Messages.class,
                R.layout.item_message,
                ChatMessageViewHolder.class,
                ref_chatchildnode2) {
            @Override
            protected void populateViewHolder(ChatMessageViewHolder chatMessageViewHolder, Messages m, int i) {
                boolean a = m.getName().toString().equals( mAuth.getCurrentUser().getDisplayName().toString());
                Log.d("value is",""+a);

                if(a)

                {
                    Log.d("inside","hi i am inside");
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.weight = 1.0f;
                    params.gravity = Gravity.RIGHT;
                    if(m.getImage() != null) {


                        chatMessageViewHolder.mImageView.setVisibility(View.VISIBLE);
                        chatMessageViewHolder.messageTextView.setVisibility(View.GONE);
                        String UserPic  = m.getImage();
                        Uri uri1 = Uri.parse(UserPic);
                        Picasso.with(ChatScreen.this).load(UserPic).into(chatMessageViewHolder.mImageView);
                        chatMessageViewHolder.mImageView.setLayoutParams(params);
                    }

                    chatMessageViewHolder.nameTextView.setText(m.getName());
                    chatMessageViewHolder.nameTextView.setLayoutParams(params);
                    chatMessageViewHolder.messageTextView.setLayoutParams(params);
                    chatMessageViewHolder.messageTextView.setText(m.getText());

                }
                else {
                    Log.d("auther",""+m.getName());
                    Log.d("auther1",""+mAuth.getCurrentUser().getDisplayName());
                    Log.d("inside","hi i am inside else");
                    LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params1.weight = 1.0f;
                    params1.gravity = Gravity.LEFT;
                    if(m.getImage() != null) {


                        chatMessageViewHolder.mImageView.setVisibility(View.VISIBLE);
                        chatMessageViewHolder.messageTextView.setVisibility(View.GONE);
                        String UserPic  = m.getImage();
                        Uri uri1 = Uri.parse(UserPic);
                        Picasso.with(ChatScreen.this).load(UserPic).into(chatMessageViewHolder.mImageView);
                        chatMessageViewHolder.mImageView.setLayoutParams(params1);
                    }


                    chatMessageViewHolder.nameTextView.setText(m.getName());
                    chatMessageViewHolder.nameTextView.setLayoutParams(params1);
                    chatMessageViewHolder.messageTextView.setLayoutParams(params1);
                    chatMessageViewHolder.messageTextView.setText(m.getText());
                }

            }
        };

        mRecyclerView.setAdapter(mFirebaseAdapter1);
        mRecyclerView.smoothScrollToPosition(mFirebaseAdapter1.getItemCount());
        mFirebaseAdapter1.notifyDataSetChanged();



        mRecyclerView.setAdapter(mFirebaseAdapter2);
        mRecyclerView.smoothScrollToPosition(mFirebaseAdapter1.getItemCount());
        mFirebaseAdapter2.notifyDataSetChanged();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==SELECT_PICTURE && resultCode == RESULT_OK)
        {
            Uri imageUri = data.getData();
            //Toast.makeText(this, "url"+imageUri, Toast.LENGTH_SHORT).show();
            CropImage.activity(imageUri)
                    .setAspectRatio(1,1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                mProgressDialogue = new ProgressDialog(ChatScreen.this);
                mProgressDialogue.setTitle("Uploading Image...");
                mProgressDialogue.setMessage("Please wait while we upload your Image");
                mProgressDialogue.setCanceledOnTouchOutside(false);
                mProgressDialogue.show();

                final Uri resultUri = result.getUri();
                String currentUserId = mAuth.getCurrentUser().getUid();
                Date date = new Date();
                StorageReference filepath = mStorageRef.child("chat_photos1").child(date.getTime()+".jpg");
                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful())
                        {
                            String download_url = task.getResult().getDownloadUrl().toString();
                            mProgressDialogue.dismiss();
                            String sender = fromUser;
                            Messages message = new Messages();
                            message.setName(sender);
                            message.setImage(download_url);
                            ref_chatchildnode1.push().setValue(message);
                            ref_chatchildnode2.push().setValue(message);




                        }
                        else
                        {
                            Toast.makeText(ChatScreen.this, "Error", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chat_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.ClearChat: {
                myRef.child("Messages").child(toUser + "" + fromUser).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(ChatScreen.this, ChatScreen.class);
                        intent.putExtra("TO_USER", toUser);
                        startActivity(intent);
                    }
                });
                return true;
            }
            case R.id.DeleteChat: {
                myRef.child("Messages").child(toUser + "" + fromUser).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Intent intent = new Intent(ChatScreen.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);


        }

    }
}





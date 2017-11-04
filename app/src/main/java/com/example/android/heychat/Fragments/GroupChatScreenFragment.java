package com.example.android.heychat.Fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.android.heychat.Adapter.ChatMessageViewHolder;
import com.example.android.heychat.AddUserToGroup;
import com.example.android.heychat.GroupChat;
import com.example.android.heychat.Model.Messages;
import com.example.android.heychat.R;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

//import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import static android.app.Activity.RESULT_OK;
import static java.lang.System.load;



import static android.app.Activity.RESULT_CANCELED;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupChatScreenFragment extends Fragment {

    //Initialise layout Elements

    private EditText msgGroupEditText;
    private Button mGroupSendButton;
    private RecyclerView mGroupRecyclerView;
    ArrayList<Messages> chatGroupMsgList = new ArrayList<Messages>();
    public static final int DEFAULT_MSG1_LENGTH_LIMIT = 1000;
    private String fromUser;
    private String abc;
    private ProgressDialog mProgressDialogue;
    private ImageButton mPhotoPickerForgroup;
    private static  final int SELECT_PICTURE=100;

    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;
    private FirebaseRecyclerAdapter<Messages, ChatMessageViewHolder> mFirebaseAdapter;
    Firebase firebase_chatnode = new Firebase("https://heychat-25011.firebaseio.com/GroupMessages");
    Firebase ref_chatchildnode1 = null;

    //Initialise the firebase Element

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database;
    private StorageReference mStorageRef;
    private DatabaseReference myRef;


    public GroupChatScreenFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_group_chat_screen, container, false);

        //Recycler View Implementation
        mGroupRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_group_view);
        RecyclerView.LayoutManager layoutmgr = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        mGroupRecyclerView.setLayoutManager(layoutmgr);
        mGroupRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //Referencing the Firebase kElements


        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        fromUser = mAuth.getCurrentUser().getDisplayName();

        //Referencing the layout elements
        msgGroupEditText = (EditText)view.findViewById(R.id.messageEditTextforgroup);
        mGroupSendButton = (Button)view.findViewById(R.id.sendButtonforgroup);
        mPhotoPickerForgroup = (ImageButton) view.findViewById(R.id.photoPickerButtonforgroup);

        // on click of photo picker button
        mPhotoPickerForgroup.setOnClickListener(new View.OnClickListener() {
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


        //retrieve the value send by activity

            GroupChat activity = (GroupChat) getActivity();
            abc = activity.getAbc();
            Log.d("abc", "" + abc);

        ref_chatchildnode1 = firebase_chatnode.child(abc);

        msgGroupEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0) {
                    mGroupSendButton.setEnabled(true);
                } else {
                    mGroupSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        msgGroupEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG1_LENGTH_LIMIT)});

        mGroupSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = msgGroupEditText.getText().toString().trim();
                String sender = fromUser;
                Messages message = new Messages();
                message.setName(sender);
                message.setText(msg);
                ref_chatchildnode1.push().setValue(message);
                msgGroupEditText.setText("");


            }
        });
        ref_chatchildnode1.addChildEventListener(new com.firebase.client.ChildEventListener() {
            @Override
            public void onChildAdded(com.firebase.client.DataSnapshot dataSnapshot, String s) {
                Messages chatmsg = dataSnapshot.getValue(Messages.class);
                chatGroupMsgList.add(chatmsg);
            }

            @Override
            public void onChildChanged(com.firebase.client.DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(com.firebase.client.DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(com.firebase.client.DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mFirebaseAdapter = new FirebaseRecyclerAdapter<Messages, ChatMessageViewHolder>(Messages.class,
                R.layout.item_message,
                ChatMessageViewHolder.class,
                ref_chatchildnode1) {
            @Override
            protected void populateViewHolder(ChatMessageViewHolder chatMessageViewHolder, Messages m, int i) {
                boolean a = m.getName().equals( mAuth.getCurrentUser().getDisplayName());
                Log.d("value is",""+a);
                if(a) {

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.weight = 1.0f;
                    params.gravity = Gravity.RIGHT;
                    if(m.getImage() != null) {


                        chatMessageViewHolder.mImageView.setVisibility(View.VISIBLE);
                        chatMessageViewHolder.messageTextView.setVisibility(View.GONE);
                        String UserPic  = m.getImage();
                        Uri uri1 = Uri.parse(UserPic);
                        Picasso.with(getActivity().getApplicationContext()).load(UserPic).into(chatMessageViewHolder.mImageView);
                        chatMessageViewHolder.mImageView.setLayoutParams(params);
                    }

                    chatMessageViewHolder.nameTextView.setText(m.getName());
                    chatMessageViewHolder.nameTextView.setLayoutParams(params);
                    chatMessageViewHolder.messageTextView.setText(m.getText());
                    chatMessageViewHolder.messageTextView.setLayoutParams(params);
                }
                else
                {
                    LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params1.weight = 1.0f;
                    params1.gravity = Gravity.LEFT;
                    if(m.getImage() != null) {


                        chatMessageViewHolder.mImageView.setVisibility(View.VISIBLE);
                        chatMessageViewHolder.messageTextView.setVisibility(View.GONE);
                        String UserPic  = m.getImage();
                        Uri uri1 = Uri.parse(UserPic);
                        Picasso.with(getActivity().getApplicationContext()).load(UserPic).into(chatMessageViewHolder.mImageView);
                        chatMessageViewHolder.mImageView.setLayoutParams(params1);
                    }

                    chatMessageViewHolder.nameTextView.setText(m.getName());
                    chatMessageViewHolder.nameTextView.setLayoutParams(params1);
                    chatMessageViewHolder.messageTextView.setText(m.getText());
                    chatMessageViewHolder.messageTextView.setLayoutParams(params1);
                }
            }
        };

        mGroupRecyclerView.setAdapter(mFirebaseAdapter);
        mGroupRecyclerView.smoothScrollToPosition(mFirebaseAdapter.getItemCount());
        mFirebaseAdapter.notifyDataSetChanged();

    }

    @Override
    public void onResume() {
        super.onResume();
        GroupChat activity = (GroupChat) getActivity();
        abc = activity.getAbc();
        Log.d("Onresume",""+abc);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==SELECT_PICTURE && resultCode == RESULT_OK)
        {
            Uri imageUri = data.getData();
            //Toast.makeText(this, "url"+imageUri, Toast.LENGTH_SHORT).show();
            CropImage.activity(imageUri)
                    .setAspectRatio(1,1)
                    .start(this.getActivity());
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                mProgressDialogue = new ProgressDialog(getActivity().getApplicationContext());
                mProgressDialogue.setTitle("Uploading Image...");
                mProgressDialogue.setMessage("Please wait while we upload your Image");
                mProgressDialogue.setCanceledOnTouchOutside(false);
                mProgressDialogue.show();

                final Uri resultUri = result.getUri();
                String currentUserId = mAuth.getCurrentUser().getUid();
                StorageReference filepath = mStorageRef.child("group_photos").child(currentUserId+".jpg");
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





                        }
                        else
                        {
                            Toast.makeText(getActivity().getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
            }
        }

    }
}



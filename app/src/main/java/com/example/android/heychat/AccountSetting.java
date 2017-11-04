package com.example.android.heychat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.heychat.Model.Users_Info;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.HashMap;

import static android.R.attr.path;

public class AccountSetting extends AppCompatActivity {

    //Initialising the layout variable
    private EditText mUsernameUpdate, mFirstNameUpdate,  mLastNameUpdate, mEmailUpdate, mPasswordUpdate;
    private ImageView mImageView;
    private Button btnUpload;
    private Button mUpdate;
    private Toolbar mAccountSettingToolbar;
    private  String Username;
    private Uri selectedImageUri;
    private String path;
    private  String UpdateUsername,UpdatePassword,UpdateFirstname,UpdateLastname,UpdateEmail;
    private HashMap<String,String> map;
    private Users_Info usersdata;
    private static  final int SELECT_PICTURE=100;
    private ProgressDialog mProgressDialogue;

    //initialising the Firebase variable
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef,myRef1;
    private StorageReference mStorageRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);

        //referencing the layout kvariable
        mUsernameUpdate = (EditText) findViewById(R.id.UsernameUpdate);
        mFirstNameUpdate = (EditText) findViewById(R.id.FirstNameUpdate);
        mLastNameUpdate = (EditText) findViewById(R.id.LastNameUpdate);
        mEmailUpdate = (EditText) findViewById(R.id.EmailUpdate);
        mPasswordUpdate = (EditText) findViewById(R.id.PasswordUpdate);
        mImageView = (ImageView) findViewById(R.id.UserImage);
         btnUpload = (Button)findViewById(R.id.btnChoosePhoto);
        mUpdate = (Button) findViewById(R.id.Update);
        mAccountSettingToolbar = (Toolbar) findViewById(R.id.Account_Setting_toolbar);
        setSupportActionBar(mAccountSettingToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Account Settings");

        //referencing the Firebase database
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        //get the current user username
        Username = mAuth.getCurrentUser().getDisplayName();

        //On click Upload button
        btnUpload.setOnClickListener(new View.OnClickListener() {
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









    //Get refernce to  the username node and update the data
       myRef1 = myRef.child("Users_Info").child(Username);


        //Display Image when loaded
        myRef.child("Users_Info").child(mAuth.getCurrentUser().getDisplayName()).child("image").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String UserPic = dataSnapshot.getValue(String.class);
                Uri uri1 = Uri.parse(UserPic);
                Picasso.with(AccountSetting.this).load(uri1).into(mImageView);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Retrieve the data from database and show
        myRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

               usersdata  = dataSnapshot.getValue(Users_Info.class);
              mUsernameUpdate.setText(usersdata.getUsername());
              mFirstNameUpdate.setText(usersdata.getFirstName());
              mLastNameUpdate.setText(usersdata.getLastName());
              mPasswordUpdate.setText(usersdata.getPassword());
              mEmailUpdate.setText(usersdata.getEmail());
              mUsernameUpdate.setEnabled(false);
              mEmailUpdate.setEnabled(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        //Write to the database after clicking on the button
        mUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateUsername = mUsernameUpdate.getText().toString().trim();
                UpdatePassword = mPasswordUpdate.getText().toString().trim();
                UpdateEmail = mEmailUpdate.getText().toString().trim();
                UpdateFirstname = mFirstNameUpdate.getText().toString().trim();
                UpdateLastname = mLastNameUpdate.getText().toString().trim();
                if(!TextUtils.isEmpty(UpdateUsername) && !TextUtils.isEmpty(UpdatePassword) && !TextUtils.isEmpty(UpdateEmail)  && !TextUtils.isEmpty(UpdateFirstname)  && !TextUtils.isEmpty(UpdateLastname)) {
                    UpdateInformation();
                }
                else
                {
                    Toast.makeText(AccountSetting.this, "All fields are mandatory!", Toast.LENGTH_SHORT).show();
                }
            }
        });




    }



    private void UpdateInformation() {
        map= new HashMap<String, String>();
        map.put("Email",UpdateEmail);
        map.put("FirstName",UpdateFirstname);
        map.put("LastName",UpdateLastname);
        map.put("Password",UpdatePassword);
        map.put("Username",UpdateUsername);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(UpdateEmail,usersdata.getPassword());
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            user.updatePassword(UpdatePassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        myRef1.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()) {
                                                    Toast.makeText(AccountSetting.this, "Your Profile is updated", Toast.LENGTH_SHORT).show();
                                                }
                                                else
                                                {
                                                    Toast.makeText(AccountSetting.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                    }
                                    else
                                    {
                                        Toast.makeText(AccountSetting.this, "Password must be of six Characters", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                        }

                    }
                });


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
                mProgressDialogue = new ProgressDialog(AccountSetting.this);
                mProgressDialogue.setTitle("Uploading Image...");
                mProgressDialogue.setMessage("Please wait while we upload your Image");
                mProgressDialogue.setCanceledOnTouchOutside(false);
                mProgressDialogue.show();

                final Uri resultUri = result.getUri();
                String currentUserId = mAuth.getCurrentUser().getUid();
                StorageReference filepath = mStorageRef.child("chat_photos").child(currentUserId+".jpg");
                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful())
                        {
                            String download_url = task.getResult().getDownloadUrl().toString();
                            myRef.child("Users_Info").child(mAuth.getCurrentUser().getDisplayName()).child("image").setValue(download_url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        mProgressDialogue.dismiss();
                                        myRef.child("Users_Info").child(mAuth.getCurrentUser().getDisplayName()).child("image").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                String UserPic = dataSnapshot.getValue(String.class);
                                                Uri uri1 = Uri.parse(UserPic);
                                                Picasso.with(AccountSetting.this).load(uri1).into(mImageView);
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                        Toast.makeText(AccountSetting.this, "working", Toast.LENGTH_SHORT).show();
                                    }


                                }
                            });


                        }
                        else
                        {
                            Toast.makeText(AccountSetting.this, "Error", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
            }
        }
    }
}

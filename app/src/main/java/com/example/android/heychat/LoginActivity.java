package com.example.android.heychat;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.service.textservice.SpellCheckerService;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.android.heychat.Model.Users_Info;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.sql.DataSource;

import static android.R.id.message;
import static android.os.Build.VERSION_CODES.M;
import static com.example.android.heychat.R.id.Password;

public class LoginActivity extends AppCompatActivity {
    //initialising the layout variables
    private EditText mLoginEmail,mLoginPassword;
    private  EditText editText;
    private TextView mForgetPassword;
    public Message message;
    private ArrayList<String> email;
    private Button mLoginButton;
    private String mEmail,mPassword,toUser;
    private Toolbar mToolbar;
    private ProgressDialog mProgreesDialogue;
    //Initialising Firebase Element
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mDatabase;

    private DatabaseReference myRef,mDeviceToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("LOGIN");
        //Reference the layout variable

        mLoginEmail = (EditText)findViewById(R.id.LoginEmail);
        mLoginPassword = (EditText)findViewById(R.id.LoginPassword);
        mLoginButton = (Button)findViewById(R.id.LoginButton);
        mToolbar = (Toolbar)findViewById(R.id.main_page_toolbar);
        mForgetPassword = (TextView)findViewById(R.id.ForgetPassword);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("LOGIN");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mProgreesDialogue = new ProgressDialog(this);

        //Reference to firebase element

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference();
        mDeviceToken = mDatabase.getReference();

        //onClick the Login button

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mEmail = mLoginEmail.getText().toString();
                mPassword = mLoginPassword.getText().toString();
                if(!TextUtils.isEmpty(mEmail) && !TextUtils.isEmpty(mPassword)) {
                    mProgreesDialogue.setTitle("Verifing your Credentials");
                    mProgreesDialogue.setMessage("Please wait while we verify you");
                    mProgreesDialogue.setCanceledOnTouchOutside(false);
                    mProgreesDialogue.show();
                    SignInMethod(mEmail, mPassword);
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "All Fields are mandatory!", Toast.LENGTH_SHORT).show();
                }


            }
        });

        mForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgetPassword();
            }
        });
    }

    private void forgetPassword() {
        editText = new EditText(LoginActivity.this);
        AlertDialog.Builder alertDialogue = new AlertDialog.Builder(LoginActivity.this);
        alertDialogue.setTitle("Enter Valid Email");
        alertDialogue.setMessage("We will send your Password to your registered Email");
        alertDialogue.setView(editText);
        alertDialogue.setPositiveButton("SEND",new DialogInterface.OnClickListener()
        {



            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Toast.makeText(LoginActivity.this, "hey u are ok!", Toast.LENGTH_SHORT).show();
                toUser = editText.getText().toString();
            //   if (UserExist()) {

                   myRef.child("Users_Info").orderByChild("Email").equalTo(toUser).addChildEventListener(new ChildEventListener() {
                       @Override
                       public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                          Users_Info info  = dataSnapshot.getValue(Users_Info.class);
                           String password = info.getPassword();
                           final String Username = "livelifekush@gmail.com";
                           final String Password = "JAYA.11101150";
                           Properties properties = new Properties();
                           properties.put("mail.smtp.auth", "true");
                           properties.put("mail.smtp.starttls.enable", "true");
                           properties.put("mail.smtp.host", "smtp.gmail.com");
                           properties.put("mail.smtp.port", "587");


                           Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
                               @Override
                               protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                                   // return super.getPasswordAuthentication();
                                   return new javax.mail.PasswordAuthentication(Username, Password);
                               }
                           });
                           try {
                               message = new MimeMessage(session);
                               message.setFrom(new InternetAddress(Username));
                               message.setRecipients(Message.RecipientType.TO,
                                       InternetAddress.parse(toUser));
                               message.setSubject("Your Password");
                               message.setText("Your password is"+password);

                               new SendfeedbackJob().execute();

                           } catch (MessagingException e) {
                               throw new RuntimeException(e);
                           }

                       }

                       @Override
                       public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                       }
                       @Override
                       public void onChildRemoved(DataSnapshot dataSnapshot) {
                       }
                       @Override
                       public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                       }
                       @Override
                       public void onCancelled(DatabaseError databaseError) {
                       }
                   });


                }
         //  }
        });

        alertDialogue.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(LoginActivity.this, "i am not ok", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog alert = alertDialogue.create();
        alert.show();
    }

   /* private boolean UserExist() {
          myRef.child("UsersEmail").addChildEventListener(new ChildEventListener() {
              @Override
              public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                  email = new ArrayList<String>();
                 String abc = dataSnapshot.getValue(String.class);
                 email.add(abc);
                  findUserExist();
              }

              @Override
              public void onChildChanged(DataSnapshot dataSnapshot, String s) {

              }

              @Override
              public void onChildRemoved(DataSnapshot dataSnapshot) {

              }

              @Override
              public void onChildMoved(DataSnapshot dataSnapshot, String s) {

              }

              @Override
              public void onCancelled(DatabaseError databaseError) {

              }
          });



        return true;
    }*/

   /* private void findUserExist() {

    }*/

    public void SignInMethod(String email, String password)
    {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                Log.d("Successful",""+task.isSuccessful());
                if(!task.isSuccessful())
                {
                    mProgreesDialogue.hide();
                    Toast.makeText(LoginActivity.this, "You have Entered wrong credentials", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    mProgreesDialogue.dismiss();
                    String DeviceToken = FirebaseInstanceId.getInstance().getToken();
                    mDeviceToken.child("Users_Info").child(mAuth.getCurrentUser().getDisplayName()).child("device_token").setValue(DeviceToken);
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private class SendfeedbackJob extends AsyncTask<String, Integer, Void>{
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
                mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
                mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
                mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
                mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
                mc.addMailcap("message/rfc822;; x-java-content- handler=com.sun.mail.handlers.message_rfc822");
                Transport.send(message);
            }
            catch(Exception e)
            {
                Log.e("SendMail",e.getMessage(),e);
            }

            return null;
        }
    }
}

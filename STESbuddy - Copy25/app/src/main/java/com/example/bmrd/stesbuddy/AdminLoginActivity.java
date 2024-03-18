package com.example.bmrd.stesbuddy;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;
//import com.squareup.picasso.Picasso;
//import com.theartofdev.edmodo.cropper.CropImage;
//import com.theartofdev.edmodo.cropper.CropImageView;


//import id.zelory.compressor.Compressor;

public class AdminLoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private DatabaseReference mPostReference;

    private Toolbar mToolbar;

    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;

    private Task<Void> mDatabase;

    //android layout

    private EditText eventDate;
    private EditText vacacyEdit;


    private TextView mName;
    //   private StorageReference mImageStorage;

    private Button mAcceptNGO;
    private Button mAcceptRest;


    private String current_uid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_admin_login);


            mName = (TextView) findViewById(R.id.NGOnameText);



            eventDate = (EditText) findViewById(R.id.dateTextEnter);


            mAcceptNGO = (Button) findViewById(R.id.acceptNGOBTN);
            mAcceptRest = (Button) findViewById(R.id.acceptRestBTN);

            //         mImageStorage= FirebaseStorage.getInstance().getReference();


            ////
            mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

            current_uid = mCurrentUser.getUid();


            mUserDatabase = FirebaseDatabase.getInstance().getReference().child("admin").child(current_uid);



            mUserDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    // Toast.makeText(MainMessActivity.this,dataSnapshot.toString(),Toast.LENGTH_LONG).show();

                    String name = dataSnapshot.child("name").getValue().toString();


                    mName.setText(name);

                    //for image uploading
                    //               Picasso.with(MainMessActivity.this).load(image).into(mImage);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            mAcceptRest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Intent login_intent=new Intent(AdminLoginActivity.this,AcceptRestActivity.class);
                    startActivity(login_intent);




                }
            });



            mAcceptNGO.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Intent login_intent=new Intent(AdminLoginActivity.this,AcceptNGOActivity.class);
                    startActivity(login_intent);




                }
            });





        } catch (Exception e) {
            Toast.makeText(AdminLoginActivity.this, "You are not authorized NGO owner please contact the app owner to get authorized or login as student", Toast.LENGTH_LONG).show();

            //sendToStart
            Intent startIntent = new Intent(AdminLoginActivity.this, MainActivity.class);
            startActivity(startIntent);
            finish();                           //to make sure the user dont come back to main page by pressing back button


            // sendToStart();
        }


    }


}

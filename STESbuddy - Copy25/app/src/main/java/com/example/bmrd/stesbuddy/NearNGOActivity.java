package com.example.bmrd.stesbuddy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class NearNGOActivity extends AppCompatActivity {

    private DatabaseReference mPostReference;

    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;
    private String current_uid;

    private Button mNearByOnly;

    private TextView mTextLatVol;
    private TextView mTextLonVol;

    private TextView mId;



    private RecyclerView mNGOlist;

    private DatabaseReference mNGODatabase;

    private FirebaseAuth mAuth;

   // private String  IdForApplyVol;

    private Toolbar mToolbar;
    private DatabaseReference mDatabase;

    private DatabaseReference mDatabase2;

    private ProgressDialog mRegProgress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_ngo);

        mNGODatabase= FirebaseDatabase.getInstance().getReference().child("NGO_list");

        mNearByOnly=(Button)findViewById(R.id.viewNearByBTN);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        current_uid = mCurrentUser.getUid();




        mTextLatVol=(TextView) findViewById(R.id.textLatVol);
        mTextLonVol=(TextView) findViewById(R.id.textLonVol);

        mId=(TextView) findViewById(R.id.idText);

        mNGOlist=(RecyclerView) findViewById(R.id.NGO_list);
        mNGOlist.setHasFixedSize(true);
        mNGOlist.setLayoutManager(new LinearLayoutManager(this));


        ///these two lines are there to delete previous history of nearby NGOs to the user
        mPostReference = FirebaseDatabase.getInstance().getReference().child("NearByNGO").child(current_uid);
        mPostReference.removeValue();


        mNearByOnly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent login_intent=new Intent(NearNGOActivity.this,OnlyNearNGOActivity.class);
                startActivity(login_intent);

            }
        });


        /*
       // RecyclerView recyclerView = findViewById(R.id.recycler);
        mNGOlist.addOnItemTouchListener(
                new NearNGOActivity(context, mNGOlist ,new NearNGOActivity.OnItemClickListener() {
                     public void onItemClick(View view, int position) {
                        // do whatever
                    }

                     public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
*/


/*
        mApplyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //IdForApplyVol
                String IdForApplyVol= mId.getText().toString();

                addVolunteer(IdForApplyVol);



//                Intent login_intent=new Intent(NearNGOActivity.this,OnlyNearNGOActivity.class);
//                startActivity(login_intent);

            }
        });
*/


        ////////////////////////////////
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Volunteers_loc").child(current_uid);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String latVolString = dataSnapshot.child("Lat").getValue().toString();

                mTextLatVol.setText(latVolString);



                //  Double latVol=Double.parseDouble(latVolString);
                String lonVolString = dataSnapshot.child("Lan").getValue().toString();
                //  lonVol=Double.parseDouble(lonVolString);

                mTextLonVol.setText(lonVolString);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }

    protected void onStart(){
        super.onStart();

        FirebaseRecyclerAdapter<NGO, NGOViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<NGO, NGOViewHolder>(
                NGO.class,
                R.layout.single_ngo_layout,
                NGOViewHolder.class,
                mNGODatabase


        ) {
            @Override
            protected void populateViewHolder(final NGOViewHolder viewHolder, final NGO model, int position) {

                double latNGO=Double.parseDouble(model.getLat());
                double lonNGO=Double.parseDouble(model.getLon());

                System.out.println(latNGO+"((((((((((((((((((((((((((((((((((((");


                Double latVol= Double.parseDouble(mTextLatVol.getText().toString());
                Double lonVol= Double.parseDouble(mTextLonVol.getText().toString());


                System.out.println(latVol+")))))))))))))))))))))))))))))))))))))");
                System.out.println(getDistanceFromLatLonInKm(latNGO,lonNGO,latVol,lonVol)+"-=-=-=-=-=-=-=-=-=--===-=-==");

                double dist= getDistanceFromLatLonInKm(latNGO,lonNGO,latVol,lonVol);

//////////////////////////////////

                System.out.println(dist+"***************************************************////////////////");

               // String IdForApplyVol=model.getId();

                viewHolder.parent_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(NearNGOActivity.this,"You clicked on"+viewHolder.getPosition(),Toast.LENGTH_LONG).show();

                    Intent i= new Intent(NearNGOActivity.this,SelectedNGO.class);

                        String IdForApplyVol=model.getId();

                    Bundle bundle=new Bundle();
                    bundle.putString("id",IdForApplyVol);
                    i.putExtras(bundle);
                    startActivity(i);

                    }
                });



                if(dist<10.0) {

                    register_user(model.getId(), model.getName(), model.getLocation(),model.getDomain(),model.getDate(),model.getVacancy());
                }


                    viewHolder.setName(model.getName());
                    viewHolder.setDomain(model.getDomain());
                    viewHolder.setLocation(model.getLocation());
                viewHolder.setDate(model.getDate());
                viewHolder.setVacancy(model.getVacancy());
                viewHolder.setId(model.getId());



            }
        };


        mNGOlist.smoothScrollToPosition(4);
        mNGOlist.setAdapter(firebaseRecyclerAdapter);
        //System.out.println(mNGOlist.getAdapter().getItemCount()-1+"//////////////////////////******/*/**/*/*/*/*/*");


    }

    public static class NGOViewHolder extends RecyclerView.ViewHolder{

        View mView;

        RelativeLayout parent_layout;

        public NGOViewHolder(View itemView) {
            super(itemView);

            mView=itemView;


            parent_layout=itemView.findViewById(R.id.single_layout);


          //  NGOViewHolder.parent_layout.setOnClickListener



        }

        public void setName(String name){
            TextView NGONameView =(TextView) mView.findViewById(R.id.textName);
            NGONameView.setText(name);

        }

        public void setDomain(String domain){
            TextView NGODomainView =(TextView) mView.findViewById(R.id.textDomain);
            NGODomainView.setText(domain);

        }

        public void setLocation(String location){
            TextView NGOLocationView =(TextView) mView.findViewById(R.id.textLoc);
            NGOLocationView.setText(location);

        }

        public void setVacancy(String vacancy){
            TextView NGOVacancyView =(TextView) mView.findViewById(R.id.vacancyText);
            NGOVacancyView.setText(vacancy);

        }


        public void setDate(String date){
            TextView NGODateView =(TextView) mView.findViewById(R.id.dateText);
            NGODateView.setText(date);

        }


        public void setId(String id){
            TextView NGOIdView =(TextView) mView.findViewById(R.id.idText);
            NGOIdView.setText(id);

        }

    }

    public double getDistanceFromLatLonInKm (double lat1,double lon1,double lat2,double lon2) {
        double R = 6371; // Radius of the earth in km
        double dLat = deg2rad(lat2-lat1);  // deg2rad below
        double dLon = deg2rad(lon2-lon1);
        double a =
                Math.sin(dLat/2) * Math.sin(dLat/2) +
                        Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
                                Math.sin(dLon/2) * Math.sin(dLon/2)
                ;
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c; // Distance in km
        return d;
    }

    public double deg2rad(double deg) {
        return deg * (Math.PI/180);
    }


    // adding this data to new dataset
    private void register_user(final String id, final String ngo_name, final String ngo_location, final String ngo_domain, final String ngo_date, final String ngo_vacancy) {


                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();

                    String uid=current_user.getUid();
//                    mDatabase=FirebaseDatabase.getInstance().getReference().child("NearByNGO").child(uid);
//
//                    HashMap<String,String> VolunteersMap= new HashMap<>();
//                    VolunteersMap.put("id",id);
//                    //VolunteersMap.put("Email_id",email);
//
//                    mDatabase.setValue(VolunteersMap);

        mDatabase=FirebaseDatabase.getInstance().getReference().child("NearByNGO").child(uid).child(id);

        HashMap<String,String> VolunteersMap2= new HashMap<>();
        VolunteersMap2.put("name",ngo_name);
        VolunteersMap2.put("location",ngo_location);
        VolunteersMap2.put("domain",ngo_domain);
        VolunteersMap2.put("vacancy",ngo_vacancy);
        VolunteersMap2.put("date",ngo_date);
        VolunteersMap2.put("id",id);
        mDatabase.setValue(VolunteersMap2);


System.out.println(id+"}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}{}}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{");

                  //  mRegProgress.dismiss();                                 //dismiss progress bar onn success

//                    Intent mainIntent=new Intent(RegisterActivity.this,MainActivity.class);
//                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(mainIntent);
//                    finish();

                }


    private void addVolunteer(final String id) {


        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();

        String uid=current_user.getUid();
//                    mDatabase=FirebaseDatabase.getInstance().getReference().child("NearByNGO").child(uid);
//
//                    HashMap<String,String> VolunteersMap= new HashMap<>();
//                    VolunteersMap.put("id",id);
//                    //VolunteersMap.put("Email_id",email);
//
//                    mDatabase.setValue(VolunteersMap);



        mUserDatabase= FirebaseDatabase.getInstance().getReference().child("Volunteers").child("uid");


        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Toast.makeText(MainMessActivity.this,dataSnapshot.toString(),Toast.LENGTH_LONG).show();

                String name=dataSnapshot.child("Name").getValue().toString();


                FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();

                String uid=current_user.getUid();


                mDatabase2=FirebaseDatabase.getInstance().getReference().child("AplliedVolunteers").child(id).child(uid);

                HashMap<String,String> VolunteersMap2= new HashMap<>();
                VolunteersMap2.put("name",name);

                mDatabase2.setValue(VolunteersMap2);


                //  String image=dataSnapshot.child("image").getValue().toString();


                //for image uploading
                //  Picasso.with(AmbegaoActivity.this).load(image).into(mImage1);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






        System.out.println(id+"}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}{}}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{");

    }




}

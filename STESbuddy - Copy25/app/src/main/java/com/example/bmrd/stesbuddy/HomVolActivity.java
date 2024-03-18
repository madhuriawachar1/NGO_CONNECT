package com.example.bmrd.stesbuddy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomVolActivity extends AppCompatActivity {

    private Button myInfoButton;
    private Button gotSelectedButton;
    private Button allNGOButton;
    private Button getLocButton;
    private Button recommendButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hom_vol);

        myInfoButton=(Button) findViewById(R.id.myInfoBTN);
        gotSelectedButton=(Button) findViewById(R.id.gotSelectedInBTN);
        allNGOButton=(Button) findViewById(R.id.allNGOBTN);
        getLocButton=(Button) findViewById(R.id.getLocBTN);
        recommendButton=(Button) findViewById(R.id.recommendBTN);

        recommendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent login_intent=new Intent(HomVolActivity.this,RecommendActivity.class);
                startActivity(login_intent);

            }
        });


        myInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent login_intent=new Intent(HomVolActivity.this,MyInfoActivity.class);
                startActivity(login_intent);

            }
        });

        gotSelectedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent login_intent=new Intent(HomVolActivity.this,GotSelectedInActivity.class);
                startActivity(login_intent);

            }
        });


        allNGOButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent login_intent=new Intent(HomVolActivity.this,ShowNGOActivity.class);
                startActivity(login_intent);

            }
        });



        getLocButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent login_intent=new Intent(HomVolActivity.this,MapsActivity4.class);
                startActivity(login_intent);

            }
        });






    }
}

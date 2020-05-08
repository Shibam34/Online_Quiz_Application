package com.example.shibam.logindemo2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class SecondActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private Button getquiz;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

       // logout = (Button) findViewById(R.id.btn_logout);
        getquiz = (Button) findViewById(R.id.btn_getquiz);

        firebaseAuth = FirebaseAuth.getInstance();

     /*   logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogOut();
            }
        });*/

        getquiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                startActivity(new Intent(SecondActivity.this,Quiz.class));
                finish();
            }
        });


    }

   /* private void LogOut(){
        firebaseAuth.signOut();
        startActivity(new Intent(SecondActivity.this,MainActivity.class));
        finish();
    }*/
}

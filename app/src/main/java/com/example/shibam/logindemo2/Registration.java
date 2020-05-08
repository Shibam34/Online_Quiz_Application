package com.example.shibam.logindemo2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity {
    private EditText regName,regClg,regRoll,regEmail,regPassword,regMobile;
    private Button registraion;
    private TextView backToLogin;
    private FirebaseAuth firebaseAuth;
    String name,clg,roll,mail,password,mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        regName = (EditText)findViewById(R.id.et_regstudent_name);
        regClg = (EditText)findViewById(R.id.et_regcollege_name);
        regRoll = (EditText)findViewById(R.id.et_regroll);
        regEmail = (EditText)findViewById(R.id.et_regemail);
        regPassword = (EditText)findViewById(R.id.et_regpassword);
        regMobile = (EditText)findViewById(R.id.et_regmobile);
        registraion = (Button)findViewById(R.id.btn_register);
        backToLogin = (TextView)findViewById(R.id.tv_login_scrn);

        firebaseAuth=FirebaseAuth.getInstance();


        registraion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //validate();
                name = regName.getText().toString();
                clg = regClg.getText().toString();
                roll = regRoll.getText().toString();
                mail = regEmail.getText().toString();
                password = regPassword.getText().toString();
                mobile = regMobile.getText().toString();

                if(v.getId()==R.id.btn_register){
                    if(name.trim().length()==0){
                        showerror(1);
                        return;
                    }
                    else if(clg.trim().length()==0 ){
                        showerror(2);
                        return;
                    }
                    else if((mail.trim().length() == 0) || (!mail.contains("@")) || (mail.indexOf("@")<2) || (mail.substring(mail.indexOf('@'),mail.length())).indexOf('.')<3 ){
                        showerror(3);
                        return;
                    }
                    else if(password.trim().length()==0 || password.trim().length()<8){
                        showerror(4);
                        return;
                    }
                    else if(mobile.trim().length()==0 || mobile.trim().length()<10 || mobile.trim().length()>10){
                        showerror(5);
                        return;
                    }

                    String user_email = mail.trim();
                    String user_password = password.trim();

                    firebaseAuth.createUserWithEmailAndPassword(user_email,user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                sendEmailVerification();


                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Registration Failed",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });

        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registration.this,MainActivity.class));
                finish();
            }
        });
    }


    public void showerror(int i){
        switch (i){
            case 1: regName.requestFocus();
                regName.setError("Invalid Name");
                break;
            case 2: regClg.requestFocus();
                regClg.setError("Invalid, N/A if not applicable");
                break;
            case 3: regEmail.requestFocus();
                regEmail.setError("Invalid Email");
                break;
            case 4: regPassword.requestFocus();
                regPassword.setError("Length should be atleast 8 characters");
                break;
            case 5: regMobile.requestFocus();
                regMobile.setError("Invalid Number");
                break;
        }
    }

    private void sendEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        sendUserData();
                        Toast.makeText(getApplicationContext(),"Successfully registered,verification mail sent..",Toast.LENGTH_SHORT).show();




                        firebaseAuth.signOut();
                        startActivity(new Intent(Registration.this,MainActivity.class));
                        finish();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Verification mail hasn't been sent!!",Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }

    private void sendUserData(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myref = firebaseDatabase.getReference(firebaseAuth.getUid());
        UserDatabase userDatabase = new UserDatabase(mail,mobile,name,clg,roll);
        myref.setValue(userDatabase);
    }
}

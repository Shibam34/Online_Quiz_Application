package com.example.shibam.logindemo2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.apache.commons.net.time.TimeTCPClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    private TextView signup,attempts,forgotpassword,timeStart;
    private EditText logname,logpassword;
    private Button login;
    private int counter = 5;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    RequestQueue queue;
    String startDate,endDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signup = (TextView)findViewById(R.id.tv_signup);
        attempts = (TextView)findViewById(R.id.tv_attempts);
        forgotpassword = (TextView)findViewById(R.id.tv_forgot_password);
        logname = (EditText)findViewById(R.id.et_name);
        logpassword = (EditText)findViewById(R.id.et_password);
        timeStart = (TextView)findViewById(R.id.timeStart);
        login = (Button)findViewById(R.id.btn_login);

        attempts.setText("Number of attempts left : "+counter);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        queue = Volley.newRequestQueue(this);

        jsonParse();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId()==R.id.btn_login){
                    if((logname.getText().toString().trim().length() == 0) || (!logname.getText().toString().contains("@")) || (logname.getText().toString().indexOf("@")<2) || (logname.getText().toString().substring(logname.getText().toString().indexOf('@'),logname.getText().toString().length())).indexOf('.')<3 ){
                        showerror1(1);
                        return;
                    }
                    else if(logpassword.getText().toString().trim().length()==0 || logpassword.getText().toString().trim().length()<8){
                        showerror1(2);
                        return;
                    }
                }
                validate(logname.getText().toString().trim(),logpassword.getText().toString().trim());


            }
        });

        if(user!=null){
            startActivity(new Intent(MainActivity.this,SecondActivity.class));
            finish();
        }

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Registration.class));
                finish();
            }
        });

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ForgotPasswordActivity.class));
            }
        });
    }

    private void validate(String userName,String userPassword){

        progressDialog.setMessage("Loading...Please wait...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(userName,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    //Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_SHORT).show();
                    checkEmailVerification();
                }
                else{
                    counter--;
                    attempts.setText("Number of attempts left : "+counter);
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Login Failed",Toast.LENGTH_SHORT).show();
                    if(counter==0){
                        login.setEnabled(false);
                    }
                }
            }
        });
    }
    private void checkEmailVerification(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Boolean emailFlag = firebaseUser.isEmailVerified();

        if(emailFlag){
            Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this,SecondActivity.class));
            finish();
        }
        else{
            Toast.makeText(getApplicationContext(),"Verify your Email",Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }

    public void showerror1(int i) {
        switch (i) {
            case 1:
                logname.requestFocus();
                logname.setError("Invalid Name");
                break;
            case 2:
                logpassword.requestFocus();
                logpassword.setError("Invalid");
                break;
        }
    }

    private void jsonParse(){

        String url ="https://script.google.com/macros/s/AKfycbxOLElujQcy1-ZUer1KgEvK16gkTLUqYftApjNCM_IRTL3HSuDk/exec?id=1q08F35gnOcLRldfZAPY53SovfMAH6ot-bkzntHHZQVI&sheet=Sheet2";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("Sheet2");

                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject item = jsonArray.getJSONObject(i);

                                startDate = item.getString("Start_Time");
                                endDate = item.getString("End_Time");


                                timeStart.setText("StartTime:"+startDate+"\nEndTime :"+endDate);
                            }
                        } catch (JSONException e) {
                            Log.e("error",e.getMessage());
                        }
                        new Time().execute();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(request);
    }

    public class Time extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Log.e("Inside","There");
                TimeTCPClient client = new TimeTCPClient();
                try {
                    // Set timeout of 60 seconds
                    client.setDefaultTimeout(60000);

                    // Connecting to time server
                    // Other time servers can be found at : http://tf.nist.gov/tf-cgi/servers.cgi#
                    // Make sure that your program NEVER queries a server more frequently than once every 4 seconds
                    client.connect("time-e-g.nist.gov");
                    Calendar calendar = Calendar.getInstance();

                    calendar.setTime(client.getDate());
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
                    String actTime = sdf.format(calendar.getTime());
                    Date d = sdf.parse(actTime);
                    Date d2 = sdf.parse(startDate);
                    Date d3 = sdf.parse(endDate);
                    Log.e("My time",sdf.format(calendar.getTime()));
                    Log.e("My Time 2",d.getTime()+"");
                    if(d2.getTime()<d.getTime() && d3.getTime()>d.getTime()){
                        Log.e("Is Valid Time ","true");
                        login.setEnabled(true);
                        login.setClickable(true);
                        login.setBackgroundResource(R.drawable.rectangle_button);

                    }
                    else{
                        Log.e("Is Valid Time ","false");
                        login.setEnabled(false);
                        login.setClickable(false);
                        login.setBackgroundResource(R.drawable.nonclickible);

                    }
                } finally {
                    client.disconnect();
                }
            } catch (Exception e) {
                Log.e("Error",e.getMessage()+"error");
            }
            return null;
        }
    }
}

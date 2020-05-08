package com.example.shibam.logindemo2;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class Quiz extends AppCompatActivity {
    ProgressBar progressBar;
    Layout vertical;
    TextView question, time, points, qno;
    private RequestQueue Queue;
    ImageView start, rule;
    CountDownTimer cdt;
    JSONArray jsonArray;
    Toast t;
    Button op1, op2, op3, op4;
    Drawable yellow, red;
    int count;
    int totPoints=0;
    String correct_option;
    ArrayList<Integer> arr = new ArrayList<Integer>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        progressBar = (ProgressBar) findViewById(R.id.progressBarCircle);
        question = (TextView) findViewById(R.id.question);
        time = (TextView) findViewById(R.id.textViewTime);
        points = (TextView) findViewById(R.id.tvpoint);
        qno = (TextView) findViewById(R.id.tvqno);
        op1 = (Button) findViewById(R.id.op1);
        op2 = (Button) findViewById(R.id.op2);
        op3 = (Button) findViewById(R.id.op3);
        op4 = (Button) findViewById(R.id.op4);
        start = (ImageView) findViewById(R.id.quizstart);
        rule = (ImageView) findViewById(R.id.quizrule);
        yellow = getDrawable(R.drawable.timerstyle);
        red = getDrawable(R.drawable.timerstylered);
        start.setEnabled(false);
        start.setVisibility(View.INVISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                start.setEnabled(true);
                start.setVisibility(View.VISIBLE);
            }
        }, 4000);
        Queue = Volley.newRequestQueue(this);
        jsonParse();
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rule.setVisibility(View.INVISIBLE);
                start.setVisibility(View.INVISIBLE);
                op1.setEnabled(true);
                op2.setEnabled(true);
                op3.setEnabled(true);
                op4.setEnabled(true);
                Collections.shuffle(arr);
                getQuestion(arr.get(count));
                //countdown();
            }
        });

        op1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(op1.getText().toString().equals(correct_option)){
                    totPoints ++;
                    points.setText("Points: "+totPoints+"/"+jsonArray.length());
                }else{
                    t = Toast.makeText(Quiz.this, "Wrong Answer", Toast.LENGTH_SHORT);
                    t.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
                    t.show();
                }
                cdt.onFinish();
            }
        });

        op2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(op2.getText().toString().equals(correct_option)){
                    totPoints ++;
                    points.setText("Points: "+totPoints+"/"+jsonArray.length());
                }else{
                    t = Toast.makeText(Quiz.this, "Wrong Answer", Toast.LENGTH_SHORT);
                    t.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
                    t.show();
                }
                cdt.onFinish();
            }
        });

        op3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(op3.getText().toString().equals(correct_option)){
                    totPoints ++;
                    points.setText("Points: "+totPoints+"/"+jsonArray.length());
                }else{
                    t = Toast.makeText(Quiz.this, "Wrong Answer", Toast.LENGTH_SHORT);
                    t.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
                    t.show();
                }
                cdt.onFinish();
            }
        });

        op4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(op4.getText().toString().equals(correct_option)){
                    totPoints ++;
                    points.setText("Points: "+totPoints+"/"+jsonArray.length());
                }else{
                    t = Toast.makeText(Quiz.this, "Wrong Answer", Toast.LENGTH_SHORT);
                    t.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
                    t.show();
                }
                cdt.onFinish();
            }
        });
    }

    private void jsonParse() {

        String url = "https://script.google.com/macros/s/AKfycbxOLElujQcy1-ZUer1KgEvK16gkTLUqYftApjNCM_IRTL3HSuDk/exec?id=1q08F35gnOcLRldfZAPY53SovfMAH6ot-bkzntHHZQVI&sheet=Sheet1";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            jsonArray = response.getJSONArray("Sheet1");
                            for(int i=0;i<jsonArray.length();i++)
                                arr.add(i);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        Queue.add(request);
    }

    public void getQuestion(int no) {
        try {
            JSONObject jo = jsonArray.getJSONObject(no);
            int sl_no = jo.getInt("Sl_no");
            //Toast.makeText(this, sl_no + "", Toast.LENGTH_SHORT).show();
            qno.setText("Qno : "+(count+1)+"/"+jsonArray.length());
            points.setText("Points : "+totPoints+"/"+jsonArray.length());
            question.setText(jo.getString("Question"));
            op1.setText(jo.getString("Option1"));
            op2.setText(jo.getString("Option2"));
            op3.setText(jo.getString("Option3"));
            op4.setText(jo.getString("Option4"));
            correct_option = jo.getString("Correct_Option");
            countdown();

        } catch (Exception e) {
        }
        count++;
    }

    public void countdown() {
        progressBar.setProgressDrawable(yellow);
        time.setTextColor(Color.parseColor("#F5FA55"));
        cdt = new CountDownTimer(10500, 1000) {
            @Override
            public void onTick(long remainingTime) {
                if (remainingTime < 5000) {
                    progressBar.setProgressDrawable(red);
                    time.setTextColor(Color.parseColor("#ff0000"));
                }
                time.setText(remainingTime / 1000 + "");
                progressBar.setProgress((int) remainingTime / 1000);
            }

            @Override
            public void onFinish() {
                cdt.cancel();
                //++count;
                if (count < jsonArray.length())
                    getQuestion(arr.get(count));
                else {
                    Toast t = Toast.makeText(getApplicationContext(), "No More Questions", Toast.LENGTH_LONG);
                    t.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                    t.show();
                    startActivity(new Intent(Quiz.this,MainActivity.class));
                    finish();
                }
            }
        }.start();
    }

}


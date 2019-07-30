package com.example.trackingapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    EditText username,password,name,bussnmbr,nmbr,child;
    TextView childtxt;
    Button register;
    private RadioGroup radioGroup;
    private RadioButton parentbtn,driverbtn;
    private FirebaseAuth firebaseAuth;


    DatabaseReference dbref1,dbref2,dbref3,dbrefnull;
    FirebaseUser user;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

          username = findViewById(R.id.username);
          password = findViewById(R.id.password);
          name = findViewById(R.id.name);
          bussnmbr = findViewById(R.id.busnmbr);
          nmbr = findViewById(R.id.nmbr);
          childtxt = findViewById(R.id.childtxt);
          child = findViewById(R.id.child);

          radioGroup = (RadioGroup) findViewById(R.id.usertype);
          firebaseAuth = FirebaseAuth.getInstance();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.parentbtn:

                        //final String user_type = "parent";
                        childtxt.setVisibility(View.VISIBLE);
                        child.setVisibility(View.VISIBLE);

                        register = findViewById(R.id.register);
                        register.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                registerParent();
                            }
                        });

                        break;
                    case R.id.driverbtn:

                        childtxt.setVisibility(View.GONE);
                        child.setVisibility(View.GONE);
                        register = findViewById(R.id.register);
                        register.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                registerDriver();
                            }
                        });

                        break;
                }
            }
        });

    }

    private void registerDriver(){

        final String user_type = "driver";
        final  String username1 = username.getText().toString().trim();
        final String password1 = password.getText().toString().trim();
        final String name1 = name.getText().toString().trim();
        final String nmbr1 = nmbr.getText().toString().trim();
        final String bussnmbr1 = bussnmbr.getText().toString().trim();


        if(TextUtils.isEmpty(username1)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password1)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(username1, password1)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){
                            //display some message here
                            user = FirebaseAuth.getInstance().getCurrentUser();
                            uid = user.getUid();

                            dbref1 = FirebaseDatabase.getInstance().getReference("users");
                            dbref1.push().setValue(uid);

                            dbref2 = dbref1.child(uid);

                            Map<String, String> Data = new HashMap<String, String>();
                            Data.put("email",username1);
                            Data.put("name",name1);
                            Data.put("number",nmbr1);
                            Data.put("user_type",user_type);
                            Data.put("bus_number",bussnmbr1);
                            dbref2.setValue(Data);

                            Toast.makeText(SignUpActivity.this,"Successfully registered , Please login now",Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                            startActivity(intent);

                        }else{
                            //display some message here
                            Toast.makeText(SignUpActivity.this,"Registration Error",Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    private void registerParent(){

       // final  String childtxt1 = childtxt.getText().toString().trim();
        final String child1 = child.getText().toString().trim();
        final String user_type = "parent";
        final  String username1 = username.getText().toString().trim();
        final String password1 = password.getText().toString().trim();
        final String name1 = name.getText().toString().trim();
        final String nmbr1 = nmbr.getText().toString().trim();
        final String bussnmbr1 = bussnmbr.getText().toString().trim();

        if(TextUtils.isEmpty(username1)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password1)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(username1, password1)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){
                            //display some message here
                            user = FirebaseAuth.getInstance().getCurrentUser();
                            uid = user.getUid();

                            dbref1 = FirebaseDatabase.getInstance().getReference("users");
                            dbref1.push().setValue(uid);
                            dbref2 = dbref1.child(uid);

                            Map<String, String> Data1 = new HashMap<String, String>();
                            Data1.put("email",username1);
                            Data1.put("name",name1);
                            Data1.put("number",nmbr1);
                            Data1.put("user_type",user_type);
                           // Data.put("bus_number",bussnmbr1);
                          //  Data.put("child1",child1);
                            dbref2.setValue(Data1);
                           dbref3 = dbref2.child("children").child("child1");
                            Map<String, String> Data2 = new HashMap<String, String>();
                            Data2.put("bus_number",bussnmbr1);
                            Data2.put("name",child1);
                            dbref3.setValue(Data2);

                            Toast.makeText(SignUpActivity.this,"Successfully registered , Please login now",Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                            startActivity(intent);

                        }else{
                            //display some message here
                            Toast.makeText(SignUpActivity.this,"Registration Error",Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    }


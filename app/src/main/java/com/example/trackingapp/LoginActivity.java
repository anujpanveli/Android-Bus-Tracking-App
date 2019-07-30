package com.example.trackingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class LoginActivity extends AppCompatActivity {

    EditText username1,password1;
    TextView signup;
    Button parentlogin,login;
    DatabaseReference dbref1,dbref2;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    String uid;

    private FirebaseAuth.AuthStateListener authStateListener;

    private static final String TAG = "Alert";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        username1 =  findViewById(R.id.username);
        password1 =  findViewById(R.id.password);
        signup = findViewById(R.id.sgnup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });

    //    parentlogin = findViewById(R.id.parentbtn);
        login = findViewById(R.id.loginbtn);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(username1.getText().toString().isEmpty() && password1.getText().toString().isEmpty())) {
                    firebaseAuth.signInWithEmailAndPassword(username1.getText().toString().trim(), password1.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                user = FirebaseAuth.getInstance().getCurrentUser();
                                uid = user.getUid();

                                dbref1 = FirebaseDatabase.getInstance().getReference("users");
                                dbref2 = dbref1.child(uid);
                                dbref2.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        //String name = (String) dataSnapshot.child(uid).child("name").getValue();
                                        String userType = (String) dataSnapshot.child("user_type").getValue();
                                        Log.d(TAG,"Before user type");
                                        if(userType.equals("parent")){
                                            String child1 = (String)dataSnapshot.child("children").child("child1").child("name").getValue();
                                            String driverId1 = (String)dataSnapshot.child("children").child("child1").child("driver_id").getValue();

                                               // Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
                                                Intent intent = new Intent(LoginActivity.this, MapsService1.class);
                                                intent.putExtra("driverId1", driverId1);
                                                intent.putExtra("parentUID",uid);
                                                startActivity(intent);
                                                Log.d(TAG,"Inside Parent user type with 1 child");

                                        }else if(userType.equals("driver")){
                                            Intent intent = new Intent(LoginActivity.this, TrackerActivity.class);
                                            intent.putExtra("uid",uid);
                                            startActivity(intent);
                                            Log.d(TAG,"Inside driver user type ");

                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_LONG).show();
                                    }
                                });

                                // ProgressBar.setVisibility(View.GONE);

                                Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Login failed! Please try again later", Toast.LENGTH_LONG).show();
                                // ProgressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }
        });

      }




}






package com.codehunters.usher;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class NameActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText etemail,etname;
    Spinner acctype;
    String type;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    String email,password,name;
    Button singup;
    private DatabaseReference databaseReference;
    String uid;
    FirebaseUser userid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(getResources().getColor(R.color.Trans));

        setContentView(R.layout.activity_name);
        etname=findViewById(R.id.editTextName);
        etemail=findViewById(R.id.editTextEmail);
        firebaseAuth = FirebaseAuth.getInstance();
         userid = firebaseAuth.getCurrentUser();
         singup=findViewById(R.id.signup);
        Bundle bundle=getIntent().getExtras();
        email=bundle.getString("email");
        password=bundle.getString("password");

        etemail.setText(email);
        acctype=findViewById(R.id.acctype);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");


        progressDialog = new ProgressDialog(this);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.accountType,R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        acctype.setAdapter(adapter);
        acctype.setOnItemSelectedListener(this);
        singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        type=parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private void registerUser() {
        progressDialog.setMessage("Signing up....");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    uploaddata();



                }
                else {
                    Toast.makeText(getApplicationContext(),"Unable to signup!Try Again",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    private void uploaddata(){
        user userdata=new user(email,etname.getText().toString(),type,"null","null");
        Toast.makeText(getApplicationContext(),email+etname.getText().toString()+type,Toast.LENGTH_LONG).show();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userid.getUid());
        databaseReference.setValue(userdata).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();

                    finish();
                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));

                }
            }
        });





    }
}


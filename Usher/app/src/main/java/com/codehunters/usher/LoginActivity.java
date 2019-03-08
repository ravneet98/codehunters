package com.codehunters.usher;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText etemail;
    EditText etpassword;
    Button singin;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private CheckBox checkBox_remembermMe;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private TextView textViewForgotPassword,textViewsignup;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(getResources().getColor(R.color.Trans));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_login);
        sharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = sharedPreferences.edit();
        checkBox_remembermMe = (CheckBox) findViewById(R.id.checkBox_rememberMe);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        //CHECKING IF USER HAS LOGINED AND STATE OF REMEMBER ME CHECK BOK
        if (firebaseAuth.getCurrentUser() != null && sharedPreferences.getBoolean("state", false)) {
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        etemail=findViewById(R.id.editTextEmail);
        etpassword=findViewById(R.id.password);
        singin=findViewById(R.id.login);
        textViewForgotPassword = (TextView) findViewById(R.id.textviewForgotPassword);
        textViewsignup=findViewById(R.id.textViewsignup);


        singin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();

            }
        });
        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ForgotPassword.class));
            }
        });
        textViewsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SignupActivity.class));
            }
        });
    }
    private void userLogin() {
        String email = etemail.getText().toString().trim();
        String password = etpassword.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Signing in....");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    finish();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));

                } else {
                    Toast.makeText(LoginActivity.this, "Could not Sign In", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}

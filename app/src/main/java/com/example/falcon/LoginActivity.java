package com.example.falcon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private TextInputLayout logPasswordTV,logEmailTV;
    private Button loginAccountbtn;
    private ProgressBar progressBar;
    private static final String TAG = "MyActivity";
    private Toolbar loginToolbar;
    private ProgressDialog loginProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        loginToolbar=findViewById(R.id.loginAppBar);
        setSupportActionBar(loginToolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loginProgressDialog=new ProgressDialog(this);
        logEmailTV=findViewById(R.id.logEmail);
        logPasswordTV=findViewById(R.id.logPassword);
        loginAccountbtn=findViewById(R.id.logbtn);


        loginAccountbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password=logPasswordTV.getEditText().getText().toString();
                String email=logEmailTV.getEditText().getText().toString();
                if(!TextUtils.isEmpty(email)&&!(TextUtils.isEmpty(password))){
                    loginProgressDialog.setTitle("Logging In");
                    loginProgressDialog.setMessage("Please wait while we check your data");
                    loginProgressDialog.setCanceledOnTouchOutside(false);
                    loginProgressDialog.show();
                    loginUser(email,password);
                }
            }
        });
    }



    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    loginProgressDialog.dismiss();
                    Intent mainIntent=new Intent(LoginActivity.this,MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);
                    finish();
                }else{
                    loginProgressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "error occurred ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

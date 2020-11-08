package com.example.falcon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StatusActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private TextInputLayout editStatusTV;
    private Button saveStatusBtn;
    //progress dialog
    private ProgressDialog progressDialog;

    //Firebase Code
    private DatabaseReference statuesDatabaseRederence;
    private FirebaseUser userFirebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        //Firebase
        userFirebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        String current_uid=userFirebaseUser.getUid();
        statuesDatabaseRederence= FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);

        mToolbar=findViewById(R.id.mainAppBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Account Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String status_value=getIntent().getStringExtra("statues_value");
        progressDialog=new ProgressDialog(this);
        editStatusTV=findViewById(R.id.editStatuesTV);
        saveStatusBtn=findViewById(R.id.statusSaveBtn);
        editStatusTV.getEditText().setText(status_value);
        saveStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //progree dialog
                progressDialog.setTitle("Saving Changes");
                progressDialog.setMessage("Please wait while we save changes ...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                String status=editStatusTV.getEditText().getText().toString();
                statuesDatabaseRederence.child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                        }else{
                            Toast.makeText(StatusActivity.this, "Error occur ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}

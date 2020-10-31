package com.example.falcon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity {
    Button reqbtn;
    Button logbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        reqbtn=findViewById(R.id.startReqbtn);
        reqbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reqIntent=new Intent(StartActivity.this,RegisterActivity.class) ;
                startActivity(reqIntent);

            }
        });
        logbtn=findViewById(R.id.logbtn);
        logbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logIntent=new Intent(StartActivity.this,LoginActivity.class) ;
                startActivity(logIntent);
            }
        });
    }
}

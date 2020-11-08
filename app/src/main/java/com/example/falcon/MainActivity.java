package com.example.falcon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Toolbar mainToolbar;
    private ViewPager mViewpager;
    private SectionsPageradapter mSectionsPageradapter;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        mainToolbar=findViewById(R.id.mainAppBar);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setTitle("Falcon Chat ");


        //tabs

        mViewpager= (ViewPager) findViewById(R.id.mainTabPager);
        try{
        mSectionsPageradapter=new SectionsPageradapter(getSupportFragmentManager());

        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        mViewpager.setAdapter(mSectionsPageradapter);
        mTabLayout=(TabLayout) findViewById(R.id.mainTab);
        mTabLayout.setupWithViewPager(mViewpager);

    }

        @Override
        public void onStart () {
        try {

        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
            FirebaseUser currentUser = mAuth.getCurrentUser();

            if (currentUser == null) {
                sendToStartActivity();
            }
        }catch(Exception e){
                Log.v(e.getMessage(),"Error");
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }

    }



    private void sendToStartActivity() {
        Intent startIntent=new Intent(MainActivity.this,StartActivity.class);
        startActivity(startIntent);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);
         getMenuInflater().inflate(R.menu.main_menu,menu);
         return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.mainLogoutBtn){
            FirebaseAuth.getInstance().signOut();
            //send to start activity after logout
            sendToStartActivity();
        }else if(item.getItemId()==R.id.mainSettingBtn){
            Intent settingsIntent=new Intent(MainActivity.this,SettingsActivity.class);
            startActivity(settingsIntent);
        }else if(item.getItemId()==R.id.mainAllUsersBtn){
            Intent usersIntent=new Intent(MainActivity.this,UsersActivity.class);
            startActivity(usersIntent);
        }

        return true;
    }
}

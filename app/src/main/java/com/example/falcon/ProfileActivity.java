package com.example.falcon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {
    private ImageView profileImageView;
    private TextView profileNameTV,profileStatusTV,profileFriendsCount;
    private Button profileSendAddBTN;
    private DatabaseReference profileDatabaseReference;
    private ProgressDialog profileProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profileProgressDialog=new ProgressDialog(this);
        profileProgressDialog.setTitle("Loading User Data ....");
        profileProgressDialog.setMessage("Please wait while we load user data");
        profileProgressDialog.setCanceledOnTouchOutside(false);
        profileProgressDialog.show();

        profileImageView=findViewById(R.id.profileImageView);
        profileNameTV=findViewById(R.id.profileNameTV);
        profileStatusTV=findViewById(R.id.profileStatusTV);
        profileFriendsCount=findViewById(R.id.countFriendsTV);
        profileSendAddBTN=findViewById(R.id.profileFriendRequestBTN);
        String user_id=getIntent().getStringExtra("user_id");
        profileDatabaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

        profileDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String profileName=snapshot.child("name").getValue().toString();
                String profileStatus=snapshot.child("status").getValue().toString();
                String profileImage=snapshot.child("image").getValue().toString();
                profileNameTV.setText(profileName);
                profileStatusTV.setText(profileStatus);
                Picasso.get().load(profileImage).into(profileImageView);
                profileProgressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}

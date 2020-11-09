
package com.example.falcon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersActivity extends AppCompatActivity {
    private Toolbar usersToolbar;
    private RecyclerView  usersRecyclerView;
    private DatabaseReference usersDatabaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        usersToolbar=findViewById(R.id.usersAppbar);
        setSupportActionBar(usersToolbar);
        getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        usersDatabaseReference= FirebaseDatabase.getInstance().getReference().child("Users");

        usersRecyclerView=findViewById(R.id.usersRecyclerView);
        usersRecyclerView.setHasFixedSize(true);
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Users,UserViewHolder>firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Users, UserViewHolder>(
                Users.class,
                R.layout.user_item,
                UserViewHolder.class,
                usersDatabaseReference
        ) {
            @Override
            protected void populateViewHolder(UserViewHolder userViewHolder, Users users, int i) {
                userViewHolder.setName(users.getName());
                userViewHolder.setStatus(users.getStatus());
                userViewHolder.setUserImage(users.getThumb_image(),getApplicationContext());
                final String userID=getRef(i).getKey().toString();
                userViewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent profileIntent=new Intent(UsersActivity.this,ProfileActivity.class);
                        profileIntent.putExtra("user_id",userID);
                        startActivity(profileIntent);
                    }
                });

            }
        };
       usersRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder{
        static View view;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            view=itemView;
        }
        public static void setName(String name){
            TextView nameTV=view.findViewById(R.id.nameTV);
            nameTV.setText(name);
        }
        public static void setStatus(String status){
            TextView statusTV=view.findViewById(R.id.statusTV);
            statusTV.setText(status);
        }

        public void setUserImage(String thumb_image, Context context){
            CircleImageView userImageCircleView=view.findViewById(R.id.userImageCRV);
            Picasso.get().load(thumb_image).into(userImageCircleView);
        }
    }
}

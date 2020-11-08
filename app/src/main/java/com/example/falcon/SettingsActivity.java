package com.example.falcon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.PrivateKey;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class SettingsActivity extends AppCompatActivity {
    private static final int MAX_LENGTH =10 ;
    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;
    private TextView nameTV;
    private TextView statusTV;
    private CircleImageView userImageCIMG;
    private Button changeStatusBtn,imageBtn;

    private static final int GALLERY_PICK=1;

    private ProgressDialog settingsProgressDialog;
    //Storage Firebase
    private StorageReference imageStorage;

    byte[] thumb_byte;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        nameTV=findViewById(R.id.setgsDisplayNameTV);
        statusTV=findViewById(R.id.stgsStatusTV);
        userImageCIMG=findViewById(R.id.settingsImage);
        changeStatusBtn=findViewById(R.id.stgsStatuesBtn);
        imageBtn=findViewById(R.id.stgsImangBtn);
        imageStorage= FirebaseStorage.getInstance().getReference();
        mCurrentUser= FirebaseAuth.getInstance().getCurrentUser();
        String current_uid=mCurrentUser.getUid();
        mUserDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               String name=snapshot.child("name").getValue().toString();
               String image="default";
               try{
                   image=snapshot.child("image").getValue().toString();
               }catch (Exception e){

               }

               String status=snapshot.child("status").getValue().toString();
               nameTV.setText(name);
               statusTV.setText(status);
               if(!image.equals("default")) {
                   Picasso.get().load(image).into(userImageCIMG);

               }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        changeStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String statues_value=statusTV.getText().toString();
                Intent statusIntent=new Intent(SettingsActivity.this,StatusActivity.class);
                statusIntent.putExtra("statues_value",statues_value);
                startActivity(statusIntent);
            }
        });

        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent=new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent,"SELECT IMAGE"),GALLERY_PICK);

            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_PICK&&resultCode==RESULT_OK) {
            Uri imageUri=data.getData();

            // start cropping activity for pre-acquired image saved on the device
            CropImage.activity(imageUri)
                    .setAspectRatio(1,1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                settingsProgressDialog=new ProgressDialog(SettingsActivity.this);
                settingsProgressDialog.setTitle("Uploading Image ...");
                settingsProgressDialog.setMessage("Please wait while we upload and process the image ");
                settingsProgressDialog.setCanceledOnTouchOutside(false);
                settingsProgressDialog.show();

                Uri resultUri = result.getUri();
                File thumb_filePath=new File(resultUri.getPath());

                String currentUserId=mCurrentUser.getUid().toString();

                try {
                    Bitmap thumb_bitmap = new Compressor(this)
                            .setMaxWidth(200)
                            .setMaxHeight(200)
                            .setQuality(75)
                            .compressToBitmap(thumb_filePath);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    thumb_byte = baos.toByteArray();
                } catch (IOException e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

                final StorageReference filePath=imageStorage.child("profile_image").child(currentUserId+".jpg");
                StorageReference thumb_filebath=imageStorage.child("profile_image").child("thumbs").child(currentUserId+".jpg");
                filePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        final Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                        firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                final String downloadUrl = uri.toString();
                                mUserDatabase.child("image").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            settingsProgressDialog.dismiss();
                                        }else{
                                            settingsProgressDialog.dismiss();
                                        }
                                    }
                                });
                            }
                        });
                    }
                });

/*                 //code deprecated
                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            String download_url = task.getResult().getUploadSessionUri().toString();
                            mUserDatabase.child("image").setValue(download_url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        settingsProgressDialog.dismiss();
                                    }
                                }
                            });

                            settingsProgressDialog.dismiss();
                        }else{
                            Toast.makeText(SettingsActivity.this, "Error in uploading", Toast.LENGTH_SHORT).show();
                            settingsProgressDialog.dismiss();
                        }
                    }
                });
                */

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(MAX_LENGTH);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }
}

package com.example.firestore_img;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import id.zelory.compressor.Compressor;

public class MainActivity extends AppCompatActivity {

    private ImageView userImage;
    private EditText userName;
    private Button submit;
    private ProgressDialog progressDialog;
    private Uri imageUri = null;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;


    private String user_id;
    private Bitmap compressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userName=findViewById(R.id.name);
        userImage=findViewById(R.id.imageView);
        submit = findViewById(R.id.submit);


        firebaseAuth = FirebaseAuth.getInstance();


        user_id = firebaseAuth.getCurrentUser().getUid();


        firebaseFirestore = FirebaseFirestore.getInstance();


        storageReference = FirebaseStorage.getInstance().getReference();

        userImage.setOnClickListener(new View.OnClickListener() {


                                         @Override


                                         public void onClick(View view) {


                                             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


                                                 if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {


                                                     Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();


                                                     ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);


                                                 } else {


                                                     choseImage();

                                                 }


                                             } else {


                                                 choseImage();

                                             }


                                         }

                                     }


        );




        submit.setOnClickListener(new View.OnClickListener() {


            @Override


            public void onClick(View view) {


                progressDialog.setMessage("Storing Data...");


                progressDialog.show();


                final String username = userName.getText().toString();

                if(!TextUtils.isEmpty(username)){


                    File newFile = new File(imageUri.getPath());


                    try {


                        compressed = new Compressor(MainActivity.this)


                                .setMaxHeight(125)


                                .setMaxWidth(125)


                                .setQuality(50)


                                .compressToBitmap(newFile);


                    } catch (IOException e) {


                        e.printStackTrace();


                    }

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();


                    compressed.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);


                    byte[] thumbData = byteArrayOutputStream.toByteArray();


                    UploadTask image_path = storageReference.child("user_image").child(user_id + ".jpg").putBytes(thumbData);
                        image_path.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                if (task.isSuccessful()){


                                    storeData(task, username);
                                }
                                else {


                                    String error = task.getException().getMessage();


                                    Toast.makeText(MainActivity.this, "(IMAGE Error) : " + error, Toast.LENGTH_LONG).show();


                                    progressDialog.dismiss();


                                }


                            }
                        });







                }    }






        } );




    }




    private void choseImage() {



        CropImage.activity()


                .setGuidelines(CropImageView.Guidelines.ON)


                .setAspectRatio(1, 1)


                .start(MainActivity.this);

    }




    private void storeData(Task<UploadTask.TaskSnapshot> task, String username) {

        Uri download_uri;



        if (task != null) {



            download_uri = task.getResult().getDownloadUrl();



        } else {



            download_uri = imageUri;


        }
        Map<String, String> userData = new HashMap<>();



        userData.put("userName",username);

        firebaseFirestore.collection("Users").document(user_id).set(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "mast kaam", Toast.LENGTH_SHORT).show();
                }

            }
        });






    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {


            CropImage.ActivityResult result = CropImage.getActivityResult(data);


            if (resultCode == RESULT_OK) {


                imageUri = result.getUri();


                userImage.setImageURI(imageUri);


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {


                Exception error = result.getError();


            }

        }



}}

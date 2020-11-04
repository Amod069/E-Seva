package com.example.e_seva;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class complaint extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner Area_from_comp,complainttype;
    String sub_ward,download_url;
    String comp_type;
    Button register,chooseimg;
    EditText name,address_person,descrip,contact;

    ImageView imagechoose;

    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private StorageReference storageReference;
    private StorageTask uploadtask;

    private Uri imageuri;


    private static final String KEY_USERNAME = "Username";
    private static final String KEY_DESCRIPTION = "Description";
    private static final String KEY_AREA = "Area";
    private static final String KEY_ADDRESS = "Address";
    private static final String KEY_DATE = "Date";
    private static final String KEY_IMAGE = "Image";
    private static final String KEY_CONTACT = "Contact_Number";
    ProgressDialog dialog;
    ProgressDialog dialog1;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_complaint );
        
        db =FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("Images");

        Area_from_comp = findViewById(R.id.Area_complaint);
        complainttype=findViewById(R.id.complaint_type);
        name =findViewById(R.id.name);
        address_person =findViewById(R.id.address);
        descrip = findViewById(R.id.description);
        register =findViewById(R.id.register);
        contact=findViewById(R.id.contact);

        chooseimg = findViewById(R.id.chooseimage);
        imagechoose=findViewById(R.id.imagechoose);
       /* StrictMode.ThreadPolicy policy= new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);*/
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.complaints_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        complainttype.setAdapter(adapter);
         complainttype.setOnItemSelectedListener(this);
        complainttype.setPrompt("Select Complaint Type");
        dialog = new ProgressDialog(complaint.this);
        dialog1 = new ProgressDialog(complaint.this);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.Area_from, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Area_from_comp.setAdapter(adapter1);
        Area_from_comp.setOnItemSelectedListener(this);
        Area_from_comp.setPrompt("Select Your Area");
        imagechoose.setVisibility(View.GONE);








        chooseimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withActivity(complaint.this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        Filechooser();

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

            }
        });




    }

    private void Filechooser(){


                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imageuri =data.getData();
            imagechoose.setImageURI(imageuri);
            chooseimg.setVisibility(View.GONE);
            imagechoose.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.Area_complaint){
            String area_from_where = parent.getItemAtPosition(position).toString();
            if(area_from_where.toString().equals("Thankur Complex") || area_from_where.toString().equals("Thakur Village") ||
                    area_from_where.toString().equals("Lokhandwala Complex") ||area_from_where.toString().equals("Patel nagar")
                    || area_from_where.toString().equals("MG Road Kandivali") || area_from_where.toString().equals("Mathuradas Road")||area_from_where.equals("Damupada")
                    || area_from_where.equals("Charkop Khade")||area_from_where.equals("Hanuman Nagar")||area_from_where.equals("Poisar River")||area_from_where.equals("Mahavir Nagar")
                    ||area_from_where.equals("Miiltary Road")||area_from_where.equals("Lala Lajapatrai Road")||area_from_where.equals("Khajura Talov Road")||area_from_where.equals("Ganesh Nagar")
                    ||area_from_where.equals("FCI")||area_from_where.equals("Samata Nagar")||area_from_where.equals("Kandivali Fire Station")){
                sub_ward = "R South";
            }
            else if(area_from_where.equals("Dahisar") || area_from_where.equals("Poisar Depot")||area_from_where.equals("Dhaisar Check Naka")){
                sub_ward = "R North";

            }
            else if(area_from_where.equals("Oshiwara") || area_from_where.equals("Goregoan")){
                sub_ward="P South";

            }
            else if(area_from_where.equals("Malad Station") || area_from_where.equals("Chincholi Bunder Road")||area_from_where.equals("Malvani")
                    || area_from_where.equals("Marve Road")|| area_from_where.equals("Gen.Vaidya Marg")||area_from_where.equals("Kurar Village(East)")||
                        area_from_where.equals("Madh(West)")){
                sub_ward = "P North";

            }
            else {
                Toast.makeText(this, "Something Went Wrong Please try Again", Toast.LENGTH_SHORT).show();
            }


        }
        else if(parent.getId() == R.id.complaint_type){
            String comp_type = parent.getItemAtPosition(position).toString();
        }
        else{
            Toast.makeText(this, "Something Went Wrong ,Please Try Again ", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void upload(View View){
       if(uploadtask!=null && uploadtask.isInProgress()){
            Toast.makeText(this, "Your Complain is Getting Register", Toast.LENGTH_SHORT).show();
        }else{
            Register_Complaint();
        }

    }
    public void Register_Complaint() {



        if(TextUtils.isEmpty(address_person.getText().toString()) && TextUtils.isEmpty(descrip.getText().toString()) && TextUtils.isEmpty(name.getText().toString()) && TextUtils.isEmpty(contact.getText().toString())){
            address_person.setError("Please fill your address");
            descrip.setError("Please fill the description");
            name.setError("Please fill the username");
            contact.setError("Please Enter Proper Contact Number.");
        }
        if(TextUtils.isEmpty(address_person.getText().toString())){
            address_person.setError("Please fill your address");
            return;

        }else if(TextUtils.isEmpty(descrip.getText().toString())){
            descrip.setError("Please fill the description");
            return;
        }
        else if(descrip.getText().toString().length()<=25){
            descrip.setError("Description to short");
            return;
        }
        else if(TextUtils.isEmpty(name.getText().toString())){
            name.setError("Please fill the username");
            return;
        }
        else if(TextUtils.isEmpty(contact.getText().toString())){
            contact.setError("Please fill the contact No.");
            return;
        }
        else if(contact.length()<10){
            contact.setError("Please Enter Proper Contact Number.");
            return;
        }




        final StorageReference Ref = storageReference.child(firebaseUser.getUid()+System.currentTimeMillis()+"."+getExtension(imageuri));


       uploadtask = Ref.putFile(imageuri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        dialog1.setTitle("Registering Your Complaints");
                        dialog1.setMessage("Please Wait..");
                        dialog1.show();
                        Task<Uri> result = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                        result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                download_url = uri.toString();
                                SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy");
                                String date_format = s.format(new Date());
                                String comp_type= complainttype.getSelectedItem().toString();
                                String address = Area_from_comp.getSelectedItem().toString();
                                String username = name.getText().toString();
                                String address_per = address_person.getText().toString();
                                String Descrip = descrip.getText().toString();
                                String Contact = contact.getText().toString();
                                Map<String, Object> note = new HashMap<>();
                                note.put(KEY_USERNAME,username);
                                note.put(KEY_AREA,address);
                                note.put(KEY_ADDRESS,address_per);
                                note.put(KEY_DESCRIPTION,Descrip);
                                note.put(KEY_DATE,date_format);
                                note.put(KEY_IMAGE,download_url);
                                note.put(KEY_CONTACT,Contact);


                                db.collection("Complaints").document(comp_type).collection(sub_ward).document().set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        dialog1.dismiss();
                                        Toast.makeText(complaint.this, "Your Complaint is Register SuccessFully", Toast.LENGTH_SHORT).show();
                                        Toast.makeText(complaint.this, "Thank You", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(complaint.this, home_screenn.class));
                                        Intent intenthome = new Intent(complaint.this, home_screenn.class);
                                        intenthome.addFlags(intenthome.FLAG_ACTIVITY_CLEAR_TOP);
                                        intenthome.addFlags(intenthome.FLAG_ACTIVITY_CLEAR_TASK);
                                       }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(complaint.this, "Complaint Registration Failed please Try Again", Toast.LENGTH_SHORT).show();

                                    }
                                });


                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        Toast.makeText(complaint.this, "Image Failed To Upload", Toast.LENGTH_SHORT).show();
                    }
                });


    }



    private String getExtension(Uri uri) {
        if(uri==null){
            Toast.makeText(this, "Please Select The Image", Toast.LENGTH_SHORT).show();
            chooseimg.setError("Select Image");
        }
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));

    }




}


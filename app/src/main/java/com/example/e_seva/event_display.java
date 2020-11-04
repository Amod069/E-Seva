package com.example.e_seva;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;


public class event_display extends AppCompatActivity {

    private FirebaseFirestore db;
    TextView E_Date,E_Venue,E_Organizer,E_Description,E_Title;
    ImageView E_Image;
    String Document_ID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView ( R.layout.display_info);
        E_Date=findViewById(R.id.event_entity_date);
        E_Venue=findViewById(R.id.event_entity_venue);
        E_Organizer=findViewById(R.id.event_entity_organizer);
        E_Description=findViewById(R.id.event_entity_description);
        E_Title=findViewById(R.id.event_entity_title);
        E_Image=findViewById(R.id.event_entity_image);
        db=FirebaseFirestore.getInstance();
       /* Bundle extras = getIntent().getExtras();
        if(extras!=null){
         Document_ID= extras.getString("Document_ID");
        }*/
        String path = db.collection("Events").document().getPath().toString();
        String[] document_name = path.split("/");
        Document_ID=document_name[1];



        final ProgressDialog dialog = ProgressDialog.show(event_display.this, "",
                "Loading Data Please wait...", true);
        try {
            db.collection("Events").document(Document_ID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        String organizer, title, venue, date, descrip, image;
                        organizer = documentSnapshot.getString("Organizer");
                        title = documentSnapshot.getString("Title");
                        venue = documentSnapshot.getString("Venue");
                        date = documentSnapshot.getString("Date");
                        descrip = documentSnapshot.getString("Description");
                        image = documentSnapshot.getString("Image");
                        E_Date.setText(date);
                        E_Organizer.setText(organizer);
                        E_Description.setText(descrip);
                        E_Title.setText(title);
                        E_Venue.setText(venue);
                        Picasso.with(event_display.this).load(image).into(E_Image);
                        dialog.dismiss();

                    } else
                        Toast.makeText(event_display.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.show(event_display.this,"Something went wrong","Kuch toh galat huva",true);
                    Toast.makeText(event_display.this, "Error 404", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
        }catch (Exception e){
            Toast.makeText(this,e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        finally {

        }
    }
}

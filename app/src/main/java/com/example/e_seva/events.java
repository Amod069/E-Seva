package com.example.e_seva;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

public class events extends AppCompatActivity {
    private RecyclerView firestore_list;
    private FirebaseFirestore db;
    private FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.events_recyler);

        firestore_list=findViewById(R.id.events_recyler1);
        db = FirebaseFirestore.getInstance();
        Query query = db.collection("Events");
        FirestoreRecyclerOptions<Events_Field> options = new FirestoreRecyclerOptions.Builder<Events_Field>()
                .setQuery(query, Events_Field.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<Events_Field, EventsViewHolder>(options) {
            @NonNull
            @Override
            public EventsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_events, parent, false);
                return new EventsViewHolder(view);


            }

            @Override
            protected void onBindViewHolder(@NonNull EventsViewHolder eventsViewHolder, int i, @NonNull Events_Field events_field) {
                Picasso.with(events.this).load(events_field.getEvent_image()).into(eventsViewHolder.event_image);
                eventsViewHolder.Date.setText(events_field.getDate());
                eventsViewHolder.Venue.setText(events_field.getVenue());
                eventsViewHolder.event_title.setText(events_field.getEvent_title());
                eventsViewHolder.organizer.setText(events_field.getOrganizer());
                eventsViewHolder.description.setText(events_field.getDescription());
            }
        };
        firestore_list.setHasFixedSize(true);
        firestore_list.setLayoutManager(new LinearLayoutManager(this));
        firestore_list.setAdapter(adapter);

    }

    private class EventsViewHolder extends RecyclerView.ViewHolder {
        ImageView event_image;
        TextView Date,Venue,event_title,organizer,description;

        public EventsViewHolder(@NonNull View itemView) {
            super(itemView);
            event_image=itemView.findViewById(R.id.event_image);
            Date =itemView.findViewById(R.id.event_date);
            Venue=itemView.findViewById(R.id.event_venue);
            event_title=itemView.findViewById(R.id.event_title);
            organizer=itemView.findViewById(R.id.event_organizer);
            description=itemView.findViewById(R.id.event_description);

        }
    }
    /*public void send_data(){
        Intent intent_d = new Intent(events.this, event_display.class);
        startActivity(intent_d);

    }*/

    @Override
    protected void onStart() {
        super.onStart();

        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}

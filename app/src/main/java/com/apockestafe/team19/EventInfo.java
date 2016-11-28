package com.apockestafe.team19;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.facebook.FacebookSdk;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class EventInfo extends AppCompatActivity {

    private Button mapButton, listRideButton, backButton, inviteButton, itemsButton;
    private DatabaseReference ref;
    private  FirebaseDatabase database;
    private TextView eventDescription;
    private ListView friendsListView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> enums;
    SharedPreferencesEditor editor;
    //WebDialog dialog;
    //GameRequestDialog requestDialog;
    String s;
//    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        editor = new SharedPreferencesEditor(getSharedPreferences("login", MODE_PRIVATE));

        setContentView(R.layout.activity_event_info);

        eventDescription = (TextView) findViewById(R.id.eventDescription);

        s = getIntent().getStringExtra("eventNumber");

        friendsListView = (ListView) findViewById(R.id.friendsListView);
        TextView textView = new TextView(this);
        textView.setText("Who is Attending:");
        friendsListView.addHeaderView(textView);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("TEAM19");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String description = (String) dataSnapshot.child("events").child(s).child("description").getValue();
                String time = (String) dataSnapshot.child("events").child(s).child("time").getValue();
                String date = (String) dataSnapshot.child("events").child(s).child("date").getValue();
                String location = (String) dataSnapshot.child("events").child(s).child("location").getValue();
                eventDescription.setText("Time: " + time + " " + date + "\nEvent Description:\n" + description);

                setTitle((String) dataSnapshot.child("events").child(s).child("title").getValue());

                String myName = editor.getName();
                GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {};
                ArrayList<String> names = dataSnapshot.child("events").child(s)
                        .child("attendingList").getValue(t);
                if (names == null) {
                    names = new ArrayList<>();
                    names.add(myName);
                    ref.child("events").child(s).child("attendingList").setValue(names);
                } else {
                    int index = Collections.binarySearch(names, myName);
                    if(index < 0) {
                        names.add(myName);
                        ref.child("events").child(s).child("attendingList").setValue(names);
                    }
                }
                adapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1, names);
                friendsListView.setAdapter(adapter);
//                listHandler();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        itemsButton = (Button) findViewById(R.id.itemsButton);
        itemsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(EventInfo.this, ItemsActivity.class);
                i.putExtra("eventNumber", s);
                startActivity(i);
            }
        });

        mapButton = (Button)findViewById(R.id.MapButton);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EventInfo.this, MapsActivity.class);
                i.putExtra("eventNumber", s);
                startActivity(i);
            }
        });

        listRideButton = (Button)findViewById(R.id.listRideButton);
        listRideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EventInfo.this, ListRideActivity.class);
                i.putExtra("eventNumber", s);
                startActivity(i);
            }
        });

        backButton = (Button)findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EventInfo.this, MainActivity.class));
                finish();
            }
        });
        inviteButton = (Button)findViewById(R.id.inviteButton);
        inviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonHandler();
            }
        });

        listHandler(ref);
    }

    public void buttonHandler() {
        final String s = getIntent().getStringExtra("eventNumber");
        String appLinkUrl, previewImageUrl;
        appLinkUrl = "https://fb.me/1033003976822219";
        previewImageUrl = "www.google.com";
        if(AppInviteDialog.canShow()) {
            AppInviteContent content = new AppInviteContent.Builder()
                    .setApplinkUrl(appLinkUrl)
                    .setPreviewImageUrl(previewImageUrl)
                    .setPromotionDetails("eventNumber", s)
                    .build();
            AppInviteDialog.show(this,content);
        }
    }

    public void listHandler(DatabaseReference ref2) {
        DatabaseReference r = ref2.child("events").child(s).child("attendingList");
        r.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {};
//                enums = dataSnapshot.child("events").child(s).child("attendingList").getValue(t);
                enums = dataSnapshot.getValue(t);
                if (enums == null)
                    enums = new ArrayList<>();
                adapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1, enums);
                friendsListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

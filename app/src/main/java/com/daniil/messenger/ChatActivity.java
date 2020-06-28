package com.daniil.messenger;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.daniil.messenger.Adapters.MessageAdapter;
import com.daniil.messenger.Models.Chat;
import com.daniil.messenger.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private Intent intent;
    private FirebaseUser user;
    private DatabaseReference ref;
    private TextView text;
    private ImageView profilepic;
    private EditText chatbox;
    private ImageView sendMessage;
    private String savedMessage;
    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private List<Chat> mchat;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
        text = findViewById(R.id.chatName);
        profilepic = findViewById(R.id.chatProfilePic);
        Toolbar toolbar = findViewById(R.id.chatToolBar);
        chatbox = findViewById(R.id.chatbox);
        sendMessage = findViewById(R.id.sendmessage);
        user = FirebaseAuth.getInstance().getCurrentUser();
        recyclerView = findViewById(R.id.chatscreen);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linManager = new LinearLayoutManager(getApplicationContext());
        linManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linManager);


        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        intent = getIntent();
        toolbar.setNavigationIcon(R.drawable.back_button);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        String userId = intent.getStringExtra("userId");
        Log.d("chat",userId);
        ref = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User fuser = dataSnapshot.getValue(User.class);
                text.setText(fuser.nick);
                if(fuser.linkPhoto.equals("default")){
                    Glide.with(getApplicationContext()).load(R.drawable.defaultprofile).into(profilepic);
                } else{
                    Glide.with(getApplicationContext()).load(fuser.linkPhoto).into(profilepic);
                }
                readMessage(user.getUid(),userId,fuser.getLinkPhoto());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        ref.addValueEventListener(vListener);

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!chatbox.getText().toString().matches("")){
                    savedMessage = chatbox.getText().toString();
                    sendMessage(savedMessage, user.getUid(), userId);
                    chatbox.setText("");
                } else{
                    Toast.makeText(getApplicationContext(),"Please send message", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void sendMessage(String message, String sender, String recipient){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hash = new HashMap<>();
        hash.put("sender",sender);
        hash.put("message",message);
        hash.put("recipient",recipient);
        reference.child("Chats").push().setValue(hash);
    }
    private void readMessage(String myID, String userID, String imageURL){
        mchat = new ArrayList<>();
        ref = FirebaseDatabase.getInstance().getReference("Chats");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mchat.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if(chat.getRecipient().equals(myID) && chat.getSender().equals(userID) || chat.getRecipient().equals(userID) && chat.getSender().equals(myID)){
                        mchat.add(chat);
                    }
                    messageAdapter = new MessageAdapter(getApplicationContext(),mchat,imageURL);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

package com.daniil.messenger.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.daniil.messenger.Adapters.UsersAdapter;
import com.daniil.messenger.Models.Chat;
import com.daniil.messenger.Models.User;
import com.daniil.messenger.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class MessagesFragment extends Fragment {

    private RecyclerView recyclerView;
    private UsersAdapter usersAdapter;
    private List<User> userList;
    private FirebaseUser fUser;
    private DatabaseReference ref;
    private List<String> usernameList;
    private List<String> chatType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);
        recyclerView = view.findViewById(R.id.chatList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        usernameList = new ArrayList<>();
        ref = FirebaseDatabase.getInstance().getReference("Chats");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usernameList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    Toast.makeText(getContext(),chat.getType(),Toast.LENGTH_SHORT).show();
                        if(chat.getSender().equals(fUser.getUid())){
                            usernameList.add(chat.getRecipient());
                        }
                        if(chat.getRecipient().equals(fUser.getUid())){
                            usernameList.add(chat.getSender());
                        }
                        readChat();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        // Inflate the layout for this fragment
        return view;
    }
    private void readChat(){
        userList = new ArrayList<>();
        ref = FirebaseDatabase.getInstance().getReference("users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    for(String id : usernameList){
                        if(user.getUid().equals(id)){
                            if(userList.size() != 0){
                                for(User user1 : userList){
                                    if(!user.getUid().equals(user1.getUid())){
                                        userList.add(user);
                                    }
                                }
                            } else{
                                userList.add(user);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        usersAdapter = new UsersAdapter(getContext(),userList, chatType);
        recyclerView.setAdapter(usersAdapter);
    }
}

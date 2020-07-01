package com.daniil.messenger.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.daniil.messenger.ChatActivity;
import com.daniil.messenger.Models.Chat;
import com.daniil.messenger.Models.User;
import com.daniil.messenger.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.CustomViewHolder> {

    private Context context;
    private List<Chat> chatResults;
    public static final int MESSAGE_TYPE_LEFT = 0;
    public static final int MESSAGE_TYPE_RIGHT = 1;
    private String imageURL;
    private FirebaseUser fUser;

    class CustomViewHolder extends RecyclerView.ViewHolder {

        public final View mView;

        TextView showMessage;
        ImageView profilePic;
        ConstraintLayout constraintLayout;
        CustomViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            showMessage = mView.findViewById(R.id.message);
            profilePic = mView.findViewById(R.id.profileimage);
            constraintLayout = mView.findViewById(R.id.userConstraintLayout);
        }
    }
    public MessageAdapter(Context context, List<Chat> itemResults,String imageURL){
        this.imageURL = imageURL;
        this.context = context;
        this.chatResults = itemResults;
    }
    @Override
    public MessageAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == MESSAGE_TYPE_RIGHT){
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.CustomViewHolder(view);
        }
        else{
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.CustomViewHolder(view);
        }
    }
    @Override
    public void onBindViewHolder(MessageAdapter.CustomViewHolder holder, final int position) {
        Chat chat = chatResults.get(position);
        holder.showMessage.setText(chat.getMessage());
        if(imageURL.equals("default")){
            Glide.with(context).load(R.drawable.defaultprofile).into(holder.profilePic);
        }
        else{
            Glide.with(context).load(imageURL).into(holder.profilePic);
        }
    }

    @Override
    public int getItemCount() {
        return chatResults.size();
    }

    @Override
    public int getItemViewType(int position) {
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        if(chatResults.get(position).getSender().equals(fUser.getUid())){
            return MESSAGE_TYPE_RIGHT;
        }
        else{
            return MESSAGE_TYPE_LEFT;
        }
    }
}

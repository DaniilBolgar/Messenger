package com.daniil.messenger.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.daniil.messenger.ChatActivity;
import com.daniil.messenger.Models.User;
import com.daniil.messenger.R;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.CustomViewHolder> {

    private Context context;
    private List<User> userResults;
    private List<String> chatType;

    class CustomViewHolder extends RecyclerView.ViewHolder {

        public final View mView;

        TextView userName;
        ImageView profilePic;
        ConstraintLayout constraintLayout;
        CustomViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            userName = mView.findViewById(R.id.userName);
            profilePic = mView.findViewById(R.id.userProfile);
            constraintLayout = mView.findViewById(R.id.userConstraintLayout);
        }
    }
    public UsersAdapter(Context context, List<User> itemResults, List<String> chatType){
        this.context = context;
        this.userResults = itemResults;
        this.chatType = chatType;
    }
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.user_item, parent, false);
        return new CustomViewHolder(view);
    }
    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
        Log.d("email",userResults.get(position).email);
        holder.userName.setText(userResults.get(position).getNick());
            if(!userResults.get(position).getLinkPhoto().equals("default")) {
                Glide.with(context).load(userResults.get(position).getLinkPhoto()).into(holder.profilePic);
            }
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("userId",userResults.get(position).getUid());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userResults.size();
    }
}

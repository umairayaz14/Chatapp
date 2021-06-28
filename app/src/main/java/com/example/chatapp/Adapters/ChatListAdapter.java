package com.example.chatapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.Activites.Chatting;
import com.example.chatapp.R;
import com.example.chatapp.models.ChatListModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

    Context mContext;
    List<ChatListModel> list;

    public ChatListAdapter(Context mContext, List<ChatListModel> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.omnio_chat_list_design , parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.userID.setText(list.get(position).getGroupKey());
        holder.username.setText(list.get(position).getGroupName());
        holder.lastMsg.setText(list.get(position).getLastMessage());

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Chatting.class);
                intent.putExtra("key",list.get(position).getGroupKey());
                intent.putExtra("name",list.get(position).getGroupName());
                intent.putExtra("lastmsg",list.get(position).getLastMessage());
                mContext.startActivity(intent);
            }
        });

    }



    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout mainLayout;
        CircleImageView userImg;
        TextView username;
        TextView lastMsg;
        TextView userID;

        public ViewHolder(View itemView) {
            super(itemView);
            userImg = (CircleImageView)itemView.findViewById(R.id.userimg);
            username = (TextView) itemView.findViewById(R.id.username);
            lastMsg = (TextView)itemView.findViewById(R.id.lastmsg);
            userID = (TextView)itemView.findViewById(R.id.userid);
            mainLayout = (RelativeLayout)itemView.findViewById(R.id.mainLayout);
        }
    }

}

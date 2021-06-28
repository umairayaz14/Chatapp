package com.example.chatapp.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.models.MessageModel;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class MessagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context ctx;
    private List<MessageModel> items;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences prefs;

    public static final int MSG_BY_ME = 0;
    public static final int MSG_BY_YOU = 5;


    public MessagesAdapter(Context ctx, List<MessageModel> items) {
        this.ctx = ctx;
        this.items = items;
        prefs = ctx.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(ctx);

        switch (viewType) {
            case MSG_BY_ME :
                View v1 = inflater.inflate(R.layout.message_byme, parent, false);
                viewHolder = new SendTextView(v1);
                break;
            case MSG_BY_YOU :
                View v5 = inflater.inflate(R.layout.message_byyou, parent, false);
                viewHolder = new ReceiveTextView(v5);
                break;
            default: return null;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof SendTextView) {
                ((SendTextView) holder).text_content.setText(items.get(position).getContent());
                ((SendTextView) holder).text_time.setText(items.get(position).getDateTime());

        }
        else if(holder instanceof  ReceiveTextView){
                ((ReceiveTextView) holder).text_content.setText(items.get(position).getContent());
                ((ReceiveTextView) holder).text_time.setText(items.get(position).getDateTime());
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    @Override
    public int getItemViewType(int position) {

        int returnView = -1;

        if(items.get(position).getSender().equals(prefs.getString("userID",null))){
            if(items.get(position).getMsgType().equals("text")){
                returnView = MSG_BY_ME;
            }

        }
        else{
            if(items.get(position).getMsgType().equals("text")){
                returnView = MSG_BY_YOU;
            }
        }
        return returnView;
    }

    public void insertItem(MessageModel message) {
        this.items.add(message);
        notifyItemInserted(getItemCount());
    }

    public class SendTextView extends RecyclerView.ViewHolder {
        public View lyt_parent;
        public TextView text_content;
        public TextView text_time;

        public SendTextView(@NonNull View itemView) {
            super(itemView);
            this.text_content = (TextView) itemView.findViewById(R.id.text_content);
            this.text_time = (TextView) itemView.findViewById(R.id.text_time);
            this.lyt_parent = itemView.findViewById(R.id.lyt_parent);


        }
    }

    public class ReceiveTextView extends RecyclerView.ViewHolder {
        public View lyt_parent;
        public TextView text_content;
        public TextView text_time;

        public ReceiveTextView(@NonNull View itemView) {
            super(itemView);
            this.text_content = (TextView) itemView.findViewById(R.id.text_content);
            this.text_time = (TextView) itemView.findViewById(R.id.text_time);
            this.lyt_parent = itemView.findViewById(R.id.lyt_parent);
        }
    }

}


package com.example.chatapp.Activites;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.chatapp.Adapters.MessagesAdapter;
import com.example.chatapp.R;
import com.example.chatapp.models.MessageModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Chatting extends AppCompatActivity {

    TextView groupName,onlinestatus;

    MessagesAdapter messagesAdapter;
    List<MessageModel> messagelList;
    RecyclerView recyclerView;

    EditText sms_content;
    FloatingActionButton btn_send;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences prefs;
    private RequestQueue mRequestQue;
    private String URL = "https://fcm.googleapis.com/fcm/send";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        mRequestQue = Volley.newRequestQueue(Chatting.this);
        groupName = (TextView)findViewById(R.id.groupName);
        onlinestatus = (TextView)findViewById(R.id.onlinestatus);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        messagelList = new ArrayList<>();
        messagesAdapter = new MessagesAdapter(Chatting.this,messagelList);
        sms_content = (EditText)findViewById(R.id.text_content);
        btn_send = (FloatingActionButton)findViewById(R.id.btn_send);
        recyclerView.setLayoutManager(new LinearLayoutManager(Chatting.this));

        groupName.setText(getIntent().getStringExtra("name"));
        onlinestatus.setText("");
        getAllMessages();
        getTypingIndicator();

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sms_content.getText().toString().isEmpty()){
                    Toast.makeText(Chatting.this, "Can't send empty message", Toast.LENGTH_SHORT).show();
                }
                else{
                    SendMessage();
                }
            }
        });

        sms_content.addTextChangedListener(new TextWatcher() {

            boolean isTyping = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            private Timer timer = new Timer();
            private final long DELAY = 3000; // milliseconds

            @Override
            public void afterTextChanged(final Editable s) {
                if(sms_content.getEditableText().toString().isEmpty())
                {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("GroupChatList").child(getIntent().getStringExtra("key"));
                    reference.child("TypingStatus").setValue("");
                }
                else{
                    Log.d("", "");
                    if(!isTyping) {
                        Log.d("TAG", "started typing");
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("GroupChatList").child(getIntent().getStringExtra("key"));
                        reference.child("TypingStatus").setValue(prefs.getString("userName",null)+" is typing...");
                        // Send notification for start typing event
                        isTyping = true;
                    }
                    timer.cancel();
                    timer = new Timer();
                    timer.schedule(
                            new TimerTask() {
                                @Override
                                public void run() {
                                    isTyping = false;
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("GroupChatList").child(getIntent().getStringExtra("key"));
                                    reference.child("TypingStatus").setValue("");
                                    Log.d("TAG", "stopped typing");
                                    //send notification for stopped typing event
                                }
                            },
                            DELAY
                    );
                }
            }
        });


    }


    private void SendMessage() {
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String currentDateandTime = sdf.format(currentTime);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ChatMessages").child(getIntent().getStringExtra("key"));
        String MessageID = reference.push().getKey();

        HashMap<String , String> messageMap = new HashMap<>();
        messageMap.put("Sender",prefs.getString("userID",null));
        messageMap.put("SenderName",prefs.getString("userName",null));
        messageMap.put("MsgType","text");
        messageMap.put("Content",sms_content.getText().toString());
        messageMap.put("DateTime",currentDateandTime);
        messageMap.put("MsgID",MessageID);
        reference.child(MessageID).setValue(messageMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("GroupChatList").child(getIntent().getStringExtra("key"));
                            FirebaseMessaging.getInstance().unsubscribeFromTopic("user");
                            reference2.child("LastMessage").setValue(sms_content.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            sendNotification(sms_content.getText().toString());
                                            sms_content.setText("");
                                        }
                                    FirebaseMessaging.getInstance().subscribeToTopic("user");
                                }
                            });
                        }
                    }
                });
    }

    private void getAllMessages(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ChatMessages").child(getIntent().getStringExtra("key"));
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagelList.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    String sender = snapshot1.child("Sender").getValue().toString();
                    String senderName = snapshot1.child("SenderName").getValue().toString();
                    String type = snapshot1.child("MsgType").getValue().toString();
                    String content = snapshot1.child("Content").getValue().toString();
                    String time = snapshot1.child("DateTime").getValue().toString();
                    String msgid = snapshot1.child("MsgID").getValue().toString();

                    messagelList.add(new MessageModel(sender,senderName,type,content,time,msgid));

                }
                messagesAdapter = new MessagesAdapter(Chatting.this,messagelList);
                recyclerView.setAdapter(messagesAdapter);
                if(recyclerView.getAdapter().getItemCount()>0){
                    recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount()-1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Chatting.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTypingIndicator(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("GroupChatList");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    if(snap.child("GroupKey").getValue().toString().equals(getIntent().getStringExtra("key"))){
                        String indicator = snap.child("TypingStatus").getValue().toString();
                        if(indicator.replace(" is typing...","").equals(prefs.getString("userName",null)))
                        {
                            //do nothing
                        }
                        else {
                            onlinestatus.setText(indicator);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void sendNotification(String msg){

        JSONObject json  = new JSONObject();
        try{
            json.put("to","/topics/"+"user");
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title","New Message");
            notificationObj.put("body",msg);

//            JSONObject extraData = new JSONObject();
//            extraData.put("brandId","puma");
//            extraData.put("category","Shoes");



            json.put("notification",notificationObj);
//            json.put("data",extraData);


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, json,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.d("MUR", response.toString());
                            //Toast.makeText(InventoryListActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("MUR", "onError: "+error.networkResponse);
                    Toast.makeText(Chatting.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> header = new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization","key=AAAAq7QL2kw:APA91bH8xNu5Z9B3d_buwolZWIelhlKoQGrUebEmdg1VqcEe8LZ-A2jLqQFTbQgBm-liIMSXcByvareT8G5pom0YPawkBcSs-lWyBR65F5UkQGRiP7VY2afxsr0V8wmixlujEzecU-gr");
                    return header;
                }
            };
            mRequestQue.add(request);


        }
        catch (JSONException ex){
            Toast.makeText(Chatting.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }



    }



    @Override
    protected void onPause() {
        super.onPause();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("GroupChatList").child(getIntent().getStringExtra("key"));
        reference.child("TypingStatus").setValue("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("GroupChatList").child(getIntent().getStringExtra("key"));
        reference.child("TypingStatus").setValue("");
    }

    @Override
    protected void onResume() {
        super.onResume();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("GroupChatList").child(getIntent().getStringExtra("key"));
        reference.child("TypingStatus").setValue("");
    }
}
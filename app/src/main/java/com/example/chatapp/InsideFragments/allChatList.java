package com.example.chatapp.InsideFragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.Adapters.ChatListAdapter;
import com.example.chatapp.R;
import com.example.chatapp.models.ChatListModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class allChatList extends Fragment {


    RecyclerView recyclerView;
    public List<ChatListModel> chatList;
    ChatListAdapter chatListAdapter;
    DatabaseReference databaseReference;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences prefs;
    SearchView searchView;
    Button powerOff;
    private FirebaseAuth mAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_chat_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.chat_list);
        powerOff = (Button)view.findViewById(R.id.powerOff);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        chatList = new ArrayList<>();
        FirebaseMessaging.getInstance().subscribeToTopic("user");
        LoadMyChatList();

        powerOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentUser != null){
                    prefs = getContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.clear();
                    editor.commit();
                    editor.apply();
                    mAuth.signOut();
                    getActivity().finish();
                }
            }
        });

        return view;
    }



    private void LoadMyChatList() {
        databaseReference = FirebaseDatabase.getInstance().getReference("GroupChatList");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    String key = snapshot1.child("GroupKey").getValue().toString();
                    String name = snapshot1.child("GroupName").getValue().toString();
                    String lastMessage = snapshot1.child("LastMessage").getValue().toString();
                    chatList.add(new ChatListModel(key,name,lastMessage));
                }
                chatListAdapter = new ChatListAdapter(getContext(),chatList);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(chatListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Something wrong", Toast.LENGTH_SHORT).show();
            }
        });




    }
}
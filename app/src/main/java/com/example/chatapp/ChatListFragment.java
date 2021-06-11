package com.example.chatapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.example.chatapp.chat.ChatDialog;
import com.example.chatapp.models.Dialog;
import com.example.chatapp.models.Message;
import com.example.chatapp.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.commons.models.IDialog;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatListFragment extends Fragment {

    private static final String TAG = " ";
    //ArrayList<ChatDialog> chats = new ArrayList<>();

    ArrayList<Dialog> chats = new ArrayList<>();

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    DialogsList dialogsList;

    Button startChatButton;

    public ChatListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat_list, container, false);

          dialogsList = (DialogsList) view.findViewById(R.id.chats_list);

         startChatButton = (Button) view.findViewById(R.id.start_chat_button);

        DialogsListAdapter dialogsListAdapter = new DialogsListAdapter<ChatDialog>(new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, @Nullable String url, @Nullable Object payload) {
                if(!url.equals(""))
                    Picasso.get().load(url).into(imageView);
            }
        });
        dialogsList.setAdapter(dialogsListAdapter);
        startChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //startChat("My first chat");
                showChatDialogAlert();
            }
        });

        listenToIncomingChats();
        getChatsList();
        //startChat();
/*
        Map<String, Object> city = new HashMap<>();
        city.put("name", "Los Angeles");
        city.put("state", "CA");
        city.put("country", "USA");

        db.collection("cities").document("LA")
                .set(city)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });

 */
        return view;
    }

    private void getChatsList() {
        firestore.collection("chats")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("Chat List", document.getId() + " => " + document.getData());

                                Dialog dialog = document.toObject(Dialog.class);
                                dialog.setId(document.getId());

                                if (!chats.contains(dialog))
                                {
                                    ChatDialog chatDialog = new ChatDialog(dialog);
                                    DialogsListAdapter adapter = (DialogsListAdapter) dialogsList.getAdapter();
                                    adapter.addItem(chatDialog);
                                    chats.add(dialog);
                                }
                               // ChatDialog chatDialog = document.toObject(ChatDialog.class);
                               // DialogsListAdapter adapter = (DialogsListAdapter)dialogsList.getAdapter();
                               // adapter.addItem(chatDialog);
                            }
                        } else {
                            Log.d("Chat List", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void startChat(String firstMessage) {
        User user = User.userFromFirebaseUser(FirebaseAuth.getInstance().getCurrentUser());
        Message newMessage = new Message(firstMessage, user);
        Dialog newChat = new Dialog(newMessage);

        firestore.collection("chats")
                .add(newChat)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("Chat List", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Chat List", "Error adding document", e);
                    }
                });

        /*
        // Create a new user with a first and last name
        Map<String, Object> chat = new HashMap<>();
        chat.put("id", "123");
        chat.put("dialogPhoto", "");
        chat.put("dialogName", firstMessage);
        chat.put("unreadCount", 0);

        db.collection("chats")
                .add(chat)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("Chat List", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Chat List", "Error adding document", e);
                    }
                });

         */
    }

    private  void listenToIncomingChats()
    {
        final CollectionReference collectionReference = firestore.collection("chats");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error == null)
                {
                    for (QueryDocumentSnapshot document : value)
                    {
                        Dialog dialog = document.toObject(Dialog.class);
                        dialog.setId(document.getId());

                        if (!chats.contains(dialog))
                        {
                            ChatDialog chatDialog = new ChatDialog(dialog);
                            DialogsListAdapter adapter = (DialogsListAdapter) dialogsList.getAdapter();
                            adapter.addItem(chatDialog);

                            chats.add(dialog);
                        }
                    }
                }
                else
                {
                    error.printStackTrace();
                }
            }
        });
    }



    private void  showChatDialogAlert()
    {
     final EditText messageEditText = new EditText(getContext());
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("Start a chat")
                .setMessage("Enter your first message")
                .setView(messageEditText)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog,int which){
            String message = String.valueOf(messageEditText.getText());
            startChat(message);
        }
    })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();

        }
    }
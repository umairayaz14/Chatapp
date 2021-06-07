package com.example.chatapp.chat;

import com.example.chatapp.models.User;
import com.google.firebase.auth.FirebaseUser;
import com.stfalcon.chatkit.commons.models.IUser;

public class ChatUser implements IUser {
    String id;
    String name;
    String avatar;

    public ChatUser(User user)
    {
        this.id = user.getId();
        this.name = user.getName();
        this.avatar = user.getAvatar();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAvatar() {
        return avatar;
    }


   /*
    public static User userFromFirebaseUser(FirebaseUser firebaseUser)
    {
        User user = new User();
        user.id = firebaseUser.getUid();
        user.name = firebaseUser.getDisplayName();

        if (firebaseUser.getPhotoUrl() != null)
        {
            user.avatar = firebaseUser.getPhotoUrl().getPath();
        }
        else
        {
            user.avatar = "";
        }

        return user;
    }
    */
}


package com.example.chatapp.models;

import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;

public class User implements Serializable {
    String id;
    String name;
    String avatar;

    public User()
    {
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }



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

}

package com.codehunters.usher;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
//CUSTOM ADAPTER FOR CHAT MESSAGES

public class CustomAdapterChat extends ArrayAdapter<ChatMessage> {
    Context mContext;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String uid;

    public CustomAdapterChat(Context context, ArrayList<ChatMessage> users) {

        super(context, 0, users);
        mContext = context;
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        uid = firebaseUser.getUid();

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View result;
        ChatMessage chatMessage = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.chatmessages, parent, false);

            if (chatMessage.uid.equals(uid))
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.chatmessagesmine, parent, false);


        }

        if (chatMessage.uid.equals(uid)) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.chatmessagesmine, parent, false);
            TextView tvmessage = (TextView) convertView.findViewById(R.id.messagetext);
            TextView tvtime = (TextView) convertView.findViewById(R.id.time);
            tvtime.setText(chatMessage.Time);
            tvmessage.setText(chatMessage.messageText);



        } else {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.chatmessages, parent, false);
            TextView tvmessage = (TextView) convertView.findViewById(R.id.messagetext);
            TextView tvtime = (TextView) convertView.findViewById(R.id.time);
            tvtime.setText(chatMessage.Time);
            tvmessage.setText(chatMessage.messageText);

        }

        return convertView;

    }
}

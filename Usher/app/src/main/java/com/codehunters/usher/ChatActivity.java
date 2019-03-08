package com.codehunters.usher;

import android.app.Activity;
import android.app.ActivityManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import android.widget.RelativeLayout;
import android.widget.TextView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import java.util.Timer;
import java.util.TimerTask;




//CLASS FOR CHATTING ACTIVITY

public class ChatActivity extends AppCompatActivity {
    ListView listView;
    RelativeLayout activity_chat;
    EditText message;
    private FirebaseAuth firebaseAuth;

    private DatabaseReference databaseReference;
    String otherUserId;
    String chatid;
    ImageView send, hidekeyboard, reload;
    ArrayList<ChatMessage> chatlist = new ArrayList<>();
    CustomAdapterChat adapter;
    private String userid;
    String ImageUrl;
    TextView username;
    ImageView profilePic;
    String name;
    ImageView back;
    String key = "123";
    String salt = "123";
    byte[] iv = new byte[16];

    RelativeLayout loading;
    long messageCount;
    ArrayList<String> chatKey=new ArrayList<String>();
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_chat);
        final Bundle bundle = getIntent().getExtras();
        listView =  findViewById(R.id.listview);
        adapter = new CustomAdapterChat(this, chatlist);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user!=null) {
            userid = user.getUid();
        }


        if (bundle != null) {
            chatid = bundle.getString("chatid");

        }


        send =  findViewById(R.id.send);
        reload =  findViewById(R.id.reload);
        hidekeyboard =  findViewById(R.id.hidekeyboard);
      //  hidekeyboard.setVisibility(View.GONE);
        message =  findViewById(R.id.message);

        hidekeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidekeyboard.setVisibility(View.GONE);
                message.onEditorAction(EditorInfo.IME_ACTION_DONE);

            }
        });


        //DATABASEREFERENCE FOR IMAGE URL

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Chats").child(chatid);
        chatMessages();
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidekeyboard.setVisibility(View.VISIBLE);

            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!message.getText().toString().isEmpty()) {
                   // send.setVisibility(View.GONE);
                    //loading.setVisibility(View.VISIBLE);
                }

                try {
                    chat();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                message.setText("");


            }
        });

        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 chatMessages();

            }
        });
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {

                databaseReference = FirebaseDatabase.getInstance().getReference().child("Chats").child(chatid).child("Messages");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getChildrenCount()!=messageCount){

                            chatMessages();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        }, 0, 3000);
        registerForContextMenu(listView);



    }
    //FUNCTION TO UPLOAD MESSAGES
    private void chat() throws Exception {
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        final String Time = df.format(currentTime);
        if (!message.getText().toString().isEmpty()) {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Chats").child(chatid).child("Messages").push();
            databaseReference.child("message").setValue(message.getText().toString());
            databaseReference.child("Userid").setValue(userid);
            databaseReference.child("Time").setValue(Time);
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Chats").child(chatid);


        }


    }
    //FUNCTION TO LOAD CHAT MESSAGES
    private void chatMessages() {

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Chats").child(chatid).child("Messages");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatlist.clear();
                chatKey.clear();
                messageCount=dataSnapshot.getChildrenCount();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    chatlist.add(new ChatMessage((String)ds.child("message").getValue(), (String)ds.child("Userid").getValue(), (String)ds.child("Time").getValue()));
                    chatKey.add((String)ds.getKey());





                }
                listView.setAdapter(adapter);
                //send.setVisibility(View.VISIBLE);
               // loading.setVisibility(View.GONE);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onCreateContextMenu(final ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        final AdapterView.AdapterContextMenuInfo adapterMenuInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
        position = adapterMenuInfo.position;
        if(chatlist.get(position).uid.equals(firebaseAuth.getCurrentUser().getUid())) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.chatoptions, menu);
        }



    }
    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if(item.getItemId()==R.id.unsend) {
            String messageKey = chatKey.get(position);
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Chats").child(chatid).child("Messages");

            databaseReference = FirebaseDatabase.getInstance().getReference().child("Chats").child(chatid).child("Messages");
            databaseReference.child(messageKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        chatMessages();
                    }
                }
            });
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Chats").child(chatid).child("Messages");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    messageCount=dataSnapshot.getChildrenCount();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

        }
        else{
            return false;
        }
        return true;
    }


}

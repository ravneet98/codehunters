package com.codehunters.usher;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;



import java.util.ArrayList;

//CUSTOM ADAPTER TO LOAD FOLLOWER AND FOLLOWING LIST
public class CustomAdapter extends ArrayAdapter<user> {
    Context mContext;
    private int lastPosition = -1;

    public CustomAdapter(Context context, ArrayList<user> users) {

        super(context, 0, users);
        mContext = context;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View result;
        View v=convertView;
        if (v == null) {

           // convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_info, parent, false);
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.user_info, null);

        }
        user p = getItem(position);
        TextView tvName = (TextView) v.findViewById(R.id.name);
        TextView tvEmail = (TextView) v.findViewById(R.id.email);

            tvName.setText(p.name);
            tvEmail.setText(p.email);



            return v;

    }

}

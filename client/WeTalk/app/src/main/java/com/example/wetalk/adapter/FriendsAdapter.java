package com.example.wetalk.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wetalk.Friends;
import com.example.wetalk.R;

import java.util.List;

public class FriendsAdapter extends ArrayAdapter<Friends> {
    private int resourceId;
    public FriendsAdapter(Context context, int textViewResourceId, List<Friends> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Friends friends=getItem(position);
        //View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        View view;
        if(convertView==null){
            view=LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        }else{
            view=convertView;
        }
        ImageView friendsImage=(ImageView)view.findViewById(R.id.friends_image);
        TextView friendsName=(TextView)view.findViewById(R.id.friends_name);
        friendsImage.setImageResource(friends.getImagNumber());
        friendsName.setText(friends.getName());
        return view;
    }
}

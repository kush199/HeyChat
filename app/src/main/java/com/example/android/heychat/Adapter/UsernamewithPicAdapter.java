package com.example.android.heychat.Adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.heychat.Model.UserNameWithPic;
import com.example.android.heychat.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import static java.security.AccessController.getContext;

/**
 * Created by CHALLIDO on 7/20/2017.
 */

public class UsernamewithPicAdapter extends BaseAdapter {

    private TextView  mUsernameTextView;
    private ImageView mUserPic;
    Context context;
    List<UserNameWithPic> rowItems;

    public UsernamewithPicAdapter(Context context, List<UserNameWithPic> rowItems) {
        this.context = context;
        this.rowItems = rowItems;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater mInflater =(LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if(convertView == null)
        {
            convertView = mInflater.inflate(R.layout.llistwithimage,null);
        }
         mUserPic =(ImageView)convertView.findViewById(R.id.ImagePic);
        mUsernameTextView =(TextView)convertView.findViewById(R.id.usernameForChat);
        UserNameWithPic usernamepic= rowItems.get(position);
        mUsernameTextView.setVisibility(View.VISIBLE);
        mUserPic.setVisibility(View.VISIBLE);
        mUsernameTextView.setText(usernamepic.getUserName());
        Picasso.with(context).load(usernamepic.getImage()).fit().centerCrop().into(mUserPic);
        return convertView;
    }





}

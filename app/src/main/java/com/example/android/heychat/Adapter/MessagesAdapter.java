package com.example.android.heychat.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.heychat.Model.Messages;
import com.example.android.heychat.R;

import java.util.List;

/**
 * Created by kush_1 on 6/23/2017.
 */

public class MessagesAdapter  extends ArrayAdapter<Messages>{

    private TextView messageTextView;
    private TextView autherTextView;
    public MessagesAdapter(Context context, int resource, List<Messages> objects) {
        super(context, resource, objects);


    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
        {
            convertView =((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_message,parent, false);
        }

        messageTextView =(TextView)convertView.findViewById(R.id.messageTextView);
        autherTextView = (TextView)convertView.findViewById(R.id.nameTextView);
        Messages message = getItem(position);
        messageTextView.setVisibility(View.VISIBLE);
        autherTextView.setVisibility(View.VISIBLE);
        messageTextView.setText(message.getText());
        autherTextView.setText(message.getName());

        return convertView;
    }
}

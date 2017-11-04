package com.example.android.heychat.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import android.widget.TextView;

import com.example.android.heychat.R;

/**
 * Created by kush_1 on 6/24/2017.
 */

public class ChatMessageViewHolder extends RecyclerView.ViewHolder {
    public TextView nameTextView,messageTextView;
    public ImageView mImageView;

    public ChatMessageViewHolder(View itemView) {
        super(itemView);
        mImageView = (ImageView) itemView.findViewById(R.id.Imagesend);

        nameTextView = (TextView) itemView.findViewById(R.id.nameTextView);
        messageTextView = (TextView) itemView.findViewById(R.id.messageTextView);
    }
}
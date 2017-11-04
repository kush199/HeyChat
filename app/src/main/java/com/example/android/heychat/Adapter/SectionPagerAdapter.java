package com.example.android.heychat.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.heychat.Fragments.ChatFragment;
import com.example.android.heychat.Fragments.FriendsFragment;
import com.example.android.heychat.Fragments.RequestFragment;

/**
 * Created by kush_1 on 6/28/2017.
 */

public class SectionPagerAdapter extends FragmentPagerAdapter {
    public SectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position)
        {
            case 0:
                ChatFragment chatFragment = new ChatFragment();
                return  chatFragment;
            case 1:
                FriendsFragment friendsFragment = new FriendsFragment();
                return friendsFragment;
            case 2:
                RequestFragment requestFragment = new RequestFragment();
                return requestFragment;
            default:
                return null;

        }

    }

    @Override
    public int getCount() {
        return 3;
    }

    public CharSequence getPageTitle(int position)
    {
        switch (position)
        {
            case 0:
                return "CHATS";
            case 1:
                return "GROUPS";
            case 2:
                return "NEWS FEED";
            default:
                return null;
        }
    }
}

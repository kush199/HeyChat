package com.example.android.heychat.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.heychat.Fragments.GroupChatScreenFragment;
import com.example.android.heychat.Fragments.GroupFriendListFragment;

/**
 * Created by CHALLIDO on 7/6/2017.
 */

public class SectionGrooupPagerAdapter extends FragmentPagerAdapter {
    public SectionGrooupPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch(position)
        {
            case 0:
                GroupChatScreenFragment groupChatScreen = new GroupChatScreenFragment();
                return groupChatScreen;
            case 1:
                GroupFriendListFragment groupFriendListFragment = new GroupFriendListFragment();
                return  groupFriendListFragment;
            default:
                return  null;
        }

    }

    @Override
    public int getCount() {
        return 2;
    }

    public  CharSequence getPageTitle(int position)
    {
        switch(position)
        {
            case 0:
                return "Group Chat";
            case 1:
                return "Friends List";
            default:
                return null;
        }
    }
}

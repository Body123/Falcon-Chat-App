package com.example.falcon;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

class SectionsPageradapter extends FragmentPagerAdapter {


    public SectionsPageradapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                RequestsFragment requestsFragment=new RequestsFragment();
                return requestsFragment;
            case 1:
                ChatsFragment chatsFragment=new ChatsFragment();
                return chatsFragment;
            case 2:
                FriendFragment friendFragment=new FriendFragment();
                return friendFragment;
             default:
                 return null;


        }
    }


    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public  CharSequence getPageTitle(int position){
        switch (position){
            case 0:
                return  "REQUESTS";
            case 1:
                return  "CHATS";
            case 2:
                return  "FRIENDS";
            default:
                return null;
        }
    }
}

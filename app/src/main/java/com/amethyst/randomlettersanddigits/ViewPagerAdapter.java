package com.amethyst.randomlettersanddigits;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private Fragment[] fragments = new Fragment[2];

    public ViewPagerAdapter(FragmentActivity activity) {
        super(activity);
        Log.d("ViewPagerAdapter","constructor");

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Log.d("createFragment","");
        switch (position) {
            case 0: {
                AutoModeFragment autoModeFragment = new AutoModeFragment();
                fragments[0] = autoModeFragment;
                return autoModeFragment;   // First tab
            }
            case 1:
                ManualModeFragment manualModeFragment = new ManualModeFragment();
                fragments[1] = manualModeFragment;
                return manualModeFragment; // Second tab

        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 2; // You have two tabs
    }

    public Fragment GetFragmentByPosition(int position) {

        return fragments[position];
    }

}
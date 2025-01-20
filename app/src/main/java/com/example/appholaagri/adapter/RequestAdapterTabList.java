package com.example.appholaagri.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.appholaagri.model.RequestTabListData.RequestTabListData;
import com.example.appholaagri.view.FollowingRequestFragment;
import com.example.appholaagri.view.IsendRequestFragment;
import com.example.appholaagri.view.SendToMeRequestFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestAdapterTabList extends FragmentStateAdapter {
    private List<RequestTabListData> requestTabListData;
    private Map<Integer, Fragment> fragmentMap = new HashMap<>(); // Lưu trữ các fragment

    public RequestAdapterTabList(@NonNull FragmentActivity fragmentActivity, List<RequestTabListData> requestTabListData) {
        super(fragmentActivity);
        this.requestTabListData = requestTabListData;
    }

    public void setTabList(List<RequestTabListData> tabList) {
        this.requestTabListData = tabList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        RequestTabListData tabData = requestTabListData.get(position);
        Fragment fragment;

        switch (position) {
            case 0:
                fragment = new IsendRequestFragment();
                break;
            case 1:
                fragment = new SendToMeRequestFragment();
                break;
            case 2:
                fragment = new FollowingRequestFragment();
                break;
            default:
                throw new IllegalStateException("Invalid position");
        }

        Bundle bundle = new Bundle();
        bundle.putInt("tab_id", tabData.getId());
        fragment.setArguments(bundle);

        // Lưu fragment vào map
        fragmentMap.put(position, fragment);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return requestTabListData != null ? requestTabListData.size() : 0;
    }

    public Fragment getFragmentAtPosition(int position) {
        return fragmentMap.get(position);
    }
}
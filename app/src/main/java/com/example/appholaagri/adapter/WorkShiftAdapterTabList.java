package com.example.appholaagri.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.appholaagri.model.RecordConditionModel.RecordConditionTabList;
import com.example.appholaagri.view.ConfirmRecordConditionFragment;
import com.example.appholaagri.view.InitRecordConditionFragment;
import com.example.appholaagri.view.MyShiftFragment;
import com.example.appholaagri.view.RefusedRecordConditionFragment;
import com.example.appholaagri.view.TeamShiftFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkShiftAdapterTabList extends FragmentStateAdapter {
    private static List<RecordConditionTabList> recordConditionTabLists;
    private Map<Integer, Fragment> fragmentMap = new HashMap<>(); // Lưu trữ các fragment

    public WorkShiftAdapterTabList(@NonNull FragmentActivity fragmentActivity, List<RecordConditionTabList> requestTabListData) {
        super(fragmentActivity);
        this.recordConditionTabLists = requestTabListData;
    }
    public static int getTabIdAtPosition(int position) {
        return recordConditionTabLists.get(position).getId();
    }
    public List<RecordConditionTabList> getTabList() {
        return recordConditionTabLists;
    }

    public void setTabList(List<RecordConditionTabList> tabList) {
        this.recordConditionTabLists = tabList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        RecordConditionTabList tabData = recordConditionTabLists.get(position);
        Fragment fragment;

        switch (position) {
            case 0:
                fragment = new MyShiftFragment();
                break;
            case 1:
                fragment = new TeamShiftFragment();
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
        return recordConditionTabLists != null ? recordConditionTabLists.size() : 0;
    }

    public Fragment getFragmentAtPosition(int position) {
        return fragmentMap.get(position);
    }
}
package com.example.appholaagri.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.appholaagri.view.MonitoringClosestValueFragment;
import com.example.appholaagri.view.MonitoringFluctuatingValueFragment;
import com.example.appholaagri.view.PlantCareHistoryFragment;
import com.example.appholaagri.view.PlantConditionFragment;
import com.example.appholaagri.view.PlantInfoFragment;
import com.example.appholaagri.view.PlantNutritionFragment;
import com.example.appholaagri.view.PlantNutritionValueFragment;

public class MonitoringMeasurementPointAdapterTabList extends FragmentStateAdapter {
    private final int monitoringId;

    public MonitoringMeasurementPointAdapterTabList(@NonNull FragmentActivity fragmentActivity, int monitoringId) {
        super(fragmentActivity);
        this.monitoringId = monitoringId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new MonitoringClosestValueFragment();
                break;
            case 1:
                fragment = new MonitoringFluctuatingValueFragment();
                break;
            default:
                fragment = new MonitoringClosestValueFragment();
        }

        // Truyền planId vào Fragment bằng Bundle
        Bundle args = new Bundle();
        args.putInt("monitoringId", monitoringId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

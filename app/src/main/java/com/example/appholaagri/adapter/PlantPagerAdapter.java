package com.example.appholaagri.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.appholaagri.view.PlantCareHistoryFragment;
import com.example.appholaagri.view.PlantConditionFragment;
import com.example.appholaagri.view.PlantInfoFragment;
import com.example.appholaagri.view.PlantNutritionFragment;
import com.example.appholaagri.view.PlantNutritionValueFragment;


public class PlantPagerAdapter extends FragmentStateAdapter {
    private final int plantId;

    public PlantPagerAdapter(@NonNull FragmentActivity fragmentActivity, int plantId) {
        super(fragmentActivity);
        this.plantId = plantId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new PlantInfoFragment();
                break;
            case 1:
                fragment = new PlantCareHistoryFragment();
                break;
            case 2:
                fragment = new PlantConditionFragment();
                break;
            case 3:
                fragment = new PlantNutritionFragment();
                break;
            case 4:
                fragment = new PlantNutritionValueFragment();
                break;
            default:
                fragment = new PlantInfoFragment();
        }

        // Truyền planId vào Fragment bằng Bundle
        Bundle args = new Bundle();
        args.putInt("plantId", plantId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}


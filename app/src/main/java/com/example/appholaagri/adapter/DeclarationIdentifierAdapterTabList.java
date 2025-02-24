package com.example.appholaagri.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.appholaagri.view.BangCongFragment;
import com.example.appholaagri.view.BangLuongFragment;
import com.example.appholaagri.view.DeclarationIdentifierPlantFragment;
import com.example.appholaagri.view.DeclarationIdentifierSensorFragment;

public class DeclarationIdentifierAdapterTabList extends FragmentStateAdapter {
    public DeclarationIdentifierAdapterTabList(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public DeclarationIdentifierAdapterTabList(@NonNull Fragment fragment) {
        super(fragment);
    }

    public DeclarationIdentifierAdapterTabList(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new DeclarationIdentifierPlantFragment();
            case 1:
                return new DeclarationIdentifierSensorFragment();
            default:
                throw new IllegalStateException("Invalid position");
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

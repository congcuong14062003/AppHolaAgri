package com.example.appholaagri.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.appholaagri.view.BangCongFragment;
import com.example.appholaagri.view.BangLuongFragment;
import com.example.appholaagri.view.ConfirmTimekeepingFragment;
import com.example.appholaagri.view.InitTimekeeipingFragment;
import com.example.appholaagri.view.RefuseTimeKeepingFragment;

public class SalaryTableAdapterTabList extends FragmentStateAdapter {
    public SalaryTableAdapterTabList(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public SalaryTableAdapterTabList(@NonNull Fragment fragment) {
        super(fragment);
    }

    public SalaryTableAdapterTabList(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new BangCongFragment();
            case 1:
                return new BangLuongFragment();
            default:
                throw new IllegalStateException("Invalid position");
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

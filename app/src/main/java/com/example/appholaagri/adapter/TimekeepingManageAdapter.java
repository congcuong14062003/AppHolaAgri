package com.example.appholaagri.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.appholaagri.view.ConfirmTimekeepingFragment;
import com.example.appholaagri.view.DatePickerFragment;
import com.example.appholaagri.view.RefuseTimeKeepingFragment;
import com.example.appholaagri.view.TodayFragment;
import com.example.appholaagri.view.WaitTimekeeipingFragment;

public class TimekeepingManageAdapter extends FragmentStateAdapter {
    public TimekeepingManageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public TimekeepingManageAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public TimekeepingManageAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new WaitTimekeeipingFragment();
            case 1:
                return new ConfirmTimekeepingFragment();
            case 2:
                return new RefuseTimeKeepingFragment();
            default:
                throw new IllegalStateException("Invalid position");
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}

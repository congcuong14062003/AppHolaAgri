package com.example.appholaagri.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.appholaagri.view.DatePickerFragment;
import com.example.appholaagri.view.TodayFragment;

public class TimekeepingStatisticsAdapterTabList extends FragmentStateAdapter {
    public TimekeepingStatisticsAdapterTabList(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public TimekeepingStatisticsAdapterTabList(@NonNull Fragment fragment) {
        super(fragment);
    }

    public TimekeepingStatisticsAdapterTabList(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new TodayFragment(); // Tab "Hôm nay"
            case 1:
                return new DatePickerFragment(); // Tab "Ngày tháng năm chọn"
            default:
                throw new IllegalStateException("Invalid position");
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

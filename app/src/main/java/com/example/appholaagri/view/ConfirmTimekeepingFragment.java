package com.example.appholaagri.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.appholaagri.R;
import com.example.appholaagri.adapter.TimeKeepingManageConfirmAdapter;
import com.example.appholaagri.adapter.TimeKeepingManageInitAdapter;
import com.example.appholaagri.model.TimeKeepingManageModel.TimeKeepingManageData;
import com.example.appholaagri.utils.TimekeepingManageApiHelper;

import java.util.ArrayList;
import java.util.List;

public class ConfirmTimekeepingFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private TimeKeepingManageConfirmAdapter adapter;
    private LinearLayout emptyStateLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_confirm_timekeeping, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewConfirmTimekeeing);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        emptyStateLayout = view.findViewById(R.id.emptyStateLayout);
        // Call API
        fetchTimekeepingList();
        return view;
    }

    private void fetchTimekeepingList() {
        List<Integer> departmentIds = new ArrayList<>();
        departmentIds.add(-1);

        List<Integer> teamIds = new ArrayList<>();
        teamIds.add(-1);
        TimekeepingManageApiHelper.fetchTimekeepingList(
                getContext(),
                2, // Status for "Confirmed"
                "01/01/2024", // Start date
                "13/01/2025", // End date
                "", // Key search
                departmentIds,
                teamIds,
                new TimekeepingManageApiHelper.TimekeepingApiCallback() {
                    @Override
                    public void onSuccess(List<TimeKeepingManageData> data) {
                        if (data == null || data.isEmpty()) {
                            // Hiển thị empty state
                            recyclerView.setVisibility(View.GONE);
                            emptyStateLayout.setVisibility(View.VISIBLE);
                        } else {
                            // Hiển thị RecyclerView
                            recyclerView.setVisibility(View.VISIBLE);
                            emptyStateLayout.setVisibility(View.GONE);

                            adapter = new TimeKeepingManageConfirmAdapter(data);
                            recyclerView.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        // Xử lý khi API thất bại
                        recyclerView.setVisibility(View.GONE);
                        emptyStateLayout.setVisibility(View.VISIBLE);
                    }
                }
        );
    }
}

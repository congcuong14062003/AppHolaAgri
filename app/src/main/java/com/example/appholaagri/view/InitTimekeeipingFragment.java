package com.example.appholaagri.view;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.appholaagri.R;
import com.example.appholaagri.adapter.TimeKeepingManageInitAdapter;
import com.example.appholaagri.model.TimeKeepingManageModel.TimeKeepingManageData;
import com.example.appholaagri.utils.TimekeepingManageApiHelper;

import java.util.ArrayList;
import java.util.List;

public class InitTimekeeipingFragment extends Fragment {
    private RecyclerView recyclerView;
    private TimeKeepingManageInitAdapter adapter;
    private LinearLayout emptyStateLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_init_timekeeiping, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewInitTimekeeing);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        emptyStateLayout = view.findViewById(R.id.emptyStateLayout);

        AppCompatButton confirmButton = view.findViewById(R.id.confirm_btn);
        AppCompatButton refusedButton = view.findViewById(R.id.refused_btn);
        ImageView checkAllIcon = view.findViewById(R.id.checkAllIcon);

        final boolean[] isAllChecked = {false}; // Theo dõi trạng thái "check all"
        confirmButton.setEnabled(false);
        refusedButton.setEnabled(false);

        // Gọi API để lấy danh sách
        fetchTimekeepingList(confirmButton, refusedButton, checkAllIcon, isAllChecked);

        return view;
    }

    private void fetchTimekeepingList(AppCompatButton confirmButton, AppCompatButton refusedButton,
                                      ImageView checkAllIcon, boolean[] isAllChecked) {
        List<Integer> departmentIds = new ArrayList<>();
        departmentIds.add(-1);

        List<Integer> teamIds = new ArrayList<>();
        teamIds.add(-1);

        TimekeepingManageApiHelper.fetchTimekeepingList(
                getContext(),
                1, // Status for "Waiting for confirmation"
                "01/01/2024", // Start date
                "13/01/2025", // End date
                "", // Key search
                departmentIds,
                teamIds,
                new TimekeepingManageApiHelper.TimekeepingApiCallback() {
                    @Override
                    public void onSuccess(List<TimeKeepingManageData> data) {
                        if (data == null || data.isEmpty()) {
                            recyclerView.setVisibility(View.GONE);
                            emptyStateLayout.setVisibility(View.VISIBLE);
                        } else {
                            recyclerView.setVisibility(View.VISIBLE);
                            emptyStateLayout.setVisibility(View.GONE);

                            adapter = new TimeKeepingManageInitAdapter(data);
                            recyclerView.setAdapter(adapter);

                            // Lắng nghe trạng thái chọn
                            adapter.setOnSelectionChangedListener(hasSelection -> {
                                updateButtonState(confirmButton, refusedButton, hasSelection);
                            });

                            // Sự kiện click nút "checkAllIcon"
                            checkAllIcon.setOnClickListener(v -> {
                                if (isAllChecked[0]) {
                                    adapter.clearSelection(); // Bỏ chọn tất cả
                                    checkAllIcon.setImageResource(R.drawable.no_check); // Đổi icon về "no_check"
                                } else {
                                    adapter.selectAll(); // Chọn tất cả
                                    checkAllIcon.setImageResource(R.drawable.duyet); // Đổi icon về "checked"
                                }
                                isAllChecked[0] = !isAllChecked[0]; // Đảo trạng thái
                            });
                        }
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        recyclerView.setVisibility(View.GONE);
                        emptyStateLayout.setVisibility(View.VISIBLE);
                    }
                }
        );
    }

    private void updateButtonState(AppCompatButton confirmButton, AppCompatButton refusedButton, boolean hasSelection) {
        if (hasSelection) {
            confirmButton.setEnabled(true);
            refusedButton.setEnabled(true);

            confirmButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1CAE80")));
            confirmButton.setTextColor(Color.parseColor("#FFFFFF"));

            refusedButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF5C39")));
            refusedButton.setTextColor(Color.parseColor("#FFFFFF"));
        } else {
            confirmButton.setEnabled(false);
            refusedButton.setEnabled(false);

            confirmButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e8f7f3")));
            confirmButton.setTextColor(Color.parseColor("#1CAE80"));

            refusedButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#feefea")));
            refusedButton.setTextColor(Color.parseColor("#FF5C39"));
        }
    }
}

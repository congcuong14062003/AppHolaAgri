package com.example.appholaagri.adapter;

import android.view.ViewGroup;

import com.example.appholaagri.model.WorkSummaryDetailModel.AttendanceSummaryRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.appholaagri.R;

import java.util.List;

public class DayAdapterBangCongDetail extends RecyclerView.Adapter<DayAdapterBangCongDetail.DayViewHolder> {
    private final List<AttendanceSummaryRes> dayList;

    public DayAdapterBangCongDetail(List<AttendanceSummaryRes> dayList) {
        this.dayList = dayList;
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chi_tiet_cong_day_detail, parent, false);
        return new DayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        AttendanceSummaryRes day = dayList.get(position);
        holder.txtDay.setText(day.getDisplayDate());
        holder.txt_so_cong.setText(day.getTotalCoEfficient().toString());

        // Bắt sự kiện click vào cong_layout để mở/đóng details_layout
        holder.cong_layout.setOnClickListener(view -> {
            if (holder.details_layout.getVisibility() == View.VISIBLE) {
                // Nếu detailsLayout đang hiển thị, ẩn đi
                holder.details_layout.setVisibility(View.GONE);
            } else {
                // Nếu detailsLayout đang ẩn, hiển thị ra
                holder.details_layout.setVisibility(View.VISIBLE);
            }
        });

        // Bắt sự kiện chạm ngoài để đóng details_layout
        holder.itemView.setOnTouchListener((view, event) -> {
            if (holder.details_layout.getVisibility() == View.VISIBLE) {
                holder.details_layout.setVisibility(View.GONE);
            }
            return false; // Trả về false để sự kiện có thể tiếp tục
        });

        // Set adapter cho RecyclerView
        ShiftAdapterBangCongDetail shiftAdapter = new ShiftAdapterBangCongDetail(day.getShiftSummaryRes());
        holder.recyclerViewShifts.setAdapter(shiftAdapter);
    }


    @Override
    public int getItemCount() {
        return dayList.size();
    }

    public static class DayViewHolder extends RecyclerView.ViewHolder {
        TextView txtDay, txt_so_cong;
        RecyclerView recyclerViewShifts;
        LinearLayout cong_layout, details_layout;  // Thêm các biến này

        public DayViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDay = itemView.findViewById(R.id.txt_day);
            txt_so_cong = itemView.findViewById(R.id.txt_so_cong);
            recyclerViewShifts = itemView.findViewById(R.id.recycler_view_shifts);
            cong_layout = itemView.findViewById(R.id.cong_layout);  // Lấy đối tượng cong_layout
            details_layout = itemView.findViewById(R.id.details_layout);  // Lấy đối tượng details_layout


            recyclerViewShifts.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        }
    }
}


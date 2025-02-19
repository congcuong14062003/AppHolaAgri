package com.example.appholaagri.adapter;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appholaagri.R;
import com.example.appholaagri.model.RequestDetailModel.BreakTime;
import com.example.appholaagri.model.RequestDetailModel.ListDayReq;
import com.example.appholaagri.utils.CustomToast;
import com.example.appholaagri.view.CreateRequestOvertTimeActivity;

import java.util.List;

public class BreakTimeOverTimeAdapter extends RecyclerView.Adapter<BreakTimeOverTimeAdapter.BreakTimeViewHolder> {
    private List<BreakTime> breakTimes;
    private List<ListDayReq> listDayReqs;
    private CreateRequestOvertTimeActivity createRequestOvertTimeActivity;
    private int statusRequest;
    Context context;

    public BreakTimeOverTimeAdapter(List<BreakTime> breakTimes, Context context, List<ListDayReq> listDayReqs, CreateRequestOvertTimeActivity activity, int statusRequest) {
        this.breakTimes = breakTimes;
        this.listDayReqs = listDayReqs;
        this.createRequestOvertTimeActivity = activity;
        this.statusRequest = statusRequest;
        this.context = context;

    }


    @NonNull
    @Override
    public BreakTimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_break_time, parent, false);
        return new BreakTimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BreakTimeViewHolder holder, int position) {
        BreakTime breakTime = breakTimes.get(position);
        if (breakTime.getStartTime().isEmpty() || breakTime.getEndTime().isEmpty()) {
            holder.etBreakTimeTimeOvertime.setText("");  // Giữ nguyên hint
        } else {
            holder.etBreakTimeTimeOvertime.setText(breakTime.getStartTime() + " - " + breakTime.getEndTime());
        }
        // Kiểm tra statusRequest để bật/tắt các trường nhập
        if(statusRequest > 2) {
            holder.etBreakTimeTimeOvertime.setEnabled(false);
            holder.etBreakTimeTimeOvertime.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#dee0df")));
            holder.deleteBreakBtn.setVisibility(View.GONE);
            holder.txt_time_overtime.setVisibility(View.VISIBLE);
        }


        // Xử lý khi chọn thời gian nghỉ giữa giờ
        holder.etBreakTimeTimeOvertime.setOnClickListener(v -> {
            TimePickerDialog startTimePicker = new TimePickerDialog(holder.itemView.getContext(), (view, startHour, startMinute) -> {
                String startTime = String.format("%02d:%02d", startHour, startMinute);
                breakTime.setStartTime(startTime);

                TimePickerDialog endTimePicker = new TimePickerDialog(holder.itemView.getContext(), (view2, endHour, endMinute) -> {
                    String endTime = String.format("%02d:%02d", endHour, endMinute);

                    // Chuyển đổi giờ & phút thành số phút từ đầu ngày
                    int startTotalMinutes = startHour * 60 + startMinute;
                    int endTotalMinutes = endHour * 60 + endMinute;

                    if (endTotalMinutes <= startTotalMinutes) {
                        // Giờ kết thúc không hợp lệ, hiển thị lỗi
                        CustomToast.showCustomToast(context, "Giờ kết thúc phải lớn hơn giờ bắt đầu!");

                    } else {
                        // Cập nhật giờ nếu hợp lệ
                        breakTime.setEndTime(endTime);
                        holder.etBreakTimeTimeOvertime.setText(startTime + " - " + endTime);
                        createRequestOvertTimeActivity.updateRequestDetailData();
                    }
                }, startHour + 1, startMinute, true); // Gợi ý giờ kết thúc lớn hơn giờ bắt đầu
                endTimePicker.show();
            }, 8, 0, true);
            startTimePicker.show();
        });


        // Xử lý nút xóa nghỉ giữa giờ
        holder.deleteBreakBtn.setOnClickListener(v -> {
            breakTimes.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, breakTimes.size());
            createRequestOvertTimeActivity.updateRequestDetailData();
        });
    }

    public void updateBreakTimes(List<BreakTime> newBreakTimes) {
        this.breakTimes = newBreakTimes;
        notifyDataSetChanged();
    }



    @Override
    public int getItemCount() {
        return breakTimes.size();
    }

    public static class BreakTimeViewHolder extends RecyclerView.ViewHolder {
        EditText etBreakTimeTimeOvertime;
        ImageView deleteBreakBtn;
        TextView txt_time_overtime;
        public BreakTimeViewHolder(@NonNull View itemView) {
            super(itemView);
            etBreakTimeTimeOvertime = itemView.findViewById(R.id.etBreakTimeTimeOvertime);
            deleteBreakBtn = itemView.findViewById(R.id.delete_break_time);
            txt_time_overtime = itemView.findViewById(R.id.txt_time_overtime);
        }
    }
}

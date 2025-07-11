package com.example.appholaagri.adapter;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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
    private boolean isEdit; // Thay statusRequest bằng isEdit
    private Context context;

    // Cập nhật constructor để nhận isEdit
    public BreakTimeOverTimeAdapter(List<BreakTime> breakTimes, Context context, List<ListDayReq> listDayReqs, CreateRequestOvertTimeActivity activity, boolean isEdit) {
        this.breakTimes = breakTimes;
        this.listDayReqs = listDayReqs;
        this.createRequestOvertTimeActivity = activity;
        this.isEdit = isEdit;
        this.context = context;
    }

    // Phương thức để cập nhật isEdit và làm mới giao diện
    public void setIsEdit(boolean isEdit) {
        this.isEdit = isEdit;
        notifyDataSetChanged();
        Log.d("BreakTimeOverTimeAdapter", "isEdit: " + isEdit);
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
            holder.etBreakTimeTimeOvertime.setText(""); // Giữ nguyên hint
        } else {
            holder.etBreakTimeTimeOvertime.setText(breakTime.getStartTime() + " - " + breakTime.getEndTime());
        }

        // Kiểm tra isEdit để bật/tắt các trường nhập và nút
        if (!isEdit) {
            holder.etBreakTimeTimeOvertime.setEnabled(false);
            holder.etBreakTimeTimeOvertime.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#dee0df")));
            holder.deleteBreakBtn.setVisibility(View.GONE);
            holder.txt_time_overtime.setVisibility(View.VISIBLE);
        } else {
            holder.etBreakTimeTimeOvertime.setEnabled(true);
            holder.etBreakTimeTimeOvertime.setBackgroundTintList(null); // Reset background tint
            holder.etBreakTimeTimeOvertime.setBackground(ContextCompat.getDrawable(context, R.drawable.input_infor));
            holder.deleteBreakBtn.setVisibility(View.VISIBLE);
            holder.txt_time_overtime.setVisibility(View.GONE);

            // Xử lý khi chọn thời gian nghỉ giữa giờ
            holder.etBreakTimeTimeOvertime.setOnClickListener(v -> {
                TimePickerDialog startTimePicker = new TimePickerDialog(context, (view, startHour, startMinute) -> {
                    String startTime = String.format("%02d:%02d", startHour, startMinute);
                    breakTime.setStartTime(startTime);

                    TimePickerDialog endTimePicker = new TimePickerDialog(context, (view2, endHour, endMinute) -> {
                        String endTime = String.format("%02d:%02d", endHour, endMinute);
                        int startTotalMinutes = startHour * 60 + startMinute;
                        int endTotalMinutes = endHour * 60 + endMinute;

                        if (endTotalMinutes <= startTotalMinutes) {
                            CustomToast.showCustomToast(context, "Giờ kết thúc phải lớn hơn giờ bắt đầu!");
                        } else {
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
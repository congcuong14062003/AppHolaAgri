package com.example.appholaagri.adapter;

import android.app.TimePickerDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appholaagri.R;
import com.example.appholaagri.model.RequestDetailModel.BreakTime;
import com.example.appholaagri.model.RequestDetailModel.ListDayReq;
import com.example.appholaagri.view.CreateRequestOvertTime;

import java.util.List;

public class BreakTimeOverTimeAdapter extends RecyclerView.Adapter<BreakTimeOverTimeAdapter.BreakTimeViewHolder> {
    private List<BreakTime> breakTimes;
    private List<ListDayReq> listDayReqs;
    private CreateRequestOvertTime createRequestOvertTimeActivity;

    public BreakTimeOverTimeAdapter(List<BreakTime> breakTimes, List<ListDayReq> listDayReqs, CreateRequestOvertTime activity) {
        this.breakTimes = breakTimes;
        this.listDayReqs = listDayReqs;
        this.createRequestOvertTimeActivity = activity;
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
//        holder.etBreakTimeTimeOvertime.setText(breakTime.getStartTime() + " - " + breakTime.getEndTime());

        // Xử lý khi chọn thời gian nghỉ giữa giờ
        holder.etBreakTimeTimeOvertime.setOnClickListener(v -> {
            TimePickerDialog startTimePicker = new TimePickerDialog(holder.itemView.getContext(), (view, startHour, startMinute) -> {
                String startTime = String.format("%02d:%02d", startHour, startMinute);
                breakTime.setStartTime(startTime);

                TimePickerDialog endTimePicker = new TimePickerDialog(holder.itemView.getContext(), (view2, endHour, endMinute) -> {
                    String endTime = String.format("%02d:%02d", endHour, endMinute);
                    breakTime.setEndTime(endTime);

                    // Hiển thị đúng định dạng
                    holder.etBreakTimeTimeOvertime.setText(startTime + " - " + endTime);
                    createRequestOvertTimeActivity.updateRequestDetailData();

                }, 13, 0, true);
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




    @Override
    public int getItemCount() {
        return breakTimes.size();
    }

    public static class BreakTimeViewHolder extends RecyclerView.ViewHolder {
        EditText etBreakTimeTimeOvertime;
        ImageView deleteBreakBtn;

        public BreakTimeViewHolder(@NonNull View itemView) {
            super(itemView);
            etBreakTimeTimeOvertime = itemView.findViewById(R.id.etBreakTimeTimeOvertime);
            deleteBreakBtn = itemView.findViewById(R.id.delete_break_time);
        }
    }
}

package com.example.appholaagri.adapter;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appholaagri.R;
import com.example.appholaagri.model.RequestDetailModel.BreakTime;
import com.example.appholaagri.model.RequestDetailModel.ListDayReq;
import com.example.appholaagri.view.CreateRequestOvertTime;

import java.util.List;

public class DayOverTimeAdapter extends RecyclerView.Adapter<DayOverTimeAdapter.DayViewHolder> {
    private List<ListDayReq> dayReqs;
    private Context context;
    private CreateRequestOvertTime createRequestOvertTimeActivity;
    private BreakTimeOverTimeAdapter breakTimeAdapter;
    public DayOverTimeAdapter(List<ListDayReq> dayReqs, Context context, CreateRequestOvertTime activity) {
        this.dayReqs = dayReqs;
        this.context = context;
        this.createRequestOvertTimeActivity = activity;
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_day_overtime, parent, false);
        return new DayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        ListDayReq dayReq = dayReqs.get(position);
        holder.etDateOvertime.setText(dayReq.getDay());
        holder.etTimeOvertime.setText(dayReq.getStartTime());

        // Kiểm tra xem adapter của breakTimeRecycler đã được gán chưa
        breakTimeAdapter = (BreakTimeOverTimeAdapter) holder.breakTimeRecycler.getAdapter();
        if (breakTimeAdapter == null) {
            breakTimeAdapter = new BreakTimeOverTimeAdapter(dayReq.getBreakTimes(), dayReqs, createRequestOvertTimeActivity);
            holder.breakTimeRecycler.setAdapter(breakTimeAdapter);
        } else {
            // Nếu đã có adapter, chỉ cần cập nhật lại dữ liệu
            breakTimeAdapter.notifyDataSetChanged();
        }

        // Thêm breakTime mới vào list
        holder.addBreakTimeBtn.setOnClickListener(v -> {
            BreakTime newBreakTime = new BreakTime("", "");
            dayReq.getBreakTimes().add(newBreakTime);

            // Cập nhật lại adapter của breakTimeRecycler
            breakTimeAdapter.notifyItemInserted(dayReq.getBreakTimes().size() - 1);

            // Cập nhật lại toàn bộ adapter của Day
            notifyDataSetChanged();  // Đảm bảo cập nhật lại danh sách toàn bộ
        });



        // Xử lý khi ấn nút xóa ngày
        if (dayReqs.size() > 1) {
            holder.deleteDayBtn.setVisibility(View.VISIBLE);
        } else {
            holder.deleteDayBtn.setVisibility(View.GONE);
        }

        // Xử lý khi xóa ngày
        holder.deleteDayBtn.setOnClickListener(v -> {
            dayReqs.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, dayReqs.size());  // Cập nhật lại vị trí các phần tử
        });


        holder.etDateOvertime.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(context);
            datePickerDialog.setOnDateSetListener((view, year, month, dayOfMonth) -> {
                // Dùng String.format để đảm bảo định dạng ngày tháng có 2 chữ số
                String selectedDate = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year);
                dayReq.setDay(selectedDate);
                holder.etDateOvertime.setText(selectedDate);
                createRequestOvertTimeActivity.updateRequestDetailData();
            });
            datePickerDialog.show();
        });


        // Xử lý khi chọn thời gian
        holder.etTimeOvertime.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(context, (view, hourOfDay, minute) -> {
                String startTime = String.format("%02d:%02d", hourOfDay, minute);  // Đảm bảo giờ và phút có 2 chữ số
                dayReq.setStartTime(startTime);
                TimePickerDialog endTimePickerDialog = new TimePickerDialog(context, (view1, hourOfEndDay, minute1) -> {
                    String endTime = String.format("%02d:%02d", hourOfEndDay, minute1);  // Đảm bảo giờ và phút có 2 chữ số
                    dayReq.setEndTime(endTime);
                    String fullTime = startTime + " - " + endTime;
                    holder.etTimeOvertime.setText(fullTime);
                    createRequestOvertTimeActivity.updateRequestDetailData();
                }, 17, 0, true);
                endTimePickerDialog.show();
            }, 8, 0, true);
            timePickerDialog.show();
        });
    }



    @Override
    public int getItemCount() {
        return dayReqs.size();
    }

    public static class DayViewHolder extends RecyclerView.ViewHolder {
        EditText etDateOvertime, etTimeOvertime;
        RecyclerView breakTimeRecycler;
        AppCompatButton addBreakTimeBtn;
        ImageView deleteDayBtn;

        public DayViewHolder(@NonNull View itemView) {
            super(itemView);
            etDateOvertime = itemView.findViewById(R.id.etDateOvertime);
            etTimeOvertime = itemView.findViewById(R.id.etTimeOvertime);
            breakTimeRecycler = itemView.findViewById(R.id.breakTimeRecyclerView);
            addBreakTimeBtn = itemView.findViewById(R.id.add_break_time_btn);
            deleteDayBtn = itemView.findViewById(R.id.delete_day);
            breakTimeRecycler.setLayoutManager(new LinearLayoutManager(itemView.getContext()));

        }
    }
}

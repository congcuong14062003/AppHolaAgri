package com.example.appholaagri.adapter;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
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
import com.example.appholaagri.utils.CustomToast;
import com.example.appholaagri.view.CreateRequestOvertTimeActivity;

import java.util.ArrayList;
import java.util.List;

public class DayOverTimeAdapter extends RecyclerView.Adapter<DayOverTimeAdapter.DayViewHolder> {
    private List<ListDayReq> dayReqs;
    private Context context;
    private CreateRequestOvertTimeActivity createRequestOvertTimeActivity;
    private BreakTimeOverTimeAdapter breakTimeAdapter;
    private int statusRequest;
    public DayOverTimeAdapter(List<ListDayReq> dayReqs, Context context, CreateRequestOvertTimeActivity activity, Integer statusRequest) {
        this.dayReqs = dayReqs;
        this.context = context;
        this.createRequestOvertTimeActivity = activity;
        this.statusRequest = statusRequest;
    }
    public void setStatusRequest(Integer statusRequest) {
        this.statusRequest = statusRequest;
        notifyDataSetChanged(); // Làm mới adapter để cập nhật giao diện
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
        String startTime1 = dayReq.getStartTime();
        String endTime1 = dayReq.getEndTime();
        if (startTime1.isEmpty() || endTime1.isEmpty()) {
            holder.etTimeOvertime.setText("");  // Giữ nguyên hint
        } else {
            holder.etTimeOvertime.setText(startTime1 + " - " + endTime1);
        }


        // Kiểm tra statusRequest để bật/tắt các trường nhập
        if( statusRequest > 1) {
            holder.etDateOvertime.setEnabled(false);
            holder.etDateOvertime.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#dee0df")));
            holder.etTimeOvertime.setEnabled(false);
            holder.etTimeOvertime.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#dee0df")));
            holder.addBreakTimeBtn.setVisibility(View.GONE);
            holder.delete_day.setVisibility(View.GONE);
        }



        // Cập nhật danh sách breakTimes
        if (dayReq.getBreakTimes() == null) {
            dayReq.setBreakTimes(new ArrayList<>()); // Tránh null pointer
        }

        // Đảm bảo RecyclerView có LayoutManager
        if (holder.breakTimeRecycler.getLayoutManager() == null) {
            holder.breakTimeRecycler.setLayoutManager(new LinearLayoutManager(context));
        }

        // Kiểm tra nếu adapter đã tồn tại thì cập nhật dữ liệu, nếu chưa thì khởi tạo mới
        if (holder.breakTimeRecycler.getAdapter() == null) {
            BreakTimeOverTimeAdapter breakTimeAdapter = new BreakTimeOverTimeAdapter(dayReq.getBreakTimes(),context, dayReqs, createRequestOvertTimeActivity, statusRequest);
            holder.breakTimeRecycler.setAdapter(breakTimeAdapter);
        } else {
            // Nếu adapter đã tồn tại, chỉ cần cập nhật dữ liệu mới
            ((BreakTimeOverTimeAdapter) holder.breakTimeRecycler.getAdapter()).updateBreakTimes(dayReq.getBreakTimes());
        }

        // Xử lý khi ấn nút thêm break time
        holder.addBreakTimeBtn.setOnClickListener(v -> {
            BreakTime newBreakTime = new BreakTime("", "");
            dayReq.getBreakTimes().add(newBreakTime);

            // Cập nhật RecyclerView
            holder.breakTimeRecycler.getAdapter().notifyItemInserted(dayReq.getBreakTimes().size() - 1);
            createRequestOvertTimeActivity.updateRequestDetailData();
        });


        if(statusRequest <= 2) {
            // Xử lý khi ấn nút xóa ngày
            if (dayReqs.size() > 1) {
                holder.delete_day.setVisibility(View.VISIBLE);
            } else {
                holder.delete_day.setVisibility(View.GONE);
            }
        }


        // Xử lý khi xóa ngày
        holder.delete_day.setOnClickListener(v -> {
            dayReqs.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, dayReqs.size());  // Cập nhật lại vị trí các phần tử
            notifyDataSetChanged();
            // Gọi hàm cập nhật dữ liệu sau khi xóa
            createRequestOvertTimeActivity.updateRequestDetailData();
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
            TimePickerDialog startTimePickerDialog = new TimePickerDialog(context, (view, startHour, startMinute) -> {
                String startTime = String.format("%02d:%02d", startHour, startMinute);
                dayReq.setStartTime(startTime);

                TimePickerDialog endTimePickerDialog = new TimePickerDialog(context, (view1, endHour, endMinute) -> {
                    String endTime = String.format("%02d:%02d", endHour, endMinute);

                    // Chuyển đổi giờ & phút thành số phút từ đầu ngày
                    int startTotalMinutes = startHour * 60 + startMinute;
                    int endTotalMinutes = endHour * 60 + endMinute;

                    if (endTotalMinutes <= startTotalMinutes) {
                        // Giờ kết thúc không hợp lệ, hiển thị lỗi
                        CustomToast.showCustomToast(context, "Giờ kết thúc phải lớn hơn giờ bắt đầu!");
                    } else {
                        // Cập nhật giờ nếu hợp lệ
                        dayReq.setEndTime(endTime);
                        String fullTime = startTime + " - " + endTime;
                        holder.etTimeOvertime.setText(fullTime);
                        createRequestOvertTimeActivity.updateRequestDetailData();
                    }
                }, startHour + 1, startMinute, true); // Gợi ý giờ kết thúc lớn hơn giờ bắt đầu
                endTimePickerDialog.show();
            }, 8, 0, true);
            startTimePickerDialog.show();
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
        ImageView delete_day;

        public DayViewHolder(@NonNull View itemView) {
            super(itemView);
            etDateOvertime = itemView.findViewById(R.id.etDateOvertime);
            etTimeOvertime = itemView.findViewById(R.id.etTimeOvertime);
            breakTimeRecycler = itemView.findViewById(R.id.breakTimeRecyclerView);
            addBreakTimeBtn = itemView.findViewById(R.id.add_break_time_btn);
            delete_day = itemView.findViewById(R.id.delete_day);
            breakTimeRecycler.setLayoutManager(new LinearLayoutManager(itemView.getContext()));

        }
    }
}

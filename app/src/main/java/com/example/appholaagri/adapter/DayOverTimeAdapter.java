package com.example.appholaagri.adapter;

import android.app.DatePickerDialog;
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

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
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
    private boolean isEdit;
    private RecyclerView dayRecyclerView;
    private List<BreakTimeOverTimeAdapter> breakTimeAdapters = new ArrayList<>();

    public DayOverTimeAdapter(List<ListDayReq> dayReqs, Context context, CreateRequestOvertTimeActivity activity, boolean isEdit, RecyclerView dayRecyclerView) {
        this.dayReqs = dayReqs;
        this.context = context;
        this.createRequestOvertTimeActivity = activity;
        this.isEdit = isEdit;
        this.dayRecyclerView = dayRecyclerView;
    }

    public void setIsEdit(boolean isEdit) {
        this.isEdit = isEdit;
        notifyDataSetChanged();
        for (BreakTimeOverTimeAdapter adapter : breakTimeAdapters) {
            adapter.setIsEdit(isEdit);
        }
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
            holder.etTimeOvertime.setText("");
        } else {
            holder.etTimeOvertime.setText(startTime1 + " - " + endTime1);
        }

        holder.edt_content_request_create.setText(dayReq.getPurpose() != null ? dayReq.getPurpose() : "");
        holder.edt_result_request_create.setText(dayReq.getResult() != null ? dayReq.getResult() : "");

        if (!isEdit) {
            holder.etDateOvertime.setEnabled(false);
            holder.etDateOvertime.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#dee0df")));
            holder.etTimeOvertime.setEnabled(false);
            holder.etTimeOvertime.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#dee0df")));
            holder.edt_content_request_create.setEnabled(false);
            holder.edt_content_request_create.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#dee0df")));
            holder.edt_result_request_create.setEnabled(false);
            holder.edt_result_request_create.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#dee0df")));
            holder.addBreakTimeBtn.setVisibility(View.GONE);
            holder.delete_day.setVisibility(View.GONE);
        } else {
            holder.etDateOvertime.setEnabled(true);
            holder.etDateOvertime.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_background));
            holder.etDateOvertime.setBackgroundTintList(null);

            holder.etTimeOvertime.setEnabled(true);
            holder.etTimeOvertime.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_background_right));
            holder.etTimeOvertime.setBackgroundTintList(null);

            holder.edt_content_request_create.setEnabled(true);
            holder.edt_content_request_create.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_rounded_color_stroke));
            holder.edt_content_request_create.setBackgroundTintList(null);

            holder.edt_result_request_create.setEnabled(true);
            holder.edt_result_request_create.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_rounded_color_stroke));
            holder.edt_result_request_create.setBackgroundTintList(null);

            holder.addBreakTimeBtn.setVisibility(View.VISIBLE);
            // Hiển thị/ẩn nút xóa dựa trên số lượng mục
            holder.delete_day.setVisibility(dayReqs.size() > 1 ? View.VISIBLE : View.GONE);

            holder.edt_content_request_create.addTextChangedListener(new android.text.TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(android.text.Editable s) {
                    dayReq.setPurpose(s.toString());
                    createRequestOvertTimeActivity.updateRequestDetailData();
                }
            });

            holder.edt_result_request_create.addTextChangedListener(new android.text.TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(android.text.Editable s) {
                    dayReq.setResult(s.toString());
                    createRequestOvertTimeActivity.updateRequestDetailData();
                }
            });
        }

        if (dayReq.getBreakTimes() == null) {
            dayReq.setBreakTimes(new ArrayList<>());
        }

        if (holder.breakTimeRecycler.getLayoutManager() == null) {
            holder.breakTimeRecycler.setLayoutManager(new LinearLayoutManager(context));
        }

        if (holder.breakTimeRecycler.getAdapter() == null) {
            BreakTimeOverTimeAdapter breakTimeAdapter = new BreakTimeOverTimeAdapter(dayReq.getBreakTimes(), context, dayReqs, createRequestOvertTimeActivity, isEdit);
            holder.breakTimeRecycler.setAdapter(breakTimeAdapter);
            breakTimeAdapter.setIsEdit(isEdit);
            breakTimeAdapters.add(breakTimeAdapter);
        } else {
            ((BreakTimeOverTimeAdapter) holder.breakTimeRecycler.getAdapter()).updateBreakTimes(dayReq.getBreakTimes());
        }

        holder.addBreakTimeBtn.setOnClickListener(v -> {
            if (isEdit) {
                BreakTime newBreakTime = new BreakTime("", "");
                dayReq.getBreakTimes().add(newBreakTime);
                holder.breakTimeRecycler.getAdapter().notifyItemInserted(dayReq.getBreakTimes().size() - 1);
                createRequestOvertTimeActivity.updateRequestDetailData();
            }
        });

        holder.delete_day.setOnClickListener(v -> {
            if (isEdit) {
                dayReqs.remove(position);
                notifyDataSetChanged(); // Cập nhật toàn bộ danh sách
                createRequestOvertTimeActivity.updateRequestDetailData();
            }
        });

        holder.etDateOvertime.setOnClickListener(v -> {
            if (isEdit) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(context);
                datePickerDialog.setOnDateSetListener((view, year, month, dayOfMonth) -> {
                    String selectedDate = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year);
                    dayReq.setDay(selectedDate);
                    holder.etDateOvertime.setText(selectedDate);
                    createRequestOvertTimeActivity.updateRequestDetailData();
                });
                datePickerDialog.show();
            }
        });

        holder.etTimeOvertime.setOnClickListener(v -> {
            if (isEdit) {
                TimePickerDialog startTimePickerDialog = new TimePickerDialog(context, (view, startHour, startMinute) -> {
                    String startTime = String.format("%02d:%02d", startHour, startMinute);
                    dayReq.setStartTime(startTime);

                    TimePickerDialog endTimePickerDialog = new TimePickerDialog(context, (view1, endHour, endMinute) -> {
                        String endTime = String.format("%02d:%02d", endHour, endMinute);
                        int startTotalMinutes = startHour * 60 + startMinute;
                        int endTotalMinutes = endHour * 60 + endMinute;

                        if (endTotalMinutes <= startTotalMinutes) {
                            CustomToast.showCustomToast(context, "Giờ kết thúc phải lớn hơn giờ bắt đầu!");
                        } else {
                            dayReq.setEndTime(endTime);
                            String fullTime = startTime + " - " + endTime;
                            holder.etTimeOvertime.setText(fullTime);
                            createRequestOvertTimeActivity.updateRequestDetailData();
                        }
                    }, startHour + 1, startMinute, true);
                    endTimePickerDialog.show();
                }, 8, 0, true);
                startTimePickerDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dayReqs.size();
    }

    public static class DayViewHolder extends RecyclerView.ViewHolder {
        EditText etDateOvertime, etTimeOvertime, edt_content_request_create, edt_result_request_create;
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
            edt_content_request_create = itemView.findViewById(R.id.edt_content_request_create);
            edt_result_request_create = itemView.findViewById(R.id.edt_result_request_create);
            breakTimeRecycler.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        }
    }
}
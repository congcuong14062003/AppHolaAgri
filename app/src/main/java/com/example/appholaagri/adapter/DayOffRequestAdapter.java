package com.example.appholaagri.adapter;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appholaagri.R;
import com.example.appholaagri.model.RequestDetailModel.RequestDetailData;
import com.example.appholaagri.model.RequestDetailModel.RequestMethod;
import com.example.appholaagri.utils.CustomToast;
import com.example.appholaagri.view.CreateRequestDayOffActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class DayOffRequestAdapter extends RecyclerView.Adapter<DayOffRequestAdapter.DayViewHolder> {
    private List<RequestDetailData.DayOffFormReq> dayOffFormReqs;
    private Context context;
    private CreateRequestDayOffActivity createRequestDayOffActivity;
    private boolean isEdit;
    private RecyclerView dayRecyclerView;
    private List<RequestMethod> requestMethods;

    public DayOffRequestAdapter(List<RequestDetailData.DayOffFormReq> dayOffFormReqs, Context context,
                                CreateRequestDayOffActivity activity, boolean isEdit, RecyclerView dayRecyclerView) {
        this.dayOffFormReqs = dayOffFormReqs;
        this.context = context;
        this.createRequestDayOffActivity = activity;
        this.isEdit = isEdit;
        this.dayRecyclerView = dayRecyclerView;
        this.requestMethods = new ArrayList<>();
    }

    public void setIsEdit(boolean isEdit) {
        this.isEdit = isEdit;
        notifyDataSetChanged();
    }

    public void setRequestMethods(List<RequestMethod> methods) {
        this.requestMethods = methods;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_day_off_request, parent, false);
        return new DayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        RequestDetailData.DayOffFormReq dayOffFormReq = dayOffFormReqs.get(position);

        // Setup Spinner for RequestMethod
        ArrayAdapter<String> methodAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item);
        methodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        List<String> methodNames = new ArrayList<>();
        methodNames.add("Chọn hình thức");
        for (RequestMethod method : requestMethods) {
            methodNames.add(method.getName());
        }
        methodAdapter.addAll(methodNames);
        holder.selectMethodRequest.setAdapter(methodAdapter);

        // Set selected RequestMethod
        if (dayOffFormReq.getRequestMethod() != null) {
            String selectedMethodName = dayOffFormReq.getRequestMethod().getName();
            int spinnerPosition = methodAdapter.getPosition(selectedMethodName);
            holder.selectMethodRequest.setSelection(spinnerPosition);
        } else {
            holder.selectMethodRequest.setSelection(0); // "Chọn hình thức"
        }

        // Handle Spinner selection
        holder.selectMethodRequest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int spinnerPosition, long id) {
                if (!isEdit || spinnerPosition == 0) return; // Bỏ qua nếu không chỉnh sửa hoặc chọn "Chọn hình thức"
                RequestMethod selectedMethod = requestMethods.get(spinnerPosition - 1); // -1 vì có mục "Chọn hình thức"
                dayOffFormReq.setRequestMethod(selectedMethod);
                createRequestDayOffActivity.updateRequestDetailData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Set Duration
        holder.edtDuration.setText(dayOffFormReq.getDuration() != null ? String.format(Locale.US, "%.2f", dayOffFormReq.getDuration()) : "");

        // Set Date and Time
        holder.etNgayBatDau.setText(dayOffFormReq.getStartDate() != null ? dayOffFormReq.getStartDate() : "");
        holder.etGioBatDau.setText(dayOffFormReq.getStartTime() != null ? dayOffFormReq.getStartTime() : "");
        holder.etNgayKetThuc.setText(dayOffFormReq.getEndDate() != null ? dayOffFormReq.getEndDate() : "");
        holder.etGioKetThuc.setText(dayOffFormReq.getEndTime() != null ? dayOffFormReq.getEndTime() : "");

        // Kiểm tra isEdit để bật/tắt các trường nhập và đặt màu nền
        if (!isEdit) {
            holder.selectMethodRequest.setEnabled(false);
            holder.selectMethodRequest.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#dee0df")));

            holder.edtDuration.setEnabled(false);
            holder.edtDuration.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#dee0df")));

            holder.etNgayBatDau.setEnabled(false);
            holder.etNgayBatDau.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#dee0df")));

            holder.etGioBatDau.setEnabled(false);
            holder.etGioBatDau.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#dee0df")));

            holder.etNgayKetThuc.setEnabled(false);
            holder.etNgayKetThuc.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#dee0df")));

            holder.etGioKetThuc.setEnabled(false);
            holder.etGioKetThuc.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#dee0df")));

            holder.btnDelete.setVisibility(View.GONE);
        } else {
            holder.selectMethodRequest.setEnabled(true);
            holder.selectMethodRequest.setBackground(ContextCompat.getDrawable(context, R.drawable.input_infor));
            holder.selectMethodRequest.setBackgroundTintList(null);

            holder.edtDuration.setEnabled(true);
            holder.edtDuration.setBackground(ContextCompat.getDrawable(context, R.drawable.input_infor));
            holder.edtDuration.setBackgroundTintList(null);

            holder.etNgayBatDau.setEnabled(true);
            holder.etNgayBatDau.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_background));
            holder.etNgayBatDau.setBackgroundTintList(null);

            holder.etGioBatDau.setEnabled(true);
            holder.etGioBatDau.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_background_right));
            holder.etGioBatDau.setBackgroundTintList(null);

            holder.etNgayKetThuc.setEnabled(true);
            holder.etNgayKetThuc.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_background));
            holder.etNgayKetThuc.setBackgroundTintList(null);

            holder.etGioKetThuc.setEnabled(true);
            holder.etGioKetThuc.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_background_right));
            holder.etGioKetThuc.setBackgroundTintList(null);

            // Show/Hide delete button based on number of items
            holder.btnDelete.setVisibility(dayOffFormReqs.size() > 1 ? View.VISIBLE : View.GONE);
        }

        // Date and Time pickers
        holder.etNgayBatDau.setOnClickListener(v -> showDatePicker(holder.etNgayBatDau, position));
        holder.etGioBatDau.setOnClickListener(v -> showTimePicker(holder.etGioBatDau, position));
        holder.etNgayKetThuc.setOnClickListener(v -> showDatePicker(holder.etNgayKetThuc, position));
        holder.etGioKetThuc.setOnClickListener(v -> showTimePicker(holder.etGioKetThuc, position));

        // Duration input validation
        holder.edtDuration.addTextChangedListener(new TextWatcher() {
            private String current = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (!isEdit) return;
                String input = s.toString();
                if (!input.equals(current)) {
                    holder.edtDuration.removeTextChangedListener(this);
                    if (!isValidDecimal(input)) {
                        s.replace(0, s.length(), current);
                    } else {
                        current = input;
                        try {
                            dayOffFormReq.setDuration(input.isEmpty() ? null : Double.parseDouble(input));
                            createRequestDayOffActivity.updateRequestDetailData();
                        } catch (NumberFormatException e) {
                            dayOffFormReq.setDuration(null);
                        }
                    }
                    holder.edtDuration.addTextChangedListener(this);
                }
            }

            private boolean isValidDecimal(String input) {
                if (input.isEmpty() || input.equals(".")) return true;
                return Pattern.matches("^\\d*\\.?\\d{0,2}$", input);
            }
        });

        // Delete button
        holder.btnDelete.setOnClickListener(v -> {
            if (isEdit && dayOffFormReqs.size() > 1) {
                dayOffFormReqs.remove(position);
                notifyDataSetChanged(); // Cập nhật toàn bộ danh sách để đảm bảo nút xóa được ẩn nếu chỉ còn 1 mục
                createRequestDayOffActivity.updateRequestDetailData();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dayOffFormReqs.size();
    }

    private void showDatePicker(EditText editText, int position) {
        if (!isEdit) return;
        final Calendar calendar = Calendar.getInstance();
        if (!editText.getText().toString().isEmpty()) {
            try {
                Date selectedDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(editText.getText().toString());
                if (selectedDate != null) {
                    calendar.setTime(selectedDate);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                (view, year1, month1, dayOfMonth) -> {
                    String selectedDate = String.format("%02d/%02d/%d", dayOfMonth, month1 + 1, year1);
                    editText.setText(selectedDate);
                    if (editText.getId() == R.id.etNgayBatDau) {
                        dayOffFormReqs.get(position).setStartDate(selectedDate);
                    } else {
                        dayOffFormReqs.get(position).setEndDate(selectedDate);
                    }
                    validateDates(position);
                    createRequestDayOffActivity.updateRequestDetailData();
                },
                year, month, day);
        datePickerDialog.show();
    }

    private void showTimePicker(EditText editText, int position) {
        if (!isEdit) return;
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                context,
                (view, hourOfDay, minute1) -> {
                    String selectedTime = String.format("%02d:%02d", hourOfDay, minute1);
                    editText.setText(selectedTime);
                    if (editText.getId() == R.id.etGioBatDau) {
                        dayOffFormReqs.get(position).setStartTime(selectedTime);
                    } else {
                        dayOffFormReqs.get(position).setEndTime(selectedTime);
                    }
                    validateTimes(position);
                    createRequestDayOffActivity.updateRequestDetailData();
                },
                hour, minute, true);
        timePickerDialog.show();
    }

    private void validateDates(int position) {
        RequestDetailData.DayOffFormReq dayOffFormReq = dayOffFormReqs.get(position);
        String startDate = dayOffFormReq.getStartDate();
        String endDate = dayOffFormReq.getEndDate();
        if (!startDate.isEmpty() && !endDate.isEmpty() && compareDates(startDate, endDate) > 0) {
            CustomToast.showCustomToast(context, "Ngày kết thúc không được nhỏ hơn ngày bắt đầu!");
            dayOffFormReq.setEndDate("");
            DayViewHolder holder = (DayViewHolder) dayRecyclerView.findViewHolderForAdapterPosition(position);
            if (holder != null) {
                holder.etNgayKetThuc.setText("");
            }
            notifyItemChanged(position);
        }
        validateTimes(position);
    }

    private void validateTimes(int position) {
        RequestDetailData.DayOffFormReq dayOffFormReq = dayOffFormReqs.get(position);
        String startDate = dayOffFormReq.getStartDate();
        String endDate = dayOffFormReq.getEndDate();
        String startTime = dayOffFormReq.getStartTime();
        String endTime = dayOffFormReq.getEndTime();
        if (!startDate.isEmpty() && !endDate.isEmpty() && startDate.equals(endDate) &&
                !startTime.isEmpty() && !endTime.isEmpty() && compareTimes(startTime, endTime) > 0) {
            CustomToast.showCustomToast(context, "Giờ kết thúc không được nhỏ hơn giờ bắt đầu!");
            dayOffFormReq.setEndTime("");
            DayViewHolder holder = (DayViewHolder) dayRecyclerView.findViewHolderForAdapterPosition(position);
            if (holder != null) {
                holder.etGioKetThuc.setText("");
            }
            notifyItemChanged(position);
        }
    }

    private int compareDates(String date1, String date2) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date d1 = sdf.parse(date1);
            Date d2 = sdf.parse(date2);
            return d1.compareTo(d2);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private int compareTimes(String time1, String time2) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        try {
            Date date1 = sdf.parse(time1);
            Date date2 = sdf.parse(time2);
            return date1.compareTo(date2);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static class DayViewHolder extends RecyclerView.ViewHolder {
        private Spinner selectMethodRequest;
        private EditText edtDuration, etNgayBatDau, etGioBatDau, etNgayKetThuc, etGioKetThuc;
        private ImageView btnDelete;

        public DayViewHolder(@NonNull View itemView) {
            super(itemView);
            selectMethodRequest = itemView.findViewById(R.id.select_method_request);
            edtDuration = itemView.findViewById(R.id.edt_duration);
            etNgayBatDau = itemView.findViewById(R.id.etNgayBatDau);
            etGioBatDau = itemView.findViewById(R.id.etGioBatDau);
            etNgayKetThuc = itemView.findViewById(R.id.etNgayKetThuc);
            etGioKetThuc = itemView.findViewById(R.id.etGioKetThuc);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
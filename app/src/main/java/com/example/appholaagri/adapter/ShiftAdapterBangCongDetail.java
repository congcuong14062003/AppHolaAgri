package com.example.appholaagri.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.appholaagri.R;
import com.example.appholaagri.model.WorkSummaryDetailModel.ShiftSummaryRes;

import java.util.List;

public class ShiftAdapterBangCongDetail extends RecyclerView.Adapter<ShiftAdapterBangCongDetail.ShiftViewHolder> {
    private final List<ShiftSummaryRes> shiftList;

    public ShiftAdapterBangCongDetail(List<ShiftSummaryRes> shiftList) {
        this.shiftList = shiftList;
    }

    @NonNull
    @Override
    public ShiftViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chi_tiet_cong_shift_detail, parent, false);
        return new ShiftViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ShiftViewHolder holder, int position) {
        ShiftSummaryRes shift = shiftList.get(position);
        holder.txtShiftName.setText(shift.getShiftName());
        holder.txtShiftTime.setText(" (" + shift.getStartTime() + " - " + shift.getEndTime() + ")");
        holder.txtShiftNote.setText(shift.getNote());

        // Hiển thị check-in time
        if (shift.getCheckInTime() != null) {
            holder.checkinTimeDetail.setText(shift.getCheckInTime());
        } else {
            // Nếu không có check-in time, hiển thị "--:--" với màu sắc
            SpannableString checkInText = new SpannableString("--:--");
            checkInText.setSpan(new ForegroundColorSpan(Color.parseColor("#ea5b5d")), 0, checkInText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.checkinTimeDetail.setText(checkInText);
        }

        // Hiển thị check-out time
        if (shift.getCheckOutTime() != null) {
            holder.checkoutTimeDetail.setText(shift.getCheckOutTime());
        } else {
            // Nếu không có check-out time, hiển thị "--:--" với màu sắc
            SpannableString checkOutText = new SpannableString("--:--");
            checkOutText.setSpan(new ForegroundColorSpan(Color.parseColor("#ea5b5d")), 0, checkOutText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.checkoutTimeDetail.setText(checkOutText);
        }
    }

    @Override
    public int getItemCount() {
        return shiftList.size();
    }

    public static class ShiftViewHolder extends RecyclerView.ViewHolder {
        TextView txtShiftName, txtShiftNote, txtShiftTime, checkinTimeDetail, checkoutTimeDetail;

        public ShiftViewHolder(@NonNull View itemView) {
            super(itemView);
            txtShiftName = itemView.findViewById(R.id.txt_shift_name);
            txtShiftTime = itemView.findViewById(R.id.txt_shift_time);
            txtShiftNote = itemView.findViewById(R.id.txt_shift_note);
            checkinTimeDetail = itemView.findViewById(R.id.checkinTimeDetail);
            checkoutTimeDetail = itemView.findViewById(R.id.checkoutTimeDetail);
        }
    }
}

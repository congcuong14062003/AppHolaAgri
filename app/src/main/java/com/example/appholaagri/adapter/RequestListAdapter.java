package com.example.appholaagri.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.appholaagri.R;
import com.example.appholaagri.model.RequestModel.RequestListData;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RequestListAdapter extends RecyclerView.Adapter<RequestListAdapter.ViewHolder> {

    private List<RequestListData.RequestData> dayDataList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Integer requestId, Integer StatusRequest, String groupRequestCode, Integer GroupRequest);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public RequestListAdapter(List<RequestListData.RequestData> dayDataList) {
        this.dayDataList = dayDataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.request_item_recycle_view, parent, false);
        return new ViewHolder(view, onItemClickListener);
    }

    public void addData(List<RequestListData.RequestData> newData) {
        if (newData != null && !newData.isEmpty()) {
            int startPosition = dayDataList.size();
            dayDataList.addAll(newData);
            notifyItemRangeInserted(startPosition, newData.size());
        }
    }

    public void clearData() {
        if (dayDataList != null) {
            dayDataList.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RequestListData.RequestData dayData = dayDataList.get(position);

        holder.txt_request_name.setText(dayData.getRequestName());
        holder.txt_employee_name.setText(dayData.getEmployee().getEmployeeName());
        holder.txt_createdDate.setText(dayData.getCreatedDate());
        holder.txt_status.setText(dayData.getStatus().getName());
        holder.txt_code_request.setText(dayData.getRequestCode());

        try {
            String colorCode = dayData.getStatus().getColor();
            int color = Color.parseColor(colorCode);
            holder.txt_status.setTextColor(color);
            int backgroundColor = Color.argb(50, Color.red(color), Color.green(color), Color.blue(color));
            holder.txt_status.setBackgroundTintList(ColorStateList.valueOf(backgroundColor));
        } catch (IllegalArgumentException e) {
            Log.e("RequestAdapter", "Invalid color code: " + dayData.getStatus().getColor());
            holder.txt_status.setTextColor(Color.BLACK);
            holder.txt_status.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
        }

        Picasso.get()
                .load(dayData.getEmployee().getEmployeeAvatar())
                .placeholder(R.drawable.avatar)
                .error(R.drawable.avatar)
                .into(holder.img_avatar_empployee);

        if (dayData.getIsUrgent() == 1) {
            holder.isurgent.setVisibility(View.VISIBLE);
        } else {
            holder.isurgent.setVisibility(View.GONE);
        }

        // Handle copy icon click
        holder.copy_code_request.setOnClickListener(v -> {
            String requestCode = dayData.getRequestCode();
            ClipboardManager clipboard = (ClipboardManager) holder.itemView.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Request Code", requestCode);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(holder.itemView.getContext(), "Đã copy mã đề xuất: " + requestCode, Toast.LENGTH_SHORT).show();
            v.setTag("handled"); // Mark event as handled
        });

        // Prevent parent click when copy icon is clicked
        holder.itemView.setOnClickListener(v -> {
            if (v.getTag() != null && v.getTag().equals("handled")) {
                v.setTag(null); // Reset tag
                return; // Prevent parent click
            }
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(dayData.getRequestId(), dayData.getStatus().getId(), dayData.getGroupRequestCode(), dayData.getGroupRequestType());
            }
        });
    }

    @Override
    public int getItemCount() {
        return dayDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_request_name, txt_employee_name, txt_createdDate, txt_status, txt_code_request;
        public ImageView img_avatar_empployee, isurgent, copy_code_request;

        public ViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            txt_request_name = itemView.findViewById(R.id.txt_request_name);
            txt_employee_name = itemView.findViewById(R.id.txt_employee_name);
            txt_createdDate = itemView.findViewById(R.id.txt_createdDate);
            txt_status = itemView.findViewById(R.id.txt_status);
            img_avatar_empployee = itemView.findViewById(R.id.img_avatar_empployee);
            txt_code_request = itemView.findViewById(R.id.txt_code_request);
            copy_code_request = itemView.findViewById(R.id.copy_code_request);
            isurgent = itemView.findViewById(R.id.isurgent);
        }
    }
}
package com.example.appholaagri.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appholaagri.R;
import com.example.appholaagri.model.IdentificationSensorModel.IdentificationSensorRequest;
import com.example.appholaagri.model.PlanAppInitFormModel.PlanAppInitFormResponse;

import java.util.List;


public class MeasurementLocationAdapter extends RecyclerView.Adapter<MeasurementLocationAdapter.ViewHolder> {

    private List<IdentificationSensorRequest.MonitoringDetail> monitoringDetails;
    private OnItemClickListener listener;
    private int selectedPosition = -1;

    public interface OnItemClickListener {
        void onItemClick(String name);
    }

    public MeasurementLocationAdapter(List<IdentificationSensorRequest.MonitoringDetail> monitoringDetails, OnItemClickListener listener) {
        this.monitoringDetails = monitoringDetails;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_measurement_location, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        IdentificationSensorRequest.MonitoringDetail monitoringDetail = monitoringDetails.get(position);
        holder.delete_measurement.setVisibility(View.GONE);
        // Xử lý khi ấn nút xóa ngày
        if (monitoringDetails.size() > 1) {
            holder.delete_measurement.setVisibility(View.VISIBLE);
        } else {
            holder.delete_measurement.setVisibility(View.GONE);
        }
        // Xử lý khi xóa ngày
        holder.delete_measurement.setOnClickListener(v -> {
            monitoringDetails.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, monitoringDetails.size());  // Cập nhật lại vị trí các phần tử
            notifyDataSetChanged();
            // Gọi hàm cập nhật dữ liệu sau khi xóa

        });
    }


    @Override
    public int getItemCount() {
        return monitoringDetails.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView delete_measurement;

        public ViewHolder(View itemView) {
            super(itemView);
            delete_measurement = itemView.findViewById(R.id.delete_measurement);
        }
    }
}
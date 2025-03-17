package com.example.appholaagri.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appholaagri.R;
import com.example.appholaagri.model.ListSensorModel.ListSensorResponse;
import com.example.appholaagri.model.PlantationListModel.PlantationListResponse;

import java.util.List;

public class SensorListAdapter extends RecyclerView.Adapter<SensorListAdapter.ViewHolder> {

    private List<ListSensorResponse.SensorData> sensorDataList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Integer monitoringId);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public SensorListAdapter(List<ListSensorResponse.SensorData> sensorDataList) {
        this.sensorDataList = sensorDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_sensor, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListSensorResponse.SensorData sensorData = sensorDataList.get(position);

        holder.name_sensor.setText(sensorData.getNameSensor());

        // Chuyển các giá trị khác sang chuỗi
        holder.dondien.setText(String.valueOf(sensorData.getNamePlantation()));
        holder.area.setText(String.valueOf(sensorData.getNameCultivationArea()));
        holder.location.setText(String.valueOf(sensorData.getArea()));
        holder.status_sensor.setText(sensorData.getStatusString());
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(sensorData.getIdMonitoring());
            }
        });
    }


    @Override
    public int getItemCount() {
        return sensorDataList.size();
    }

    public void addData(List<ListSensorResponse.SensorData> newData) {
        if (newData != null && !newData.isEmpty()) {
            int startPosition = sensorDataList.size();
            sensorDataList.addAll(newData);
            notifyItemRangeInserted(startPosition, newData.size());
        }
    }

    public void clearData() {
        if (sensorDataList != null) {
            sensorDataList.clear();
            notifyDataSetChanged();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name_sensor, dondien, area, location, status_sensor;

        public ViewHolder(View itemView) {
            super(itemView);
            name_sensor = itemView.findViewById(R.id.name_sensor);
            dondien = itemView.findViewById(R.id.dondien_txt);
            area = itemView.findViewById(R.id.area);
            location = itemView.findViewById(R.id.location);
            status_sensor = itemView.findViewById(R.id.status_sensor);
        }
    }
}

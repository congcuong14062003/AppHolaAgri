package com.example.appholaagri.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appholaagri.R;
import com.example.appholaagri.model.ListPlantModel.ListPlantResponse;
import com.example.appholaagri.model.ListSensorModel.ListSensorResponse;

import java.util.List;

public class PlantListAdapter extends RecyclerView.Adapter<PlantListAdapter.ViewHolder> {

    private List<ListPlantResponse.PlanData> sensorDataList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Integer plantId);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public PlantListAdapter(List<ListPlantResponse.PlanData> sensorDataList) {
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
        ListPlantResponse.PlanData planData = sensorDataList.get(position);


        // Chuyển các giá trị khác sang chuỗi
        holder.name_sensor.setText(planData.getCode()+ " - " + planData.getNameCropVarieties());
        holder.dondien.setText(String.valueOf(planData.getNamePlantation()));
        holder.area.setText(String.valueOf(planData.getNameCultivationArea()));
        holder.location.setText(String.valueOf(planData.getCultivationAreaFrom()));

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(planData.getId());
            }
        });
    }


    @Override
    public int getItemCount() {
        return sensorDataList.size();
    }

    public void addData(List<ListPlantResponse.PlanData> newData) {
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
        public TextView name_sensor, dondien, area, location;

        public ViewHolder(View itemView) {
            super(itemView);
            name_sensor = itemView.findViewById(R.id.name_sensor);
            dondien = itemView.findViewById(R.id.dondien_txt);
            area = itemView.findViewById(R.id.area);
            location = itemView.findViewById(R.id.location);
        }
    }
}

package com.example.appholaagri.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appholaagri.R;
import com.example.appholaagri.model.PlantationListModel.PlantationListResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PlantationListAdapter extends RecyclerView.Adapter<PlantationListAdapter.ViewHolder> {

    private List<PlantationListResponse.PlantationData> plantationListResponses;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Integer plantationId);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public PlantationListAdapter(List<PlantationListResponse.PlantationData> plantationListResponses) {
        this.plantationListResponses = plantationListResponses;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_plantaion, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlantationListResponse.PlantationData plantation = plantationListResponses.get(position);

        holder.name_plantation.setText(plantation.getName());

        // Chuyển acreage sang chuỗi
        holder.acreage.setText(String.format("%.1f ha", (double) plantation.getAcreage() / 10000));


        // Chuyển các giá trị khác sang chuỗi
        holder.lot_number.setText(String.valueOf(plantation.getAreaName()));
        holder.monitoring_point.setText(String.valueOf(plantation.getTotalMonitoring()));
        holder.number_plant.setText(String.valueOf(plantation.getTotalPlant()));

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(plantation.getId());
            }
        });
    }


    @Override
    public int getItemCount() {
        return plantationListResponses.size();
    }

    public void addData(List<PlantationListResponse.PlantationData> newData) {
        if (newData != null && !newData.isEmpty()) {
            int startPosition = plantationListResponses.size();
            plantationListResponses.addAll(newData);
            notifyItemRangeInserted(startPosition, newData.size());
        }
    }

    public void clearData() {
        if (plantationListResponses != null) {
            plantationListResponses.clear();
            notifyDataSetChanged();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name_plantation, acreage, lot_number,monitoring_point, number_plant;

        public ViewHolder(View itemView) {
            super(itemView);
            name_plantation = itemView.findViewById(R.id.name_plantation);
            acreage = itemView.findViewById(R.id.acreage);
            lot_number = itemView.findViewById(R.id.lot_number);
            monitoring_point = itemView.findViewById(R.id.monitoring_point);
            number_plant = itemView.findViewById(R.id.number_plant);
        }
    }
}

package com.example.appholaagri.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appholaagri.R;
import com.example.appholaagri.model.PlantDetailModel.PlantDetailResponse;

import java.util.List;

public class ListPlantCareHistoryAdapter extends RecyclerView.Adapter<ListPlantCareHistoryAdapter.ViewHolder> {
    private List<PlantDetailResponse.CropCareWork> careList;

    public ListPlantCareHistoryAdapter(List<PlantDetailResponse.CropCareWork> careList) {
        this.careList = careList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plant_care_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlantDetailResponse.CropCareWork care = careList.get(position);

        // Set dữ liệu vào các TextView
        holder.plantDate.setText(care.getRealTime());
        holder.planWork.setText(care.getNameCropCareWork());
        holder.plantImplementer.setText(care.getPerformByName());
        holder.plantingTeam.setText(care.getNameTeam());
        holder.plantContent.setText(care.getDescription());
    }

    @Override
    public int getItemCount() {
        return (careList != null) ? careList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView plantDate, planWork, plantImplementer, plantingTeam, plantContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            plantDate = itemView.findViewById(R.id.plant_date);
            planWork = itemView.findViewById(R.id.plan_work);
            plantImplementer = itemView.findViewById(R.id.plant_implementer);
            plantingTeam = itemView.findViewById(R.id.planting_team);
            plantContent = itemView.findViewById(R.id.plant_content);
        }
    }
}

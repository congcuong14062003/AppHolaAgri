package com.example.appholaagri.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appholaagri.R;
import com.example.appholaagri.model.RecordConditionModel.RecordConditionResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecordConditionAdapter extends RecyclerView.Adapter<RecordConditionAdapter.ViewHolder> {

    private final List<RecordConditionResponse.RecordData> recordDataList;

    public RecordConditionAdapter() {
        this.recordDataList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_record_condition, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecordConditionResponse.RecordData recordData = recordDataList.get(position);

        holder.date_record.setText(recordData.getAtDate());
        holder.employee_record.setText(recordData.getEmployee().getName());

        Picasso.get()
                .load(recordData.getEmployee().getUrl())
                .placeholder(R.drawable.avatar)
                .error(R.drawable.avatar)
                .into(holder.avatar_record);
    }

    @Override
    public int getItemCount() {
        return recordDataList.size();
    }

    public void addData(List<RecordConditionResponse.RecordData> newData) {
        int startPosition = recordDataList.size();
        recordDataList.addAll(newData);
        notifyItemRangeInserted(startPosition, newData.size());
    }

    public void clearData() {
        recordDataList.clear();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView date_record, employee_record;
        public ImageView avatar_record;

        public ViewHolder(View itemView) {
            super(itemView);
            avatar_record = itemView.findViewById(R.id.avatar_record);
            date_record = itemView.findViewById(R.id.date_record);
            employee_record = itemView.findViewById(R.id.employee_record);
        }
    }
}

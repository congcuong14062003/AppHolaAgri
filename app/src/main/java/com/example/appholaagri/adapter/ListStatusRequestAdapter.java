package com.example.appholaagri.adapter;
import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.appholaagri.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appholaagri.model.RequestStatusModel.RequestStatusData;

import java.util.List;
public class ListStatusRequestAdapter extends RecyclerView.Adapter<ListStatusRequestAdapter.ViewHolder> {

    private final List<RequestStatusData> statusList;
    private final OnStatusClickListener listener;
    private int selectedPosition = -1; // Vị trí mặc định
    public interface OnStatusClickListener {
        void onStatusClick(RequestStatusData status);
    }

    public ListStatusRequestAdapter(List<RequestStatusData> statusList, OnStatusClickListener listener) {
        this.statusList = statusList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_status_request_recycle_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,@SuppressLint("RecyclerView") int position) {
        RequestStatusData requestStatusData = statusList.get(position);
        RequestStatusData status = statusList.get(position);
        holder.statusName.setText(status.getName());
        holder.statusCount.setText( " (" + status.getCount() + ")");
        holder.itemView.setOnClickListener(v -> {
            selectedPosition = position;
            listener.onStatusClick(status);
                }
        );
    }

    @Override
    public int getItemCount() {
        return statusList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView statusName, statusCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            statusName = itemView.findViewById(R.id.status_name);
            statusCount = itemView.findViewById(R.id.status_count);
        }
    }
}
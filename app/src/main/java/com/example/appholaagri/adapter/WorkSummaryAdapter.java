package com.example.appholaagri.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.appholaagri.R;
import com.example.appholaagri.model.WorkSummaryModel.WorkSummaryData;
import com.example.appholaagri.view.WorkSummaryDetailActivity;

import java.util.List;

public class WorkSummaryAdapter extends RecyclerView.Adapter<WorkSummaryAdapter.ViewHolder> {

    private List<WorkSummaryData.Item> dayDataList;

    public WorkSummaryAdapter(List<WorkSummaryData.Item> dayDataList) {
        this.dayDataList = dayDataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bang_cong_item_recycle_view, parent, false);
        return new ViewHolder(view);
    }

    public void addData(List<WorkSummaryData.Item> newData) {
        int startPosition = dayDataList.size();
        dayDataList.addAll(newData);
        notifyItemRangeInserted(startPosition, newData.size());
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Lấy dữ liệu ngày
        WorkSummaryData.Item dayData = dayDataList.get(position);

        // Hiển thị ngày
        holder.name_bang_cong.setText(dayData.getWorkSummaryMonthly().getName());
        holder.time_bang_cong.setText(dayData.getTime().getCreatedDate());
        // Khi người dùng nhấn vào item
        holder.itemView.setOnClickListener(v -> {
            // Tạo Intent để chuyển sang màn hình chi tiết
            Intent intent = new Intent(v.getContext(), WorkSummaryDetailActivity.class);

            // Truyền dữ liệu cần thiết
            intent.putExtra("keySearch", "");
            intent.putExtra("page", 1);
            intent.putExtra("size", 20);

            // Truyền thông tin công việc vào Intent (dữ liệu WorkSummaryMonthly)

            intent.putExtra("workSummaryMonthly_code", dayData.getWorkSummaryMonthly().getCode());
            intent.putExtra("workSummaryMonthly_id", dayData.getWorkSummaryMonthly().getId());
            intent.putExtra("workSummaryMonthly_name", dayData.getWorkSummaryMonthly().getName());
            intent.putExtra("workSummaryMonthly_status", dayData.getWorkSummaryMonthly().getStatus());
            intent.putExtra("displayDate", dayData.getDisplayDate());

            // Start the activity
            v.getContext().startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return dayDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView time_bang_cong, name_bang_cong;


        public ViewHolder(View itemView) {
            super(itemView);
            name_bang_cong = itemView.findViewById(R.id.name_bang_cong);
            time_bang_cong = itemView.findViewById(R.id.time_bang_cong);
        }
    }
}
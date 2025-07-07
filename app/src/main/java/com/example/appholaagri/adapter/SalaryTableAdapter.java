package com.example.appholaagri.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.appholaagri.R;
import com.example.appholaagri.model.SalaryTableModel.SalaryTableResponse;
import com.example.appholaagri.view.SalaryTableDetailActivity;

import java.util.List;

public class SalaryTableAdapter extends RecyclerView.Adapter<SalaryTableAdapter.ViewHolder> {

    private List<SalaryTableResponse.Data> dayDataList;

    public SalaryTableAdapter(List<SalaryTableResponse.Data> dayDataList) {
        this.dayDataList = dayDataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bang_luong_item_recycle_view, parent, false);
        return new ViewHolder(view);
    }

    public void addData(List<SalaryTableResponse.Data> newData) {
        int startPosition = dayDataList.size();
        dayDataList.addAll(newData);
        notifyItemRangeInserted(startPosition, newData.size());
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Lấy dữ liệu ngày
        SalaryTableResponse.Data dayData = dayDataList.get(position);

        // Hiển thị ngày
        holder.name_bang_cong.setText(dayData.getName());
        holder.time_bang_cong.setText(dayData.getModifiedDate());
        // Khi người dùng nhấn vào item
        holder.itemView.setOnClickListener(v -> {
            // Tạo Intent để chuyển sang màn hình chi tiết
            Intent intent = new Intent(v.getContext(), SalaryTableDetailActivity.class);
            intent.putExtra("idSalary", dayData.getId());

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
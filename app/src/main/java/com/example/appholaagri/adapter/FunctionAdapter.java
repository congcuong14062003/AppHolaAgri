package com.example.appholaagri.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appholaagri.R;
import com.example.appholaagri.model.FunctionItemHomeModel.FunctionItemHomeModel;

import java.util.List;

public class FunctionAdapter extends RecyclerView.Adapter<FunctionAdapter.ViewHolder> {
    private List<FunctionItemHomeModel> functionList;
    private Context context;
    private boolean isFarmItem;

    public FunctionAdapter(Context context, List<FunctionItemHomeModel> functionList, boolean isFarmItem) {
        this.context = context;
        this.functionList = functionList;
        this.isFarmItem = isFarmItem;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = isFarmItem ? R.layout.item_function_farm : R.layout.item_function_home;
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FunctionItemHomeModel item = functionList.get(position);
        holder.functionName.setText(item.getName());
        holder.functionIcon.setImageResource(item.getIconResId());

        holder.itemView.setOnClickListener(v -> {
            if (item.getTargetActivity() != null) {
                Intent intent = new Intent(context, item.getTargetActivity());
                // Truyền GroupRequestId và GroupRequestType nếu có
                if (item.getGroupRequestId() != null) {
                    intent.putExtra("GroupRequestId", item.getGroupRequestId());
                }
                if (item.getGroupRequestType() != null) {
                    intent.putExtra("GroupRequestType", item.getGroupRequestType());
                }
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return functionList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView functionIcon;
        TextView functionName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            functionIcon = itemView.findViewById(R.id.function_icon);
            functionName = itemView.findViewById(R.id.function_name);
        }
    }
}
package com.example.appholaagri.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appholaagri.R;
import com.example.appholaagri.model.FunctionItemHomeModel.FunctionItemHomeModel;
import com.squareup.picasso.Picasso;

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

        // Adjust item width for Farm Management
        if (isFarmItem) {
            int width = parent.getWidth() / Math.min(functionList.size(), 3); // Divide by item count or 3, whichever is smaller
            ViewGroup.LayoutParams params = view.getLayoutParams();
            params.width = width;
            view.setLayoutParams(params);
        } else {
            // For other sections, ensure 4 items per row (width handled by GridLayoutManager)
            ViewGroup.LayoutParams params = view.getLayoutParams();
            params.width = parent.getWidth() / 4; // Divide by 4 for 4 items per row
            view.setLayoutParams(params);
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FunctionItemHomeModel item = functionList.get(position);
        holder.functionName.setText(item.getName());

        if (!TextUtils.isEmpty(item.getIconResId())) {
            Picasso.get()
                    .load(item.getIconResId())
                    .placeholder(R.drawable.avatar)
                    .error(R.drawable.avatar)
                    .fit()
                    .centerCrop()
                    .into(holder.functionIcon);
        } else {
            holder.functionIcon.setImageResource(R.drawable.avatar);
        }

        holder.itemView.setOnClickListener(v -> {
            if (item.getTargetActivity() != null) {
                Intent intent = new Intent(context, item.getTargetActivity());
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
package com.example.appholaagri.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.example.appholaagri.R;
import com.example.appholaagri.model.RequestDetailModel.RequestMethod;
import java.util.List;

public class RequestMethodAdapter extends RecyclerView.Adapter<RequestMethodAdapter.ViewHolder> {
    private List<RequestMethod> methodList;
    private OnItemClickListener onItemClickListener;
    private int selectedPosition = -1;
    private TextView selectedTextView;
    private View overlay_background;
    private ConstraintLayout overlayFilterStatus;

    public interface OnItemClickListener {
        void onItemClick(RequestMethod method);
    }

    public RequestMethodAdapter(List<RequestMethod> methodList, TextView selectedTextView,
                                ConstraintLayout overlayFilterStatus, View overlay_background,
                                Integer GroupRequestType, OnItemClickListener listener) {
        this.methodList = methodList;
        this.onItemClickListener = listener;
        this.selectedTextView = selectedTextView;
        this.overlay_background = overlay_background;
        this.overlayFilterStatus = overlayFilterStatus;
        setDefaultSelection(GroupRequestType);
    }

    public void setSelectedMethodById(int methodId) {
        for (int i = 0; i < methodList.size(); i++) {
            if (methodList.get(i).getId() == methodId) {
                selectedPosition = i;
                selectedTextView.setText(methodList.get(i).getName());
                notifyDataSetChanged();
                onItemClickListener.onItemClick(methodList.get(i));
                break;
            }
        }
    }

    private void setDefaultSelection(Integer GroupRequestType) {
        int defaultId = (GroupRequestType == 2) ? 3 : (GroupRequestType == 1) ? 1 : -1;
        setSelectedMethodById(defaultId);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_request_method, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        RequestMethod method = methodList.get(position);
        holder.textView.setText(method.getName());
        holder.itemView.setBackgroundResource(position == selectedPosition ? R.drawable.bg_rounded_checked : R.drawable.bg_rounded);

        holder.itemView.setOnClickListener(v -> {
            setSelectedMethodById(method.getId());
            if (overlay_background != null) overlay_background.setVisibility(View.GONE);
            if (overlayFilterStatus != null) overlayFilterStatus.setVisibility(View.GONE);
        });
    }

    @Override
    public int getItemCount() {
        return methodList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_method_name);
        }
    }
}

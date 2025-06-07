package com.example.appholaagri.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appholaagri.R;
import com.example.appholaagri.model.RequestDetailModel.Follower;

import java.util.ArrayList;
import java.util.List;

public class FollowerAdapter extends RecyclerView.Adapter<FollowerAdapter.ViewHolder> {
    private List<Follower> followerList;
    private List<Follower> selectedFollowers;
    private OnFollowerClickListener listener;

    public interface OnFollowerClickListener {
        void onFollowerClick(Follower follower);
    }

    public FollowerAdapter(List<Follower> followerList, List<Follower> selectedFollowers, OnFollowerClickListener listener) {
        this.followerList = new ArrayList<>(followerList);
        this.selectedFollowers = selectedFollowers;
        this.listener = listener;
    }

    public void updateList(List<Follower> newList) {
        this.followerList = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_follower_select, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Follower follower = followerList.get(position);
        holder.tvEmployeeName.setText(follower.getName() != null ? follower.getName() : "");
        holder.tvEmployeeCode.setText(follower.getCode() != null ? follower.getCode() : "");
        holder.tvEmployeeTeam.setText(follower.getTeam() != null ? follower.getTeam() : "");

        // Hiển thị biểu tượng checked nếu follower có trong selectedFollowers
        boolean isSelected = selectedFollowers.stream().anyMatch(selected -> selected.getId() == follower.getId());
        holder.imgCheckedIcon.setVisibility(isSelected ? View.VISIBLE : View.GONE);

        // Sự kiện nhấn vào item
        holder.itemView.setOnClickListener(v -> listener.onFollowerClick(follower));
    }

    @Override
    public int getItemCount() {
        return followerList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvatarConfirm, imgCheckedIcon;
        TextView tvEmployeeName, tvEmployeeCode, tvEmployeeTeam;

        ViewHolder(View itemView) {
            super(itemView);
            imgAvatarConfirm = itemView.findViewById(R.id.imgAvatarConfirm);
            imgCheckedIcon = itemView.findViewById(R.id.imgCheckedIcon);
            tvEmployeeName = itemView.findViewById(R.id.tvEmployeeName);
            tvEmployeeCode = itemView.findViewById(R.id.tvEmployeeCode);
            tvEmployeeTeam = itemView.findViewById(R.id.tvEmployeeTeam);
        }
    }
}
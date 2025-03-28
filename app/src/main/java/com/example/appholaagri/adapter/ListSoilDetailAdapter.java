package com.example.appholaagri.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appholaagri.R;
import com.example.appholaagri.model.PlantDetailModel.PlantDetailResponse;
import com.example.appholaagri.model.SoilDetailModel.SoilDetailRespose;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ListSoilDetailAdapter extends RecyclerView.Adapter<ListSoilDetailAdapter.ViewHolder> {
    private List<SoilDetailRespose.SoilInfo> soilInfos;

    public ListSoilDetailAdapter(List<SoilDetailRespose.SoilInfo> soilInfos) {
        this.soilInfos = soilInfos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nutrition_soil, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (soilInfos == null || position >= soilInfos.size()) return; // Tránh lỗi truy cập ngoài danh sách

        SoilDetailRespose.SoilInfo care = soilInfos.get(position);
        if (care == null) return; // Kiểm tra null

        if (care.getType() == 1 || care.getType() == 4) {
            holder.circle.setVisibility(View.VISIBLE);
            if(care.getSoilIndex50cm() != null) {
                holder.circle_50.setVisibility(View.VISIBLE);
            }
        } else if (care.getType() == 2 || care.getType() == 3) {
            if(care.getSoilIndex50cm() != null) {
                holder.line_50.setVisibility(View.VISIBLE);
            }
            holder.line.setVisibility(View.VISIBLE);
        }

        if (care.getType() == 1) {
            holder.image_soil.setImageResource(R.drawable.zero_to_hundred);
            holder.image_soil_50.setImageResource(R.drawable.zero_to_hundred);
        } else if (care.getType() == 4) {
            holder.image_soil.setImageResource(R.drawable.do_am_image);
            holder.image_soil_50.setImageResource(R.drawable.do_am_image);
        }

        if (care.getType() == 2 || care.getType() == 3) {
            holder.line_soil.setImageResource(
                    care.getType() == 2 ? R.drawable.line : R.drawable.dashed_line_plant
            );
            holder.line_soil_50.setImageResource(
                    care.getType() == 2 ? R.drawable.line : R.drawable.dashed_line_plant
            );
            // Nếu type == 2 thì đặt marginTop = 5dp
            if (care.getType() == 2) {
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.line_soil.getLayoutParams();
                params.topMargin = (int) (5 * holder.itemView.getResources().getDisplayMetrics().density); // Chuyển dp -> px
                holder.line_soil.setLayoutParams(params);

                ViewGroup.MarginLayoutParams params2 = (ViewGroup.MarginLayoutParams) holder.line_soil_50.getLayoutParams();
                params.topMargin = (int) (5 * holder.itemView.getResources().getDisplayMetrics().density); // Chuyển dp -> px
                holder.line_soil_50.setLayoutParams(params);
            }
        }

        // Kiểm tra null trước khi truy cập SoilIndex30cm
        if (care.getSoilIndex30cm() != null) {
            if (care.getType() == 2) {
                holder.status_line.setText(care.getSoilIndex30cm().getConclude());

                float optimalFrom = (float) care.getSoilIndex30cm().getOptimalQuantityFrom();
                float optimalTo = (float) care.getSoilIndex30cm().getOptimalQuantityTo();
                float realQuantity = (float) care.getSoilIndex30cm().getRealQuantity();

                holder.tvOptimalFrom.setText(String.valueOf(optimalFrom));
                holder.tvOptimalTo.setText(String.valueOf(optimalTo));

                float minValue = optimalFrom - (optimalTo - optimalFrom);
                float maxValue = optimalTo + (optimalTo - optimalFrom);


                float realPosition;

                if (realQuantity == 0) {
                    realPosition = 0f; // Nếu realQuantity_50 = 0, đặt vị trí về bên trái cuối cùng
                } else {
                    realPosition = (realQuantity - minValue) / (maxValue - minValue);
                    realPosition = Math.max(0f, Math.min(1f, realPosition)); // Giới hạn từ 0 đến 1
                }

//                float realPosition = (realQuantity - minValue) / (maxValue - minValue);
//                realPosition = Math.max(0f, Math.min(1f, realPosition)); // Giới hạn từ 0 đến 1

                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) holder.dot_line.getLayoutParams();
                params.horizontalBias = realPosition; // Gán vị trí theo tỉ lệ
                holder.dot_line.setLayoutParams(params);
            } else if (care.getType() == 3) {
                holder.line_item.setVisibility(View.GONE);
                holder.dot_line.setVisibility(View.GONE);
                holder.dot_line_arrow.setVisibility(View.VISIBLE);

                float realQuantity = (float) care.getSoilIndex30cm().getRealQuantity();
                float realPosition = realQuantity / 14f; // Giới hạn trong khoảng 0 - 1
                realPosition = Math.max(0f, Math.min(1f, realPosition));

                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) holder.dot_line_arrow.getLayoutParams();
                params.horizontalBias = realPosition;
                holder.dot_line_arrow.setLayoutParams(params);
            }

            // Gán giá trị nhiệt độ
            double temperature = care.getSoilIndex30cm().getRealQuantity();
            holder.value_soil.setText(temperature + " " + care.getSoilIndex30cm().getUnit());
            holder.status_soil.setText(care.getSoilIndex30cm().getConclude());

            // Tính toán góc xoay
            float rotation = -120 + ((float) temperature * 240 / 100);
            holder.arrow_pointer.setRotation(rotation);
        }



        if (care.getSoilIndex50cm() != null) {
            if (care.getType() == 2) {
                holder.status_line_50.setText(care.getSoilIndex50cm().getConclude());

                float optimalFrom_50 = (float) care.getSoilIndex50cm().getOptimalQuantityFrom();
                float optimalTo_50 = (float) care.getSoilIndex50cm().getOptimalQuantityTo();
                float realQuantity_50 = (float) care.getSoilIndex50cm().getRealQuantity();

                holder.tv_optimal_from_50.setText(String.valueOf(optimalFrom_50));
                holder.tv_optimal_to_50.setText(String.valueOf(optimalTo_50));

                float minValue = optimalFrom_50 - (optimalTo_50 - optimalFrom_50);
                float maxValue = optimalTo_50 + (optimalTo_50 - optimalFrom_50);

                float realPosition;

                if (realQuantity_50 == 0) {
                    realPosition = 0f; // Nếu realQuantity_50 = 0, đặt vị trí về bên trái cuối cùng
                } else {
                    realPosition = (realQuantity_50 - minValue) / (maxValue - minValue);
                    realPosition = Math.max(0f, Math.min(1f, realPosition)); // Giới hạn từ 0 đến 1
                }

                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) holder.dot_line_50.getLayoutParams();
                params.horizontalBias = realPosition; // Gán vị trí theo tỉ lệ
                holder.dot_line_50.setLayoutParams(params);
            } else if (care.getType() == 3) {
                holder.line_item_50.setVisibility(View.GONE);
                holder.dot_line_50.setVisibility(View.GONE);
                holder.dot_line_arrow_50.setVisibility(View.VISIBLE);

                float realQuantity = (float) care.getSoilIndex50cm().getRealQuantity();
                float realPosition = realQuantity / 14f; // Giới hạn trong khoảng 0 - 1
                realPosition = Math.max(0f, Math.min(1f, realPosition));

                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) holder.dot_line_arrow_50.getLayoutParams();
                params.horizontalBias = realPosition;
                holder.dot_line_arrow_50.setLayoutParams(params);
            }
                // Gán giá trị nhiệt độ
                double temperature_50 = care.getSoilIndex50cm().getRealQuantity();
                holder.value_soil_50.setText(temperature_50 + " " + care.getSoilIndex50cm().getUnit());
                holder.status_soil_50.setText(care.getSoilIndex50cm().getConclude());

                // Tính toán góc xoay
                float rotation_50 = -120 + ((float) temperature_50 * 240 / 100);
                holder.arrow_pointer_50.setRotation(rotation_50);
        }

        if (care.getIconUrl() != null) {
            Picasso.get()
                    .load(care.getIconUrl())
                    .placeholder(R.drawable.x_mark)
                    .error(R.drawable.x_mark)
                    .into(holder.icon_nutritional_conditions);
        }

        // Gán dữ liệu chung
        holder.msm_line.setText(care.getSoilIndex30cm().getRealQuantity() + " " + care.getSoilIndex30cm().getUnit());
        if(care.getSoilIndex50cm() != null) {
            holder.msm_line_50.setText(care.getSoilIndex50cm().getRealQuantity() + " " + care.getSoilIndex50cm().getUnit());
        }
       ;
        holder.name_title.setText(care.getNameVi() != null ? care.getNameVi() : "N/A");
        holder.soil_date.setText(care.getDate() != null ? care.getDate() : "N/A");

        if (care.getWarning() != null && !care.getWarning().isEmpty()) {
            holder.warning_layout.setVisibility(View.VISIBLE);
            holder.warning.setText(care.getWarning());
        } else {
            holder.warning_layout.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return (soilInfos != null) ? soilInfos.size() : 0;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout circle, warning_layout, line, line_item,
                circle_50, line_50, line_item_50;
        ImageView icon_nutritional_conditions, arrow_pointer, image_soil, line_soil, dot_line,dot_line_arrow,
                arrow_pointer_50, dot_line_50, line_soil_50, image_soil_50, dot_line_arrow_50;
        TextView name_title, soil_date, value_soil, status_soil, warning, tvOptimalFrom, tvOptimalTo, msm_line, status_line,
                value_soil_50, status_soil_50, tv_optimal_from_50, tv_optimal_to_50, status_line_50, msm_line_50;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            circle = itemView.findViewById(R.id.circle);
            line = itemView.findViewById(R.id.line);
            icon_nutritional_conditions = itemView.findViewById(R.id.icon_nutritional_conditions);
            arrow_pointer = itemView.findViewById(R.id.arrow_pointer);  // ⚡ Thêm ánh xạ này
            name_title = itemView.findViewById(R.id.name_title);
            soil_date = itemView.findViewById(R.id.soil_date);
            value_soil = itemView.findViewById(R.id.value_soil);
            status_soil = itemView.findViewById(R.id.status_soil);
            status_line = itemView.findViewById(R.id.status_line);
            warning = itemView.findViewById(R.id.warning);
            warning_layout = itemView.findViewById(R.id.warning_layout);
            image_soil = itemView.findViewById(R.id.image_soil);
            tvOptimalFrom = itemView.findViewById(R.id.tv_optimal_from); // Thanh ngang
            tvOptimalTo = itemView.findViewById(R.id.tv_optimal_to); // Thanh ngang
            dot_line = itemView.findViewById(R.id.dot_line); // Chấm tròn
            line_soil = itemView.findViewById(R.id.line_soil); // Chấm tròn
            msm_line = itemView.findViewById(R.id.msm_line); // Chấm tròn
            line_item = itemView.findViewById(R.id.line_item); // Chấm tròn
            dot_line_arrow = itemView.findViewById(R.id.dot_line_arrow); // Chấm tròn

            // 50
            arrow_pointer_50 = itemView.findViewById(R.id.arrow_pointer_50);  // ⚡ Thêm ánh xạ này
            value_soil_50 = itemView.findViewById(R.id.value_soil_50);  // ⚡ Thêm ánh xạ này
            status_soil_50 = itemView.findViewById(R.id.status_soil_50);  // ⚡ Thêm ánh xạ này
            circle_50 = itemView.findViewById(R.id.circle_50);  // ⚡ Thêm ánh xạ này
            dot_line_50 = itemView.findViewById(R.id.dot_line_50);  // ⚡ Thêm ánh xạ này
            line_soil_50 = itemView.findViewById(R.id.line_soil_50);  // ⚡ Thêm ánh xạ này
            tv_optimal_from_50 = itemView.findViewById(R.id.tv_optimal_from_50);  // ⚡ Thêm ánh xạ này
            tv_optimal_to_50 = itemView.findViewById(R.id.tv_optimal_to_50);  // ⚡ Thêm ánh xạ này
            status_line_50 = itemView.findViewById(R.id.status_line_50);  // ⚡ Thêm ánh xạ này
            msm_line_50 = itemView.findViewById(R.id.msm_line_50);  // ⚡ Thêm ánh xạ này
            line_50 = itemView.findViewById(R.id.line_50);  // ⚡ Thêm ánh xạ này
            image_soil_50 = itemView.findViewById(R.id.image_soil_50);  // ⚡ Thêm ánh xạ này
            line_item_50 = itemView.findViewById(R.id.line_item_50);  // ⚡ Thêm ánh xạ này
            dot_line_arrow_50 = itemView.findViewById(R.id.dot_line_arrow_50);  // ⚡ Thêm ánh xạ này
        }
    }

}

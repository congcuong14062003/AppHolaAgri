package com.example.appholaagri.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appholaagri.R;
import com.example.appholaagri.model.IdentificationSensorModel.IdentificationSensorRequest;
import com.example.appholaagri.model.PlanAppInitFormModel.PlanAppInitFormResponse;
import com.example.appholaagri.model.SensorAppInitFormModel.SensorAppInitFormResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MeasurementLocationAdapter extends RecyclerView.Adapter<MeasurementLocationAdapter.ViewHolder> {
    private List<IdentificationSensorRequest.MonitoringDetail> monitoringDetails;
    private Context context;
    private OnItemClickListener listener;
    private List<SensorAppInitFormResponse.Area> areaList; // Danh sách khu vực
    private OnMonitoringDetailsChangeListener monitoringDetailsChangeListener;

    public interface OnItemClickListener {
        void onItemClick(String name);
    }
    public interface OnItemSelectedCallback {
        void onItemSelected(int position);
    }
    public void setOnMonitoringDetailsChangeListener(OnMonitoringDetailsChangeListener listener) {
        this.monitoringDetailsChangeListener = listener;
    }
    public MeasurementLocationAdapter(Context context, List<IdentificationSensorRequest.MonitoringDetail> monitoringDetails,
                                      List<SensorAppInitFormResponse.Area> areaList) {
        this.context = context;
        this.monitoringDetails = monitoringDetails;
        this.areaList = new ArrayList<>(areaList); // Sao chép danh sách

        // Log danh sách khu vực khi adapter khởi tạo
        Log.d("MeasurementLocationAdapter", "Danh sách Area khi khởi tạo: " + areaList.toString());
    }
    public void updateMonitoringDetails(List<IdentificationSensorRequest.MonitoringDetail> newMonitoringDetails) {
        this.monitoringDetails.clear();
        this.monitoringDetails.addAll(newMonitoringDetails);
        notifyDataSetChanged();
    }

    public void updateAreaList(List<SensorAppInitFormResponse.Area> newAreaList) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String newAreaLists = gson.toJson(newAreaList);
        Log.d("MeasurementLocationAdapter", "Updating areaList: " + newAreaLists);

        this.areaList.clear();
        this.areaList.addAll(newAreaList);
        notifyDataSetChanged();  // Cập nhật toàn bộ RecyclerView
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_measurement_location, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        IdentificationSensorRequest.MonitoringDetail detail = monitoringDetails.get(position);
        holder.delete_measurement.setVisibility(View.GONE);
        if (monitoringDetails.size() > 1) {
            holder.delete_measurement.setVisibility(View.VISIBLE);
        } else {
            holder.delete_measurement.setVisibility(View.GONE);
        }
        holder.delete_measurement.setOnClickListener(view -> {
            removeItem(position);
        });

        // Gọi hàm cập nhật dữ liệu sau khi xóa
        // Ở đây bạn có thể dùng areaList để hiển thị dữ liệu động
        holder.bind(detail); // Gọi bind để cập nhật Spinner
    }
    public void removeItem(int position) {
        if (position >= 0 && position < monitoringDetails.size()) {
            monitoringDetails.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, monitoringDetails.size());

            if (monitoringDetailsChangeListener != null) {
                monitoringDetailsChangeListener.onMonitoringDetailsChanged(new ArrayList<>(monitoringDetails));
            }
        }
    }


    @Override
    public int getItemCount() {
        return monitoringDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Spinner spinnerArea, spinnerRowFrom, spinnerRowTo, spinnerColumnFrom, spinnerColumnTo;
        ImageView delete_measurement;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            spinnerArea = itemView.findViewById(R.id.spinnerArea);
            spinnerRowFrom = itemView.findViewById(R.id.spinnerRowFrom);
            spinnerRowTo = itemView.findViewById(R.id.spinnerRowTo);
            spinnerColumnFrom = itemView.findViewById(R.id.spinnerColumnFrom);
            spinnerColumnTo = itemView.findViewById(R.id.spinnerColumnTo);
            delete_measurement = itemView.findViewById(R.id.delete_measurement);
        }

        public void bind(IdentificationSensorRequest.MonitoringDetail item) {
            // Tạo danh sách khu vực
            List<String> areaNames = new ArrayList<>();
            areaNames.add("--Chọn khu vực--");
            for (SensorAppInitFormResponse.Area area : areaList) {
                areaNames.add(area.getName());
            }

            ArrayAdapter<String> areaAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, areaNames);
            areaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerArea.setAdapter(areaAdapter);

            // Tự động chọn khu vực
            if (item.getIdCultivationArea() > 0) {
                for (int i = 0; i < areaList.size(); i++) {
                    if (areaList.get(i).getId() == item.getIdCultivationArea()) {
                        spinnerArea.setSelection(i + 1); // +1 do vị trí 0 là "--Chọn khu vực--"
                        break;
                    }
                }
            } else {
                spinnerArea.setSelection(0);
            }

            spinnerArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position > 0) {
                        SensorAppInitFormResponse.Area selectedArea = areaList.get(position - 1);
                        item.setIdCultivationArea(selectedArea.getId());

                        // Cập nhật danh sách hàng & cột khi chọn khu vực
                        updateRowAndColumnSpinners(selectedArea, item);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
        }

        // 📌 Hàm cập nhật Spinner hàng/cột
        private void updateRowAndColumnSpinners(SensorAppInitFormResponse.Area selectedArea, IdentificationSensorRequest.MonitoringDetail item) {
            // Cập nhật danh sách hàng
            List<String> rowList = new ArrayList<>();
            rowList.add("--Chọn hàng--");
            for (int i = 1; i <= selectedArea.getTotalRow(); i++) {
                rowList.add("Hàng " + i);
            }

            // Cập nhật danh sách cột
            List<String> columnList = new ArrayList<>();
            columnList.add("--Chọn cột--");
            for (int i = 1; i <= selectedArea.getTotalColumn(); i++) {
                columnList.add("Cột " + i);
            }

            // Cập nhật Spinner RowFrom
            setSpinnerAdapter(spinnerRowFrom, rowList, selectedPos -> {
                item.setRowFrom(selectedPos > 0 ? selectedPos : 0);
                logMonitoringDetails();
            });

            // Cập nhật Spinner RowTo
            setSpinnerAdapter(spinnerRowTo, rowList, selectedPos -> {
                item.setRowTo(selectedPos > 0 ? selectedPos : 0);
                logMonitoringDetails();
            });

            // Cập nhật Spinner ColumnFrom
            setSpinnerAdapter(spinnerColumnFrom, columnList, selectedPos -> {
                item.setColumnFrom(selectedPos > 0 ? selectedPos : 0);
                logMonitoringDetails();
            });

            // Cập nhật Spinner ColumnTo
            setSpinnerAdapter(spinnerColumnTo, columnList, selectedPos -> {
                item.setColumnTo(selectedPos > 0 ? selectedPos : 0);
                logMonitoringDetails();
            });

            // Tự động chọn hàng/cột dựa vào dữ liệu có sẵn
            spinnerRowFrom.setSelection(item.getRowFrom());
            spinnerRowTo.setSelection(item.getRowTo());
            spinnerColumnFrom.setSelection(item.getColumnFrom());
            spinnerColumnTo.setSelection(item.getColumnTo());
        }
    }
    private void setSpinnerAdapter(Spinner spinner, List<String> data, OnItemSelectedCallback callback) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                callback.onItemSelected(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
    private void logMonitoringDetails() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(monitoringDetails);
        Log.d("MeasurementLocationAdapter", "Monitoring Details: " + json);
    }
    public interface OnMonitoringDetailsChangeListener {
        void onMonitoringDetailsChanged(List<IdentificationSensorRequest.MonitoringDetail> updatedList);
    }
}

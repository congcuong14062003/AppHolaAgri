package com.example.appholaagri.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appholaagri.R;
import com.example.appholaagri.adapter.ListPlantCareHistoryAdapter;
import com.example.appholaagri.model.PlantDetailModel.PlantDetailResponse;
import com.example.appholaagri.viewmodel.PlantViewModel;

import java.util.List;

public class PlantCareHistoryFragment extends BaseFragment {
    private PlantViewModel plantViewModel;
    private ListPlantCareHistoryAdapter adapter;
    private RecyclerView recyclerView;
    private View emptyStateLayout, progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout
        View view = inflater.inflate(R.layout.fragment_plant_care_history, container, false);

        // Ánh xạ View
        recyclerView = view.findViewById(R.id.recyclerTakeCareOfPlant);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        emptyStateLayout = view.findViewById(R.id.emptyStateLayout);
        progressBar = view.findViewById(R.id.progressBar);

        // Khởi tạo ViewModel
        plantViewModel = new ViewModelProvider(requireActivity()).get(PlantViewModel.class);

        // Quan sát dữ liệu từ ViewModel
        plantViewModel.getPlantData().observe(getViewLifecycleOwner(), plantDetailResponse -> {
            if (plantDetailResponse != null) {
                List<PlantDetailResponse.CropCareWork> careList = plantDetailResponse.getAomPlantCropCareWork();  // Sửa đúng tên hàm
                if (careList != null && !careList.isEmpty()) {
                    adapter = new ListPlantCareHistoryAdapter(careList);
                    recyclerView.setAdapter(adapter);

                    // Hiển thị danh sách & ẩn trạng thái rỗng
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyStateLayout.setVisibility(View.GONE);
                } else {
                    // Hiển thị trạng thái rỗng nếu không có dữ liệu
                    recyclerView.setVisibility(View.GONE);
                    emptyStateLayout.setVisibility(View.VISIBLE);
                }
            }
            // Ẩn progressBar sau khi có dữ liệu
            progressBar.setVisibility(View.GONE);
        });

        return view;
    }
}

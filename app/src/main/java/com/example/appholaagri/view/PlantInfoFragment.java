package com.example.appholaagri.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.appholaagri.R;
import com.example.appholaagri.model.PlantDetailModel.PlantDetailResponse;
import com.example.appholaagri.viewmodel.PlantViewModel;

public class PlantInfoFragment extends BaseFragment {
    private TextView plandCode, planType, plantation, plantingPeriod, plantStage, plantArea, plantDaysOld, plantStatus;
    private PlantViewModel plantViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plant_info, container, false);

        plandCode = view.findViewById(R.id.pland_code);
        planType = view.findViewById(R.id.plan_type);
        plantation = view.findViewById(R.id.plantation);
        plantingPeriod = view.findViewById(R.id.planting_period);
        plantStage = view.findViewById(R.id.plant_stage);
        plantArea = view.findViewById(R.id.plant_area);
        plantDaysOld = view.findViewById(R.id.plant_days_old);
        plantStatus = view.findViewById(R.id.plant_status);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Lấy ViewModel từ Activity
        plantViewModel = new ViewModelProvider(requireActivity()).get(PlantViewModel.class);

        // Lắng nghe dữ liệu từ ViewModel
        plantViewModel.getPlantData().observe(getViewLifecycleOwner(), new Observer<PlantDetailResponse>() {
            @Override
            public void onChanged(PlantDetailResponse plantDetailResponse) {
                updateUserUI(plantDetailResponse);
            }
        });
    }

    private void updateUserUI(PlantDetailResponse plantDetailResponse) {
        if (plantDetailResponse != null) {
            plandCode.setText(plantDetailResponse.getCode());
            planType.setText(plantDetailResponse.getNameCropVarieties());
            plantation.setText(plantDetailResponse.getNamePlantation());
            plantingPeriod.setText(plantDetailResponse.getNameCultivationPeriod());
            plantStage.setText(plantDetailResponse.getNameDevelopmentStage());
            plantArea.setText(plantDetailResponse.getCultivationAreaFrom());
            plantDaysOld.setText(plantDetailResponse.getDayDifference() + " ngày");
            plantStatus.setText(plantDetailResponse.getStatusString());
        }
    }
}

package com.example.appholaagri.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.example.appholaagri.R;
import com.example.appholaagri.model.ApiResponse.ApiResponse;
import com.example.appholaagri.model.PlanAppInitFormModel.PlanAppInitFormResponse;
import com.example.appholaagri.model.SensorAppInitFormModel.SensorAppInitFormResponse;
import com.example.appholaagri.service.ApiClient;
import com.example.appholaagri.service.ApiInterface;
import com.example.appholaagri.utils.CustomToast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeclarationIdentifierSensorFragment extends Fragment {
    private int selectedPlantationId = -1;
    private int selectedAssetId = -1;
    private String token;

    private Spinner spinnerPlantation, spinnerAsset;
    private ImageView rbTypeSensorPermanent, rbTypeSensorMobile;
    // đồn điền
    private List<SensorAppInitFormResponse.Plantation> plantationList = new ArrayList<>();
    private final List<String> plantationNames = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    // tài sản
    private List<SensorAppInitFormResponse.Asset> assetList = new ArrayList<>();
    private final List<String> assetListName = new ArrayList<>();
    private ArrayAdapter<String> adapterAsset;

    private ApiInterface apiInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_declaration_identifier_sensor, container, false);
        initViews(view);
        initApi();
        loadToken();
        getInitForSensor();
        return view;
    }

    private void initViews(View view) {
        spinnerPlantation = view.findViewById(R.id.spinnerSensorPlantation);
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, plantationNames);
        spinnerPlantation.setAdapter(adapter);
        spinnerPlantation.setOnItemSelectedListener(spinnerListener);


        spinnerAsset = view.findViewById(R.id.spinnerSensorAsset);
        adapterAsset = new ArrayAdapter<>(requireContext(),  android.R.layout.simple_spinner_dropdown_item, assetListName);
        spinnerAsset.setAdapter(adapterAsset);
        spinnerAsset.setOnItemSelectedListener(spinnerAssetListener);



    }

    private void initApi() {
        apiInterface = ApiClient.getClient(requireContext()).create(ApiInterface.class);
    }

    private void loadToken() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("AppPreferences", requireActivity().MODE_PRIVATE);
        token = sharedPreferences.getString("auth_token", "");
    }

    private final AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            selectedPlantationId = (position > 0) ? plantationList.get(position - 1).getId() : -1;
            Log.d("SpinnerSelection", "Selected Plantation ID: " + selectedPlantationId);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    };

    private final AdapterView.OnItemSelectedListener spinnerAssetListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position > 0) { // Bỏ qua item mặc định
                SensorAppInitFormResponse.Asset selectedAsset = assetList.get(position - 1);
                selectedAssetId = selectedAsset.getId();
                int isFixed = selectedAsset.getIsFixed(); // Giả sử có phương thức isFixed()
                Log.d("SpinnerSelection", "Selected Asset ID: " + selectedAssetId + ", isFixed: " + isFixed);
                if(isFixed == 1) {
                    handleScannerQr();
                }
            } else {
                selectedAssetId = -1; // Reset ID nếu chọn item mặc định
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    };


    private void getInitForSensor() {
        apiInterface.sensorInitForm(token, "").enqueue(new Callback<ApiResponse<SensorAppInitFormResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<SensorAppInitFormResponse>> call, Response<ApiResponse<SensorAppInitFormResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getStatus() == 200) {
                    ApiResponse<SensorAppInitFormResponse> apiResponse = response.body();
                    plantationList = apiResponse.getData().getPlantationList();
                    assetList = apiResponse.getData().getAssetList();
                    updateSpinnerData();
                    updateSpinnerAssetData();
                } else {
                    CustomToast.showCustomToast(getContext(), response.body() != null ? response.body().getMessage() : "Lỗi dữ liệu");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<SensorAppInitFormResponse>> call, Throwable t) {
                Log.e("API Error", t.getMessage());
            }
        });
    }

    private void updateSpinnerData() {
        plantationNames.clear();
        plantationNames.add("--Chọn đồn điền--");

        for (SensorAppInitFormResponse.Plantation plantation : plantationList) {
            plantationNames.add(plantation.getName());
        }
        adapter.notifyDataSetChanged();
    }

    private void updateSpinnerAssetData() {
        assetListName.clear();
        assetListName.add("--Chọn tài sản--");

        for (SensorAppInitFormResponse.Asset asset : assetList) {
            assetListName.add(asset.getName());
        }
        adapterAsset.notifyDataSetChanged();
    }

    private void handleScannerQr() {

    }
}

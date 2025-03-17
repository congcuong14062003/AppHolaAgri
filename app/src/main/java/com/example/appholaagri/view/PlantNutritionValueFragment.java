package com.example.appholaagri.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.appholaagri.R;
import com.example.appholaagri.model.ApiResponse.ApiResponse;
import com.example.appholaagri.model.FluctuationSoilModel.FluctuationSoilRequest;
import com.example.appholaagri.model.FluctuationSoilModel.FluctuationSoilResponse;
import com.example.appholaagri.service.ApiClient;
import com.example.appholaagri.service.ApiInterface;
import com.example.appholaagri.utils.CustomToast;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PlantNutritionValueFragment extends BaseFragment {
    private CombinedChart mChart, mChartDark;
    FlexboxLayout buttonContainer, buttonContainerDark;
    private Map<String, List<Entry>> dataEntriesMap;
    private Map<String, List<Entry>> dataEntriesDarkMap;
    private Map<String, String> colorMap;
    private List<String> xLabels;
    private boolean showAll = true;
    private int plantId;
    private View emptyStateLayout, progressBar;
    private String selectedItem = null;

    Map<Integer, String> dateMap = new HashMap<>(); // 🔹 Dùng int làm key để lưu byGroup -> date
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_plant_nutrition_value, container, false);
        mChart = view.findViewById(R.id.combinedChart);
        buttonContainer = view.findViewById(R.id.buttonContainer);
        mChartDark = view.findViewById(R.id.combinedChartDark);
        buttonContainerDark = view.findViewById(R.id.buttonContainerDark);
        emptyStateLayout = view.findViewById(R.id.emptyStateLayout);
        progressBar = view.findViewById(R.id.progressBar);
        if (getArguments() != null) {
            plantId = getArguments().getInt("plantId", -1);
            callChartData(plantId);
        }

        setupChart();
        setupChartDark();
        return view;
    }
    // tầng nông
    private void setupChart() {
        mChart.getDescription().setEnabled(false);
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);
        mChart.setHighlightFullBarEnabled(false);
        mChart.getLegend().setEnabled(false);

        // 🔹 Cho phép kéo và zoom ngang
        mChart.setDragEnabled(true);
        mChart.setScaleXEnabled(true);
        mChart.setScaleYEnabled(false); // Không cho phép zoom theo trục Y
        mChart.setPinchZoom(true); // Hỗ trợ zoom đa điểm

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setLabelRotationAngle(0);
        xAxis.setTextSize(2f);
        xAxis.setCenterAxisLabels(false);
        xAxis.enableGridDashedLine(10f, 5f, 0f);

        mChart.getAxisLeft().enableGridDashedLine(10f, 5f, 0f);
        mChart.getAxisRight().setEnabled(false);

        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                int index = (int) e.getX(); // byGroup bắt đầu từ 1

                // 🔹 Kiểm tra index hợp lệ trước khi truy cập danh sách
                if (index < 1 || index > xLabels.size()) {
                    Log.e("Chart", "Index out of bounds: " + index + ", max size: " + xLabels.size());
                    return;
                }

                String date = dateMap.get(index);
                if (date == null) {
                    Log.e("Chart", "Date not found for index: " + index);
                    return;
                }


                // Lấy danh sách chỉ số đang hiển thị trên biểu đồ
                List<String> activeIndicators = new ArrayList<>();
                for (ILineDataSet dataSet : mChart.getLineData().getDataSets()) {
                    activeIndicators.add(dataSet.getLabel());
                }

                // Lọc dữ liệu theo chỉ số đang hiển thị
                Map<String, Float> valuesForDate = new HashMap<>();
                for (Map.Entry<String, List<Entry>> entry : dataEntriesMap.entrySet()) {
                    String name = entry.getKey();
                    if (!activeIndicators.contains(name)) continue;  // Bỏ qua chỉ số không hiển thị

                    for (Entry entryData : entry.getValue()) {
                        if ((int) entryData.getX() == index) { // 🔹 Không cần trừ 1 ở đây vì dữ liệu lấy từ API
                            valuesForDate.put(name, entryData.getY());
                            break;
                        }
                    }
                }

                showDataDialog(date, valuesForDate);
            }
            @Override
            public void onNothingSelected() {
                // Không làm gì khi không có điểm nào được chọn
            }
        });

    }
    // tầng sâu
    private void setupChartDark() {
        mChartDark.getDescription().setEnabled(false);
        mChartDark.setBackgroundColor(Color.WHITE);
        mChartDark.setDrawGridBackground(false);
        mChartDark.setDrawBarShadow(false);
        mChartDark.setHighlightFullBarEnabled(false);
        mChartDark.getLegend().setEnabled(false);

        // 🔹 Cho phép kéo và zoom ngang
        mChartDark.setDragEnabled(true);
        mChartDark.setScaleXEnabled(true);
        mChartDark.setScaleYEnabled(false); // Không cho phép zoom theo trục Y
        mChartDark.setPinchZoom(true); // Hỗ trợ zoom đa điểm

        XAxis xAxis = mChartDark.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setLabelRotationAngle(0);
        xAxis.setTextSize(2f);
        xAxis.setCenterAxisLabels(false);
        xAxis.enableGridDashedLine(10f, 5f, 0f);

        mChartDark.getAxisLeft().enableGridDashedLine(10f, 5f, 0f);
        mChartDark.getAxisRight().setEnabled(false);

        mChartDark.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                int index = (int) e.getX(); // byGroup bắt đầu từ 1

                // 🔹 Kiểm tra index hợp lệ trước khi truy cập danh sách
                if (index < 1 || index > xLabels.size()) {
                    Log.e("Chart", "Index out of bounds: " + index + ", max size: " + xLabels.size());
                    return;
                }

                String date = dateMap.get(index);
                if (date == null) {
                    Log.e("Chart", "Date not found for index: " + index);
                    return;
                }


                // Lấy danh sách chỉ số đang hiển thị trên biểu đồ
                List<String> activeIndicators = new ArrayList<>();
                for (ILineDataSet dataSet : mChartDark.getLineData().getDataSets()) {
                    activeIndicators.add(dataSet.getLabel());
                }

                // Lọc dữ liệu theo chỉ số đang hiển thị
                Map<String, Float> valuesForDate = new HashMap<>();
                for (Map.Entry<String, List<Entry>> entry : dataEntriesDarkMap.entrySet()) {
                    String name = entry.getKey();
                    if (!activeIndicators.contains(name)) continue;  // Bỏ qua chỉ số không hiển thị

                    for (Entry entryData : entry.getValue()) {
                        if ((int) entryData.getX() == index) { // 🔹 Không cần trừ 1 ở đây vì dữ liệu lấy từ API
                            valuesForDate.put(name, entryData.getY());
                            break;
                        }
                    }
                }

                showDataDialog(date, valuesForDate);
            }
            @Override
            public void onNothingSelected() {
                // Không làm gì khi không có điểm nào được chọn
            }
        });

    }

    private void showDataDialog(String date, Map<String, Float> valuesForDate) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Thông tin ngày: " + date);

        // Tạo layout chứa danh sách các chỉ số
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 20, 40, 20);

        for (Map.Entry<String, Float> entry : valuesForDate.entrySet()) {
            String name = entry.getKey();
            float value = entry.getValue();
            String colorHex = colorMap.get(name);

            // Tạo TextView hiển thị chỉ số
            TextView textView = new TextView(getContext());
            textView.setText(name + ": " + value);
            textView.setTextSize(16);
            textView.setTextColor(Color.BLACK);
            textView.setPadding(10, 10, 10, 10);

            // Thêm biểu tượng màu trước chỉ số
            SpannableString spannable = new SpannableString("⬤  " + textView.getText());
            spannable.setSpan(new ForegroundColorSpan(Color.parseColor(colorHex)), 0, 1, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(spannable);

            layout.addView(textView);
        }

        builder.setView(layout);
        builder.setPositiveButton("OK", null);
        builder.show();
    }

    private void callChartData(int plantId) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);

        ApiInterface apiInterface = ApiClient.getClient(getContext()).create(ApiInterface.class);
        FluctuationSoilRequest request = new FluctuationSoilRequest(plantId, 200);
        // Hiển thị progressBar khi bắt đầu tải dữ liệu
        progressBar.setVisibility(View.VISIBLE);
        emptyStateLayout.setVisibility(View.GONE);
        mChart.setVisibility(View.GONE);
        mChartDark.setVisibility(View.GONE);

        Call<ApiResponse<FluctuationSoilResponse>> call = apiInterface.fluctuationSoil(token, request);
        call.enqueue(new Callback<ApiResponse<FluctuationSoilResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<FluctuationSoilResponse>> call, Response<ApiResponse<FluctuationSoilResponse>> response) {
                // Ẩn progressBar sau khi nhận được phản hồi từ API
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    List<FluctuationSoilResponse.FluctuationValue> fluctuationValues = response.body().getData().getFluctuationValue();

                    if (fluctuationValues != null && !fluctuationValues.isEmpty()) {
                        emptyStateLayout.setVisibility(View.GONE);
                        mChart.setVisibility(View.VISIBLE);
                        mChartDark.setVisibility(View.VISIBLE);
                        processChartData(fluctuationValues);
                        processChartDarkData(fluctuationValues);
                    } else {
                        emptyStateLayout.setVisibility(View.VISIBLE);
                        mChart.setVisibility(View.GONE);
                        mChartDark.setVisibility(View.GONE);
                    }
                } else {
                    emptyStateLayout.setVisibility(View.VISIBLE);
                    mChartDark.setVisibility(View.GONE);
                    mChart.setVisibility(View.GONE);
                    CustomToast.showCustomToast(getContext(), "Không thể lấy dữ liệu!");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<FluctuationSoilResponse>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                emptyStateLayout.setVisibility(View.VISIBLE);
                mChart.setVisibility(View.GONE);
                mChartDark.setVisibility(View.GONE);
                CustomToast.showCustomToast(getContext(), "Lỗi kết nối API!");
            }
        });
    }

    private void processChartData(List<FluctuationSoilResponse.FluctuationValue> fluctuationValues) {
        if (fluctuationValues == null || fluctuationValues.isEmpty()) {
            CustomToast.showCustomToast(getContext(), "Không có dữ liệu để hiển thị!");
            return;
        }

        xLabels = new ArrayList<>();
        dataEntriesMap = new HashMap<>();
        colorMap = new HashMap<>();
        for (FluctuationSoilResponse.FluctuationValue value : fluctuationValues) {
            String date = value.getDate();
            int byGroup = value.getByGroup(); // 🔹 Lấy byGroup từ API (bắt đầu từ 1)
            dateMap.put(byGroup, date); // 🔹 Lưu byGroup -> date
            xLabels.add(String.valueOf(byGroup));

            for (FluctuationSoilResponse.DataIndex data : value.getDataIndex30cm()) {
                if (data.getRealQuantity() == null) continue;

                String nameVi = data.getNameVi();
                float realQuantity = data.getRealQuantity();
                String colorHex = data.getColor();

                if (!dataEntriesMap.containsKey(nameVi)) {
                    dataEntriesMap.put(nameVi, new ArrayList<>());
                    colorMap.put(nameVi, colorHex);
                }

                // 🔹 Dùng byGroup làm X
                dataEntriesMap.get(nameVi).add(new Entry((float) byGroup, realQuantity));
            }
        }

        // 🔹 Định dạng trục X để hiển thị ngày theo byGroup
        XAxis xAxis = mChart.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return dateMap.getOrDefault((int) value, ""); // 🔹 Hiển thị ngày từ byGroup
            }
        });

        xAxis.setLabelCount(xLabels.size(), false);
        xAxis.setGranularity(1f);
        xAxis.setAvoidFirstLastClipping(true);

        // 🔹 Đảm bảo trục X hiển thị tốt
        mChart.setVisibleXRangeMaximum(3f);
        mChart.setVisibleXRangeMinimum(3f);
        mChart.moveViewToX(fluctuationValues.size() > 3 ? fluctuationValues.size() - 2 : 0);

        renderChart(null);
        renderButtons();
    }





    private void renderChart(String selectedItem) {
        LineData lineData = new LineData();

        for (Map.Entry<String, List<Entry>> entry : dataEntriesMap.entrySet()) {
            String name = entry.getKey();
            List<Entry> entries = entry.getValue();
            String colorHex = colorMap.get(name);
            int lineColor = Color.parseColor(colorHex);

            if (selectedItem == null || selectedItem.equals(name)) {
                LineDataSet dataSet = new LineDataSet(entries, name);
                dataSet.setColor(lineColor);
                dataSet.setCircleColor(lineColor);
                dataSet.setCircleRadius(4f);
                dataSet.setDrawValues(true);


                lineData.addDataSet(dataSet);
            }
        }

        CombinedData data = new CombinedData();
        data.setData(lineData);
        mChart.setData(data);
        mChart.invalidate();
        if (xLabels != null && xLabels.size() > 3) {
            mChart.setVisibleXRangeMaximum(3f); // Chỉ hiển thị 3 ngày trên màn hình

            // 🔹 Di chuyển đến 3 điểm cuối cùng
            float lastIndex = xLabels.size() - 1;
            mChart.moveViewToX(lastIndex - 2); // Dịch chuyển đến điểm thứ (last - 2) để hiển thị 3 điểm cuối
        }
    }

    private void renderButtons() {
        buttonContainer.removeAllViews();

        if (dataEntriesMap == null || colorMap == null) return;

        // 🌟 Tạo nút "Tất cả"
        addCustomButton("Tất cả", "#000000", R.drawable.icon_line_chart, null);

        // 🌟 Tạo các nút theo dữ liệu API
        for (Map.Entry<String, String> entry : colorMap.entrySet()) {
            String nameVi = entry.getKey();  // Tên chỉ số
            String colorHex = entry.getValue(); // Màu từ API

            // Loại bỏ đơn vị khỏi tên chỉ số
            String cleanName = nameVi.replaceAll("\\s*\\(.*?\\)", "");

            addCustomButton(cleanName, colorHex, R.drawable.icon_line_chart, nameVi);
        }
    }


    /**
     * 📌 Hàm tạo nút layout có icon trên, text dưới.
     */
    private void addCustomButton(String text, String colorHex, int iconRes, String itemName) {
        LinearLayout buttonLayout = new LinearLayout(getContext());
        buttonLayout.setOrientation(LinearLayout.VERTICAL);
        buttonLayout.setGravity(Gravity.CENTER);
        buttonLayout.setPadding(10, 10, 10, 10);

        // Nếu nút "Tất cả" đang được chọn, giữ nguyên màu API cho tất cả
        boolean isSelected = (selectedItem == null && itemName == null) || (selectedItem != null && selectedItem.equals(itemName));
        int secondaryColor = ContextCompat.getColor(getContext(), R.color.secon_textcolor);
        String displayColor = (selectedItem == null) ? colorHex : (isSelected ? colorHex : String.format("#%06X", (0xFFFFFF & secondaryColor)));


        ImageView icon = new ImageView(getContext());
        icon.setImageResource(iconRes);
        icon.setColorFilter(Color.parseColor(displayColor));
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(80, 80);
        icon.setLayoutParams(iconParams);

        TextView textView = new TextView(getContext());
        textView.setText(text);
        textView.setTextColor(Color.parseColor(displayColor));
        textView.setGravity(Gravity.CENTER);

        buttonLayout.addView(icon);
        buttonLayout.addView(textView);

        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int buttonWidth = screenWidth / 5;

        FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(
                buttonWidth,
                FlexboxLayout.LayoutParams.WRAP_CONTENT);
        buttonLayout.setLayoutParams(params);

        // Gán sự kiện click
        buttonLayout.setOnClickListener(v -> {
            selectedItem = itemName; // Nếu chọn "Tất cả", itemName sẽ là null
            renderChart(selectedItem);
            renderButtons(); // Cập nhật lại màu sắc các nút
        });

        buttonContainer.addView(buttonLayout);
    }





    private void renderDarkChart(String selectedItem) {
        LineData lineData = new LineData();

        for (Map.Entry<String, List<Entry>> entry : dataEntriesDarkMap.entrySet()) {
            String name = entry.getKey();
            List<Entry> entries = entry.getValue();
            String colorHex = colorMap.get(name);
            int lineColor = Color.parseColor(colorHex);

            if (selectedItem == null || selectedItem.equals(name)) {
                LineDataSet dataSet = new LineDataSet(entries, name);
                dataSet.setColor(lineColor);
                dataSet.setCircleColor(lineColor);
                dataSet.setCircleRadius(4f);
                dataSet.setDrawValues(true);


                lineData.addDataSet(dataSet);
            }
        }

        CombinedData data = new CombinedData();
        data.setData(lineData);
        mChartDark.setData(data);
        mChartDark.invalidate();
        if (xLabels != null && xLabels.size() > 3) {
            mChartDark.setVisibleXRangeMaximum(3f); // Chỉ hiển thị 3 ngày trên màn hình

            // 🔹 Di chuyển đến 3 điểm cuối cùng
            float lastIndex = xLabels.size() - 1;
            mChartDark.moveViewToX(lastIndex - 2); // Dịch chuyển đến điểm thứ (last - 2) để hiển thị 3 điểm cuối
        }
    }
    private void processChartDarkData(List<FluctuationSoilResponse.FluctuationValue> fluctuationValues) {
        if (fluctuationValues == null || fluctuationValues.isEmpty()) {
            CustomToast.showCustomToast(getContext(), "Không có dữ liệu để hiển thị!");
            return;
        }

        xLabels = new ArrayList<>();
        dataEntriesDarkMap = new HashMap<>();
        colorMap = new HashMap<>();
        for (FluctuationSoilResponse.FluctuationValue value : fluctuationValues) {
            String date = value.getDate();
            int byGroup = value.getByGroup(); // 🔹 Lấy byGroup từ API (bắt đầu từ 1)
            dateMap.put(byGroup, date); // 🔹 Lưu byGroup -> date
            xLabels.add(String.valueOf(byGroup));

            for (FluctuationSoilResponse.DataIndex data : value.getDataIndex50cm()) {
                if (data.getRealQuantity() == null) continue;

                String nameVi = data.getNameVi();
                float realQuantity = data.getRealQuantity();
                String colorHex = data.getColor();

                if (!dataEntriesDarkMap.containsKey(nameVi)) {
                    dataEntriesDarkMap.put(nameVi, new ArrayList<>());
                    colorMap.put(nameVi, colorHex);
                }

                // 🔹 Dùng byGroup làm X
                dataEntriesDarkMap.get(nameVi).add(new Entry((float) byGroup, realQuantity));
            }
        }

        // 🔹 Định dạng trục X để hiển thị ngày theo byGroup
        XAxis xAxis = mChartDark.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return dateMap.getOrDefault((int) value, ""); // 🔹 Hiển thị ngày từ byGroup
            }
        });

        xAxis.setLabelCount(xLabels.size(), false);
        xAxis.setGranularity(1f);
        xAxis.setAvoidFirstLastClipping(true);

        // 🔹 Đảm bảo trục X hiển thị tốt
        mChartDark.setVisibleXRangeMaximum(3f);
        mChartDark.setVisibleXRangeMinimum(3f);
        mChartDark.moveViewToX(fluctuationValues.size() > 3 ? fluctuationValues.size() - 2 : 0);

        renderDarkChart(null);
        renderDarkButtons();
}
    private void renderDarkButtons() {
    buttonContainerDark.removeAllViews();

    if (dataEntriesDarkMap == null || colorMap == null) return;

    // 🌟 Tạo nút "Tất cả"
    addCustomDarkButton("Tất cả", "#000000", R.drawable.icon_line_chart, null);

    // 🌟 Tạo các nút theo dữ liệu API
    for (Map.Entry<String, String> entry : colorMap.entrySet()) {
        String nameVi = entry.getKey();  // Tên chỉ số
        String colorHex = entry.getValue(); // Màu từ API

        // Loại bỏ đơn vị khỏi tên chỉ số
        String cleanName = nameVi.replaceAll("\\s*\\(.*?\\)", "");

        addCustomDarkButton(cleanName, colorHex, R.drawable.icon_line_chart, nameVi);
    }
}

    private void addCustomDarkButton(String text, String colorHex, int iconRes, String itemName) {
        LinearLayout buttonLayout = new LinearLayout(getContext());
        buttonLayout.setOrientation(LinearLayout.VERTICAL);
        buttonLayout.setGravity(Gravity.CENTER);
        buttonLayout.setPadding(10, 10, 10, 10);

        // Nếu nút "Tất cả" đang được chọn, giữ nguyên màu API cho tất cả
        boolean isSelected = (selectedItem == null && itemName == null) || (selectedItem != null && selectedItem.equals(itemName));
        int secondaryColor = ContextCompat.getColor(getContext(), R.color.secon_textcolor);
        String displayColor = (selectedItem == null) ? colorHex : (isSelected ? colorHex : String.format("#%06X", (0xFFFFFF & secondaryColor)));


        ImageView icon = new ImageView(getContext());
        icon.setImageResource(iconRes);
        icon.setColorFilter(Color.parseColor(displayColor));
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(80, 80);
        icon.setLayoutParams(iconParams);

        TextView textView = new TextView(getContext());
        textView.setText(text);
        textView.setTextColor(Color.parseColor(displayColor));
        textView.setGravity(Gravity.CENTER);

        buttonLayout.addView(icon);
        buttonLayout.addView(textView);

        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int buttonWidth = screenWidth / 5;

        FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(
                buttonWidth,
                FlexboxLayout.LayoutParams.WRAP_CONTENT);
        buttonLayout.setLayoutParams(params);

        // Gán sự kiện click
        buttonLayout.setOnClickListener(v -> {
            selectedItem = itemName; // Nếu chọn "Tất cả", itemName sẽ là null
            renderDarkChart(selectedItem);
            renderDarkButtons();
        });

        buttonContainerDark.addView(buttonLayout);
    }
}
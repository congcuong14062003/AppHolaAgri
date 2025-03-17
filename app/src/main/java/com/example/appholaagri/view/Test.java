package com.example.appholaagri.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.service.autofill.Dataset;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appholaagri.R;
import com.example.appholaagri.model.ApiResponse.ApiResponse;
import com.example.appholaagri.model.FluctuationSoilModel.FluctuationSoilRequest;
import com.example.appholaagri.model.FluctuationSoilModel.FluctuationSoilResponse;
import com.example.appholaagri.service.ApiClient;
import com.example.appholaagri.service.ApiInterface;
import com.example.appholaagri.utils.CustomToast;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
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

public class Test extends AppCompatActivity {
    private CombinedChart mChart;
    FlexboxLayout buttonContainer;
    private Map<String, List<Entry>> dataEntriesMap;
    private Map<String, String> colorMap;
    private List<String> xLabels;
    private boolean showAll = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_test);

        mChart = findViewById(R.id.combinedChart);
        setupChart();
        fetchChartData(); // Gọi API lấy dữ liệu từ server
    }


    private void setupChart() {
        mChart.getDescription().setEnabled(false);
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);
        mChart.setHighlightFullBarEnabled(false);
//        mChart.setOnChartValueSelectedListener(this);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0f);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setLabelRotationAngle(-30);
    }
    private void fetchChartData() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);

        ApiInterface apiInterface = ApiClient.getClient(this).create(ApiInterface.class);
        FluctuationSoilRequest request = new FluctuationSoilRequest();

        Call<ApiResponse<FluctuationSoilResponse>> call = apiInterface.fluctuationSoil(token, request);
        call.enqueue(new Callback<ApiResponse<FluctuationSoilResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<FluctuationSoilResponse>> call, Response<ApiResponse<FluctuationSoilResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    processChartData(response.body().getData().getFluctuationValue());
                } else {
                    Toast.makeText(Test.this, "Không thể lấy dữ liệu!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<FluctuationSoilResponse>> call, Throwable t) {
                Toast.makeText(Test.this, "Lỗi kết nối API!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void processChartData(List<FluctuationSoilResponse.FluctuationValue> fluctuationValues) {
        if (fluctuationValues == null || fluctuationValues.isEmpty()) {
            Toast.makeText(Test.this, "Không có dữ liệu để hiển thị!", Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> xLabels = new ArrayList<>();
        List<Entry> entries = new ArrayList<>();

        for (int i = 0; i < fluctuationValues.size(); i++) {
            FluctuationSoilResponse.FluctuationValue value = fluctuationValues.get(i);
            xLabels.add(value.getDate()); // Lấy ngày làm trục X

            float totalValue = 0f;
            int count = 0;

            for (FluctuationSoilResponse.DataIndex data : value.getDataIndex30cm()) {
                if (data.getRealQuantity() != null) {
                    totalValue += data.getRealQuantity();
                    count++;
                }
            }

            if (count > 0) {
                entries.add(new Entry(i, totalValue / count)); // Lấy giá trị trung bình nếu có nhiều dữ liệu
            }
        }

        // Cập nhật trục X
        XAxis xAxis = mChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xLabels));
        xAxis.setLabelCount(Math.min(10, xLabels.size()), true);

        // Tạo dataset
        LineDataSet dataSet = new LineDataSet(entries, "Biến động dinh dưỡng đất");
        dataSet.setColor(Color.GREEN);
        dataSet.setCircleColor(Color.GREEN);
        dataSet.setCircleRadius(4f);
        dataSet.setLineWidth(2.5f);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setDrawValues(false);
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

        LineData lineData = new LineData(dataSet);
        CombinedData data = new CombinedData();
        data.setData(lineData);

        mChart.setData(data);
        mChart.invalidate();

        // 🌟 Zoom vào 5 ngày cuối cùng 🌟
        if (xLabels.size() > 5) {
            int totalDays = xLabels.size();
            int startIndex = Math.max(0, totalDays - 5);

            mChart.setVisibleXRangeMaximum(5f);
            mChart.moveViewToX(startIndex);
        }
    }


}
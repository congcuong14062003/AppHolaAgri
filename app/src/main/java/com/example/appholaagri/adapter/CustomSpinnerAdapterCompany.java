package com.example.appholaagri.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.appholaagri.R;
import com.example.appholaagri.model.RequestDetailModel.RequestDetailData;

import java.util.List;

public class CustomSpinnerAdapterCompany extends ArrayAdapter<RequestDetailData.CompanyList> {
    private final List<RequestDetailData.CompanyList> companyList;
    private int selectedPosition = -1;

    public CustomSpinnerAdapterCompany(@NonNull Context context, @NonNull List<RequestDetailData.CompanyList> companyList) {
        super(context, R.layout.spinner_item_company, companyList);
        this.companyList = companyList;
    }

    public void setSelectedPosition(int position) {
        this.selectedPosition = position;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createView(position, convertView, parent, R.layout.spinner_item_company);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createView(position, convertView, parent, R.layout.spinner_dropdown_item_company);
    }

    private View createView(int position, @Nullable View convertView, @NonNull ViewGroup parent, int layoutId) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(layoutId, parent, false);
        }

        TextView textView = view.findViewById(layoutId == R.layout.spinner_item_company
                ? R.id.spinner_item_text
                : R.id.spinner_dropdown_item_text);

        RequestDetailData.CompanyList company = companyList.get(position);
        if (company != null && company.getName() != null) {
            textView.setText(company.getName());
        }

        // Tùy chỉnh màu cho mục được chọn
        if (position == selectedPosition) {
//            textView.setBackgroundColor(getContext().getResources().getColor(android.R.color.holo_blue_light));
//            textView.setTextColor(getContext().getResources().getColor(R.color.secondarycolor));
        } else {
            textView.setBackgroundColor(getContext().getResources().getColor(android.R.color.transparent));
            textView.setTextColor(getContext().getResources().getColor(android.R.color.black));
        }

        return view;
    }
}
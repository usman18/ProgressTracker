package com.uk.progresstracker.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.uk.progresstracker.Model.Report;
import com.uk.progresstracker.R;
import com.uk.progresstracker.Utils;

import java.util.Calendar;

import io.realm.Realm;
import io.realm.Sort;

/**
 * Created by usman on 14-09-2018.
 */

public class HomeFragment extends Fragment {


    private Realm realm;

    private Calendar calendar;

    private TextView tvMonth;
    private TextView tvYear;

    private TextView tvRank;
    private TextView tvRankName;
    private TextView tvSuccess;
    private TextView tvSuccessName;
    private TextView tvCollection;
    private TextView tvCollectionName;
    private TextView tvWtLoss;
    private TextView tvWtLossName;
    private TextView tvAvgWtLoss;
    private TextView tvAvgWtLossName;

    private String currentMonth;
    private String currentYear;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return LayoutInflater.from(getContext())
                .inflate(R.layout.home_layout,container,false);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initialize(view);
        setUI();

    }

    private void initialize(View view) {

        realm = Realm.getDefaultInstance();

        calendar = Calendar.getInstance();

        int monthIndex = calendar.get(Calendar.MONTH);
        currentMonth = Utils.months[monthIndex];

        currentYear = String.valueOf(calendar.get(Calendar.YEAR));

        tvMonth = view.findViewById(R.id.month);
        tvYear = view.findViewById(R.id.year);

        tvMonth.setText(currentMonth);
        tvYear.setText(currentYear);


        tvRank = view.findViewById(R.id.tvRank);
        tvRankName = view.findViewById(R.id.tvRankName);
        tvSuccess = view.findViewById(R.id.tvSuccess);
        tvSuccessName = view.findViewById(R.id.tvSuccessName);
        tvCollection = view.findViewById(R.id.tvCollection);
        tvCollectionName = view.findViewById(R.id.tvCollectionName);
        tvWtLoss = view.findViewById(R.id.tvWeighLoss);
        tvWtLossName = view.findViewById(R.id.tvWeighLossName);
        tvAvgWtLoss = view.findViewById(R.id.tvAvgWeightLosss);
        tvAvgWtLossName = view.findViewById(R.id.tvAvgWeightLosssName);

        view.findViewById(R.id.ll)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDatePicker();
                    }
                });


    }

    private void showDatePicker() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.date_picker_dialog,null);

        builder.setView(view);

        final DatePicker datePicker = view.findViewById(R.id.datePicker);

        TextView tvCancel = view.findViewById(R.id.tvCancel);
        TextView tvOk = view.findViewById(R.id.tvOk);

        final AlertDialog dialog = builder.create();
        dialog.show();

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int monthIndex = datePicker.getMonth();
                int year = datePicker.getYear();


                currentMonth = Utils.months[monthIndex];
                currentYear = String.valueOf(year);

                tvMonth.setText(currentMonth);
                tvYear.setText(currentYear);

                setUI();

                dialog.dismiss();

            }
        });

    }

    private void setUI() {

        setHighestRank();
        setHighestSuccessPercent();
        setHighestCollection();
        setHighestWtLoss();
        setHighestAvgWtLoss();

    }

    private void setHighestRank() {


        Report report = realm.where(Report.class)
                .sort("rank", Sort.ASCENDING)
                .equalTo("year",currentYear)
                .equalTo("month",currentMonth)
                .findFirst();

        if (report != null) {
            tvRank.setText(String.valueOf(report.getRank()));

            String name = Utils.getNameFromId(report.getId());
            tvRankName.setText(name);
        }else {

            tvRank.setText("--");
            tvRankName.setText("--");

        }

    }


    private void setHighestSuccessPercent() {

        Report report
                = realm.where(Report.class)
                .sort("successPercentage",Sort.DESCENDING)
                .equalTo("year",currentYear)
                .equalTo("month",currentMonth)
                .findFirst();

        if (report != null) {

            String percent = report.getSuccessPercentage() + " %";

            tvSuccess.setText(percent);
            tvSuccessName.setText(Utils.getNameFromId(report.getId()));

        }else {

            tvSuccess.setText("--");
            tvSuccessName.setText("--");

        }

    }

    private void setHighestCollection() {

        Report report
                = realm.where(Report.class)
                .sort("collection",Sort.DESCENDING)
                .equalTo("year",currentYear)
                .equalTo("month",currentMonth)
                .findFirst();


        if (report != null) {

            String collection = report.getCollection() + " Rs";

            tvCollection.setText(collection);
            tvCollectionName.setText(Utils.getNameFromId(report.getId()));


        }else {

            tvCollection.setText("--");
            tvCollectionName.setText("--");

        }

    }

    private void setHighestWtLoss() {

        Report report
                = realm.where(Report.class)
                .sort("weightLoss",Sort.DESCENDING)
                .equalTo("year",currentYear)
                .equalTo("month",currentMonth)
                .findFirst();


        if (report != null) {

            String wtLoss = report.getWeightLoss() + " Kg";

            tvWtLoss.setText(wtLoss);
            tvWtLossName.setText(Utils.getNameFromId(report.getId()));

        }else {

            tvWtLoss.setText("--");
            tvWtLossName.setText("--");

        }

    }

    private void setHighestAvgWtLoss() {

        Report report
                = realm.where(Report.class)
                .sort("avgWeightLoss",Sort.DESCENDING)
                .equalTo("year",currentYear)
                .equalTo("month",currentMonth)
                .findFirst();


        if (report != null) {

            String avgWtLoss = String.valueOf(report.getAvgWeightLoss());

            tvAvgWtLoss.setText(avgWtLoss);
            tvAvgWtLossName.setText(Utils.getNameFromId(report.getId()));


        }else {

            tvAvgWtLoss.setText("--");
            tvAvgWtLossName.setText("--");

        }


    }






}

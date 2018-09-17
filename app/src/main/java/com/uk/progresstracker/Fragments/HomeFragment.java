package com.uk.progresstracker.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    }

    private void setUI() {

        setHighestRank();
        setHighestSuccessPercent();
        setHighestCollection();
        setHighestWtLoss();
        setHighestAvgWtLoss();

    }

    private void setHighestRank() {

        int monthIndex = calendar.get(Calendar.MONTH);
        String currentMonth = Utils.months[monthIndex];

        Log.d("Check","Monht is " + currentMonth);

        Report report = realm.where(Report.class)
                .sort("rank", Sort.ASCENDING)
                .equalTo("month",currentMonth)
                .findFirst();

        if (report != null) {
            tvRank.setText(String.valueOf(report.getRank()));

            String name = Utils.getNameFromId(report.getId());
            tvRankName.setText(name);
        }

    }


    private void setHighestSuccessPercent() {

        int monthIndex = calendar.get(Calendar.MONTH);
        String currentMonth = Utils.months[monthIndex];

        Report report
                = realm.where(Report.class)
                .sort("successPercentage",Sort.DESCENDING)
                .equalTo("month",currentMonth)
                .findFirst();

        if (report != null) {

            String percent = report.getSuccessPercentage() + " %";

            tvSuccess.setText(percent);
            tvSuccessName.setText(Utils.getNameFromId(report.getId()));

        }

    }

    private void setHighestCollection() {

        int monthIndex = calendar.get(Calendar.MONTH);
        String currentMonth = Utils.months[monthIndex];

        Report report
                = realm.where(Report.class)
                .sort("collection",Sort.DESCENDING)
                .equalTo("month",currentMonth)
                .findFirst();


        if (report != null) {

            String collection = report.getCollection() + " Rs";

            tvCollection.setText(collection);
            tvCollectionName.setText(Utils.getNameFromId(report.getId()));


        }

    }

    private void setHighestWtLoss() {

        int monthIndex = calendar.get(Calendar.MONTH);
        String currentMonth = Utils.months[monthIndex];

        Report report
                = realm.where(Report.class)
                .sort("weightLoss",Sort.DESCENDING)
                .equalTo("month",currentMonth)
                .findFirst();


        if (report != null) {

            String wtLoss = report.getWeightLoss() + " Kg";

            tvWtLoss.setText(wtLoss);
            tvWtLossName.setText(Utils.getNameFromId(report.getId()));


        }

    }

    private void setHighestAvgWtLoss() {

        int monthIndex = calendar.get(Calendar.MONTH);
        String currentMonth = Utils.months[monthIndex];

        Report report
                = realm.where(Report.class)
                .sort("avgWeightLoss",Sort.DESCENDING)
                .equalTo("month",currentMonth)
                .findFirst();


        if (report != null) {

            String avgWtLoss = String.valueOf(report.getAvgWeightLoss());

            tvAvgWtLoss.setText(avgWtLoss);
            tvAvgWtLossName.setText(Utils.getNameFromId(report.getId()));


        }

    }






}

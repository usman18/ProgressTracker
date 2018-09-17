package com.uk.progresstracker.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.uk.progresstracker.Model.Report;
import com.uk.progresstracker.R;
import com.uk.progresstracker.Utils;

import java.util.ArrayList;
import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by usman on 14-09-2018.
 */

public class StatisticsFragment extends Fragment{

    private Realm realm;

    private BarChart successChart;
    private BarChart wtLossChart;
    private BarChart avgWtLossChart;
    private BarChart collectionChart;

    private int month;
    private String currentMonth;

    RealmResults<Report> reports;
    private ArrayList<String> names;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return LayoutInflater.from(getContext())
                .inflate(R.layout.stats_layout,container,false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initialize(view);
        setUI();

    }

    private void initialize(View view) {

        realm = Realm.getDefaultInstance();

        names = new ArrayList<>();

        successChart = view.findViewById(R.id.chartSuccess);
        wtLossChart = view.findViewById(R.id.chartWtLoss);
        avgWtLossChart = view.findViewById(R.id.chartAvgWtLoss);
        collectionChart = view.findViewById(R.id.chartCollection);

        month = Calendar.getInstance().get(Calendar.MONTH);
        currentMonth = Utils.months[month];

        //Todo
        reports = realm.where(Report.class)
                .equalTo("month",currentMonth)
                .findAll();

        Log.d("Check","Number of reports is " + reports.size());



    }

    private void setUI() {

        setSuccessChart();
        setWtLossChart();
        setAvgWtLossChart();
        setCollectionChart();

    }

    private void setSuccessChart() {

        names.clear();

        ArrayList<BarEntry>
                entries = new ArrayList<>();


        int counter = 0;

        for (Report r : reports) {

            entries.add(new BarEntry(counter++,(float) r.getSuccessPercentage()));
            Log.d("Check","Id " + r.getId() + " Success " + r.getSuccessPercentage());
            names.add(Utils.getNameFromId(r.getId()));

        }


        Log.d("Check","Number of NAMES is " + names.size());

        XAxisValueFormatter formatter = new XAxisValueFormatter(names.toArray(new String[names.size()]));

        XAxis xAxis = successChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(formatter);
        xAxis.setGranularity(1f);

        YAxis lyaxis = successChart.getAxisLeft();
        lyaxis.setAxisMinimum(0f);
        lyaxis.setAxisMaximum(100);     //since percent

        YAxis ryaxis = successChart.getAxisRight();
        ryaxis.setAxisMinimum(0f);
        ryaxis.setAxisMaximum(100);


        BarDataSet dataSet = new BarDataSet(entries,"Success %");
        dataSet.setColors(Utils.colorsArray,getContext());

        BarData data = new BarData(dataSet);

        data.setValueTextSize(12);

        successChart.setData(data);
        data.setBarWidth(0.6f);
        successChart.setVisibleXRangeMaximum(5);

        successChart.getDescription().setEnabled(false);
        successChart.invalidate();

    }


    private void setWtLossChart() {

        names.clear();

        ArrayList<BarEntry>
                entries = new ArrayList<>();

        int counter = 0;

        for (Report r : reports) {

            entries.add(new BarEntry(counter++,(float) r.getWeightLoss()));
            Log.d("Check","Id " + r.getId() + " Success " + r.getWeightLoss());
            names.add(Utils.getNameFromId(r.getId()));

        }


        Log.d("Check","Number of NAMES is " + names.size());

        XAxisValueFormatter formatter = new XAxisValueFormatter(names.toArray(new String[names.size()]));

        XAxis xAxis = wtLossChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(formatter);
        xAxis.setGranularity(1f);

        YAxis lyaxis = wtLossChart.getAxisLeft();
        lyaxis.setAxisMinimum(0f);

        YAxis ryaxis = wtLossChart.getAxisRight();
        ryaxis.setAxisMinimum(0f);


        BarDataSet dataSet = new BarDataSet(entries,"Weight Loss (kg)");
        dataSet.setColors(Utils.colorsArray,getContext());

        BarData data = new BarData(dataSet);

        data.setValueTextSize(12);

        wtLossChart.setData(data);
        data.setBarWidth(0.6f);
        wtLossChart.setVisibleXRangeMaximum(5);

        wtLossChart.getDescription().setEnabled(false);
        wtLossChart.invalidate();

    }

    private void setAvgWtLossChart() {

        names.clear();

        ArrayList<BarEntry>
                entries = new ArrayList<>();

        int counter = 0;

        for (Report r : reports) {

            entries.add(new BarEntry(counter++,(float) r.getAvgWeightLoss()));
            Log.d("Check","Id " + r.getId() + " Success " + r.getAvgWeightLoss());
            names.add(Utils.getNameFromId(r.getId()));

        }


        Log.d("Check","Number of NAMES is " + names.size());

        XAxisValueFormatter formatter = new XAxisValueFormatter(names.toArray(new String[names.size()]));

        XAxis xAxis = avgWtLossChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(formatter);
        xAxis.setGranularity(1f);

        YAxis lyaxis = avgWtLossChart.getAxisLeft();
        lyaxis.setAxisMinimum(0f);

        YAxis ryaxis = avgWtLossChart.getAxisRight();
        ryaxis.setAxisMinimum(0f);


        BarDataSet dataSet = new BarDataSet(entries,"Average Weight Loss ( 0 - 4 )");
        dataSet.setColors(Utils.colorsArray,getContext());

        BarData data = new BarData(dataSet);

        data.setValueTextSize(12);

        avgWtLossChart.setData(data);
        data.setBarWidth(0.6f);
        avgWtLossChart.setVisibleXRangeMaximum(5);

        avgWtLossChart.getDescription().setEnabled(false);
        avgWtLossChart.invalidate();

    }


    private void setCollectionChart() {

        names.clear();

        ArrayList<BarEntry>
                entries = new ArrayList<>();

        int counter = 0;

        for (Report r : reports) {

            entries.add(new BarEntry(counter++,(float) r.getCollection()));
            Log.d("Check","Id " + r.getId() + " Success " + r.getCollection());
            names.add(Utils.getNameFromId(r.getId()));

        }


        Log.d("Check","Number of NAMES is " + names.size());

        XAxisValueFormatter formatter = new XAxisValueFormatter(names.toArray(new String[names.size()]));

        XAxis xAxis = collectionChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(formatter);
        xAxis.setGranularity(1f);

        YAxis lyaxis = collectionChart.getAxisLeft();
        lyaxis.setAxisMinimum(0f);

        YAxis ryaxis = collectionChart.getAxisRight();
        ryaxis.setAxisMinimum(0f);


        BarDataSet dataSet = new BarDataSet(entries,"Collection (Rs)");
        dataSet.setColors(Utils.colorsArray,getContext());

        BarData data = new BarData(dataSet);

        data.setValueTextSize(12);

        collectionChart.setData(data);
        data.setBarWidth(0.6f);
        collectionChart.setVisibleXRangeMaximum(5);

        collectionChart.getDescription().setEnabled(false);
        collectionChart.invalidate();

    }


    class XAxisValueFormatter implements IAxisValueFormatter{

        private String[] values;

        public XAxisValueFormatter(String[] values) {
            this.values = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return values[(int) value];
        }
    }


}

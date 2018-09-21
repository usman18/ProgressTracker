package com.uk.progresstracker.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.uk.progresstracker.Model.Report;
import com.uk.progresstracker.R;
import com.uk.progresstracker.Utils;

import java.util.ArrayList;
import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by usman on 14-09-2018.
 */

public class StatisticsFragment extends Fragment{

    private Realm realm;

    private TextView tvMonth;
    private TextView tvYear;

    private BarChart rankChart;
    private BarChart successChart;
    private BarChart wtLossChart;
    private BarChart avgWtLossChart;
    private BarChart collectionChart;

    private String monthName;
    private String year;

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

        tvMonth = view.findViewById(R.id.month);
        tvYear = view.findViewById(R.id.year);

        rankChart = view.findViewById(R.id.chartRank);
        successChart = view.findViewById(R.id.chartSuccess);
        wtLossChart = view.findViewById(R.id.chartWtLoss);
        avgWtLossChart = view.findViewById(R.id.chartAvgWtLoss);
        collectionChart = view.findViewById(R.id.chartCollection);

        view.findViewById(R.id.ll)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDatePicker();
                    }
                });

        //Initially month and year will be current month and year
        int month = Calendar.getInstance().get(Calendar.MONTH);
        monthName = Utils.months[month];
        year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));

        tvMonth.setText(monthName);
        tvYear.setText(year);

        reports = realm.where(Report.class)
                .equalTo("year",year)
                .equalTo("month",monthName)
                .sort("id",Sort.ASCENDING)
                .findAll();

        Log.d("Check","Number of reports is " + reports.size());


    }

    private void setUI() {

        setSuccessChart();
        setWtLossChart();
        setAvgWtLossChart();
        setCollectionChart();
        setRankChart();

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

                monthName = Utils.months[monthIndex];
                year = String.valueOf(datePicker.getYear());

                tvMonth.setText(monthName);
                tvYear.setText(year);

                reports = realm.where(Report.class)
                        .equalTo("year",year)
                        .equalTo("month",monthName)
                        .sort("id",Sort.ASCENDING)
                        .findAll();

                Log.d("Check","Month is " + monthName + " year is " + year);
                Log.d("Check","Size is " + reports.size());

                for (Report report : reports) {

                    Log.d("Check","ID : " + report.getId() + " WtLoss : " + report.getWeightLoss())    ;
                    Log.d("Check","Success is " + report.getSuccessPercentage());

                }

                dialog.dismiss();

                setUI();


            }
        });

    }


    private void setRankChart() {

        names.clear();
        rankChart.clear();

        if (reports.size() == 0) {
            return;
        }

        int maxRank = getMaxRank(); //lowest rank basically, highest by number

        ArrayList<BarEntry>
                entries = new ArrayList<>();

        int counter = 0;

        for (Report r : reports) {

            entries.add(new BarEntry(counter++,(float) ((maxRank - r.getRank()) + 1)));   // subtracting from the maximum since it higher ranks (lower by number) must appear higher on graph
            Log.d("Check","Id " + r.getId() + " Rank " + r.getRank());
            names.add(Utils.getNameFromId(r.getId()));

        }


        Log.d("Check","Number of NAMES is " + names.size());

        XAxisValueFormatter formatter = new XAxisValueFormatter(names.toArray(new String[names.size()]));

        XAxis xAxis = rankChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(formatter);
        xAxis.setGranularity(1f);

        YAxis lyaxis = rankChart.getAxisLeft();
        lyaxis.setEnabled(false);
        lyaxis.setSpaceBottom(0f);

        YAxis ryaxis = rankChart.getAxisRight();
        ryaxis.setEnabled(false);
        ryaxis.setSpaceBottom(0f);


        BarDataSet dataSet = new BarDataSet(entries,"Rankings");
        dataSet.setColors(Utils.colorsArray,getContext());
        dataSet.setValueFormatter(new RankValueFormatter(maxRank));

        BarData data = new BarData(dataSet);

        data.setValueTextSize(12);

        rankChart.setData(data);


        rankChart.animateY(1500, Easing.EasingOption.EaseOutBounce);

        data.setBarWidth(0.6f);
        rankChart.setVisibleXRangeMaximum(5);

        rankChart.getDescription().setEnabled(false);
        rankChart.invalidate();

    }

    private int getMaxRank() {

        Report report = realm.where(Report.class)
                .equalTo("year",year)
                .equalTo("month", monthName)
                .sort("rank", Sort.DESCENDING)
                .findFirst();

        if (report != null)
            return report.getRank();
        return 100;
    }

    private void setSuccessChart() {

        names.clear();
        successChart.clear();       //This will be used when filter results are not available

        if (reports.size() == 0) {
            return;
        }

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

        successChart.animateY(1500, Easing.EasingOption.EaseOutBounce);


        successChart.setData(data);
        data.setBarWidth(0.6f);
        successChart.setVisibleXRangeMaximum(5);

        successChart.getDescription().setEnabled(false);
        successChart.invalidate();

    }


    private void setWtLossChart() {

        names.clear();
        wtLossChart.clear();

        if (reports.size() == 0) {
            return;
        }

        ArrayList<BarEntry>
                entries = new ArrayList<>();

        int counter = 0;

        for (Report r : reports) {

            entries.add(new BarEntry(counter++,(float) r.getWeightLoss()));
            Log.d("Check","Id " + r.getId() + " Success " + r.getWeightLoss());
            names.add(Utils.getNameFromId(r.getId()));

        }

        Log.d("Check","Number of entries is " + entries.size());

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

        wtLossChart.animateY(1500, Easing.EasingOption.EaseOutBounce);

        data.setBarWidth(0.6f);
        wtLossChart.setVisibleXRangeMaximum(5);

        wtLossChart.getDescription().setEnabled(false);
        wtLossChart.invalidate();

    }

    private void setAvgWtLossChart() {

        names.clear();
        avgWtLossChart.clear();

        if (reports.size() == 0) {
            return;
        }

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

        avgWtLossChart.animateY(1500, Easing.EasingOption.EaseOutBounce);

        data.setBarWidth(0.6f);
        avgWtLossChart.setVisibleXRangeMaximum(5);

        avgWtLossChart.getDescription().setEnabled(false);
        avgWtLossChart.invalidate();

    }


    private void setCollectionChart() {

        names.clear();
        collectionChart.clear();

        if (reports.size() == 0) {
            return;
        }

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

        collectionChart.animateY(1500, Easing.EasingOption.EaseOutBounce);

        data.setBarWidth(0.6f);
        collectionChart.setVisibleXRangeMaximum(5);

        collectionChart.getDescription().setEnabled(false);
        collectionChart.invalidate();

    }


    class XAxisValueFormatter implements IAxisValueFormatter{

        private String[] values;

        public XAxisValueFormatter(String[] values) {
            this.values = values;

            Log.d("Check","Size of array is " + values.length);
            Log.d("Check","Array is as below");

            for (String s : values) {
                Log.d("Check","Element is " + s);
            }

        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            int index = (int) value;
            Log.d("Check","Index is " + index);

            if (index >= values.length)
                return  "Dummy";
            return values[index];

        }
    }

    class RankValueFormatter implements IValueFormatter{

        int maxRank;

        public RankValueFormatter(int max) {
            maxRank = max;
        }

        private String getActualValue(int value) {

            return String.valueOf((maxRank - value) + 1);

        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return getActualValue((int)value);
        }
    }



}

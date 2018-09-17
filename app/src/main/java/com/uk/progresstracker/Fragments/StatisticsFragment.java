package com.uk.progresstracker.Fragments;

import android.graphics.Color;
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
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by usman on 14-09-2018.
 */

public class StatisticsFragment extends Fragment{

    private Realm realm;

    private BarChart successChart;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return LayoutInflater.from(getContext())
                .inflate(R.layout.stats_layout,container,false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        intialize(view);
        setUI();

    }

    private void intialize(View view) {

        realm = Realm.getDefaultInstance();

        successChart = view.findViewById(R.id.chartSuccess);

    }

    private void setUI() {

        setSuccessChart();

    }

    private void setSuccessChart() {

        ArrayList<BarEntry>
                entries = new ArrayList<>();

        int month = Calendar.getInstance().get(Calendar.MONTH);
        String currentMonth = Utils.months[month];

        RealmResults<Report>
                reports = realm.where(Report.class)
                .findAll();

        Log.d("Check","Number of reports is " + reports.size());

        ArrayList<String>
                names = new ArrayList<>();


        int counter = 0;

        for (Report r : reports) {

            entries.add(new BarEntry(counter++,(float) r.getSuccessPercentage()));
            Log.d("Check","Id " + r.getId() + " Success " + r.getSuccessPercentage());
            names.add(Utils.getNameFromId(r.getId()));

        }

        Random rnd = new Random();

        ArrayList<Integer> colors = new ArrayList<>();

        for (int i = 0; i< reports.size(); i++) {

            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            while (colors.contains(color)) {
                 color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            }

            colors.add(color);
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

        int []colorsArray = new int[colors.size()];

        for (int i = 0; i < colors.size() ; i++){
            colorsArray[i] = colors.get(i);
        }

        BarDataSet dataSet = new BarDataSet(entries,"Success %");
//        dataSet.setColors(colorsArray,getContext());

        BarData data = new BarData(dataSet);
        data.setBarWidth(0.9f);
        data.setValueTextSize(12);



        successChart.setData(data);
        successChart.getDescription().setEnabled(false);
        successChart.invalidate();

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

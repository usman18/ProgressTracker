package com.uk.progresstracker.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

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
import com.uk.progresstracker.Model.TeamMember;
import com.uk.progresstracker.R;
import com.uk.progresstracker.Utils;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.Sort;

public class IndividualStatisticsActivity extends AppCompatActivity {
    
    private Realm realm;
    private TeamMember member;
    private RealmList<Report> reports;
    
    private ArrayList<String> months;
    
    private BarChart rankChart;
    private BarChart successChart;
    private BarChart wtLossChart;
    private BarChart avgWtLossChart;
    private BarChart collectionChart;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_statistics);

        initialize();
        setupUI();

    }

    private void initialize() {

        rankChart = findViewById(R.id.chartRank);
        successChart = findViewById(R.id.chartSuccess);
        wtLossChart = findViewById(R.id.chartWtLoss);
        avgWtLossChart = findViewById(R.id.chartAvgWtLoss);
        collectionChart = findViewById(R.id.chartCollection);

        realm = Realm.getDefaultInstance();

        months = new ArrayList<>();

        member = realm.where(TeamMember.class)
                .findFirst();
        
        if (member != null)
            reports = member.getReports();
        
     
    }

    private void setupUI() {

        setRankChart();
        setSuccessChart();
        setWtLossChart();
        setAvgWtLossChart();
        setCollectionChart();

    }

    private void setRankChart() {
        
        if (reports == null)
            return;
        
        months.clear();

        int maxRank = getMaxRank(); //lowest rank basically, highest by number

        ArrayList<BarEntry>
                entries = new ArrayList<>();

        int counter = 0;

        for (Report r : reports) {

            entries.add(new BarEntry(counter++,(float) (maxRank - r.getRank())));   // subtracting from the maximum since it higher ranks (lower by number) must appear higher on graph
            Log.d("Check","Id " + r.getId() + " Rank " + r.getRank());
            //Todo : even include year
            months.add(r.getMonth());

        }


        Log.d("Check","Number of NAMES is " + months.size());

        XAxisValueFormatter formatter = new XAxisValueFormatter(months.toArray(new String[months.size()]));

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
        dataSet.setColors(Utils.colorsArray,IndividualStatisticsActivity.this);
        dataSet.setValueFormatter(new RankValueFormatter(maxRank));

        BarData data = new BarData(dataSet);

        data.setValueTextSize(12);

        rankChart.setData(data);
        data.setBarWidth(0.6f);
        rankChart.setVisibleXRangeMaximum(5);

        rankChart.getDescription().setEnabled(false);
        rankChart.invalidate();

    }

    private void setSuccessChart() {

        months.clear();

        ArrayList<BarEntry>
                entries = new ArrayList<>();


        int counter = 0;

        for (Report r : reports) {

            entries.add(new BarEntry(counter++,(float) r.getSuccessPercentage()));
            Log.d("Check","Id " + r.getId() + " Success " + r.getSuccessPercentage());
            months.add(r.getMonth());

        }


        Log.d("Check","Number of NAMES is " + months.size());

       XAxisValueFormatter formatter = new XAxisValueFormatter(months.toArray(new String[months.size()]));

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
        dataSet.setColors(Utils.colorsArray,IndividualStatisticsActivity.this);

        BarData data = new BarData(dataSet);

        data.setValueTextSize(12);

        successChart.setData(data);
        data.setBarWidth(0.6f);
        successChart.setVisibleXRangeMaximum(5);

        successChart.getDescription().setEnabled(false);
        successChart.invalidate();

    }


    private void setWtLossChart() {

        months.clear();

        ArrayList<BarEntry>
                entries = new ArrayList<>();

        int counter = 0;

        for (Report r : reports) {

            entries.add(new BarEntry(counter++,(float) r.getWeightLoss()));
            Log.d("Check","Id " + r.getId() + " Success " + r.getWeightLoss());
            months.add(r.getMonth());

        }


        Log.d("Check","Number of NAMES is " + months.size());

        XAxisValueFormatter formatter = new XAxisValueFormatter(months.toArray(new String[months.size()]));

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
        dataSet.setColors(Utils.colorsArray,IndividualStatisticsActivity.this);

        BarData data = new BarData(dataSet);

        data.setValueTextSize(12);

        wtLossChart.setData(data);
        data.setBarWidth(0.6f);
        wtLossChart.setVisibleXRangeMaximum(5);

        wtLossChart.getDescription().setEnabled(false);
        wtLossChart.invalidate();

    }


    private void setAvgWtLossChart() {

        months.clear();

        ArrayList<BarEntry>
                entries = new ArrayList<>();

        int counter = 0;

        for (Report r : reports) {

            entries.add(new BarEntry(counter++,(float) r.getAvgWeightLoss()));
            Log.d("Check","Id " + r.getId() + " Success " + r.getAvgWeightLoss());
            months.add(Utils.getNameFromId(r.getId()));

        }


        Log.d("Check","Number of NAMES is " + months.size());

        XAxisValueFormatter formatter = new XAxisValueFormatter(months.toArray(new String[months.size()]));

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
        dataSet.setColors(Utils.colorsArray,IndividualStatisticsActivity.this);

        BarData data = new BarData(dataSet);

        data.setValueTextSize(12);

        avgWtLossChart.setData(data);
        data.setBarWidth(0.6f);
        avgWtLossChart.setVisibleXRangeMaximum(5);

        avgWtLossChart.getDescription().setEnabled(false);
        avgWtLossChart.invalidate();

    }

    private void setCollectionChart() {

        months.clear();

        ArrayList<BarEntry>
                entries = new ArrayList<>();

        int counter = 0;

        for (Report r : reports) {

            entries.add(new BarEntry(counter++,(float) r.getCollection()));
            Log.d("Check","Id " + r.getId() + " Success " + r.getCollection());
            months.add(r.getMonth());

        }


        Log.d("Check","Number of NAMES is " + months.size());

        XAxisValueFormatter formatter = new XAxisValueFormatter(months.toArray(new String[months.size()]));

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
        dataSet.setColors(Utils.colorsArray,IndividualStatisticsActivity.this);

        BarData data = new BarData(dataSet);

        data.setValueTextSize(12);

        collectionChart.setData(data);
        data.setBarWidth(0.6f);
        collectionChart.setVisibleXRangeMaximum(5);

        collectionChart.getDescription().setEnabled(false);
        collectionChart.invalidate();

    }




    private int getMaxRank() {

        Report report = realm.where(Report.class)
                .sort("rank", Sort.DESCENDING)
                .findFirst();

        if (report != null)
            return report.getRank();
        return 100;
    }


    class XAxisValueFormatter implements IAxisValueFormatter {

        private String[] values;

        public XAxisValueFormatter(String[] values) {
            this.values = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return values[(int) value];
        }
    }

    class RankValueFormatter implements IValueFormatter {

        int maxRank;

        public RankValueFormatter(int max) {
            maxRank = max;
        }

        private String getActualValue(int value) {

            return String.valueOf(maxRank - value);

        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return getActualValue((int)value);
        }
    }

}
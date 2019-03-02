package com.uk.progresstracker.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

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
import com.uk.progresstracker.Model.TeamMember;
import com.uk.progresstracker.R;
import com.uk.progresstracker.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.Sort;

public class IndividualStatisticsActivity extends AppCompatActivity {

    private static final String TAG = "Check";
    private Realm realm;
    private RealmList<Report> reports;
    
    private ArrayList<String> dates;
    
    private BarChart rankChart;
    private BarChart successChart;
    private BarChart wtLossChart;
    private BarChart avgWtLossChart;
    private BarChart collectionChart;
    private BarChart penaltyChart;
    private BarChart activityChart;

    private String eid;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_statistics);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {

            eid = bundle.getString("eid");
            name = bundle.getString("name");

            if (eid == null || name == null) {
                displayMsg();
            }


        }else {
            displayMsg();
        }

        Utils.addBackButton(this,getSupportActionBar(),name);
        initialize();
        setupUI();

    }

    private void displayMsg() {

        Toast.makeText(IndividualStatisticsActivity.this,"Something went wrong",Toast.LENGTH_SHORT)
                .show();

        finish();
    }

    private void initialize() {

        rankChart = findViewById(R.id.chartRank);
        successChart = findViewById(R.id.chartSuccess);
        wtLossChart = findViewById(R.id.chartWtLoss);
        avgWtLossChart = findViewById(R.id.chartAvgWtLoss);
        collectionChart = findViewById(R.id.chartCollection);
        penaltyChart = findViewById(R.id.chartPenalty);
        activityChart = findViewById(R.id.chartActivity);

        realm = Realm.getDefaultInstance();

        dates = new ArrayList<>();
    
        TeamMember member = realm.where(TeamMember.class)
            .equalTo("eid", eid)
            .equalTo("name", name)
            .findFirst();
        
        if (member != null)
            reports = member.getReports();

        Log.d(TAG, "initialize: Size is " + reports.size());

        
     
    }

    private void setupUI() {


        sortReportsByTimeStamp();
        setSuccessChart();
        setWtLossChart();
        setAvgWtLossChart();
        setCollectionChart();
        setRankChart();
        setPenaltyChart();
        setActivityChart();

    }

    private void setActivityChart() {
        dates.clear();

        if (reports.size() == 0)
            return;

        ArrayList<BarEntry>
                entries = new ArrayList<>();

        int counter = 0;

        for (Report r : reports) {

            entries.add(new BarEntry(counter++,(float) r.getActivity()));
            dates.add(Utils.formatToDate(r.getTimestamp()));
            
        }


        Log.d("Check","Number of NAMES is " + dates.size());

        XAxisValueFormatter formatter = new XAxisValueFormatter(dates.toArray(new String[dates.size()]));

        XAxis xAxis = activityChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(formatter);
        xAxis.setGranularity(1f);

        YAxis lyaxis = activityChart.getAxisLeft();
        lyaxis.setAxisMinimum(0f);

        YAxis ryaxis = activityChart.getAxisRight();
        ryaxis.setAxisMinimum(0f);


        BarDataSet dataSet = new BarDataSet(entries,"Activity");
        dataSet.setColors(Utils.colorsArray,IndividualStatisticsActivity.this);

        BarData data = new BarData(dataSet);

        data.setValueTextSize(12);

        activityChart.setData(data);

        activityChart.animateY(1500, Easing.EasingOption.EaseOutBounce);

        data.setBarWidth(0.6f);
        activityChart.setVisibleXRangeMaximum(5);

        activityChart.getDescription().setEnabled(false);
        activityChart.invalidate();


    }

    private void setPenaltyChart() {
        dates.clear();

        if (reports.size() == 0)
            return;

        ArrayList<BarEntry>
                entries = new ArrayList<>();

        int counter = 0;

        for (Report r : reports) {

            entries.add(new BarEntry(counter++,(float) r.getPenalty()));
            Log.d("Check","Id " + r.getId() + " Penalty " + r.getPenalty());
            dates.add(Utils.formatToDate(r.getTimestamp()));
    
    
        }


        Log.d("Check","Number of NAMES is " + dates.size());

        XAxisValueFormatter formatter = new XAxisValueFormatter(dates.toArray(new String[dates.size()]));

        XAxis xAxis = penaltyChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(formatter);
        xAxis.setGranularity(1f);

        YAxis lyaxis = penaltyChart.getAxisLeft();
        lyaxis.setAxisMinimum(0f);

        YAxis ryaxis = penaltyChart.getAxisRight();
        ryaxis.setAxisMinimum(0f);


        BarDataSet dataSet = new BarDataSet(entries,"Penalty");
        dataSet.setColors(Utils.colorsArray,IndividualStatisticsActivity.this);

        BarData data = new BarData(dataSet);

        data.setValueTextSize(12);

        penaltyChart.setData(data);

        penaltyChart.animateY(1500, Easing.EasingOption.EaseOutBounce);

        data.setBarWidth(0.6f);
        penaltyChart.setVisibleXRangeMaximum(5);

        penaltyChart.getDescription().setEnabled(false);
        penaltyChart.invalidate();

    }

    private void sortReportsByTimeStamp() {

        realm.beginTransaction();

        Collections.sort(reports, new Comparator<Report>() {
            @Override
            public int compare(Report o1, Report o2) {
                return ~Long.compare(o1.getTimestamp(), o2.getTimestamp());
            }
        });

        realm.commitTransaction();


    }

    private void setRankChart() {

        dates.clear();

        if (reports.size() == 0)
            return;
        

        int maxRank = getMaxRank(); //lowest rank basically, highest by number

        Log.d("Check", "setRankChart: " + "Max rank " + maxRank);

        ArrayList<BarEntry>
                entries = new ArrayList<>();

        int counter = 0;

        for (Report r : reports) {

            entries.add(new BarEntry(counter++, (float) ((maxRank - r.getRank()) + 1)));   // subtracting from the maximum since it higher ranks (lower by number) must appear higher on graph
            Log.d("Check","Id " + r.getId() + " Rank " + r.getRank());
            dates.add(Utils.formatToDate(r.getTimestamp()));
            
        }


        Log.d("Check","Number of NAMES is " + dates.size());

        XAxisValueFormatter formatter = new XAxisValueFormatter(dates.toArray(new String[dates.size()]));

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

        rankChart.animateY(1500, Easing.EasingOption.EaseOutBounce);

        data.setBarWidth(0.6f);
        rankChart.setVisibleXRangeMaximum(5);

        rankChart.getDescription().setEnabled(false);
        rankChart.invalidate();

    }



    private void setSuccessChart() {

        dates.clear();

        if (reports.size() == 0)
            return;
        
        ArrayList<BarEntry>
                entries = new ArrayList<>();
        
        int counter = 0;

        for (Report r : reports) {
            entries.add(new BarEntry(counter++,(float) r.getSuccessPercentage()));
            dates.add(Utils.formatToDate(r.getTimestamp()));
        }

        
        XAxisValueFormatter formatter = new XAxisValueFormatter(dates.toArray(new String[dates.size()]));

        XAxis xAxis = successChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(formatter);
        xAxis.setGranularity(1f);

        YAxis lyaxis = successChart.getAxisLeft();
        lyaxis.setAxisMinimum(0f);

        if (reports.size() != 0)
            lyaxis.setAxisMaximum(100);     //since percent

        YAxis ryaxis = successChart.getAxisRight();
        ryaxis.setAxisMinimum(0f);

        if (reports.size() != 0)
            ryaxis.setAxisMaximum(100);


        BarDataSet dataSet = new BarDataSet(entries,"Success %");
        dataSet.setColors(Utils.colorsArray,IndividualStatisticsActivity.this);

        BarData data = new BarData(dataSet);

        data.setValueTextSize(12);

        successChart.setData(data);

        successChart.animateY(1500, Easing.EasingOption.EaseOutBounce);

        data.setBarWidth(0.6f);
        successChart.setVisibleXRangeMaximum(5);

        successChart.getDescription().setEnabled(false);
        successChart.invalidate();

    }


    private void setWtLossChart() {

        dates.clear();

        if (reports.size() == 0)
            return;

        ArrayList<BarEntry>
                entries = new ArrayList<>();

        int counter = 0;

        for (Report r : reports) {

            entries.add(new BarEntry(counter++,(float) r.getWeightLoss()));
            dates.add(Utils.formatToDate(r.getTimestamp()));
            
        }
        
        
        XAxisValueFormatter formatter = new XAxisValueFormatter(dates.toArray(new String[dates.size()]));

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

        wtLossChart.animateY(1500, Easing.EasingOption.EaseOutBounce);

        data.setBarWidth(0.6f);

        wtLossChart.setVisibleXRangeMaximum(5);

        wtLossChart.getDescription().setEnabled(false);
        wtLossChart.invalidate();

    }


    private void setAvgWtLossChart() {

        dates.clear();

        if (reports.size() == 0)
            return;

        ArrayList<BarEntry>
                entries = new ArrayList<>();

        int counter = 0;

        for (Report r : reports) {

            entries.add(new BarEntry(counter++,(float) r.getAvgWeightLoss()));
            Log.d("Check","Id " + r.getId() + " Success " + r.getAvgWeightLoss());
            dates.add(Utils.formatToDate(r.getTimestamp()));
    
        }


        Log.d("Check","Number of NAMES is " + dates.size());

        XAxisValueFormatter formatter = new XAxisValueFormatter(dates.toArray(new String[dates.size()]));

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

        avgWtLossChart.animateY(1500, Easing.EasingOption.EaseOutBounce);

        data.setBarWidth(0.6f);
        avgWtLossChart.setVisibleXRangeMaximum(5);

        avgWtLossChart.getDescription().setEnabled(false);
        avgWtLossChart.invalidate();

    }

    private void setCollectionChart() {

        dates.clear();

        if (reports.size() == 0)
            return;

        ArrayList<BarEntry>
                entries = new ArrayList<>();

        int counter = 0;

        for (Report r : reports) {

            entries.add(new BarEntry(counter++,(float) r.getCollection()));
            Log.d("Check","Id " + r.getId() + " Success " + r.getCollection());
            dates.add(Utils.formatToDate(r.getTimestamp()));
    
        }


        Log.d("Check","Number of NAMES is " + dates.size());

        XAxisValueFormatter formatter = new XAxisValueFormatter(dates.toArray(new String[dates.size()]));

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

        collectionChart.animateY(1500, Easing.EasingOption.EaseOutBounce);

        data.setBarWidth(0.6f);
        collectionChart.setVisibleXRangeMaximum(5);

        collectionChart.getDescription().setEnabled(false);
        collectionChart.invalidate();

    }




    private int getMaxRank() {

        Report report = reports.sort("rank",Sort.DESCENDING)
                .first();

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

        RankValueFormatter(int max) {
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return false;
    }



}

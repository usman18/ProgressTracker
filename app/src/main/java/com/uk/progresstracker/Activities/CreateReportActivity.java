package com.uk.progresstracker.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.uk.progresstracker.Model.Report;
import com.uk.progresstracker.Model.TeamMember;
import com.uk.progresstracker.R;
import com.uk.progresstracker.Utils;

import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmList;

public class CreateReportActivity extends AppCompatActivity {
    
    private static final String TAG = "CreateReportActivity";
    
    private String eid;

    private String selectedMonth = "";
    private String selectedYear = "";
    private Calendar selectedDate;
    
    private String wtLoss = "";
    private String avgWtLoss = "";
    private String successPercent = "";
    private String collection = "";
    private String rank = "";
    private String penalty = "";
    private String activity = "";
    private String id;

    private TeamMember member;
    private Report report;


    private TextView tvMonth;
    private TextView tvYear;

    private TextInputEditText etWeightLoss;
    private TextInputEditText etAvgWeightLoss;
    private TextInputEditText etRank;
    private TextInputEditText etSuccessPercent;
    private TextInputEditText etCollection;
    private TextInputEditText etPenalty;
    private TextInputEditText etActivity;

    private Button btnSubmit;
    private Button btnDiscard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_report);
    
        Realm realm = Realm.getDefaultInstance();
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {

            eid = bundle.getString("eid");

            if (eid != null) {

                member = realm.where(TeamMember.class)
                        .equalTo("eid",eid)
                        .findFirst();
            }

        }else {
            displayMsg();
        }


        if (member == null) {
            displayMsg();
        }

        initialize();
        updateUI(selectedMonth,selectedYear);

    }

    private void initialize() {

        Utils.addBackButton(CreateReportActivity.this,getSupportActionBar(),member.getName());
        tvMonth = findViewById(R.id.month);
        tvYear = findViewById(R.id.year);

        etWeightLoss = findViewById(R.id.etWeightLoss);
        etAvgWeightLoss = findViewById(R.id.etAvgWeightLoss);
        etSuccessPercent = findViewById(R.id.etSuccessPercentage);
        etRank = findViewById(R.id.etRank);
        etCollection = findViewById(R.id.etCollection);
        etActivity = findViewById(R.id.etActivity);
        etPenalty = findViewById(R.id.etPenalty);
        etActivity = findViewById(R.id.etActivity);

        final TextView tvName = findViewById(R.id.tvName);
        tvName.setText(member.getName());

        final Spinner spMonth = findViewById(R.id.spMonths);
        spMonth.setSelection((Calendar.getInstance().get(Calendar.MONTH) + 1));

        btnSubmit = findViewById(R.id.btnSubmit);
        btnDiscard = findViewById(R.id.btnDiscard);

        
        selectedDate = Calendar.getInstance();  //Initially current date
        selectedDate.set(Calendar.HOUR_OF_DAY, 0);
        selectedDate.set(Calendar.MINUTE, 0);
        selectedDate.set(Calendar.SECOND, 0);
        selectedDate.set(Calendar.MILLISECOND, 0);
        
        selectedMonth = Utils.getMonth();   //Initially current month
        selectedYear = Utils.getYear();     // Initially current year


        findViewById(R.id.ll)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDatePicker();
                    }
                });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isNull = false;

                if (TextUtils.isEmpty(etWeightLoss.getText().toString().trim())) {
                    etWeightLoss.setError("Please enter weight loss");
                    isNull = true;
                }

                if (TextUtils.isEmpty(etAvgWeightLoss.getText().toString().trim())) {
                    etAvgWeightLoss.setError("Please enter average weight loss");
                    isNull = true;
                }

                if (TextUtils.isEmpty(etCollection.getText().toString().trim())) {
                    etCollection.setError("Please enter collection");
                    isNull = true;
                }

                if (TextUtils.isEmpty(etSuccessPercent.getText().toString().trim())) {
                    etSuccessPercent.setError("Please enter success");
                    isNull = true;
                }

                if (TextUtils.isEmpty(etRank.getText().toString().trim())) {
                    etRank.setError("Please enter rank");
                    isNull = true;
                }

                if (TextUtils.isEmpty(etPenalty.getText().toString().trim())) {
                    etRank.setError("Please enter penalty");
                    isNull = true;
                }

                if (TextUtils.isEmpty(etActivity.getText().toString().trim())) {
                    etRank.setError("Please enter activity");
                    isNull = true;
                }

                if (!isNull) {


                    String weightLoss = etWeightLoss.getText().toString().trim();
                    String avgWeightLoss = etAvgWeightLoss.getText().toString().trim();
                    String rank = etRank.getText().toString().trim();
                    String collection = etCollection.getText().toString().trim();
                    String successPercent = etSuccessPercent.getText().toString().trim();
                    String penalty = etPenalty.getText().toString().trim();
                    String activity = etActivity.getText().toString().trim();

                    saveToDb(member,weightLoss,avgWeightLoss,rank,collection,successPercent,penalty,activity);

                    Snackbar.make((CreateReportActivity.this)
                            .findViewById(R.id.root_layout),"Saved Successfully !",Snackbar.LENGTH_SHORT)
                            .show();

                    lazyFinish();

                }


            }
        });


        btnDiscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (id == null) {
                    Toast.makeText(CreateReportActivity.this,"Id is null, could not delete !",Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                deleteFromDb(id);

                Snackbar.make(findViewById(R.id.root_layout),
                        "Deleted Report Successfully",Snackbar.LENGTH_SHORT)
                        .show();

                lazyFinish();

            }
        });


    }


    private void lazyFinish() {

        new Handler()
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {
                       finish();
                    }
                },2500);

    }


    private void displayMsg() {
    
        Toast.makeText(CreateReportActivity.this,"Something went wrong !",Toast.LENGTH_SHORT)
                .show();
        finish();
    
    }

    private Report getPreviousEntry(String month,String year) {


        String id = member.getEid();
        String name = member.getName();


        String report_id = id + "_" + name + "_" + selectedDate.getTimeInMillis();

        Log.d("Check","Id is " + report_id);
        
        Realm realm = Realm.getDefaultInstance();
        
        return realm.where(Report.class)
                .equalTo("id",report_id)
                .findFirst();

    }

    private void deleteFromDb(final String id) {

        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();

        Report report = realm.where(Report.class)
                .equalTo("id",id)
                .findFirst();

        if (report != null)
            report.deleteFromRealm();

        realm.commitTransaction();

    }


    private void saveToDb(final TeamMember member, String weightLoss, String avgWeightLoss, String rank, String collection, String successPercent, String penalty, String activity) {

        final String eid = member.getEid();
        String memberName = member.getName();
        
        Realm realm = Realm.getDefaultInstance();

        final Report report = new Report();

        report.setWeightLoss(Double.parseDouble(weightLoss));
        report.setAvgWeightLoss(Double.parseDouble(avgWeightLoss));
        report.setCollection(Double.parseDouble(collection));
        report.setRank(Integer.parseInt(rank));
        report.setSuccessPercentage(Double.parseDouble(successPercent));
        report.setActivity(Integer.parseInt(activity));
        report.setPenalty(Integer.parseInt(penalty));
        report.setTimestamp(selectedDate.getTimeInMillis());
        
        report.setId(eid + "_" + memberName + "_" + selectedDate.getTimeInMillis());
        Log.d("Check", "New id is " + report.getId());
        //POJO completely set up here


        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TeamMember teamMember = realm.where(TeamMember.class)
                        .equalTo("eid",eid)
                        .findFirst();

                boolean found = false;

                RealmList<Report> reports = teamMember.getReports();

                Log.d("Check","Given id " + report.getId());
                

                Log.d("Check","Size of reports is " + reports.size());

                for (Report r : reports) {

                    if (r.getId().equals(report.getId())) {
                        Log.d("Check","Found !");

                        found = true;

                        r.setSuccessPercentage(report.getSuccessPercentage());
                        r.setRank(report.getRank());
                        r.setWeightLoss(report.getWeightLoss());
                        r.setAvgWeightLoss(report.getAvgWeightLoss());
                        r.setCollection(report.getCollection());
                        r.setPenalty(report.getPenalty());
                        r.setActivity(report.getActivity());

                        break;

                    }

                }

                if (!found) {
                    Log.d("Check"," Not Found !");
                    reports.add(report);
                    Log.d("Check","Appended in list");
                }



            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d("Check","Successful !");
                Log.d("Check","Month " + report.getTimestamp());
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.d("Check","In on error " + error.getMessage());
            }
        });


    }

    private void showDatePicker() {

        AlertDialog.Builder builder = new AlertDialog.Builder(CreateReportActivity.this);

        View view = LayoutInflater.from(CreateReportActivity.this)
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
                
                selectedDate.set(Calendar.MONTH, monthIndex);
                selectedDate.set(Calendar.YEAR, year);
                selectedDate.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                
                
                //Setting to absolute start
                selectedDate.set(Calendar.HOUR_OF_DAY, 0);
                selectedDate.set(Calendar.MINUTE, 0);
                selectedDate.set(Calendar.SECOND, 0);
                selectedDate.set(Calendar.MILLISECOND, 0);
    
                Log.d(TAG, "onClick: Time " + Utils.formatTo12Hr(selectedDate.getTimeInMillis()));
                
                selectedMonth = Utils.months[monthIndex];
                selectedYear = String.valueOf(year);

                tvMonth.setText(selectedMonth);
                tvYear.setText(selectedYear);

                dialog.dismiss();

                updateUI(selectedMonth,selectedYear);

            }
        });

    }

    private void updateUI(String month,String year) {

        tvMonth.setText(month);
        tvYear.setText(year);

        report = getPreviousEntry(month,year);

        if (report != null) {

            id = report.getId();
            wtLoss = String.valueOf(report.getWeightLoss());
            avgWtLoss = String.valueOf(report.getAvgWeightLoss());
            rank = String.valueOf(report.getRank());
            collection = String.valueOf(report.getCollection());
            successPercent = String.valueOf(report.getSuccessPercentage());
            penalty = String.valueOf(report.getPenalty());
            activity = String.valueOf(report.getActivity());

        }else {

            id = null;
            wtLoss = "";
            avgWtLoss = "";
            rank = "";
            collection = "";
            successPercent = "";
            penalty = "";
            activity = "";

        }

        if (report == null) {
            btnDiscard.setVisibility(View.GONE);
        }else {
            btnDiscard.setVisibility(View.VISIBLE);
        }

        //setting previous values if any
        etWeightLoss.setText(wtLoss);
        etAvgWeightLoss.setText(avgWtLoss);
        etRank.setText(rank);
        etCollection.setText(collection);
        etSuccessPercent.setText(successPercent);
        etActivity.setText(activity);
        etPenalty.setText(penalty);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
        }

        return false;
    }
}

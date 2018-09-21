package com.uk.progresstracker.Activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.uk.progresstracker.Model.Report;
import com.uk.progresstracker.Model.TeamMember;
import com.uk.progresstracker.R;
import com.uk.progresstracker.Utils;

import java.util.Calendar;

import io.realm.Realm;

public class CreateReportActivity extends AppCompatActivity {

    private Realm realm;

    private String eid;

    private String month = "";
    private String wtLoss = "";
    private String avgWtLoss = "";
    private String successPercent = "";
    private String collection = "";
    private String rank = "";

    private TeamMember member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_report);
        
        realm = Realm.getDefaultInstance();



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


        if (member == null)
            displayMsg();


        Report report = getPreviousEntry(member);

        if (report != null) {

            month = report.getMonth();
            wtLoss = String.valueOf(report.getWeightLoss());
            avgWtLoss = String.valueOf(report.getAvgWeightLoss());
            rank = String.valueOf(report.getRank());
            collection = String.valueOf(report.getCollection());
            successPercent = String.valueOf(report.getSuccessPercentage());

        }


        Button btnSubmit = findViewById(R.id.btnSubmit);
        Button btnDiscard = findViewById(R.id.btnDiscard);

        if (report == null)
            btnDiscard.setVisibility(View.GONE);



//        AlertDialog.Builder builder = new AlertDialog.Builder(context);

//        View view = LayoutInflater.from(context)
//                .inflate(R.layout.report_dialog,null);

//        builder.setView(view);


        final TextView tvName = findViewById(R.id.tvName);
        tvName.setText(member.getName());

        final Spinner spMonth = findViewById(R.id.spMonths);
        spMonth.setSelection((Calendar.getInstance().get(Calendar.MONTH) + 1));

        final TextInputEditText etWeightLoss = findViewById(R.id.etWeightLoss);
        final TextInputEditText etAvgWeightLoss = findViewById(R.id.etAvgWeightLoss);
        final TextInputEditText etSuccessPercent = findViewById(R.id.etSuccessPercentage);
        final TextInputEditText etRank = findViewById(R.id.etRank);
        final TextInputEditText etCollection = findViewById(R.id.etCollection);

        //setting previous values if any
        etWeightLoss.setText(wtLoss.trim());
        etAvgWeightLoss.setText(avgWtLoss);
        etRank.setText(rank);
        etCollection.setText(collection);
        etSuccessPercent.setText(successPercent);


//        final AlertDialog dialog = builder.create();

//        dialog.show();

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

                if (spMonth.getSelectedItemPosition() == 0) {
                    Toast.makeText(CreateReportActivity.this,"Please select month",Toast.LENGTH_LONG)
                            .show();
                    isNull = true;
                }


                if (!isNull) {

                    String name = tvName.getText().toString().trim();

                    String month = spMonth.getSelectedItem().toString();

                    String weightLoss = etWeightLoss.getText().toString().trim();
                    String avgWeightLoss = etAvgWeightLoss.getText().toString().trim();
                    String rank = etRank.getText().toString().trim();
                    String collection = etCollection.getText().toString().trim();
                    String successPercent = etSuccessPercent.getText().toString().trim();

                    //Todo
//                    saveToDb(members.get(pos),month,weightLoss,avgWeightLoss,rank,collection,successPercent);
//                    dialog.dismiss();

                    Snackbar.make((CreateReportActivity.this)
                            .findViewById(R.id.root_layout),"Saved Successfully !",Snackbar.LENGTH_SHORT)
                            .show();

                }


            }
        });


        btnDiscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    //Todo
//                deleteFromDb(report.getId());
//                dialog.dismiss();

                Snackbar.make(findViewById(R.id.root_layout),
                        "Deleted Report Successfully",Snackbar.LENGTH_SHORT)
                        .show();

            }
        });

    }

    private void displayMsg() {
    
        Toast.makeText(CreateReportActivity.this,"Something went wrong !",Toast.LENGTH_SHORT)
                .show();
        finish();
    
    }

    private Report getPreviousEntry(TeamMember member) {


        String id = member.getEid();
        String name = member.getName();


        String month = Utils.months[Calendar.getInstance().get(Calendar.MONTH)];
        String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));

        String report_id = id + "_" + name + "_" + month + "_" + year;

        Realm realm = Realm.getDefaultInstance();


        return realm.where(Report.class)
                .equalTo("id",report_id)
                .findFirst();

    }

}

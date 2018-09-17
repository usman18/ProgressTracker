package com.uk.progresstracker.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.uk.progresstracker.Adapter.ReportAdapter;
import com.uk.progresstracker.Model.Report;
import com.uk.progresstracker.Model.TeamMember;
import com.uk.progresstracker.R;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class ReportActivity extends AppCompatActivity {


    private Realm realm;

    private RecyclerView rvMembers;
    private ReportAdapter adapter;
    private RealmResults<TeamMember> realmResults;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        setUpRealm();
        initialize();

        Report report
                = realm.where(Report.class)
                .sort("rank")
                .findFirst();


        if (report != null) {

            Log.d("Check","ID is " + report.getId());
            Log.d("Check","Rank is " + report.getRank());
            Log.d("Check","Collection is " + report.getCollection());

        }


    }


    private void setUpRealm() {

        realm = Realm.getDefaultInstance();

        realmResults = realm.where(TeamMember.class)
                .findAll().sort("name", Sort.ASCENDING);


    }



    private void initialize() {

        rvMembers = findViewById(R.id.rvMembers);
        rvMembers.setLayoutManager(new LinearLayoutManager(ReportActivity.this));
        rvMembers.setHasFixedSize(true);

        adapter = new ReportAdapter(this,realmResults);
        rvMembers.setAdapter(adapter);

    }
}

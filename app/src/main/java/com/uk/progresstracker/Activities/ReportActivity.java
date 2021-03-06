package com.uk.progresstracker.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.uk.progresstracker.Adapter.ReportAdapter;
import com.uk.progresstracker.Model.TeamMember;
import com.uk.progresstracker.R;
import com.uk.progresstracker.Utils;

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

        Utils.addBackButton(this,getSupportActionBar(),"Monthly Report");
        setUpRealm();
        initialize();

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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return false;
    }

}

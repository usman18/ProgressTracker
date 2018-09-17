package com.uk.progresstracker;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.uk.progresstracker.Model.TeamMember;

import io.realm.Realm;

public class AddMemberActivity extends AppCompatActivity {

    private Realm realm;

    private TextInputEditText etName;
    private TextInputEditText etEid;

    private Button btnAddMember;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        initialize();


    }

    private void initialize() {

        realm = Realm.getDefaultInstance();

        etName = findViewById(R.id.member_name);
        etEid = findViewById(R.id.member_eid);

        btnAddMember = findViewById(R.id.add_member);

        btnAddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkIfNull()) {

                    if (TextUtils.isEmpty(etName.getText().toString()))
                        etName.setError("Please enter name");

                    if (TextUtils.isEmpty(etEid.getText().toString()))
                        etEid.setError("Please enter eid");

                }else {

                    //Submit logic here

                    final TeamMember member = new TeamMember();
                    member.setEid(etEid.getText().toString().trim());
                    member.setName(etName.getText().toString().trim());

                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {

                            realm.copyToRealmOrUpdate(member);
                            Log.d("Check","Transaction done");
                            updateUI();
                        }
                    });

                }

            }
        });

    }

    private void updateUI() {

        Snackbar.make(findViewById(R.id.root_layout),"Member Added !",Snackbar.LENGTH_SHORT)
                .show();

        new Handler()
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                },2000);

    }

    private boolean checkIfNull() {

        return TextUtils.isEmpty(etName.getText().toString())
                || TextUtils.isEmpty(etEid.getText().toString());

    }
}

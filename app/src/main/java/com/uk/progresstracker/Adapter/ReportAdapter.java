package com.uk.progresstracker.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.uk.progresstracker.Model.Report;
import com.uk.progresstracker.Model.TeamMember;
import com.uk.progresstracker.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by usman on 15-09-2018.
 */

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.MemberViewHolder>{

    private Context context;
    private RealmResults<TeamMember> members;

    public ReportAdapter(Context context, RealmResults<TeamMember> members) {
        this.context = context;
        this.members = members;
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MemberViewHolder(LayoutInflater.from(context)
        .inflate(R.layout.member_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {

        TeamMember member = members.get(position);

        holder.tvName.setText(member.getName());

    }

    @Override
    public int getItemCount() {
        return members.size();
    }


    class MemberViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;

        MemberViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showReportDialog(getAdapterPosition());
                }
            });

        }
    }


    private void showReportDialog(final int pos) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        View view = LayoutInflater.from(context)
                .inflate(R.layout.report_dialog,null);
        builder.setView(view);

        final TextView tvName = view.findViewById(R.id.tvName);
        tvName.setText(members.get(pos).getName());

        final Spinner spMonth = view.findViewById(R.id.spMonths);

        final EditText etWeightLoss = view.findViewById(R.id.etWeightLoss);
        final EditText etAvgWeightLoss = view.findViewById(R.id.etAvgWeightLoss);
        final EditText etSuccessPercent = view.findViewById(R.id.etSuccessPercentage);
        final EditText etRank = view.findViewById(R.id.etRank);
        final EditText etCollection = view.findViewById(R.id.etCollection);

        Button btnSubmit = view.findViewById(R.id.btnSubmit);
        Button btnDiscard = view.findViewById(R.id.btnDiscard);


        final AlertDialog dialog = builder.create();

        dialog.show();

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
                    Toast.makeText(context,"Please select month",Toast.LENGTH_LONG)
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

                    saveToDb(members.get(pos),month,weightLoss,avgWeightLoss,rank,collection,successPercent);
                    dialog.dismiss();
                }


            }
        });


        btnDiscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });



    }

    private void saveToDb(final TeamMember member, String month, String weightLoss, String avgWeightLoss, String rank, String collection, String successPercent) {

        final String name = member.getName();
        final String eid = member.getEid();

        Realm realm = Realm.getDefaultInstance();

        final Report report = new Report();

        report.setWeightLoss(Double.parseDouble(weightLoss));
        report.setAvgWeightLoss(Double.parseDouble(avgWeightLoss));
        report.setCollection(Double.parseDouble(collection));
        report.setRank(Integer.parseInt(rank));
        report.setSuccessPercentage(Double.parseDouble(successPercent));
        report.setMonth(month);

        //Todo  : Later add a spinner for year since editing might go wrong
        String year = new SimpleDateFormat("yyyy", Locale.ENGLISH).format(Calendar.getInstance().getTime());
        report.setYear(year);

        report.setId(eid + "_" + name + "_" + month + "_" + year);
        //POJO completely set up here

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TeamMember teamMember = realm.where(TeamMember.class)
                        .equalTo("eid",eid)
                        .equalTo("name",name)
                        .findFirst();

                RealmList<Report> reports = teamMember.getReports();

                if (reports != null) {

                    reports.add(report);
                    Log.d("Check","Report added");

                }else {

                    Log.d("Check","Reports were null");

                }

            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d("Check","Successful !");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.d("Check","In on error " + error.getMessage());
            }
        });


    }

}

package com.uk.progresstracker.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.uk.progresstracker.Activities.IndividualStatisticsActivity;
import com.uk.progresstracker.Model.TeamMember;
import com.uk.progresstracker.R;

import io.realm.Realm;
import io.realm.RealmResults;
/**
 * Created by usman on 15-09-2018.
 */

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberViewHolder>{

    private Context context;
    private RealmResults<TeamMember> members;

    public MemberAdapter(Context context, RealmResults<TeamMember> members) {
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
                    Intent intent = new Intent(context, IndividualStatisticsActivity.class);
                    intent.putExtra("name",members.get(getAdapterPosition()).getName());
                    intent.putExtra("eid",members.get(getAdapterPosition()).getEid());
                    context.startActivity(intent);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showOptionsDialog();
                    return false;
                }
            });

        }

        private void showOptionsDialog() {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setTitle("Alert");
            builder.setMessage("Are you sure you want to delete this member ?");
             builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    boolean deleted = deleteMemberFromDb(members.get(getAdapterPosition()).getEid());
                    String msg;

                    if (deleted) {

                        msg = "Deleted Successfully !";

                    }else {

                        msg = "Could not delete, contact developer!";
                    }

                    dialog.dismiss();
                    Toast.makeText(context,msg,Toast.LENGTH_SHORT)
                            .show();

                }
            });

            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });


            builder.create().show();


        }

        private void showMemberInfo() {

            TeamMember member = members.get(getAdapterPosition());
            final String eid = member.getEid();

            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.activity_add_member,null);

            builder.setView(view);

            final EditText etName = view.findViewById(R.id.member_name);
            final EditText etEid = view.findViewById(R.id.member_eid);

            etName.setText(member.getName());
            etEid.setText(member.getEid());

            Button btnSubmit = view.findViewById(R.id.add_member);
            btnSubmit.setText("SUBMIT");

            final AlertDialog dialog = builder.create();
            dialog.show();

            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    boolean isNull = false;

                    if (TextUtils.isEmpty(etName.getText().toString())) {
                        isNull = true;
                        etName.setError("Please enter name");
                    }

                    if (TextUtils.isEmpty(etEid.getText().toString())) {
                        isNull = true;
                        etEid.setError("Please enter eid");
                    }


                    if (isNull)
                        return;


                    Realm realm = Realm.getDefaultInstance();

                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {

                            TeamMember teamMember =realm.where(TeamMember.class)
                                    .equalTo("eid",eid)
                                    .findFirst();

                            if (teamMember != null) {
                                teamMember.setName(etName.getText().toString());
                            }else {
                                Toast.makeText(context,"Could not update",Toast.LENGTH_SHORT).show();
                            }

                            dialog.dismiss();

                        }
                    }, new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {

                            Log.d("Check","Successfully edited !");

                        }
                    }, new Realm.Transaction.OnError() {
                        @Override
                        public void onError(Throwable error) {

                            Log.d("Check",error.toString());
                        }
                    });


                }
            });


        }

        private boolean deleteMemberFromDb(final String id) {

            boolean deleteSuccessful = false;

            Realm realm = Realm.getDefaultInstance();

            realm.beginTransaction();

            TeamMember member = realm.where(TeamMember.class)
                    .equalTo("eid",id)
                    .findFirst();

            if (member != null) {

                member.getReports().deleteAllFromRealm();
                member.deleteFromRealm();
                deleteSuccessful = true;

            }

            realm.commitTransaction();

            return deleteSuccessful;

        }


    }

}

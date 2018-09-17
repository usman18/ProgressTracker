package com.uk.progresstracker.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.Intent;

import com.uk.progresstracker.Activities.IndividualStatisticsActivity;
import com.uk.progresstracker.Model.TeamMember;
import com.uk.progresstracker.R;

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

        }
    }

}

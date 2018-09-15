package com.uk.progresstracker.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uk.progresstracker.Adapter.MemberAdapter;
import com.uk.progresstracker.Model.TeamMember;
import com.uk.progresstracker.R;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by usman on 14-09-2018.
 */

public class MembersFragment extends Fragment {

    private Realm realm;

    private RecyclerView rvMembers;
    private MemberAdapter adapter;
    private RealmResults<TeamMember> realmResults;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.members_layout,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpRealm();
        initialize(view);

    }

    private void setUpRealm() {

        realm = Realm.getDefaultInstance();

        realmResults = realm.where(TeamMember.class)
                .findAll().sort("name", Sort.ASCENDING);

        realmResults.addChangeListener(new OrderedRealmCollectionChangeListener<RealmResults<TeamMember>>() {
            @Override
            public void onChange(RealmResults<TeamMember> teamMembers, OrderedCollectionChangeSet changeSet) {
                changeSet.getChanges();
                if (adapter != null)
                    adapter.notifyDataSetChanged();

            }
        });


    }



    private void initialize(View view) {

        rvMembers = view.findViewById(R.id.rvMembers);
        rvMembers.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMembers.setHasFixedSize(true);

        adapter = new MemberAdapter(getContext(),realmResults);
        rvMembers.setAdapter(adapter);

    }
}

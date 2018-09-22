package com.uk.progresstracker.Model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by usman on 14-09-2018.
 */

public class TeamMember extends RealmObject {

    @PrimaryKey
    private String eid;
    private String name;

    private RealmList<Report> reports;

    public TeamMember() {
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RealmList<Report> getReports() {
        return reports;
    }

    public void setReports(RealmList<Report> reports) {
        this.reports = reports;
    }

}

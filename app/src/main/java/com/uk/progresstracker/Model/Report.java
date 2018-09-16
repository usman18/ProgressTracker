package com.uk.progresstracker.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by usman on 14-09-2018.
 */

//Report class holds the data of the monthly report generated for a team member

public class Report extends RealmObject {


    @PrimaryKey
    private String id;  //Eid + current millis (There is no real use of eid except for keeping the records unique)

    private long month;
    private double weightLoss;
    private double avgWeightLoss;
    private double successPercentage;
    private int rank;
    private double collection;  //Money

    public long getMonth() {
        return month;
    }

    public void setMonth(long month) {
        this.month = month;
    }

    public double getWeightLoss() {
        return weightLoss;
    }

    public void setWeightLoss(double weightLoss) {
        this.weightLoss = weightLoss;
    }

    public double getAvgWeightLoss() {
        return avgWeightLoss;
    }

    public void setAvgWeightLoss(double avgWeightLoss) {
        this.avgWeightLoss = avgWeightLoss;
    }

    public double getSuccessPercentage() {
        return successPercentage;
    }

    public void setSuccessPercentage(double successPercentage) {
        this.successPercentage = successPercentage;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public double getCollection() {
        return collection;
    }

    public void setCollection(double collection) {
        this.collection = collection;
    }
}

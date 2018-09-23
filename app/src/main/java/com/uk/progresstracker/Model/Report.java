package com.uk.progresstracker.Model;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by usman on 14-09-2018.
 */

//Report class holds the data of the monthly report generated for a team member

public class Report extends RealmObject {

    @PrimaryKey
    private String id;  //eid + _ + name + _ month + _ year

    private String month;
    private String year;

    private double weightLoss;
    private double avgWeightLoss;
    private double successPercentage;
    private int rank;
    private int penalty;
    private int activity;
    private double collection;  //Money

    public int getPenalty() {
        return penalty;
    }

    public void setPenalty(int penalty) {
        this.penalty = penalty;
    }

    public int getActivity() {
        return activity;
    }

    public void setActivity(int activity) {
        this.activity = activity;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
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

    @Override
    public String toString() {
        JSONObject object = new JSONObject();

        try {

            object.put("id",id);
            object.put("month",month);
            object.put("year",year);
            object.put("rank",rank);
            object.put("collection",collection);
            object.put("successPercentage",successPercentage);
            object.put("weightLoss",weightLoss);
            object.put("avgWeightLoss",avgWeightLoss);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }
}

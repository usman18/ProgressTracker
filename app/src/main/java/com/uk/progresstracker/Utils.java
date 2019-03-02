package com.uk.progresstracker;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;

/**
 * Created by usman on 17-09-2018.
 */

public class Utils {

    public static final long DAY_IN_MILLIS = 24 * 60 * 60 * 1000;
    
    public static String[] months
            = {"Jan","Feb","March","April","May","June","July","Aug","Sept","Oct","Nov","Dec"};

    
    private static final String DATE_FORMAT = "dd/MM/yy";
    private static final String TWELVE_HR_TIME = "hh:mm";
    
    
    public static String formatToDate(long timeInMillis) {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        return format.format(timeInMillis);
    }
    

    public static String formatTo12Hr(long timeInMillis) {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        return format.format(timeInMillis);
    }
    


    public static int[] colorsArray
            = {R.color.colorPrimary,R.color.golden,R.color.colorAccent,R.color.cobalt_blue,
                R.color.safeGreen,R.color.justyellow,R.color.colorPrimaryDark,R.color.success_green,
                R.color.dark_grey};



    public static String getNameFromReportId(String id) {
                                             //eid + "_" + name + "_" + ...
    
        Realm realm = Realm.getDefaultInstance();
        
        Log.d("Check","Given id is " + id);

        int index = id.indexOf("_");
        String subId = id.substring(index + 1);
        int index2 = subId.indexOf("_");

        Log.d("Check","Name is " + subId.substring(0,index2));

        return subId.substring(0,index2);

    }

    public static void addBackButton(Context context,ActionBar actionBar, String title) {

        if (actionBar == null || context == null)
            return;

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        if (title != null)
            actionBar.setTitle(title.trim());

    }


    public static String getMonth() {

        return months[Calendar.getInstance().get(Calendar.MONTH)];

    }

    public static String getYear() {

        return new SimpleDateFormat("yyyy", Locale.ENGLISH).format(new Date());

    }



}

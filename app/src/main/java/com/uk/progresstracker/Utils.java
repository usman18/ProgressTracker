package com.uk.progresstracker;

import android.content.Context;
import android.support.v7.app.ActionBar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by usman on 17-09-2018.
 */

public class Utils {

    public static String[] months
            = {"Jan","Feb","March","April","May","June","July","Aug","Sept","Oct","Nov","Dec"};


    public static int getMonthIndex(String month) {

        for (int i = 0; i < months.length; i++) {

            if (month.equalsIgnoreCase(months[i]))
                return i;
        }
        return -1;
    }


    public static int[] colorsArray
            = {R.color.colorPrimary,R.color.golden,R.color.colorAccent,R.color.cobalt_blue,
                R.color.safeGreen,R.color.justyellow,R.color.colorPrimaryDark,R.color.success_green,
                R.color.dark_grey};



    public static String getNameFromId(String id) {
                                             //eid + "_" + name + "_" + ...
        int index = id.indexOf("_");
        String subId = id.substring(index + 1);
        int index2 = subId.indexOf("_");

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

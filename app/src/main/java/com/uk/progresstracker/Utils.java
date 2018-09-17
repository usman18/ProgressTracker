package com.uk.progresstracker;

/**
 * Created by usman on 17-09-2018.
 */

public class Utils {

    public static String[] months
            = {"Jan","Feb","March","April","May","June","July","Aug","Sept","Oct","Nov","Dec"};

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


}

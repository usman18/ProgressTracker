package com.uk.progresstracker;

/**
 * Created by usman on 17-09-2018.
 */

public class Utils {

    public static String[] months
            = {"Jan","Feb","March","April","May","June","July","Aug","Sept","Oct","Nov","Dec"};





    public static String getNameFromId(String id) {



                                             //eid + "_" + name + "_" + ...
        int index = id.indexOf("_");
        String subId = id.substring(index + 1);
        int index2 = subId.indexOf("_");

        return subId.substring(0,index2);

    }


}

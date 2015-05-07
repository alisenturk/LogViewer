package com.logviewer.util;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Helper implements Serializable {

    public static void errorLogger(Class className, Exception e) {
        /* ErrorHandler eh = new ErrorHandler(className, e);
         eh.setLogtype("error");
         eh.setServerName("SRV1");
         eh.logwrite();*/
    }

    public static void errorLogger(Class className, Exception e, String extraInfo) {
        /* ErrorHandler eh = new ErrorHandler(className, e);
         eh.setLogtype("error");
         eh.setServerName("SRV1");
         eh.setExtraInfo(extraInfo);
         eh.logwrite();*/
    }

    public static String date2String(Date date) {
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern("dd/MM/yyyy");
            return sdf.format(date);
        } else {
            return "";
        }
    }

    public static String date2String(Date date, String format) {
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern(format);
            return sdf.format(date);
        } else {
            return "";
        }
    }
     public static String date2ISODate(Date date) {
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            return sdf.format(date);
        } else {
            return "";
        }
    }
    public static String checkNulls(Object value, String newVal) {
        try {
            if (value == null) {
                return newVal;
            } else {
                String str = String.valueOf(value).trim();
                if (str.length() < 1) {
                    return newVal;
                } else {
                    return str;
                }
            }
        } catch (Exception e) {
            Helper.errorLogger(Helper.class, e);
        }
        return "";
    }
    
    public static java.util.Date str2Time(String strDate) {
        try {
            String[] str = new String[3];
            Calendar cal = Calendar.getInstance();
            
                
            String strYear  = strDate.substring(6,10);
            String strMonth = strDate.substring(3,5);
            String strDay   = strDate.substring(0,2);
            String strHour  = strDate.substring(11,13);
            String strMin   = strDate.substring(14,16);
            
            str[0] = strDay;
            str[1] = strMonth;
            str[2] = strYear;
            
            int year = Integer.valueOf(str[2]);
            int month = Integer.valueOf(str[1]) - 1;
            int date = Integer.valueOf(str[0]);
            int hour = Integer.valueOf(strHour);
            int min  = Integer.valueOf(strMin);
            cal.set(year, month, date,hour,min,0);
            return cal.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

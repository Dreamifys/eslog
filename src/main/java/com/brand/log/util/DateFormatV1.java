package com.brand.log.util;

import org.springframework.context.annotation.Configuration;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Configuration
public class DateFormatV1 {

    /**
     * 传入Data类型日期，返回字符串类型时间（ISO8601标准时间）
     */

    public String getISO8601Timestamp(Date date, String zone){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        if(zone!=null && zone.length()>0) {
            TimeZone tz = TimeZone.getTimeZone(zone);
            df.setTimeZone(tz);
        }
        return df.format(date);
    }

    public String getDate(String pattern, String zone){
        Date date = new Date();
        DateFormat df = new SimpleDateFormat(pattern);
        if(zone!=null && zone.length()>0) {
            TimeZone tz = TimeZone.getTimeZone(zone);
            df.setTimeZone(tz);
        }
        return df.format(date);
    }



}

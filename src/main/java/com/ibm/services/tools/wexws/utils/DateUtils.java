package com.ibm.services.tools.wexws.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;


public class DateUtils {

	public static String unixTimeStampToDateString(String unixTS){
		return dateToFormattedString(unixTimeStampToDate(unixTS));
	}

	public static Date unixTimeStampToDate(String unixTS){
		long unixSeconds = Long.parseLong(unixTS);
		Date date = null;
		try {
			date = new Date(unixSeconds * 1000L); // *1000 is to convert seconds to milliseconds
		} catch (Exception ex) {

		}
		return date;
	}
	
	public static String dateToFormattedString(Date date){
		String formattedDate = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"); // the format of your date
			sdf.setTimeZone(TimeZone.getTimeZone("GMT-3")); // give a timezone reference for formating (see comment at the bottom
			formattedDate = sdf.format(date);
		} catch (Exception ex) {

		}
		return formattedDate;
	}
	
	public static String dateToUnixTimeStamp(Date date){
		long unixSeconds = date.getTime() / 1000L;
		return String.valueOf(unixSeconds);
	}
	
	public static long diffInDays(String value){
		try {
			Calendar today = new GregorianCalendar(TimeZone.getTimeZone("GMT-0000"));
			long days = ((Long.parseLong(value)) - today.getTimeInMillis()) / (1000 * 60 * 60 * 24);
			return days;
		} catch (Exception ex) {
			return 0;
		}
		
	}
	
	public static long diffInDays(String value1, String value2){
		try {
			long days = (Long.parseLong(value1) - Long.parseLong(value2)) / (1000 * 60 * 60 * 24);
			return days;
		} catch (Exception ex) {
			return 0;
		}
		
	}
	
	
}

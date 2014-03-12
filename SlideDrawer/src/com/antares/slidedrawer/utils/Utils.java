package com.antares.slidedrawer.utils;

import java.util.Calendar;

public class Utils {

	public static String getDay(int day) {
		String sDay = "";
		switch (day) {
		case Calendar.MONDAY:
			sDay = "Mon";
			break;
		case Calendar.TUESDAY:
			sDay = "Tue";
			break;
		case Calendar.WEDNESDAY:
			sDay = "Wed";
			break;
		case Calendar.THURSDAY:
			sDay = "Thu";
			break;
		case Calendar.FRIDAY:
			sDay = "Fri";
			break;
		case Calendar.SATURDAY:
			sDay = "Sat";
			break;
		case Calendar.SUNDAY:
			sDay = "Sun";
			break;
		default:
			break;
		}
		return sDay;
	}

	public static String getLongDay(int day) {
		String sDay = "";
		switch (day) {
		case Calendar.MONDAY:
			sDay = "Monday";
			break;
		case Calendar.TUESDAY:
			sDay = "Tuesday";
			break;
		case Calendar.WEDNESDAY:
			sDay = "Wednesday";
			break;
		case Calendar.THURSDAY:
			sDay = "Thursday";
			break;
		case Calendar.FRIDAY:
			sDay = "Friday";
			break;
		case Calendar.SATURDAY:
			sDay = "Saturday";
			break;
		case Calendar.SUNDAY:
			sDay = "Sunday";
			break;
		default:
			break;
		}
		return sDay;
	}

	public static String getMonth(int month) {
		String sMonth = "";
		switch (month) {
		case Calendar.JANUARY:
			sMonth = "Jan";
			break;
		case Calendar.FEBRUARY:
			sMonth = "Feb";
			break;
		case Calendar.MARCH:
			sMonth = "Mar";
			break;
		case Calendar.APRIL:
			sMonth = "Apr";
			break;
		case Calendar.MAY:
			sMonth = "May";
			break;
		case Calendar.JUNE:
			sMonth = "Jun";
			break;
		case Calendar.JULY:
			sMonth = "Jul";
			break;
		case Calendar.AUGUST:
			sMonth = "Aug";
			break;
		case Calendar.SEPTEMBER:
			sMonth = "Sept";
			break;
		case Calendar.OCTOBER:
			sMonth = "Oct";
			break;
		case Calendar.NOVEMBER:
			sMonth = "Nov";
			break;
		case Calendar.DECEMBER:
			sMonth = "Dec";
			break;
		default:
			break;
		}
		return sMonth;
	}
}

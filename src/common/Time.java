package common;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Time {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		showTime_HMS();
		showTime_YMD();
	}

	public static String showTime_HMS() {
		Date day = new Date();
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
		// System.out.println(df.format(day));
		return (df.format(day));
	}

	public static String showTime_YMD() {
		Date day = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		// System.out.println(df.format(day));
		return (df.format(day));
	}

}

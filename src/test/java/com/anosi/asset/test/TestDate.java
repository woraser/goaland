package com.anosi.asset.test;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

public class TestDate {
	
	@Test
	public void testDate(){
	    Calendar cal = Calendar.getInstance();  
	    cal.setTime(new Date()); // 设置时间为当前时间
	    cal.add(Calendar.DATE, -1); // 日期减1
	    cal.set(Calendar.HOUR_OF_DAY, 0);  
	    cal.set(Calendar.SECOND, 0);  
	    cal.set(Calendar.MINUTE, 0);  
	    cal.set(Calendar.MILLISECOND, 0);  
	    Date date = cal.getTime();
	    System.out.println(date);
	}

}

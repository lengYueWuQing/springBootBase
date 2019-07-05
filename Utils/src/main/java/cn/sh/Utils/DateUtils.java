package cn.sh.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	/**
     * 日期格式化
     * @param format
     * @param date
     * @return
     */
    public static Date dateFormat(String format,Date date){  
        DateFormat df = new SimpleDateFormat(format);
        return String2Date(format, df.format(date));
    }
    
    /**
	 * 将字符串类型日期格式化成日期
	 * 
	 * @param format
	 *            格式化样式字符串
	 * @param dateTime
	 *            格式化对象日期字符串
	 * @return 格式化日期
	 */
	public static Date String2Date(String format, String dateTime) {
		if (dateTime == null || "".equals(dateTime = dateTime.trim())) {
			return null;
		}
		// 定义日期解析格式
		SimpleDateFormat formatter = new SimpleDateFormat(format);

		Date date = null;
		try {
			date = formatter.parse(dateTime);
		} catch (ParseException e) {
			return null;
		}

		return date;
	}
	
	/**
	 * 获取昨天的开始时间（昨天的凌晨）
	 * 
	 * @param date
	 * @return
	 */
	public static Date getTheBeforeDay(Date date) {
		if (date == null) {
			return null;
		}
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		// 日期加一天
		cal.add(Calendar.DATE, -1);
		// 时分秒设为0
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return cal.getTime();
	}
	
	/**
	 * 返回加减后的日期。
	 * 
	 * @param date
	 *            计算对象的日期
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @param day
	 *            日
	 * @return 加算后的日期
	 */
	public static Date addDate(Date date, int year, int month, int day) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.YEAR, year);
		cal.add(Calendar.MONTH, month);
		cal.add(Calendar.DATE, day);
		return cal.getTime();
	}
	
	/**
	 * 
	 * addDate:在制定时间上增加 时，分，秒，毫秒
	 *
	 * @param date
	 *            需要增加的时间
	 * @param hour
	 *            加小时
	 * @param minute
	 *            分钟
	 * @param second
	 *            秒钟
	 * @param millisecond
	 *            毫秒
	 * @return
	 */
	public static Date addDate(Date date, int hour, int minute, int second, int millisecond) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, second);
		cal.set(Calendar.MILLISECOND, millisecond);
		return cal.getTime();
	}
	
}

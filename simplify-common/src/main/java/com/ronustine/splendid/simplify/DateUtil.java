package com.ronustine.splendid.simplify;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class DateUtil {

	private DateUtil() {

	}

	/**
	 * 根据指定的Date生成相应的Calendar; 每次调用都是一个新的对象，防止单例模式下，线程安全问题
	 * 
	 * @param date
	 * @return
	 * @param
	 * @return Calendar
	 */
	private static Calendar getCalendar4Date(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}

	/**
	 * 返回指定日期的真实年份 (如：2011-05-20返回2011)
	 * 
	 * 注意：它与date.getYear()的返回值不同。 当前方法返回的就是真实的年份值，
	 * 而date.getYear()返回的是真实年份减去1900, 所以date.getYear()方法已经废弃
	 * 
	 * @param date
	 *            Date 日期
	 * @return 返回年份表示的整型(如：2011)
	 */
	public static int getYear(Date date) {
		Calendar calendar = getCalendar4Date(date);
		return calendar.get(Calendar.YEAR);
	}

	/**
	 * 返回当前日期
	 * 
	 * @precondition 调用方法的前提条件写在这，分行写。
	 * 
	 * @return
	 * @see xxxClass#xxxMethod
	 * @since 1.0
	 * @todo 未完成的事项
	 */
	public static Date getCurrentDate() {
		return new Date();
	}
	
	@SuppressWarnings("deprecation")
	public static Date getCurrentDateNoHour(){
		Date now = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.set(now.getYear()+1900,now.getMonth(),now.getDate());
		return calendar.getTime();
	}

	/**
	 * 返回指定日期的真实月份 (如：2011-05-20返回5) 注意：返回值为1-12。1表示1月，2表示2月 与date.getMonth()
	 * 0表示1月不同。
	 * 
	 * @param date
	 *            Date 日期
	 * @return 返回表示月份的数值(1-12)
	 */
	public static int getMonth(Date date) {
		Calendar calendar = getCalendar4Date(date);
		return calendar.get(Calendar.MONTH) + 1;
	}

	/**
	 * 返回指定日期的日份 （如：2011-5-20返回20)
	 * 
	 * @param date
	 *            Date 日期
	 * @return 返回指定日期天
	 */
	public static int getDay(Date date) {
		Calendar calendar = getCalendar4Date(date);
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 返回指定日期中的小时(24小时制) (如：2011-05-20 18:52:49返回18)
	 * 
	 * @param date
	 *            日期
	 * @return 返回小时
	 */
	public static int getHour(Date date) {
		Calendar calendar = getCalendar4Date(date);
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	
	
	/**
	 * 返回指定日期中的分钟 (如：2011-05-20 18:52:49 返回 52)
	 * 
	 * @param date
	 *            日期
	 * @return 返回分钟
	 */
	public static int getMinute(Date date) {
		Calendar calendar = getCalendar4Date(date);
		return calendar.get(Calendar.MINUTE);
	}

	/**
	 * 返回指定日期中的秒钟 (如：2011-05-20 18:52:49 返回 49)
	 * 
	 * @param date
	 *            Date 日期
	 * @return 返回秒钟
	 */
	public static int getSecond(Date date) {
		Calendar calendar = getCalendar4Date(date);
		return calendar.get(Calendar.SECOND);
	}

	/**
	 * 返回指定日期(从1970 年 1 月 1 日 00:00:00 )的毫秒数
	 * 
	 * @param date
	 *            日期
	 * @return 返回毫秒
	 */
	public static long getMillis(Date date) {
		Calendar calendar = getCalendar4Date(date);
		return calendar.getTimeInMillis();
	}

	/**
	 * 功能描述：日期相加
	 * 
	 * @param date
	 *            Date 日期
	 * @param day
	 *            int 天数
	 * @return 返回相加后的日期
	 */
	public static Date addDate(Date date, int day) {
		Calendar calendar = Calendar.getInstance();
		long millis = getMillis(date) + ((long) day) * 24 * 3600 * 1000;
		calendar.setTimeInMillis(millis);
		return calendar.getTime();
	}

	/**
	 * 功能描述：日期相减
	 * 
	 * @param date
	 *            Date 日期
	 * @param date1
	 *            Date 日期
	 * @return 返回相减后的日期
	 */
	public static int diffDate(Date date, Date date1) {
		return (int) ((getMillis(date) - getMillis(date1)) / (24 * 3600 * 1000));
	}
	

	
	
	/**
	 * 功能描述：日期想减
	 * @param date
	 * @param date1
	 * @return 返回想减后的小时
	 */
	public static int diffDateToHour(Date date, Date date1){
		return (int) ((getMillis(date) - getMillis(date1)) / (3600 * 1000));
	}

	// /**
	// * 功能描述：取得指定月份的第一天
	// *
	// * @param strdate
	// * String 字符型日期
	// * @return String yyyy-MM-dd 格式
	// */
	// public static String getMonthBegin(String strdate) {
	// date = parseDate(strdate);
	// return format(date, "yyyy-MM") + "-01";
	// }
	//
	// /**
	// * 功能描述：取得指定月份的最后一天
	// *
	// * @param strdate
	// * String 字符型日期
	// * @return String 日期字符串 yyyy-MM-dd格式
	// */
	// public static String getMonthEnd(String strdate) {
	// date = parseDate(getMonthBegin(strdate));
	// calendar = Calendar.getInstance();
	// calendar.setTime(date);
	// calendar.add(Calendar.MONTH, 1);
	// calendar.add(Calendar.DAY_OF_YEAR, -1);
	// return formatDate(calendar.getTime());
	// }

	/**
	 * 用给定的格式解析字符型日期 (如：2011-05-20 18:56:49 yyyy-MM-dd hh:mm:ss)
	 * 
	 * 注意：给定格式必须与给定的字符型日期相匹配
	 * 
	 * @param dateStr
	 *            String 字符型日期
	 * @param format
	 *            String 格式
	 * @return Date 日期
	 * @throws ParseException
	 *             日期串与格式不匹配或日期串不合法时，抛出异常
	 */
	public static Date parseDate(String dateStr, String format) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		Date date = dateFormat.parse(dateStr);
		return date;
	}
	public static Date parseDate(String dateStr) throws ParseException {
		return parseDate(dateStr,"yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * Timestamp 转换成String
	 * 
	 * @param date
	 *            Date 日期
	 * @param format
	 *            String 格式
	 * @return 返回字符型日期
	 */
	public static String parseTimestampTOStr(Timestamp timeStamp, String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		String result = dateFormat.format(timeStamp);
		return result;
	}

	/**
	 * 格式化给定日期
	 * 
	 * @param date
	 *            Date 日期
	 * @param format
	 *            String 格式
	 * @return 返回字符型日期
	 */
	public static String format(Date date, String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		String result = dateFormat.format(date);
		return result;
	}
	
	public static String convertDateStrFormat(String dateStr,String sourceFormat,String targetFormat) throws ParseException{
		SimpleDateFormat sourceDateFormat = new SimpleDateFormat(sourceFormat);
		SimpleDateFormat targetDateFormat = new SimpleDateFormat(targetFormat);
		String result = targetDateFormat.format(sourceDateFormat.parse(dateStr));
		return result;
	}
	
	

	/**
	 * 把指定日期解析成Timestamp
	 * @precondition 调用方法的前提条件写在这，分行写。
	 * @param date
	 * @return
	 * @see          xxxClass#xxxMethod
	 * @since        1.0
	 * @todo         未完成的事项
	 */
	public static Timestamp parseDateTOTimestamp(Date date)
	{
	    return new Timestamp(date.getTime());
	}
	/**
	 * 用给定的格式解析字符型日期 (如：2011-05-20 18:56:49 yyyy-MM-dd hh:mm:ss)
	 * 
	 * 注意：给定格式必须与给定的字符型日期相匹配
	 * 
	 * @param dateStr
	 *            String 字符型日期
	 * @param format
	 *            String 格式
	 * @return Timestamp 日期
	 * @throws ParseException
	 *             日期串与格式不匹配或日期串不合法时，抛出异常
	 */
	public static Timestamp parseStrTOTimestamp(String dateStr, String format) throws ParseException {
		Date d = parseDate(dateStr, format);
		return new Timestamp(d.getTime());
	}

	/**
	 * 把形如yyyy-MM-dd HH:mm:ss的时间形式转换成Timestamp类型(形如yyyy-MM-dd HH:mm:ss.SSS)
	 * 
	 * @precondition 调用方法的前提条件写在这，分行写。
	 * @param dateStr
	 * @return
	 * @see com.founder.saas.em.common.util.TimeUtil#strToTimestamp
	 * @since 1.0
	 * @todo 未完成的事项
	 */
	public static Timestamp strToTimestamp(String dateStr) {

		Date date = null;
		if(dateStr.length()==5)
        {
            String nowDate = format(new Date(), "yyyy-MM-dd");
            dateStr = nowDate+" "+dateStr+":00";
        }
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		try {
			System.out.println("----------------日期格式转换之前"+date);
			date = sdf.parse(dateStr);
			System.out.println("----------------日期格式转换之后"+date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}

		Timestamp timestamp = new Timestamp(date.getTime());

		return timestamp;
	}

	/**
	 * 将timestamp类型转化成yyyy-MM-dd格式的字符串
	 * 
	 * @precondition 调用方法的前提条件写在这，分行写。
	 * @param timestamp
	 * @return
	 * @see com.founder.saas.framework.util.DateUtil#timestampToStr
	 * @since 1.0
	 * @todo 未完成的事项
	 */
	public static String timestampToStr(Timestamp timestamp) {
		// 2011-10-11 17:19:09.873
		if (timestamp == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		try {

			Date date = sdf.parse(timestamp.toString());

			return format(date, "yyyy-MM-dd HH:mm:ss");

		} catch (ParseException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return "";
	}

	/**
	 *用给定的格式解析字符型日期 (如：2011-05-20 18:56:49 yyyy-MM-dd hh:mm:ss)
	 * 
	 * 注意：给定格式必须与给定的字符型日期相匹配
	 * @precondition 调用方法的前提条件写在这，分行写。
	 * @param dateStr
	 * @param format
	 * @return
	 * @see          com.founder.saas.framework.util.DateUtil#strToTimestamp
	 * @since        1.0
	 * @todo         未完成的事项
	 */
	public static Timestamp strToTimestamp(String dateStr, String format){
		try {
			Date d = parseDate(dateStr, format);
			return new Timestamp(d.getTime());
		} catch (ParseException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @precondition 调用方法的前提条件写在这，分行写。
	 * @param timestamp
	 *            时间 format 格式化格式
	 * @return 格式化后的字符串
	 * @see com.founder.saas.framework.util.DateUtil#timestampToStr
	 * @since 1.0
	 * @todo 未完成的事项
	 */
	public static String timestampToStr(Timestamp timestamp, String format) {
		// 2011-10-11 17:19:09.873
		if (timestamp == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		try {

			Date date = sdf.parse(timestamp.toString());

			return format(date, format);

		} catch (ParseException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return "";
	}
	
	/**
	 * 
	 * @precondition 调用方法的前提条件写在这，分行写。
	 * @param timestamp
	 * @return
	 * @see          com.founder.saas.framework.util.DateUtil#timestampToDate
	 * @since        1.0
	 * @todo         未完成的事项
	 */
	public static Date timestampToDate(Timestamp timestamp) {
		// 2011-10-11 17:19:09.873
		if (timestamp == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		try {

			return sdf.parse(timestamp.toString());


		} catch (ParseException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 返回当前时间的Timestamp类型
	 * 
	 * @precondition 调用方法的前提条件写在这，分行写。
	 * @return
	 * @see com.founder.saas.framework.util.DateUtil#nowTimeToTimestamp
	 * @since 1.0
	 * @todo 未完成的事项
	 */
	public static Timestamp nowTimeToTimestamp() {

		return new Timestamp(new Date().getTime());
	}

	/**
	 * 返回本月第一天格式yyyy-MM-dd
	 * 
	 * @precondition 调用方法的前提条件写在这，分行写。
	 * @return
	 * @see com.founder.saas.framework.util.DateUtil#getMonthBegin
	 * @since 1.0
	 * @todo 未完成的事项
	 */
	public static String getMonthBeginStr() {

		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.DATE, 1);
		SimpleDateFormat simpleFormate = new SimpleDateFormat(" yyyy-MM-dd ");
		return simpleFormate.format(calendar.getTime());

	}

	/**
	 * 返回本月第一天Timestamp
	 * 
	 * @precondition 调用方法的前提条件写在这，分行写。
	 * @return
	 * @see com.founder.saas.framework.util.DateUtil#getMonthBegin
	 * @since 1.0
	 * @todo 未完成的事项
	 */
	public static Timestamp getMonthBeginTimestamp() {
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.DATE, 1);
		return new Timestamp(calendar.getTime().getTime());
	}

	/**
	 * 返回本月第一天格式yyyy-MM-dd
	 * 
	 * @precondition 调用方法的前提条件写在这，分行写。
	 * @return
	 * @see com.founder.saas.framework.util.DateUtil#getMonthEndStr
	 * @since 1.0
	 * @todo 未完成的事项
	 */
	public static String getMonthEndStr() {
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.DATE, 1);
		calendar.roll(Calendar.DATE, -1);
		SimpleDateFormat simpleFormate = new SimpleDateFormat("yyyy-MM-dd");
		return simpleFormate.format(calendar.getTime());
	}

	/**
	 * 返回本月最后一天Timestamp
	 * 
	 * @precondition 调用方法的前提条件写在这，分行写。
	 * @return
	 * @see com.founder.saas.framework.util.DateUtil#getMonthEndTimestamp
	 * @since 1.0
	 * @todo 未完成的事项
	 */
	public static Timestamp getMonthEndTimestamp() {
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.DATE, 1);
		calendar.roll(Calendar.DATE, -1);
		return new Timestamp(calendar.getTime().getTime());
	}

	/**
	 * 
	 * @precondition 调用方法的前提条件写在这，分行写。
	 * @param birthday
	 *            出生日期
	 * @return 返回年龄
	 * @see com.founder.saas.framework.util.DateUtil#timestampToStr
	 * @since 1.0
	 * @todo 未完成的事项
	 */
	public static String getAge(Date birthday) {
		if (birthday == null) {
			return "";
		}
		int days = diffDate(new Date(), birthday);
		return String.valueOf(days / 365);
	}

	
    
    /**
     * 返回当天开始的Timestamp，比如2011-11-16 00:00:00.000
     * @precondition 调用方法的前提条件写在这，分行写。
     * @return
     * @see          xxxClass#xxxMethod
     * @author fan_fuchun
     * @since        1.0
     * @todo         未完成的事项
     */
    public static Timestamp getTodayBeginTimestamp()
    {
        StringBuffer sb = new StringBuffer(DateUtil.format(DateUtil.getCurrentDate(), "yyyyMMdd")).append("00:00:00");
        return DateUtil.strToTimestamp(sb.toString(), "yyyyMMddHH:mm:ss");
    }
    
    /**
     * 返回当天结束的Timestamp，比如2011-11-16 23:59:59.999
     * @precondition 调用方法的前提条件写在这，分行写。
     * @return
     * @see          xxxClass#xxxMethod
     * @author fan_fuchun
     * @since        1.0
     * @todo         未完成的事项
     */
    public static Timestamp getTodayEndTimestamp()
    {
        StringBuffer sb = new StringBuffer(DateUtil.format(DateUtil.getCurrentDate(), "yyyyMMdd")).append("23:59:59.999");
        return DateUtil.strToTimestamp(sb.toString(), "yyyyMMddHH:mm:ss.SSS");
    }
    public static Timestamp getTimestamp(int year,int month,int day){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month-1, day);
        return parseDateTOTimestamp(calendar.getTime());
    }
    public static Date getDate(int year,int month,int day){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month-1, day);
        return calendar.getTime();
    }
    /**
     * 将当前时间 加秒钟
     * @param date
     * @return
     */
    public static Date addSecond(Date date,int second) {     
    	   Calendar calendar = Calendar.getInstance();     
    	   calendar.setTime(date);     
    	   calendar.add(Calendar.SECOND, second);     
    	 return calendar.getTime();     
    	}    
    /**
     * 获取日期时间的日期部分
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date getOnlyDate(Date date) throws ParseException{
    	if(date==null)
    	{
    		return null;
    	}
    	
    	SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
    	String dateString = format.format(date);
    	return format.parse(dateString);    	
    }
    
    /**
     * 获取日期时间的时间部分
     * @param date
     * @return
     * @throws ParseException
     */
    public static String getOnlyTime(Date date) throws ParseException{
    	if(date==null)
    	{
    		return null;
    	}
    	return DateFormat.getTimeInstance().format(date);    	
    }
    
//	@SuppressWarnings("deprecation")
//	public static void main(String[] arg) throws ParseException{
//		//int i = diffDate(new Date("2010/03/15"),new Date("2010/03/21"));
//		Date d =new Date();
//		System.out.print("========="+getOnlyDate(d));
//	}
    
    
    
    private static String datePattern1 = "\\d{4}-\\d{2}-\\d{2}";  
//    private static String datePattern2 = "^((\\d{2}(([02468][048])|([13579][26]))"  
//            + "[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|"  
//            + "(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?"  
//            + "((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?("  
//            + "(((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?"  
//            + "((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";
    
    private static String datePattern2 = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";
    private static Pattern datePattern = Pattern.compile(datePattern1);
    private static Pattern dateTimePattern = Pattern.compile(datePattern2);
    /** 
     * 判断日期格式:yyyy-MM-dd hh:mm:ss
     *  
     * @param sDate 
     * @return 
     */  
    public static boolean isValidDate(String sDate) {  
        if ((sDate != null)&&sDate.length()==19) {  
//            Matcher match = datePattern.matcher(sDate);  
//            if (match.matches()) {  
                  
        	Matcher match = dateTimePattern.matcher(sDate);  
            return match.matches();  
//            } else {  
//                return false;  
//            }  
        }  
        return false;  
    }  
  
    public static String parseToTrueDateStr(String str){
    	String str1 = str.substring(0, 4);
		String str2 = str.substring(4, 6);
		String str3 = str.substring(6, 8);
		String str4 = str.substring(8, 10);
		String str5 = str.substring(10, 12);
		String str6 = str.substring(12, 14);
		String strdate = str1+"-"+str2+"-"+str3+" "+str4+":"+str5+":"+str6;
		return strdate;
    }
    
    /**
     * 根据字符串自动转成util.Date。
     * 字符串规则：注意年月日前后顺序
     * @param dateStr
     */
    public static Date parseDateAuto(String dateStr){
    	HashMap<String, String> dateRegFormat = new HashMap<String, String>();
        dateRegFormat.put(
            "^\\d{4}\\D+\\d{1,2}\\D+\\d{1,2}\\D+\\d{1,2}\\D+\\d{1,2}\\D+\\d{1,2}\\D*$",
            "yyyy-MM-dd-HH-mm-ss");//2014年3月12日 13时5分34秒，2014-03-12 12:05:34，2014/3/12 12:5:34
        dateRegFormat.put("^\\d{4}\\D+\\d{2}\\D+\\d{2}\\D+\\d{2}\\D+\\d{2}$",
            "yyyy-MM-dd-HH-mm");//2014-03-12 12:05
        dateRegFormat.put("^\\d{4}\\D+\\d{2}\\D+\\d{2}\\D+\\d{2}$",
            "yyyy-MM-dd-HH");//2014-03-12 12
        dateRegFormat.put("^\\d{4}\\D+\\d{2}\\D+\\d{2}$", "yyyy-MM-dd");//2014-03-12
        dateRegFormat.put("^\\d{4}\\D+\\d{2}$", "yyyy-MM");//2014-03
        dateRegFormat.put("^\\d{4}$", "yyyy");//2014
        dateRegFormat.put("^\\d{14}$", "yyyyMMddHHmmss");//20140312120534
        dateRegFormat.put("^\\d{12}$", "yyyyMMddHHmm");//201403121205
        dateRegFormat.put("^\\d{10}$", "yyyyMMddHH");//2014031212
        dateRegFormat.put("^\\d{8}$", "yyyyMMdd");//20140312
        
        DateFormat dateFormat;
        String dateStrReplace;
        Date date = new Date();

        for (String key : dateRegFormat.keySet()) {
            if (Pattern.compile(key).matcher(dateStr).matches()) {
          	  dateFormat = new SimpleDateFormat(dateRegFormat.get(key));
              dateStrReplace = dateStr.replaceAll("\\D+", "-");
              try {
					date = dateFormat.parse(dateStrReplace);
				} catch (ParseException e) {
					e.printStackTrace();
				}
              break;
            }
          }
        return date;
    }
    
    public static void main(String args[]) throws IllegalAccessException, InvocationTargetException
    {
//    	String str ="1990-12-12 13:12:12";
//    	System.out.println(isValidDate(str));
//    	str ="1934-12-12 12:12:61";
//    	System.out.println(isValidDate(str));
//    	str ="1934-13-12 12:12:59";
//    	System.out.println(isValidDate(str));
//    	str ="1934-12-12 24:00:00";
//    	System.out.println(isValidDate(str));
    	parseDateAuto("2014年3月12日 13时5分34秒");
//    	Patient psrc1 = new Patient();
//    	psrc1.setName("gg");
//    	psrc1.setAge("1000");
//    	psrc1.setInPatientDate(Timestamp.valueOf("2010-01-01 01:20:00"));
//    	
//    	Patient psrc2 = new Patient();
//    	//psrc2.setName("mmmmmmm");
//    	psrc2.setAge("2000");
//    	
//    	Patient dest1 = new Patient();
//    	dest1.setName("ssssssss");
//    	Patient dest2 = new Patient();
//    	dest2.setName("mmmmmmmm");
//    	
//    	BeanUtils.copyProperties(dest1, psrc1);
//    	
//    	org.springframework.beans.BeanUtils.copyProperties(psrc2, dest2);
//    	System.out.println("src1=null+1000-----------dest1="+dest1.getName()+"+"+dest1.getAge());
//    	System.out.println("src2=null+2000-----------dest2="+dest2.getName()+"+"+dest2.getAge());
//    	
//    	
//    	Date d = Timestamp.valueOf("2010-01-01 21:20:00");
//    	
//    	
//    	try {
//			String dd = getOnlyTime(d);
//			System.out.println(dd);
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    	
    }
}

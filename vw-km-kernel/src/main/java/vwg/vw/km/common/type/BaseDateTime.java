/*
 * Copyright (c) VW All Rights Reserved.
 *
 * This SOURCE CODE FILE, which has been provided by VW Software as part
 * of an VW Software product for use ONLY by licensed users of the product,
 * includes CONFIDENTIAL and PROPRIETARY information of VW Software.
 *
 * USE OF THIS SOFTWARE IS GOVERNED BY THE TERMS AND CONDITIONS
 * OF THE LICENSE STATEMENT AND LIMITED WARRANTY FURNISHED WITH
 * THE PRODUCT.
 *
 * IN PARTICULAR, YOU WILL INDEMNIFY AND HOLD INPRISE, ITS RELATED
 * COMPANIES AND ITS SUPPLIERS, HARMLESS FROM AND AGAINST ANY CLAIMS
 * OR LIABILITIES ARISING OUT OF THE USE, REPRODUCTION, OR DISTRIBUTION
 * OF YOUR PROGRAMS, INCLUDING ANY CLAIMS OR LIABILITIES ARISING OUT OF
 * OR RESULTING FROM THE USE, MODIFICATION, OR DISTRIBUTION OF PROGRAMS
 * OR FILES CREATED FROM, BASED ON, AND/OR DERIVED FROM THIS SOURCE
 * CODE FILE.
 */

package vwg.vw.km.common.type;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;

import vwg.vw.km.common.exception.BaseRtBaseTypeFailureException;
import vwg.vw.km.common.exception.BaseRtFormatException;
import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;

/**
 * <p>
 * Title: EKM
 * <p>
 * Description : Class description goes here
 * </p>
 * <p>
 * Copyright: VW (c) 2011
 * </p>
 * .
 * 
 * @author Sebri Zouhaier changed by $Author: saidi $
 * @version $Revision: 1.12 $ $Date: 2015/12/07 13:13:31 $
 */
public class BaseDateTime extends BaseHibernateType {
	/** the <code>serialVersionUID</code> instance holder */
	private static final long serialVersionUID = -7460330606745100150L;

	private final Log log = LogManager.get().getLog(BaseDateTime.class);

	/*********************************************************************************************************************
	 * protected constant members
	 ********************************************************************************************************************/

	// **********************
	// allowed string length
	// **********************
	private static int DATE_STRING_SHORT_LEN = 9;

	private static int DATE_STRING_LEN = 10;

	protected static int DATE_TIME_MILLIS_LEN = 17;

	protected static int DATE_TIME_STRING_LEN = 19;

	protected static int DATE_TIME_STRING_MILLIS_LEN = 21;

	protected static final String[] months = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct",
			"Nov", "Dec" };

	public static final long MILISECONDS_PER_SECONDS = 1000L;

	public static final long SECONDS_PER_MINUTE = 60L;

	public static final long MINUTES_PER_HOUR = 60L;

	public static final long HOURS_PER_DAY = 24L;

	public static final long MILISECONDS_PER_MINUTE = 60000L;

	public static final long MILISECONDS_PER_HOUR = 3600000L;

	public static final long MILISECONDS_PER_DAY = 86400000L;

	public static final String TIME_000000000 = "000000000";

	public static final long MILISECONDS_PER_31_DAY_MONTH = 2678400000L;

	public static final long DAYLIGT_OFFSET = 0L; // 3600000L

	// **********************
	// formats
	// **********************
	public static final int NONE = 0;

	public static final int YYYYMMDDHHMMSSMMM = 1;

	public static final int YYYYMMDDHHMMSS = 2;

	public static final int YYYYMMDDHHMM = 3;

	public static final int YYYYMMDD = 4;

	public static final int HHMMSSMMM = 5;

	public static final int HHMMSS = 6;

	public static final int HHMM = 7;

	public static final int HH = 8;

	public static final int MI = 9;

	public static final int SS = 10;

	public static final int DD_MM_YYYY = 11;

	public static final int DD_MM_YY = 12;

	public static final int DDMMMYYYY = 13;

	public static final int DDMMYYYY = 14;

	public static final int DDMMMYY = 15;

	public static final int DDMMYY = 16;

	public static final int YYYY_MIN_MM_MIN_DD_BLNK_HH_COL_MM_COL_SS_PNT_MMM = 17;

	public static final int YYYY_MIN_MM_MIN_DD_BLNK_HH_COL_MM_COL_SS = 18;

	public static final int YYYY_MM_DD_HH_MM_SS_MMM = 19;

	public static final int YYYY_MM_DD = 20;

	public static final int HH_MM_SS = 22;

	public static final int HH_MM = 23;

	public static final int DDMMMYY_MIN_HH_MM_SS = 24;

	private static final int DDMMMYY_MIN_HH_MM = 25;

	public static final int YYYY = 26;

	private static final int YY = 27;

	public static final int MO = 28;

	public static final int DD = 29;

	public static final int YYYY_MIN_MM_MIN_DD = 30;

	private static final int YYMMDD = 31;

	private static final int YYMM = 32;

	private static final int MMYYYY = 34;

	private static final int DD_MM_YYYY_SPC_HH_COL_MM_COL_SS = 35;

	public static final int DD_MM_YYYY_SPC_HH_COL_MM = 36;

	public static final String PNT = ".";

	public static final String COL = ":";

	private static final String SPC = " ";

	private static final String BLNK = "";

	public static final String MIN = "-";

	private static int localUtcOffset;

	private String erroneousInitializer;

	private int thisDateFormat;

	private String thisDateDelimiter;

	private Date thisDate;

	private int[] SQL_TYPES = { Types.DATE };

	public BaseDateTime() {

	}

	public BaseDateTime(String s) {
		set(s);
	}

	public BaseDateTime(Date d) {
		thisDate = d;
		thisDateFormat = YYYYMMDDHHMMSSMMM;
		thisDateDelimiter = BLNK;
	}

	public Date getDate() {
		return thisDate;
	}

	/**
	 * @see BaseType
	 * @return ist intern gesetzt
	 */
	@Override
	public boolean isNull() {
		return (thisDate == null);
	}

	public long getTime() {
		if (!isNull()) {
			return thisDate.getTime();
		}
		throw new BaseRtBaseTypeFailureException("This date is null.");
	}

	/**
	 * creates a new instance of BaseDateTime with System.currentTimeMillis()
	 * 
	 * @return BaseDateTime the created new instance for current time
	 */
	public static BaseDateTime getCurrentDateTime() {
		return new BaseDateTime(new Date(System.currentTimeMillis()));
	}

	/*********************************************************************************************************************
	 * public getters
	 ********************************************************************************************************************/

	/**
	 * return short time format HH:MM
	 * 
	 * @return String
	 */
	public String getCurrentYear() {
		return getCurrentDateTime().getString(YYYY);
	}

	/**
	 * return current month
	 * 
	 * @return String
	 */
	public String getCurrentMonth() {
		return getCurrentDateTime().getString(MO);
	}

	/**
	 * return short time format HH:MM
	 * 
	 * @return String
	 */
	public String getShortTime() {
		return this.getString(HH_MM, ":");
	}

	/**
	 * return long time format HH:MM:SS
	 * 
	 * @return String
	 */
	public String getLongTime() {
		return this.getString(HH_MM_SS, ":");
	}

	/**
	 * return just the date separated by "."
	 * 
	 * @return String
	 */
	public String getDateSeparatedPt() {
		return this.getString(DD_MM_YYYY, ".");
	}

	/**
	 * @see getString(int, String)
	 * @param format
	 *            das gew�nschte Format
	 * @return den formatierten String
	 */
	public String getString(int format) {
		return this.getString(format, thisDateDelimiter);
	}

	/**
	 * @see getString(int, String)
	 * @return den formatierten String
	 */
	@Override
	public String getString() {
		return this.getString(thisDateFormat, thisDateDelimiter);
	}

	/**
	 * Diese Methode liefert das Datum mit dem gew�nschten Format
	 * 
	 * @param format
	 *            das gew�nschte Format
	 * @param del
	 *            der gew�nschte Delimiter
	 * @return den formatierten String
	 */
	public String getString(int format, String del) {

		String dateTimeString, returnString = "";
		int pointIndex;
		DateFormat dateFormat;
		String yyyy, yy, mo, mmm, dd, hh, ss, mi, mmmm;
		long inMillis;
		Long seconds, millis;

		if (erroneousInitializer != null) {
			returnString = erroneousInitializer;
		} else if (thisDate == null) {
			returnString = "";
		} else {
			try {
				Date defaultDate = null;
				if (TimeZone.getDefault().inDaylightTime(thisDate)) {
					defaultDate = new Date(thisDate.getTime() - localUtcOffset - DAYLIGT_OFFSET);
				} else {
					defaultDate = new Date(thisDate.getTime() - localUtcOffset);
				}
				dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, Locale.GERMANY);
				dateTimeString = dateFormat.format(defaultDate);

				// der Tag
				pointIndex = dateTimeString.indexOf(".");
				dd = dateTimeString.substring(0, pointIndex);

				// Ersetzten von "01" statt "1"
				if (dd.length() < 2) {
					dd = "0" + dd;
				}
				dateTimeString = dateTimeString.substring(pointIndex + 1);

				// der Monat (einfach)
				pointIndex = dateTimeString.indexOf(".");
				mo = dateTimeString.substring(0, pointIndex);
				if (mo.length() < 2) {
					mo = "0" + mo;
				}

				// der Monat (vollst�ndig)
				mmm = nameOfMonth(Integer.parseInt(mo));

				dateTimeString = dateTimeString.substring(pointIndex + 1);

				// das Jahr
				yy = dateTimeString.substring(2, 4);
				yyyy = dateTimeString.substring(0, 4);
				dateTimeString = dateTimeString.substring(5);

				// die Zeit
				hh = dateTimeString.substring(0, 2);
				mi = dateTimeString.substring(3, 5);

				inMillis = thisDate.getTime() % (SECONDS_PER_MINUTE * MILISECONDS_PER_SECONDS);
				seconds = new Long(inMillis / MILISECONDS_PER_SECONDS);
				millis = new Long(inMillis - (seconds.longValue() * MILISECONDS_PER_SECONDS));

				ss = seconds.toString(); // java.toString
				for (int i = ss.length(); i < 2; i++) {
					ss = "0" + ss;
				}

				mmmm = millis.toString(); // java.toString
				for (int j = mmmm.length(); j < 3; j++) {
					mmmm = "0" + mmmm;
				}

				switch (format) {
				case YYYYMMDDHHMMSSMMM: {
					returnString = yyyy + mo + dd + hh + mi + ss + mmmm;
					break;
				}
				case HH_MM: {
					returnString = hh + ":" + mi;
					break;
				}
				case HH_MM_SS: {
					returnString = hh + del + mi + del + ss;
					break;
				}
				case YYYYMMDDHHMMSS: {
					returnString = yyyy + mo + dd + hh + mi + ss;
					break;
				}
				case YYYYMMDDHHMM: {
					returnString = yyyy + mo + dd + hh + mi;
					break;
				}
				case YYYYMMDD: {
					returnString = yyyy + mo + dd;
					break;
				}
				case DD_MM_YYYY: {
					returnString = dd + del + mo + del + yyyy;
					break;
				}
				case DD_MM_YY: {
					returnString = dd + del + mo + del + yy;
					break;
				}
				case DDMMMYYYY: {
					returnString = dd + mmm + yyyy;
					break;
				}
				case DDMMYYYY: {
					returnString = dd + mo + yyyy;
					break;
				}
				case DDMMMYY: {
					returnString = dd + mmm + yy;
					break;
				}
				case DDMMYY: {
					returnString = dd + mo + yy;
					break;
				}
				case YYMMDD: {
					returnString = yy + mo + dd;
					break;
				}
				case YYYY_MM_DD_HH_MM_SS_MMM: {
					returnString = yyyy + del + mo + del + dd + del + hh + del + mi + del + ss + del + mmmm;
					break;
				}
				case YYYY_MM_DD: {
					returnString = yyyy + del + mo + del + dd;
					break;
				}
				case DDMMMYY_MIN_HH_MM_SS: {
					returnString = dd + mmm + yy + MIN + hh + del + mi + del + ss;
					break;
				}
				case DDMMMYY_MIN_HH_MM: {
					returnString = dd + mmm + yy + MIN + hh + del + mi;
					break;
				}
				case YYYY: {
					returnString = yyyy;
					break;
				}
				case YY: {
					returnString = yy;
					break;
				}
				case HH: {
					returnString = hh;
					break;
				}
				case MI: {
					returnString = mi;
					break;
				}
				case YYMM: {
					returnString = yy + mo;
					break;
				}
				case MO: {
					returnString = mo;
					break;
				}
				case DD: {
					returnString = dd;
					break;
				}
				case YYYY_MIN_MM_MIN_DD_BLNK_HH_COL_MM_COL_SS_PNT_MMM: {
					returnString = yyyy + MIN + mo + MIN + dd + SPC + hh + COL + mi + COL + ss + PNT + mmmm;
					break;
				}
				case YYYY_MIN_MM_MIN_DD_BLNK_HH_COL_MM_COL_SS: {
					returnString = yyyy + MIN + mo + MIN + dd + SPC + hh + COL + mi + COL + ss;
					break;
				}

				case YYYY_MIN_MM_MIN_DD: {
					returnString = yyyy + MIN + mo + MIN + dd;
					break;
				}

				case DD_MM_YYYY_SPC_HH_COL_MM_COL_SS: {
					returnString = dd + del + mo + del + yyyy + SPC + hh + COL + mi + COL + ss;
					break;
				}
				case DD_MM_YYYY_SPC_HH_COL_MM: {
					returnString = dd + del + mo + del + yyyy + SPC + hh + COL + mi;
					break;
				}
				default:
					throw new BaseRtFormatException("BaseDate:getString(): Unimplemented dateformat used.");
				} // END SWITCH
			} catch (NumberFormatException e) { // END TRY
				throw new BaseRtFormatException("BaseDate:getString(): wrong number format " + e.getMessage(), e);
			} catch (StringIndexOutOfBoundsException e) {
				throw new BaseRtFormatException("BaseDate:getString(): string index out of bounds " + e.getMessage(),
						e);
			}
		} // END ELSE
		return returnString;
	}

	/**
	 * @todo
	 * @param s
	 */
	protected void set(String s) {
		SimpleDateFormat baseFormatStr;
		if (s == null || s.length() == 0) {
			thisDate = null;
		} else {
			if (s.length() == DATE_STRING_SHORT_LEN) { // 9
				// Datum soll gesetzt werden
				try {

					baseFormatStr = new SimpleDateFormat("ddMMMyyyy");

					// damit der Parser, das Datum richtig interpretiert, mu�
					// folgende Setting
					// durchgef�hrt werden
					// Beispiel: date [20011231]=Mon Dec 31 00:00:00 GMT+01:00
					// 2001
					baseFormatStr.setLenient(false);

					thisDate = baseFormatStr.parse(s);
					thisDateFormat = DDMMMYYYY;
					thisDateDelimiter = BLNK;
				} catch (ParseException e) {
					throw new BaseRtFormatException("invalid format", e);
				}
			} else if (s.length() == DATE_STRING_LEN) { // 10
				// Das Datum und die Zeit sollen gesetzt werden

				try {

					baseFormatStr = new SimpleDateFormat("dd.MM.yyyy");
					// damit der Parser, das Datum richtig interpretiert, mu�
					// folgende Setting
					// durchgef�hrt werden
					// Beispiel: date [20011231]=Mon Dec 31 00:00:00 GMT+01:00
					// 2001
					baseFormatStr.setLenient(false);
					thisDate = baseFormatStr.parse(s);
					thisDateFormat = YYYYMMDD;
					thisDateDelimiter = BLNK;
				} catch (ParseException e) {
				}
			}
			/*
			 * else if (s.length() == DATE_STRING_LEN) { // 10 // Das Datum und die Zeit sollen gesetzt werden
			 * 
			 * try { baseFormatStr = new SimpleDateFormat("yyyy-MM-dd"); // damit der Parser, das Datum richtig
			 * interpretiert, mu� // folgende Setting // durchgef�hrt werden // Beispiel: date [20011231]=Mon Dec 31
			 * 00:00:00 GMT+01:00 // 2001 baseFormatStr.setLenient(false); thisDate = baseFormatStr.parse(s);
			 * thisDateFormat = YYYYMMDD; thisDateDelimiter = BLNK; } catch (ParseException e) { } }
			 */
			else if (s.length() == DATE_TIME_STRING_LEN) { // 19
				// Das Datum und die Zeit sollen gesetzt werden

				try {

					baseFormatStr = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
					SimpleDateFormat baseFormatStr1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					// damit der Parser, das Datum richtig interpretiert, mu�
					// folgende Setting
					// durchgef�hrt werden
					// Beispiel: date [20011231]=Mon Dec 31 00:00:00 GMT+01:00
					// 2001
					baseFormatStr.setLenient(false);
					baseFormatStr1.setLenient(false);
					try {
						thisDate = baseFormatStr.parse(s);
					} catch (Exception e) {
						thisDate = baseFormatStr1.parse(s);
					}
					thisDateFormat = YYYYMMDDHHMMSS;
					thisDateDelimiter = BLNK;
				} catch (ParseException e) {
					throw new BaseRtFormatException("invalid format", e);
				}
			} else if (s.length() == DATE_TIME_MILLIS_LEN) { // 17
				// Das Datum und die Zeit sollen gesetzt werden
				try {

					baseFormatStr = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
					// damit der Parser, das Datum richtig interpretiert, mu�
					// folgende Setting
					// durchgef�hrt werden
					// Beispiel: date [20011231]=Mon Dec 31 00:00:00 GMT+01:00
					// 2001
					baseFormatStr.setLenient(false);
					thisDate = baseFormatStr.parse(s);
					thisDateFormat = YYYYMMDDHHMMSSMMM;
					thisDateDelimiter = BLNK;
				}

				catch (ParseException e) {
					throw new BaseRtFormatException("invalid format", e);
				}
			}
			// 2006-08-01 00:00:00.0
			else if (s.length() == DATE_TIME_STRING_MILLIS_LEN) { // 21
				// Das Datum und die Zeit sollen gesetzt werden

				try {

					baseFormatStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
					// damit der Parser, das Datum richtig interpretiert, mu�
					// folgende Setting
					// durchgef�hrt werden
					// Beispiel: date [20011231]=Mon Dec 31 00:00:00 GMT+01:00
					// 2001
					baseFormatStr.setLenient(false);
					thisDate = baseFormatStr.parse(s);
					thisDateFormat = YYYYMMDDHHMMSSMMM;
					thisDateDelimiter = BLNK;
				} catch (ParseException e) {
					throw new BaseRtFormatException("invalid format", e);
				}
			} else if (s.length() == 5) { // 5
				// Das Datum und die Zeit sollen gesetzt werden

				try {

					baseFormatStr = new SimpleDateFormat("HH:mm");

					baseFormatStr.setLenient(false);
					thisDate = baseFormatStr.parse(s);
					thisDateFormat = HH_MM;
					thisDateDelimiter = BLNK;
				} catch (ParseException e) {
					throw new BaseRtFormatException("invalid format", e);
				}
			} else if (s.length() == 7) { // 7
				// Das Datum und die Zeit sollen gesetzt werden

				try {

					baseFormatStr = new SimpleDateFormat("MM.yyyy");

					baseFormatStr.setLenient(false);
					thisDate = baseFormatStr.parse(s);
					thisDateFormat = MMYYYY;
					thisDateDelimiter = BLNK;
				} catch (ParseException e) {
					throw new BaseRtFormatException("invalid format", e);
				}
			} else if (s.length() == 8) { // 8
				// Das Datum und die Zeit sollen gesetzt werden

				try {

					baseFormatStr = new SimpleDateFormat("HH:mm:ss");

					baseFormatStr.setLenient(false);
					thisDate = baseFormatStr.parse(s);
					thisDateFormat = HH_MM_SS;
					thisDateDelimiter = BLNK;
				} catch (ParseException e) {
					throw new BaseRtFormatException("invalid format", e);
				}
			} else {
				erroneousInitializer = s;
				throw new BaseRtFormatException("invalid format: " + s);
			}
		}
	}

	/**
	 * 
	 * @return int hash code
	 */
	@Override
	public int hashCode() {
		if (thisDate == null) {
			return NULL_HASH_VALUE;
		} else {
			return thisDate.hashCode();
		}
	}

	/**
	 * 
	 * @return int BaseType reference
	 */
	@Override
	public int getBaseType() {
		return BaseType.TYPE_DATETIME;
	}

	/**
	 * Name des Monats nach Reihenfolge
	 * 
	 * @param index
	 *            der Index f�r den gesuchten Monat
	 * @return der gefundene Monat als String
	 */
	protected String nameOfMonth(int index) {
		return months[index - 1];
	}

	/**
	 * Hibernate implementation Bei extention of this class, this method should be overritten
	 * 
	 * @param value
	 *            String
	 * @return new BaseDateTime instance
	 */
	@Override
	public BaseHibernateType getNewInstance(String value) {
		return new BaseDateTime(value);
	}

	/**
	 * Hibernate implementation
	 * 
	 * @return int[] list of allowed sql types
	 */
	@Override
	public int[] sqlTypes() {
		return SQL_TYPES;
	}

	/**
	 * Hibernate implementation
	 * 
	 * @param x
	 *            Object
	 * @param y
	 *            Object
	 * @return boolean
	 */
	@Override
	public boolean equals(Object x, Object y) {
		if (x == null && y == null) {
			return true;
		}
		if (x == null) {
			return false;
		}
		if (y == null) {
			return false;
		}
		boolean returnValue = ((BaseDateTime) x).equals(y);
		if (log.isDebugEnabled()) {
			log.debug("params: " + x + ",  " + y + ", returnValue : " + returnValue);
		}
		return returnValue;
	}

	@Override
	public boolean equals(Object other) {
		if ((this == other)) {
			return true;
		}
		if ((other == null)) {
			return false;
		}
		if (!(other instanceof BaseDateTime)) {
			return false;
		}
		BaseDateTime castOther = (BaseDateTime) other;

		return ((this.getDate() == castOther.getDate()) || (this.getDate() != null && castOther.getDate() != null
				&& this.getDate().equals(castOther.getDate())));
	}

	@Override
	public Class<BaseDateTime> returnedClass() {
		return BaseDateTime.class;
	}

	/**
	 * Hibernate implementation
	 * 
	 * @param statement
	 *            PreparedStatement
	 * @param value
	 *            Object
	 * @param index
	 *            int
	 * @throws HibernateException
	 * @throws SQLException
	 */
	@Override
	public void nullSafeSet(PreparedStatement statement, Object value, int index,
			SharedSessionContractImplementor session) throws HibernateException, SQLException {
		if (log.isDebugEnabled()) {
			log.debug("getString(): " + getString() + ", object: " + value + ", index : " + index);
		}
		if (value == null || (value instanceof BaseDateTime && ((BaseDateTime) value).isNull())) {
			statement.setNull(index, sqlTypes()[0]);
		} else {
			statement.setTimestamp(index, new java.sql.Timestamp(((BaseDateTime) value).getTime()));
		}
	}

	@Override
	public String toString() {
		String returnString;
		if (thisDate != null) {
			returnString = getString(BaseDateTime.DD_MM_YYYY_SPC_HH_COL_MM_COL_SS, PNT);
		} else {
			returnString = BaseType.EMPTY_STRING;
		}
		return returnString;
	}

	/**
	 * Get the next day after a given date
	 * 
	 * @param target
	 *            a given date
	 * @return the next valid day object after the date
	 */
	static public Date getNextDay(Date target) {

		Date next = new Date(target.getTime() + (24 * 3600 * 1000));
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(next);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	static public Date getPreviousDay(Date target) {

		Date next = new Date(target.getTime() - (24 * 3600 * 1000));
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(next);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public BaseDateTime getNextDay() {

		Date next = new Date(thisDate.getTime() + (24 * 3600 * 1000));
		return new BaseDateTime(next);
	}

	/**
	 * Adding two Date
	 * 
	 * @param date
	 *            BaseDateTime
	 * @return BaseDateTime
	 */
	public BaseDateTime addDate(BaseDateTime date) {
		Date calculatedTime = new Date();
		long calculated = thisDate.getTime() + date.getTime();
		calculatedTime.setTime(calculated);
		return new BaseDateTime(calculatedTime);
	}

	/**
	 * Add seconds to a specific date
	 * 
	 * @param seconds
	 *            int
	 * @return BaseDateTime
	 */
	public BaseDateTime addSeconds(int seconds) {
		Date calculatedTime = new Date();
		long calculated = thisDate.getTime() + (seconds * 1000);
		calculatedTime.setTime(calculated);
		return new BaseDateTime(calculatedTime);
	}

	/**
	 * Add minutes to a specific date
	 * 
	 * @param minutes
	 *            int
	 * @return BaseDateTime
	 */
	public BaseDateTime addMinutes(int minutes) {
		Date calculatedTime = new Date();
		long calculated = thisDate.getTime() + (minutes * 60 * 1000);
		calculatedTime.setTime(calculated);
		return new BaseDateTime(calculatedTime);
	}

	/**
	 * Add years to a specific date
	 * 
	 * @param years
	 *            int
	 * @return BaseDateTime
	 */
	public BaseDateTime addYears(int years) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(thisDate);
		calendar.add(Calendar.YEAR, years);
		return new BaseDateTime(calendar.getTime());
	}

	/**
	 * Add months to a specific date
	 * 
	 * @param months
	 *            int
	 * @return BaseDateTime
	 */
	public BaseDateTime addMonths(int months) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(thisDate);
		calendar.add(Calendar.MONTH, months);
		return new BaseDateTime(calendar.getTime());
	}

	/**
	 * if the given date is greate than 14 day from current date then it's a valid mutation date
	 * 
	 * @return boolean
	 */
	public boolean isValidMutationDate() {
		Date calculatedTime = new Date();
		long calculated = thisDate.getTime() - calculatedTime.getTime();
		if (calculated < (14 * 24 * 60 * 60 * 1000)) {
			return false;
		}
		return true;
	}

	public static void main(String[] args) {
		BaseDateTime baseDateTime = new BaseDateTime("15-12-03 00:00:00");
		System.out.println(baseDateTime.getDate());
		BaseDateTime baseDateTime1 = new BaseDateTime("05.05.2011 08:47:29");
		System.out.println(baseDateTime1.getDate());
	}
}

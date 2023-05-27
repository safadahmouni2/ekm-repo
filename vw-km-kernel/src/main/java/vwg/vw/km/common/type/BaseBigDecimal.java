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

import java.math.BigDecimal;
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import vwg.vw.km.common.exception.BaseRtFormatException;
import vwg.vw.km.common.exception.BaseRtMaxLengthExceededException;
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
 * @version $Revision: 1.9 $ $Date: 2013/09/19 13:38:28 $
 */
public class BaseBigDecimal extends BaseHibernateType {

	/** the <code>serialVersionUID</code> instance holder */
	private static final long serialVersionUID = -4902050658001659457L;

	private final Log log = LogManager.get().getLog(BaseBigDecimal.class);

	private int[] SQL_TYPES = { Types.NUMERIC };

	// Alle Konstanten, die f�r das runden von Zahlen ben�tigt werden.

	public final static int ROUND_CEILING = BigDecimal.ROUND_CEILING;

	public final static int ROUND_DOWN = BigDecimal.ROUND_DOWN;

	public final static int ROUND_FLOOR = BigDecimal.ROUND_FLOOR;

	public final static int ROUND_HALF_DOWN = BigDecimal.ROUND_HALF_DOWN;

	public final static int ROUND_HALF_EVEN = BigDecimal.ROUND_HALF_EVEN;

	public final static int ROUND_HALF_UP = BigDecimal.ROUND_HALF_UP;

	public final static int ROUND_UNNECESSARY = BigDecimal.ROUND_UNNECESSARY;

	public final static int ROUND_UP = BigDecimal.ROUND_UP;

	DecimalFormat formater = new DecimalFormat("#0.00");

	/*********************************************************************************************************************
	 * protected members
	 ********************************************************************************************************************/

	/**
	 * Fehlerhafter Initialisierer. Falls ein Basistyp mit einem ung�ltigen Wert instanziiert wird, wird dieser Wert in
	 * dieser Variable gespeichert und dann vom getString() zur�ckgeliefert.
	 * 
	 */
	protected String erroneousInitializer = null;

	/**
	 * Membervariable
	 */
	protected BigDecimal baseBigDecimal;

	/*********************************************************************************************************************
	 * public constructors
	 ********************************************************************************************************************/

	public BaseBigDecimal() {
	}

	/**
	 * 
	 * @param d
	 *            der zu allokierende BaseBigDecimal
	 */
	public BaseBigDecimal(BaseBigDecimal d) {
		set(d);
	}

	/**
	 * in this case replacment of . and , should be done
	 * 
	 * @param s
	 *            der zu allokierende String
	 */
	public BaseBigDecimal(String s) {
		set(s);
	}

	/**
	 * 
	 * @param d
	 *            Number is the superclass of classes BigDecimal, BigInteger, Byte, Double, Float, Integer, Long, and
	 *            Short.
	 */
	public BaseBigDecimal(Number d) {
		set(d);
	}

	/*********************************************************************************************************************
	 * public getters
	 ********************************************************************************************************************/

	/**
	 * Liefert die intern verwendete BigDecimal-Membervariable zur�ck. Ist diese null wird ein leerer String - ""
	 * zur�ckgeliefert.
	 * 
	 * @return den String
	 */
	@Override
	public String getString() {
		String returnString;

		if (erroneousInitializer != null) {
			returnString = erroneousInitializer;
		} else if (baseBigDecimal == null) {
			returnString = BaseType.EMPTY_STRING;
		} else {
			returnString = (formater.format(baseBigDecimal.doubleValue())).replace(",", ".");
		}
		return returnString;
	}

	/**
	 * @param i
	 *            das zu vergleichende Objekt
	 * @return das Ergebnis des Vergleichs
	 */
	public int compareTo(BaseType i) {
		int result;

		if ((baseBigDecimal == null) && i.isNull()) {
			result = 0;
		} else if (baseBigDecimal == null) {
			result = -1;
		} else if (i.isNull()) {
			result = 1;
		} else {
			result = compareTo((BaseBigDecimal) i);
		}
		return result;
	}

	/**
	 * @param d
	 *            das zu vergleichende Objekt
	 * @return das Ergebnis des Vergleichs
	 */
	public int compareTo(BaseBigDecimal d) {
		int result;

		if ((baseBigDecimal == null) && (d == null || d.isNull())) {
			result = 0;
		} else if (baseBigDecimal == null) {
			result = -1;
		} else if (d == null || d.isNull()) {
			result = 1;
		} else {
			result = baseBigDecimal.compareTo(d.value());
		}
		return result;
	}

	/**
	 * @param o
	 *            das zu vergleichende Objekt
	 * @return das Ergebnis des Vergleichs
	 */
	public int compareTo(Object o) {
		return compareTo((BaseBigDecimal) o);
	}

	/*********************************************************************************************************************
	 * public others
	 ********************************************************************************************************************/

	/**
	 * @see com.thinktank.framework.common.type.BaseType
	 * @return den BaseType
	 */
	@Override
	public int getBaseType() {
		return BaseType.TYPE_BIGDECIMAL;
	}

	/**
	 * @see java.math.BigDecimal
	 * @return einen neuen BaseBigDecimal
	 */
	public BaseBigDecimal abs() {
		return new BaseBigDecimal(baseBigDecimal.abs());
	}

	/**
	 * @see java.math.BigDecimal
	 * @param val
	 *            der zu addierende BaseBigDecimal
	 * @return einen neuen BaseBigDecimal
	 */
	public BaseBigDecimal add(BaseBigDecimal val) {
		return new BaseBigDecimal(baseBigDecimal.add(val.value()));
	}

	/**
	 * @see java.math.BigDecimal
	 * @param val
	 *            der zu subtrahierende BaseBigDecimal
	 * @return einen neuen BaseBigDecimal
	 */
	public BaseBigDecimal subtract(BaseBigDecimal val) {
		return new BaseBigDecimal(baseBigDecimal.subtract(val.value()));
	}

	/**
	 * @see java.math.BigDecimal
	 * @param val
	 *            der zu multiplizierende BaseBigDecimal
	 * @return einen neuen BaseBigDecimal
	 */
	public BaseBigDecimal multiply(BaseBigDecimal val) {
		return new BaseBigDecimal(baseBigDecimal.multiply(val.value()));
	}

	/**
	 * @see java.math.BigDecimal
	 * @param val
	 *            der zu multiplizierende BaseLong
	 * @return einen neuen BaseBigDecimal
	 */
	public BaseBigDecimal multiply(Long val) {
		return multiply(new BaseBigDecimal(val.toString()));
	}

	/**
	 * @see java.math.BigDecimal
	 * @param val
	 *            der zu dividierende BaseBigDecimal
	 * @return einen neuen BaseBigDecimal
	 */
	public BaseBigDecimal divide(BaseBigDecimal val) {
		return new BaseBigDecimal(baseBigDecimal.divide(val.value(), ROUND_UNNECESSARY));
	}

	/**
	 * @see java.math.BigDecimal
	 * @param val
	 *            der zu dividierende BaseBigDecimal
	 * @param roundingMode
	 *            die Rundungsart
	 * @return einen neuen BaseBigDecimal
	 */
	public BaseBigDecimal divide(BaseBigDecimal val, int roundingMode) {
		return new BaseBigDecimal(baseBigDecimal.divide(val.value(), roundingMode));
	}

	/**
	 * @see java.math.BigDecimal
	 * @param val
	 *            der zu dividierende BaseBigDecimal
	 * @param scale
	 *            die Skalierung (Anzahl der Nachkommastellen)
	 * @param roundingMode
	 *            die Rundungsart
	 * @return einen neuen BaseBigDecimal
	 */
	public BaseBigDecimal divide(BaseBigDecimal val, int scale, int roundingMode) {
		return new BaseBigDecimal(baseBigDecimal.divide(val.value(), scale, roundingMode));
	}

	/**
	 * @see java.math.BigDecimal
	 * @param val
	 *            der zu dividierende BaseLong
	 * @return einen neuen BaseBigDecimal
	 */
	public BaseBigDecimal divide(Long val) {
		return divide(new BaseBigDecimal(val.toString()));
	}

	/**
	 * @see java.math.BigDecimal
	 * @param val
	 *            der zu dividierende BaseLong
	 * @param roundingMode
	 *            die Rundungsart
	 * @return einen neuen BaseBigDecimal
	 */
	public BaseBigDecimal divide(Long val, int roundingMode) {
		return divide(new BaseBigDecimal(val.toString()), roundingMode);
	}

	/**
	 * @see java.math.BigDecimal
	 * @param val
	 *            der zu dividierende BaseLong
	 * @param scale
	 *            die Skalierung (Anzahl der Nachkommastellen)
	 * @param roundingMode
	 *            die Rundungsart
	 * @return einen neuen BaseBigDecimal
	 */
	public BaseBigDecimal divide(Long val, int scale, int roundingMode) {
		return divide(new BaseBigDecimal(val.toString()), scale, roundingMode);
	}

	/**
	 * @see java.math.BigDecimal
	 * @return einen neuen BaseBigDecimal
	 */
	public BaseBigDecimal negate() {
		return new BaseBigDecimal(value().negate());
	}

	/**
	 * @see java.math.BigDecimal
	 * @return das signum
	 */
	public int signum() {
		return value().signum();
	}

	/**
	 * @see java.math.BigDecimal
	 * @param val
	 *            der BaseBigDecimal
	 * @return einen neuen BaseBigDecimal
	 */
	public BaseBigDecimal min(BaseBigDecimal val) {
		return new BaseBigDecimal(baseBigDecimal.min(val.value()));
	}

	/**
	 * @see java.math.BigDecimal
	 * @param val
	 *            der BaseBigDecimal
	 * @return einen neuen BaseBigDecimal
	 */
	public BaseBigDecimal max(BaseBigDecimal val) {
		return new BaseBigDecimal(baseBigDecimal.max(val.value()));
	}

	/**
	 * @see com.thinktank.framework.common.type.BaseType
	 * @return intern gesetzt?
	 */
	@Override
	public boolean isNull() {
		return (baseBigDecimal == null);
	}

	/**
	 * @see com.thinktank.framework.common.type.BaseType
	 * @param d
	 *            das zu vergleichende Objekt
	 * @return das Ergebnis des Vergleichs
	 */
	@Override
	public boolean equals(Object d) {
		if (log.isDebugEnabled()) {
			log.debug("param: " + d);
		}
		return equals((BaseBigDecimal) d);
	}

	/**
	 * @param d
	 *            das zu vergleichende Objekt
	 * @return das Ergebnis des Vergleichs
	 */
	public boolean equals(BaseBigDecimal d) {
		if (log.isDebugEnabled()) {
			log.debug("param: " + d);
		}
		boolean result;
		// PTS-Problem_22602 : ZS: Fix the null check in the equals() method of BaseBigDecimal class
		if (isNull() && (d == null || d.isNull())) {
			result = true;
		} else if (isNull()) {
			result = false;
		} else if (d == null || d.isNull()) {
			result = false;
		} else {
			// equals-Methode ber�cksichtigt Anzahl der Nachkommastellen,
			// d.h 7.0 != 7.00
			result = baseBigDecimal.compareTo(d.value()) == 0;
		}
		return result;
	}

	/**
	 * @see com.thinktank.framework.common.type.BaseType
	 * @param d
	 *            das zu vergleichende Objekt
	 * @return das Ergebnis des Vergleichs
	 */
	public boolean equals(BaseType d) {
		if (log.isDebugEnabled()) {
			log.debug("param: " + d);
		}
		return equals((BaseBigDecimal) d);
	}

	/**
	 * @see com.thinktank.framework.common.type.BaseType
	 * @return den HashCode des Objekts
	 */
	@Override
	public int hashCode() {
		if (log.isDebugEnabled()) {
			log.debug("hashCode entering ...");
		}
		if (baseBigDecimal == null) {
			return NULL_HASH_VALUE;
		} else {
			return baseBigDecimal.hashCode();
		}
	}

	/**
	 * Liefert die intern verwendete BigDecimal-Membervariable zur�ck. Ist diese null wird ein leerer String - ""
	 * zur�ckgeliefert.
	 * 
	 * @return den intern verwendeten BigDecimal als String
	 */
	@Override
	public String toString() {
		String returnString;

		if (baseBigDecimal != null) {
			returnString = formater.format(baseBigDecimal.doubleValue()).replace(".", ",");
		} else {
			returnString = BaseType.EMPTY_STRING;
		}

		return returnString;
	}

	/**
	 * @return der intern verwendete BigDecimal
	 */
	public BigDecimal value() {
		return baseBigDecimal;
	}

	/**
	 * Der Defaultwert f�r die kleinste zul�ssige Zahl. In der abgeleiteten Klasse sollte diese Methode mit dem
	 * tats�chlich erwarteten Ergebnis �berschrieben werden.
	 * 
	 * @return der kleinste zul�ssige Wert
	 */
	public double minValue() {
		return -Double.MAX_VALUE;
	}

	/**
	 * Der Defaultwert f�r die gr��te zul�ssige Zahl. In der abgeleiteten Klasse sollte diese Methode mit dem
	 * tats�chlich erwarteten Ergebnis �berschrieben werden.
	 * 
	 * @return der gr��te zul�ssige Wert
	 */
	public double maxValue() {
		return Double.MAX_VALUE;
	}

	/*********************************************************************************************************************
	 * protected setters
	 ********************************************************************************************************************/

	/**
	 * @param d
	 *            - der zusetzende BigDecimal-Wert
	 * @exception BaseUcMaxLengthExceededException
	 *                - d au�erhalb der erlaubten Grenzen
	 */
	protected void set(Number d) {
		if (d == null) {
			baseBigDecimal = null;
		} else {
			if ((d.doubleValue() < minValue()) || (d.doubleValue() > maxValue())) {
				erroneousInitializer = d.toString();
				throw new BaseRtMaxLengthExceededException("BaseBigDecimal: set(BigDecimal): value out of range " + d);
			}
			baseBigDecimal = new BigDecimal(d.toString());
		}
	}

	/**
	 * @param d
	 *            - der zusetzende BaseBigDecimal-Wert
	 * @exception BaseUcMaxLengthExceededException
	 *                - d au�erhalb der erlaubten Grenzen
	 */
	protected void set(BaseBigDecimal d) {
		if (d == null) {
			baseBigDecimal = null;
		} else {
			if ((d.value().doubleValue() < minValue()) || (d.value().doubleValue() > maxValue())) {
				erroneousInitializer = d.getString();
				throw new BaseRtMaxLengthExceededException(
						"BaseBigDecimal: set(BaseBigDecimal): value out of range " + d);
			}
			baseBigDecimal = d.value();
		}
	}

	/**
	 * @param s
	 *            - der zusetzende String
	 * @exception BaseUcMaxLengthExceededException
	 *                - s au�erhalb der erlaubten Grenzen
	 * @exception BaseUcNumberFormatException
	 *                - s kann nicht in Integer gewandelt werden
	 */
	protected void set(String s) {
		String orig = s;
		if (log.isDebugEnabled()) {
			log.debug("param : " + s);
		}
		// Wenn das Parameter NULL ist oder ein leeres String, wird intern als null behandelt
		if (s == null || s.trim().equals("")) {
			baseBigDecimal = null;
		} else {
			try {

				// allowed format ###0,00
				if (s.indexOf(".") > -1 && s.indexOf(",") > -1) {
					throw new BaseRtFormatException("BaseBigDecimal:set(" + s + "): wrong number format " + orig);
				}
				s = s.replaceAll(",", ".");
				s = s.trim();
				BigDecimal tempBigDecimal = new BigDecimal(s);
				if ((tempBigDecimal.doubleValue() < minValue()) || (tempBigDecimal.doubleValue() > maxValue())) {
					erroneousInitializer = s;
					throw new BaseRtMaxLengthExceededException("BaseBigDecimal: set(String): value out of range " + s);
				}
				baseBigDecimal = new BigDecimal(s);
			} catch (NumberFormatException e) {
				erroneousInitializer = s;
				throw new BaseRtFormatException("BaseBigDecimal:set(" + s + "): wrong number format " + orig);
			}
		}
	}

	/**
	 * @return double value
	 */
	public double doubleValue() {
		if (baseBigDecimal == null) {
			throw new RuntimeException("baseBigDecimal == null, Not yet implemented.");
		}
		return baseBigDecimal.doubleValue();
	}

	/**
	 * @return long value
	 */
	public long longValue() {
		if (baseBigDecimal == null) {
			throw new RuntimeException("baseBigDecimal == null, Not yet implemented.");
		}
		return baseBigDecimal.longValue();
	}

	/**
	 * Hibernate implementation Bei extention of this class, this method should be overritten
	 * 
	 * @param value
	 *            String
	 * @return new BaseBigDecimal instance
	 */
	@Override
	public BaseHibernateType getNewInstance(String value) {
		return new BaseBigDecimal(value.replace(".", ","));
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
		boolean returnValue = ((BaseBigDecimal) x).equals(y);
		if (log.isDebugEnabled()) {
			log.debug("params: " + x + ",  " + y + ", returnValue : " + returnValue);
		}
		return returnValue;
	}

	@Override
	public Class<BaseBigDecimal> returnedClass() {
		return BaseBigDecimal.class;
	}

	/**
	 * return partie enti貥 vwg x
	 * 
	 * @return den String
	 */
	public String toStringWithoutComma() {
		String returnString;

		if (erroneousInitializer != null) {
			returnString = erroneousInitializer;
		} else if (baseBigDecimal == null) {
			returnString = BaseType.EMPTY_STRING;
		} else {

			BaseBigDecimal bbd = new BaseBigDecimal(baseBigDecimal);
			String bbS = bbd.getStringFormatted();
			int indexComma = bbS.indexOf(",");
			if (bbS.equals("0,00")) {
				returnString = "0";
			} else {

				if (indexComma > 0) {
					returnString = bbS.substring(0, indexComma);
				} else {
					returnString = bbd.getStringFormatted();
				}
			}
		}
		return returnString;
	}

	/**
	 * Liefert die intern verwendete BigDecimal-Membervariable zur�ck. Ist diese null wird ein leerer String - ""
	 * zur�ckgeliefert.
	 * 
	 * @return den intern verwendeten BigDecimal als String
	 */
	public String getStringFormatted() {
		String returnString;
		String cheresult = "";
		if (baseBigDecimal != null) {
			String chdep = formater.format(baseBigDecimal.doubleValue()).replace(".", ",");

			int indexComma = chdep.indexOf(",");
			int index = indexComma;
			if (indexComma > 0) {
				while (index > 3) {
					String cheinter = chdep.substring(index - 3, index);
					index = index - 3;
					String ch = "." + cheinter;
					cheresult = ch.concat(cheresult);
				}
				if (index == 0) {
					cheresult = cheresult.substring(0, indexComma);
				} else {
					cheresult = (chdep.substring(0, index)).concat(cheresult);
				}
				cheresult = cheresult.concat(chdep.substring(indexComma, chdep.length()));
			} else {
				cheresult = chdep;
			}
			returnString = cheresult;
		} else {
			returnString = BaseType.EMPTY_STRING;
		}

		return returnString;
	}

	public String getExportString() {
		if (erroneousInitializer != null) {
			return erroneousInitializer;
		} else if (baseBigDecimal == null) {
			return BaseType.EMPTY_STRING;
		}
		// 19.09.2013: ZS: PTS-Requirement_22184 :Kostenatrribute output blank when exported without costs.
		else if (baseBigDecimal.equals(BigDecimal.ZERO)) {
			return BaseType.EMPTY_STRING;
		} else {
			// BigDecimal t = baseBigDecimal.multiply(CountryThreadManager.get().getConversionRate().value());
			BigDecimal t = baseBigDecimal;
			NumberFormat formatter = new DecimalFormat("#0.00");
			return formatter.format(t);
		}
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		BaseBigDecimal d1 = null;
		BaseBigDecimal d2 = new BaseBigDecimal();

		// PTS-Problem_22602 : ZS: Fix the null check in the equals() method of BaseBigDecimal class
		System.out.println("Result: " + d2.equals(d1));
	}
}

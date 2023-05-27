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
 * @author Sebri Zouhaier changed by $Author: zouhair $
 * @version $Revision: 1.3 $ $Date: 2011/06/03 11:43:16 $
 */
public abstract class BaseType implements java.io.Serializable {

	/** the <code>serialVersionUID</code> instance holder */
	private static final long serialVersionUID = 4812278458676530886L;

	public static final String NULL_STRING = "null";

	public static final String EMPTY_STRING = "";

	public static final String BLANK_STRING = " ";

	public static final int TYPE_STRING = 1;

	public static final int TYPE_DATE = 2;

	public static final int TYPE_INTEGER = 3;

	public static final int TYPE_LONG = 4;

	public static final int TYPE_BOOLEAN = 5;

	public static final int TYPE_FLOAT = 6;

	public static final int TYPE_DOUBLE = 7;

	public static final int TYPE_BIGDECIMAL = 8;

	public static final int TYPE_TIME = 9;

	public static final int TYPE_DATETIME = 10;

	public static final int TYPE_STRING_BUFFER = 11;

	public static final int TYPE_CLOB = 12;

	public static final int TYPE_BLOB = 13;

	/**
	 * Die Konstante, wenn für einen Grunddatentyp kein Hashwert berechnet werden kann, weil die Membervariable des
	 * Grunddatentyp null ist.
	 */
	protected static final int NULL_HASH_VALUE = Integer.MAX_VALUE;

	/*********************************************************************************************************************
	 * public others
	 ********************************************************************************************************************/

	/**
	 * Zeigt an, ob die Membervariable des Grunddatentyps gesetzt ist.
	 * 
	 * @return Membervariable gesetzt: true oder false
	 */
	public abstract boolean isNull();

	/**
	 * Diese Methode soll -1,0 oder +1 anhand eines Vergleichs mit einem beliebigen Objekt zurückliefern. Diese Methode
	 * wird bei Komparatoren benötigt.
	 * 
	 * @param type
	 *            das zu vergeleichende Objekt
	 * @return Ergebnis des Vergleichs
	 */
	// public abstract int compareTo(Object type);
	/**
	 * Diese Methode soll -1,0 oder +1 anhand eines Vergleichs mit einem Objekt von der selben Klasse zurückliefern.
	 * Diese Methode wird bei Komparatoren benötigt.
	 * 
	 * @param type
	 *            das zu vergeleichende Objekt
	 * @return Ergebnis des Vergleichs
	 */
	// public abstract int compareTo(BaseType type);
	/**
	 * Diese Methode überprüft das vorhandene Objekt auf Gleichheit mit einem beliebigen Objekt, unter noch
	 * festzulegenden Vergleichskriterien.
	 * 
	 * @param type
	 *            das zu vergeleichende Objekt
	 * @return Ergebnis des Vergleichs
	 */
	// public abstract boolean equals(Object type);
	/**
	 * Diese Methode überprüft das vorhandene Objekt auf Gleichheit mit einem Objekt von der selben Klasse, unter noch
	 * festzulegenden Vergleichskriterien.
	 * 
	 * @param type
	 *            das zu vergeleichende Objekt
	 * @return Ergebnis des Vergleichs
	 */

	// public abstract boolean equals(BaseType type);
	/**
	 * Zeigt an, zu welchem Basistyp dieses Objekt gehört.
	 * 
	 * @return den GrundTyp (siehe GrundTypkonstanten)
	 */
	public abstract int getBaseType();

	/**
	 * Liefert einen Hashcode zurück, der unter noch festzulegenden Kriterien berechnet wird.
	 * 
	 * @return Hashcode
	 */
	@Override
	public abstract int hashCode();

	/**
	 * Liefert einen String zurück.
	 * 
	 * @return den String
	 */
	public abstract String getString();

}

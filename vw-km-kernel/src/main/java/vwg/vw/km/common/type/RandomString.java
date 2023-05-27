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

import java.security.SecureRandom;
import java.util.Random;

/**
 * <p>
 * Title: EKM
 * <p>
 * Description : Class description goes here
 * </p>
 * <p>
 * Copyright: VW (c) 2020
 * </p>
 * 
 * @author abidh changed by $Author: abidh $
 * @version $Revision: 1.1 $ $Date: 2020/01/27 16:47:20 $
 */
public class RandomString {

	/**
	 * Generate a random string.
	 */
	public String nextString() {
		for (int idx = 0; idx < buf.length; ++idx)
			buf[idx] = symbols[random.nextInt(symbols.length)];
		return new String(buf);
	}

	public static final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public static final String digits = "0123456789";

	public static final String alphanum = upper + digits;

	private final Random random;

	private final char[] symbols;

	private final char[] buf;

	public RandomString(int length, Random random, String symbols) {
		if (length < 1)
			throw new IllegalArgumentException();
		if (symbols.length() < 2)
			throw new IllegalArgumentException();
		if (random == null) {
			throw new NullPointerException("random cannot be null!");
		}
		this.random = random;
		this.symbols = symbols.toCharArray();
		this.buf = new char[length];
	}

	/**
	 * Create an alphanumeric string generator.
	 */
	public RandomString(int length, Random random) {
		this(length, random, alphanum);
	}

	/**
	 * Create an alphanumeric strings from a secure generator.
	 */
	public RandomString(int length) {
		this(length, new SecureRandom());
	}

	/**
	 * Create session identifiers.
	 */
	public RandomString() {
		this(21);
	}
}

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

package vwg.vw.km.common.exception;

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
 * @author Sebri Zouhaier changed by $Author: monam $
 * @version $Revision: 1.2 $ $Date: 2011/03/11 12:04:45 $
 */
public class BaseRtFormatException extends BaseRtBaseTypeFailureException {

	/** Comment for <code>serialVersionUID</code> */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param message
	 *            String
	 * @param ex
	 *            Throwable
	 */
	public BaseRtFormatException(String message, Throwable ex) {
		super(message, ex);
	}

	/**
	 * 
	 * @param message
	 *            String
	 */
	public BaseRtFormatException(String message) {
		super(message);
	}
}

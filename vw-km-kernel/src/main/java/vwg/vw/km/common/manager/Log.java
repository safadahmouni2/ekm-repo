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

package vwg.vw.km.common.manager;

import java.io.Serializable;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.spi.AbstractLogger;
import org.apache.logging.log4j.spi.ExtendedLoggerWrapper;

import vwg.vw.km.common.type.BaseDateTime;

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
 * @version $Revision: 1.8 $ $Date: 2022/01/19 09:37:04 $
 */

public class Log extends ExtendedLoggerWrapper implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4263835949958609018L;

	// private final ExtendedLoggerWrapper logger;

	/**
	 * @param name
	 *            String, name of the logger
	 */

	public Log(final Logger logger) {
		super((AbstractLogger) logger, logger.getName(), logger.getMessageFactory());
		// this.logger = this;
	}

	/**
	 * System.out.println
	 * 
	 * @param message
	 *            String
	 */
	public void out(String message) {
		if (true) {
			System.out.println(BaseDateTime.getCurrentDateTime().getString(
					BaseDateTime.YYYY_MIN_MM_MIN_DD_BLNK_HH_COL_MM_COL_SS_PNT_MMM) + " : SYSTEM_OUT : " + message);
		}
	}

	/**
	 * System.out.println + Throwable printStackTrace
	 * 
	 * @param message
	 *            String
	 * @param th
	 *            Throwable
	 */
	public void out(String message, Throwable th) {
		out(message);
		if (th != null) {
			th.printStackTrace();
		}
	}
}

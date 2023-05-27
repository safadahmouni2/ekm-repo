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

package vwg.vw.km.test;

import junit.framework.TestCase;
import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;

/**
 * <p>
 * Title: EKM
 * <p>
 * Description : BaseTestCase
 * </p>
 * <p>
 * Copyright: VW (c) 2013
 * </p>
 * 
 * @author Zied Saidi changed by $Author: saidi $
 * @version $Revision: 1.1 $ $Date: 2013/05/29 08:30:32 $
 */
public abstract class BaseTestCase extends TestCase {

	/** the <code>log</code> instance holder */
	private final Log log = LogManager.get().getLog(BaseTestCase.class);
	private long startTime = 0L;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		startTime = System.currentTimeMillis();
		log.info("START : " + getName() + " -------------------");
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		log.info("END : " + getName() + ", duration=" + (System.currentTimeMillis() - startTime)
				+ " -------------------");
	}
}

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
package vwg.vw.km.common.type.test;

import vwg.vw.km.common.type.BaseBigDecimal;
import vwg.vw.km.test.BaseTestCase;

/**
 * <p>
 * Title: EKM
 * <p>
 * Description : BaseBigDecimalTest
 * </p>
 * <p>
 * Copyright: VW (c) 2013
 * </p>
 * 
 * @author Zied Saidi changed by $Author: saidi $
 * @version $Revision: 1.1 $ $Date: 2013/05/29 08:30:32 $
 */
public class BaseBigDecimalTest extends BaseTestCase {

	public void testEquals() {
		BaseBigDecimal d1 = new BaseBigDecimal("1");
		BaseBigDecimal d2 = new BaseBigDecimal("1,00");
		assertEquals(d2, d1);
	}
}

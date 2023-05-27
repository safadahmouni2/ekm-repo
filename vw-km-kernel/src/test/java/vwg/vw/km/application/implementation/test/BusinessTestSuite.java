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
package vwg.vw.km.application.implementation.test;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <p>
 * Title: EKM
 * <p>
 * Description : BusinessTestSuite
 * </p>
 * <p>
 * Copyright: VW (c) 2013
 * </p>
 * 
 * @author Zied Saidi changed by $Author: saidi $
 * @version $Revision: 1.1 $ $Date: 2013/06/06 08:35:38 $
 */
public class BusinessTestSuite extends BaseBusinessTestCase {

  /**
   * @return Test the test suite
   */
  public static Test suite() {

    TestSuite suite = new TestSuite();
    //suite.addTestSuite(PersonneMoraleManagerTest.class);
    
    return suite;
  }
}

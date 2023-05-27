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

package vwg.vw.km.application.implementation.impl;

import java.util.List;

import vwg.vw.km.application.implementation.TablesSpaceStatisticManager;
import vwg.vw.km.common.type.TableSpaceStatisticModel;
import vwg.vw.km.integration.persistence.dao.TableSpaceStatisticDAO;

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
 * @author abir changed by $Author: mrada $
 * @version $Revision: 1.4 $ $Date: 2014/03/05 14:15:12 $
 */

/*
 * PTS requirement 22205: statistic page Responsible ABA
 */
public class TablesSpaceStatisticManagerImpl implements TablesSpaceStatisticManager {

	private TableSpaceStatisticDAO tablesSpaceStatisticDao;

	/**
	 * @see vwg.vw.km.application.implementation.TablesSpaceStatisticManager#getTableSpaces()
	 */
	public List<TableSpaceStatisticModel> getTableSpaces() {
		return tablesSpaceStatisticDao.getTableSpaces();
	}

	public void setDao(TableSpaceStatisticDAO dao) {
		this.tablesSpaceStatisticDao = dao;

	}

}

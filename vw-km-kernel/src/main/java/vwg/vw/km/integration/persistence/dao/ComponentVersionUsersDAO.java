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
package vwg.vw.km.integration.persistence.dao;

import java.util.List;

import vwg.vw.km.integration.persistence.dao.base.BaseDao;
import vwg.vw.km.integration.persistence.model.BrandModel;
import vwg.vw.km.integration.persistence.model.ComponentStandUsersModel;

/**
 * <p>
 * Title: EKM
 * <p>
 * Description : Class description goes here
 * </p>
 * <p>
 * Copyright: VW (c) 2012
 * </p>
 * .
 * 
 * @author Ziad Saidi changed by $Author: saidi $
 * @version $Revision: 1.6 $ $Date: 2013/02/01 08:31:40 $
 */
public interface ComponentVersionUsersDAO extends BaseDao<ComponentStandUsersModel> {

	/**
	 * Return ComponentStandUsers object for a component Stand and a brand
	 * 
	 * @param componentStand
	 * @param brand
	 * @return ComponentStandUsers object
	 */
	public ComponentStandUsersModel getComponentStandUsers(Long standId, BrandModel brand);

	/**
	 * Return ComponentStandUsers object for an component and a brand
	 * 
	 * @param componentId
	 * @param brand
	 * @return
	 */
	public ComponentStandUsersModel getComponentUsers(Long componentId, BrandModel brand);

	/**
	 * Return the list of stand users to be activated
	 * 
	 * @return
	 */
	public List<ComponentStandUsersModel> getInactivatedComponentStandUsers();

	public List<ComponentStandUsersModel> getComponentUsers(Long componentId);
}

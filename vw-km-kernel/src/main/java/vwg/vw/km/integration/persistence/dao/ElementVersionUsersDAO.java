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
import vwg.vw.km.integration.persistence.model.ElementVersionUsersModel;

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
 * @version $Revision: 1.7 $ $Date: 2013/10/03 16:58:37 $
 */
public interface ElementVersionUsersDAO extends BaseDao<ElementVersionUsersModel> {

	/**
	 * Return the list of ElementVersionUses objects for an element version
	 * 
	 * @param elementVersionId
	 * @return
	 */
	public List<ElementVersionUsersModel> getUsedElementByElementId(Long elementVersionId);

	/**
	 * Return ElementVersionUses object for an element version and a brand
	 * 
	 * @param elementVersion
	 * @param brandId
	 * @return
	 */
	public ElementVersionUsersModel getElementVersionUsers(Long elementVersionId, String brandId);

	/**
	 * Return ElementVersionUses object for an element and a brand
	 * 
	 * @param elementId
	 * @param brandId
	 * @return
	 */
	public ElementVersionUsersModel getElementUsers(Long elementId, String brandId);

	/**
	 * return the list of inactivated element Version Users objects
	 * 
	 * @return
	 */
	List<ElementVersionUsersModel> getInactivatedElementVersionUsers();
}

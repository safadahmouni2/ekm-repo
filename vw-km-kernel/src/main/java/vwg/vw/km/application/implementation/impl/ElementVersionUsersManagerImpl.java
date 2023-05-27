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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import vwg.vw.km.application.implementation.ElementVersionUsersManager;
import vwg.vw.km.application.implementation.impl.base.DBManagerImpl;
import vwg.vw.km.integration.persistence.dao.ElementVersionUsersDAO;
import vwg.vw.km.integration.persistence.model.BrandModel;
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
public class ElementVersionUsersManagerImpl extends DBManagerImpl<ElementVersionUsersModel, ElementVersionUsersDAO>
		implements ElementVersionUsersManager {

	/**
	 * @see vwg.vw.km.application.implementation.base.BaseManager#getObjectPK(vwg.vw.km.integration.persistence.model.base.BaseModel)
	 */
	public Serializable getObjectPK(ElementVersionUsersModel model) {
		return model.getId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * vwg.vw.km.application.implementation.ElementVersionUsesManager#getUsedElementByElementVersionId(java.lang.Long)
	 */
	public List<ElementVersionUsersModel> getUsedElementByElementId(Long elementId) {
		return dao.getUsedElementByElementId(elementId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.application.implementation.ElementVersionUsesManager#getBrandsForUsedElement(java.lang.Long)
	 */
	public List<BrandModel> getBrandsByElement(Long elementVersionId) {
		Set<BrandModel> brands = new HashSet<BrandModel>();
		List<ElementVersionUsersModel> elementVersionUsesList = getUsedElementByElementId(elementVersionId);
		for (ElementVersionUsersModel elementVersionUses : elementVersionUsesList) {
			brands.add(elementVersionUses.getBrand());
		}
		return new ArrayList<BrandModel>(brands);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.application.implementation.ElementVersionUsesManager#getElementVersionUses(vwg.vw.km.integration.
	 * persistence.model.ElementVersionModel, vwg.vw.km.integration.persistence.model.BrandModel)
	 */
	public ElementVersionUsersModel getElementVersionUses(Long elementVersionId, String brandId) {
		return dao.getElementVersionUsers(elementVersionId, brandId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.application.implementation.ElementVersionUsersManager#getElementUsers(java.lang.Long,
	 * vwg.vw.km.integration.persistence.model.BrandModel)
	 */
	public ElementVersionUsersModel getElementUsers(Long elementId, String brandId) {
		return dao.getElementUsers(elementId, brandId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.application.implementation.ElementVersionUsersManager#getInactiveElementVersionUsers()
	 */
	public List<ElementVersionUsersModel> getInactivatedElementVersionUsers() {
		return dao.getInactivatedElementVersionUsers();
	}
}

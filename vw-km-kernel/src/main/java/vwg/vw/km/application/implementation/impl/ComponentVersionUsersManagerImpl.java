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
import java.util.List;

import vwg.vw.km.application.implementation.ComponentVersionUsersManager;
import vwg.vw.km.application.implementation.impl.base.DBManagerImpl;
import vwg.vw.km.integration.persistence.dao.ComponentVersionUsersDAO;
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
public class ComponentVersionUsersManagerImpl extends DBManagerImpl<ComponentStandUsersModel, ComponentVersionUsersDAO>
		implements ComponentVersionUsersManager {

	/**
	 * @see vwg.vw.km.application.implementation.base.BaseManager#getObjectPK(vwg.vw.km.integration.persistence.model.base.BaseModel)
	 */
	public Serializable getObjectPK(ComponentStandUsersModel model) {
		return model.getId();
	}

	/**
	 * @see vwg.vw.km.application.implementation.ComponentVersionUsersManager#getComponentStandUsers(vwg.vw.km.integration.persistence.model.ComponentStandModel,
	 *      vwg.vw.km.integration.persistence.model.BrandModel)
	 */
	public ComponentStandUsersModel getComponentStandUsers(Long standId, BrandModel brand) {
		return dao.getComponentStandUsers(standId, brand);
	}

	protected ComponentStandUsersModel beforeSave(ComponentStandUsersModel baseModel) {
		baseModel = super.beforeSave(baseModel);
		if (baseModel.getActive() == null) {
			baseModel.setActive(Boolean.FALSE);
		}
		return baseModel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.application.implementation.ComponentVersionUsersManager#getComponentUsers(java.lang.Long,
	 * vwg.vw.km.integration.persistence.model.BrandModel)
	 */
	public ComponentStandUsersModel getComponentUsers(Long componentId, BrandModel brand) {
		return dao.getComponentUsers(componentId, brand);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.application.implementation.ComponentVersionUsersManager#getInactivatedComponentStandUsers()
	 */
	public List<ComponentStandUsersModel> getInactivatedComponentStandUsers() {
		return dao.getInactivatedComponentStandUsers();
	}

	public List<ComponentStandUsersModel> getComponentUsers(Long componentId) {
		return dao.getComponentUsers(componentId);
	}

}

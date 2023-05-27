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

import vwg.vw.km.application.implementation.WorkAreaManager;
import vwg.vw.km.application.implementation.impl.base.DBManagerImpl;
import vwg.vw.km.common.manager.SessionManager;
import vwg.vw.km.common.type.BaseDateTime;
import vwg.vw.km.integration.persistence.dao.WorkAreaDAO;
import vwg.vw.km.integration.persistence.model.UserModel;
import vwg.vw.km.integration.persistence.model.WorkAreaModel;

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
 * @author Sebri Zouhaier changed by $Author: zouhair $
 * @version $Revision: 1.9 $ $Date: 2018/11/13 14:18:12 $
 */
public class WorkAreaManagerImpl extends DBManagerImpl<WorkAreaModel, WorkAreaDAO> implements WorkAreaManager {

	/**
	 * @see vwg.vw.km.application.implementation.base.BaseManager#getObjectPK(vwg.vw.km.integration.persistence.model.base.BaseModel)
	 */
	public Serializable getObjectPK(WorkAreaModel model) {
		return model.getWorkAreaId();
	}

	/**
	 * @see vwg.vw.km.application.implementation.impl.base.DBManagerImpl#beforeSave(vwg.vw.km.integration.persistence.model.base.BaseModel)
	 */
	@Override
	protected WorkAreaModel beforeSave(WorkAreaModel baseModel) {
		baseModel = super.beforeSave(baseModel);
		UserModel user = (UserModel) SessionManager.get().getUserObject();
		if (baseModel.getWorkAreaId() == null) {
			if (baseModel.getCreationDate() == null || baseModel.getCreationDate().isNull()) {
				baseModel.setCreationDate(BaseDateTime.getCurrentDateTime());
			}
			if (user != null && baseModel.getCreatorRefId() == null) {
				baseModel.setCreatorRefId(user.getUserId());
			}
		} else {
			baseModel.setModificationDate(BaseDateTime.getCurrentDateTime());
			if (user != null) {
				baseModel.setModifierRefId(user.getUserId());
			}
		}
		return baseModel;
	}

	/**
	 * @see vwg.vw.km.application.implementation.WorkAreaManager#getWorkAreaModelByDesignation(String)
	 */
	public WorkAreaModel getWorkAreaModelByDesignation(String designation) {
		return dao.getWorkAreaModelByDesignation(designation);
	}

	/**
	 * @see vwg.vw.km.application.implementation.WorkAreaManager#getWorkAreasBySearchString(String)
	 */
	public List<WorkAreaModel> getWorkAreasBySearchString(String searchString) {
		return dao.getWorkAreasBySearchString(searchString);
	}

	/**
	 * @see vwg.vw.km.application.implementation.WorkAreaManager#getAlphaOrderedWorkAreas()
	 */
	public List<WorkAreaModel> getAlphaOrderedWorkAreas() {
		return dao.getAlphaOrderedWorkAreas();
	}

	public WorkAreaModel getWorkAreaByBrand(String brandId) {
		return dao.getWorkAreaByBrand(brandId);
	}
}

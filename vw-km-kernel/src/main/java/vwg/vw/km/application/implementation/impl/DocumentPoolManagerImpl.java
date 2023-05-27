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

import vwg.vw.km.application.implementation.DocumentPoolManager;
import vwg.vw.km.application.implementation.impl.base.DBManagerImpl;
import vwg.vw.km.common.manager.SessionManager;
import vwg.vw.km.common.type.BaseDateTime;
import vwg.vw.km.integration.persistence.dao.DocumentPoolDAO;
import vwg.vw.km.integration.persistence.model.DocumentPoolModel;
import vwg.vw.km.integration.persistence.model.UserModel;

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
 * @version $Revision: 1.8 $ $Date: 2018/11/13 14:18:12 $
 */
public class DocumentPoolManagerImpl extends DBManagerImpl<DocumentPoolModel, DocumentPoolDAO>
		implements DocumentPoolManager {

	/**
	 * @see vwg.vw.km.application.implementation.base.BaseManager#getObjectPK(vwg.vw.km.integration.persistence.model.base.BaseModel)
	 */
	public Serializable getObjectPK(DocumentPoolModel model) {
		return model.getDocumentPoolId();
	}

	/**
	 * @see vwg.vw.km.application.implementation.impl.base.DBManagerImpl#beforeSave(vwg.vw.km.integration.persistence.model.base.BaseModel)
	 */
	@Override
	protected DocumentPoolModel beforeSave(DocumentPoolModel baseModel) {
		baseModel = super.beforeSave(baseModel);
		UserModel user = (UserModel) SessionManager.get().getUserObject();
		if (baseModel.getDocumentPoolId() == null) {
			if (baseModel.getCreationDate() == null || baseModel.getCreationDate().isNull()) {
				baseModel.setCreationDate(BaseDateTime.getCurrentDateTime());
			}
			if (user != null && baseModel.getCreatorRefId() == null) {
				baseModel.setCreatorRefId(user.getUserId());
			}
		}
		return baseModel;
	}
}

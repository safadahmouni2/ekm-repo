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

import vwg.vw.km.application.implementation.EnumStatusManager;
import vwg.vw.km.application.implementation.impl.base.DBManagerImpl;
import vwg.vw.km.integration.persistence.dao.EnumStatusDAO;
import vwg.vw.km.integration.persistence.model.EnumStatusModel;
import vwg.vw.km.integration.persistence.model.EnumTransitionModel;

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
 * @author Sebri Zouhaier changed by $Author: monam $
 * @version $Revision: 1.8 $ $Date: 2011/08/17 10:28:11 $
 */
public class EnumStatusManagerImpl extends DBManagerImpl<EnumStatusModel, EnumStatusDAO> implements EnumStatusManager {

	/**
	 * @see vwg.vw.km.application.implementation.base.BaseManager#getObjectPK(vwg.vw.km.integration.persistence.model.base.BaseModel)
	 */
	public Serializable getObjectPK(EnumStatusModel model) {
		return model.getEnumStatusId();
	}

	/**
	 * @see vwg.vw.km.application.implementation.EnumStatusManager#getAllowedTransitions(EnumStatusModel, Boolean, List)
	 */
	public List<EnumTransitionModel> getAllowedTransitions(EnumStatusModel fromStatus, Boolean isBatchTransaction,
			List<Long> withoutToStatus) {
		return dao.getAllowedTransitions(fromStatus, isBatchTransaction, withoutToStatus);
	}

}

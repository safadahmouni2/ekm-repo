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

import org.springframework.beans.BeanUtils;

import vwg.vw.km.application.implementation.ComponentElementManager;
import vwg.vw.km.application.implementation.impl.base.DBManagerImpl;
import vwg.vw.km.integration.persistence.dao.ComponentElementDAO;
import vwg.vw.km.integration.persistence.model.ComponentElementModel;
import vwg.vw.km.integration.persistence.model.ComponentStandModel;

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
 * @author Sebri Zouhaier changed by $Author: benamora $
 * @version $Revision: 1.15 $ $Date: 2013/12/10 11:40:19 $
 */
public class ComponentElementManagerImpl extends DBManagerImpl<ComponentElementModel, ComponentElementDAO>
		implements ComponentElementManager {

	/**
	 * @see vwg.vw.km.application.implementation.base.BaseManager#getObjectPK(vwg.vw.km.integration.persistence.model.base.BaseModel)
	 */
	public Serializable getObjectPK(ComponentElementModel model) {
		return model.getComponentElementId();
	}

	/**
	 * @see vwg.vw.km.application.implementation.ComponentElementManager#getComponentElementListByComponentStandId(Long)
	 */
	public List<ComponentElementModel> getComponentElementListByComponentStandId(Long componentStandId) {
		return dao.getComponentElementListByComponentStandId(componentStandId);
	}

	/**
	 * @see vwg.vw.km.application.implementation.ComponentElementManager#copyComponentElements(ComponentStandModel,
	 *      ComponentStandModel)
	 */
	public void copyComponentElements(ComponentStandModel from, ComponentStandModel to) {
		List<ComponentElementModel> setComponentElementModel = getComponentElementListByComponentStandId(from.getId());
		if (setComponentElementModel != null) {
			for (ComponentElementModel gs : setComponentElementModel) {
				ComponentElementModel cloned = new ComponentElementModel();
				BeanUtils.copyProperties(gs, cloned);
				cloned.setLoaded(false);
				cloned.setComponentStand(to);
				cloned.setElementVersion(gs.getElementVersion());
				saveObject(cloned);
			}
		}
	}

	public List<ComponentElementModel> getComponentElementListByElementVersionId(Long elementVersionId) {
		return dao.getComponentElementListByElementVersionId(elementVersionId);
	}

	public ComponentElementModel getComponentElementByElementVersionAndComponent(Long elementVersionId,
			Long componentId) {
		return dao.getComponentElementByElementVersionAndComponent(elementVersionId, componentId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.application.implementation.ComponentElementManager#getAllElementsComponentsAssociations()
	 */
	public List<Object[]> getAllElementsComponentsAssociations() {
		return dao.getAllElementsComponentsAssociations();
	}

}

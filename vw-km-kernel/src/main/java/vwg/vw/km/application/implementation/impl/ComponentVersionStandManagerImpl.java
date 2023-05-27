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
import java.util.Map;

import vwg.vw.km.application.implementation.ComponentVersionStandManager;
import vwg.vw.km.application.implementation.impl.base.DBManagerImpl;
import vwg.vw.km.common.type.BaseDateTime;
import vwg.vw.km.integration.persistence.dao.ComponentVersionStandDAO;
import vwg.vw.km.integration.persistence.model.BrandModel;
import vwg.vw.km.integration.persistence.model.ComponentStandModel;
import vwg.vw.km.integration.persistence.model.ElementModel;
import vwg.vw.km.integration.persistence.model.ElementVersionModel;

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
 * @version $Revision: 1.10 $ $Date: 2012/12/10 09:53:48 $
 */
public class ComponentVersionStandManagerImpl extends DBManagerImpl<ComponentStandModel, ComponentVersionStandDAO>
		implements ComponentVersionStandManager {

	/**
	 * @see vwg.vw.km.application.implementation.base.BaseManager#getObjectPK(vwg.vw.km.integration.persistence.model.base.BaseModel)
	 */
	public Serializable getObjectPK(ComponentStandModel model) {
		return model.getId();
	}

	/**
	 * @see vwg.vw.km.application.implementation.ComponentVersionStandManager#getLastComponentVersionStand(java.lang.Long)
	 */
	public ComponentStandModel getLastComponentVersionStand(Long componentVersionId) {
		return dao.getLastComponentVersionStand(componentVersionId);
	}

	/**
	 * @see vwg.vw.km.application.implementation.ComponentVersionStandManager#getComponentVersionStandByLinkedElementVersion(java.util.Map)
	 */
	public List<ComponentStandModel> getComponentVersionStandByLinkedElementVersion(
			Map<BrandModel, List<ElementVersionModel>> oldElementVersionInUseByBrand) {
		return dao.getComponentVersionStandByLinkedElementVersion(oldElementVersionInUseByBrand);
	}

	public List<ComponentStandModel> getComponentVersionStandByNotOwnerLinkedElement(
			Map<BrandModel, List<ElementModel>> elementBrand) {
		return dao.getComponentVersionStandByNotOwnerLinkedElement(elementBrand);
	}

	/**
	 * @see vwg.vw.km.application.implementation.ComponentVersionStandManager#getMaxNumberByComponentId(java.lang.Long)
	 */

	public Long getMaxNumberByComponentId(Long componentId) {
		return dao.getMaxNumberByComponentId(componentId);
	}

	public List<ComponentStandModel> getStandsByComponentId(Long componentId) {
		return dao.getStandsByComponentId(componentId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * vwg.vw.km.application.implementation.ComponentVersionStandManager#getLastComponentStandsBefore(java.lang.Long,
	 * java.lang.Long)
	 */
	public List<ComponentStandModel> getLastComponentStandsBefore(Long componentId, Long standId) {
		return dao.getLastComponentStandsBefore(componentId, standId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.application.implementation.ComponentVersionStandManager#getLastComponentStand(java.lang.Long,
	 * java.lang.Long)
	 */
	public ComponentStandModel getLastComponentStand(Long componentId, Long standId) {
		return dao.getLastComponentStand(componentId, standId);
	}

	public List<ComponentStandModel> getToExportComponentStands(BaseDateTime beforeDate, List<Long> libraryIds,
			List<String> userIds, List<Long> forlderIds, Long primaryLibrary, boolean approvedStatus) {
		return dao.getToExportComponentStands(beforeDate, libraryIds, userIds, forlderIds, primaryLibrary,
				approvedStatus);
	}
}

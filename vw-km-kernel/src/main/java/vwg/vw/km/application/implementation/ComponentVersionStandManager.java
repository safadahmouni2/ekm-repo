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
package vwg.vw.km.application.implementation;

import java.util.List;
import java.util.Map;

import vwg.vw.km.application.implementation.base.BaseManager;
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
 * Copyright: VW (c) 2011
 * </p>
 * 
 * 
 * @author Ziad Saidi changed by $Author: saidi $
 * @version $Revision: 1.10 $ $Date: 2012/12/10 09:53:48 $
 */
public interface ComponentVersionStandManager extends BaseManager<ComponentStandModel, ComponentVersionStandDAO> {

	/**
	 * get the last stand of a component version
	 * 
	 * @param componentVersionId
	 * @return a componentVersionStand object
	 */
	public ComponentStandModel getLastComponentVersionStand(Long componentVersionId);

	/**
	 * get the list of attached stand
	 * 
	 * @param Map
	 *            <BrandModel, List<ElementVersionModel>> oldElementVersionInUseByBrand
	 * @return a List of componentVersionStand object
	 */
	public List<ComponentStandModel> getComponentVersionStandByLinkedElementVersion(
			Map<BrandModel, List<ElementVersionModel>> oldElementVersionInUseByBrand);

	/**
	 * get the list of attached stand by not owner element version
	 * 
	 * @param Map
	 *            <BrandModel, List<ElementModel>> elementVersionBrand
	 * @return a List of componentVersionStand object
	 */
	public List<ComponentStandModel> getComponentVersionStandByNotOwnerLinkedElement(
			Map<BrandModel, List<ElementModel>> elementVersionBrand);

	/**
	 * get the max number of stand by component
	 * 
	 * @param componentId
	 * @return a Long object the max of stand number by component
	 */

	public Long getMaxNumberByComponentId(Long componentId);

	public List<ComponentStandModel> getStandsByComponentId(Long componentId);

	/**
	 * return the last stands before a stand
	 * 
	 * @param componentId
	 * @param standId
	 * @return
	 */
	public List<ComponentStandModel> getLastComponentStandsBefore(Long componentId, Long standId);

	/**
	 * return the last stand before a stand
	 * 
	 * @param componentId
	 * @param standId
	 * @return
	 */
	public ComponentStandModel getLastComponentStand(Long componentId, Long standId);

	public List<ComponentStandModel> getToExportComponentStands(BaseDateTime beforeDate, List<Long> libraryIds,
			List<String> userIds, List<Long> forlderIds, Long primaryLibrary, boolean approvedStatus);
}

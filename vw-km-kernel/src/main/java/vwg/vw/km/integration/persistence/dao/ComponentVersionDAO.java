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

import vwg.vw.km.common.type.BaseDateTime;
import vwg.vw.km.integration.persistence.dao.base.BaseDao;
import vwg.vw.km.integration.persistence.model.ComponentVersionModel;

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
 * @author Sebri Zouhaier changed by $Author: saidi $
 * @version $Revision: 1.25 $ $Date: 2015/12/07 13:13:31 $
 */
public interface ComponentVersionDAO extends BaseDao<ComponentVersionModel> {

	/**
	 * 
	 * @param componentId
	 * @return last version of given component
	 */
	public ComponentVersionModel getLastComponentVersionByComponentId(Long componentId);

	/**
	 * 
	 * @return list of all approved components version to set "in use" state
	 */
	public List<ComponentVersionModel> getComponentVersionToBeTraitedByBatch();

	/**
	 * 
	 * @param componentId
	 * @return Last ComponentVersion InUse for given componentId
	 */
	public ComponentVersionModel getLastComponentVersionInUse(Long componentId);

	/**
	 * 
	 * @param componentId
	 * @return Component Version Attached Ids for given componentId
	 */
	public List<Long> getComponentVersionAttachedIds(Long componentId);

	/**
	 * 
	 * @param componentId
	 * @param beforeDate
	 * @return Last ComponentVersion Before given Date for given component id
	 */
	public ComponentVersionModel getLastComponentVersionBeforeDate(Long componentId, BaseDateTime beforeDate);

	/**
	 * check if this version is the last one
	 * 
	 * @param componentVersionId
	 * @param componentId
	 * @param statusId
	 */
	public boolean isTheLastComponentVersion(Long componentVersionId, Long componentId, Long statusId);

	/**
	 * get the last component versions before a given one. the list will include this reference version
	 * 
	 * @param componentId
	 * @param versionId
	 * @return
	 */
	public List<ComponentVersionModel> getLastComponentVersionsBeforeVersion(Long componentId, Long versionId);

	/**
	 * get the next component versions after a given one .
	 * 
	 * @param componentVersionId
	 * @param componentId
	 * @return
	 */
	public ComponentVersionModel getNextComponentVersion(Long componentVersionId, Long componentId);

	/**
	 * For statistic purpose select all components versions in the DB
	 * 
	 * @return
	 */
	public List<Object[]> getAllComponentsVersions();

	/**
	 * For statistic purpose select all components versions count grouped by component id
	 * 
	 * @return
	 */
	public List<Object[]> getComponentsCountByVersion();

	public List<ComponentVersionModel> getComponentsVersionsBySearchString(List<Long> listOfFolders, Long loggedUser,
			String searchString);
}

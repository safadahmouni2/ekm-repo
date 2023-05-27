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
import vwg.vw.km.integration.persistence.model.BrandModel;
import vwg.vw.km.integration.persistence.model.ComponentModel;

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
 * @version $Revision: 1.25 $ $Date: 2015/12/11 08:00:44 $
 */
public interface ComponentDAO extends BaseDao<ComponentModel> {
	/**
	 * return list of components in given folder that satisfy conditions
	 * 
	 * @param folderId
	 *            folders of components
	 * @param loggedUser
	 *            if != null return all "created" components only for given user
	 * @param inUse
	 *            if != null return only "in use" components
	 * @return
	 */
	public List<ComponentModel> getComponentsByFolder(Long folderId, Long loggedUser, Boolean inUse);

	/**
	 * return component by given number or designation and not the given component id
	 * 
	 * @param componentId
	 *            if componentId and loaded ==> returned component shouldn't be with id = componentId
	 * @param loaded
	 * @param componentNumber
	 * @param designation
	 * @return found component
	 */
	public ComponentModel getComponentByNmberOrDesignation(Long componentId, boolean loaded, String componentNumber,
			String designation, String ownerId);

	/**
	 * return component by given number or designation
	 * 
	 * @param componentNumber
	 * @param designation
	 * @param ownerId
	 * @return
	 */
	public ComponentModel getComponentByNmberOrDesignation(String componentNumber, String designation, String ownerId);

	/**
	 * return list of components after search operation
	 * 
	 * @param listOfFolders
	 *            folders of components
	 * @param loggedUser
	 *            if != null don't find "created" components for not given user
	 * @param searchString
	 * @return found components
	 */
	public List<ComponentModel> getComponentsBySearchString(List<Long> listOfFolders, Long loggedUser,
			String searchString);

	/**
	 * 
	 * @param searchStatus
	 * @param loggedUser
	 * @return list of components by given status list or all by status and only created for given user
	 */
	public List<ComponentModel> getComponentsByStatus(List<Long> searchStatus, Long loggedUser);

	/**
	 * return list of inactive components
	 * 
	 * @param folderId
	 * @param loggedUser
	 * @return
	 */
	public List<ComponentModel> getInactiveComponentsByFolder(Long folderId, Long loggedUser);

	/**
	 * Return the list of BS for the working folder of the user
	 * 
	 * @param searchStatus
	 * @param userId
	 * @return
	 */
	public List<ComponentModel> getComponentsForWorkingFolder(List<Long> searchStatus, Long userId, Long folderId);

	/**
	 * Check is there an other BS with same external ID in the library of the user
	 * 
	 * @param externalId
	 * @param librayId
	 * @return
	 */
	public ComponentModel getComponentByExternalIdAndLib(String externalId, Long librayId);

	/**
	 * Get max display order by folder
	 * 
	 * @param folderId
	 * @return Long max display order
	 */
	public Long getMaxDisplayOrderByFolderId(Long folderId);

	/**
	 * performComponentChangeOrder
	 * 
	 * @param componentId
	 * @param folderId
	 * @param sortOperation
	 */
	public void performComponentChangeOrder(Long componentId, Long folderId, String operation);

	/**
	 * UpdateDisplayOrdersAfterDragAndDrop
	 * 
	 * @param componentId
	 * @param folderId
	 * @return Long max display order
	 */
	public Long updateDisplayOrdersForOtherNodes(Long componentId, Long folderId);

	/**
	 * Check if folder has components that should be visible by users filter
	 * 
	 * @param usersFilters
	 *            : user brands to filter by
	 * @param userBrand:
	 *            user default Brand
	 * @return bausteinIds:list of components to be filtered
	 */
	public List<Long> getComponentsListToBeFiltred(Long folderId, String userBrand);

	/**
	 * Check if folder has components that should be visible by users filter
	 * 
	 * @param ownerFilters
	 *            : owner brands to filter by
	 * @param folderId:
	 *            parent folder id
	 * @param bausteinIds:list
	 *            of components to be filtered
	 * @return
	 */
	public boolean hasVisibleComponentsByOwnerFilter(List<BrandModel> ownerFilters, Long folderId,
			List<Long> bausteinIds);

	/**
	 * Check if folder has components that should be visible by users filter
	 * 
	 * @param usersFilters
	 *            : user brands to filter by
	 * @param folderId:
	 *            parent folder id
	 * @param bausteinIds:list
	 *            of components to be filtered
	 * @return
	 */
	public boolean hasVisibleComponentsByUsersFilter(List<BrandModel> usersFilters, Long folderId,
			List<Long> bausteinIds);

	/**
	 * Check if folder has components
	 * 
	 * @param folderId:
	 *            parent folder id
	 *
	 * @return
	 */
	public boolean getHasComponents(Long folderId);

	/**
	 * Check if folder has inactive components
	 * 
	 * @param folderId
	 * @return
	 */
	public List<Long> getInactiveComponentIds(Long folderId);

	/**
	 * For statistic purpose select all components in the DB
	 * 
	 * @return
	 */
	public List<Object[]> getAllComponents();

	public ComponentModel getComponentByExternalIdAndBrand(String externalId, String brandId);
}

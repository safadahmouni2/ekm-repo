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
import vwg.vw.km.integration.persistence.model.ElementModel;

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
 * @version $Revision: 1.28 $ $Date: 2013/12/19 15:24:27 $
 */
public interface ElementDAO extends BaseDao<ElementModel> {

	/**
	 * return list of elements for given criterias
	 * 
	 * @param folderId
	 *            folder
	 * @param loggedUser
	 * @param status
	 *            list of status
	 * @return
	 */
	public List<ElementModel> getElementsByFolder(Long folderId, Long loggedUser, List<Long> status);

	/**
	 * return element by given number or designation and not the given element id
	 * 
	 * @param elementId
	 *            if elementId and loaded ==> returned element shouldn't be with id = elementId
	 * @param loaded
	 * @param elementNumber
	 * @param designation
	 * @param ownerId
	 * @return found element
	 */
	public ElementModel getElementByNmberOrDesignation(Long elementId, boolean loaded, String elementNumber,
			String designation, String ownerId);

	/**
	 * return list of elements after search operation
	 * 
	 * @param listOfFolders
	 *            folders of elements
	 * @param loggedUser
	 *            if != null don't find "created" elements for not given user
	 * @param searchString
	 * @return found elements
	 */
	public List<ElementModel> getElementsBySearchString(List<Long> listOfFolders, Long loggedUser, String searchString);

	/**
	 * return list of elements for given status
	 * 
	 * @param searchStatus
	 * @param loggedUser
	 *            if != null don't return "created" element version created by another user
	 * @return List<ElementModel>
	 */
	public List<ElementModel> getElementsByStatus(List<Long> searchStatus, Long loggedUser);

	/**
	 * Return the list of inactive EL
	 * 
	 * @param folderId
	 * @param loggedUser
	 * @return
	 */
	public List<ElementModel> getInactiveElements(Long folderId);

	/**
	 * Return the list of EL for the working folder of the user
	 * 
	 * @param searchStatus
	 * @param userId
	 * @param folderId
	 * @return
	 */
	public List<ElementModel> getElementsForWorkingFolder(List<Long> searchStatus, Long userId, Long folderId);

	/**
	 * Get max display order by folder
	 * 
	 * @param folderId
	 * @return Long max display order
	 */
	public Long getMaxDisplayOrderByFolderId(Long folderId);

	/**
	 * performElementChangeOrder
	 * 
	 * @param elementId
	 * @param folderId
	 * @param sortOperation
	 */
	public void performElementChangeOrder(Long elementId, Long folderId, String operation);

	/**
	 * performElementChangeOrder
	 * 
	 * @param elementId
	 * @param folderId
	 * @return Long max display order
	 */
	public Long updateDisplayOrdersForOtherNodes(Long elementId, Long folderId);

	/**
	 * Check if folder has Elements that should be visible by users filter
	 * 
	 * @param usersFilters
	 *            : user brands to filter by
	 * @param userBrand:
	 *            user default Brand
	 * @return elementsIds:list of Elements to be filtered
	 */
	public List<Long> getElementsListToBeFiltred(Long folderId, String userBrand);

	/**
	 * Check if folder has Elements that should be visible by users filter
	 * 
	 * @param ownerFilters
	 *            : owner brands to filter by
	 * @param folderId:
	 *            parent folder id
	 * @param elementsIds:list
	 *            of Elements to be filtered
	 * @return
	 */
	public boolean hasVisibleElementsByOwnerFilter(List<BrandModel> ownerFilters, Long folderId,
			List<Long> elementsIds);

	/**
	 * Check if folder has Elements that should be visible by users filter
	 * 
	 * @param usersFilters
	 *            : user brands to filter by
	 * @param folderId:
	 *            parent folder id
	 * @param elementsIds:list
	 *            of Elements to be filtered
	 * @return
	 */
	public boolean hasVisibleElementsByUsersFilter(List<BrandModel> usersFilters, Long folderId,
			List<Long> elementsIds);

	/**
	 * Check if folder has Elements
	 * 
	 * @param folderId:
	 *            parent folder id
	 * @return
	 */
	public boolean getHasElements(Long folderId);

	/**
	 * Check if folder has Elements
	 * 
	 * @param folderId:
	 *            parent folder id
	 * @return
	 */
	public List<Long> getInactiveElementIds(Long folderId);

	/**
	 * For statistic purpose select all elements in the DB
	 * 
	 * @return
	 */
	public List<Object[]> getAllElements();
}

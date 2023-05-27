package vwg.vw.km.application.implementation;

// Generated Feb 24, 2011 12:09:56 PM by Hibernate Tools 3.2.0.b9

import java.util.List;

import vwg.vw.km.application.implementation.base.BaseManager;
import vwg.vw.km.integration.persistence.dao.ElementDAO;
import vwg.vw.km.integration.persistence.model.BrandModel;
import vwg.vw.km.integration.persistence.model.ElementModel;

/**
 * @hibernate.class table="T_ELEMENT"
 * 
 */
public interface ElementManager extends BaseManager<ElementModel, ElementDAO> {

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
	public List<ElementModel> getElementsBySearchString(Long startFolderId, Long loggedUser, String searchString);

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
	 * @return Long max display order
	 */
	public void performElementChangeOrder(Long elementId, Long folderId, String operation);

	/**
	 * UpdateDisplayOrdersAfterDragAndDrop
	 * 
	 * @param elementId
	 * @param folderId
	 */
	public void updateDisplayOrdersForOtherNodes(Long elementId, Long folderId);

	/**
	 * Check if folder has Elements that should be visible by users filter
	 * 
	 * @param usersFilters
	 *            : user brands to filter by
	 * @param userBrand:
	 *            user default Brand
	 * @return bausteinIds:list of Elements to be filtered
	 */
	public boolean hasVisibleElementsByUsersFilter(List<BrandModel> usersFilters, Long folderId,
			List<Long> elementsIds);

	/**
	 * Check if folder has Elements that should be visible by users filter
	 * 
	 * @param ownerFilters
	 *            : owner brands to filter by
	 * @param folderId:
	 *            parent folder id
	 * @param bausteinIds:list
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
	 * @param bausteinIds:list
	 *            of Elements to be filtered
	 * @return
	 */
	public List<Long> getElementsListToBeFiltred(Long folderId, String userBrand);

	/**
	 * Check if folder has Elements
	 * 
	 * @param folderId:
	 *            parent folder id
	 * @return
	 */
	public boolean getHasElements(Long folderId);

	/**
	 * getthe inactive Elements ids in folder
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

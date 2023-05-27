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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import vwg.vw.km.application.implementation.ElementManager;
import vwg.vw.km.application.implementation.ElementVersionManager;
import vwg.vw.km.application.implementation.EnumObjectTypeManager;
import vwg.vw.km.application.implementation.FolderManager;
import vwg.vw.km.application.implementation.impl.base.DBManagerImpl;
import vwg.vw.km.common.type.BaseDateTime;
import vwg.vw.km.integration.persistence.dao.ElementDAO;
import vwg.vw.km.integration.persistence.model.BrandModel;
import vwg.vw.km.integration.persistence.model.ElementModel;
import vwg.vw.km.integration.persistence.model.ElementVersionModel;
import vwg.vw.km.integration.persistence.model.FolderModel;

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
 * @version $Revision: 1.43 $ $Date: 2015/12/07 13:13:31 $
 */
/**
 * <p>
 * Title: EKM
 * <p>
 * Description : Class description goes here
 * </p>
 * <p>
 * Copyright: VW (c) 2012
 * </p>
 * 
 * @author saidi changed by $Author: saidi $
 * @version $Revision: 1.43 $ $Date: 2015/12/07 13:13:31 $
 */
public class ElementManagerImpl extends DBManagerImpl<ElementModel, ElementDAO> implements ElementManager {

	private FolderManager folderManager;

	private ElementVersionManager elementVersionManager;

	/**
	 * @see vwg.vw.km.application.implementation.base.BaseManager#getObjectPK(vwg.vw.km.integration.persistence.model.base.BaseModel)
	 */
	public Serializable getObjectPK(ElementModel model) {
		return model.getElementId();
	}

	/**
	 * @see vwg.vw.km.application.implementation.impl.base.DBManagerImpl#beforeSave(vwg.vw.km.integration.persistence.model.base.BaseModel)
	 */
	@Override
	protected ElementModel beforeSave(ElementModel baseModel) {
		baseModel = super.beforeSave(baseModel);
		if (baseModel.getElementId() == null) {
			baseModel.setCreationDate(BaseDateTime.getCurrentDateTime());
		} else {
			baseModel.setModificationDate(BaseDateTime.getCurrentDateTime());
		}
		if (Boolean.TRUE.equals(baseModel.getInactive())) {
			if (Boolean.FALSE.equals(baseModel.getOldInactive()) || baseModel.getOldInactive() == null) {
				baseModel.setInactiveFromDate(BaseDateTime.getCurrentDateTime());
			}
		} else {
			baseModel.setInactiveFromDate(null);
		}
		return baseModel;
	}

	/**
	 * @see vwg.vw.km.application.implementation.ElementManager#getElementsByFolder(Long, Long, List)
	 */
	public List<ElementModel> getElementsByFolder(Long folderId, Long loggedUser, List<Long> status) {

		List<ElementModel> elements = dao.getElementsByFolder(folderId, loggedUser, status);
		loadElementVersionUsers(elements);
		return elements;
	}

	private void loadElementVersionUsers(List<ElementModel> elements) {
		if (elements != null && !elements.isEmpty()) {
			for (ElementModel element : elements) {
				loadElementVersionUsers(element);
			}
		}
	}

	private void loadElementVersionUsers(ElementModel element) {
		if (element.getElementVersions() != null && !element.getElementVersions().isEmpty()) {
			List<ElementVersionModel> elementversions = new ArrayList<ElementVersionModel>(
					element.getElementVersions());
			for (ElementVersionModel elementVersion : elementversions) {
				elementVersion.getElementVersionUsers().size();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.application.implementation.ElementManager#getElementByNmberOrDesignation(java.lang.Long, boolean,
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
	public ElementModel getElementByNmberOrDesignation(Long elementId, boolean loaded, String elementNumber,
			String designation, String ownerId) {
		return dao.getElementByNmberOrDesignation(elementId, loaded, elementNumber, designation, ownerId);
	}

	/**
	 * @see vwg.vw.km.application.implementation.ElementManager#getElementsBySearchString(Long, Long, String)
	 */
	public List<ElementModel> getElementsBySearchString(Long startFolderId, Long loggedUser, String searchString) {
		List<Long> listOfFolders = new ArrayList<Long>();
		listOfFolders.add(startFolderId);
		folderManager.getAllSubFolders(startFolderId, listOfFolders, EnumObjectTypeManager.ELEMENT_ID);

		// if the search is started from the root folder, search will include the working folders
		if (startFolderId != null && startFolderId.equals(6L)) {
			List<FolderModel> workingFolders = folderManager.getWorkingFoldersUnder(null,
					EnumObjectTypeManager.ELEMENT_ID, null);
			for (FolderModel folder : workingFolders) {
				listOfFolders.add(folder.getFolderId());
			}
		}
		Set<ElementModel> elements = new HashSet<ElementModel>();
		List<ElementVersionModel> elementVersions = elementVersionManager.getElementsBySearchString(listOfFolders,
				loggedUser, searchString);
		for (ElementVersionModel elementVersionModel : elementVersions) {
			elements.add(elementVersionModel.getElement());
		}
		loadElementVersionUsers(new ArrayList<ElementModel>(elements));
		return new ArrayList<ElementModel>(elements);
	}

	/**
	 * @see vwg.vw.km.application.implementation.ElementManager#getElementsByStatus(List, Long)
	 */
	public List<ElementModel> getElementsByStatus(List<Long> searchStatus, Long loggedUser) {
		List<ElementModel> elements = dao.getElementsByStatus(searchStatus, loggedUser);
		loadElementVersionUsers(elements);
		return elements;
	}

	public void setFolderManager(FolderManager folderManager) {
		this.folderManager = folderManager;
	}

	public void setElementVersionManager(ElementVersionManager elementVersionManager) {
		this.elementVersionManager = elementVersionManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.application.implementation.ElementManager#getInactiveElements(java.lang.Long)
	 */
	public List<ElementModel> getInactiveElements(Long folderId) {
		List<ElementModel> elements = dao.getInactiveElements(folderId);
		loadElementVersionUsers(elements);
		return elements;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.application.implementation.ElementManager#getElementsForWorkingFolder(java.util.List,
	 * java.lang.Long, java.lang.Long)
	 */
	public List<ElementModel> getElementsForWorkingFolder(List<Long> searchStatus, Long userId, Long folderId) {
		List<ElementModel> elements = dao.getElementsForWorkingFolder(searchStatus, userId, folderId);
		loadElementVersionUsers(elements);
		return elements;
	}

	/**
	 * @see vwg.vw.km.application.implementation.ElementManager#getMaxDisplayOrderByFolderId(Long)
	 */
	public Long getMaxDisplayOrderByFolderId(Long folderId) {
		return dao.getMaxDisplayOrderByFolderId(folderId);
	}

	/**
	 * @see vwg.vw.km.application.implementation.ElementManager#performComponentChangeOrder(Long,Long,String)
	 */
	public void performElementChangeOrder(Long elementId, Long folderId, String operation) {
		dao.performElementChangeOrder(elementId, folderId, operation);

	}

	/**
	 * @see vwg.vw.km.application.implementation.ComponentManager#updateDisplayOrdersAfterDragAndDrop(Long,Long)
	 */
	public void updateDisplayOrdersForOtherNodes(Long elementId, Long folderId) {
		dao.updateDisplayOrdersForOtherNodes(elementId, folderId);
	}

	/**
	 * @see vwg.vw.km.application.implementation.ElementManager#hasVisibleElementsByOwnerFilter(Long,String)
	 */
	public List<Long> getElementsListToBeFiltred(Long folderId, String userBrand) {
		return dao.getElementsListToBeFiltred(folderId, userBrand);
	}

	/**
	 * @see vwg.vw.km.application.implementation.ElementManager#hasVisibleElementsByOwnerFilter(List,Long,List)
	 */
	public boolean hasVisibleElementsByOwnerFilter(List<BrandModel> ownerFilters, Long folderId,
			List<Long> elementsIds) {
		return dao.hasVisibleElementsByOwnerFilter(ownerFilters, folderId, elementsIds);
	}

	/**
	 * @see vwg.vw.km.application.implementation.ElementManager#hasVisibleElementsByUsersFilter(List,Long,List)
	 */
	public boolean hasVisibleElementsByUsersFilter(List<BrandModel> usersFilters, Long folderId,
			List<Long> elementsIds) {
		return dao.hasVisibleElementsByUsersFilter(usersFilters, folderId, elementsIds);
	}

	/**
	 * @see vwg.vw.km.application.implementation.ElementManager#getHasElements(Long)
	 */
	public boolean getHasElements(Long folderId) {
		return dao.getHasElements(folderId);
	}

	/**
	 * @see vwg.vw.km.application.implementation.ElementManager#getInactiveElementIds(Long)
	 */
	public List<Long> getInactiveElementIds(Long folderId) {
		return dao.getInactiveElementIds(folderId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.application.implementation.ElementManager#getAllElements()
	 */
	public List<Object[]> getAllElements() {
		return dao.getAllElements();
	}
}

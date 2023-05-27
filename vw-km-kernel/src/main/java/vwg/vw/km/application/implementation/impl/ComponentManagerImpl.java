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

import vwg.vw.km.application.implementation.ComponentManager;
import vwg.vw.km.application.implementation.ComponentVersionManager;
import vwg.vw.km.application.implementation.ComponentVersionUsersManager;
import vwg.vw.km.application.implementation.EnumObjectTypeManager;
import vwg.vw.km.application.implementation.FolderManager;
import vwg.vw.km.application.implementation.impl.base.DBManagerImpl;
import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;
import vwg.vw.km.common.type.BaseDateTime;
import vwg.vw.km.integration.persistence.dao.ComponentDAO;
import vwg.vw.km.integration.persistence.model.BrandModel;
import vwg.vw.km.integration.persistence.model.ComponentModel;
import vwg.vw.km.integration.persistence.model.ComponentVersionModel;
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
 * @version $Revision: 1.36 $ $Date: 2015/12/11 08:00:44 $
 */
public class ComponentManagerImpl extends DBManagerImpl<ComponentModel, ComponentDAO> implements ComponentManager {

	private FolderManager folderManager;

	private ComponentVersionUsersManager componentVersionUsersManager;

	private ComponentVersionManager componentVersionManager;

	private final Log log = LogManager.get().getLog(ComponentManagerImpl.class);

	/**
	 * @see vwg.vw.km.application.implementation.base.BaseManager#getObjectPK(vwg.vw.km.integration.persistence.model.base.BaseModel)
	 */
	public Serializable getObjectPK(ComponentModel model) {
		return model.getComponentId();
	}

	/**
	 * @see vwg.vw.km.application.implementation.impl.base.DBManagerImpl#beforeSave(vwg.vw.km.integration.persistence.model.base.BaseModel)
	 */
	@Override
	protected ComponentModel beforeSave(ComponentModel baseModel) {
		baseModel = super.beforeSave(baseModel);
		if (baseModel.getComponentId() == null) {
			baseModel.setCreationDate(BaseDateTime.getCurrentDateTime());
			baseModel.setManifacturerRefId(1L);
		} else {
			baseModel.setModificationDate(BaseDateTime.getCurrentDateTime());
			baseModel.setModifierRefId(1L);
		}
		if (Boolean.TRUE.equals(baseModel.getInactive())) {
			if (Boolean.FALSE.equals(baseModel.getOldInactive()) || baseModel.getOldInactive() == null) {
				baseModel.setInactiveFromDate(BaseDateTime.getCurrentDateTime());
			}
		} else {
			baseModel.setInactiveFromDate(null);
		}
		// baseModel.setOldInactive(baseModel.getInactive());
		return baseModel;
	}

	/**
	 * @see vwg.vw.km.application.implementation.ComponentManager#getComponentsByFolder(Long, Long, Boolean)
	 */
	public List<ComponentModel> getComponentsByFolder(Long folderId, Long loggedUser, Boolean inUse) {
		List<ComponentModel> components = dao.getComponentsByFolder(folderId, loggedUser, inUse);
		loadComponentVersionUsers(components);
		return components;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.application.implementation.ComponentManager#getComponentByNmberOrDesignation(java.lang.Long,
	 * boolean, java.lang.String, java.lang.String, java.lang.String)
	 */
	public ComponentModel getComponentByNmberOrDesignation(Long componentId, boolean loaded, String componentNumber,
			String designation, String ownerId) {
		return dao.getComponentByNmberOrDesignation(componentId, loaded, componentNumber, designation, ownerId);
	}

	/**
	 * @see vwg.vw.km.application.implementation.ComponentManager#getComponentsBySearchString(Long, Long, String)
	 */
	public List<ComponentModel> getComponentsBySearchString(Long startFolderId, Long loggedUser, String searchString) {
		List<Long> listOfFolders = new ArrayList<Long>();
		log.info("startFolderId " + startFolderId);
		listOfFolders.add(startFolderId);
		folderManager.getAllSubFolders(startFolderId, listOfFolders, EnumObjectTypeManager.COMPONENT_ID);

		// if the search is started from the root folder, search will include the working folders
		if (startFolderId != null && startFolderId.equals(7L)) {
			List<FolderModel> workingFolders = folderManager.getWorkingFoldersUnder(null,
					EnumObjectTypeManager.COMPONENT_ID, null);
			for (FolderModel folder : workingFolders) {
				listOfFolders.add(folder.getFolderId());
			}
		}
		Set<ComponentModel> components = new HashSet<ComponentModel>();
		List<ComponentVersionModel> componentVersions = componentVersionManager
				.getComponentsVersionsBySearchString(listOfFolders, loggedUser, searchString);
		for (ComponentVersionModel componentVersionModel : componentVersions) {
			components.add(componentVersionModel.getComponent());
		}
		loadComponentVersionUsers(new ArrayList<ComponentModel>(components));
		return new ArrayList<ComponentModel>(components);
	}

	/**
	 * @see vwg.vw.km.application.implementation.ComponentManager#getComponentsByStatus(List, Long)
	 */
	public List<ComponentModel> getComponentsByStatus(List<Long> searchStatus, Long loggedUser) {
		List<ComponentModel> components = dao.getComponentsByStatus(searchStatus, loggedUser);
		loadComponentVersionUsers(components);
		return components;
	}

	private void loadComponentVersionUsers(List<ComponentModel> components) {
		if (components != null && !components.isEmpty()) {
			for (ComponentModel component : components) {
				loadComponentVersionUsers(component);
			}
		}
	}

	private void loadComponentVersionUsers(ComponentModel component) {
		component.setUsers(componentVersionUsersManager.getComponentUsers(component.getComponentId()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.application.implementation.ComponentManager#getInactiveComponentsByFolder(java.lang.Long,
	 * java.lang.Long)
	 */
	public List<ComponentModel> getInactiveComponentsByFolder(Long folderId, Long loggedUser) {
		List<ComponentModel> components = dao.getInactiveComponentsByFolder(folderId, loggedUser);
		loadComponentVersionUsers(components);
		return components;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.application.implementation.ComponentManager#getComponentByNmberOrDesignation(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	public ComponentModel getComponentByNmberOrDesignation(String componentNumber, String designation, String ownerId) {
		return dao.getComponentByNmberOrDesignation(componentNumber, designation, ownerId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.application.implementation.ComponentManager#getComponentsForWorkingFolder(java.util.List,
	 * java.lang.Long, java.lang.Long)
	 */
	public List<ComponentModel> getComponentsForWorkingFolder(List<Long> searchStatus, Long userId, Long folderId) {
		List<ComponentModel> components = dao.getComponentsForWorkingFolder(searchStatus, userId, folderId);
		loadComponentVersionUsers(components);
		return components;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.application.implementation.ComponentManager#getComponentByExternalIdAndLib(java.lang.String,
	 * java.lang.Long)
	 */
	public ComponentModel getComponentByExternalIdAndLib(String externalId, Long librayId) {
		return dao.getComponentByExternalIdAndLib(externalId, librayId);
	}

	/**
	 * @see vwg.vw.km.application.implementation.ComponentManager#getMaxDisplayOrderByFolderId(Long)
	 */
	public Long getMaxDisplayOrderByFolderId(Long folderId) {
		return dao.getMaxDisplayOrderByFolderId(folderId);
	}

	/**
	 * @see vwg.vw.km.application.implementation.ComponentManager#performComponentChangeOrder(Long,Long,String)
	 */
	public void performComponentChangeOrder(Long elementId, Long folderId, String operation) {
		dao.performComponentChangeOrder(elementId, folderId, operation);
	}

	/**
	 * @see vwg.vw.km.application.implementation.ComponentManager#updateDisplayOrdersAfterDragAndDrop(Long,Long,String)
	 */
	public void updateDisplayOrdersForOtherNodes(Long componentId, Long folderId) {
		dao.updateDisplayOrdersForOtherNodes(componentId, folderId);
	}

	/**
	 * @see vwg.vw.km.application.implementation.ComponentManager#getComponentsListToBeFiltred(Long,Long,String)
	 */
	public List<Long> getComponentsListToBeFiltred(Long folderId, String userBrand) {
		return dao.getComponentsListToBeFiltred(folderId, userBrand);
	}

	/**
	 * @see vwg.vw.km.application.implementation.ComponentManager#hasVisibleComponentsByOwnerFilter(Long,Long,String)
	 */
	public boolean hasVisibleComponentsByOwnerFilter(List<BrandModel> ownerFilters, Long folderId,
			List<Long> bausteinIds) {
		return dao.hasVisibleComponentsByOwnerFilter(ownerFilters, folderId, bausteinIds);
	}

	/**
	 * @see vwg.vw.km.application.implementation.ComponentManager#hasVisibleComponentsByUsersFilter(Long,Long,String)
	 */
	public boolean hasVisibleComponentsByUsersFilter(List<BrandModel> usersFilters, Long folderId,
			List<Long> bausteinIds) {
		return dao.hasVisibleComponentsByUsersFilter(usersFilters, folderId, bausteinIds);
	}

	public boolean getHasComponents(Long folderId) {
		return dao.getHasComponents(folderId);
	}

	public List<Long> getInactiveComponentIds(Long folderId) {
		return dao.getInactiveComponentIds(folderId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.application.implementation.ElementManager#getAllElements()
	 */
	public List<Object[]> getAllComponents() {
		return dao.getAllComponents();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.application.implementation.ComponentManager#getComponentByExternalIdAndBrand(java.lang.String,
	 * java.lang.String)
	 */
	public ComponentModel getComponentByExternalIdAndBrand(String externalId, String brandId) {
		return dao.getComponentByExternalIdAndBrand(externalId, brandId);
	}

	public void setFolderManager(FolderManager folderManager) {
		this.folderManager = folderManager;
	}

	public void setComponentVersionUsersManager(ComponentVersionUsersManager componentVersionUsersManager) {
		this.componentVersionUsersManager = componentVersionUsersManager;
	}

	public void setComponentVersionManager(ComponentVersionManager componentVersionManager) {
		this.componentVersionManager = componentVersionManager;
	}

}

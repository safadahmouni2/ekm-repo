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
package vwg.vw.km.application.service.logic;

import java.util.List;

import vwg.vw.km.application.implementation.EnumObjectTypeManager;
import vwg.vw.km.application.implementation.FolderManager;
import vwg.vw.km.application.service.base.BaseService;
import vwg.vw.km.application.service.exception.DestinationFolderIsSubFolderException;
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
 * @version $Revision: 1.47 $ $Date: 2013/09/30 14:14:53 $
 */
@SuppressWarnings("rawtypes")
public class NavigationService extends BaseService {

	private CostElementService elementService;

	private CostComponentService componentService;

	private HourlyRateCatalogService hourlyRateCatalogService;

	public void setElementService(CostElementService elementService) {
		this.elementService = elementService;
	}

	public void setComponentService(CostComponentService componentService) {
		this.componentService = componentService;
	}

	public void setHourlyRateCatalogService(HourlyRateCatalogService hourlyRateCatalogService) {
		this.hourlyRateCatalogService = hourlyRateCatalogService;
	}

	private FolderManager folderManager;

	private EnumObjectTypeManager enumObjectTypeManager;

	public void setFolderManager(FolderManager folderManager) {
		this.folderManager = folderManager;
	}

	public void setEnumObjectTypeManager(EnumObjectTypeManager enumObjectTypeManager) {
		this.enumObjectTypeManager = enumObjectTypeManager;
	}

	/**
	 * load subfolders of folderId with type = objectType
	 * 
	 * @param folderId
	 * @param objectType
	 * @return
	 */
	public List<FolderModel> loadFolders(Long folderId, Long objectType) {
		return folderManager.getFoldersUnder(folderId, objectType);
	}

	public boolean getHasFoldersUnder(Long folderId, Long objectType) {
		return folderManager.getHasFoldersUnder(folderId, objectType);
	}

	/**
	 * load subfolders of folderId with type = objectType
	 * 
	 * @param folderId
	 * @param objectType
	 * @return
	 */
	public List<FolderModel> loadWorkingFolders(Long folderId, Long objectType, Long userId) {
		return folderManager.getWorkingFoldersUnder(folderId, objectType, userId);
	}

	/**
	 * save new folder obejct
	 * 
	 * @param name
	 * @param parent
	 * @param objectType
	 * @return
	 */
	public FolderModel saveNewFolder(String name, Long parent, Long objectType) {
		FolderModel e = new FolderModel();
		e.setDesignation(name + (folderManager.getMaxFolderId() + 1));
		FolderModel pfolder = folderManager.getObject(parent);
		e.setFolder(pfolder);
		e.setObjectType(enumObjectTypeManager.getObject(objectType));
		if (pfolder.getCreatorRefId() != null) {
			e.setCreatorRefId(pfolder.getCreatorRefId());
		}
		folderManager.saveObject(e);
		return e;
	}

	/**
	 * save already exting folder object
	 * 
	 * @param folderModel
	 * @return
	 */
	public FolderModel addUpdateObject(FolderModel folderModel) {
		folderManager.saveObject(folderModel);
		return folderModel;
	}

	/**
	 * return true if parent folder contains a folder with name = name and a folder id != folderid
	 * 
	 * @param parentFolderId
	 * @param folderId
	 * @param name
	 * @return
	 */
	private Boolean isParentFolderContains(Long parentFolderId, Long folderId, String name) {
		FolderModel f = folderManager.getFolderUnderParentByName(parentFolderId, folderId, name);
		if (f != null) {
			return true;
		}
		return false;
	}

	/**
	 * Check if it exist the same name to set for other folders with the same parent. If true return false, else save
	 * the new name and return true
	 * 
	 * @param name
	 *            new folder name
	 * @param folderId
	 * @return
	 */
	public Boolean saveNewNameFolder(String name, Long folderId) {
		FolderModel e = folderManager.getObject(folderId);
		if (isParentFolderContains((e.getFolder() != null ? e.getFolder().getFolderId() : null), folderId, name)) {
			return false;
		}
		e.setDesignation(name);
		folderManager.saveObject(e);
		return true;
	}

	/**
	 * delete given folder
	 * 
	 * @param folderId
	 * @param objectType
	 * @return error code if exist and empty String if delete done
	 */
	public String deleteFolder(Long folderId, Long objectType) {
		if (folderManager.getFoldersUnder(folderId, objectType).size() > 0) {
			return "folder.notEmptyContent";
		}
		if (EnumObjectTypeManager.ELEMENT_ID.equals(objectType)) {
			if (elementService.getElementsByFolder(folderId, null, null).size() > 0) {
				return "folder.notEmptyContent";
			}
		}
		if (EnumObjectTypeManager.COMPONENT_ID.equals(objectType)) {
			if (componentService.getComponentsByFolder(folderId, null).size() > 0) {
				return "folder.notEmptyContent";
			}
		}
		if (EnumObjectTypeManager.HOURLY_RATE_CATALOG_ID.equals(objectType)) {
			if (hourlyRateCatalogService.getHourlyRateCatalogsByFolder(folderId).size() > 0) {
				return "folder.notEmptyContent";
			}
		}
		folderManager.removeObject(folderId);
		return "";
	}

	/**
	 * move folder 1 under folder 2 method used after drag & drop or cut and paste
	 * 
	 * @param folderId
	 * @param toFolder
	 * @return true if possible and done
	 * @throws DestinationFolderIsSubFolderException
	 */
	public Boolean saveFolderToFolder(Long folderId, Long toFolder) throws DestinationFolderIsSubFolderException {
		FolderModel f = folderManager.getObject(folderId);
		FolderModel parent = folderManager.getObject(toFolder);
		// check if The destination folder is a subfolder of the source folder
		List<Long> subFolderIds = getFolderIdsHierarchy(toFolder);
		if (subFolderIds != null && !subFolderIds.isEmpty() && subFolderIds.contains(folderId)) {
			throw new DestinationFolderIsSubFolderException(
					"The destination folder is a subfolder of the source folder");
		}
		if (isParentFolderContains(parent.getFolderId(), f.getFolderId(), f.getDesignation())) {
			return false;
		}
		f.setFolder(parent);
		folderManager.saveObject(f);
		return true;
	}

	/**
	 * get Folder Ids Hierarchy from start folder to the top of tree
	 * 
	 * @param startFolderId
	 * @return
	 */
	public List<Long> getFolderIdsHierarchy(Long startFolderId) {
		return folderManager.getFolderIdsHierarchy(startFolderId);
	}

	public FolderModel getFolder(Long folderId) {
		return folderManager.getObject(folderId);
	}
}

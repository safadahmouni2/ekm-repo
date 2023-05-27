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
import java.util.Set;

import vwg.vw.km.application.implementation.FolderManager;
import vwg.vw.km.application.implementation.impl.base.DBManagerImpl;
import vwg.vw.km.common.manager.SessionManager;
import vwg.vw.km.common.type.BaseDateTime;
import vwg.vw.km.integration.persistence.dao.FolderDAO;
import vwg.vw.km.integration.persistence.model.FolderModel;
import vwg.vw.km.integration.persistence.model.UserModel;

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
 * @author Sebri Zouhaier changed by $Author: abidh $
 * @version $Revision: 1.19 $ $Date: 2020/03/06 09:40:27 $
 */
public class FolderManagerImpl extends DBManagerImpl<FolderModel, FolderDAO> implements FolderManager {

	/**
	 * @see vwg.vw.km.application.implementation.base.BaseManager#getObjectPK(vwg.vw.km.integration.persistence.model.base.BaseModel)
	 */
	public Serializable getObjectPK(FolderModel model) {
		return model.getFolderId();
	}

	/**
	 * @see vwg.vw.km.application.implementation.impl.base.DBManagerImpl#beforeSave(vwg.vw.km.integration.persistence.model.base.BaseModel)
	 */
	@Override
	protected FolderModel beforeSave(FolderModel baseModel) {
		baseModel = super.beforeSave(baseModel);
		UserModel user = (UserModel) SessionManager.get().getUserObject();
		if (baseModel.getFolderId() == null) {
			if (baseModel.getCreationDate() == null || baseModel.getCreationDate().isNull()) {
				baseModel.setCreationDate(BaseDateTime.getCurrentDateTime());
			}
			// 06.03.2020: HAB: PTS_Maintenance-10276722: created new folder in the tree is considered as working folder

			// if (user != null && baseModel.getCreatorRefId() == null) {
			// baseModel.setCreatorRefId(user.getUserId());
			// }
		} else {
			baseModel.setModificationDate(BaseDateTime.getCurrentDateTime());
			if (user != null) {
				baseModel.setModifierRefId(user.getUserId());
			}
		}
		return baseModel;
	}

	/**
	 * @see vwg.vw.km.application.implementation.FolderManager#getFoldersUnder(Long, Long)
	 */
	public List<FolderModel> getFoldersUnder(Long folderId, Long objectTypeId) {
		return dao.getFoldersUnder(folderId, objectTypeId);
	}

	/**
	 * @see vwg.vw.km.application.implementation.FolderManager#getFolderUnderParentByName(Long, Long, String)
	 */
	public FolderModel getFolderUnderParentByName(Long parentFolderId, Long folderId, String name) {
		return dao.getFolderUnderParentByName(parentFolderId, folderId, name);
	}

	/**
	 * @see vwg.vw.km.application.implementation.FolderManager#getMaxFolderId()
	 */
	public Long getMaxFolderId() {
		Long i = dao.getMaxFolderId();
		if (i == null) {
			return 0L;
		}
		return i;
	}

	/**
	 * @see vwg.vw.km.application.implementation.FolderManager#getFolderIdsHierarchy(Long)
	 */
	public List<Long> getFolderIdsHierarchy(Long startFolderId) {
		return dao.getFolderIdsHierarchy(startFolderId);
	}

	/**
	 * @see vwg.vw.km.application.implementation.FolderManager#getAllSubFolders(Long, List, Long)
	 */
	public void getAllSubFolders(Long folderId, List<Long> list, Long objectTypeId) {
		List<FolderModel> listUnder = getFoldersUnder(folderId, objectTypeId);
		if (listUnder != null && !listUnder.isEmpty()) {
			for (FolderModel subFolder : listUnder) {
				list.add(subFolder.getFolderId());
				getAllSubFolders(subFolder.getFolderId(), list, objectTypeId);
			}
		} else {
			return;
		}
	}

	/**
	 * @see vwg.vw.km.application.implementation.FolderManager#getComponentExportedFolders()
	 */
	public List<FolderModel> getComponentExportedFolders() {
		return dao.getComponentExportedFolders();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.application.implementation.FolderManager#getWorkingFoldersUnder(java.lang.Long, java.lang.Long,
	 * java.lang.Long)
	 */
	public List<FolderModel> getWorkingFoldersUnder(Long folderId, Long objectTypeId, Long userId) {
		return dao.getWorkingFoldersUnder(folderId, objectTypeId, userId);
	}

	public FolderModel getFolderByExternalID(String externalID) {
		return dao.getFolderByExternalID(externalID);
	}

	public void getFolderParentsUtilFolder(FolderModel folder, Long parentEndId, Set<FolderModel> parents) {
		if (!parentEndId.equals(folder.getFolderId()) && folder.getFolder() != null) {
			if (!parentEndId.equals(folder.getFolder().getFolderId())) {
				getFolderParentsUtilFolder(folder.getFolder(), parentEndId, parents);
			}
			parents.add(folder.getFolder());
		}
	}

	public boolean getHasFoldersUnder(Long folderId, Long objectTypeId) {
		return dao.getHasFoldersUnder(folderId, objectTypeId);
	}

	public List<FolderModel> getFoldersBySearchString(Long parentFolder, String searchString, Long objectTypeId) {
		return dao.getFoldersBySearchString(parentFolder, objectTypeId, searchString);
	}
}

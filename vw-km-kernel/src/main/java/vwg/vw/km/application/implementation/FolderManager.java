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
import java.util.Set;

import vwg.vw.km.application.implementation.base.BaseManager;
import vwg.vw.km.integration.persistence.dao.FolderDAO;
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
 * @version $Revision: 1.17 $ $Date: 2013/10/24 10:08:44 $
 */
public interface FolderManager extends BaseManager<FolderModel, FolderDAO> {

	/**
	 * return list of sub folders for given folder id
	 * 
	 * @param folderId
	 * @param objectTypeId
	 *            type of folder
	 * @return
	 */
	public List<FolderModel> getFoldersUnder(Long folderId, Long objectTypeId);

	/**
	 * Check duplicated folder name for sub folders
	 * 
	 * @param parentFolderId
	 * @param folderId
	 * @param name
	 * @return
	 */
	public FolderModel getFolderUnderParentByName(Long parentFolderId, Long folderId, String name);

	/**
	 * getMaxFolderId
	 * 
	 * @return
	 */
	public Long getMaxFolderId();

	/**
	 * get Folder Ids Hierarchy from start folder to the top of tree
	 * 
	 * @param startFolderId
	 * @return
	 */
	public List<Long> getFolderIdsHierarchy(Long startFolderId);

	/**
	 * set ids of all subFolder under current folder
	 * 
	 * @param folderId
	 * @param list
	 * @param objectTypeId
	 */
	public void getAllSubFolders(Long folderId, List<Long> list, Long objectTypeId);

	/**
	 * get Component Exported Folders
	 * 
	 * @return
	 */
	public List<FolderModel> getComponentExportedFolders();

	/**
	 * get the list of folders under the user arbeitsordner
	 * 
	 * @param folderId
	 * @param objectTypeId
	 * @param userId
	 * @return
	 */
	public List<FolderModel> getWorkingFoldersUnder(Long folderId, Long objectTypeId, Long userId);

	/**
	 * Get a Folder by his external Id
	 * 
	 * @param externalID
	 * @return
	 */
	public FolderModel getFolderByExternalID(String externalID);

	public void getFolderParentsUtilFolder(FolderModel folder, Long parentEndId, Set<FolderModel> parents);

	public boolean getHasFoldersUnder(Long folderId, Long objectTypeId);

	/**
	 * 24.10.2013: ZS: PTS_Requirement-22210: Search folders
	 * 
	 * @param parentFolder
	 * @param searchString
	 * @param objectTypeId
	 *            TODO
	 * @return
	 */
	public List<FolderModel> getFoldersBySearchString(Long parentFolder, String searchString, Long objectTypeId);
}

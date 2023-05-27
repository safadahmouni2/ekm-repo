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
package vwg.vw.km.application.service.dto.base;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
 * @author Sebri Zouhaier changed by $Author: saidi $
 * @version $Revision: 1.21 $ $Date: 2013/10/24 10:08:44 $
 */
public abstract class BaseDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final String REMARKS_SEPARATOR = "<br/>";

	protected UserModel userLogged;

	protected Set<String> pageChanges = new HashSet<String>();

	protected boolean showSaveChangesPopup = false;

	protected boolean searchMode = false;

	protected boolean newMode = false;

	private boolean ascending = false;

	// list of search result
	protected List<FolderModel> folders;

	public void setAscending(boolean ascending) {
		this.ascending = ascending;
	}

	public boolean getAscending() {
		return ascending;
	}

	public UserModel getUserLogged() {
		return userLogged;
	}

	public void setUserLogged(UserModel userLogged) {
		this.userLogged = userLogged;
	}

	public Set<String> getPageChanges() {
		return pageChanges;
	}

	public void setPageChanges(Set<String> pageChanges) {
		this.pageChanges = pageChanges;
	}

	public boolean isShowSaveChangesPopup() {
		return showSaveChangesPopup;
	}

	public void setShowSaveChangesPopup(boolean showSaveChangesPopup) {
		this.showSaveChangesPopup = showSaveChangesPopup;
	}

	public boolean isSearchMode() {
		return searchMode;
	}

	public void setSearchMode(boolean searchMode) {
		this.searchMode = searchMode;
	}

	protected boolean searchTitel = Boolean.TRUE;

	public boolean isSearchTitel() {
		return searchTitel;
	}

	public void setSearchTitel(boolean searchTitel) {
		this.searchTitel = searchTitel;
	}

	private String searchString = "";

	public String getSearchString() {
		if (searchString != null) {
			searchString = searchString.trim();
		}
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	private String searchStringResult = "";

	public String getSearchStringResult() {
		return searchStringResult;
	}

	public void setSearchStringResult(String searchStringResult) {
		this.searchStringResult = searchStringResult;
	}

	private Long folderId = null;

	public Long getFolderId() {
		return folderId;
	}

	public void setFolderId(Long folderId) {
		this.folderId = folderId;
	}

	private String searchFolder = "";

	public String getSearchFolder() {
		return searchFolder;
	}

	public void setSearchFolder(String searchFolder) {
		this.searchFolder = searchFolder;
	}

	protected boolean noSearchResult = Boolean.FALSE;

	public boolean isNoSearchResult() {
		return noSearchResult;
	}

	public void setNoSearchResult(boolean noSearchResult) {
		this.noSearchResult = noSearchResult;
	}

	/**
	 * copy Util Attributes For Search operation from start DTO to the new search DTO
	 * 
	 * @return new DTO
	 */
	public BaseDTO copyUtilAttributesForSearch() {
		throw new RuntimeException("Implement me for search");
	}

	public boolean isNewMode() {
		return newMode;
	}

	public void setNewMode(boolean newMode) {
		this.newMode = newMode;
	}

	// 24.10.2013: ZS: PTS_Requirement-22210: Search folders

	/**
	 * @return
	 */
	public List<FolderModel> getFolders() {
		return folders;
	}

	/**
	 * @param folders
	 */
	public void setFolders(List<FolderModel> folders) {
		this.folders = folders;
	}

	/**
	 * 24.10.2013: ZS: PTS_Requirement-22210: Search folders
	 * 
	 * @return
	 */
	public boolean isNoFoldersSearchResult() {
		if (folders != null && !folders.isEmpty()) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

}

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
package vwg.vw.km.application.service.dto;

import java.util.List;

import vwg.vw.km.application.implementation.HourlyRateCatalogManager;
import vwg.vw.km.application.implementation.RoleManager;
import vwg.vw.km.application.service.dto.base.BaseDTO;
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
 * @author Sebri Zouhaier changed by $Author: zouhair $
 * @version $Revision: 1.24 $ $Date: 2016/11/02 17:41:58 $
 */
public class UserDTO extends BaseDTO {

	private static final long serialVersionUID = 5543468454130133112L;

	private UserModel user;

	private Long userId;

	private List<UserModel> users;

	private Long workAreaId = null;

	public Long getWorkAreaId() {
		return workAreaId;
	}

	public void setWorkAreaId(Long workAreaId) {
		this.workAreaId = workAreaId;
	}

	public boolean dtoInited = false;

	public boolean userCreationAllowedOnPage = Boolean.TRUE;

	/**
	 * Check if the user can Add or not
	 * 
	 * @return
	 */
	public boolean isUserCreationAllowed() {
		// ZS: PTS_Change-34868: Add check on Role Root Administrator for the New action
		if (!getUserLogged().haveRole(RoleManager.Role.ROOT_ADMINISTRATOR.value())) {
			return Boolean.FALSE;
		}
		return userCreationAllowedOnPage;
	}

	public void setUserCreationAllowedOnPage(boolean userCreationAllowed) {
		this.userCreationAllowedOnPage = userCreationAllowed;
	}

	public UserDTO() {
		user = new UserModel();
		user.setDefaultStdSatzKatRefId(HourlyRateCatalogManager.DEFAULT_CATALOG_ID);
		user.setStatusElementPublish(Boolean.FALSE);
	}

	public UserModel getUser() {
		return user;
	}

	public void setUser(UserModel user) {
		this.user = user;
	}

	public List<UserModel> getUsers() {
		return users;
	}

	public void setUsers(List<UserModel> users) {
		this.users = users;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public boolean isNoUsers() {
		if (users != null && !users.isEmpty()) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	/**
	 * @see vwg.vw.km.application.service.dto.base.BaseDTO#copyUtilAttributesForSearch()
	 */
	public UserDTO copyUtilAttributesForSearch() {
		UserDTO userDTO = new UserDTO();
		userDTO.setSearchString(getSearchString());
		userDTO.setSearchStringResult(getSearchString());
		userDTO.setSearchMode(Boolean.TRUE);
		userDTO.setNoSearchResult(isNoSearchResult());
		// if no result in no result page
		if (isNoSearchResult()) {
			userDTO.setSearchFolder(getSearchFolder());
			userDTO.setFolderId(getFolderId());
		}
		return userDTO;
	}

	/**
	 * Check if the user can delete or not
	 * 
	 * @return
	 */
	public boolean isUserDeleteAllowed() {
		// ZS: PTS_Change-34658: Add check on Role Root Administrator for the delete action in edit and list mask
		if (getUserLogged().haveRole(RoleManager.Role.ROOT_ADMINISTRATOR.value())) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
}

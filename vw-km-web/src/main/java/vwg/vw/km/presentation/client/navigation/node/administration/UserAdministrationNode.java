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
package vwg.vw.km.presentation.client.navigation.node.administration;

import java.util.ArrayList;
import java.util.List;

import vwg.vw.km.application.implementation.EnumObjectTypeManager;
import vwg.vw.km.application.implementation.RoleManager;
import vwg.vw.km.application.service.dto.UserDTO;
import vwg.vw.km.application.service.dto.WorkAreaDTO;
import vwg.vw.km.integration.persistence.model.UserModel;
import vwg.vw.km.integration.persistence.model.WorkAreaModel;
import vwg.vw.km.presentation.client.navigation.NavigationBean;
import vwg.vw.km.presentation.client.navigation.node.ContextMenuEnum;
import vwg.vw.km.presentation.client.navigation.node.FolderNode;
import vwg.vw.km.presentation.client.navigation.node.Node;
import vwg.vw.km.presentation.client.navigation.node.StaticFolderNode;

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
 * @author Sebri Zouhaier changed by $Author: mrad $
 * @version $Revision: 1.42 $ $Date: 2016/11/01 10:04:42 $
 */
public class UserAdministrationNode extends StaticFolderNode {

	private UserSearchResultNode userSearchResultNode = new UserSearchResultNode(treeBean, this);

	public UserAdministrationNode(NavigationBean tree, FolderNode parent, boolean addSearchNode) {
		super(tree, parent, 3L);

		// ZS: PTS_Change-34868: Add check on Role Root Administrator for the New action
		if (treeBean.getUserFromSession().haveRole(RoleManager.Role.ROOT_ADMINISTRATOR.value())) {
			setContextMenu(ContextMenuEnum.StaticFolderContextMenu);
		}
		setTemplateName("./administration/userAdministration/userList.xhtml");
		setAddObjectIcon("add-user");
		if (addSearchNode) {
			treeBean.getObjectTypeSearchResultNode().put(getObjectContentId(), userSearchResultNode);
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.FolderNode#getChilds()
	 */
	@Override
	public List<Node> getChilds() {
		List<Node> childs = new ArrayList<Node>();
		childs.add(userSearchResultNode);
		// load work areas
		List<WorkAreaModel> workAreas = treeBean.getWorkAreaService().loadWorkAreas(new WorkAreaDTO()).getWorkAreas();
		for (WorkAreaModel workAreaModel : workAreas) {
			WorkAreaUserListNode n = new WorkAreaUserListNode(treeBean, this, workAreaModel.getDesignation(),
					workAreaModel.getWorkAreaId());
			childs.add(n);
		}

		// load users
		UserDTO d = new UserDTO();
		d.setUserLogged(treeBean.getUserFromSession());
		d.setWorkAreaId(null);
		List<UserModel> list = treeBean.getUserService().loadUsers(d).getUsers();
		setDto(d);
		for (UserModel userModel : list) {
			String userLabel = (userModel.getFirstName() != null)
					? userModel.getLastName() + " " + userModel.getFirstName()
					: userModel.getLastName();
			UserNode n = new UserNode(treeBean, this, userLabel, userModel.getUserId(), Boolean.FALSE);
			childs.add(n);
			objectChildsCount++;
			indexOfLastObject = childs.size() - 1;
		}
		return childs;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.FolderNode#getNewObjectNodeInstance()
	 */
	@Override
	public UserNode getNewObjectNodeInstance() {
		return new UserNode(treeBean, this, treeBean.getMessage("user.newUser.tree.msge"), null, Boolean.FALSE);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.FolderNode#getObjectContentId()
	 */
	@Override
	public Long getObjectContentId() {
		return EnumObjectTypeManager.USER_ID;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#getRightIds()
	 */
	@Override
	public List<Long> getRightIds() {
		List<Long> rights = new ArrayList<Long>();
		rights.add(RoleManager.Right.VIEW_USER.value());
		return rights;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#getCRUDRightIds()
	 */
	@Override
	public List<Long> getCRUDRightIds() {
		List<Long> rights = new ArrayList<Long>();
		rights.add(RoleManager.Right.MANAGE_USER.value());
		return rights;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.FolderNode#getListClickedMessage()
	 */
	@Override
	protected String getListClickedMessage() {
		return treeBean.getMessage("action.user.list");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#getCutMessage()
	 */
	@Override
	protected String getCutMessage() {
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#getDeleteSuccessMessage()
	 */
	@Override
	protected String getDeleteSuccessMessage() {
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.FolderNode#getPasteMessage()
	 */
	@Override
	protected String getPasteMessage() {
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#getEditMessage()
	 */
	@Override
	protected String getEditMessage() {
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.FolderNode#getCreateMessage()
	 */
	@Override
	protected String getCreateMessage() {
		return treeBean.getMessage("action.user.create");
	}
}

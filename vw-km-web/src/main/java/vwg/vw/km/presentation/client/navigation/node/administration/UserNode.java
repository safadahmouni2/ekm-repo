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

import java.util.List;

import javax.faces.event.ActionEvent;

import vwg.vw.km.application.implementation.EnumObjectTypeManager;
import vwg.vw.km.application.service.dto.UserDTO;
import vwg.vw.km.integration.persistence.model.UserModel;
import vwg.vw.km.presentation.client.navigation.NavigationBean;
import vwg.vw.km.presentation.client.navigation.node.FolderNode;
import vwg.vw.km.presentation.client.navigation.node.ObjectNode;

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
 * @version $Revision: 1.30 $ $Date: 2014/03/06 08:57:31 $
 */
public class UserNode extends ObjectNode {

	public UserNode(NavigationBean tree, FolderNode parent, String text, Long userId, boolean isSearchNode) {
		super(tree, parent, isSearchNode);
		setMenuDisplayText(text);
		setMenuContentTitle(text);
		setFromBundle(Boolean.FALSE);
		setTemplateName("./administration/userAdministration/userAdministration.xhtml");
		setPopupPageName("./administration/userAdministration/popup.xhtml");
		setLeafIcon(makeIcon("user.gif"));
		setNodeId(userId);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.ObjectNode#specificNodeClicked()
	 */
	@Override
	public void specificNodeClicked() {
		refresh();
		UserDTO userDto = new UserDTO();
		userDto.setUserLogged(treeBean.getUserFromSession());
		userDto.setUserId(getNodeId());
		setDto(treeBean.getUserService().loadUserDTO(userDto));
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#getObjectTypeId()
	 */
	@Override
	public Long getObjectTypeId() {
		return EnumObjectTypeManager.USER_ID;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#getDeleteMessage()
	 */
	@Override
	public String getDeleteMessage() {
		return treeBean.getMessage("userAdministration.deleteConfirm");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#specificDeleteClicked(javax.faces.event.ActionEvent)
	 */
	@Override
	public String specificDeleteClicked(ActionEvent event) {
		specificNodeClicked();
		UserDTO dto = (UserDTO) getDto();
		dto.setUserId(dto.getUser().getUserId());
		treeBean.getUserService().delete(dto);

		// ZS: PTS_Problem-36558: Refresh Problem when deleting an object
		// Refresh the User administration folder
		treeBean.getUserAdministrationNode().expandClicked(event);
		// if the deleted user is in search result folders it will be removed
		UserSearchResultNode searchNode = (UserSearchResultNode) treeBean.getObjectTypeSearchResultNode()
				.get(EnumObjectTypeManager.USER_ID);
		if (searchNode.getDto() != null) {
			List<UserModel> users = ((UserDTO) searchNode.getDto()).getUsers();
			for (UserModel user : users) {
				if (user.getUserId().equals(getNodeId())) {
					users.remove(user);
					((UserDTO) searchNode.getDto()).setUsers(users);
					searchNode.expandClicked(event);
					break;
				}
			}
		}
		return "";
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
		return treeBean.getMessage("action.user.delete");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.ObjectNode#getCopyMessage()
	 */
	@Override
	protected String getCopyMessage() {
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#getEditMessage()
	 */
	@Override
	protected String getEditMessage() {
		return treeBean.getMessage("action.user.edit");
	}
}

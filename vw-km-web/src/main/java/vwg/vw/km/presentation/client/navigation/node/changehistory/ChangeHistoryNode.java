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
package vwg.vw.km.presentation.client.navigation.node.changehistory;
import vwg.vw.km.presentation.client.navigation.node.StaticNodeIdEnum;
import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;

import vwg.vw.km.application.implementation.EnumObjectTypeManager;
import vwg.vw.km.application.implementation.RoleManager;
import vwg.vw.km.application.service.dto.ChangeHistoryDTO;
import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;
import vwg.vw.km.integration.persistence.model.UserModel;
import vwg.vw.km.presentation.client.navigation.NavigationBean;
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
 * @author boubakers changed by $Author: saidi $
 * @version $Revision: 1.18 $ $Date: 2014/01/29 16:20:49 $
 */

public class ChangeHistoryNode extends StaticFolderNode {

	/**
	 * PTS requirement 22211: Benachrichtigungsfunktion Responsible :ABA Change ChangeHistoryNode to extends from
	 * ObjectNode to StaticFolderNode because we will add a child node for Last Changes Notifications Override getChilds
	 * and nodeClicked methods
	 */

	// logger
	private final Log log = LogManager.get().getLog(ChangeHistoryNode.class);
	public static String CHANGE_HISTORY_SEARCH_MASK = "./changeHistory/changeHistorySearch.xhtml";
	LastChangesNotificationsNode changesNotificationsNode = new LastChangesNotificationsNode(treeBean, this);

	public ChangeHistoryNode(NavigationBean tree, FolderNode parent) {
		super(tree, parent, StaticNodeIdEnum.CHANGE_HISTORY_NODE.value());
		setTemplateName(CHANGE_HISTORY_SEARCH_MASK);
		setPopupPageName("./changeHistory/popup.xhtml");
		setLeafIcon("./xmlhttp/css/xp/css-images/tree_folder_close.gif");
		setActiveFilter(treeBean.getUserFromSession().getDisableChangesNotifications());
	}

	@Override
	public void nodeClicked(ActionEvent event) {
		if (log.isInfoEnabled()) {
			log.info("Changes History parent node clicked ");
		}

		if (treeBean.getNeedSaveMessageConfirmation()) {
			treeBean.setGotoNode(this);
			return;
		}
		expandClicked(event);
		if (!this.expanded) {
			this.setExpanded(Boolean.TRUE);
		} else {
			this.setExpanded(Boolean.FALSE);
		}
		treeBean.setSelectedNodeObject(this);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.FolderNode#getChilds()
	 */
	@Override
	public List<Node> getChilds() {
		UserModel user = treeBean.getUserFromSession();
		ChangeHistoryDTO changeHistoryDTO = new ChangeHistoryDTO();
		changeHistoryDTO.setFolderId(getNodeId());
		changeHistoryDTO.setUserLogged(user);

		List<Node> childs = new ArrayList<Node>();
		// If the filter is not applied the node must be displayed
		if (user.getDisableChangesNotifications() == false) {
			if (log.isInfoEnabled()) {
				log.info("Changes Notifications filter is disabled by user " + user.getFullName());
			}
			changesNotificationsNode.setVisible(Boolean.TRUE);
		} else {
			if (log.isInfoEnabled()) {
				log.info("Changes Notifications filter is enabled by user " + user.getFullName());
			}
			if (changesNotificationsNode.isAccessAllowed()) {
				changesNotificationsNode.setVisible(Boolean.FALSE);
			}
		}

		childs.add(changesNotificationsNode);
		objectChildsCount++;
		setDto(treeBean.getChangeHistoryService().loadDTO(changeHistoryDTO));
		setTemplateName(CHANGE_HISTORY_SEARCH_MASK);
		return childs;
	}

	/**
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#getRightIds()
	 */
	@Override
	public List<Long> getRightIds() {
		List<Long> rights = new ArrayList<Long>();
		rights.add(RoleManager.Right.MANAGE_COMPONENT.value());
		rights.add(RoleManager.Right.MANAGE_ELEMENT.value());
		return rights;
	}

	@Override
	protected String getCopyMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getCutMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getDeleteSuccessMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getEditMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getPasteMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean moveObjectNodeToFolder(Node node, FolderNode toNode, boolean isCopy) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getCreateMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getListClickedMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getObjectContentId() {
		return EnumObjectTypeManager.FOLDER_ID;
	}

}

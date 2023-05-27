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

import java.util.ArrayList;
import java.util.List;

import vwg.vw.km.application.implementation.EnumObjectTypeManager;
import vwg.vw.km.application.implementation.RoleManager;
import vwg.vw.km.application.service.dto.ChangeHistoryDTO;
import vwg.vw.km.presentation.client.navigation.NavigationBean;
import vwg.vw.km.presentation.client.navigation.node.FolderNode;
import vwg.vw.km.presentation.client.navigation.node.LeafNode;

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
 * @author abir changed by $Author: saidi $
 * @version $Revision: 1.6 $ $Date: 2014/01/20 13:20:49 $
 */
public class LastChangesNotificationsNode extends LeafNode {
	/**
	 * PTS requirement 22211:ABA :Benachrichtigungsfunktion
	 */
	public static String CHANGE_HISTORY_NOTIFICATION_MASK = "./changeHistory/lastChangeHistoryNotifications.xhtml";

	public LastChangesNotificationsNode(NavigationBean tree, FolderNode parent) {
		super(tree, parent);
		setNodeId(16L);
		setTemplateName(CHANGE_HISTORY_NOTIFICATION_MASK);
		setLeafIcon(makeIcon("f_c.gif"));

	}

	@Override
	public void specificLeafNodeClicked() {
		treeBean.setSelectedNodeObject(this);
		ChangeHistoryDTO dto = new ChangeHistoryDTO();
		dto.setFromLastChangesPage(Boolean.TRUE);
		setDto(dto);
		// If the user has Logged IN once, get changes list from last loginTime to current login time
		if (treeBean.getUserFromSession().getPreviousLoginTime() != null) {
			setDto(treeBean.getChangeHistoryService().loadDTOForLastChangeNotifications(dto));
			// From Date is last login dateTime
			dto.getSearchObject().setFromDate(treeBean.getUserFromSession().getPreviousLoginTime().getDate());
			// Get last Changes from last login
			treeBean.getChangeHistoryService().search(dto);
		}
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
	public Long getObjectTypeId() {
		return EnumObjectTypeManager.FOLDER_ID;
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
}

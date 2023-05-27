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

import javax.faces.event.ActionEvent;

import vwg.vw.km.application.implementation.EnumObjectTypeManager;
import vwg.vw.km.application.service.dto.WorkAreaDTO;
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
 * @version $Revision: 1.29 $ $Date: 2014/01/20 13:20:49 $
 */
public class WorkAreaNode extends ObjectNode {

	public static final String WORK_AREA_ATTACHED_MESSAGE_ID = "workArea.attachedToUser.message";

	public static final String WORK_AREA_ATTACHED_TO_CATALOG_MESSAGE_ID = "workArea.attachedToCatalog.message";

	public WorkAreaNode(NavigationBean tree, FolderNode parent, String text, Long workAreaId, boolean isSearchNode,
			String brandIcon) {
		super(tree, parent, isSearchNode);
		setMenuDisplayText(text);
		setMenuContentTitle(text);
		setFromBundle(Boolean.FALSE);
		setTemplateName("./administration/workAreaAdministration/workArea.xhtml");
		setPopupPageName("./administration/workAreaAdministration/popup.xhtml");
		setLeafIcon(makeIcon("work_area.gif"));
		setOwnerImg(brandIcon);
		setNodeId(workAreaId);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.ObjectNode#specificNodeClicked()
	 */
	@Override
	public void specificNodeClicked() {
		refresh();
		// fill DTO ()
		setDto(treeBean.getWorkAreaService().loadDTO(new WorkAreaDTO(), getNodeId()));
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#getObjectTypeId()
	 */
	@Override
	public Long getObjectTypeId() {
		return EnumObjectTypeManager.WORK_AREA_ID;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#specificDeleteClicked(javax.faces.event.ActionEvent)
	 */
	@Override
	public String specificDeleteClicked(ActionEvent event) {
		specificNodeClicked();
		WorkAreaDTO dto = (WorkAreaDTO) getDto();
		treeBean.getWorkAreaService().getIsWorkAreaAttached(dto);
		treeBean.getWorkAreaService().getIsWorkAreaAttachedToCatalog(dto);
		if (dto.getIsWorkAreaAttached()) {
			return treeBean.getMessage(WORK_AREA_ATTACHED_MESSAGE_ID);
		} else if (dto.getIsWorkAreaAttachedToCatalog()) {
			return treeBean.getMessage(WORK_AREA_ATTACHED_TO_CATALOG_MESSAGE_ID);
		}
		// no problem to delete object
		treeBean.getWorkAreaService().delete(dto);
		return "";
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#getDeleteMessage()
	 */
	@Override
	public String getDeleteMessage() {
		return treeBean.getMessage("workArea.deleteConfirm");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.ObjectNode#getCopyMessage()
	 */
	@Override
	protected String getCopyMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#getEditMessage()
	 */
	@Override
	protected String getEditMessage() {
		return treeBean.getMessage("action.workArea.edit");
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
		return treeBean.getMessage("action.workArea.delete");
	}
}

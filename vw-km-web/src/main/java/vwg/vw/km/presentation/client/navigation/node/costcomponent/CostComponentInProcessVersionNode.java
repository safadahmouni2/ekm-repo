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
package vwg.vw.km.presentation.client.navigation.node.costcomponent;

import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import vwg.vw.km.application.implementation.EnumObjectTypeManager;
import vwg.vw.km.application.implementation.EnumStatusManager;
import vwg.vw.km.application.service.dto.CostComponentDTO;
import vwg.vw.km.integration.persistence.model.ComponentModel;
import vwg.vw.km.integration.persistence.model.FolderModel;
import vwg.vw.km.presentation.client.navigation.NavigationBean;
import vwg.vw.km.presentation.client.navigation.node.BranchNode;
import vwg.vw.km.presentation.client.navigation.node.FolderNode;
import vwg.vw.km.presentation.client.navigation.node.ObjectNode;
import vwg.vw.km.presentation.client.navigation.node.SearchFolderNode;
import vwg.vw.km.presentation.client.navigation.node.working.WorkingCostComponentNode;

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
 * @author Ziad Saidi changed by $Author: mrada $
 * @version $Revision: 1.15 $ $Date: 2014/03/06 13:54:58 $
 */
public class CostComponentInProcessVersionNode extends ObjectNode {

	public CostComponentInProcessVersionNode(NavigationBean tree, BranchNode parent, String text, Long costComponentId,
			boolean schowLastVersion, boolean isSearchNode, List<String> users) {
		super(tree, parent, isSearchNode);
		setMenuDisplayText(text);
		setMenuContentTitle(text);
		setFromBundle(Boolean.FALSE);
		setTemplateName("./costComponent/costComponent.xhtml");
		setPopupPageName("./costComponent/popup.xhtml");
		setNodeId(costComponentId);
		setLeafIcon("images/es.gif");
		setUsers(users);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#getObjectTypeId()
	 */
	@Override
	public Long getObjectTypeId() {
		return EnumObjectTypeManager.COMPONENT_ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.ObjectNode#specificNodeClicked()
	 */
	@Override
	public void specificNodeClicked() {

		refresh();

		CostComponentDTO costComponentDTO = new CostComponentDTO();
		costComponentDTO.setUserLogged(treeBean.getUserFromSession());
		setDto(treeBean.getCostComponentService().loadDTOForInProcessVersion(costComponentDTO, getNodeId()));
		// ZS: PTS_Problem-36558: Refresh Problem when deleting an object
		// click node from search result
		if (this.getParent().getParent() instanceof SearchFolderNode) {
			showComponent(new ActionEvent(FacesContext.getCurrentInstance().getViewRoot()));
		} else {
			ComponentModel component = costComponentDTO.getComponentVersion().getComponent();
			component.setUsers(treeBean.getCostComponentService().loadComponentVersionUsers(component));
			getParent().setUsers(component.getUsersImg());
			setUsers(costComponentDTO.getComponentVersion().getUsersImg());
			treeBean.getNavigationElementBean().setDirty(true);
		}
	}

	/**
	 * Go to the component in his regular folder
	 * 
	 * @param event
	 */
	private void showComponent(ActionEvent event) {
		CostComponentDTO dto = (CostComponentDTO) getDto();

		Long componentId = dto.getComponentVersion().getComponent().getComponentId();
		Long folderId = dto.getComponentVersion().getComponent().getFolder().getFolderId();
		FolderModel folder = treeBean.getNavigationService().getFolder(folderId);
		// component is in a working folder
		if (folder.getCreatorRefId() != null) {
			Long userId = folder.getCreatorRefId();
			FolderNode workingNode = treeBean.getWorkingNode();
			workingNode.setExpanded(true);
			workingNode.expandClicked(event);

			FolderNode userWorkingFolder = (FolderNode) workingNode.getNodeById(userId,
					EnumObjectTypeManager.FOLDER_ID);
			WorkingCostComponentNode n = (WorkingCostComponentNode) treeBean.goToNode(userWorkingFolder, componentId,
					folderId, EnumObjectTypeManager.COMPONENT_ID, event);
			if (n != null) {
				n.nodeClicked(event);
			}
		} else {
			CostComponentNode n = (CostComponentNode) treeBean.goToNode(componentId, folderId,
					EnumObjectTypeManager.COMPONENT_ID, event);
			if (n != null) {
				n.nodeClicked(event);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.BranchNode#getDeleteMessage()
	 */
	@Override
	public String getDeleteMessage() {
		return treeBean.getMessage("costcomponent.deleteConfirm");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#specificDeleteClicked(javax.faces.event.ActionEvent)
	 */
	@Override
	public String specificDeleteClicked(ActionEvent event) {
		// specificNodeClicked(); Removed to avoid delete of in use
		CostComponentDTO dto = (CostComponentDTO) getDto();
		dto.setComponentVersionId(dto.getComponentVersion().getComponentVersionId());
		treeBean.getCostComponentService().deleteVersion(dto);
		// ZS: PTS_Problem-36558: Refresh Problem when deleting an object
		// Refresh the search folder
		Long status = dto.getComponentVersion().getEnumStatus().getEnumStatusId();
		if (status.equals(EnumStatusManager.Status.CREATED.value())
				|| status.equals(EnumStatusManager.Status.IN_PROGRESS.value())
				|| status.equals(EnumStatusManager.Status.TO_BE_APPROVED.value())) {
			treeBean.refreshSearchStatusNode(event, EnumObjectTypeManager.COMPONENT_ID, null, status);
		}
		// if the deleted component is in search result folders it will be removed
		CostComponentSearchResultNode searchNode = (CostComponentSearchResultNode) treeBean
				.getObjectTypeSearchResultNode().get(EnumObjectTypeManager.COMPONENT_ID);
		if (searchNode.getDto() != null) {
			List<ComponentModel> components = ((CostComponentDTO) searchNode.getDto()).getComponents();
			for (ComponentModel component : components) {
				if (component.getComponentId().equals(this.getParent().getNodeId())) {
					components.remove(component);
					((CostComponentDTO) searchNode.getDto()).setComponents(components);
					searchNode.expandClicked(event);
					break;
				}
			}
		}
		if (dto.isComponentDeleted()) {
			setParent(getParent().getParent());
		}
		return "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#getDeleteSuccessMessage()
	 */
	@Override
	protected String getDeleteSuccessMessage() {
		return treeBean.getMessage("action.costComponent.delete");
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
	protected String getEditMessage() {
		return treeBean.getMessage("action.costComponent.edit");
	}
}

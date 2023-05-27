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
package vwg.vw.km.presentation.client.navigation.node.costelement;

import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import vwg.vw.km.application.implementation.EnumObjectTypeManager;
import vwg.vw.km.application.implementation.EnumStatusManager;
import vwg.vw.km.application.service.dto.CostElementDTO;
import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;
import vwg.vw.km.integration.persistence.model.ElementModel;
import vwg.vw.km.integration.persistence.model.FolderModel;
import vwg.vw.km.presentation.client.navigation.NavigationBean;
import vwg.vw.km.presentation.client.navigation.node.BranchNode;
import vwg.vw.km.presentation.client.navigation.node.FolderNode;
import vwg.vw.km.presentation.client.navigation.node.ObjectNode;
import vwg.vw.km.presentation.client.navigation.node.SearchFolderNode;
import vwg.vw.km.presentation.client.navigation.node.working.WorkingCostElementNode;

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
 * @author Sebri Zouhaier changed by $Author: mrada $
 * @version $Revision: 1.22 $ $Date: 2014/03/06 13:54:58 $
 */
public class CostElementVersionNode extends ObjectNode {

	private final Log log = LogManager.get().getLog(CostElementVersionNode.class);

	public CostElementVersionNode(NavigationBean tree, BranchNode parent, String text, Long costElementVersionId,
			boolean schowLastVersion, boolean isSearchNode, String owner, List<String> users) {
		super(tree, parent, isSearchNode);
		setMenuDisplayText(text);
		setMenuContentTitle(text);
		setFromBundle(Boolean.FALSE);
		setTemplateName("./costElement/costElement.xhtml");
		setPopupPageName("./costElement/popup.xhtml");
		setLeafIcon(makeIcon("es.gif"));
		setNodeId(costElementVersionId);
		setUsers(users);
		setOwnerImg(owner);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#getObjectTypeId()
	 */
	@Override
	public Long getObjectTypeId() {
		return EnumObjectTypeManager.ELEMENT_ID;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.ObjectNode#specificNodeClicked()
	 */

	@Override
	public void specificNodeClicked() {

		log.info("Element version clicked: " + getNodeId());
		refresh();
		CostElementDTO costElementDTO = new CostElementDTO();
		costElementDTO.setUserLogged(treeBean.getUserFromSession());
		setDto(treeBean.getCostElementService().loadDTOForDetail(costElementDTO, getNodeId(),
				this.getParent().getNodeId(), Boolean.FALSE, Boolean.FALSE));
		// AMR: PTS_Problem-36558: Refresh Problem when deleting an object
		if (this.getParent().getParent() instanceof SearchFolderNode) {
			showElement(new ActionEvent(FacesContext.getCurrentInstance().getViewRoot()));
		} else {
			setUsers(costElementDTO.getElementVersion().getUsersImg());
			getParent().setUsers(costElementDTO.getElementVersion().getElement().getUsersImg());
			treeBean.getNavigationElementBean().setDirty(true);
		}

	}

	/**
	 * 
	 * @param event
	 */
	private void showElement(ActionEvent event) {
		CostElementDTO dto = (CostElementDTO) getDto();
		Long elementId = dto.getElementVersion().getElement().getElementId();
		Long folderId = dto.getElementVersion().getElement().getFolder().getFolderId();
		FolderModel folder = treeBean.getNavigationService().getFolder(folderId);
		if (folder.getCreatorRefId() != null) {
			Long userId = folder.getCreatorRefId();
			FolderNode workingNode = treeBean.getWorkingNode();
			workingNode.setExpanded(true);
			workingNode.expandClicked(event);

			FolderNode userWorkingFolder = (FolderNode) workingNode.getNodeById(userId,
					EnumObjectTypeManager.FOLDER_ID);
			WorkingCostElementNode n = (WorkingCostElementNode) treeBean.goToNode(userWorkingFolder, elementId,
					folderId, EnumObjectTypeManager.ELEMENT_ID, event);
			if (n != null) {
				n.nodeClicked(event);
				n.setExpanded(true);
				n.expandClicked(event);
			}
		} else {
			CostElementNode n = (CostElementNode) treeBean.goToNode(elementId, folderId,
					EnumObjectTypeManager.ELEMENT_ID, event);
			if (n != null) {
				n.nodeClicked(event);
				n.setExpanded(true);
				n.expandClicked(event);
			}
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#getDeleteMessage()
	 */
	@Override
	public String getDeleteMessage() {
		return treeBean.getMessage("costelement.deleteConfirm");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#specificDeleteClicked(javax.faces.event.ActionEvent)
	 */
	@Override
	public String specificDeleteClicked(ActionEvent event) {

		// specificNodeClicked(); Removed to avoid delete of in use
		CostElementDTO dto = (CostElementDTO) getDto();
		dto.setElementVersionId(dto.getElementVersion().getElementVersionId());
		treeBean.getCostElementService().deleteVersion(dto);
		treeBean.getNavigationElementBean().setDirty(true);

		// AMR: PTS_Problem-36558: Refresh Problem when deleting an object
		// Refresh the search folder
		Long status = dto.getElementVersion().getEnumStatus().getEnumStatusId();
		if (status.equals(EnumStatusManager.Status.CREATED.value())
				|| status.equals(EnumStatusManager.Status.IN_PROGRESS.value())
				|| status.equals(EnumStatusManager.Status.TO_BE_APPROVED.value())) {
			treeBean.refreshSearchStatusNode(event, EnumObjectTypeManager.ELEMENT_ID, null, status);
		}
		// if the deleted component is in search result folders it will be removed
		CostElementSearchResultNode searchNode = (CostElementSearchResultNode) treeBean.getObjectTypeSearchResultNode()
				.get(EnumObjectTypeManager.ELEMENT_ID);
		if (searchNode.getDto() != null) {
			List<ElementModel> elements = ((CostElementDTO) searchNode.getDto()).getElements();
			for (ElementModel element : elements) {
				if (element.getElementId().equals(this.getParent().getNodeId())) {
					elements.remove(element);
					((CostElementDTO) searchNode.getDto()).setElements(elements);
					searchNode.expandClicked(event);
					break;
				}
			}
		}
		// if element deleted change parent to be refreshed
		if (dto.isElementDeleted()) {
			setParent(getParent().getParent());
		}
		return "";
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.ObjectNode#getCopyMessage()
	 */
	@Override
	protected String getCopyMessage() {
		return treeBean.getMessage("action.costElement.copy");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#getEditMessage()
	 */
	@Override
	protected String getEditMessage() {
		return treeBean.getMessage("action.costElement.edit");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#getCutMessage()
	 */
	@Override
	protected String getCutMessage() {
		return treeBean.getMessage("action.costElement.cut");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#getDeleteSuccessMessage()
	 */
	@Override
	protected String getDeleteSuccessMessage() {
		return treeBean.getMessage("action.costElement.delete");
	}
}

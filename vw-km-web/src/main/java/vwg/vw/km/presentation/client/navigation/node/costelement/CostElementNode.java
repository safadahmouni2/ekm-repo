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

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import vwg.vw.km.application.implementation.EnumObjectTypeManager;
import vwg.vw.km.application.implementation.EnumStatusManager;
import vwg.vw.km.application.service.dto.CostElementDTO;
import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;
import vwg.vw.km.integration.persistence.model.BrandModel;
import vwg.vw.km.integration.persistence.model.ElementModel;
import vwg.vw.km.integration.persistence.model.ElementVersionModel;
import vwg.vw.km.integration.persistence.model.FolderModel;
import vwg.vw.km.integration.persistence.model.UserModel;
import vwg.vw.km.presentation.client.navigation.NavigationBean;
import vwg.vw.km.presentation.client.navigation.node.BranchNode;
import vwg.vw.km.presentation.client.navigation.node.ContextMenuEnum;
import vwg.vw.km.presentation.client.navigation.node.FolderNode;
import vwg.vw.km.presentation.client.navigation.node.Node;
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
 * @author Sebri Zouhaier changed by $Author: zouhair $
 * @version $Revision: 1.89 $ $Date: 2016/12/22 13:04:53 $
 */
public class CostElementNode extends BranchNode {

	private final Log log = LogManager.get().getLog(CostElementNode.class);

	public CostElementNode(NavigationBean tree, FolderNode parent, String text, Long costElementId,
			boolean schowLastVersion, boolean isSearchNode, BrandModel owner, List<String> users) {
		super(tree, parent, costElementId);
		setMenuDisplayText(text);
		setMenuContentTitle(text);
		setFromBundle(Boolean.FALSE);
		setTemplateName("./costElement/costElement.xhtml");
		setPopupPageName("./costElement/popup.xhtml");
		setLeafIcon(makeIcon("e.gif"));
		setBranchContractedIcon(makeIcon("e.gif"));
		setBranchExpandedIcon(makeIcon("e.gif"));
		setOwnerImg(owner.getOwnImg());
		setOwner(owner);
		setUsers(users);
		setSearchNode(isSearchNode);

		if (!treeBean.isVisitorUser()) {
			if (isSearchNode) {
				setContextMenu(ContextMenuEnum.SearchObjectContextMenu);
			} else {
				if (treeBean.isLoggedUserAdminOnBrand(getOwner())) {
					setContextMenu(ContextMenuEnum.ObjectContextMenu);
				} else {
					setContextMenu(ContextMenuEnum.SearchObjectContextMenu);
				}
			}
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#nodeClicked(javax.faces.event.ActionEvent)
	 */
	@Override
	public void nodeClicked(ActionEvent event) {
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
		// AMR: PTS_Problem-36558: Refresh Problem when deleting an object
		// click node from search result
		if (this.getParent() instanceof SearchFolderNode) {
			showElement(event);
		} else {
			treeBean.setSelectedNodeObject(this);
			if (objectChildsCount > 0) {
				childs.get(0).nodeClicked(event);
				this.setExpanded(Boolean.TRUE);
				return;
			}
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
	 * @see vwg.vw.km.presentation.client.navigation.node.BranchNode#getChilds()
	 */
	@Override
	public List<Node> getChilds() {
		UserModel loggedUser = treeBean.getUserFromSession();
		List<Node> childs = new ArrayList<Node>();
		CostElementDTO costElementDTO = new CostElementDTO();
		costElementDTO.setAscending(Boolean.FALSE);
		costElementDTO.setUserLogged(loggedUser);
		treeBean.getCostElementService().loadDTOForDetail(costElementDTO, getNodeId(), this.getParent().getNodeId(),
				Boolean.TRUE, Boolean.TRUE);
		List<ElementVersionModel> elementVersions = costElementDTO.getVersionHistoryList();
		setDto(costElementDTO);

		String standLabel = treeBean.getMessage("costelement.elementstand");
		String standInProcessingLabel = treeBean.getMessage("costelement.elementstand.inprocessing");
		int indexLastUseVersion = costElementDTO.getIndexOfLastInUseVersion();
		int i = 0;
		ElementModel element = null;
		for (ElementVersionModel model : elementVersions) {
			String textNode = standInProcessingLabel;
			String owner = null;
			if (EnumStatusManager.Status.IN_USE.value().equals(model.getEnumStatus().getEnumStatusId())) {
				textNode = standLabel + " " + model.getNumber();
				if (indexLastUseVersion == i) {
					owner = model.getElement().getOwnerImg();
				}
			}
			CostElementVersionNode n = new CostElementVersionNode(treeBean, this, textNode, model.getElementVersionId(),
					Boolean.TRUE, getSearchNode(), owner, model.getUsersImg());
			childs.add(n);
			objectChildsCount++;
			indexOfLastObject = childs.size() - 1;
			i++;
			element = model.getElement();
		}

		if (element != null) {
			setUsers(element.getUsersImg());
		}
		return childs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.BranchNode#getCreateMessage()
	 */
	@Override
	protected String getCreateMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.BranchNode#getListClickedMessage ()
	 */
	@Override
	protected String getListClickedMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.BranchNode#getNewFolderName()
	 */
	@Override
	public String getNewFolderName() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.BranchNode#getObjectContentId ()
	 */
	@Override
	public Long getObjectContentId() {
		return EnumObjectTypeManager.ELEMENT_ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.BranchNode#getPasteMessage()
	 */
	@Override
	protected String getPasteMessage() {
		return treeBean.getMessage("action.costElement.past");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#getCutMessage()
	 */
	@Override
	protected String getCutMessage() {
		return treeBean.getMessage("action.costElement.cut");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#getDeleteSuccessMessage ()
	 */
	@Override
	protected String getDeleteSuccessMessage() {
		return treeBean.getMessage("action.costElement.delete");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#getEditMessage()
	 */
	@Override
	protected String getEditMessage() {
		return treeBean.getMessage("action.costElement.edit");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#getObjectTypeId()
	 */
	@Override
	public Long getObjectTypeId() {
		return EnumObjectTypeManager.ELEMENT_ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * vwg.vw.km.presentation.client.navigation.node.Node#specificCopyClicked(vwg.vw.km.presentation.client.navigation.
	 * node.Node)
	 */
	@Override
	public Node specificCopyClicked(Node node) {
		CostElementDTO costElementDTO = new CostElementDTO();
		costElementDTO.setUserLogged(treeBean.getUserFromSession());

		treeBean.getCostElementService().loadDTOForCopy(costElementDTO, getNodeId(), this.getParent().getNodeId(),
				Boolean.TRUE, Boolean.TRUE);
		costElementDTO = treeBean.getCostElementService().copyElement(costElementDTO);
		node.setMenuDisplayText(costElementDTO.getElementVersion().getElement().getNumberAndDesignation());
		// set the owner icon in the tree
		node.setOwnerImg(costElementDTO.getElementVersion().getElement().getOwnerImg());
		node.setDto(costElementDTO);
		log.info("Copy Element version: " + ((CostElementDTO) node.getDto()).getElementVersion());
		return node;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#getAddNewObjectAllowed()
	 */
	@Override
	public boolean getAddNewObjectAllowed() {
		UserModel user = treeBean.getUserFromSession();
		if (user.getDefaultBrand() == null || "".equals(user.getDefaultBrand().trim())) {
			treeBean.openErrorPopup(treeBean.getMessage("common.message.defaultbrand.required"));
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	@Override
	public boolean getHasUpdateFolderRight() {
		return true;
	}

	/**
	 * sort a node under a folder by up/down/top/bottom
	 * 
	 * @param event
	 */
	@Override
	public void sortClicked(ActionEvent event) {
		// PTS problem 34579 : check if there is a brand filter applied to
		// components
		if (treeBean.getUserFromSession().isActiveElementFilter()) {
			treeBean.openErrorPopup(treeBean.getMessage("action.brand.filter.active.alert"));
			return;
		}
		String actionToDo = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get("sort_parameter");
		log.info("Sort " + actionToDo + " clicked for " + this.getMenuDisplayText());
		treeBean.getCostElementService().saveNewOrder(getNodeId(), this.getParent().getNodeId(), actionToDo);
		getParent().setExpanded(Boolean.FALSE);
		getParent().nodeClicked(event);
	}

	@Override
	protected String getMarkedCopyMessage(String nodeMarkedCount) {
		return treeBean.getMessage("action.multi.costElement.copy", nodeMarkedCount);
	}

	@Override
	protected String getMarkedCutMessage(String nodeMarkedCount) {
		return treeBean.getMessage("action.multi.costElement.cut", nodeMarkedCount);
	}

	@Override
	protected String getChangeStatusMessage(String changedCount, String toStatus) {
		return "Status von " + changedCount + " Elemente wurde auf \"" + toStatus + "\" gesetzt!";
	}

	@Override
	protected String specificGoToStatus(ActionEvent event, Node node, Long toStatusId) {
		CostElementDTO costElementDTO = new CostElementDTO();
		costElementDTO.setUserLogged(treeBean.getUserFromSession());
		treeBean.getCostElementService().loadDTOForCopy(costElementDTO, node.getNodeId(), node.getParent().getNodeId(),
				Boolean.TRUE, Boolean.TRUE);
		ElementVersionModel version = costElementDTO.getElementVersion();
		log.info("Change status from node of version: " + version);
		if (EnumStatusManager.Status.TO_BE_APPROVED.value().equals(toStatusId)) {
			if (!(costElementDTO.getUserIsOwner() && costElementDTO.getUserHasWriteAccess())) {
				return "Sie haben nicht die Rechte den Status dieses Elementes zu ändern: "
						+ version.getElement().getElementNumber();
			}
		} else {
			if (!(costElementDTO.getUserIsOwner() && !treeBean.isVisitorUser())) {
				return "Sie haben nicht die Rechte den Status dieses Elementes zu ändern:"
						+ version.getElement().getElementNumber();
			}
		}

		if (version.getIsLocked() != null && version.getIsLocked()) {
			return "Element " + version.getElement().getElementNumber() + ":"
					+ treeBean.getMessage("costelement.writeAccessNotAllowed", version.getProcessor().getFullName());
		}

		Long fromStatusId = version.getEnumStatus().getEnumStatusId();
		if (!fromStatusId.equals(version.getEnumStatus().getEnumStatusId())) {
			return "Element " + version.getElement().getElementNumber() + ":" + treeBean
					.getMessage("costelement.changeStatus.alreadyChanged", version.getProcessor().getFullName());
		}
		// TO-DO get possible from status from DB
		if (EnumStatusManager.Status.TO_BE_APPROVED.value().equals(toStatusId)) {
			if (!fromStatusId.equals(EnumStatusManager.Status.CREATED.value())
					&& !fromStatusId.equals(EnumStatusManager.Status.IN_PROGRESS.value())) {
				return "Element " + version.getElement().getElementNumber() + ": Statuswechsel von \""
						+ treeBean.getMessage("status.designationbyId." + fromStatusId) + "\" zum \""
						+ treeBean.getMessage("status.designationbyId." + toStatusId) + "\" nicht erlaubt!";
			}
		} else if (EnumStatusManager.Status.IN_PROGRESS.value().equals(toStatusId)) {
			if (EnumStatusManager.Status.IN_PROGRESS.value().equals(fromStatusId)
					|| EnumStatusManager.Status.CREATED.value().equals(fromStatusId)) {
				return "";
			} else if (!fromStatusId.equals(EnumStatusManager.Status.TO_BE_APPROVED.value())
					&& !fromStatusId.equals(EnumStatusManager.Status.APPROVED.value())
					&& !fromStatusId.equals(EnumStatusManager.Status.IN_USE.value())) {
				return "Element " + version.getElement().getElementNumber() + ": Statuswechsel von \""
						+ treeBean.getMessage("status.designationbyId." + fromStatusId) + "\" zum \""
						+ treeBean.getMessage("status.designationbyId." + toStatusId) + "\" nicht erlaubt!";
			}
		}
		costElementDTO.setToStatusId(toStatusId);
		String fromStatusDesignation = treeBean.getMessage("status.designationbyId." + fromStatusId);
		String toStatusDesignation = treeBean.getMessage("status.designationbyId." + toStatusId);
		log.info("go From Status  :  " + fromStatusDesignation + " To " + toStatusDesignation);
		String goToStatus = treeBean.getCostElementService().saveNewStatus(costElementDTO);
		if (goToStatus.equals("")) {
			node.setDto(treeBean.getCostElementService().loadDTOForDetail(costElementDTO,
					costElementDTO.getElementVersionId(), costElementDTO.getFolderId(), Boolean.FALSE, Boolean.FALSE));
			treeBean.refreshSearchStatusNode(event, EnumObjectTypeManager.ELEMENT_ID, fromStatusId, toStatusId);
			return null;
		} else {
			return "Element " + version.getElement().getElementNumber() + ":"
					+ treeBean.getMessage("componentversion.consecutif.samestatus.exist", toStatusDesignation);
		}
	}

}
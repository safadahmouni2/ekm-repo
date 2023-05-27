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
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import vwg.vw.km.application.implementation.EnumObjectTypeManager;
import vwg.vw.km.application.implementation.EnumStatusManager;
import vwg.vw.km.application.service.dto.CostComponentDTO;
import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;
import vwg.vw.km.integration.persistence.model.BrandModel;
import vwg.vw.km.integration.persistence.model.ComponentModel;
import vwg.vw.km.integration.persistence.model.ComponentVersionModel;
import vwg.vw.km.integration.persistence.model.FolderModel;
import vwg.vw.km.integration.persistence.model.UserModel;
import vwg.vw.km.presentation.client.navigation.NavigationBean;
import vwg.vw.km.presentation.client.navigation.node.BranchNode;
import vwg.vw.km.presentation.client.navigation.node.ContextMenuEnum;
import vwg.vw.km.presentation.client.navigation.node.FolderNode;
import vwg.vw.km.presentation.client.navigation.node.Node;
import vwg.vw.km.presentation.client.navigation.node.SearchFolderNode;
import vwg.vw.km.presentation.client.navigation.node.StaticNodeIdEnum;
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
 * @author Sebri Zouhaier changed by $Author: saidi $
 * @version $Revision: 1.85 $ $Date: 2018/02/13 07:45:41 $
 */
public class CostComponentNode extends BranchNode {

	private final Log log = LogManager.get().getLog(CostComponentNode.class);

	public CostComponentNode(NavigationBean tree, FolderNode parent, String text, Long costComponentId,
			boolean schowLastVersion, boolean isSearchNode, BrandModel owner, List<String> users, String library) {
		super(tree, parent, costComponentId);
		setMenuDisplayText(text);
		setMenuContentTitle(text);
		setFromBundle(Boolean.FALSE);
		setNodeId(costComponentId);
		setBranchContractedIcon(makeIcon("b.gif"));
		setBranchExpandedIcon(makeIcon("b.gif"));
		setLeafIcon(makeIcon("b.gif"));
		setOwnerImg(owner.getOwnImg());
		setOwner(owner);
		setUsers(users);
		setLibrary(library);
		setSearchNode(isSearchNode);
		setTemplateName("./costComponent/costComponent.xhtml");
		setPopupPageName("./costComponent/popup.xhtml");

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

	/*
	 * @see vwg.vw.km.presentation.client.navigation.node.BranchNode#getChilds()
	 */
	@Override
	public List<Node> getChilds() {
		List<Node> childs = new ArrayList<Node>();
		CostComponentDTO costComponentDTO = new CostComponentDTO();
		costComponentDTO.setUserLogged(treeBean.getUserFromSession());

		treeBean.getCostComponentService().loadDTOForDetail(costComponentDTO, getNodeId(), this.getParent().getNodeId(),
				"COMPONENT", Boolean.TRUE);
		List<ComponentVersionModel> componentVersions = costComponentDTO.getVersionHistoryList();
		setDto(costComponentDTO);
		String standLabel = treeBean.getMessage("costcomponent.version");

		int indexLastUseVersion = costComponentDTO.getIndexOfLastInUseVersion();
		if (log.isInfoEnabled()) {
			log.info("Number of componentVersions :" + componentVersions.size());
		}
		String standInProcessingLabel = treeBean.getMessage("costcomponent.stand.inprocessing");
		int i = 0;
		ComponentModel component = null;
		for (ComponentVersionModel model : componentVersions) {
			String owner = null;
			Node n = null;
			if (!EnumStatusManager.Status.IN_USE.value().equals(model.getEnumStatus().getEnumStatusId())) {
				n = new CostComponentInProcessVersionNode(treeBean, this, standInProcessingLabel,
						model.getComponentVersionId(), Boolean.TRUE, getSearchNode(), model.getUsersImg());
			} else {
				if (indexLastUseVersion == i) {
					owner = model.getComponent().getOwnerImg();
				}
				n = new CostComponentVersionNode(treeBean, this, standLabel + " " + model.getNumber(),
						model.getComponentVersionId(), Boolean.TRUE, getSearchNode(), owner, model.getUsersImg());
			}
			childs.add(n);
			objectChildsCount++;
			indexOfLastObject = childs.size() - 1;
			i++;
			component = model.getComponent();
		}

		if (component != null) {
			component.setUsers(treeBean.getCostComponentService().loadComponentVersionUsers(component));
			setUsers(component.getUsersImg());
		}
		return childs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#nodeClicked(javax.faces.event.ActionEvent)
	 */
	@Override
	public void nodeClicked(ActionEvent event) {
		if (log.isInfoEnabled()) {
			log.info(this.getMenuDisplayText() + " node is clicked");
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
		// click node from search result
		if (this.getParent() instanceof SearchFolderNode) {
			showComponent(event);
		} else {
			if (objectChildsCount > 0) {
				childs.get(0).nodeClicked(event);
				this.setExpanded(Boolean.TRUE);
				return;
			}
		}
	}

	private void showComponent(ActionEvent event) {
		CostComponentDTO dto = (CostComponentDTO) getDto();

		Long componentId = dto.getComponentVersion().getComponent().getComponentId();
		Long folderId = dto.getComponentVersion().getComponent().getFolder().getFolderId();
		FolderModel folder = treeBean.getNavigationService().getFolder(folderId);

		if (dto.getComponentVersion().getComponent().getInactive()) {
			FolderNode componentNode = (FolderNode) treeBean.getRootComponentNode();
			FolderNode searchFolder = (FolderNode) componentNode.getNodeById(
					StaticNodeIdEnum.COST_COMPONENT_SEARCH_FOLDER_NODE.value(), EnumObjectTypeManager.FOLDER_ID);
			searchFolder.setExpanded(true);
			searchFolder.expandClicked(event);
			FolderNode inactiveFolder = (FolderNode) searchFolder.getNodeById(-47L, EnumObjectTypeManager.FOLDER_ID);

			CostComponentNode n = (CostComponentNode) goToInactiveNode(inactiveFolder, componentId, folderId,
					EnumObjectTypeManager.COMPONENT_ID, event);
			if (n != null) {
				n.nodeClicked(event);
			}
		} else {
			// component is in a working folder
			if (folder.getCreatorRefId() != null) {
				Long userId = folder.getCreatorRefId();
				FolderNode workingNode = treeBean.getWorkingNode();
				workingNode.setExpanded(true);
				workingNode.expandClicked(event);

				FolderNode userWorkingFolder = (FolderNode) workingNode.getNodeById(userId,
						EnumObjectTypeManager.FOLDER_ID);
				WorkingCostComponentNode n = (WorkingCostComponentNode) treeBean.goToNode(userWorkingFolder,
						componentId, folderId, EnumObjectTypeManager.COMPONENT_ID, event);
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
	}

	private Node goToInactiveNode(FolderNode actualNode, Long nodeId, Long startFolderId, Long objectTypeId,
			ActionEvent event) {
		List<Long> folderIdsHierarchy = treeBean.getNavigationService().getFolderIdsHierarchy(startFolderId);
		for (int i = 1; i < folderIdsHierarchy.size(); i++) {
			log.info(folderIdsHierarchy.get(i) + " actualNode: " + actualNode);
			actualNode.setExpanded(true);
			actualNode.expandClicked(event);
			actualNode = (FolderNode) actualNode.getNodeById(-folderIdsHierarchy.get(i),
					EnumObjectTypeManager.FOLDER_ID);
		}
		actualNode.setExpanded(true);
		actualNode.expandClicked(event);
		return actualNode.getNodeById(nodeId, objectTypeId);
	}

	/**
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
	 * @see
	 * vwg.vw.km.presentation.client.navigation.node.Node#specificCopyClicked(vwg.vw.km.presentation.client.navigation
	 * .node.Node)
	 */
	@Override
	public Node specificCopyClicked(Node node) {
		CostComponentDTO costComponentDTO = new CostComponentDTO();
		costComponentDTO.setUserLogged(treeBean.getUserFromSession());
		costComponentDTO = treeBean.getCostComponentService().loadDTOForCopy(costComponentDTO, getNodeId(),
				this.getParent().getNodeId(), "COMPONENT", Boolean.TRUE);
		costComponentDTO = treeBean.getCostComponentService().copyComponent(costComponentDTO);
		node.setMenuDisplayText(costComponentDTO.getComponentVersion().getComponent().getNumberAndDesignation());
		// set the owner icon in the tree
		node.setOwnerImg(costComponentDTO.getComponentVersion().getComponent().getOwnerImg());
		node.setLibrary(costComponentDTO.getComponentVersion().getComponent().getLibrayIcon());
		node.setDto(costComponentDTO);
		log.info("Copy component: " + costComponentDTO.getComponentVersion().getComponent());
		return node;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#getDeleteMessage()
	 */
	@Override
	public String getDeleteMessage() {
		return treeBean.getMessage("costcomponent.deleteConfirm");
	}

	/**
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
		return "";
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.ObjectNode#getCopyMessage()
	 */
	@Override
	protected String getCopyMessage() {
		return treeBean.getMessage("action.costComponent.copy");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#getEditMessage()
	 */
	@Override
	protected String getEditMessage() {
		return treeBean.getMessage("action.costComponent.edit");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#getCutMessage()
	 */
	@Override
	protected String getCutMessage() {
		return treeBean.getMessage("action.costComponent.cut");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#getDeleteSuccessMessage()
	 */
	@Override
	protected String getDeleteSuccessMessage() {
		return treeBean.getMessage("action.costComponent.delete");
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
	public String getNewFolderName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getObjectContentId() {
		return EnumObjectTypeManager.COMPONENT_ID;
	}

	@Override
	protected String getPasteMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.BranchNode#getHasUpdateFolderRight()
	 */
	@Override
	public boolean getHasUpdateFolderRight() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#getAddNewObjectAllowed()
	 */
	@Override
	public boolean getAddNewObjectAllowed() {
		UserModel user = treeBean.getUserFromSession();
		if (user.getDefaultBrand() == null) {
			treeBean.openErrorPopup(treeBean.getMessage("common.message.defaultbrand.required"));
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	/**
	 * 12/11/20113:ABA: PTS requirement 22190:Individuelle Platzierung von Objekten sort a node under a folder by
	 * up/down/top/bottom
	 * 
	 * @param event
	 */
	@Override
	public void sortClicked(ActionEvent event) {
		// PTS problem 34579 : check if there is a brand filter applied to
		// components
		if (treeBean.getUserFromSession().isActiveComponentFilter()) {
			treeBean.openErrorPopup(treeBean.getMessage("action.brand.filter.active.alert"));
			return;
		}

		String actionToDo = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get("sort_parameter");
		log.info("Sort " + actionToDo + " clicked for " + this.getMenuDisplayText());
		treeBean.getCostComponentService().saveNewOrder(getNodeId(), this.getParent().getNodeId(), actionToDo);
		getParent().setExpanded(Boolean.FALSE);
		getParent().nodeClicked(event);
	}

	@Override
	protected String getMarkedCopyMessage(String nodeMarkedCount) {
		return treeBean.getMessage("action.multi.costComponent.copy", nodeMarkedCount);
	}

	@Override
	protected String getMarkedCutMessage(String nodeMarkedCount) {
		return treeBean.getMessage("action.multi.costComponent.cut", nodeMarkedCount);
	}

	@Override
	protected String getChangeStatusMessage(String changedCount, String toStatus) {
		return "Status von " + changedCount + " Bausteine wurde auf \"" + toStatus + "\" gesetzt!";
	}

	@Override
	protected String specificGoToStatus(ActionEvent event, Node node, Long toStatusId) {

		CostComponentDTO costComponentDTO = new CostComponentDTO();
		costComponentDTO.setUserLogged(treeBean.getUserFromSession());
		treeBean.getCostComponentService().loadDTOForDetail(costComponentDTO, node.getNodeId(),
				node.getParent().getNodeId(), "COMPONENT", Boolean.TRUE);
		ComponentVersionModel version = costComponentDTO.getComponentVersion();
		log.info("Change status from node of version: " + version);
		if (EnumStatusManager.Status.TO_BE_APPROVED.value().equals(toStatusId)) {
			if (!(costComponentDTO.getUserIsOwner() && costComponentDTO.getUserHasWriteAccess())) {
				return "Sie haben nicht die Rechte den Status dieses Bausteins zu ändern: "
						+ version.getComponent().getComponentNumber();
			}
		} else {
			if (!(costComponentDTO.getUserIsOwner() && !treeBean.isVisitorUser())) {
				return "Sie haben nicht die Rechte den Status dieses Bausteins zu ändern: "
						+ version.getComponent().getComponentNumber();
			}
		}

		if (version.getIsLocked() != null && version.getIsLocked()) {
			return "Baustein " + version.getComponent().getComponentNumber() + ": "
					+ treeBean.getMessage("costcomponent.writeAccessNotAllowed", version.getProcessor().getFullName());
		}

		Long fromStatusId = version.getEnumStatus().getEnumStatusId();
		if (!fromStatusId.equals(version.getEnumStatus().getEnumStatusId())) {
			return "Baustein " + version.getComponent().getComponentNumber() + ": " + treeBean
					.getMessage("costcomponent.changeStatus.alreadyChanged", version.getProcessor().getFullName());
		}
		// TO-DO get possible from status from DB
		if (EnumStatusManager.Status.TO_BE_APPROVED.value().equals(toStatusId)) {
			if (!fromStatusId.equals(EnumStatusManager.Status.CREATED.value())
					&& !fromStatusId.equals(EnumStatusManager.Status.IN_PROGRESS.value())) {
				return "Baustein " + version.getComponent().getComponentNumber() + ": Statuswechsel von \""
						+ treeBean.getMessage("status.designationbyId." + fromStatusId) + "\" zu \""
						+ treeBean.getMessage("status.designationbyId." + toStatusId) + "\" nicht erlaubt!";
			}
		} else if (EnumStatusManager.Status.IN_PROGRESS.value().equals(toStatusId)) {
			if (EnumStatusManager.Status.IN_PROGRESS.value().equals(fromStatusId)
					|| EnumStatusManager.Status.CREATED.value().equals(fromStatusId)) {
				return "";
			} else if (!fromStatusId.equals(EnumStatusManager.Status.TO_BE_APPROVED.value())
					&& !fromStatusId.equals(EnumStatusManager.Status.APPROVED.value())
					&& !fromStatusId.equals(EnumStatusManager.Status.IN_USE.value())) {
				return "Baustein " + version.getComponent().getComponentNumber() + ": Statuswechsel von \""
						+ treeBean.getMessage("status.designationbyId." + fromStatusId) + "\" zu \""
						+ treeBean.getMessage("status.designationbyId." + toStatusId) + "\" nicht erlaubt!";
			}
		}
		costComponentDTO.setToStatusId(toStatusId);
		String fromStatusDesignation = treeBean.getMessage("status.designationbyId." + fromStatusId);
		String toStatusDesignation = treeBean.getMessage("status.designationbyId." + toStatusId);
		log.info("go From Status  :  " + fromStatusDesignation + " To " + toStatusDesignation);
		String goToStatus = treeBean.getCostComponentService().saveNewStatus(costComponentDTO);
		if (goToStatus.equals("")) {
			node.setDto(treeBean.getCostComponentService().loadDTOForDetail(costComponentDTO,
					costComponentDTO.getComponentVersionId(), costComponentDTO.getFolderId(), "VERSION",
					Boolean.FALSE));
			treeBean.refreshSearchStatusNode(event, EnumObjectTypeManager.COMPONENT_ID, fromStatusId, toStatusId);
			return null;
		} else {
			return "Baustein " + version.getComponent().getComponentNumber() + ": "
					+ treeBean.getMessage("componentversion.consecutif.samestatus.exist", toStatusDesignation);
		}
	}
}

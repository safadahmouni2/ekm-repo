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
package vwg.vw.km.presentation.client.navigation.node.working;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;

import vwg.vw.km.application.implementation.EnumObjectTypeManager;
import vwg.vw.km.application.implementation.EnumStatusManager;
import vwg.vw.km.application.implementation.RoleManager;
import vwg.vw.km.application.service.dto.CostComponentDTO;
import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;
import vwg.vw.km.integration.persistence.model.BrandModel;
import vwg.vw.km.integration.persistence.model.ComponentVersionModel;
import vwg.vw.km.presentation.client.navigation.NavigationBean;
import vwg.vw.km.presentation.client.navigation.node.BranchNode;
import vwg.vw.km.presentation.client.navigation.node.ContextMenuEnum;
import vwg.vw.km.presentation.client.navigation.node.FolderNode;
import vwg.vw.km.presentation.client.navigation.node.Node;
import vwg.vw.km.presentation.client.navigation.node.costcomponent.CostComponentInProcessVersionNode;
import vwg.vw.km.presentation.client.navigation.node.costcomponent.CostComponentVersionNode;

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
 * @version $Revision: 1.21 $ $Date: 2013/12/23 13:07:59 $
 */
public class WorkingCostComponentNode extends BranchNode {

	private final Log log = LogManager.get().getLog(WorkingCostComponentNode.class);

	public WorkingCostComponentNode(NavigationBean tree, FolderNode parent, String text, Long costComponentId,
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
		// context menu shown only for the creator and the Administrator
		if (((WorkingUserComponentFolderNode) getParent()).getWorkingUserId()
				.equals(treeBean.getUserFromSession().getUserId())) {
			setContextMenu(ContextMenuEnum.ObjectContextMenu);
		} else {
			if (treeBean.getUserFromSession().haveRole(RoleManager.Role.ADMINISTRATOR.value())
					&& treeBean.getUserFromSession().getUserBrands().contains(owner)) {
				setContextMenu(ContextMenuEnum.AdminWorkObjectContextMenu);
			} // 19.11.2013: ZS: PTS_Requirement-22189: Integration of the new role BETRACHTER
			else if (treeBean.isVisitorUser()) {
				setContextMenu(null);
			} else {
				setContextMenu(ContextMenuEnum.SearchObjectContextMenu);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
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
		log.info("componentVersions :" + componentVersions.size());
		String standInProcessingLabel = treeBean.getMessage("costcomponent.stand.inprocessing");
		int i = 0;
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
		if (objectChildsCount > 0) {
			childs.get(0).nodeClicked(event);
			this.setExpanded(Boolean.TRUE);
			return;
		}
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
		treeBean.getCostComponentService().loadDTOForCopy(costComponentDTO, getNodeId(), this.getParent().getNodeId(),
				"COMPONENT", Boolean.TRUE);
		costComponentDTO = treeBean.getCostComponentService().copyComponent(costComponentDTO);
		node.setMenuDisplayText(costComponentDTO.getComponentVersion().getComponent().getNumberAndDesignation());
		// set the owner icon in the tree
		node.setOwnerImg(costComponentDTO.getComponentVersion().getComponent().getOwnerImg());
		node.setLibrary(costComponentDTO.getComponentVersion().getComponent().getLibrayIcon());
		node.setDto(costComponentDTO);
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

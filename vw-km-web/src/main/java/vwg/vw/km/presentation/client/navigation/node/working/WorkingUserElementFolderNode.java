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

import vwg.vw.km.application.implementation.EnumObjectTypeManager;
import vwg.vw.km.application.implementation.EnumStatusManager;
import vwg.vw.km.application.service.dto.CostElementDTO;
import vwg.vw.km.application.service.dto.base.BaseDTO;
import vwg.vw.km.integration.persistence.model.BrandModel;
import vwg.vw.km.integration.persistence.model.ElementModel;
import vwg.vw.km.integration.persistence.model.FolderModel;
import vwg.vw.km.integration.persistence.model.UserModel;
import vwg.vw.km.presentation.client.navigation.NavigationBean;
import vwg.vw.km.presentation.client.navigation.node.ContextMenuEnum;
import vwg.vw.km.presentation.client.navigation.node.FolderNode;
import vwg.vw.km.presentation.client.navigation.node.Node;
import vwg.vw.km.presentation.client.navigation.node.costelement.CostElementNode;

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
 * @author Ziad Saïdi changed by $Author: zouhair $
 * @version $Revision: 1.20 $ $Date: 2016/12/22 13:04:53 $
 */
public class WorkingUserElementFolderNode extends FolderNode {

	private Long workingUserId;

	public WorkingUserElementFolderNode(NavigationBean tree, FolderNode parent, Long folderId) {
		super(tree, parent, folderId);
		setTemplateName("./costElement/costElementList.xhtml");
		setWorkingUserId(getParent().getNodeId());
		// only the logged user can add new element in his own folder
		if (getWorkingUserId().equals(treeBean.getUserFromSession().getUserId())) {
			setContextMenu(ContextMenuEnum.WorkObjectContextMenu);
		}
		setAddObjectIcon("add-element");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.FolderNode#getChilds()
	 */
	@Override
	public List<Node> getChilds() {
		List<Node> childs = new ArrayList<Node>();
		UserModel loggedUser = treeBean.getUserFromSession();

		List<FolderModel> folders = treeBean.getNavigationService().loadWorkingFolders(getNodeId(),
				getObjectContentId(), getWorkingUserId());
		for (FolderModel model : folders) {
			WorkingElementFolderNode n = new WorkingElementFolderNode(treeBean, this, model.getDesignation(),
					model.getFolderId(), getWorkingUserId());
			childs.add(n);
		}

		CostElementDTO costElementDTO = new CostElementDTO();
		costElementDTO.setFolderId(getNodeId());

		List<Long> searchStatus = new ArrayList<Long>();
		searchStatus.add(EnumStatusManager.Status.CREATED.value());
		searchStatus.add(EnumStatusManager.Status.TO_BE_APPROVED.value());
		searchStatus.add(EnumStatusManager.Status.IN_PROGRESS.value());

		costElementDTO.setSearchStatus(searchStatus);
		costElementDTO.setUserLogged(loggedUser);

		if (getWorkingUserId().equals(loggedUser.getUserId())) {
			costElementDTO.setNewMode(Boolean.TRUE);
		}

		setDto(treeBean.getCostElementService().loadDTOForWorkingFolder(costElementDTO, getWorkingUserId(),
				getNodeId()));
		List<ElementModel> elements = ((CostElementDTO) getDto()).getElements();
		for (ElementModel model : elements) {
			WorkingCostElementNode n = new WorkingCostElementNode(treeBean, this, model.getNumberAndDesignation(),
					model.getElementId(), Boolean.FALSE, Boolean.FALSE, model.getOwner(), model.getUsersImg());
			childs.add(n);
			objectChildsCount++;
			indexOfLastObject = childs.size() - 1;
		}
		return childs;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.FolderNode#getObjectContentId()
	 */
	@Override
	public Long getObjectContentId() {
		return EnumObjectTypeManager.ELEMENT_ID;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.FolderNode#getListClickedMessage()
	 */
	@Override
	protected String getListClickedMessage() {
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.FolderNode#getPasteMessage()
	 */
	@Override
	protected String getPasteMessage() {
		return treeBean.getMessage("action.costElement.paste");
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
		return null;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.FolderNode#getNewObjectNodeInstance()
	 */
	@Override
	public Node getNewObjectNodeInstance() {
		String defaultBrand = treeBean.getUserFromSession().getDefaultBrand();
		BrandModel owner = treeBean.getCostElementService().getDefaultBrand(defaultBrand);
		return new CostElementNode(treeBean, this, treeBean.getMessage("costelement.newElement.tree.msge"), null,
				Boolean.FALSE, Boolean.FALSE, owner, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.BranchNode#getHasUpdateFolderRight()
	 */
	@Override
	public boolean getHasUpdateFolderRight() {
		UserModel loggedUser = treeBean.getUserFromSession();
		if (getWorkingUserId().equals(loggedUser.getUserId())) {
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.FolderNode#getNewFolderName()
	 */
	@Override
	public String getNewFolderName() {
		return treeBean.getMessage("costelement.newElementFolder.tree.msge");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * vwg.vw.km.presentation.client.navigation.node.FolderNode#moveObjectNodeToFolder(vwg.vw.km.presentation.client.
	 * navigation.node.Node, vwg.vw.km.presentation.client.navigation.node.FolderNode)
	 */
	@Override
	public Boolean moveObjectNodeToFolder(Node node, FolderNode toNode, boolean isCopy) {
		if (isCopy) {
			FolderModel distFolder = treeBean.getNavigationService().getFolder(toNode.getNodeId());
			((CostElementDTO) node.getDto()).getElementVersion().getElement().setFolder(distFolder);
			return true;
		}
		return treeBean.getCostElementService().saveElementToFolder(node.getNodeId(), toNode.getNodeId());
	}

	protected void setWorkingUserId(Long workingUserId) {
		this.workingUserId = workingUserId;
	}

	protected Long getWorkingUserId() {
		return workingUserId;
	}

	@Override
	public String specificSaveNode(BaseDTO dto) {
		treeBean.getCostElementService().getExistElementWithSameNumberOrDesignation((CostElementDTO) dto);
		if (((CostElementDTO) dto).getExistElementWithSameNumberOrDesignation()) {
			return treeBean.getMessage("costElement.multi.exist.sameDesignationOrNumber.message");
		}
		treeBean.getCostElementService().save((CostElementDTO) dto);
		return null;
	}

	@Override
	protected String getMarkedPasteMessage(String nodeMarkedCount) {
		return treeBean.getMessage("action.multi.costElement.paste", nodeMarkedCount);
	}
}

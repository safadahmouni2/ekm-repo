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

import vwg.vw.km.application.implementation.EnumObjectTypeManager;
import vwg.vw.km.application.service.dto.CostElementDTO;
import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;
import vwg.vw.km.integration.persistence.model.BrandModel;
import vwg.vw.km.integration.persistence.model.ElementModel;
import vwg.vw.km.integration.persistence.model.ElementVersionUsersModel;
import vwg.vw.km.integration.persistence.model.FolderModel;
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
 * @author Ziad Saïdi changed by $Author: saidi $
 * @version $Revision: 1.7 $ $Date: 2013/12/19 15:25:49 $
 */
public class CostElementInactiveNode extends StaticFolderNode {

	private final Log log = LogManager.get().getLog(CostElementInactiveNode.class);

	public CostElementInactiveNode(NavigationBean tree, FolderNode parent, Long folderId) {
		super(tree, parent, folderId);
		setTemplateName("./costElement/costElementList.xhtml");
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
		List<FolderModel> folders = treeBean.getNavigationService().loadFolders(6L, getObjectContentId());
		for (FolderModel model : folders) {
			CostElementInactiveFolderNode n = new CostElementInactiveFolderNode(treeBean, this, model.getDesignation(),
					model.getFolderId());
			if (!isEmptyFolder(model.getFolderId(), getObjectContentId(), loggedUser)) {
				childs.add(n);
			}
		}
		CostElementDTO costElementDTO = new CostElementDTO();
		costElementDTO.setFolderId(getNodeId());
		costElementDTO.setUserLogged(treeBean.getUserFromSession());
		setDto(treeBean.getCostElementService().loadDTOForInactiveList(6L, costElementDTO));
		List<ElementModel> elements = ((CostElementDTO) getDto()).getElements();
		List<ElementModel> visibleElements = new ArrayList<ElementModel>();
		for (ElementModel model : elements) {
			CostElementNode node = new CostElementNode(treeBean, this, model.getNumberAndDesignation(),
					model.getElementId(), Boolean.FALSE, Boolean.TRUE, model.getOwner(), model.getUsersImg());
			boolean isElementVisibleByFilter = isElementVisibleByFilter(model, loggedUser);
			node.setVisible(isElementVisibleByFilter);
			if (isElementVisibleByFilter) {
				visibleElements.add(model);
			}
			childs.add(node);
			objectChildsCount++;
			indexOfLastObject = childs.size() - 1;
		}
		((CostElementDTO) getDto()).setElements(visibleElements);
		log.info("Number of Elements found: " + childs.size() + " for folder " + this.getMenuDisplayText());
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
		return null;
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

	/**
	 * 
	 * @param folderId
	 * @param objectContentId
	 * @return
	 */
	public boolean isEmptyFolder(Long folderId, Long objectContentId, UserModel loggedUser) {
		if (hasInactiveElements(folderId, loggedUser)) {
			return false;
		}
		List<FolderModel> subFolders = treeBean.getNavigationService().loadFolders(folderId, objectContentId);

		if (subFolders.size() > 0) {
			for (FolderModel subFolder : subFolders) {
				if (!isEmptyFolder(subFolder.getFolderId(), objectContentId, loggedUser)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * check if the folder has inactive components
	 * 
	 * @param folderId
	 * @param user
	 * @return
	 */
	public boolean hasInactiveElements(Long folderId, UserModel user) {
		List<BrandModel> ownerFilters = user.getElementOwnerFilter();
		List<BrandModel> usersFilters = user.getElementUserFilter();
		List<Long> inactiveElementIds = treeBean.getCostElementService().getInactiveElementIds(folderId);

		if (inactiveElementIds != null && !inactiveElementIds.isEmpty()) {

			// check If the user does not specify any filter
			if (ownerFilters.isEmpty() && usersFilters.isEmpty()) {
				return Boolean.TRUE;
			}
			log.info(" #Folder " + folderId + " has " + inactiveElementIds.size() + " Elements to be filtred by brand");
			// Check If folder's elements are all setted not visible after being filtered by brand
			if (!ownerFilters.isEmpty()) {
				if (treeBean.getCostElementService().getHasVisibleElementsByOwnerFilter(ownerFilters, folderId,
						inactiveElementIds)) {
					return Boolean.TRUE;
				}
			}
			if (!usersFilters.isEmpty()) {
				if (treeBean.getCostElementService().getHasVisibleElementsByUsersFilter(usersFilters, folderId,
						inactiveElementIds)) {
					return Boolean.TRUE;
				}
			}

		}
		return Boolean.FALSE;
	}

	protected boolean isElementVisibleByFilter(ElementModel model, UserModel user) {
		if (user.isActiveElementFilter()) {
			if (!user.getElementOwnerFilter().isEmpty() && user.getElementOwnerFilter().contains(model.getOwner())) {
				return Boolean.TRUE;
			}
			if (!user.getElementUserFilter().isEmpty()) {
				List<ElementVersionUsersModel> users = model.getUsers();
				if (users != null) {
					for (ElementVersionUsersModel standUser : users) {
						if (user.getElementUserFilter().contains(standUser.getBrand())) {
							return Boolean.TRUE;
						}
					}
				}
			}
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
}

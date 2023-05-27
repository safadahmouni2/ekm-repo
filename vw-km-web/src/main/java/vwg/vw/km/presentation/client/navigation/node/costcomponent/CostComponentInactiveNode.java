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

import vwg.vw.km.application.implementation.EnumObjectTypeManager;
import vwg.vw.km.application.service.dto.CostComponentDTO;
import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;
import vwg.vw.km.integration.persistence.model.BrandModel;
import vwg.vw.km.integration.persistence.model.ComponentModel;
import vwg.vw.km.integration.persistence.model.ComponentStandUsersModel;
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
 * @author Sebri Zouhaier changed by $Author: saidi $
 * @version $Revision: 1.8 $ $Date: 2014/07/01 07:17:49 $
 */
public class CostComponentInactiveNode extends StaticFolderNode {

	private final Log log = LogManager.get().getLog(CostComponentInactiveNode.class);

	public CostComponentInactiveNode(NavigationBean tree, FolderNode parent, Long folderId) {
		super(tree, parent, folderId);
		setTemplateName("./costComponent/costComponentList.xhtml");
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
		List<FolderModel> folders = treeBean.getNavigationService().loadFolders(7L, getObjectContentId());
		for (FolderModel model : folders) {
			CostComponentInactiveFolderNode n = new CostComponentInactiveFolderNode(treeBean, this,
					model.getDesignation(), model.getFolderId());
			if (!isEmptyFolder(model.getFolderId(), getObjectContentId(), loggedUser)) {
				childs.add(n);
			}
		}
		CostComponentDTO costComponentDTO = new CostComponentDTO();
		costComponentDTO.setFolderId(getNodeId());
		costComponentDTO.setUserLogged(loggedUser);
		setDto(treeBean.getCostComponentService().loadDTOForInactiveList(7L, costComponentDTO));
		List<ComponentModel> components = ((CostComponentDTO) getDto()).getComponents();
		List<ComponentModel> visibleComponents = new ArrayList<ComponentModel>();
		for (ComponentModel model : components) {
			CostComponentNode node = new CostComponentNode(treeBean, this, model.getNumberAndDesignation(),
					model.getComponentId(), Boolean.FALSE, Boolean.TRUE, model.getOwner(), model.getUsersImg(),
					model.getLibrayIcon());
			boolean isComponentVisibleByFilter = isComponentVisibleByFilter(model, loggedUser);
			node.setVisible(isComponentVisibleByFilter);
			if (isComponentVisibleByFilter) {
				visibleComponents.add(model);
			}
			childs.add(node);
			objectChildsCount++;
			indexOfLastObject = childs.size() - 1;
		}
		((CostComponentDTO) getDto()).setComponents(visibleComponents);
		log.info("Number of components found: " + childs.size() + " for folder " + this.getMenuDisplayText());
		return childs;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.FolderNode#getObjectContentId()
	 */
	@Override
	public Long getObjectContentId() {
		return EnumObjectTypeManager.COMPONENT_ID;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.FolderNode#getListClickedMessage()
	 */
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
	public boolean isEmptyFolder(Long folderId, Long objectContentId, UserModel user) {
		if (hasInactiveComponents(folderId, user)) {
			return false;
		}
		List<FolderModel> subFolders = treeBean.getNavigationService().loadFolders(folderId, objectContentId);

		if (subFolders.size() > 0) {
			for (FolderModel subFolder : subFolders) {
				if (!isEmptyFolder(subFolder.getFolderId(), objectContentId, user)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Check if a folder has no sub folders which contain BS filtered by brand components
	 * 
	 * @return true/false
	 */
	public boolean hasInactiveComponents(Long folderId, UserModel user) {
		List<BrandModel> ownerFilters = user.getComponentOwnerFilter();
		List<BrandModel> usersFilters = user.getComponentUserFilter();
		List<Long> inactiveComponentIds = treeBean.getCostComponentService().getInactiveComponentIds(folderId);

		if (inactiveComponentIds != null && !inactiveComponentIds.isEmpty()) {
			// Check If the user does not specify any filter
			if (ownerFilters.isEmpty() && usersFilters.isEmpty()) {
				return Boolean.TRUE;
			}
			log.info(" #Folder " + folderId + " has " + inactiveComponentIds.size()
					+ " Components to be filtred by brand");
			// Check If folder's components are all setted not visible after being filtered by brand
			if (!ownerFilters.isEmpty()) {
				if (treeBean.getCostComponentService().getHasVisibleComponentsByOwnerFilter(ownerFilters, folderId,
						inactiveComponentIds)) {
					return Boolean.TRUE;
				}
			}

			if (!usersFilters.isEmpty()) {
				if (treeBean.getCostComponentService().getHasVisibleComponentsByUsersFilter(usersFilters, folderId,
						inactiveComponentIds)) {
					return Boolean.TRUE;
				}
			}
		}
		return Boolean.FALSE;
	}

	/**
	 * 
	 * @param model
	 * @param loggedUser
	 * @return
	 */
	protected boolean isComponentVisibleByFilter(ComponentModel model, UserModel loggedUser) {
		if (loggedUser.isActiveComponentFilter()) {
			if (!loggedUser.getComponentOwnerFilter().isEmpty()
					&& loggedUser.getComponentOwnerFilter().contains(model.getOwner())) {
				return Boolean.TRUE;
			}
			if (!loggedUser.getComponentUserFilter().isEmpty()) {
				List<ComponentStandUsersModel> users = model.getUsers();
				if (users != null) {
					for (ComponentStandUsersModel standUser : users) {
						if (loggedUser.getComponentUserFilter().contains(standUser.getBrand())) {
							return Boolean.TRUE;
						}
					}
				}
			}
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	@Override
	public CostComponentNode getNewObjectNodeInstance() {
		UserModel user = treeBean.getUserFromSession();
		BrandModel owner = treeBean.getCostComponentService().getDefaultBrand(user.getDefaultBrand());
		String libIcon = treeBean.getCostComponentService().getDefaultLibraryIcon(user.getDefaultLibrary());
		return new CostComponentNode(treeBean, this, treeBean.getMessage("costcomponent.newComponent.tree.msge"), null,
				Boolean.FALSE, Boolean.FALSE, owner, null, libIcon);
	}
}

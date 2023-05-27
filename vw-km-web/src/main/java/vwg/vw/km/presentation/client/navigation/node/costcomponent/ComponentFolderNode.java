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

import javax.faces.event.ActionEvent;

import vwg.vw.km.application.implementation.EnumObjectTypeManager;
import vwg.vw.km.application.service.dto.CostComponentDTO;
import vwg.vw.km.application.service.dto.base.BaseDTO;
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
 * @author Sebri Zouhaier changed by $Author: mrad $
 * @version $Revision: 1.56 $ $Date: 2016/11/01 10:04:42 $
 */
/**
 * <p>
 * Title: EKM
 * <p>
 * Description : Class description goes here
 * </p>
 * <p>
 * Copyright: VW (c) 2011
 * </p>
 * 
 * @author mrad changed by $Author: mrad $
 * @version $Revision: 1.56 $ $Date: 2016/11/01 10:04:42 $
 */
public class ComponentFolderNode extends FolderNode {

	private final Log log = LogManager.get().getLog(ComponentFolderNode.class);

	public ComponentFolderNode(NavigationBean tree, FolderNode parent, Long folderId) {
		super(tree, parent, folderId);
		setAddObjectIcon("add-component");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.FolderNode#getNewFolderName()
	 */
	@Override
	public String getNewFolderName() {
		return treeBean.getMessage("costcomponent.newComponentFolder.tree.msge");
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
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.FolderNode#getNewObjectNodeInstance()
	 */
	@Override
	public CostComponentNode getNewObjectNodeInstance() {
		UserModel user = treeBean.getUserFromSession();
		BrandModel owner = treeBean.getCostComponentService().getDefaultBrand(user.getDefaultBrand());
		String libIcon = treeBean.getCostComponentService().getDefaultLibraryIcon(user.getDefaultLibrary());
		return new CostComponentNode(treeBean, this, treeBean.getMessage("costcomponent.newComponent.tree.msge"), null,
				Boolean.FALSE, Boolean.FALSE, owner, null, libIcon);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.FolderNode#moveObjectNodeToFolder(vwg.vw.km.presentation.client.navigation.node.Node,
	 *      vwg.vw.km.presentation.client.navigation.node.FolderNode)
	 */
	@Override
	public Boolean moveObjectNodeToFolder(Node node, FolderNode toNode, boolean isCopy) {
		if (isCopy) {
			FolderModel distFolder = treeBean.getNavigationService().getFolder(toNode.getNodeId());
			((CostComponentDTO) node.getDto()).getComponentVersion().getComponent().setFolder(distFolder);
			return true;
		}
		return treeBean.getCostComponentService().saveComponentToFolder(node.getNodeId(), toNode.getNodeId());
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.FolderNode#getListClickedMessage()
	 */
	@Override
	protected String getListClickedMessage() {
		return treeBean.getMessage("action.component.list");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.FolderNode#getPasteMessage()
	 */
	@Override
	protected String getPasteMessage() {
		return treeBean.getMessage("action.costComponent.paste");
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
		return treeBean.getMessage("action.folder.deleteFolder");
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
		return treeBean.getMessage("action.costComponent.create");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.FolderNode#getExistingObjectMessage()
	 */
	@Override
	public String getExistingObjectMessage() {
		return treeBean.getMessage("costElement.exist.sameDesignationOrNumber.message");
	}

	protected boolean isInactiveComponentVisible(ComponentModel model) {
		UserModel user = treeBean.getUserFromSession();
		if (user.getDefaultBrand() != null) {
			BrandModel userBrand = treeBean.getCostComponentService().getDefaultBrand(user.getDefaultBrand());
			if (userBrand.equals(model.getOwner())) {
				return Boolean.FALSE;
			}
			List<ComponentStandUsersModel> users = model.getUsers();
			if (users != null) {
				for (ComponentStandUsersModel standUser : users) {
					if (standUser.getBrand().equals(userBrand)) {
						return Boolean.TRUE;
					}
				}
			}
		}
		return Boolean.FALSE;
	}

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

	/**
	 * 08.11.2013: ABA: PTS_Problem-26631: Check if a folder has no sub folders which contain filtered by brand
	 * components
	 * 
	 * @return true/false
	 */

	public boolean hasVisibleComponents(Long folderId, UserModel user) {
		List<BrandModel> ownerFilters = user.getComponentOwnerFilter();
		List<BrandModel> usersFilters = user.getComponentUserFilter();
		String userBrand = user.getDefaultBrand();
		List<Long> componentListToBeFiltred = treeBean.getCostComponentService().getComponentsListToBeFiltred(folderId,
				userBrand);

		if (treeBean.getCostComponentService().getHasComponents(folderId)) {

			if (componentListToBeFiltred.isEmpty()) {
				return Boolean.FALSE;
			}
			// Check If the user does not specify any filter
			if (ownerFilters.isEmpty() && usersFilters.isEmpty()) {
				return Boolean.TRUE;
			}

			log.info(" #Folder " + folderId + " has " + componentListToBeFiltred.size()
					+ " Components to be filtred by brand");

			// Check If folder's components are all setted not visible after being filtered by brand
			if (!ownerFilters.isEmpty()) {
				if (treeBean.getCostComponentService().getHasVisibleComponentsByOwnerFilter(ownerFilters, folderId,
						componentListToBeFiltred)) {
					return Boolean.TRUE;
				}
			}

			if (!usersFilters.isEmpty()) {
				if (treeBean.getCostComponentService().getHasVisibleComponentsByUsersFilter(usersFilters, folderId,
						componentListToBeFiltred)) {
					return Boolean.TRUE;
				}
			}

		}
		return Boolean.FALSE;
	}

	public boolean isEmptyFolder(Long folderId, Long objectContentId, UserModel user) {
		if (hasVisibleComponents(folderId, user)) {
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

	public boolean isEmptyFolder(UserModel user) {
		return isEmptyFolder(getNodeId(), getObjectContentId(), user);

	}

	@Override
	public String specificSaveNode(BaseDTO dto) {
		treeBean.getCostComponentService().getExistComponentWithSameNumberOrDesignation((CostComponentDTO) dto);
		if (((CostComponentDTO) dto).getExistComponentWithSameNumberOrDesignation()) {
			return treeBean.getMessage("costComponent.multi.exist.sameDesignationOrNumber.message");
		}
		treeBean.getCostComponentService().save((CostComponentDTO) dto);
		return null;
	}

	@Override
	protected String getMarkedPasteMessage(String nodeMarkedCount) {
		return treeBean.getMessage("action.multi.costComponent.paste", nodeMarkedCount);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.FolderNode#specificNodeClicked(javax.faces.event.ActionEvent)
	 * ZS: PTS_Change-26769 :If only one element or component is found in the folder, the List mask is displayed.
	 */
	@Override
	protected void specificNodeClicked(ActionEvent event) {
		// nothing to do just display the list
	}
}

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

import javax.faces.event.ActionEvent;

import vwg.vw.km.application.implementation.EnumObjectTypeManager;
import vwg.vw.km.application.service.dto.CostElementDTO;
import vwg.vw.km.application.service.dto.base.BaseDTO;
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
 * @version $Revision: 1.55 $ $Date: 2016/12/22 13:04:53 $
 */
public class ElementFolderNode extends FolderNode {

	private final Log log = LogManager.get().getLog(ElementFolderNode.class);

	public ElementFolderNode(NavigationBean tree, FolderNode parent, Long folderId) {
		super(tree, parent, folderId);
		setAddObjectIcon("add-element");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.FolderNode#getNewFolderName()
	 */
	@Override
	public String getNewFolderName() {
		return treeBean.getMessage("costelement.newElementFolder.tree.msge");
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#getAddNewObjectAllowed()
	 */
	@Override
	public boolean getAddNewObjectAllowed() {
		UserModel user = treeBean.getUserFromSession();
		log.info("getAddNewObjectAllowed-------------------------------------------");
		log.info(user.getDefaultBrand());
		if (user.getDefaultBrand() == null || "".equals(user.getDefaultBrand().trim())) {
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
	public Node getNewObjectNodeInstance() {
		String defaultBrand = treeBean.getUserFromSession().getDefaultBrand();
		BrandModel owner = treeBean.getCostElementService().getDefaultBrand(defaultBrand);
		return new CostElementNode(treeBean, this, treeBean.getMessage("costelement.newElement.tree.msge"), null,
				Boolean.FALSE, Boolean.FALSE, owner, null);
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
			((CostElementDTO) node.getDto()).getElementVersion().getElement().setFolder(distFolder);
			return true;
		}
		return treeBean.getCostElementService().saveElementToFolder(node.getNodeId(), toNode.getNodeId());
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.FolderNode#getListClickedMessage()
	 */
	@Override
	protected String getListClickedMessage() {
		return treeBean.getMessage("action.element.list");
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
		return treeBean.getMessage("action.costElement.create");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.FolderNode#getExistingObjectMessage()
	 */
	@Override
	public String getExistingObjectMessage() {
		return treeBean.getMessage("costComponent.exist.sameDesignationOrNumber.message");
	}

	/**
	 * 
	 * @param model
	 * @return
	 */
	protected boolean isInactiveElementVisible(ElementModel model, BrandModel userBrand) {
		if (userBrand != null) {
			if (userBrand.equals(model.getOwner())) {
				return Boolean.FALSE;
			}
			List<ElementVersionUsersModel> users = model.getUsers();
			if (users != null) {
				for (ElementVersionUsersModel standUser : users) {
					if (standUser.getBrand().equals(userBrand)) {
						return Boolean.TRUE;
					}
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

	/**
	 * 16.09.2013: ZS: PTS_Requirement-22194:Integrate the filter of empty folders display Check if a folder has no sub
	 * folders or components
	 * 
	 * @return true/false
	 */
	/**
	 * 08.11.2013: ABA: PTS_Problem-26631: Check if a folder has no sub folders which contain filtered by brand
	 * components
	 * 
	 * @return true/false
	 */

	public boolean hasVisibleElements(Long folderId, UserModel user) {

		List<BrandModel> ownerFilters = user.getElementOwnerFilter();
		List<BrandModel> usersFilters = user.getElementUserFilter();
		String userBrand = user.getDefaultBrand();
		List<Long> elementListToBeFiltred = treeBean.getCostElementService().getElementsListToBeFiltred(folderId,
				userBrand);

		if (treeBean.getCostElementService().getHasElements(folderId)) {

			if (elementListToBeFiltred.isEmpty()) {
				return Boolean.FALSE;
			}
			// check If the user does not specify any filter
			if (ownerFilters.isEmpty() && usersFilters.isEmpty()) {
				return Boolean.TRUE;
			}
			log.info(" #Folder " + folderId + " has " + elementListToBeFiltred.size()
					+ " Elements to be filtred by brand");
			// Check If folder's elements are all setted not visible after being filtered by brand
			if (!ownerFilters.isEmpty()) {
				if (treeBean.getCostElementService().getHasVisibleElementsByOwnerFilter(ownerFilters, folderId,
						elementListToBeFiltred)) {
					return Boolean.TRUE;
				}
			}

			if (!usersFilters.isEmpty()) {
				if (treeBean.getCostElementService().getHasVisibleElementsByUsersFilter(usersFilters, folderId,
						elementListToBeFiltred)) {
					return Boolean.TRUE;
				}
			}

		}
		return Boolean.FALSE;
	}

	public boolean isEmptyFolder(Long folderId, Long objectContentId, UserModel user) {

		if (hasVisibleElements(folderId, user)) {
			return false;
		}
		List<FolderModel> subFolders = treeBean.getNavigationService().loadFolders(folderId, objectContentId);

		if (subFolders.size() > 0) {
			for (FolderModel folder : subFolders) {
				if (!isEmptyFolder(folder.getFolderId(), objectContentId, user)) {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.FolderNode#specificNodeClicked(javax.faces.event.ActionEvent)
	 * PTS_Change-26769 :If only one element or component is found in the folder, the List mask is displayed.
	 */
	@Override
	protected void specificNodeClicked(ActionEvent event) {
		// nothing to do just display the list
	}
}

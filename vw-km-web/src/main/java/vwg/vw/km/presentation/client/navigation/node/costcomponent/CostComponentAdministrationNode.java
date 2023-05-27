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

import vwg.vw.km.application.implementation.RoleManager;
import vwg.vw.km.application.service.dto.CostComponentDTO;
import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;
import vwg.vw.km.integration.persistence.model.ComponentModel;
import vwg.vw.km.integration.persistence.model.FolderModel;
import vwg.vw.km.integration.persistence.model.UserModel;
import vwg.vw.km.presentation.client.navigation.NavigationBean;
import vwg.vw.km.presentation.client.navigation.node.ContextMenuEnum;
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
 * @author Sebri Zouhaier changed by $Author: saidi $
 * @version $Revision: 1.55 $ $Date: 2014/01/29 16:20:49 $
 */
public class CostComponentAdministrationNode extends ComponentFolderNode {

	private final Log log = LogManager.get().getLog(CostComponentAdministrationNode.class);

	CostComponentSearchFolderNode costComponentSearchFolderNode = new CostComponentSearchFolderNode(treeBean, this);

	public CostComponentAdministrationNode(NavigationBean tree, FolderNode parent) {
		super(tree, parent, 7L);
		setContextMenu(ContextMenuEnum.ObjectFolderAdministrationContextMenu);
		setTemplateName("./costComponent/costComponentList.xhtml");
		setPopupPageName("./costComponent/popupList.xhtml");
		setActiveFilter(treeBean.getUserFromSession().isActiveComponentFilter());
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.FolderNode#getChilds()
	 */
	@Override
	public List<Node> getChilds() {
		List<Node> childs = new ArrayList<Node>();
		UserModel user = treeBean.getUserFromSession();
		childs.add(costComponentSearchFolderNode);
		List<FolderModel> folders = treeBean.getNavigationService().loadFolders(getNodeId(), getObjectContentId());
		for (FolderModel model : folders) {
			CostComponentFolderNode node = new CostComponentFolderNode(treeBean, this, model.getDesignation(),
					model.getFolderId());
			// 16.09.2013: ZS: PTS_Requirement-22194:Integrate the filter of empty folders display
			if (user.getShowEmptyFolders() != null && !user.getShowEmptyFolders() && node.isEmptyFolder(user)) {
				log.info("Component Folder " + model.getFolderId() + " is empty");
				node.setVisible(false);
			}
			childs.add(node);
		}

		CostComponentDTO costComponentDTO = new CostComponentDTO();
		costComponentDTO.setFolderId(getNodeId());
		costComponentDTO.setUserLogged(user);

		if (user.haveRole(RoleManager.Role.ADMINISTRATOR.value())) {
			costComponentDTO.setNewMode(Boolean.TRUE);
		}
		setDto(treeBean.getCostComponentService().loadDTOForList(costComponentDTO));
		List<ComponentModel> components = ((CostComponentDTO) getDto()).getComponents();
		List<ComponentModel> visibleComponents = new ArrayList<ComponentModel>();
		for (ComponentModel model : components) {
			CostComponentNode node = new CostComponentNode(treeBean, this, model.getNumberAndDesignation(),
					model.getComponentId(), Boolean.FALSE, Boolean.FALSE, model.getOwner(), model.getUsersImg(),
					model.getLibrayIcon());
			// show only active BS and used inactive BS for their users
			if (!model.getInactive() || (model.getInactive() && isInactiveComponentVisible(model))) {
				boolean isComponentVisibleByFilter = isComponentVisibleByFilter(model, user);
				node.setVisible(isComponentVisibleByFilter);
				if (isComponentVisibleByFilter) {
					visibleComponents.add(model);
				}
			} else {
				node.setVisible(false);
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
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#getRightIds()
	 */
	@Override
	public List<Long> getRightIds() {
		List<Long> rights = new ArrayList<Long>();
		rights.add(RoleManager.Right.MANAGE_COMPONENT.value());
		return rights;
	}

}

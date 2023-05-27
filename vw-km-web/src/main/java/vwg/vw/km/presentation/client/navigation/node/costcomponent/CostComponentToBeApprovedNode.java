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

import vwg.vw.km.application.implementation.EnumStatusManager;
import vwg.vw.km.application.service.dto.CostComponentDTO;
import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;
import vwg.vw.km.integration.persistence.model.BrandModel;
import vwg.vw.km.integration.persistence.model.ComponentModel;
import vwg.vw.km.integration.persistence.model.ComponentStandUsersModel;
import vwg.vw.km.integration.persistence.model.UserModel;
import vwg.vw.km.presentation.client.navigation.NavigationBean;
import vwg.vw.km.presentation.client.navigation.node.ContextMenuEnum;
import vwg.vw.km.presentation.client.navigation.node.FolderNode;
import vwg.vw.km.presentation.client.navigation.node.Node;
import vwg.vw.km.presentation.client.navigation.node.SearchFolderNode;
import vwg.vw.km.presentation.client.navigation.node.StaticNodeIdEnum;

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
 * @version $Revision: 1.31 $ $Date: 2014/03/05 17:09:43 $
 */
public class CostComponentToBeApprovedNode extends SearchFolderNode {

	private final Log log = LogManager.get().getLog(CostComponentToBeApprovedNode.class);

	public CostComponentToBeApprovedNode(NavigationBean tree, FolderNode parent) {
		super(tree, parent, StaticNodeIdEnum.COST_COMPONENT_TO_BE_APPROVED_NODE.value());
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
		CostComponentDTO costComponentDTO = new CostComponentDTO();
		List<Long> searchStatus = new ArrayList<Long>();
		searchStatus.add(EnumStatusManager.Status.TO_BE_APPROVED.value());
		costComponentDTO.setSearchStatus(searchStatus);
		costComponentDTO.setSearchMode(Boolean.TRUE);
		costComponentDTO.setSearchTitel(Boolean.FALSE);
		costComponentDTO.setUserLogged(loggedUser);
		setDto(treeBean.getCostComponentService().loadDTOForListByStatus(costComponentDTO));
		List<ComponentModel> components = ((CostComponentDTO) getDto()).getComponents();
		List<ComponentModel> visibleComponents = new ArrayList<ComponentModel>();
		for (ComponentModel model : components) {
			CostComponentNode node = new CostComponentNode(treeBean, this, model.getNumberAndDesignation(),
					model.getComponentId(), Boolean.TRUE, Boolean.FALSE, model.getOwner(), model.getUsersImg(),
					model.getLibrayIcon());

			if (treeBean.isLoggedUserAdminOnBrand(model.getOwner())) {
				node.setContextMenu(ContextMenuEnum.AdminWorkObjectContextMenu);
			}
			if (!treeBean.getUserFromSession().getUserBrands().contains(model.getOwner())) {
				node.setBranchContractedIcon(makeIcon("bgr.gif"));
				node.setBranchExpandedIcon(makeIcon("bgr.gif"));
				node.setLeafIcon(makeIcon("bgr.gif"));
				node.setLibrary("kgr.gif");
				node.setInactif(true);
			}
			// ZS: PTS_Problem-34855: Apply the user filter on BS pre-defined search folder
			boolean isComponentVisibleByFilter = isComponentVisibleByFilter(model, loggedUser);
			node.setVisible(isComponentVisibleByFilter);
			if (isComponentVisibleByFilter && !model.getInactive()) {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.BranchNode#getNewObjectNodeInstance()
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
	 * Check if the BS is visible by the filter
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
}

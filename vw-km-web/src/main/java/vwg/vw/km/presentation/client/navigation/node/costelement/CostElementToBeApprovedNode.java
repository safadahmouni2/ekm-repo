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
import vwg.vw.km.presentation.client.navigation.node.StaticNodeIdEnum;
import java.util.ArrayList;
import java.util.List;

import vwg.vw.km.application.implementation.EnumObjectTypeManager;
import vwg.vw.km.application.implementation.EnumStatusManager;
import vwg.vw.km.application.service.dto.CostElementDTO;
import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;
import vwg.vw.km.integration.persistence.model.BrandModel;
import vwg.vw.km.integration.persistence.model.ElementModel;
import vwg.vw.km.integration.persistence.model.ElementVersionUsersModel;
import vwg.vw.km.integration.persistence.model.UserModel;
import vwg.vw.km.presentation.client.navigation.NavigationBean;
import vwg.vw.km.presentation.client.navigation.node.ContextMenuEnum;
import vwg.vw.km.presentation.client.navigation.node.FolderNode;
import vwg.vw.km.presentation.client.navigation.node.Node;
import vwg.vw.km.presentation.client.navigation.node.SearchFolderNode;

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
 * @version $Revision: 1.36 $ $Date: 2016/10/24 14:53:44 $
 */
public class CostElementToBeApprovedNode extends SearchFolderNode {

	private final Log log = LogManager.get().getLog(CostElementToBeApprovedNode.class);

	public CostElementToBeApprovedNode(NavigationBean tree, FolderNode parent) {
		super(tree, parent, StaticNodeIdEnum.COST_ELEMENT_TO_BE_APPROVED_NODE.value());
		setTemplateName("./costElement/costElementList.xhtml");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.FolderNode#getChilds()
	 */
	@Override
	public List<Node> getChilds() {
		UserModel loggedUser = treeBean.getUserFromSession();
		List<Node> childs = new ArrayList<Node>();
		CostElementDTO costElementDTO = new CostElementDTO();
		List<Long> searchStatus = new ArrayList<Long>();
		searchStatus.add(EnumStatusManager.Status.TO_BE_APPROVED.value());
		costElementDTO.setSearchStatus(searchStatus);
		costElementDTO.setSearchMode(Boolean.TRUE);
		costElementDTO.setSearchTitel(Boolean.FALSE);
		costElementDTO.setUserLogged(loggedUser);
		setDto(treeBean.getCostElementService().loadDTOForListByStatus(costElementDTO));
		List<ElementModel> elements = ((CostElementDTO) getDto()).getElements();
		List<ElementModel> visibleElements = new ArrayList<ElementModel>();
		for (ElementModel model : elements) {
			CostElementNode node = new CostElementNode(treeBean, this, model.getNumberAndDesignation(),
					model.getElementId(), Boolean.TRUE, Boolean.FALSE, model.getOwner(), model.getUsersImg());

			if (treeBean.isLoggedUserAdminOnBrand(model.getOwner())) {
				node.setContextMenu(ContextMenuEnum.AdminWorkObjectContextMenu);
			}
			if (!loggedUser.getUserBrands().contains(model.getOwner())) {
				node.setBranchContractedIcon(makeIcon("egr.gif"));
				node.setBranchExpandedIcon(makeIcon("egr.gif"));
				node.setLeafIcon(makeIcon("egr.gif"));
				node.setInactif(true);
			}
			// ZS: PTS_Problem-34855: Apply the user filter on EL pre-defined search folder
			boolean isElementVisibleByFilter = isElementVisibleByFilter(model, loggedUser);
			node.setVisible(isElementVisibleByFilter);
			if (isElementVisibleByFilter && !model.getInactive()) {
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

	public Boolean moveObjectNodeToFolder(Node node, FolderNode toNode) {
		return treeBean.getCostElementService().saveElementToFolder(node.getNodeId(), toNode.getNodeId());
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
	 * 
	 * @param model
	 * @param user
	 * @return
	 */
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

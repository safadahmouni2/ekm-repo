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

import vwg.vw.km.application.implementation.RoleManager;
import vwg.vw.km.application.service.dto.CostElementDTO;
import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;
import vwg.vw.km.integration.persistence.model.BrandModel;
import vwg.vw.km.integration.persistence.model.ElementModel;
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
 * @version $Revision: 1.44 $ $Date: 2014/01/29 16:20:49 $
 */
public class CostElementFolderNode extends ElementFolderNode {

	private final Log log = LogManager.get().getLog(CostElementFolderNode.class);

	public CostElementFolderNode(NavigationBean tree, FolderNode parent, String category, Long folderId) {
		super(tree, parent, folderId);
		setMenuDisplayText(category);
		setMenuContentTitle(category);
		setFromBundle(Boolean.FALSE);
		setContextMenu(ContextMenuEnum.ObjectFolderContextMenu);
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
		UserModel user = treeBean.getUserFromSession();
		BrandModel userBrand = treeBean.getCostComponentService().getDefaultBrand(user.getDefaultBrand());
		List<FolderModel> folders = treeBean.getNavigationService().loadFolders(getNodeId(), getObjectContentId());
		for (FolderModel model : folders) {
			CostElementFolderNode node = new CostElementFolderNode(treeBean, this, model.getDesignation(),
					model.getFolderId());
			// 16.09.2013: ZS: PTS_Requirement-22194:Integrate the filter of empty folders display
			if (user.getShowEmptyFolders() != null && !user.getShowEmptyFolders() && node.isEmptyFolder(user)) {
				log.info("Element Folder " + model.getDesignation() + " is empty");
				node.setVisible(false);
			}
			childs.add(node);
		}
		CostElementDTO costElementDTO = new CostElementDTO();
		costElementDTO.setFolderId(getNodeId());
		costElementDTO.setUserLogged(user);

		if (user.haveRole(RoleManager.Role.ADMINISTRATOR.value())) {
			costElementDTO.setNewMode(Boolean.TRUE);
		}
		setDto(treeBean.getCostElementService().loadDTOForList(costElementDTO));
		List<ElementModel> elements = ((CostElementDTO) getDto()).getElements();
		List<ElementModel> visibleElements = new ArrayList<ElementModel>();
		for (ElementModel model : elements) {
			CostElementNode node = new CostElementNode(treeBean, this, model.getNumberAndDesignation(),
					model.getElementId(), Boolean.FALSE, Boolean.FALSE, model.getOwner(), model.getUsersImg());
			// show only active EL and used inactive EL for their users
			boolean isUsedInactiveElementVisible = (model.getInactive() && isInactiveElementVisible(model, userBrand));
			if (!model.getInactive() || (isUsedInactiveElementVisible)) {
				boolean isElementVisibleByFilter = isElementVisibleByFilter(model, user);
				node.setVisible(isElementVisibleByFilter);
				if (isElementVisibleByFilter) {
					// ZS: PTS_Problem_26518:Inactive used elements will be display in gray in the tree
					if (isUsedInactiveElementVisible) {
						node.setBranchContractedIcon(makeIcon("egr.gif"));
						node.setBranchExpandedIcon(makeIcon("egr.gif"));
						node.setLeafIcon(makeIcon("egr.gif"));
						node.setInactif(true);
					}
					visibleElements.add(model);
				}
			} else {
				node.setVisible(false);
			}
			childs.add(node);
			objectChildsCount++;
			indexOfLastObject = childs.size() - 1;
		}
		((CostElementDTO) getDto()).setElements(visibleElements);
		log.info("Number of Elements found: " + childs.size() + " for folder " + this.getMenuDisplayText());
		return childs;
	}

}

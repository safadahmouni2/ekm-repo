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

import vwg.vw.km.application.service.dto.CostComponentDTO;
import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;
import vwg.vw.km.integration.persistence.model.ComponentModel;
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
 * @author Sebri Zouhaier changed by $Author: saidi $
 * @version $Revision: 1.10 $ $Date: 2013/12/19 15:25:49 $
 */
public class CostComponentInactiveFolderNode extends CostComponentInactiveNode {

	private Long folderId;
	private final Log log = LogManager.get().getLog(CostComponentInactiveFolderNode.class);

	public CostComponentInactiveFolderNode(NavigationBean tree, FolderNode parent, String category, Long folderId) {
		super(tree, parent, -folderId);
		setMenuDisplayText(category);
		setMenuContentTitle(category);
		setFromBundle(Boolean.FALSE);
		setFolderId(folderId);
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
		List<FolderModel> folders = treeBean.getNavigationService().loadFolders(getFolderId(), getObjectContentId());
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
		setDto(treeBean.getCostComponentService().loadDTOForInactiveList(getFolderId(), costComponentDTO));
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

	public Long getFolderId() {
		return folderId;
	}

	public void setFolderId(Long folderId) {
		this.folderId = folderId;
	}

}

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
import java.util.Arrays;
import java.util.List;

import javax.faces.event.ActionEvent;

import vwg.vw.km.application.implementation.EnumObjectTypeManager;
import vwg.vw.km.application.service.dto.CostComponentDTO;
import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;
import vwg.vw.km.integration.persistence.model.BrandModel;
import vwg.vw.km.integration.persistence.model.ComponentModel;
import vwg.vw.km.integration.persistence.model.FolderModel;
import vwg.vw.km.integration.persistence.model.UserModel;
import vwg.vw.km.presentation.client.navigation.NavigationBean;
import vwg.vw.km.presentation.client.navigation.node.FolderNode;
import vwg.vw.km.presentation.client.navigation.node.Node;
import vwg.vw.km.presentation.client.navigation.node.SearchFolderNode;
import vwg.vw.km.presentation.client.navigation.node.StaticNodeIdEnum;
import vwg.vw.km.presentation.client.navigation.node.working.WorkingCostComponentNode;

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
public class CostComponentSearchResultNode extends SearchFolderNode {
	private final Log log = LogManager.get().getLog(CostComponentSearchResultNode.class);

	private boolean foldersFound = Boolean.FALSE;

	public CostComponentSearchResultNode(NavigationBean tree, FolderNode parent) {
		super(tree, parent, StaticNodeIdEnum.COST_COMPONENT_SEARCH_RESULT_NODE.value());
		setLeafIcon(makeIcon("search_result.png"));
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
		List<ComponentModel> components = new ArrayList<ComponentModel>();
		List<FolderModel> folders = new ArrayList<FolderModel>();
		if (getDto() != null) {
			components = ((CostComponentDTO) getDto()).getComponents();
			folders = ((CostComponentDTO) getDto()).getFolders();
		} else {
			CostComponentDTO costComponentDTO = new CostComponentDTO();
			costComponentDTO.setSearchMode(Boolean.TRUE);
			setDto(costComponentDTO);
		}
		// 24.10.2013: ZS: PTS_Requirement-22210: Integration of Search folders
		if (folders != null) {
			for (FolderModel model : folders) {
				CostComponentFolderNode node = new CostComponentFolderNode(treeBean, this, model.getDesignation(),
						model.getFolderId());
				childs.add(node);
				objectChildsCount++;
				foldersFound = Boolean.TRUE;
			}
		}
		if (components != null) {// to avoid dto !=null and components ==null
			for (ComponentModel model : components) {
				CostComponentNode n = new CostComponentNode(treeBean, this, model.getNumberAndDesignation(),
						model.getComponentId(), Boolean.FALSE, Boolean.TRUE, model.getOwner(), model.getUsersImg(),
						model.getLibrayIcon());
				childs.add(n);
				objectChildsCount++;
				indexOfLastObject = childs.size() - 1;
			}
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
		return EnumObjectTypeManager.COMPONENT_ID;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.FolderNode#nodeClicked(javax.faces.event.ActionEvent)
	 */
	@Override
	public void nodeClicked(ActionEvent event) {
		if (treeBean.getNeedSaveMessageConfirmation()) {
			treeBean.setGotoNode(this);
			return;
		}
		// Expand tree and display childs
		// Is child loaded, Is node expanded ?
		if (objectChildsCount == 1) {
			expandClicked(event);
		}
		if (!this.expanded) {
			expandClicked(event);
			this.setExpanded(Boolean.TRUE);
		} else {
			this.setExpanded(Boolean.FALSE);
		}
		treeBean.setSelectedNodeObject(this);
		Long costCompoentRootFolder = treeBean.getRootNode().getCostComponentAdministrationNode().getNodeId();
		Long userRootFolder = (treeBean.getRootNode().getAdministrationNode().getUsersAdmin() != null)
				? treeBean.getRootNode().getAdministrationNode().getUsersAdmin().getNodeId()
				: 0L;
		Long[] nodesToList = { costCompoentRootFolder, userRootFolder };
		// 24.10.2013: ZS: PTS_Requirement-22210: Integration of Search folders
		if (objectChildsCount == 1 && !Arrays.asList(nodesToList).contains(getNodeId()) && !foldersFound) {
			Node node = (childs.get(indexOfLastObject));
			node.nodeClicked(event);
			CostComponentDTO dto = (CostComponentDTO) node.getDto();
			Long componentId = dto.getComponentVersion().getComponent().getComponentId();
			Long folderId = dto.getComponentVersion().getComponent().getFolder().getFolderId();

			log.info("unique result found. Show Component in the tree " + componentId);
			if (componentId != null) {
				FolderModel folder = treeBean.getNavigationService().getFolder(folderId);
				// component is in a working folder
				if (folder.getCreatorRefId() != null) {
					Long userId = folder.getCreatorRefId();
					FolderNode workingNode = treeBean.getWorkingNode();
					workingNode.setExpanded(true);
					workingNode.expandClicked(event);

					FolderNode userWorkingFolder = (FolderNode) workingNode.getNodeById(userId,
							EnumObjectTypeManager.FOLDER_ID);
					WorkingCostComponentNode n = (WorkingCostComponentNode) treeBean.goToNode(userWorkingFolder,
							componentId, folderId, EnumObjectTypeManager.COMPONENT_ID, event);
					if (n != null) {
						n.nodeClicked(event);
					}
				} else {
					CostComponentNode n = (CostComponentNode) treeBean.goToNode(componentId, folderId,
							EnumObjectTypeManager.COMPONENT_ID, event);
					if (n != null) {
						n.nodeClicked(event);
					}
				}
			}
			this.setExpanded(Boolean.TRUE);
			return;
		}
		treeBean.setDownActionDefintion(getListClickedMessage());
	}
}

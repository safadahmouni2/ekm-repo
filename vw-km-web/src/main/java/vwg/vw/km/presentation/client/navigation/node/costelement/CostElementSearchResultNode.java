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
import java.util.Arrays;
import java.util.List;

import javax.faces.event.ActionEvent;

import vwg.vw.km.application.implementation.EnumObjectTypeManager;
import vwg.vw.km.application.service.dto.CostElementDTO;
import vwg.vw.km.integration.persistence.model.BrandModel;
import vwg.vw.km.integration.persistence.model.ElementModel;
import vwg.vw.km.integration.persistence.model.FolderModel;
import vwg.vw.km.presentation.client.navigation.NavigationBean;
import vwg.vw.km.presentation.client.navigation.node.FolderNode;
import vwg.vw.km.presentation.client.navigation.node.Node;
import vwg.vw.km.presentation.client.navigation.node.SearchFolderNode;
import vwg.vw.km.presentation.client.navigation.node.working.WorkingCostElementNode;

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
 * @author Sebri Zouhaier changed by $Author: mrada $
 * @version $Revision: 1.30 $ $Date: 2014/03/06 13:54:58 $
 */
public class CostElementSearchResultNode extends SearchFolderNode {

	private boolean foldersFound = Boolean.FALSE;

	public CostElementSearchResultNode(NavigationBean tree, FolderNode parent) {
		super(tree, parent, StaticNodeIdEnum.COST_ELEMENT_SEARCH_RESULT_NODE.value());
		setLeafIcon("images/search_result.png");
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
		List<ElementModel> elements = new ArrayList<ElementModel>();
		List<FolderModel> folders = new ArrayList<FolderModel>();
		if (getDto() != null) {
			elements = ((CostElementDTO) getDto()).getElements();
			folders = ((CostElementDTO) getDto()).getFolders();
		} else {
			CostElementDTO costElementDTO = new CostElementDTO();
			costElementDTO.setSearchMode(Boolean.TRUE);
			setDto(costElementDTO);
		}
		// 24.10.2013: ZS: PTS_Requirement-22210: Integration of Search folders
		if (folders != null) {
			for (FolderModel model : folders) {
				CostElementFolderNode node = new CostElementFolderNode(treeBean, this, model.getDesignation(),
						model.getFolderId());
				childs.add(node);
				objectChildsCount++;
				foldersFound = Boolean.TRUE;
			}
		}
		if (elements != null) {// to avoid null pointer exception dto!=null but elements ==null
			for (ElementModel model : elements) {
				CostElementNode n = new CostElementNode(treeBean, this, model.getNumberAndDesignation(),
						model.getElementId(), Boolean.FALSE, Boolean.TRUE, model.getOwner(), model.getUsersImg());
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.FolderNode#nodeClicked(javax.faces.event.ActionEvent)
	 */
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
			Node node = ((Node) childs.get(indexOfLastObject));
			node.nodeClicked(event);
			CostElementDTO dto = (CostElementDTO) node.getDto();
			Long elementId = dto.getElementVersion().getElement().getElementId();
			Long folderId = dto.getElementVersion().getElement().getFolder().getFolderId();
			FolderModel folder = treeBean.getNavigationService().getFolder(folderId);
			if (elementId != null) {
				// element is in a working folder
				if (folder.getCreatorRefId() != null) {
					Long userId = folder.getCreatorRefId();
					FolderNode workingNode = treeBean.getWorkingNode();
					workingNode.setExpanded(true);
					workingNode.expandClicked(event);

					FolderNode userWorkingFolder = (FolderNode) workingNode.getNodeById(userId,
							EnumObjectTypeManager.FOLDER_ID);
					WorkingCostElementNode n = (WorkingCostElementNode) treeBean.goToNode(userWorkingFolder, elementId,
							folderId, EnumObjectTypeManager.ELEMENT_ID, event);
					if (n != null) {
						n.nodeClicked(event);
						n.setExpanded(true);
						n.expandClicked(event);
					}
				} else {
					CostElementNode n = (CostElementNode) treeBean.goToNode(elementId, folderId,
							EnumObjectTypeManager.ELEMENT_ID, event);
					if (n != null) {
						n.nodeClicked(event);
						n.setExpanded(true);
						n.expandClicked(event);
					}
				}
			}
			this.setExpanded(Boolean.TRUE);
			return;
		}
		treeBean.setDownActionDefintion(getListClickedMessage());
	}
}

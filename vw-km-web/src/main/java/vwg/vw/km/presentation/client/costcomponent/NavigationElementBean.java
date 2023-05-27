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
package vwg.vw.km.presentation.client.costcomponent;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;

import org.icefaces.ace.event.TreeEvent;
import org.icefaces.ace.model.tree.NodeState;
import org.icefaces.ace.model.tree.NodeStateCreationCallback;
import org.icefaces.ace.model.tree.NodeStateMap;

import vwg.vw.km.application.service.base.BaseService;
import vwg.vw.km.application.service.dto.base.BaseDTO;
import vwg.vw.km.application.service.logic.CostElementService;
import vwg.vw.km.application.service.logic.NavigationService;
import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;
import vwg.vw.km.presentation.client.base.BaseBean;
import vwg.vw.km.presentation.client.navigation.TreeDataModel;

/**
 * <p>
 * Title: VW_KM
 * <p>
 * Description : Class description goes here
 * </p>
 * <p>
 * Copyright: cl (c) 2011
 * </p>
 * 
 * @author zouhairs changed by $Author: mrad $
 * @version $Revision: 1.7 $ $Date: 2016/11/14 19:20:34 $
 */
public class NavigationElementBean extends BaseBean<BaseDTO, BaseService<BaseDTO>> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7025818664529876632L;

	// logger
	private final Log log = LogManager.get().getLog(NavigationElementBean.class);

	// bound to components value attribute
	private TreeDataModel model = null;

	private NavigationService navigationService;

	private CostElementService costElementService;

	CostElementAdministrationNode rootNode = null;

	public HashMap<Long, CostElementAttachNode> elementsMap = new HashMap<Long, CostElementAttachNode>();

	public List<Long> assignedElementIds;

	private boolean isDirty = false;

	public CostElementAdministrationNode getRootNode() {
		return rootNode;
	}

	public TreeDataModel getTreeModel() {
		// build root node so that children can be attached
		rootNode = new CostElementAdministrationNode(this, null);
		rootNode.setExpanded(Boolean.FALSE);
		rootNode.nodeClicked(new ActionEvent(FacesContext.getCurrentInstance().getViewRoot()));
		model = new TreeDataModel(rootNode);
		return model;
	}

	/**
	 * Getter of tree model
	 * 
	 * @return defaultTreeModel object
	 */
	public TreeDataModel getModel() {
		if (model != null) {
			if (isDirty) {
				model = getTreeModel();
				setDirty(false);
			}
			return model;
		} else {
			return getTreeModel();
		}
	}

	/**
	 * Setter of tree model
	 * 
	 * @param model
	 */
	public void setModel(TreeDataModel model) {
		this.model = model;
	}

	/**
	 * expand Collapse node
	 * 
	 * @param event
	 */
	public void expandCollapse(AjaxBehaviorEvent event) {
		log.info("expandCollapse called");
		ElementFolderNode locationNode;
		if (event instanceof TreeEvent) {
			TreeEvent treeEvent = (TreeEvent) event;
			Object data = treeEvent.getObject();
			if (data != null && data instanceof ElementFolderNode) {
				locationNode = (ElementFolderNode) data;
				if (treeEvent.isExpandEvent()) {
					log.info("Expand requested on node " + locationNode.getClass().getSimpleName() + " :: "
							+ locationNode.getMenuDisplayText());
					locationNode.expandClicked(event);
					locationNode.setExpanded(true);
				} else if (treeEvent.isContractEvent()) {
					locationNode.collapseClicked(event);
					locationNode.setExpanded(false);
				}
			}
		}
	}

	/**
	 * Getter of the navigationService
	 * 
	 * @return instance of navigationService
	 */
	public NavigationService getNavigationService() {
		return navigationService;
	}

	/**
	 * Setter of navigationService
	 * 
	 * @param navigationService
	 */
	public void setNavigationService(NavigationService navigationService) {
		this.navigationService = navigationService;
	}

	/**
	 * Getter of the costElementService
	 * 
	 * @return instance of costElementService
	 */
	public CostElementService getCostElementService() {
		return costElementService;
	}

	/**
	 * Setter of costElementService
	 * 
	 * @param costElementService
	 */
	public void setCostElementService(CostElementService costElementService) {
		this.costElementService = costElementService;
	}

	/**
	 * Return map of costElementAttachNode objects
	 * 
	 * @return HashMap
	 */
	public HashMap<Long, CostElementAttachNode> getElementsMap() {
		return elementsMap;
	}

	/**
	 * Return the list of the assigned elements
	 * 
	 * @return
	 */
	public List<Long> getAssignedElementIds() {
		return assignedElementIds;
	}

	public void setAssignedElementIds(List<Long> assignedElementIds) {
		this.assignedElementIds = assignedElementIds;
	}

	/**
	 * Return the value of isDirty flag
	 * 
	 * @return true/false
	 */
	public boolean isDirty() {
		return isDirty;
	}

	/**
	 * Setter of isDirty flag
	 * 
	 * @param isDirty
	 */
	public void setDirty(boolean isDirty) {
		this.isDirty = isDirty;
	}

	private NodeStateMap stateMap = new NodeStateMap();

	public NodeStateMap getStateMap() {
		return stateMap;
	}

	public void setStateMap(NodeStateMap stateMap) {
		this.stateMap = stateMap;
	}

	private NodeStateCreationCallback contractProvinceInit = new ExpandAllNodeInitCallback();

	public NodeStateCreationCallback getContractProvinceInit() {
		return contractProvinceInit;
	}

	public void setContractProvinceInit(NodeStateCreationCallback contractProvinceInit) {
		this.contractProvinceInit = contractProvinceInit;
	}

	public class ExpandAllNodeInitCallback implements NodeStateCreationCallback, Serializable {
		
		private static final long serialVersionUID = -1126087076659654728L;

		@Override
		public NodeState initializeState(NodeState newState, Object node) {
			newState.setExpanded(true);
			return newState;
		}
	}
}

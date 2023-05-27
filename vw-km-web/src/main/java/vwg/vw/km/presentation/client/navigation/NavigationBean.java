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
package vwg.vw.km.presentation.client.navigation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.swing.tree.TreeNode;

import org.icefaces.ace.event.DragDropEvent;
import org.icefaces.ace.event.TreeEvent;
import org.icefaces.ace.model.tree.NodeState;
import org.icefaces.ace.model.tree.NodeStateCreationCallback;
import org.icefaces.ace.model.tree.NodeStateMap;

import vwg.vw.km.application.implementation.EnumObjectTypeManager;
import vwg.vw.km.application.implementation.RoleManager;
import vwg.vw.km.application.service.base.BaseService;
import vwg.vw.km.application.service.dto.base.BaseDTO;
import vwg.vw.km.application.service.logic.ChangeHistoryService;
import vwg.vw.km.application.service.logic.CostComponentService;
import vwg.vw.km.application.service.logic.CostElementService;
import vwg.vw.km.application.service.logic.HourlyRateCatalogService;
import vwg.vw.km.application.service.logic.NavigationService;
import vwg.vw.km.application.service.logic.NewsService;
import vwg.vw.km.application.service.logic.UserService;
import vwg.vw.km.application.service.logic.UserSettingsService;
import vwg.vw.km.application.service.logic.WorkAreaService;
import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;
import vwg.vw.km.integration.persistence.model.UserModel;
import vwg.vw.km.presentation.client.base.BaseBean;
import vwg.vw.km.presentation.client.costcomponent.NavigationElementBean;
import vwg.vw.km.presentation.client.navigation.node.BranchNode;
import vwg.vw.km.presentation.client.navigation.node.CalculatorModuleRootNode;
import vwg.vw.km.presentation.client.navigation.node.CostManagementModuleRootNode;
import vwg.vw.km.presentation.client.navigation.node.FolderNode;
import vwg.vw.km.presentation.client.navigation.node.Node;
import vwg.vw.km.presentation.client.navigation.node.StaticFolderNode;

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
 * @author zouhairs changed by $Author: saidi $
 * @version $Revision: 1.95 $ $Date: 2020/08/21 07:57:27 $
 */
public class NavigationBean extends BaseBean<BaseDTO, BaseService<BaseDTO>> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3404353920351609517L;

	// logger
	private final Log log = LogManager.get().getLog(NavigationBean.class);

	// bound to components value attribute
	private TreeDataModel model = null;

	private Node selectedNodeObject = null;

	private Node cuttedNode = null;

	private Node copiedNode = null;

	private NavigationService navigationService;

	private CostElementService costElementService;

	private WorkAreaService workAreaService;

	private ChangeHistoryService changeHistoryService;

	private UserService userService;

	private HourlyRateCatalogService hourlyRateCatalogService;

	private UserSettingsService userSettingsService;

	private CalculatorModuleRootNode calculatorModuleRootNode = null;

	private CostManagementModuleRootNode costManagementModuleRootNode = null;

	public String downActionDefintion = "";

	private CostComponentService costComponentService;

	public Map<Long, Node> objectTypeSearchResultNode = new HashMap<Long, Node>();

	public Map<Long, Map<Long, StaticFolderNode>> objectTypeStatusResultNodes = new HashMap<Long, Map<Long, StaticFolderNode>>();

	private Node nodeToBeDeleted = null;

	private FolderNode folderNodeToBeAdded = null;

	protected NavigationElementBean navigationElementBean;

	private Node gotoNode = null;

	private NewsService newsService;

	public Node getGotoNode() {
		return gotoNode;
	}

	public void setGotoNode(Node gotoNode) {
		this.gotoNode = gotoNode;
	}

	public void setNavigationElementBean(NavigationElementBean navigationElementBean) {
		this.navigationElementBean = navigationElementBean;
	}

	public NavigationElementBean getNavigationElementBean() {
		return navigationElementBean;
	}

	public static FolderNode NEW_FOLDER = new FolderNode(null, null, null) {

		@Override
		public Boolean moveObjectNodeToFolder(Node node, FolderNode toNode, boolean isCopy) {
			return null;
		}

		@Override
		public Long getObjectContentId() {
			return null;
		}

		@Override
		public String getNewFolderName() {
			return null;
		}

		@Override
		protected String getListClickedMessage() {
			return null;
		}

		@Override
		protected String getCutMessage() {
			return null;
		}

		@Override
		protected String getDeleteSuccessMessage() {
			return null;
		}

		@Override
		protected String getPasteMessage() {
			return null;
		}

		@Override
		protected String getEditMessage() {
			return null;
		}

		@Override
		protected String getCreateMessage() {
			return null;
		}
	};

	private FolderNode folderNodeToBeEdited = NEW_FOLDER;

	public FolderNode getFolderNodeToBeEdited() {
		return folderNodeToBeEdited;
	}

	public void setFolderNodeToBeEdited(FolderNode folderNodeToBeEdited) {
		this.folderNodeToBeEdited = folderNodeToBeEdited;
	}

	private boolean showDeleteConfirm = Boolean.FALSE;

	private boolean showAddFolderConfirm = Boolean.FALSE;

	private String deleteMessage = "";

	private boolean showError = Boolean.FALSE;

	private String errorMessage = "";

	private boolean showPrivacyMessage = Boolean.FALSE;

	public CalculatorModuleRootNode getRootNode() {
		return calculatorModuleRootNode;
	}

	/**
	 * build root node and append all its childs
	 * 
	 * @return
	 */
	public TreeDataModel getTreeModel() {

		List<TreeNode> treeRoots = new ArrayList<TreeNode>();

		// build root node so that children can be attached
		calculatorModuleRootNode = new CalculatorModuleRootNode(this);
		costManagementModuleRootNode = new CostManagementModuleRootNode(this);

		if (calculatorModuleRootNode.isAccessAllowed()) {

			// model is accessed by by the ice:tree component
			calculatorModuleRootNode.setExpanded(Boolean.FALSE);
			calculatorModuleRootNode.nodeClicked(new ActionEvent(FacesContext.getCurrentInstance().getViewRoot()));
			selectedNodeObject = calculatorModuleRootNode;

			/*
			 * kostenmanagementNode.setExpanded(Boolean.FALSE); kostenmanagementNode.nodeClicked(new
			 * ActionEvent(FacesContext.getCurrentInstance().getViewRoot())); selectedNodeObject = kostenmanagementNode;
			 */

			// UserModel loggedUser = this.getUserFromSession();
			// if (loggedUser.getDisableChangesNotifications() == false &&
			// !loggedUser.getRoles().isEmpty()) {
			// ChangeHistoryNode changeHistoryNode = (ChangeHistoryNode) getChangeHistoryNode();
			// changeHistoryNode.expandClicked(new ActionEvent(FacesContext.getCurrentInstance().getViewRoot()));
			// changeHistoryNode.setExpanded(Boolean.TRUE);
			// LastChangesNotificationsNode changesNotificationsNode = (LastChangesNotificationsNode)
			// getChangeHistoryNotificationNode();
			// changesNotificationsNode.nodeClicked(new ActionEvent(FacesContext.getCurrentInstance().getViewRoot()));
			// }

			treeRoots.add(calculatorModuleRootNode);

		}

		if (costManagementModuleRootNode.isAccessAllowed()) {
			costManagementModuleRootNode.setExpanded(Boolean.FALSE);
			if (!calculatorModuleRootNode.isAccessAllowed()) {
				costManagementModuleRootNode
						.nodeClicked(new ActionEvent(FacesContext.getCurrentInstance().getViewRoot()));
				selectedNodeObject = costManagementModuleRootNode;
			}

			treeRoots.add(costManagementModuleRootNode);
		}

		if (costManagementModuleRootNode.isAccessAllowed() || calculatorModuleRootNode.isAccessAllowed()) {
			model = new TreeDataModel(treeRoots);
		}

		return model;

	}

	public TreeDataModel getModel() {
		if (model != null) {
			return model;
		} else {
			return getTreeModel();
		}

	}

	public void setModel(TreeDataModel model) {
		this.model = model;
	}

	public Node getSelectedNodeObject() {
		return selectedNodeObject;
	}

	public FolderNode getSelectedNodeObjectAsFolder() {
		return (FolderNode) selectedNodeObject;
	}

	public void setSelectedNodeObject(Node selectedNodeObject) {
		this.selectedNodeObject = selectedNodeObject;
	}

	public Node getNodeToBeDeleted() {
		return nodeToBeDeleted;
	}

	public void setNodeToBeDeleted(Node nodeToBeDeleted) {
		this.nodeToBeDeleted = nodeToBeDeleted;
	}

	/**
	 * expand Collapse node
	 * 
	 * @param event
	 */
	public void expandCollapse(AjaxBehaviorEvent event) {
		log.info("expandCollapse called");
		BranchNode locationNode;
		if (event instanceof TreeEvent) {
			TreeEvent treeEvent = (TreeEvent) event;
			Object data = treeEvent.getObject();
			if (data != null && data instanceof BranchNode) {
				locationNode = (BranchNode) data;
				if (getNeedSaveMessageConfirmation()) {
					// return node as initial state
					if (treeEvent.isExpandEvent()) {
						locationNode.setExpanded(false);
					} else {
						locationNode.setExpanded(true);
					}
					return;
				}

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
	 * listener for the drag and drop action
	 * 
	 * @param dragEvent
	 */
	public void dragPanelListener(DragDropEvent dragEvent) {
		log.info("drop event called");
		if (getNeedSaveMessageConfirmation()) {
			return;
		}
		String draggedId = dragEvent.getDragId();
		String droppedId = dragEvent.getDropId();

		if (draggedId.equals(droppedId)) {
			return;
		}
		log.info("dragged id : " + draggedId);
		draggedId = draggedId.substring(draggedId.indexOf("-") + 2);
		log.info("dragged id : " + draggedId);
		draggedId = draggedId.substring(0, draggedId.indexOf("-") - 1);
		log.info("dragged id : " + draggedId);

		String[] draggedIndexes = draggedId.split(":");

		log.info("dragged id : " + droppedId);
		droppedId = droppedId.substring(droppedId.indexOf("-") + 2);
		log.info("dragged id : " + droppedId);
		droppedId = droppedId.substring(0, droppedId.indexOf("-") - 1);
		log.info("dragged id : " + droppedId);

		String[] droppedIndexes = droppedId.split(":");

		TreeNode dragged = model.getRootNode();
		log.info("dragged : " + dragged.toString());
		for (int i = 1; i < draggedIndexes.length; i++) {
			dragged = dragged.getChildAt(Integer.parseInt(draggedIndexes[i]));
			log.info("dragged : " + dragged.toString());
		}

		TreeNode dropped = model.getRootNode();
		log.info("dropped : " + dropped.toString());
		for (int i = 1; i < droppedIndexes.length; i++) {
			dropped = dropped.getChildAt(Integer.parseInt(droppedIndexes[i]));
			log.info("dropped : " + dropped.toString());
		}

		// No action when drag target and drop target are equals
		// If drop target is not a folder node escape
		if (!(dropped instanceof FolderNode)) {
			return;
		}
		FolderNode dropNode = (FolderNode) dropped;
		Node dragNode = (Node) dragged;
		if (dropNode.equals(dragNode.getParent())) {
			return;
		}
		dragNode.cutClicked(new ActionEvent(dragEvent.getComponent()));
		dropNode.pasteClicked(new ActionEvent(dragEvent.getComponent()));

	}

	public NavigationService getNavigationService() {
		return navigationService;
	}

	public void setNavigationService(NavigationService navigationService) {
		this.navigationService = navigationService;
	}

	public Node getCuttedNode() {
		return cuttedNode;
	}

	public void setCuttedNode(Node cuttedNode) {
		this.cuttedNode = cuttedNode;
	}

	public Node getCopiedNode() {
		return copiedNode;
	}

	public void setCopiedNode(Node copiedNode) {
		this.copiedNode = copiedNode;
	}

	private Node sourceCopyNode;

	public Node getSourceCopydNode() {
		return sourceCopyNode;
	}

	public void setSourceCopyNode(Node sourceCopyNode) {
		this.sourceCopyNode = sourceCopyNode;
	}

	/**
	 * get DownActionDefintion this field is emptied after return value
	 * 
	 * @return
	 */
	public String getDownActionDefintion() {
		String returnValue = downActionDefintion;
		setDownActionDefintion("");
		return returnValue;
	}

	/**
	 * get NeedSaveMessageConfirmation check on list of page changes in DTO
	 * 
	 * @return
	 */
	public boolean getNeedSaveMessageConfirmation() {
		Node selectedNode = getSelectedNodeObject();
		if (selectedNode != null && selectedNode.getDto() != null && selectedNode.getDto().getPageChanges() != null) {
			if (getSelectedNodeObject().getDto().getPageChanges().size() > 0) {
				getSelectedNodeObject().getDto().setShowSaveChangesPopup(Boolean.TRUE);
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}

	/**
	 * this method can navigate from root node to requested node by expanding all nodes in the way
	 * 
	 * @param nodeId
	 * @param startFolderId
	 * @param objectTypeId
	 * @param event
	 * @return
	 */
	public Node goToNode(Long nodeId, Long startFolderId, Long objectTypeId, ActionEvent event) {
		FolderNode actualNode = getRootNode();
		List<Long> folderIdsHierarchy = navigationService.getFolderIdsHierarchy(startFolderId);
		for (int i = 0; i < folderIdsHierarchy.size(); i++) {
			actualNode.setExpanded(true);
			actualNode.expandClicked(event);
			actualNode = (FolderNode) actualNode.getNodeById(folderIdsHierarchy.get(i),
					EnumObjectTypeManager.FOLDER_ID);
		}
		actualNode.setExpanded(true);
		actualNode.expandClicked(event);
		return actualNode.getNodeById(nodeId, objectTypeId);
	}

	public Node goToNode(FolderNode actualNode, Long nodeId, Long startFolderId, Long objectTypeId, ActionEvent event) {
		List<Long> folderIdsHierarchy = navigationService.getFolderIdsHierarchy(startFolderId);
		for (int i = 0; i < folderIdsHierarchy.size(); i++) {
			actualNode.setExpanded(true);
			actualNode.expandClicked(event);
			actualNode = (FolderNode) actualNode.getNodeById(folderIdsHierarchy.get(i),
					EnumObjectTypeManager.FOLDER_ID);
		}
		actualNode.setExpanded(true);
		actualNode.expandClicked(event);
		return actualNode.getNodeById(nodeId, objectTypeId);
	}

	public void acceptDeleteClicked(ActionEvent event) {
		log.info("Delete accepted for " + nodeToBeDeleted.getMenuDisplayText());
		closeConfirmDeletePopup(event);
		nodeToBeDeleted.acceptDeleteClicked(event);
	}

	public void closeConfirmDeletePopup(ActionEvent event) {
		setShowDeleteConfirm(Boolean.FALSE);
		setDeleteMessage(null);
	}

	public void openConfirmDeletePopup(String msg) {
		setDeleteMessage(msg);
		setShowDeleteConfirm(Boolean.TRUE);
	}

	public void closeErrorPopup(ActionEvent event) {
		setShowError(Boolean.FALSE);
		setErrorMessage(null);
	}

	public void openErrorPopup(String message) {
		setShowError(Boolean.TRUE);
		setErrorMessage(message);
	}

	/**
	 * refresh childs of a Search Status Node
	 * 
	 * @param event
	 * @param objectContentTypeId
	 *            type of node
	 * @param statusFrom
	 *            start status of modified child
	 * @param statusTo
	 *            end status of modified child
	 */
	public void refreshSearchStatusNode(ActionEvent event, Long objectContentTypeId, Long statusFrom, Long statusTo) {
		Map<Long, StaticFolderNode> statusNode = getObjectTypeStatusResultNodes().get(objectContentTypeId);
		if (statusNode != null) {
			for (Map.Entry<Long, StaticFolderNode> entry : statusNode.entrySet()) {
				if (entry.getKey().equals(statusFrom) || entry.getKey().equals(statusTo)) {
					log.info("Refresh search status nodes for status " + entry.getKey());
					entry.getValue().expandClicked(event);
				}
			}
		}
	}

	// PTS requirement 22211: ABA: Refresh tree for the user settings definition: show changes notifications page
	// setting
	public Node getChangeHistoryNode() {
		FolderNode actualNode = getRootNode();
		return actualNode.getNodeById(-15L, EnumObjectTypeManager.FOLDER_ID);
	}

	// PTS requirement 22211: ABA: Refresh tree for the user settings definition: show changes notifications page
	// setting
	public Node getChangeHistoryNotificationNode() {
		StaticFolderNode actualNode = (StaticFolderNode) getChangeHistoryNode();
		return actualNode.getNodeById(16L, EnumObjectTypeManager.FOLDER_ID);
	}

	public Node getRootComponentNode() {
		FolderNode actualNode = getRootNode();
		return actualNode.getNodeById(7L, EnumObjectTypeManager.FOLDER_ID);
	}

	public Node getRootElementNode() {
		FolderNode actualNode = getRootNode();
		return actualNode.getNodeById(6L, EnumObjectTypeManager.FOLDER_ID);
	}

	public FolderNode getWorkingNode() {
		FolderNode actualNode = getRootNode();
		return (FolderNode) actualNode.getNodeById(-14L, EnumObjectTypeManager.FOLDER_ID);
	}

	public FolderNode getUserAdministrationNode() {
		FolderNode actualNode = getRootNode();
		actualNode = (FolderNode) actualNode.getNodeById(1L, EnumObjectTypeManager.FOLDER_ID);
		return (FolderNode) actualNode.getNodeById(3L, EnumObjectTypeManager.FOLDER_ID);
	}

	public FolderNode getNewsAdministrationNode() {
		FolderNode actualNode = getRootNode();
		actualNode = (FolderNode) actualNode.getNodeById(1L, EnumObjectTypeManager.FOLDER_ID);
		return (FolderNode) actualNode.getNodeById(-16L, EnumObjectTypeManager.FOLDER_ID);
	}

	public void setDownActionDefintion(String downActionDefintion) {
		this.downActionDefintion = downActionDefintion;
	}

	public CostElementService getCostElementService() {
		return costElementService;
	}

	public void setCostElementService(CostElementService costElementService) {
		this.costElementService = costElementService;
	}

	public WorkAreaService getWorkAreaService() {
		return workAreaService;
	}

	public void setWorkAreaService(WorkAreaService workAreaService) {
		this.workAreaService = workAreaService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setHourlyRateCatalogService(HourlyRateCatalogService hourlyRateCatalogService) {
		this.hourlyRateCatalogService = hourlyRateCatalogService;
	}

	public HourlyRateCatalogService getHourlyRateCatalogService() {
		return hourlyRateCatalogService;
	}

	public CostComponentService getCostComponentService() {
		return costComponentService;
	}

	public void setCostComponentService(CostComponentService costComponentService) {
		this.costComponentService = costComponentService;
	}

	public UserSettingsService getUserSettingsService() {
		return userSettingsService;
	}

	public void setUserSettingsService(UserSettingsService userSettingsService) {
		this.userSettingsService = userSettingsService;
	}

	public Map<Long, Node> getObjectTypeSearchResultNode() {
		return objectTypeSearchResultNode;
	}

	public void setObjectTypeSearchResultNode(Map<Long, Node> objectTypeSearchResultNode) {
		this.objectTypeSearchResultNode = objectTypeSearchResultNode;
	}

	public Map<Long, Map<Long, StaticFolderNode>> getObjectTypeStatusResultNodes() {
		return objectTypeStatusResultNodes;
	}

	public void setObjectTypeStatusResultNodes(Map<Long, Map<Long, StaticFolderNode>> objectTypeStatusResultNodes) {
		this.objectTypeStatusResultNodes = objectTypeStatusResultNodes;
	}

	public boolean isShowDeleteConfirm() {
		return showDeleteConfirm;
	}

	public void setShowDeleteConfirm(boolean showDeleteConfirm) {
		this.showDeleteConfirm = showDeleteConfirm;
	}

	public String getDeleteMessage() {
		return deleteMessage;
	}

	public void setDeleteMessage(String deleteMessage) {
		this.deleteMessage = deleteMessage;
	}

	public boolean isShowError() {
		return showError;
	}

	public void setShowError(boolean showError) {
		this.showError = showError;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public void setChangeHistoryService(ChangeHistoryService changeHistoryService) {
		this.changeHistoryService = changeHistoryService;
	}

	public ChangeHistoryService getChangeHistoryService() {
		return changeHistoryService;
	}

	public void acceptAddFolderClicked(ActionEvent event) {
		log.info("Folder accepted for " + folderNodeToBeAdded.getMenuDisplayText());
		closeConfirmAddFolderPopup(event);
		folderNodeToBeAdded.addHiddenFolderClicked(event);
	}

	public void closeConfirmAddFolderPopup(ActionEvent event) {
		setShowAddFolderConfirm(Boolean.FALSE);
	}

	public void openConfirmAddFolderPopup() {
		setShowAddFolderConfirm(Boolean.TRUE);
	}

	public boolean isShowAddFolderConfirm() {
		return showAddFolderConfirm;
	}

	public void setShowAddFolderConfirm(boolean showAddFolderConfirm) {
		this.showAddFolderConfirm = showAddFolderConfirm;
	}

	public FolderNode getFolderNodeToBeAdded() {
		return folderNodeToBeAdded;
	}

	public void setFolderNodeToBeAdded(FolderNode folderNodeToBeAdded) {
		this.folderNodeToBeAdded = folderNodeToBeAdded;
	}

	private Map<Long, Set<Node>> markedNodes = new HashMap<Long, Set<Node>>();

	private Set<Node> cuttedMarkedNodes = null;

	// PTS problem 34310 :ZSE change Set to List to accept duplicate object
	// copiedMarkedNodes is changed to list to accept duplicate object with id is null
	private List<Node> copiedMarkedNodes = null;

	public Map<Long, Set<Node>> getMarkedNodes() {
		return markedNodes;
	}

	public void setMarkedNodes(Map<Long, Set<Node>> markedNodes) {
		this.markedNodes = markedNodes;
	}

	public Set<Node> getCuttedMarkedNodes() {
		return cuttedMarkedNodes;
	}

	public void setCuttedMarkedNodes(Set<Node> cuttedMarkedNodes) {
		this.cuttedMarkedNodes = cuttedMarkedNodes;
	}

	public List<Node> getCopiedMarkedNodes() {
		return copiedMarkedNodes;
	}

	public void setCopiedMarkedNodes(List<Node> copiedMarkedNodes) {
		this.copiedMarkedNodes = copiedMarkedNodes;
	}

	private Node rightClickedNodeObject = null;

	public Node getRightClickedNodeObject() {
		return rightClickedNodeObject;
	}

	public void setRightClickedNodeObject(Node rightClickedNodeObject) {
		if (rightClickedNodeObject != null) {
			log.info("Current Node right clicked is : " + rightClickedNodeObject.getMenuDisplayText());
		}
		this.rightClickedNodeObject = rightClickedNodeObject;
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

		private static final long serialVersionUID = -2902260680659393572L;

		@Override
		public NodeState initializeState(NodeState newState, Object node) {
			newState.setExpanded(true);
			return newState;
		}
	}

	public NewsService getNewsService() {
		return newsService;
	}

	public void setNewsService(NewsService newsService) {
		this.newsService = newsService;
	}

	public boolean isShowPrivacyMessage() {
		UserModel loggedUser = this.getUserFromSession();
		this.showPrivacyMessage = loggedUser.isToBeNotified();
		return this.showPrivacyMessage;

	}

	public void setShowPrivacyMessage(boolean showPrivacyMessage) {
		this.showPrivacyMessage = showPrivacyMessage;
	}

	public void closePrivacyMessage(ActionEvent event) {
		UserModel loggedUser = this.getUserFromSession();
		loggedUser.setToBeNotified(false);
		this.setSessionLoginParam(loggedUser);
		this.userService.saveNotificationDate(loggedUser);
		setShowPrivacyMessage(Boolean.FALSE);
	}

	public boolean getEmptyModel() {
		UserModel loggedUser = this.getUserFromSession();
		return !loggedUser.haveRightToDo(RoleManager.Right.MANAGE_CALCULATOR_MODULE.value())
				&& !loggedUser.haveRightToDo(RoleManager.Right.MANAGE_COST_MANAGEMENT_MODULE.value());
	}

}

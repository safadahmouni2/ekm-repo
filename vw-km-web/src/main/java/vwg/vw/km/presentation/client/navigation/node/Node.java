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
package vwg.vw.km.presentation.client.navigation.node;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Set;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.swing.tree.MutableTreeNode;

import org.apache.commons.collections4.CollectionUtils;
import org.icefaces.ace.model.tree.NodeState;

import vwg.vw.km.application.implementation.EnumStatusManager;
import vwg.vw.km.application.service.dto.base.BaseDTO;
import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;
import vwg.vw.km.presentation.client.base.BaseBean;
import vwg.vw.km.presentation.client.navigation.NavigationBean;

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
 * @version $Revision: 1.101 $ $Date: 2016/11/03 14:22:56 $
 */
public abstract class Node extends IceUserObject {

	// logger
	private final Log log = LogManager.get().getLog(Node.class);

	// This the main class that build the tree and using this class we can
	// access for service to load dynamic node from
	// database
	protected NavigationBean treeBean;

	// template, default panel to make visible in a panel stack
	private String templateName = "welcomePage.xhtml";

	private String popupPageName = "noPopup.xhtml";

	// True indicates that there is content associated with link and as a
	// results templateName and contentPanelName can be used. Otherwise we
	// just toggle the branch visibility.
	private boolean pageContent = Boolean.TRUE;

	// display or no the context menu for this given node
	private boolean displayContextMenu = Boolean.FALSE;

	// get text and title from bundle or no
	private boolean fromBundle = Boolean.TRUE;

	// text to be displayed in navigation link
	private String menuDisplayText;

	// title information to be displayed
	private String menuContentTitle;

	// node id
	private Long nodeId;

	private ContextMenuInterface contextMenu;

	private BranchNode parent;

	private BaseDTO dto;

	// add add icon id
	private String addObjectIcon = "add_file";

	// display or no the owner of the node
	private boolean displayOwner = Boolean.FALSE;

	private String ownerImg;

	// display or no the users of the node
	private boolean displayUsers = Boolean.FALSE;

	private List<String> users;

	// display or no the library of the node
	private boolean displayLibrary = Boolean.FALSE;

	private String library;

	private boolean inactif = Boolean.FALSE;

	// active or no filter of the node
	private boolean activeFilter = Boolean.FALSE;

	// display or no the node in tree
	private boolean visible = Boolean.TRUE;

	public String getAddObjectIcon() {
		return addObjectIcon;
	}

	public void setAddObjectIcon(String addObjectIcon) {
		this.addObjectIcon = addObjectIcon;
	}

	public Node(NavigationBean tree, BranchNode parent) {
		treeBean = tree;
		// set folder icons, if needed.
		setBranchContractedIcon(makeIcon("f_c.gif"));
		setBranchExpandedIcon(makeIcon("f_o.gif"));
		setLeafIcon("images/tree_document.gif");
		this.parent = parent;
	}

	@Override
	public boolean equals(Object other) {
		if ((this == other)) {
			return true;
		}
		if ((other == null)) {
			return false;
		}
		if (!(other instanceof Node)) {
			return false;
		}
		Node castOther = (Node) other;
		boolean equalIds = ((this.getNodeId() == castOther.getNodeId()) || (this.getNodeId() != null
				&& castOther.getNodeId() != null && this.getNodeId().equals(castOther.getNodeId())));
		boolean equalObjectTypes = ((this.getObjectTypeId() == castOther.getObjectTypeId())
				|| (this.getObjectTypeId() != null && castOther.getObjectTypeId() != null
						&& this.getObjectTypeId().equals(castOther.getObjectTypeId())));
		boolean equalParents = ((this.getParent() == castOther.getParent()) || (this.getParent() != null
				&& castOther.getParent() != null && this.getParent().equals(castOther.getParent())));
		return equalIds && equalObjectTypes && equalParents;
	}

	// PTS_PR:26956 : ZSE add hashCode used for bucketing in Hash implementations like HashMap, HashTable, HashSet etc.
	@Override
	public int hashCode() {
		int result = 17;
		result = 37 * result + (getNodeId() == null ? 0 : this.getNodeId().hashCode());
		result = 37 * result + (getObjectTypeId() == null ? 0 : this.getObjectTypeId().hashCode());
		result = 37 * result + (getParent() == null ? 0 : this.getParent().hashCode());
		return result;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public void setPopupPageName(String popupPageName) {
		this.popupPageName = popupPageName;
	}

	public String getPopupPageName() {
		return popupPageName;
	}

	/**
	 * return the label of node regarding fromBundle boolean field, if true get name from ressource bundle using
	 * getClass().getName()
	 * 
	 * @return
	 */
	public String getMenuDisplayText() {
		if (fromBundle) {
			String value;
			try {
				value = BaseBean.getResourceBundle().getString(getClass().getName());
			} catch (MissingResourceException e) {
				return getClass().getName();
			}
			return value;
		} else {
			return this.menuDisplayText;
		}
	}

	/**
	 * return the title of node regarding fromBundle boolean field, if true get name from ressource bundle using
	 * getClass().getName()
	 * 
	 * @return
	 */
	public String getMenuContentTitle() {
		if (fromBundle) {
			String value;
			try {
				value = BaseBean.getResourceBundle().getString(getClass().getName());
			} catch (MissingResourceException e) {
				return getClass().getName();
			}
			return value;
		} else {
			return this.menuContentTitle;
		}
	}

	public boolean isPageContent() {
		return pageContent;
	}

	public void setPageContent(boolean pageContent) {
		this.pageContent = pageContent;
	}

	public boolean isDisplayContextMenu() {
		return displayContextMenu;
	}

	public boolean isFromBundle() {
		return fromBundle;
	}

	public void setFromBundle(boolean fromBundle) {
		this.fromBundle = fromBundle;
	}

	public void setMenuDisplayText(String menuDisplayText) {
		this.menuDisplayText = menuDisplayText;
	}

	public void setMenuContentTitle(String menuContentTitle) {
		this.menuContentTitle = menuContentTitle;
	}

	public Long getNodeId() {
		return nodeId;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	public ContextMenuInterface getContextMenu() {
		return contextMenu;
	}

	public void setContextMenu(ContextMenuInterface contextMenu) {
		if (contextMenu == null) {
			this.contextMenu = null;
			this.displayContextMenu = Boolean.FALSE;
		} else {
			this.contextMenu = contextMenu;
			this.displayContextMenu = Boolean.TRUE;
		}
	}

	public boolean isDisplayOwner() {
		return displayOwner;
	}

	public void setOwnerImg(String owner) {
		if (owner == null || "".equals(owner.trim())) {
			this.ownerImg = null;
			this.displayOwner = Boolean.FALSE;
		} else {
			this.ownerImg = owner;
			this.displayOwner = Boolean.TRUE;
		}
	}

	public String getOwnerImg() {
		return ownerImg;
	}

	public boolean isDisplayUsers() {
		return displayUsers;
	}

	public void setUsers(List<String> users) {
		if (users == null || users.isEmpty()) {
			this.users = null;
			this.displayUsers = Boolean.FALSE;
		} else {
			this.users = users;
			this.displayUsers = Boolean.TRUE;
		}
	}

	public List<String> getUsers() {
		return users;
	}

	public String getLibrary() {
		return library;
	}

	public void setLibrary(String library) {
		if (library == null || "".equals(library.trim())) {
			this.library = null;
			this.displayLibrary = Boolean.FALSE;
		} else {
			this.library = library;
			this.displayLibrary = Boolean.TRUE;
		}
	}

	public boolean isDisplayLibrary() {
		return displayLibrary;
	}

	@Override
	public BranchNode getParent() {
		return parent;
	}

	public void setParent(BranchNode parent) {
		this.parent = parent;
	}

	public BaseDTO getDto() {
		return dto;
	}

	public void setDto(BaseDTO dto) {
		this.dto = dto;
	}

	/**
	 * event called when this node is clicked
	 * 
	 * @param event
	 */
	public abstract void nodeClicked(ActionEvent event);

	/**
	 * return the message to show in the action bar when this node is edited
	 * 
	 * @return
	 */
	protected abstract String getEditMessage();

	/**
	 * event used to remove node this method show the confirmation popup and cache node to be deleted
	 * 
	 * @param event
	 */
	public void deleteClicked(ActionEvent event) {
		if (treeBean.getNeedSaveMessageConfirmation()) {
			return;
		}
		log.info("Delete clicked for " + this.getMenuDisplayText());
		treeBean.setNodeToBeDeleted(this);
		treeBean.openConfirmDeletePopup(getDeleteMessage());
	}

	/**
	 * event called when user accept to delete the node
	 * 
	 * @param event
	 */
	public void acceptDeleteClicked(ActionEvent event) {
		log.info("Delete accepted for " + this.getMenuDisplayText());
		String msg = specificDeleteClicked(event);
		if (!"".equals(msg)) {
			log.error("Can not delete : " + msg);
			treeBean.openErrorPopup(msg);
			return;
		} else {
			getParent().setExpanded(Boolean.FALSE);
			getParent().nodeClicked(event);
		}
		treeBean.setDownActionDefintion(getDeleteSuccessMessage());
	}

	/**
	 * return success message when delete is done successfully
	 * 
	 * @return
	 */
	protected abstract String getDeleteSuccessMessage();

	/**
	 * this method should be overided by each node that can be deleted
	 * 
	 * @param event
	 * @return
	 */
	public String specificDeleteClicked(ActionEvent event) {
		throw new RuntimeException("Implement me for object delete");
	}

	/**
	 * return confirmation message, it should be implemented by each node that can be deleted
	 * 
	 * @return
	 */
	public String getDeleteMessage() {
		throw new RuntimeException("Implement me for object delete");
	}

	/**
	 * cut a node in memory in order to paste it in an other folder
	 * 
	 * @param event
	 */
	public void cutClicked(ActionEvent event) {
		if (treeBean.getNeedSaveMessageConfirmation()) {
			return;
		}
		log.info("Cut clicked for " + this.getMenuDisplayText());
		treeBean.setCuttedNode(this);
		treeBean.setCopiedNode(null);
		treeBean.setCuttedMarkedNodes(null);
		treeBean.setCopiedMarkedNodes(null);
		treeBean.setSourceCopyNode(this.getParent());
		treeBean.setDownActionDefintion(getCutMessage());
	}

	/**
	 * cut message shown in bar action
	 * 
	 * @return
	 */
	protected abstract String getCutMessage();

	/**
	 * return the message shown in action bar when node marked cut function is called
	 * 
	 * @param nodeMarkedCount
	 * @return
	 */
	protected String getMarkedCutMessage(String nodeMarkedCount) {
		throw new RuntimeException("Implement me for marked object cut");
	}

	/**
	 * return true when current logged user have rights to access this node
	 * 
	 * @return
	 */
	public boolean isAccessAllowed() {
		List<Long> allRights = new ArrayList<Long>();
		allRights.addAll(getRightIds());
		allRights.addAll(getCRUDRightIds());
		if (!allRights.isEmpty()) {
			boolean a = false;
			for (Long rightId : allRights) {
				a = treeBean.getUserFromSession().haveRightToDo(rightId);
				if (a) {
					log.debug("Current logged user have the right to access this Node " + getClass().getName());
					return a;
				}
			}
			return a;
		}
		log.debug("Current logged user have the right to access this Node " + getClass().getName());
		return true;
	}

	/**
	 * return true when current logged user have rights to change this node
	 * 
	 * @return
	 */
	public boolean isCRUDAllowed() {
		if (!getCRUDRightIds().isEmpty()) {
			boolean a = false;
			for (Long rightId : getCRUDRightIds()) {
				a = treeBean.getUserFromSession().haveRightToDo(rightId);
				if (a) {
					log.debug("Current logged user have the right to access this Node " + getClass().getName());
					return a;
				}
			}
			return a;
		}
		log.debug("Current logged user have the right to access this Node " + getClass().getName());
		return true;
	}

	/**
	 * list of rights those can access to this node
	 * 
	 * @return
	 */
	public List<Long> getRightIds() {
		return new ArrayList<Long>();
	}

	/**
	 * list of rights those can change to this node
	 * 
	 * @return
	 */
	public List<Long> getCRUDRightIds() {
		return getRightIds();
	}

	/**
	 * return object type id of current node
	 * 
	 * @return
	 */
	public abstract Long getObjectTypeId();

	/**
	 * Method to be implemented in folder node to check if user can create,move,rename a folder or no
	 * 
	 * @return boolean user can delete,move,rename folder or no
	 */
	public boolean getHasUpdateFolderRight() {
		return true;
	}

	private boolean searchNode;

	public boolean getSearchNode() {
		return searchNode;
	}

	public void setSearchNode(boolean searchNode) {
		this.searchNode = searchNode;
	}

	public void copyClicked(ActionEvent event) {
		if (treeBean.getNeedSaveMessageConfirmation()) {
			return;
		}

		if (!getAddNewObjectAllowed()) {
			return;
		}
		log.info("Copy Object clicked for " + this.getMenuDisplayText());

		treeBean.setSourceCopyNode(this);
		treeBean.setCuttedNode(null);
		treeBean.setCuttedMarkedNodes(null);
		treeBean.setCopiedMarkedNodes(null);
		Node node = getParent().getNewObjectNodeInstance();
		node = specificCopyClicked(node);
		node.getDto().getPageChanges().add("copy_object");

		addCopyNode(node, event);

		treeBean.setDownActionDefintion(getCopyMessage());
	}

	public void addCopyNode(Node node, ActionEvent event) {
		treeBean.setSelectedNodeObject(node);
	}

	/**
	 * this method contains special processing to copy this node in another one
	 * 
	 * @param node
	 * @return
	 */
	public Node specificCopyClicked(Node node) {
		throw new RuntimeException("Implement me for object copy");
	}

	/**
	 * Has Something To Check for new Object before add new object
	 * 
	 * @return
	 */
	public boolean getAddNewObjectAllowed() {
		return Boolean.TRUE;
	}

	/**
	 * return the message shown in action bar when copy function is called
	 * 
	 * @return
	 */
	protected abstract String getCopyMessage();

	/**
	 * return the message shown in action bar when node marked copy function is called
	 * 
	 * @param nodeMarkedCount
	 * @return
	 */
	protected String getMarkedCopyMessage(String nodeMarkedCount) {
		throw new RuntimeException("Implement me for object delete");
	}

	/**
	 * return the message shown in action bar when node marked copy function is called
	 * 
	 * @param nodeMarkedCount
	 * @return
	 */
	protected String getChangeStatusMessage(String changedCount, String toStatus) {
		throw new RuntimeException("Implement me for object ChangeStatusMessage");
	}

	public static String makeIcon(String image) {
		return "images/" + image;
	}

	public static String makeContextIcon(String image) {
		return "images/context_menu/" + image;
	}

	public void setInactif(boolean inactif) {
		this.inactif = inactif;
	}

	public boolean isInactif() {
		return inactif;
	}

	public boolean isActiveFilter() {
		return activeFilter;
	}

	public void setActiveFilter(boolean activeFilter) {
		this.activeFilter = activeFilter;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 * sort a node under a folder by up/down/top/bottom
	 * 
	 * @param event
	 */
	public void sortClicked(ActionEvent event) {
		throw new RuntimeException("Implement me for object sort");
	}

	public void markClicked(ActionEvent event) {
		String mark = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get("mark_parameter");
		if ("ALL".equals(mark)) {
			List<Node> childsToSelect = this.getParent().childs;
			log.info("Nodes to be selected count " + CollectionUtils.size(childsToSelect) + " in folder "
					+ this.getParent().getMenuDisplayText());
			for (Node n : childsToSelect) {
				if (n.getContextMenu() != null && n.getContextMenu().getSelectActive()) {
					if (n.isVisible()) {
						markOneNode(n);
					}
				}
			}
		} else {
			markOneNode(this);
		}
	}

	protected void markOneNode(Node n) {
		if (treeBean.getMarkedNodes().get(getObjectTypeId()) != null) {
			treeBean.getMarkedNodes().get(getObjectTypeId()).add(n);
		} else {
			treeBean.getMarkedNodes().clear();
			Set<Node> marked = new HashSet<Node>();
			marked.add(n);
			treeBean.getMarkedNodes().put(getObjectTypeId(), marked);
		}
	}

	public void unmarkClicked(ActionEvent event) {
		String mark = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get("mark_parameter");
		if ("ALL".equals(mark)) {
			treeBean.getMarkedNodes().clear();
		} else {
			treeBean.getMarkedNodes().get(getObjectTypeId()).remove(this);
		}
	}

	public boolean isMarkedNode() {
		return (getParent() != null) && (treeBean.getMarkedNodes().get(getObjectTypeId()) != null)
				&& (treeBean.getMarkedNodes().get(getObjectTypeId()).contains(this));
	}

	@Deprecated
	List<Node> getSelectableChilds(List<Node> childsToSelect) {
		List<Node> selectableChild = new ArrayList<Node>();
		for (Node n : childsToSelect) {
			if (n.getContextMenu() != null && n.getContextMenu().getSelectActive()) {
				if (n.isVisible()) {
					selectableChild.add(n);
				}
			}
		}
		return selectableChild;
	}

	@Deprecated
	public boolean isAllMarkedNode() {
		return (getParent() != null) && (treeBean.getMarkedNodes().get(getObjectTypeId()) != null) && (treeBean
				.getMarkedNodes().get(getObjectTypeId()).containsAll(getSelectableChilds(this.getParent().childs)));
	}

	public boolean isMoreMarkedNode() {
		return isMarkedNode() && (treeBean.getMarkedNodes().get(getObjectTypeId()).size() > 1);
	}

	/**
	 * cut a node in memory in order to paste it in an other folder
	 * 
	 * @param event
	 */
	public void cutMarkedClicked(ActionEvent event) {
		if (treeBean.getNeedSaveMessageConfirmation()) {
			return;
		}
		Set<Node> toBeCutNodes = treeBean.getMarkedNodes().get(getObjectTypeId());
		treeBean.setCuttedMarkedNodes(toBeCutNodes);
		treeBean.setCopiedMarkedNodes(null);
		treeBean.setSourceCopyNode(null);
		treeBean.setCopiedNode(null);
		treeBean.setCuttedNode(null);
		log.info("Cut " + CollectionUtils.size(toBeCutNodes) + " select clicked from " + this.getMenuDisplayText());
		if (!CollectionUtils.isEmpty(toBeCutNodes) && toBeCutNodes.size() > 1) {
			treeBean.setDownActionDefintion(getMarkedCutMessage(toBeCutNodes.size() + ""));
		} else {
			treeBean.setDownActionDefintion(getCutMessage());
		}
	}

	public void copyMarkedClicked(ActionEvent event) {
		if (treeBean.getNeedSaveMessageConfirmation()) {
			return;
		}

		if (!getAddNewObjectAllowed()) {
			return;
		}
		Set<Node> toBeCopiedNodes = treeBean.getMarkedNodes().get(getObjectTypeId());
		treeBean.setCuttedNode(null);
		treeBean.setCopiedNode(null);
		treeBean.setSourceCopyNode(null);
		treeBean.setCuttedMarkedNodes(null);
		treeBean.setCopiedMarkedNodes(null);
		log.info("Copy " + CollectionUtils.size(toBeCopiedNodes) + " select clicked from " + this.getMenuDisplayText());
		if (!CollectionUtils.isEmpty(toBeCopiedNodes)) {
			for (Node n : toBeCopiedNodes) {
				Node node = getParent().getNewObjectNodeInstance();
				node = n.specificCopyClicked(node);
				addCopyMarkedNode(node, event);
			}
		}

		if (!CollectionUtils.isEmpty(toBeCopiedNodes) && toBeCopiedNodes.size() > 1) {
			treeBean.setDownActionDefintion(getMarkedCopyMessage(toBeCopiedNodes.size() + ""));
		} else {
			treeBean.setDownActionDefintion(getCopyMessage());
		}
	}

	public void addCopyMarkedNode(Node node, ActionEvent event) {
		if (CollectionUtils.isEmpty(treeBean.getCopiedMarkedNodes())) {
			treeBean.setCopiedMarkedNodes(new ArrayList<Node>());
		}
		treeBean.getCopiedMarkedNodes().add(node);
	}

	/**
	 * change status of marked object
	 * 
	 * @param event
	 */
	public void changeStatusClicked(ActionEvent event) {
		String toStatus = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get("to_status_parameter");
		Long toStatusId = null;
		if ("TO_BE_APPROVED".equals(toStatus)) {
			toStatusId = EnumStatusManager.Status.TO_BE_APPROVED.value();
		} else if ("IN_PROGRESS".equals(toStatus)) {
			toStatusId = EnumStatusManager.Status.IN_PROGRESS.value();
		}
		Set<Node> toBeChangedNodes = treeBean.getMarkedNodes().get(getObjectTypeId());
		log.info("Change status of " + CollectionUtils.size(toBeChangedNodes) + " selected to" + toStatus
				+ " clicked from " + this.getMenuDisplayText());
		int toBeChangedSize = 0;

		if (!CollectionUtils.isEmpty(toBeChangedNodes)) {
			toBeChangedSize = toBeChangedNodes.size();
			StringBuilder error = new StringBuilder();
			for (Node n : toBeChangedNodes) {
				String saveError = specificGoToStatus(event, n, toStatusId);
				if (saveError != null) {
					if (!"".equals(saveError.trim())) {
						error.append(saveError);
						error.append("<br/>");
					}
					toBeChangedSize--;
				}
			}
			// PTS problem 26957 : done to refresh element and baustein after change status
			Node actualNode = treeBean.getSelectedNodeObject();
			if (actualNode != null && actualNode.getParent() != null) {
				if (toBeChangedNodes.contains(actualNode.getParent())) {
					actualNode.getParent().nodeClicked(event);
				} else if (actualNode.getParent() != null
						&& toBeChangedNodes.contains(actualNode.getParent().getParent())) {
					// refresh baustein when stand is in view mode
					actualNode.getParent().getParent().nodeClicked(event);
				}
			}
			treeBean.setDownActionDefintion(getChangeStatusMessage(toBeChangedSize + "",
					treeBean.getMessage("status.designationbyId." + toStatusId)));

			// ZS: PTS_Problem-34861 : Cancel multiple selection after performing the action
			treeBean.getMarkedNodes().get(getObjectTypeId()).clear();

			if (error.length() > 0) {
				treeBean.setErrorMessage(error.toString());
				treeBean.setShowError(Boolean.TRUE);
				return;
			}
		}
	}

	protected String specificGoToStatus(ActionEvent event, Node node, Long toStatusId) {
		throw new RuntimeException("Implement me for object change status");
	}

	public void rightMouseClicked(ActionEvent event) {
		treeBean.setRightClickedNodeObject(this);
	}

	public boolean isResetRightClickedNode() {
		if (treeBean.getRightClickedNodeObject() != null) {
			treeBean.setRightClickedNodeObject(null);
			return true;
		}
		return false;
	}

	@Override
	public void removeFromParent() {
		if (parent != null) {
			parent.remove(this);
		}
	}

	@Override
	public void setParent(MutableTreeNode mutableTreeNode) {
		parent = (BranchNode) mutableTreeNode;
	}

	protected boolean expanded;

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean isExpanded) {
		this.expanded = isExpanded;
		NodeState ns = treeBean.getStateMap().get(this);
		if (ns == null) {
			ns = new NodeState();
			treeBean.getStateMap().put(this, ns);
		}
		ns.setExpanded(this.expanded);
	}

	/**
	 * <p>
	 * Return the appropriate icon based on this node's leaf attribute or expanded/collapsed state.
	 * </p>
	 * <p>
	 * By default the leaf attribute is false.
	 * </p>
	 *
	 * @return String application-relative path to the image file
	 */
	public String getIcon() {
		// leaf icon is rendered based on leaf attribute
		if (this.isLeaf()) {
			if (leafIcon != null) {
				return leafIcon;
			}
		} else if (isExpanded()) {
			if (branchExpandedIcon != null) {
				return branchExpandedIcon;
			}
		} else {
			if (branchContractedIcon != null) {
				return branchContractedIcon;
			}
		}
		return icon;
	}
}

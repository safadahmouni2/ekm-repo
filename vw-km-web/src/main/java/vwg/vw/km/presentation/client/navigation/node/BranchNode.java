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
import java.util.Enumeration;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import org.apache.commons.collections4.IteratorUtils;
import org.springframework.util.CollectionUtils;

import vwg.vw.km.application.implementation.RoleManager;
import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;
import vwg.vw.km.integration.persistence.model.BrandModel;
import vwg.vw.km.presentation.client.navigation.NavigationBean;

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
 * @version $Revision: 1.21 $ $Date: 2016/11/08 13:19:58 $
 */
public abstract class BranchNode extends Node {

	protected List<Node> childs;

	// number of object childs under this Node
	protected int objectChildsCount = 0;

	// index of last parsed object Node under this node
	protected int indexOfLastObject = 0;

	private BrandModel owner;

	// logger
	private final Log log = LogManager.get().getLog(BranchNode.class);

	public BranchNode(NavigationBean tree, BranchNode parent, Long folderId) {
		super(tree, parent);
		setNodeId(folderId);
	}

	// List of method used for adding new elements
	/**
	 * event called when new folder link clicked
	 * 
	 * @param evt
	 */
	public void addFolderClicked(ActionEvent evt) {

	}

	/**
	 * new folder name used for new created folder
	 * 
	 * @return
	 */
	public abstract String getNewFolderName();

	/**
	 * return the message to show in status bar when paste function is called
	 * 
	 * @return
	 */
	protected abstract String getPasteMessage();

	/**
	 * expandClicked
	 * 
	 * @param event
	 */
	public void expandClicked(ActionEvent event) {
		objectChildsCount = 0;
		indexOfLastObject = 0;
		log.info("Expand clicked and load childs of " + getMenuDisplayText());
		childs = getChilds();
	}

	/**
	 * expandClicked
	 * 
	 * @param event
	 */
	public void expandClicked(AjaxBehaviorEvent event) {
		objectChildsCount = 0;
		indexOfLastObject = 0;
		log.info("Expand clicked and load childs of " + getMenuDisplayText());
		if (childs == null) {
			childs = getChilds();
		}
	}

	/**
	 * Adding node to his parent child list
	 * 
	 * @param child
	 */
	public void addChild(Node child) {
		if (childs != null && !childs.contains(child)) {
			childs.add(child);
		}
	}

	public void editClicked(ActionEvent event) {
		nodeClicked(event);
	}

	/**
	 * collapseClicked
	 * 
	 * @param event
	 */
	public void collapseClicked(AjaxBehaviorEvent event) {
	}

	/**
	 * return the message to show in status bar when list of childs of current node are shown in right panel
	 * 
	 * @return
	 */
	protected abstract String getListClickedMessage();

	/**
	 * this method return list of childs of current node
	 * 
	 * @return
	 */
	public List<Node> getChilds() {
		return new ArrayList<Node>();
	}

	/**
	 * fetch the respective node from list of childs by id and object type
	 * 
	 * @param id
	 * @param objectTypeId
	 * @return
	 */
	public Node getNodeById(Long id, Long objectTypeId) {
		if (id == null) {
			return null;
		}
		for (Node n : childs) {
			if (id.equals(n.getNodeId()) && objectTypeId.equals(n.getObjectTypeId())) {
				return n;
			}
		}
		return null;
	}

	/**
	 * event called when "new object" link clicked from context menu, it create a new object node and show its edit mask
	 * 
	 * @param evt
	 */
	public void addNewObjectClicked(ActionEvent evt) {
		if (treeBean.getNeedSaveMessageConfirmation()) {
			return;
		}

		if (!getAddNewObjectAllowed()) {
			return;
		}

		Node n = getNewObjectNodeInstance();
		expandClicked(evt);
		setExpanded(Boolean.TRUE);
		this.addChild(n);
		n.nodeClicked(evt);
		n.getDto().getPageChanges().add("new_object_added");
		treeBean.setDownActionDefintion(getCreateMessage());
	}

	/**
	 * this method return a new object node instance relative to this folder node
	 * 
	 * @return
	 */
	public Node getNewObjectNodeInstance() {
		throw new RuntimeException("Override me to create new instance of object in your current folder");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#getDeleteMessage()
	 */
	@Override
	public String getDeleteMessage() {
		return treeBean.getMessage("folder.deleteConfirm");
	}

	/**
	 * return message to show in status bar when new object is created
	 * 
	 * @return
	 */
	protected abstract String getCreateMessage();

	/**
	 * return the code of message returned to user when he create a folder with an existing name
	 * 
	 * @return
	 */
	public String getExistingFolderMessage() {
		return treeBean.getMessage("folder.exist.sameName");
	}

	/**
	 * return the code of message returned to user when he create an object with an existing name
	 * 
	 * @return
	 */
	public String getExistingObjectMessage() {
		throw new RuntimeException("Implement me for object delete");
	}

	/**
	 * return true when there is something to paste in this folder node
	 * 
	 * @return
	 */
	public Boolean getHasSomethingToPaste() {
		if (treeBean.getCuttedNode() != null
				&& treeBean.getCuttedNode().getObjectTypeId().equals(getObjectContentId())) {
			return true;
		}
		if (treeBean.getCuttedNode() instanceof BranchNode) {
			BranchNode folder = (BranchNode) treeBean.getCuttedNode();
			if (folder.getObjectContentId().equals(getObjectContentId())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * return true when there is something multi selected to paste in this folder node
	 * 
	 * @return
	 */
	public Boolean getHasSomethingMarkedToPaste() {
		if (!CollectionUtils.isEmpty(treeBean.getCuttedMarkedNodes())) {
			for (Node node : treeBean.getCuttedMarkedNodes()) {
				if (!node.getObjectTypeId().equals(getObjectContentId())) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public Boolean getHasSomethingToCopy() {
		if (treeBean.getCopiedNode() != null
				&& treeBean.getCopiedNode().getObjectTypeId().equals(getObjectContentId())) {
			return true;
		}
		if (treeBean.getCopiedNode() instanceof BranchNode) {
			BranchNode folder = (BranchNode) treeBean.getCopiedNode();
			if (folder.getObjectContentId().equals(getObjectContentId())) {
				return true;
			}
		}
		return false;
	}

	public Boolean getHasSomethingMarkedToCopy() {
		if (!CollectionUtils.isEmpty(treeBean.getCopiedMarkedNodes())) {
			for (Node node : treeBean.getCopiedMarkedNodes()) {
				if (!node.getObjectTypeId().equals(getObjectContentId())) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * return type of objects that this folders will contains
	 * 
	 * @return
	 */
	public abstract Long getObjectContentId();

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#getHasUpdateFolderRight()
	 */
	@Override
	public boolean getHasUpdateFolderRight() {
		return treeBean.getUserFromSession().haveRightToDo(RoleManager.Right.UPDATE_FOLDER.value());
	}

	public BrandModel getOwner() {
		return owner;
	}

	public void setOwner(BrandModel owner) {
		this.owner = owner;
	}

	@Override
	public void addCopyNode(Node node, ActionEvent event) {
		treeBean.setCopiedNode(node);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {
		if ((this == other)) {
			return true;
		}
		if ((other == null)) {
			return false;
		}
		if (!(other instanceof BranchNode)) {
			return false;
		}
		BranchNode castOther = (BranchNode) other;

		boolean equalIds = ((this.getNodeId() == castOther.getNodeId()) || (this.getNodeId() != null
				&& castOther.getNodeId() != null && this.getNodeId().equals(castOther.getNodeId())));
		boolean equalObjectTypes = ((this.getObjectTypeId() == castOther.getObjectTypeId())
				|| (this.getObjectTypeId() != null && castOther.getObjectTypeId() != null
						&& this.getObjectTypeId().equals(castOther.getObjectTypeId())));
		boolean equalObjectContents = ((this.getObjectContentId() == castOther.getObjectContentId())
				|| (this.getObjectContentId() != null && castOther.getObjectContentId() != null
						&& this.getObjectContentId().equals(castOther.getObjectContentId())));
		boolean equalParents = ((this.getParent() == castOther.getParent()) || (this.getParent() != null
				&& castOther.getParent() != null && this.getParent().equals(castOther.getParent())));
		return equalIds && equalObjectTypes && equalObjectContents && equalParents;
	}

	@Override
	public Enumeration<Node> children() {
		if (childs == null) {
			childs = getChilds();
		}
		if (childs == null) {
			return IteratorUtils.asEnumeration(IteratorUtils.emptyIterator());
		}
		List<Node> enumm = new ArrayList<Node>();
		for (Node n : childs) {
			if (n.isVisible()) {
				enumm.add(n);
			}
		}
		return IteratorUtils.asEnumeration(enumm.iterator());
	}

	@Override
	public int getIndex(TreeNode treeNode) {
		if (childs == null) {
			childs = getChilds();
		}
		return childs.indexOf(treeNode);
	}

	@Override
	public void insert(MutableTreeNode node, int i) {
		node.setParent(this);
		childs.add(i, (Node) node);
	}

	@Override
	public void remove(int i) {
		childs.remove(i);
	}

	@Override
	public void remove(MutableTreeNode mutableTreeNode) {
		childs.remove(mutableTreeNode);
	}

	@Override
	public void setUserObject(Object o) {
		// Not required for any ace:tree functionality
		throw new UnsupportedOperationException();
	}

	@Override
	public TreeNode getChildAt(int childIndex) {
		if (childs == null) {
			childs = getChilds();
		}
		return childs.get(childIndex);
	}

}

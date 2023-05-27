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

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import org.apache.commons.collections4.IteratorUtils;

import vwg.vw.km.application.implementation.EnumObjectTypeManager;

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
 * @version $Revision: 1.11 $ $Date: 2016/11/14 19:20:34 $
 */
public abstract class ElementFolderNode extends ElementNode {

	public ElementFolderNode(NavigationElementBean tree, ElementFolderNode parent, Long folderId) {
		super(tree, parent);
		setNodeId(folderId);
	}

	private List<ElementNode> childs;

	/**
	 * Expand an element node in the tree and load his own child
	 * 
	 * @param event
	 */
	public void expandClicked(ActionEvent event) {
		childs = getChilds();
	}

	/**
	 * Expand an element node in the tree and load his own child
	 * 
	 * @param event
	 */
	public void expandClicked(AjaxBehaviorEvent event) {
		if (childs == null) {
			childs = getChilds();
		}
	}

	/**
	 * Adding node to his parent child list
	 * 
	 * @param child
	 */
	public void addChild(ElementNode child) {
		if (childs != null && !childs.contains(child)) {
			childs.add(child);
		}
	}

	/**
	 * Collapse an element node in the tree
	 * 
	 * @param event
	 */
	public void collapseClicked(ActionEvent event) {
	}

	/**
	 * Collapse an element node in the tree
	 * 
	 * @param event
	 */
	public void collapseClicked(AjaxBehaviorEvent event) {
	}

	@Override
	public void nodeClicked(ActionEvent event) {
		if (!this.expanded) {
			expandClicked(event);
			this.setExpanded(Boolean.TRUE);
		} else {
			this.setExpanded(Boolean.FALSE);
		}
	}

	/**
	 * Return a new list ElementNode objects
	 * 
	 * @return list
	 */
	protected List<ElementNode> getChilds() {
		return new ArrayList<ElementNode>();
	}

	/**
	 * Return an element node by id
	 * 
	 * @param id
	 * @param objectTypeId
	 * @return
	 */
	public ElementNode getNodeById(Long id, Long objectTypeId) {
		if (id == null) {
			return null;
		}
		for (ElementNode n : childs) {
			if (id.equals(n.getNodeId()) && objectTypeId.equals(n.getObjectTypeId())) {
				return n;
			}
		}
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.costcomponent.ElementNode#getObjectTypeId()
	 */
	@Override
	public final Long getObjectTypeId() {
		return EnumObjectTypeManager.FOLDER_ID;
	}

	/**
	 * Return the enumeration value of ELEMENT_ID
	 * 
	 * @return 1L
	 */
	public Long getObjectContentId() {
		return EnumObjectTypeManager.ELEMENT_ID;
	}

	@Override
	public Enumeration<ElementNode> children() {
		if (childs == null) {
			childs = getChilds();
		}
		if (childs == null) {
			return IteratorUtils.asEnumeration(IteratorUtils.emptyIterator());
		}
		List<ElementNode> enumm = new ArrayList<ElementNode>();
		for (ElementNode n : childs) {
			if (!n.isAssigned()) {
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
		childs.add(i, (ElementNode) node);
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
		return childs.get(childIndex);
	}

}

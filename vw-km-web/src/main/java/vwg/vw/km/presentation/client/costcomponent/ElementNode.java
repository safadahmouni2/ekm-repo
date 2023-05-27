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

import java.util.MissingResourceException;

import javax.faces.event.ActionEvent;
import javax.swing.tree.TreeNode;

import org.icefaces.ace.model.tree.NodeState;

import vwg.vw.km.presentation.client.base.BaseBean;
import vwg.vw.km.presentation.client.navigation.node.ContextMenuEnum;
import vwg.vw.km.presentation.client.navigation.node.IceUserObject;

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
 * @version $Revision: 1.11 $ $Date: 2016/11/02 12:42:12 $
 */
public abstract class ElementNode extends IceUserObject {

	// This the main class that build the tree and using this class we can
	// access for service to load dynamic node from
	// database
	protected NavigationElementBean treeBean;

	// get text and title from bundle or no
	private boolean fromBundle = Boolean.TRUE;

	// text to be displayed in navigation link
	private String menuDisplayText;

	// title information to be displayed
	private String menuContentTitle;

	// node id
	private Long nodeId;

	// contextMenu
	private ContextMenuEnum contextMenu;

	// display or no the context menu for this given node
	private boolean displayContextMenu = Boolean.FALSE;

	private ElementFolderNode parent;

	private boolean selected = false;

	private boolean assigned = false;

	// display or no the owner of the node
	private boolean displayOwner = Boolean.FALSE;

	private String ownerImg;

	public ElementNode(NavigationElementBean tree, ElementFolderNode parent) {
		treeBean = tree;
		// set folder icons, if needed.
		setBranchContractedIcon("images/f_c.gif");
		setBranchExpandedIcon("images/f_o.gif");
		setLeafIcon("images/tree_document.gif");
		this.parent = parent;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {
		if ((this == other)) {
			return true;
		}
		if ((other == null)) {
			return false;
		}
		if (!(other instanceof ElementNode)) {
			return false;
		}
		ElementNode castOther = (ElementNode) other;

		return ((this.getNodeId() == castOther.getNodeId()) || (this.getNodeId() != null
				&& castOther.getNodeId() != null && this.getNodeId().equals(castOther.getNodeId())))
				&& ((this.getObjectTypeId() == castOther.getObjectTypeId())
						|| (this.getObjectTypeId() != null && castOther.getObjectTypeId() != null
								&& this.getObjectTypeId().equals(castOther.getObjectTypeId())));
	}

	/**
	 * Return the label of the element node
	 * 
	 * @return string value
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
	 * Return the menu content title of the element node
	 * 
	 * @return string value
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

	public void setContextMenu(ContextMenuEnum contextMenu) {
		if (contextMenu == null) {
			this.contextMenu = null;
			this.displayContextMenu = Boolean.FALSE;
		} else {
			this.contextMenu = contextMenu;
			this.displayContextMenu = Boolean.TRUE;
		}
	}

	/**
	 * @return the displayContextMenu
	 */
	public boolean isDisplayContextMenu() {
		return displayContextMenu;
	}

	/**
	 * @param displayContextMenu
	 *            the displayContextMenu to set
	 */
	public void setDisplayContextMenu(boolean displayContextMenu) {
		this.displayContextMenu = displayContextMenu;
	}

	/**
	 * @return the contextMenu
	 */
	public ContextMenuEnum getContextMenu() {
		return contextMenu;
	}

	/**
	 * Return the value of isFromBundle flag
	 * 
	 * @return true/false
	 */
	public boolean isFromBundle() {
		return fromBundle;
	}

	/**
	 * Setter of isFromBundle flag
	 * 
	 * @param fromBundle
	 */
	public void setFromBundle(boolean fromBundle) {
		this.fromBundle = fromBundle;
	}

	/**
	 * Setter of menu display text value
	 * 
	 * @param menuDisplayText
	 */
	public void setMenuDisplayText(String menuDisplayText) {
		this.menuDisplayText = menuDisplayText;
	}

	/**
	 * Setter of menu content title value
	 * 
	 * @param menuContentTitle
	 */
	public void setMenuContentTitle(String menuContentTitle) {
		this.menuContentTitle = menuContentTitle;
	}

	/**
	 * Getter of the node id
	 * 
	 * @return
	 */
	public Long getNodeId() {
		return nodeId;
	}

	/**
	 * Setter of the node id
	 * 
	 * @param nodeId
	 */
	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	/**
	 * Return the element node parent
	 * 
	 * @return folder node
	 */
	@Override
	public ElementFolderNode getParent() {
		return parent;
	}

	/**
	 * Setter of the element node parent
	 * 
	 * @param parent
	 */
	public void setParent(ElementFolderNode parent) {
		this.parent = parent;
	}

	/**
	 * Return if the element node is selected
	 * 
	 * @return true/false
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * Setter of the selected flag
	 * 
	 * @param selected
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * Return if the element node is assigned
	 * 
	 * @return true/ false
	 */
	public boolean isAssigned() {
		return assigned;
	}

	/**
	 * Setter of the assigned flag
	 * 
	 * @param assigned
	 *            boolean
	 */
	public void setAssigned(boolean assigned) {
		this.assigned = assigned;
	}

	/**
	 * 
	 * @param event
	 */
	public abstract void nodeClicked(ActionEvent event);

	/**
	 * 
	 * @return
	 */
	public abstract Long getObjectTypeId();

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

	@Override
	public void removeFromParent() {
		if (parent != null) {
			parent.remove(this);
		}
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

	@Override
	public TreeNode getChildAt(int childIndex) {
		return null;
	}
}

/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * "The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2006 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 *
 */

package vwg.vw.km.presentation.client.navigation.node;

import java.util.Enumeration;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

/**
 * IceUserObject - the Object that constitutes a DefaultMutableTreeNode's user-specified data.
 * <p/>
 * The TreeModel must contain a tree of DefaultMutableTreeNode instances. Each DefaultMutableTreeNode instance
 * encapsultes an IceUserObject. The IceUserObject is the extension point for the application developer.
 * </p>
 * <p/>
 * By default all nodes are folders unless the leaf attribute is set true.
 * </p>
 * <p/>
 * If the IceUserObject does not provide sufficient state for representation of the tree's nodes, then the application
 * developer should extend the IceUserObject and add state as required to their extension. When creating an
 * IceUserObject, the DefaultMutableTreeNode wrapper must be provided to the constructor. Then the node's state can be
 * set to the attributes on the IceUserObject.
 * </p>
 */
public abstract class IceUserObject implements MutableTreeNode {

	// icon fields
	protected String leafIcon;

	protected String branchExpandedIcon;

	protected String branchContractedIcon;

	protected String icon;

	// leaf field
	protected boolean leaf;

	/**
	 * <p>
	 * Set the value of the boolean leaf attribute. Setting the leaf attribute to true will force a tree node to be
	 * rendered as a leaf. By default the leaf attribute is false therefore all tree nodes will default to folders.
	 * </p>
	 * 
	 * @param leaf
	 */
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	/**
	 * <p>
	 * Return false if the tree node is a folder.By default all tree nodes are folders.
	 * </p>
	 * 
	 * @return the boolean value of the leaf attribute.
	 */
	@Override
	public boolean isLeaf() {
		return this.leaf;
	}

	/**
	 * @return null
	 */
	public String getFamily() {
		return null;
	}

	/**
	 * <p>
	 * Return the value of the <code>leafIcon</code> property.
	 * </p>
	 * 
	 * @return leafIcon
	 */
	public String getLeafIcon() {
		return leafIcon;
	}

	/**
	 * <p>
	 * Set the value of the <code>leafIcon</code> property.
	 * </p>
	 * 
	 * @param leafIcon
	 */
	public void setLeafIcon(String leafIcon) {
		this.leafIcon = leafIcon;
	}

	/**
	 * <p>
	 * Return the value of the <code>branchContractedIcon</code> property.
	 * </p>
	 * 
	 * @return branchContractedIcon
	 */
	public String getBranchContractedIcon() {
		return branchContractedIcon;
	}

	/**
	 * <p>
	 * Set the value of the <code>branchContractedIcon</code> property.
	 * </p>
	 * 
	 * @param branchContractedIcon
	 */
	public void setBranchContractedIcon(String branchContractedIcon) {
		this.branchContractedIcon = branchContractedIcon;
	}

	/**
	 * <p>
	 * Return the value of the <code>branchExpandedIcon</code> property.
	 * </p>
	 * 
	 * @return branchExpandedIcon
	 */
	public String getBranchExpandedIcon() {
		return branchExpandedIcon;
	}

	/**
	 * <p>
	 * Set the value of the <code>branchExpandedIcon</code> property.
	 * </p>
	 * 
	 * @param branchExpandedIcon
	 */
	public void setBranchExpandedIcon(String branchExpandedIcon) {
		this.branchExpandedIcon = branchExpandedIcon;
	}

	@Override
	public void insert(MutableTreeNode child, int index) {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(int index) {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(MutableTreeNode node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeFromParent() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setParent(MutableTreeNode newParent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setUserObject(Object object) {
		// TODO Auto-generated method stub

	}

	@Override
	public Enumeration<?> children() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getAllowsChildren() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getChildCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getIndex(TreeNode node) {
		// TODO Auto-generated method stub
		return 0;
	}
}
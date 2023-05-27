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

import javax.faces.event.ActionEvent;
import javax.swing.tree.TreeNode;

import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;
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
 * @version $Revision: 1.11 $ $Date: 2016/10/27 14:58:28 $
 */
public abstract class LeafNode extends Node {

	private final Log log = LogManager.get().getLog(LeafNode.class);

	public LeafNode(NavigationBean tree, BranchNode parent) {
		super(tree, parent);
		setLeaf(Boolean.TRUE);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#nodeClicked(javax.faces.event.ActionEvent)
	 */
	@Override
	public void nodeClicked(ActionEvent event) {
		if (log.isInfoEnabled()) {
			log.info(this.getMenuDisplayText() + " node is clicked");
		}
		if (treeBean.getNeedSaveMessageConfirmation()) {
			treeBean.setGotoNode(this);
			return;
		}
		treeBean.setSelectedNodeObject(this);
		specificLeafNodeClicked();
		treeBean.setDownActionDefintion(getEditMessage());
	}

	/**
	 * this method contains special actions to do when this node is clicked
	 */
	public abstract void specificLeafNodeClicked();

	@Override
	public TreeNode getChildAt(int childIndex) {
		return null;
	}

}

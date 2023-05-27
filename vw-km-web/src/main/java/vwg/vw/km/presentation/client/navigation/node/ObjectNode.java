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

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.icefaces.util.JavaScriptRunner;

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
 * @version $Revision: 1.19 $ $Date: 2016/10/20 14:49:31 $
 */
public abstract class ObjectNode extends LeafNode {

	// logger
	private final Log log = LogManager.get().getLog(ObjectNode.class);

	public ObjectNode(NavigationBean tree, BranchNode parent, boolean isSearchNode) {
		super(tree, parent);
		setSearchNode(isSearchNode);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.LeafNode#specificLeafNodeClicked()
	 */
	@Override
	public void specificLeafNodeClicked() {
		specificNodeClicked();
		getDto().setSearchMode(getSearchNode());
		JavaScriptRunner.runScript(FacesContext.getCurrentInstance(), "scrollToSelectedNode();");
	}

	/**
	 * this method contains special actions to do when this node is clicked
	 */
	public abstract void specificNodeClicked();

	/**
	 * create a new node as copy of this node in the same folder
	 * 
	 * @param event
	 */
	@Override
	public void copyClicked(ActionEvent event) {
		if (treeBean.getNeedSaveMessageConfirmation()) {
			return;
		}
		log.info("Copy Object clicked for " + this.getMenuDisplayText());
		specificNodeClicked();
		Node node = getParent().getNewObjectNodeInstance();
		node = specificCopyClicked(node);
		node.getDto().getPageChanges().add("copy_object");
		getParent().addChild(node);
		treeBean.setSelectedNodeObject(node);
		treeBean.setDownActionDefintion(getCopyMessage());
	}

	/**
	 * this method contains special processing to copy this node in another one
	 * 
	 * @param node
	 * @return
	 */
	@Override
	public Node specificCopyClicked(Node node) {
		throw new RuntimeException("Implement me for object copy");
	}

	/**
	 * return the message shown in action bar when copy function is called
	 * 
	 * @return
	 */
	@Override
	protected abstract String getCopyMessage();

	protected void refresh() {
		// TODO: to fix calendar problem refresh is disabled
		// Problem Nr: 10544357 News verwalten : Date picker not working after saving
		// FacesContext context = FacesContext.getCurrentInstance();
		// Application application = context.getApplication();
		// ViewHandler viewHandler = application.getViewHandler();
		// UIViewRoot viewRoot = viewHandler.createView(context, context.getViewRoot().getViewId());
		// context.setViewRoot(viewRoot);
	}
}

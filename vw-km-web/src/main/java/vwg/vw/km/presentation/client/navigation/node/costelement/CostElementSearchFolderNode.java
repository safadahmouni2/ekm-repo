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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vwg.vw.km.application.implementation.EnumObjectTypeManager;
import vwg.vw.km.application.implementation.EnumStatusManager;
import vwg.vw.km.presentation.client.navigation.NavigationBean;
import vwg.vw.km.presentation.client.navigation.node.FolderNode;
import vwg.vw.km.presentation.client.navigation.node.Node;
import vwg.vw.km.presentation.client.navigation.node.StaticFolderNode;

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
 * @author Sebri Zouhaier changed by $Author: saidi $
 * @version $Revision: 1.20 $ $Date: 2013/12/19 15:25:49 $
 */
public class CostElementSearchFolderNode extends StaticFolderNode {

	CostElementInProcessNode costElementInProcessNode = new CostElementInProcessNode(treeBean, this);

	CostElementToBeApprovedNode costElementToBeApprovedNode = new CostElementToBeApprovedNode(treeBean, this);

	CostElementSearchResultNode costElementSearchResultNode = new CostElementSearchResultNode(treeBean, this);

	CostElementInactiveNode costElementInactiveNode = new CostElementInactiveNode(treeBean, this, -26L);

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.FolderNode#getChilds()
	 */
	@Override
	public List<Node> getChilds() {
		List<Node> childs = new ArrayList<Node>();
		childs.add(costElementInProcessNode);
		childs.add(costElementToBeApprovedNode);
		childs.add(costElementSearchResultNode);
		childs.add(costElementInactiveNode);
		return childs;
	}

	public CostElementSearchFolderNode(NavigationBean tree, FolderNode parent) {
		super(tree, parent, StaticNodeIdEnum.COST_ELEMENT_SEARCH_FOLDER_NODE.value());
		treeBean.getObjectTypeSearchResultNode().put(getObjectContentId(), costElementSearchResultNode);
		Map<Long, StaticFolderNode> statusNodes = new HashMap<Long, StaticFolderNode>();
		statusNodes.put(EnumStatusManager.Status.IN_PROGRESS.value(), costElementInProcessNode);
		statusNodes.put(EnumStatusManager.Status.CREATED.value(), costElementInProcessNode);
		statusNodes.put(EnumStatusManager.Status.TO_BE_APPROVED.value(), costElementToBeApprovedNode);
		treeBean.getObjectTypeStatusResultNodes().put(getObjectContentId(), statusNodes);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.FolderNode#getObjectContentId()
	 */
	public final Long getObjectContentId() {
		return EnumObjectTypeManager.ELEMENT_ID;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.FolderNode#getListClickedMessage()
	 */
	protected String getListClickedMessage() {
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.FolderNode#getPasteMessage()
	 */
	@Override
	protected String getPasteMessage() {
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#getCutMessage()
	 */
	@Override
	protected String getCutMessage() {
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#getDeleteSuccessMessage()
	 */
	@Override
	protected String getDeleteSuccessMessage() {
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#getEditMessage()
	 */
	@Override
	protected String getEditMessage() {
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.FolderNode#getCreateMessage()
	 */
	@Override
	protected String getCreateMessage() {
		// TODO Auto-generated method stub
		return null;
	}
}

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
import java.util.List;

import vwg.vw.km.application.implementation.EnumObjectTypeManager;
import vwg.vw.km.presentation.client.navigation.NavigationBean;
import vwg.vw.km.presentation.client.navigation.node.administration.AdministrationNode;
import vwg.vw.km.presentation.client.navigation.node.calculationsettings.CalculationSettingsNode;
import vwg.vw.km.presentation.client.navigation.node.changehistory.ChangeHistoryNode;
import vwg.vw.km.presentation.client.navigation.node.costcomponent.CostComponentAdministrationNode;
import vwg.vw.km.presentation.client.navigation.node.costelement.CostElementAdministrationNode;
import vwg.vw.km.presentation.client.navigation.node.working.WorkingNode;

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
 * @version $Revision: 1.31 $ $Date: 2016/10/27 14:58:28 $
 */
public class RootNode extends StaticFolderNode {

	AdministrationNode administrationNode = new AdministrationNode(treeBean, this);

	CalculationSettingsNode calculationSettingsNode = new CalculationSettingsNode(treeBean, this);

	WorkingNode workingNode = new WorkingNode(treeBean, this);

	CostElementAdministrationNode costElementAdministrationNode = new CostElementAdministrationNode(treeBean, this);

	CostComponentAdministrationNode costComponentAdministrationNode = new CostComponentAdministrationNode(treeBean,
			this);

	ChangeHistoryNode changeHistoryNode = new ChangeHistoryNode(treeBean, this);

	public CostComponentAdministrationNode getCostComponentAdministrationNode() {
		return costComponentAdministrationNode;
	}

	public AdministrationNode getAdministrationNode() {
		return administrationNode;
	}

	public RootNode(NavigationBean tree) {
		super(tree, null, -1L);
		setExpanded(Boolean.TRUE);
		setTemplateName("welcomePage.xhtml");
		setPageContent(true);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.FolderNode#getChilds()
	 */
	@Override
	public List<Node> getChilds() {
		List<Node> childs = new ArrayList<Node>();
		if (administrationNode.isAccessAllowed()) {
			childs.add(administrationNode);
		}
		if (calculationSettingsNode.isAccessAllowed()) {
			childs.add(calculationSettingsNode);
		}
		if (workingNode.isAccessAllowed()) {
			childs.add(workingNode);
		}
		if (costElementAdministrationNode.isAccessAllowed()) {
			childs.add(costElementAdministrationNode);
		}
		if (costComponentAdministrationNode.isAccessAllowed()) {
			childs.add(costComponentAdministrationNode);
		}

		if (changeHistoryNode.isAccessAllowed()) {
			childs.add(changeHistoryNode);
		}
		return childs;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.FolderNode#getObjectContentId()
	 */
	@Override
	public Long getObjectContentId() {
		return EnumObjectTypeManager.FOLDER_ID;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.FolderNode#getListClickedMessage()
	 */
	@Override
	protected String getListClickedMessage() {
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
	 * @see vwg.vw.km.presentation.client.navigation.node.FolderNode#getPasteMessage()
	 */
	@Override
	protected String getPasteMessage() {
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
		return null;
	}
}

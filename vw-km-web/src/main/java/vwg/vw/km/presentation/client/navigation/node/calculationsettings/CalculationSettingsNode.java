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
package vwg.vw.km.presentation.client.navigation.node.calculationsettings;

import java.util.ArrayList;
import java.util.List;

import vwg.vw.km.application.implementation.EnumObjectTypeManager;
import vwg.vw.km.application.implementation.RoleManager;
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
 * @author Sebri Zouhaier changed by $Author: zouhair $
 * @version $Revision: 1.19 $ $Date: 2013/12/12 10:05:41 $
 */
public class CalculationSettingsNode extends StaticFolderNode {

	HourlyRateCatalogAdministrationNode node = new HourlyRateCatalogAdministrationNode(treeBean, this);

	UserDefinitionSettingsNode userSettingsNode = new UserDefinitionSettingsNode(treeBean, this);

	public CalculationSettingsNode(NavigationBean tree, FolderNode parent) {
		super(tree, parent, 4L);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.FolderNode#getChilds()
	 */
	@Override
	public List<Node> getChilds() {
		List<Node> childs = new ArrayList<Node>();
		childs.add(node);
		childs.add(userSettingsNode);
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
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#getRightIds()
	 */
	@Override
	public List<Long> getRightIds() {
		List<Long> rights = new ArrayList<Long>();
		rights.add(RoleManager.Right.MANAGE_HOURLY_RATE.value());
		return rights;
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
		return null;
	}
}

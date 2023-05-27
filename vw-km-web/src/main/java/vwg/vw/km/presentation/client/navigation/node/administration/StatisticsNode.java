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

package vwg.vw.km.presentation.client.navigation.node.administration;
import vwg.vw.km.presentation.client.navigation.node.StaticNodeIdEnum;
import java.util.ArrayList;
import java.util.List;

import vwg.vw.km.application.implementation.EnumObjectTypeManager;
import vwg.vw.km.application.implementation.RoleManager;
import vwg.vw.km.presentation.client.navigation.NavigationBean;
import vwg.vw.km.presentation.client.navigation.node.FolderNode;
import vwg.vw.km.presentation.client.navigation.node.LeafNode;

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
 * @version $Revision: 1.5 $ $Date: 2014/01/21 08:36:36 $
 */
public class StatisticsNode extends LeafNode {

	public static String STATISTICS_MASK = "./administration/statistic/statistic.xhtml";

	public StatisticsNode(NavigationBean tree, FolderNode parent) {
		super(tree, parent);
		setNodeId(StaticNodeIdEnum.STATISTICS_NODE.value());
		setTemplateName(STATISTICS_MASK);
		setPopupPageName("./administration/statistic/popup.xhtml");
		setLeafIcon(makeIcon("f_c.gif"));

	}

	@Override
	public void specificLeafNodeClicked() {
	}

	/**
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#getRightIds()
	 */
	@Override
	public List<Long> getRightIds() {
		List<Long> rights = new ArrayList<Long>();
		rights.add(RoleManager.Right.VIEW_STATISTIC.value());
		return rights;
	}

	@Override
	public Long getObjectTypeId() {
		return EnumObjectTypeManager.FOLDER_ID;
	}

	@Override
	protected String getCopyMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getCutMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getDeleteSuccessMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getEditMessage() {
		// TODO Auto-generated method stub
		return null;
	}

}

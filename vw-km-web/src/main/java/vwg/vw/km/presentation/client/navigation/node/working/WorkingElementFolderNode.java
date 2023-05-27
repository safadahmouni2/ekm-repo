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
package vwg.vw.km.presentation.client.navigation.node.working;

import vwg.vw.km.presentation.client.navigation.NavigationBean;
import vwg.vw.km.presentation.client.navigation.node.ContextMenuEnum;
import vwg.vw.km.presentation.client.navigation.node.FolderNode;

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
 * @author Ziad Saïdi changed by $Author: mrad $
 * @version $Revision: 1.3 $ $Date: 2016/11/01 10:04:42 $
 */
public class WorkingElementFolderNode extends WorkingUserElementFolderNode {

	public WorkingElementFolderNode(NavigationBean tree, FolderNode parent, String name, Long folderId,
			Long workingUserId) {
		super(tree, parent, folderId);
		setTemplateName("./costElement/costElementList.xhtml");
		setMenuDisplayText(name);
		setMenuContentTitle(name);
		setFromBundle(Boolean.FALSE);
		setWorkingUserId(workingUserId);
		// only the logged user can add new element in his own folder
		if (workingUserId.equals(treeBean.getUserFromSession().getUserId())) {
			setContextMenu(ContextMenuEnum.CatalogFolderObjectContextMenu);
		}
		setAddObjectIcon("add-element");
	}

}

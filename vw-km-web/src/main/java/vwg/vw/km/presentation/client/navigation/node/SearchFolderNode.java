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

import vwg.vw.km.presentation.client.navigation.NavigationBean;

/**
 * <p>
 * Title: eKM Light
 * <p>
 * Description : Parent class for the search folders
 * </p>
 * <p>
 * Copyright: VW (c) 2014
 * </p>
 * .
 * 
 * @author Ziad Saidi changed by $Author: saidi $
 * @version $Revision: 1.1 $ $Date: 2014/03/05 17:09:43 $
 */
public class SearchFolderNode extends StaticFolderNode {

	public SearchFolderNode(NavigationBean tree, FolderNode parent, Long folderId) {
		super(tree, parent, folderId);
	}

	@Override
	protected String getPasteMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getCreateMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getListClickedMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getObjectContentId() {
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

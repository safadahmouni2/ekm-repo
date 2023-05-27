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

import java.util.ArrayList;
import java.util.List;

import vwg.vw.km.application.service.dto.CostElementDTO;
import vwg.vw.km.integration.persistence.model.ElementModel;
import vwg.vw.km.integration.persistence.model.FolderModel;

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
 * @version $Revision: 1.12 $ $Date: 2013/10/28 08:29:32 $
 */
public class CostElementFolderNode extends ElementFolderNode {

	public CostElementFolderNode(NavigationElementBean tree, ElementFolderNode parent, Long folderId, String text) {
		super(tree, parent, folderId);
		setFromBundle(Boolean.FALSE);
		setMenuDisplayText(text);
		setMenuContentTitle(text);
		setDisplayContextMenu(false);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.costcomponent.ElementFolderNode#getChilds()
	 */
	@Override
	public List<ElementNode> getChilds() {
		List<ElementNode> childs = new ArrayList<ElementNode>();
		List<FolderModel> folders = treeBean.getNavigationService().loadFolders(getNodeId(), getObjectContentId());
		for (FolderModel model : folders) {
			CostElementFolderNode n = new CostElementFolderNode(treeBean, this, model.getFolderId(),
					model.getDesignation());
			childs.add(n);
		}
		CostElementDTO costElementDTO = new CostElementDTO();
		costElementDTO.setFolderId(getNodeId());
		costElementDTO.setUserLogged(treeBean.getUserFromSession());
		costElementDTO = treeBean.getCostElementService().loadCanBeAttachedElements(costElementDTO);
		List<ElementModel> elements = costElementDTO.getElements();
		for (ElementModel model : elements) {
			// 28.10.2013: ZS: PTS_Requirement-22203: Detection of inactive elements when they are added to BS
			if (!model.getInactive()) {
				CostElementAttachNode n = new CostElementAttachNode(treeBean, this, model.getNumberAndDesignation(),
						model.getElementId(), Boolean.FALSE, model);
				childs.add(n);
			}
		}
		return childs;
	}

}

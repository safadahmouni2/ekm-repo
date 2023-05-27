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

package vwg.vw.km.presentation.client.costelement;

import javax.faces.event.ActionEvent;

import vwg.vw.km.application.implementation.EnumObjectTypeManager;
import vwg.vw.km.application.service.dto.CostElementDTO;
import vwg.vw.km.application.service.logic.CostElementService;
import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;
import vwg.vw.km.integration.persistence.model.FolderModel;
import vwg.vw.km.presentation.client.base.BaseBean;
import vwg.vw.km.presentation.client.navigation.node.FolderNode;
import vwg.vw.km.presentation.client.navigation.node.costelement.CostElementNode;
import vwg.vw.km.presentation.client.navigation.node.working.WorkingCostElementNode;

/**
 * <p>
 * Title: VW_KM
 * <p>
 * Description : Class description goes here
 * </p>
 * <p>
 * Copyright: cl (c) 2011
 * </p>
 * 
 * @author zouhairs changed by $Author: saidi $
 * @version $Revision: 1.37 $ $Date: 2013/12/09 15:26:31 $
 */
public class CostElementListBean extends BaseBean<CostElementDTO, CostElementService> {

	/**
	* 
	*/
	private static final long serialVersionUID = 937657276924078903L;
	private final Log log = LogManager.get().getLog(CostElementListBean.class);

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.base.BaseBean#specificSearchMessage()
	 */
	@Override
	public String specificSearchMessage() {
		return getMessage("action.costElement.search");
	}

	/**
	 * Go to Element in the tree from the search detail
	 * 
	 * @param event
	 */
	public void showElement(ActionEvent event) {
		String elementId = (String) getRequestParameterMap().get("element_id");
		String folderId = (String) getRequestParameterMap().get("folder_id");
		log.info("Show Element,folder = " + elementId + "," + folderId);
		FolderModel folder = navigationTree.getNavigationService().getFolder(Long.parseLong(folderId));
		// element is in a working folder
		if (elementId != null && !"".equals(elementId) && !"null".equals(elementId)) {
			if (folder.getCreatorRefId() != null) {
				Long userId = folder.getCreatorRefId();
				FolderNode workingNode = navigationTree.getWorkingNode();
				workingNode.setExpanded(true);
				workingNode.expandClicked(event);

				FolderNode userWorkingFolder = (FolderNode) workingNode.getNodeById(userId,
						EnumObjectTypeManager.FOLDER_ID);
				WorkingCostElementNode n = (WorkingCostElementNode) navigationTree.goToNode(userWorkingFolder,
						Long.parseLong(elementId), Long.parseLong(folderId), EnumObjectTypeManager.ELEMENT_ID, event);
				if (n != null) {
					n.nodeClicked(event);
					n.setExpanded(true);
					n.expandClicked(event);
				}
			} else {
				CostElementNode n = (CostElementNode) navigationTree.goToNode(Long.parseLong(elementId),
						Long.parseLong(folderId), EnumObjectTypeManager.ELEMENT_ID, event);
				if (n != null) {
					n.nodeClicked(event);
					n.setExpanded(true);
					n.expandClicked(event);
				}
			}
		}
	}

}
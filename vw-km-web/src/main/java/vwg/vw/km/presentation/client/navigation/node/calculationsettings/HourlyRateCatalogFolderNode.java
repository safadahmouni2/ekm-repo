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

import vwg.vw.km.application.service.dto.HourlyRateCatalogDTO;
import vwg.vw.km.integration.persistence.model.FolderModel;
import vwg.vw.km.integration.persistence.model.HourlyRateCatalogModel;
import vwg.vw.km.presentation.client.navigation.NavigationBean;
import vwg.vw.km.presentation.client.navigation.node.ContextMenuEnum;
import vwg.vw.km.presentation.client.navigation.node.FolderNode;
import vwg.vw.km.presentation.client.navigation.node.Node;

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
 * @version $Revision: 1.20 $ $Date: 2013/12/12 10:05:41 $
 */
public class HourlyRateCatalogFolderNode extends CatalogFolder {

	public HourlyRateCatalogFolderNode(NavigationBean tree, FolderNode parent, String category, Long folderId) {
		super(tree, parent, folderId);
		setMenuDisplayText(category);
		setMenuContentTitle(category);
		setFromBundle(Boolean.FALSE);
		setContextMenu(ContextMenuEnum.CatalogFolderObjectContextMenu);
		setTemplateName("./hourlyRateCatalog/hourlyRateCatalogList.xhtml");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.FolderNode#getChilds()
	 */
	@Override
	public List<Node> getChilds() {
		List<Node> childs = new ArrayList<Node>();
		List<FolderModel> folders = treeBean.getNavigationService().loadFolders(getNodeId(), getObjectContentId());
		for (FolderModel model : folders) {
			HourlyRateCatalogFolderNode n = new HourlyRateCatalogFolderNode(treeBean, this, model.getDesignation(),
					model.getFolderId());
			childs.add(n);
		}
		HourlyRateCatalogDTO hourlyRateCatalogDTO = new HourlyRateCatalogDTO();

		hourlyRateCatalogDTO.setUserLogged(treeBean.getUserFromSession());
		hourlyRateCatalogDTO.setFolderId(getNodeId());
		List<HourlyRateCatalogModel> list = treeBean.getHourlyRateCatalogService().loadDTOForList(hourlyRateCatalogDTO)
				.getHourlyRateCatalogs();
		setDto(hourlyRateCatalogDTO);
		for (HourlyRateCatalogModel model : list) {
			HourlyRateCatalogNode n = new HourlyRateCatalogNode(treeBean, this, model.getDesignation(),
					model.getHourlyRateCatalogId());
			childs.add(n);
			objectChildsCount++;
			indexOfLastObject = childs.size() - 1;
		}
		return childs;
	}

}

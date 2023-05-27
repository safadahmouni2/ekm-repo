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
package vwg.vw.km.application.implementation.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import vwg.vw.km.application.implementation.EnumObjectTypeManager;
import vwg.vw.km.application.implementation.FolderManager;
import vwg.vw.km.application.implementation.HourlyRateCatalogManager;
import vwg.vw.km.application.implementation.impl.base.DBManagerImpl;
import vwg.vw.km.integration.persistence.dao.HourlyRateCatalogDAO;
import vwg.vw.km.integration.persistence.model.HourlyRateCatalogModel;

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
 * @author Sebri Zouhaier changed by $Author: monam $
 * @version $Revision: 1.12 $ $Date: 2011/08/17 10:28:11 $
 */
public class HourlyRateCatalogManagerImpl extends DBManagerImpl<HourlyRateCatalogModel, HourlyRateCatalogDAO>
		implements HourlyRateCatalogManager {

	private FolderManager folderManager;

	/**
	 * @see vwg.vw.km.application.implementation.base.BaseManager#getObjectPK(vwg.vw.km.integration.persistence.model.base.BaseModel)
	 */
	public Serializable getObjectPK(HourlyRateCatalogModel model) {
		return model.getHourlyRateCatalogId();
	}

	/**
	 * @see vwg.vw.km.application.implementation.HourlyRateCatalogManager#getHourlyRateCatalogsByFolderAndWorkArea(Long,
	 *      List)
	 */
	public List<HourlyRateCatalogModel> getHourlyRateCatalogsByFolderAndWorkArea(Long folderId,
			List<Long> workAreaIds) {
		return dao.getHourlyRateCatalogsByFolderAndWorkArea(folderId, workAreaIds);
	}

	/**
	 * @see vwg.vw.km.application.implementation.HourlyRateCatalogManager#getHourlyRateCatalogsBySearchString(Long,
	 *      String)
	 */
	public List<HourlyRateCatalogModel> getHourlyRateCatalogsBySearchString(Long startFolderId, String searchString) {
		List<Long> listOfFolders = new ArrayList<Long>();
		listOfFolders.add(startFolderId);
		folderManager.getAllSubFolders(startFolderId, listOfFolders, EnumObjectTypeManager.HOURLY_RATE_CATALOG_ID);
		return dao.getHourlyRateCatalogsBySearchString(listOfFolders, searchString);
	}

	public void setFolderManager(FolderManager folderManager) {
		this.folderManager = folderManager;
	}

	/**
	 * @see vwg.vw.km.application.implementation.HourlyRateCatalogManager#getHourlyRateCatalogsByFolder(Long)
	 */
	public List<HourlyRateCatalogModel> getHourlyRateCatalogsByFolder(Long folderId) {
		return dao.getHourlyRateCatalogsByFolder(folderId);
	}

	/**
	 * @see vwg.vw.km.application.implementation.HourlyRateCatalogManager#getHourlyRateCatalogByDesignationAndFolder(Long,
	 *      boolean, String, Long)
	 */
	public HourlyRateCatalogModel getHourlyRateCatalogByDesignationAndFolder(Long catalogId, boolean loaded,
			String designation, Long folderId) {
		return dao.getHourlyRateCatalogByDesignationAndFolder(catalogId, loaded, designation, folderId);
	}
}

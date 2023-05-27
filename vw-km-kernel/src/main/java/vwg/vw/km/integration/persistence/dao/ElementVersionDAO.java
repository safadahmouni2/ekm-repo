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
package vwg.vw.km.integration.persistence.dao;

import java.util.List;

import vwg.vw.km.integration.persistence.dao.base.BaseDao;
import vwg.vw.km.integration.persistence.model.ElementVersionModel;

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
 * @version $Revision: 1.23 $ $Date: 2015/12/07 13:13:31 $
 */
public interface ElementVersionDAO extends BaseDao<ElementVersionModel> {

	/**
	 * 
	 * @param elementId
	 * @return Last ElementVersion By ElementId
	 */
	public ElementVersionModel getLastElementVersionByElementId(Long elementId);

	/**
	 * return list of approved element versions to be released
	 * 
	 * @return ElementVersionToBeTraitedByBatch
	 */
	public List<ElementVersionModel> getElementVersionToBeTraitedByBatch();

	/**
	 * return Last ElementVersion By Status and element id
	 * 
	 * @param elementId
	 * @param statusId
	 * @return
	 */
	public ElementVersionModel getLastElementVersionByStatus(Long elementId, Long statusId);

	/**
	 * return list of all element version ids attached to given element id
	 * 
	 * @param elementId
	 * @return List<Long>
	 */
	public List<Long> getElementVersionAttachedIds(Long elementId);

	/**
	 * check if this version is the last one
	 * 
	 * @param elementVersionId
	 * @param elementId
	 * @param statusId
	 */
	public boolean isTheLastElementVersion(Long elementVersionId, Long elementId, Long statusId);

	/**
	 * get the list of element versions by status
	 * 
	 * @param elementId
	 * @param statusId
	 * @return
	 */
	public List<ElementVersionModel> getElementVersionsByStatus(Long elementId, Long statusId);

	/**
	 * For statistic purpose select all elements versions in the DB
	 * 
	 * @return
	 */
	public List<Object[]> getAllElementsVersions();

	public List<ElementVersionModel> getElementsBySearchString(List<Long> listOfFolders, Long loggedUser,
			String searchString);

}

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

import vwg.vw.km.common.type.ChangeTypeValue;
import vwg.vw.km.common.type.SearchHistoryObject;
import vwg.vw.km.integration.persistence.dao.base.BaseDao;
import vwg.vw.km.integration.persistence.model.ElementVersionChangeModel;

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
 * @version $Revision: 1.13 $ $Date: 2019/01/18 15:00:44 $
 */
public interface ElementVersionChangeDAO extends BaseDao<ElementVersionChangeModel> {

	/**
	 * 
	 * @param versionId
	 * @param type
	 * @return list of ElementVersionChangeModel for given element version id
	 */
	List<ElementVersionChangeModel> getChangesByVersion(Long versionId, String type);

	/**
	 * 
	 * @param historyObject
	 * @return list of ElementVersionChangeModel by criteria
	 */
	List<ElementVersionChangeModel> getChangesByCriteria(SearchHistoryObject historyObject);

	/**
	 * Return the last �nderungsart of a version
	 * 
	 * @param versionId
	 * @return
	 */
	ElementVersionChangeModel getLastChangeTypeByVersion(Long versionId);

	/**
	 * 
	 * @param versionId
	 * @param type
	 * @return list of ElementVersionChangeModel for given element version id and change type
	 */
	List<ElementVersionChangeModel> getChangesByVersionAndChangeType(Long versionId, ChangeTypeValue type);

	/**
	 * 
	 * @param versionIds
	 * @param type
	 * @return list of ElementVersionChangeModel for given element versions id and change type
	 */
	List<ElementVersionChangeModel> getChangesByVersionsAndChangeType(List<Long> versionIds, ChangeTypeValue type);

	/**
	 * 
	 * @param versionIds
	 * @param change
	 * @return list of ElementVersionChangeModel for given version ids and change
	 */
	List<ElementVersionChangeModel> getChangesByStandsAndChange(List<Long> versionIds, String change);

	/**
	 * get the history of a change by version
	 * 
	 * @param versionId
	 * @param change
	 * @return
	 */
	List<ElementVersionChangeModel> getChangeDetailByVersion(Long versionId, String change);

	/**
	 * anonomyze User name
	 * 
	 * @param userId
	 */
	void anonomyzeUser(Long userId);
}

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
import vwg.vw.km.integration.persistence.model.ComponentStandChangeModel;

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
 * @version $Revision: 1.11 $ $Date: 2019/01/18 15:00:44 $
 */
public interface ComponentStandChangeDAO extends BaseDao<ComponentStandChangeModel> {

	/**
	 * 
	 * @param versionId
	 * @return list of ComponentVersionChangeModel for given components version id
	 */
	List<ComponentStandChangeModel> getChangesByComponentStand(Long standId);

	/**
	 * 
	 * @param historyObject
	 * @return list of ComponentVersionChangeModel by criteria
	 */
	List<ComponentStandChangeModel> getChangesByCriteria(SearchHistoryObject historyObject);

	/**
	 * 
	 * @param standId
	 * @param ChangeTypeValue
	 *            type
	 * @return list of ComponentStandChangeModel for given element version id and change type
	 */
	List<ComponentStandChangeModel> getChangesByStandAndChangeType(Long standId, ChangeTypeValue type);

	/**
	 * 
	 * @param standIds
	 * @param ChangeTypeValue
	 *            type
	 * @return list of ComponentStandChangeModel for given element version id and change type
	 */
	List<ComponentStandChangeModel> getChangesByStandsAndChangeType(List<Long> standIds, ChangeTypeValue type);

	/**
	 * 
	 * @param standId
	 * @param change
	 * @return list of ComponentStandChangeModel for given component stand id and change
	 */
	List<ComponentStandChangeModel> getChangesByStandAndChange(Long standId, String change);

	/**
	 * anonomyze User name
	 * 
	 * @param userId
	 */
	void anonomyzeUser(Long userId);

}

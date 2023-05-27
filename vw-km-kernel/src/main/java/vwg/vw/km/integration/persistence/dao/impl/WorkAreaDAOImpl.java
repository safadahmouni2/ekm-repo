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
package vwg.vw.km.integration.persistence.dao.impl;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.criterion.MatchMode;

import vwg.vw.km.integration.persistence.dao.WorkAreaDAO;
import vwg.vw.km.integration.persistence.dao.impl.base.DBDaoImpl;
import vwg.vw.km.integration.persistence.hibernate.EscapingRestrictions;
import vwg.vw.km.integration.persistence.model.WorkAreaModel;

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
 * @version $Revision: 1.11 $ $Date: 2015/12/11 08:00:44 $
 */
public class WorkAreaDAOImpl extends DBDaoImpl<WorkAreaModel> implements WorkAreaDAO {

	/**
	 * @see vwg.vw.km.integration.persistence.dao.impl.base.DBDaoImpl#getBusinessClass()
	 */
	@Override
	protected Class<WorkAreaModel> getBusinessClass() {
		return WorkAreaModel.class;
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.WorkAreaDAO#getWorkAreaModelByDesignation(String)
	 */
	@Override
	public WorkAreaModel getWorkAreaModelByDesignation(String designation) {
		WorkAreaModel workAreaModel = null;

		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<WorkAreaModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<WorkAreaModel> root = criteria.from(WorkAreaModel.class);

		criteria.select(root)
				.where(EscapingRestrictions.ilike(builder, root.get("designation"), designation, MatchMode.EXACT));

		List<WorkAreaModel> list = getModelList(createQuery(criteria));
		if (list.size() > 0) {
			workAreaModel = setModelAsLoaded(list.get(0));
		}
		return workAreaModel;
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.WorkAreaDAO#getWorkAreasBySearchString(String)
	 */
	@Override
	public List<WorkAreaModel> getWorkAreasBySearchString(String searchString) {
		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<WorkAreaModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<WorkAreaModel> root = criteria.from(WorkAreaModel.class);

		criteria.select(root)
				.where(EscapingRestrictions.ilike(builder, root.get("designation"), searchString, MatchMode.ANYWHERE))
				.orderBy(builder.asc(builder.lower(root.get("designation"))));

		return getModelList(createQuery(criteria));
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.WorkAreaDAO#getAlphaOrderedWorkAreas()
	 */
	@Override
	public List<WorkAreaModel> getAlphaOrderedWorkAreas() {

		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<WorkAreaModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<WorkAreaModel> root = criteria.from(WorkAreaModel.class);

		criteria.select(root).orderBy(builder.asc(builder.lower(root.get("designation"))));

		return getModelList(createQuery(criteria));
	}

	@Override
	public WorkAreaModel getWorkAreaByBrand(String brandId) {

		WorkAreaModel result = null;
		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<WorkAreaModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<WorkAreaModel> root = criteria.from(WorkAreaModel.class);

		criteria.select(root).where(builder.equal(root.get("brand").get("brandId"), brandId))
				.orderBy(builder.asc(builder.lower(root.get("designation"))));

		Query q = createQuery(criteria);

		q.setMaxResults(1);

		@SuppressWarnings("unchecked")
		List<WorkAreaModel> returnValue = q.getResultList();

		if (returnValue.size() > 0) {
			result = setModelAsLoaded(returnValue.get(0));
		}
		return result;
	}
}

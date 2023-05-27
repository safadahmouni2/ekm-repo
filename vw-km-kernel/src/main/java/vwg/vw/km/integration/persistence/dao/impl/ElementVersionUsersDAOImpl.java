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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import vwg.vw.km.integration.persistence.dao.ElementVersionUsersDAO;
import vwg.vw.km.integration.persistence.dao.impl.base.DBDaoImpl;
import vwg.vw.km.integration.persistence.model.ElementVersionUsersModel;

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
 * @author Zied Saidi changed by $Author: saidi $
 * @version $Revision: 1.9 $ $Date: 2013/10/03 16:58:37 $
 */

public class ElementVersionUsersDAOImpl extends DBDaoImpl<ElementVersionUsersModel> implements ElementVersionUsersDAO {

	/**
	 * @see vwg.vw.km.integration.persistence.dao.impl.base.DBDaoImpl#getBusinessClass()
	 */
	@Override
	protected Class<ElementVersionUsersModel> getBusinessClass() {
		return ElementVersionUsersModel.class;
	}

	/**
	 * 
	 * @return cache region used by hibernate caching API
	 */
	@Override
	protected String getCacheRegion() {
		return "query.elementVersionUses";
	}

	/**
	 * 
	 * @return true if this object is cacheable
	 */
	@Override
	protected boolean isCacheable() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.integration.persistence.dao.ElementVersionUsesDAO#getUsedElementByElementVersionId(java.lang.Long)
	 */
	@Override
	public List<ElementVersionUsersModel> getUsedElementByElementId(Long elementId) {
		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<ElementVersionUsersModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<ElementVersionUsersModel> root = criteria.from(ElementVersionUsersModel.class);

		Path<Object> path = root.get("elementVersion").get("element");

		criteria.select(root).where(builder.equal(path.get("elementId"), elementId));
		return getModelList(createQuery(criteria));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.integration.persistence.dao.ElementVersionUsesDAO#getElementVersionUses(vwg.vw.km.integration.
	 * persistence.model.ElementVersionModel, vwg.vw.km.integration.persistence.model.BrandModel)
	 */
	@Override
	public ElementVersionUsersModel getElementVersionUsers(Long elementVersionId, String brandId) {
		
		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<ElementVersionUsersModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<ElementVersionUsersModel> root = criteria.from(ElementVersionUsersModel.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(builder.equal(root.get("elementVersion").get("elementVersionId"), elementVersionId));
		predicates.add(builder.equal(root.get("brand").get("brandId"), brandId));

		criteria.select(root).where(predicates.toArray(new Predicate[] {}));

		Query q = createQuery(criteria).setMaxResults(1);
		
		return setModelAsLoaded(getSingleResultOrNull(q));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.integration.persistence.dao.ElementVersionUsersDAO#getElementUsers(java.lang.Long,
	 * vwg.vw.km.integration.persistence.model.BrandModel)
	 */
	@Override
	public ElementVersionUsersModel getElementUsers(Long elementId, String brandId) {

		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<ElementVersionUsersModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<ElementVersionUsersModel> root = criteria.from(ElementVersionUsersModel.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		Path<Object> path = root.get("elementVersion").get("element");

		predicates.add(builder.equal(path.get("elementId"), elementId));
		predicates.add(builder.equal(root.get("brand").get("brandId"), brandId));

		criteria.select(root).where(predicates.toArray(new Predicate[] {}));

		Query q = createQuery(criteria).setMaxResults(1);

		return setModelAsLoaded(getSingleResultOrNull(q));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.integration.persistence.dao.ElementVersionUsersDAO#getInactiveElementVersionUsers()
	 */
	@Override
	public List<ElementVersionUsersModel> getInactivatedElementVersionUsers() {
		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<ElementVersionUsersModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<ElementVersionUsersModel> root = criteria.from(ElementVersionUsersModel.class);
		criteria.select(root).where(builder.equal(root.get("active"), Boolean.FALSE));

		return getModelList(createQuery(criteria));
	}
}

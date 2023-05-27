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

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.criterion.MatchMode;
import org.hibernate.jpa.QueryHints;

import vwg.vw.km.common.type.BaseDateTime;
import vwg.vw.km.integration.persistence.dao.UserDAO;
import vwg.vw.km.integration.persistence.dao.impl.base.DBDaoImpl;
import vwg.vw.km.integration.persistence.hibernate.EscapingRestrictions;
import vwg.vw.km.integration.persistence.model.UserModel;

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
 * @author Sebri Zouhaier changed by $Author: abidh $
 * @version $Revision: 1.30 $ $Date: 2020/02/10 11:19:42 $
 */
public class UserDAOImpl extends DBDaoImpl<UserModel> implements UserDAO {

	/**
	 * @see vwg.vw.km.integration.persistence.dao.impl.base.DBDaoImpl#getBusinessClass()
	 */
	@Override
	protected Class<UserModel> getBusinessClass() {
		return UserModel.class;
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.base.BaseDao#removeAll()
	 */
	@Override
	public void removeAll() {
		Query q = getSession()
				.createQuery("update UserModel u set u.active=false and deactivationDate = :deactivationDate");
		q.setParameter("deactivationDate", new Date());
		q.executeUpdate();
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.base.BaseDao#getObject(java.lang.Class, java.io.Serializable)
	 */
	@Override
	public UserModel getObject(Serializable id) {
		UserModel o = getSession().get(getBusinessClass(), id);
		if (o != null) {
			o.setLoaded(true);
		} else {
			o = null;
		}
		return o;
	}

	@Override
	public List<UserModel> getObjects() {
		Query q = getSession().createQuery("from " + getBusinessClass().getSimpleName() + " where active=true");
		if (isCacheable()) {
			q.setHint(QueryHints.HINT_CACHEABLE, true);
			q.setHint(QueryHints.HINT_CACHE_REGION, getCacheRegion());
		}
		return getModelList(q);
	}

	@Override
	public UserModel getUserByKUMSId(String kums) {

		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<UserModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<UserModel> root = criteria.from(UserModel.class);

		criteria.select(root)
				.where(EscapingRestrictions.ilike(builder, root.get("kUMSUserId"), kums, MatchMode.EXACT));
		Query q = createQuery(criteria);

		return setModelAsLoaded(getSingleResultOrNull(q));
	}

	@Override
	public List<UserModel> getAdministratedUsers(Long workAreaId) {

		Query q = null;
		if (workAreaId != null) {
			q = getSession().createSQLQuery(
					"select USER_ID_REF from T_REL_BENUTZER_ARBEITSBEREICH where ARBEITSBEREICH_REF = " + workAreaId);
		} else {
			q = getSession().createSQLQuery(
					"select USER_ID from T_BENUTZER minus select USER_ID_REF from T_REL_BENUTZER_ARBEITSBEREICH");
		}
		@SuppressWarnings("unchecked")
		List<BigDecimal> list = q.getResultList();
		if (list.size() <= 0) {
			return new ArrayList<UserModel>();
		}

		List<Long> l = new ArrayList<Long>();
		for (int i = 0; i < list.size(); i++) {
			Long item = new Long(list.get(i).longValue());
			l.add(item);
		}

		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<UserModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<UserModel> root = criteria.from(UserModel.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(builder.notEqual(root.get("userId"), 1L));
		predicates.add(builder.equal(root.get("anonymized"), Boolean.FALSE));
		predicates.add(root.get("userId").in(l));

		List<Order> orderList = new ArrayList<Order>();

		orderList.add(builder.asc(builder.lower(root.get("lastName"))));
		orderList.add(builder.asc(root.get("firstName")));

		criteria.distinct(true).select(root).where(predicates.toArray(new Predicate[] {})).orderBy(orderList);
		return getModelList(createQuery(criteria));
	}

	@Override
	public List<UserModel> getUsersBySearchString(String searchString) {
		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<UserModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<UserModel> root = criteria.from(UserModel.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		Predicate p1 = EscapingRestrictions.ilike(builder, root.get("lastName"), searchString, MatchMode.ANYWHERE);
		Predicate p2 = EscapingRestrictions.ilike(builder, root.get("firstName"), searchString,
				MatchMode.ANYWHERE);

		predicates.add(builder.or(p1, p2));
		predicates.add(builder.equal(root.get("anonymized"), Boolean.FALSE));

		List<Order> orderList = new ArrayList<Order>();

		orderList.add(builder.asc(root.get("firstName")));
		orderList.add(builder.asc(builder.lower(root.get("lastName"))));

		criteria.select(root).where(predicates.toArray(new Predicate[] {})).orderBy(orderList);

		return getModelList(createQuery(criteria));
	}

	@Override
	public List<UserModel> getUsersByCatalog(Long catalogId) {
		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<UserModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<UserModel> root = criteria.from(UserModel.class);

		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(builder.equal(root.get("active"), Boolean.TRUE));
		predicates.add(builder.equal(root.get("defaultStdSatzKatRefId"), catalogId));

		criteria.select(root).where(predicates.toArray(new Predicate[] {}));
		return getModelList(createQuery(criteria));
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.UserDAO#getWorkingUsers()
	 */
	@Override
	public List<UserModel> getWorkingUsers() {
		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<UserModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<UserModel> root = criteria.from(UserModel.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(builder.notEqual(root.get("userId"), 1L));
		predicates.add(builder.equal(root.get("active"), Boolean.TRUE));

		List<Order> orderList = new ArrayList<Order>();

		orderList.add(builder.asc(root.get("firstName")));
		orderList.add(builder.asc(root.get("lastName")));

		criteria.distinct(true).select(root).where(predicates.toArray(new Predicate[] {})).orderBy(orderList);

		return getModelList(createQuery(criteria));
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.UserDAO#getUsersToBeAnonomyzedByBatch()
	 */
	@Override
	public List<UserModel> getUsersToBeAnonomyzedByBatch() {
		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<UserModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<UserModel> root = criteria.from(UserModel.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(builder.equal(root.get("active"), Boolean.FALSE));
		predicates.add(builder.equal(root.get("anonymized"), Boolean.FALSE));
		predicates.add(root.get("deactivationDate").isNotNull());

		predicates.add(builder.lessThanOrEqualTo(root.get("deactivationDate").as(Date.class),
				new BaseDateTime(BaseDateTime.getCurrentDateTime().getDateSeparatedPt()).addMonths(-6).getDate()));

		criteria.select(root).where(predicates.toArray(new Predicate[] {}));

		return getModelList(createQuery(criteria));
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.UserDAO#getUsersToBeDesactivatedByBatch()
	 */
	@Override
	public List<UserModel> getUsersToBeDeactivatedByBatch() {
		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<UserModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<UserModel> root = criteria.from(UserModel.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(builder.equal(root.get("active"), Boolean.TRUE));
		predicates.add(builder.equal(root.get("anonymized"), Boolean.FALSE));
		predicates.add(root.get("lastLogInTime").isNotNull());

		predicates.add(builder.lessThanOrEqualTo(root.get("lastLogInTime").as(Date.class),
				new BaseDateTime(BaseDateTime.getCurrentDateTime().getDateSeparatedPt()).addMonths(-6).getDate()));

		criteria.select(root).where(predicates.toArray(new Predicate[] {}));

		return getModelList(createQuery(criteria));
	}

	/**
	 * ABA:PTS requirement 22205:statistic page
	 * 
	 * @see vwg.vw.km.integration.persistence.dao.UserDAO#getActiveUsersCount()
	 */
	@Override
	public int getActiveUsersCount() {

		Query q = getSession()
				.createQuery("select count(*) from " + getBusinessClass().getSimpleName() + " where active=true");
		if (isCacheable()) {
			q.setHint(QueryHints.HINT_CACHEABLE, true);
			q.setHint(QueryHints.HINT_CACHE_REGION, getCacheRegion());
		}
		int count = ((Long) q.getResultList().get(0)).intValue();

		return count;
	}

	/**
	 * 
	 *   
	 */
	@Override
	public UserModel getUserByActivationCode(String searchString) {
		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<UserModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<UserModel> root = criteria.from(UserModel.class);

		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(builder.equal(root.get("active"), Boolean.FALSE));
		predicates.add(
				EscapingRestrictions.ilike(builder, root.get("activationCode"), searchString, MatchMode.EXACT));

		criteria.select(root).where(predicates.toArray(new Predicate[] {}));

		Query q = createQuery(criteria);

		return setModelAsLoaded(getSingleResultOrNull(q));
	}

}

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

package vwg.vw.km.integration.persistence.dao.impl.base;

import java.io.Serializable;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jpa.QueryHints;

import vwg.vw.km.integration.persistence.dao.base.BaseDao;
import vwg.vw.km.integration.persistence.dao.exception.BaseRtDAOException;
import vwg.vw.km.integration.persistence.model.base.BaseModel;

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
 * @param <T>
 * @author Sebri Zouhaier changed by $Author: zouhair $
 * @version $Revision: 1.13 $ $Date: 2013/12/06 15:53:08 $
 */
public abstract class DBDaoImpl<T extends BaseModel> implements BaseDao<T> {

	/**
	 * @return Class of given object
	 */
	protected abstract Class<T> getBusinessClass();

	/**
	 * 
	 * @return cache region used by hibernate caching API
	 */
	protected String getCacheRegion() {
		return null;
	}

	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * @return a session created on the fly (volatile)
	 */
	public Session getSession() {
		return this.sessionFactory.getCurrentSession();
	}

	/**
	 * 
	 * @return true if this object is cacheable
	 */
	protected boolean isCacheable() {
		return false;
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.base.BaseDao#saveObject(java.lang.Object)
	 */
	@Override
	public Serializable saveObject(T o) {
		Serializable returnValue = getSession().save(o);
		o.setLoaded(true);
		return returnValue;
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.base.BaseDao#getObject(java.lang.Class, java.io.Serializable)
	 */
	@Override
	public T getObject(Serializable id) {
		T o = getSession().get(getBusinessClass(), id);
		if (o != null) {
			o.setLoaded(true);
		}
		return o;
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.base.BaseDao#getObjects(java.lang.Class)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<T> getObjects() {
		List<T> list = null;
		Query q = getSession().createQuery("from " + getBusinessClass().getSimpleName());

		if (isCacheable()) {
			q.setHint(QueryHints.HINT_CACHEABLE, true);
			q.setHint(QueryHints.HINT_CACHE_REGION, getCacheRegion());

		}
		list = q.getResultList();
		list = setModelsListAsLoaded(list);
		return list;
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.base.BaseDao#removeObject(java.lang.Class, java.io.Serializable)
	 */
	@Override
	public void removeObject(Serializable id) {
		getSession().delete(getObject(id));
	}

	/**
	 * @param list
	 *            : The list which its elements will be marked loaded
	 * @return The list with elements set to loaded
	 */
	protected List<T> setModelsListAsLoaded(List<T> list) {
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				list.get(i).setLoaded(true);
			}
		}
		return list;
	}

	/**
	 * @param T
	 *            : The object which will be marked loaded
	 * @return The lobject set to loaded
	 */
	protected T setModelAsLoaded(T o) {
		if (o != null) {
			o.setLoaded(true);
		}
		return o;
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.base.BaseDao#updateObject()
	 */
	@Override
	public void updateObject(T o) {
		getSession().update(o);
	}

	/**
	 * create new criteria
	 * 
	 * @param clazz
	 * @return criteria
	 */
	@SuppressWarnings("deprecation")
	public org.hibernate.Criteria createCriteria(Class<T> clazz) {
		org.hibernate.Criteria q = getSession().createCriteria(clazz);
		if (isCacheable()) {
			q.setCacheable(true);
			q.setCacheRegion(getCacheRegion());
		}
		return q;
	}

	/**
	 * create new criteria
	 * 
	 * @param clazz
	 * @return criteria
	 */

	public CriteriaBuilder createBuidler() {
		return getSession().getCriteriaBuilder();
	}

	public CriteriaQuery<T> createCriteria(Class<T> clazz, CriteriaBuilder builder) {

		return builder.createQuery(clazz);
	}

	public Query createQuery(CriteriaQuery<T> criteria) {
		Query q = getSession().createQuery(criteria);
		 if (isCacheable()) {
		 q.setHint(QueryHints.HINT_CACHEABLE, true);
		 q.setHint(QueryHints.HINT_CACHE_REGION, getCacheRegion());
		
		 }
		return q;
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.base.BaseDao#removeAll()
	 */
	@Override
	public void removeAll() {
		Query q = getSession().createQuery("delete from " + getBusinessClass().getSimpleName());
		q.executeUpdate();
	}

	/**
	 * return IN criteria
	 * 
	 * @param propertyName
	 * @param list
	 * @return criteria
	 */
	@SuppressWarnings("rawtypes")
	protected Predicate addInRestrictions(CriteriaBuilder builder, Path<T> propertyName, List list) {
		int size = list.size();
		int fromIndex = 0;
		int toIndex = (size > 1000) ? 1000 : size;
		List subList = list.subList(fromIndex, toIndex);
		Predicate lhs = propertyName.in(subList);
		toIndex = subList.size();
		size = size - toIndex;
		while (size > 0) {
			fromIndex = toIndex;
			toIndex += (size > 1000) ? 1000 : size;
			subList = list.subList(fromIndex, toIndex);
			Predicate rhs = propertyName.in(subList);
			lhs = builder.or(lhs, rhs);
			size = size - subList.size();
		}
		return lhs;
	}

	/**
	 * return IN criteria
	 * 
	 * @param propertyName
	 * @param list
	 * @return criteria
	 */
	@SuppressWarnings("rawtypes")
	protected org.hibernate.criterion.Criterion addInRestrictions(String propertyName, List list) {
		int size = list.size();
		int fromIndex = 0;
		int toIndex = (size > 1000) ? 1000 : size;
		List subList = list.subList(fromIndex, toIndex);
		org.hibernate.criterion.Criterion lhs = org.hibernate.criterion.Restrictions.in(propertyName, subList);
		toIndex = subList.size();
		size = size - toIndex;
		while (size > 0) {
			fromIndex = toIndex;
			toIndex += (size > 1000) ? 1000 : size;
			subList = list.subList(fromIndex, toIndex);
			org.hibernate.criterion.Criterion rhs = org.hibernate.criterion.Restrictions.in(propertyName, subList);
			lhs = org.hibernate.criterion.Restrictions.or(lhs, rhs);
			size = size - subList.size();
		}
		return lhs;
	}
	/**
	 * returns model list by given query
	 * 
	 * @param query
	 *            Query to be executed
	 * @return found list in db
	 */
	@SuppressWarnings("unchecked")
	protected List<T> getModelList(Query query) {
		List<T> returnValue = null;
		try {

			returnValue = query.getResultList();
			returnValue = setModelsListAsLoaded(returnValue);
		} catch (HibernateException ex) {
			throw new BaseRtDAOException("Dao exception: Class : " + getBusinessClass() + ", query=" + query, ex);
		}
		return returnValue;
	}

	/**
	 * returns model list by given query
	 * 
	 * @param query
	 *            Query to be executed
	 * @return found list in db
	 */
	@SuppressWarnings("unchecked")
	protected List<T> getModelList(org.hibernate.Criteria criteria) {
		List<T> returnValue = null;
		try {
			returnValue = criteria.list();
			returnValue = setModelsListAsLoaded(returnValue);
		} catch (HibernateException ex) {
			throw new BaseRtDAOException(
					"Dao exception: Class : " + getBusinessClass() + ", criteria=" + criteria.toString(), ex);
		}
		return returnValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * vwg.vw.km.integration.persistence.dao.base.BaseDao#evict(vwg.vw.km.integration.persistence.model.base.BaseModel)
	 */
	@Override
	public void evict(T model) {
		getSession().evict(model);
	}

	@SuppressWarnings("unchecked")
	protected T getSingleResultOrNull(Query query) {
		try {
			return (T) query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
	}
}
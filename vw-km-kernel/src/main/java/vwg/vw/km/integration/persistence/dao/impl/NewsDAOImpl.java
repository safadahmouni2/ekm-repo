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
import java.util.Date;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.criterion.MatchMode;

import vwg.vw.km.integration.persistence.dao.NewsDAO;
import vwg.vw.km.integration.persistence.dao.impl.base.DBDaoImpl;
import vwg.vw.km.integration.persistence.hibernate.EscapingRestrictions;
import vwg.vw.km.integration.persistence.model.NewsModel;

/**
 * <p>
 * Title: EKM
 * <p>
 * Description : Class description goes here
 * </p>
 * <p>
 * Copyright: VW (c) 2011
 * </p>
 * 
 * @author hamdiABID @version.
 * 
 */
public class NewsDAOImpl extends DBDaoImpl<NewsModel> implements NewsDAO {

	/**
	 * @see vwg.vw.km.integration.persistence.dao.impl.base.DBDaoImpl#getBusinessClass()
	 */
	@Override
	protected Class<NewsModel> getBusinessClass() {
		return NewsModel.class;
	}

	@Override
	public List<NewsModel> getListNewsActivAndSortedByLatestDate() {
		Date currentDate = new Date();
		
		Query q = getSession().createQuery("from " + getBusinessClass().getSimpleName()
				+ " where active=true and validFrom <= :currentDate and (dateOfExpiry IS NULL or (dateOfExpiry IS NOT NULL and dateOfExpiry >= :currentDate)) order by validFrom desc");
		q.setParameter("currentDate", currentDate);

		return getModelList(q);
	}

	@Override
	public List<NewsModel> getListNewsSortedByLatestDate() {
		Query q = getSession().createQuery("from " + getBusinessClass().getSimpleName() + " order by validFrom desc");

		return getModelList(q);
	}

	@Override
	public List<NewsModel> getNewsBySearchString(String searchString) {
		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<NewsModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<NewsModel> root = criteria.from(NewsModel.class);

		Predicate p1 = EscapingRestrictions.ilike(builder, root.get("title"), searchString, MatchMode.ANYWHERE);
		Predicate p2 = EscapingRestrictions.ilike(builder, root.get("description"), searchString,
				MatchMode.ANYWHERE);

		List<Order> orderList = new ArrayList<>();

		orderList.add(builder.asc(builder.lower(root.get("title"))));
		orderList.add(builder.asc(root.get("description")));

		criteria.select(root).where(builder.or(p1, p2)).orderBy(orderList);

		return getModelList(createQuery(criteria));
	}

}

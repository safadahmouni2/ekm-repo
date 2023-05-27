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

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.criterion.MatchMode;

import vwg.vw.km.integration.persistence.dao.HourlyRateCatalogDAO;
import vwg.vw.km.integration.persistence.dao.impl.base.DBDaoImpl;
import vwg.vw.km.integration.persistence.hibernate.EscapingRestrictions;
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
 * @author Sebri Zouhaier changed by $Author: benamora $
 * @version $Revision: 1.16 $ $Date: 2013/09/26 14:43:58 $
 */
public class HourlyRateCatalogDAOImpl extends DBDaoImpl<HourlyRateCatalogModel> implements HourlyRateCatalogDAO {

	/**
	 * @see vwg.vw.km.integration.persistence.dao.impl.base.DBDaoImpl#getBusinessClass()
	 */
	@Override
	protected Class<HourlyRateCatalogModel> getBusinessClass() {
		return HourlyRateCatalogModel.class;
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.HourlyRateCatalogDAO#getHourlyRateCatalogsByFolderAndWorkArea(Long,
	 *      List)
	 */
	@Override
	public List<HourlyRateCatalogModel> getHourlyRateCatalogsByFolderAndWorkArea(Long folderId,
			List<Long> workAreaIds) {
		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<HourlyRateCatalogModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<HourlyRateCatalogModel> root = criteria.from(HourlyRateCatalogModel.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (folderId == null) {
			if (!workAreaIds.isEmpty()) {
				predicates.add(root.get("workArea").get("workAreaId").in(workAreaIds));
			} else {
				return new ArrayList<HourlyRateCatalogModel>();
			}
		} else {
			if (!workAreaIds.isEmpty()) {
				predicates.add(builder.or(root.get("workArea").get("workAreaId").in(workAreaIds),
						root.get("workArea").get("workAreaId").isNull()));
				predicates.add(builder.and(builder.equal(root.get("folder").get("folderId"), folderId)));
			} else {
				predicates.add(builder.and(root.get("workArea").get("workAreaId").isNull(),
						builder.equal(root.get("folder").get("folderId"), folderId)));
			}
			criteria.select(root).where(predicates.toArray(new Predicate[] {}));
		}
		return getModelList(createQuery(criteria));
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.HourlyRateCatalogDAO#getHourlyRateCatalogsBySearchString(List, String)
	 */
	@Override
	public List<HourlyRateCatalogModel> getHourlyRateCatalogsBySearchString(List<Long> listOfFolders,
			String searchString) {

		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<HourlyRateCatalogModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<HourlyRateCatalogModel> root = criteria.from(HourlyRateCatalogModel.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(root.get("folder").get("folderId").in(listOfFolders));
		predicates.add(
				EscapingRestrictions.ilike(builder, root.get("designation"), searchString, MatchMode.ANYWHERE));

		criteria.select(root).where(predicates.toArray(new Predicate[] {}));
		return getModelList(createQuery(criteria));
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.HourlyRateCatalogDAO#getHourlyRateCatalogsByFolder(Long)
	 */
	@Override
	public List<HourlyRateCatalogModel> getHourlyRateCatalogsByFolder(Long folderId) {
		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<HourlyRateCatalogModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<HourlyRateCatalogModel> root = criteria.from(HourlyRateCatalogModel.class);

		List<Predicate> predicates = new ArrayList<Predicate>();
		if (folderId != null) {
			predicates.add(builder.equal(root.get("folder").get("folderId"), folderId));
		} else {
			predicates.add(root.get("folder").get("folderId").isNull());
		}

		List<Order> orderList = new ArrayList<Order>();

		orderList.add(builder.asc(root.get("hourlyRateCatalogId")));
		orderList.add(builder.asc(builder.lower(root.get("designation"))));

		criteria.select(root).where(predicates.toArray(new Predicate[] {})).orderBy(orderList);

		return getModelList(createQuery(criteria));
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.HourlyRateCatalogDAO#getHourlyRateCatalogByDesignationAndFolder(Long,
	 *      boolean, String, Long)
	 */
	@Override
	public HourlyRateCatalogModel getHourlyRateCatalogByDesignationAndFolder(Long catalogId, boolean loaded,
			String designation, Long folderId) {
		HourlyRateCatalogModel hRateCatalogModel = null;


		CriteriaBuilder builder = createBuidler();

		CriteriaQuery<HourlyRateCatalogModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<HourlyRateCatalogModel> root = criteria.from(HourlyRateCatalogModel.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(builder.equal(root.get("folder").get("folderId"), folderId));
		predicates.add(EscapingRestrictions.ilike(builder, root.get("designation"), designation, MatchMode.EXACT));

		if (catalogId != null && loaded) {
			predicates.add(builder.notEqual(root.get("hourlyRateCatalogId"), catalogId));
		}

		criteria.select(root).where(predicates.toArray(new Predicate[] {}));

		List<HourlyRateCatalogModel> list = getModelList(createQuery(criteria));

		if (list.size() > 0) {
			hRateCatalogModel = list.get(0);
		}
		return hRateCatalogModel;
	}
}

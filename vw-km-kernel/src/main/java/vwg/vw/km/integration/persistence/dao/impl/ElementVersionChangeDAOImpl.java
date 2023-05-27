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

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.criterion.MatchMode;

import vwg.vw.km.common.type.BaseDateTime;
import vwg.vw.km.common.type.ChangeType;
import vwg.vw.km.common.type.ChangeTypeValue;
import vwg.vw.km.common.type.SearchHistoryObject;
import vwg.vw.km.integration.persistence.dao.ElementVersionChangeDAO;
import vwg.vw.km.integration.persistence.dao.impl.base.DBDaoImpl;
import vwg.vw.km.integration.persistence.hibernate.EscapingRestrictions;
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
 * @version $Revision: 1.50 $ $Date: 2019/01/18 15:00:44 $
 */
/**
 * <p>
 * Title: EKM
 * <p>
 * Description : Class description goes here
 * </p>
 * <p>
 * Copyright: VW (c) 2012
 * </p>
 * 
 * @author saidi changed by $Author: zouhair $
 * @version $Revision: 1.50 $ $Date: 2019/01/18 15:00:44 $
 */
public class ElementVersionChangeDAOImpl extends DBDaoImpl<ElementVersionChangeModel>
		implements ElementVersionChangeDAO {

	/**
	 * @see vwg.vw.km.integration.persistence.dao.impl.base.DBDaoImpl#getBusinessClass()
	 */
	@Override
	protected Class<ElementVersionChangeModel> getBusinessClass() {
		return ElementVersionChangeModel.class;
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.ElementVersionChangeDAO#getChangesByVersion(java.lang.Long,java.lang.String)
	 */
	@Override
	public List<ElementVersionChangeModel> getChangesByVersion(Long versionId, String type) {
		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<ElementVersionChangeModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<ElementVersionChangeModel> root = criteria.from(ElementVersionChangeModel.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(builder.equal(root.get("elementVersion").get("elementVersionId"), versionId));

		if (type != null) {
			predicates.add(builder.equal(root.get("type"), type));
		}

		criteria.select(root).where(predicates.toArray(new Predicate[] {})).orderBy(builder.desc(root.get("changeId")));

		return getModelList(createQuery(criteria));
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.ElementVersionChangeDAO#getChangesByCriteria(SearchHistoryObject)
	 *      31.10.2013: ZS: PTS_Problem-26241: results displayed outside the period set filter
	 */
	@Override
	public List<ElementVersionChangeModel> getChangesByCriteria(SearchHistoryObject historyObject) {
		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<ElementVersionChangeModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<ElementVersionChangeModel> root = criteria.from(ElementVersionChangeModel.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		Path<ElementVersionChangeModel> elementPath = root.get("elementVersion").get("element");
		Path<ElementVersionChangeModel> folderPath = elementPath.get("folder");

		predicates.add(folderPath.get("creatorRefId").isNull());

		if (!historyObject.getElementId().isEmpty()) {
			predicates.add(addInRestrictions(builder, elementPath.get("elementId"), historyObject.getElementId()));
		}
		if (!historyObject.getStatusList().isEmpty()) {
			predicates.add(addInRestrictions(builder, root.get("elementVersion").get("enumStatus").get("enumStatusId"),
					historyObject.getStatusList()));
		}

		root.fetch("elementVersion").fetch("elementVersionUsers", JoinType.LEFT);
		root.fetch("elementVersion").fetch("element").fetch("owner", JoinType.LEFT);

		if (historyObject.getNumElement() != null && !"".equals(historyObject.getNumElement().trim())) {
			predicates.add(EscapingRestrictions.ilike(builder, elementPath.get("elementNumber"),
					historyObject.getNumElement(), MatchMode.ANYWHERE));
		}
		if (historyObject.getOwner() != null && !historyObject.getOwner().isEmpty()) {
			predicates.add(builder.equal(elementPath.get("owner").get("brandId"), historyObject.getOwner()));
		}
		// ZS: PTS-Problem_37250 : Review history query when criteria 'All' Nutzer is set
		if (historyObject.getUser() != null && !historyObject.getUser().isEmpty()) {
			CriteriaQuery<ElementVersionChangeModel> usedCriteria = builder
					.createQuery(ElementVersionChangeModel.class);

			Root<ElementVersionChangeModel> usedRoot = usedCriteria.from(ElementVersionChangeModel.class);
			List<Predicate> usedPredicates = new ArrayList<Predicate>();

			usedPredicates.add(builder.equal(usedRoot.get("newValue"), historyObject.getUser()));
			usedPredicates.add(builder.equal(usedRoot.get("type"), ChangeType.USE_STAND.value()));

			usedCriteria.select(usedRoot.get("elementVersion").get("elementVersionId"))
					.where(usedPredicates.toArray(new Predicate[] {}));

			Query q = getSession().createQuery(usedCriteria);
			predicates.add(root.get("elementVersion").get("elementVersionId").in(q.getResultList()));

		}
		if (historyObject.getFromDate() != null) {
			predicates.add(builder.greaterThanOrEqualTo(root.get("date").as(Date.class),
					new BaseDateTime(historyObject.getFromDate()).getDate()));
		}
		if (historyObject.getToDate() != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(historyObject.getToDate());
			cal.add(Calendar.DATE, 1);
			predicates
					.add(builder.lessThan(root.get("date").as(Date.class), new BaseDateTime(cal.getTime()).getDate()));
		}
		if (!historyObject.getChangeTypes().isEmpty()) {
			predicates.add(root.get("changeType").in(historyObject.getChangeTypes()));
		}
		criteria.select(root).where(predicates.toArray(new Predicate[] {})).orderBy(builder.desc(root.get("changeId")));

		return getModelList(createQuery(criteria));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.integration.persistence.dao.ElementVersionChangeDAO#getLastChangeTypeByVersion(java.lang.Long)
	 */
	@Override
	public ElementVersionChangeModel getLastChangeTypeByVersion(Long versionId) {
		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<ElementVersionChangeModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<ElementVersionChangeModel> root = criteria.from(ElementVersionChangeModel.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(builder.equal(root.get("elementVersion").get("elementVersionId"), versionId));
		predicates.add(root.get("change").isNull());

		criteria.select(root).where(predicates.toArray(new Predicate[] {})).orderBy(builder.desc(root.get("date")));

		Query q = createQuery(criteria).setMaxResults(1);

		return setModelAsLoaded(getSingleResultOrNull(q));
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.ElementVersionChangeDAO#getChangesByVersionAndChangeType(java.lang.Long,
	 *      vwg.vw.km.common.type.ChangeTypeValue)
	 */
	@Override
	public List<ElementVersionChangeModel> getChangesByVersionAndChangeType(Long versionId, ChangeTypeValue type) {

		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<ElementVersionChangeModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<ElementVersionChangeModel> root = criteria.from(ElementVersionChangeModel.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(builder.equal(root.get("elementVersion").get("elementVersionId"), versionId));

		if (type != null) {
			predicates.add(builder.equal(root.get("changeType"), type.value()));

		}

		criteria.select(root).where(predicates.toArray(new Predicate[] {})).orderBy(builder.desc(root.get("date")));

		return getModelList(createQuery(criteria));
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.ElementVersionChangeDAO#getChangesByVersionsAndChangeType(java.util.List,vwg.vw.km.common.type.ChangeTypeValue)
	 */
	@Override
	public List<ElementVersionChangeModel> getChangesByVersionsAndChangeType(List<Long> versionIds,
			ChangeTypeValue type) {

		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<ElementVersionChangeModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<ElementVersionChangeModel> root = criteria.from(ElementVersionChangeModel.class);

		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(root.get("elementVersion").get("elementVersionId").in(versionIds));
		if (type != null) {
			predicates.add(builder.equal(root.get("changeType"), type.value()));

		}

		criteria.select(root).where(predicates.toArray(new Predicate[] {})).orderBy(builder.desc(root.get("date")));
		return getModelList(createQuery(criteria));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.integration.persistence.dao.ElementVersionChangeDAO#getChangesByStandsAndChange(java.util.List,
	 * java.lang.String)
	 */
	@Override
	public List<ElementVersionChangeModel> getChangesByStandsAndChange(List<Long> versionIds, String change) {

		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<ElementVersionChangeModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<ElementVersionChangeModel> root = criteria.from(ElementVersionChangeModel.class);

		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(root.get("elementVersion").get("elementVersionId").in(versionIds));
		if (change != null) {
			predicates.add(builder.equal(root.get("change"), change));
		}

		criteria.select(root).where(predicates.toArray(new Predicate[] {})).orderBy(builder.desc(root.get("date")));

		return getModelList(createQuery(criteria));
	}

	@Override
	public List<ElementVersionChangeModel> getChangeDetailByVersion(Long versionId, String change) {

		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<ElementVersionChangeModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<ElementVersionChangeModel> root = criteria.from(ElementVersionChangeModel.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(builder.equal(root.get("elementVersion").get("elementVersionId"), versionId));
		if (change != null) {
			predicates.add(builder.equal(root.get("change"), change));
		}
		criteria.select(root).where(predicates.toArray(new Predicate[] {})).orderBy(builder.desc(root.get("changeId")));

		return getModelList(createQuery(criteria));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.integration.persistence.dao.ElementVersionChangeDAO#anonomyzeUser(java.lang.Long)
	 */
	@Override
	public void anonomyzeUser(Long userId) {
		Query query = getSession().createSQLQuery(
				"UPDATE T_ELEMENTVERSIONANDERUNG SET BENUTZER = 'Anonymous' WHERE ERSTELLER_REF_ID = :userId ");
		query.setParameter("userId", userId);
		query.executeUpdate();
	}
}
